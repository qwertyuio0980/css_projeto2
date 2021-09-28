package presentation.fx.inputcontroller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import facade.exceptions.ApplicationException;
import facade.services.IEventServiceRemote;
import facade.services.IVenueServiceRemote;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import presentation.fx.model.CreateEventModel;
import presentation.fx.model.EventType;

public class CreateEventController extends BaseController {

	//CREATE EVENT---------------------------------------

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<EventType> tiposEvento;

	@FXML
	private TextField nomeEvento;

	@FXML
	private TextField nrEmpresa;

	@FXML
	private DatePicker datas;

	@FXML
	private TextField horaInicio;

	@FXML
	private TextField horaFim;

	@FXML
	private Button adicionarDataButton;

	@FXML
	private Button criarEventoButton;

	@FXML
	private ListView<String> showDates;

    @FXML
    private Button apagarDatas;

	@FXML
	void addDate(ActionEvent event) {
		if (horaInicio.getText() == null || horaFim.getText() == null || datas.getValue() == null) {
			showError("Tens que preencher todos os campos relativos a data.");
			return;
		}

		String formatoHoras = "(((([0-1][0-9])|(2[0-3])):([0-5][0-9]))|24:00)";
		if(!horaInicio.getText().matches(formatoHoras) || !horaFim.getText().matches(formatoHoras)) {
			showError("O formato incorreto para as horas.\n");
			return;
		}

		String date = datas.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		showDates.getItems().add(horaInicio.getText() + " " + horaFim.getText() + " " + date);

		datas.setValue(datas.getValue().plusDays(1));
	}

	@FXML
	void createEvent(ActionEvent event) {

		if (nomeEvento.getText() == null) {
			showError("Preencha o nome do evento.");
			return;
		}

		if (nrEmpresa.getText().equals("0")) {
			showError("Preencha o numero da empresa.");
			return;
		}

		if(model.getSelectedEventType() == null) {
			showError("Selecione um tipo de evento.");
			return;
		}

		List<String> days = new ArrayList<>();
		List<String> startAndEndHours = new ArrayList<>();

		for (String s : showDates.getItems()) {
			String [] eventDay = s.split(" ");
			startAndEndHours.add(eventDay[0]);
			startAndEndHours.add(eventDay[1]);
			days.add(eventDay[2]);
		}

		if (days.isEmpty()) {
			showError("Adicione pelo menos um dia");
			return;
		}


		try {
			eventService.giveEventDetails(nomeEvento.getText(), 
					days, startAndEndHours, model.getSelectedEventType().getName(), Integer.parseInt(nrEmpresa.getText())); 
			model.clearProperties();
			showDates.getItems().clear();

			showInfo("Sucesso");

		} catch (ApplicationException e) {
			showError(e.getMessage());
		} 
	}

	@FXML
	void eventTypeSelected(ActionEvent event) {
		model.setSelectedEventType(tiposEvento.getValue());
	}
	
    @FXML
    void resetDatas(ActionEvent event) {
    	showDates.getItems().clear();
    }
	//---------------------------------------------------



	//ATRIBUTE VENUE-------------------------------------
	@FXML
	private ComboBox<String> listaInstalacoes;

	@FXML
	private TextField nomeEventoInstalacao;

	@FXML
	private TextField precoBilheteInd;

	@FXML
	private TextField precoBilhetePass;

	@FXML
	private DatePicker dataBilhete;

	@FXML
	private Button criarEventoButton1;


	private CreateEventModel model;

	private IEventServiceRemote eventService;

	private IVenueServiceRemote venueService;

	@FXML
	void atributeVenue(ActionEvent event) {

		//Checks
		if(model.getSelectedVenue() == null) {
			showError("Escolha uma instalacao");
			return;
		}

		if(nomeEventoInstalacao.getText() == null) {
			showError("Indique o nome de um evento");
			return;
		}

		if(precoBilheteInd.getText() == null) {
			showError("Indique o preco de um bilhete individual");
			return;
		}
		
		double precoInd;
		try {
			precoInd = Double.parseDouble(precoBilheteInd.getText());		
		} catch (Exception e) {
			showError("O preco do bilhete individual tem que ser um numero.");
			return;
		}


		if(dataBilhete.getValue() == null) {
			showError("Indique uma data para comeco de venda dos bilhetes");
			return;
		}
		String date = dataBilhete.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		double precoPass = 0;
		if(precoBilheteInd.getText() != null) {
			try {
				precoPass = Double.parseDouble(precoBilhetePass.getText());				
			} catch (Exception e) {
				showError("O preco do bilhete passe tem que ser um numero.");
				return;
			}
		}


		try {
			venueService.atributeVenue(nomeEventoInstalacao.getText(), listaInstalacoes.getValue(), date, precoInd);
			
			if(precoPass > 0) {
				venueService.setPassTicketPrice(precoPass, nomeEventoInstalacao.getText());
			}
			model.clearPropertiesVenue();


			showInfo("Sucesso");

		} catch (ApplicationException e) {
			showError(e.getMessage());
		} 
	}

	@FXML
	void venueSelected(ActionEvent event) {
		model.setSelectedVenue(listaInstalacoes.getValue());
	}
	//---------------------------------------------------


	public void setModel(CreateEventModel model) {
		//Create Event---------------------
		this.model = model;
		tiposEvento.setItems(model.getEventTypes());   
		tiposEvento.setValue(model.getSelectedEventType());

		horaInicio.textProperty().bindBidirectional(model.horaInicioProperty());
		horaFim.textProperty().bindBidirectional(model.horaFimProperty());
		datas.valueProperty().bindBidirectional(model.clientDateProperty());

		nomeEvento.textProperty().bindBidirectional(model.nomeProperty());
		nrEmpresa.textProperty().bindBidirectional(model.companyNrProperty(), new NumberStringConverter());

		//Atribute Venue--------------------

		listaInstalacoes.setItems(model.getAvailableVenues());   
		listaInstalacoes.setValue(model.getSelectedVenue());

		nomeEventoInstalacao.textProperty().bindBidirectional(model.nomeEventInstalacaoProperty());
		precoBilheteInd.textProperty().bindBidirectional(model.precoBilheteIndProperty());
		precoBilhetePass.textProperty().bindBidirectional(model.precoBilhetePassProperty());
		dataBilhete.valueProperty().bindBidirectional(model.ticketDateProperty());
	}

	@FXML
	void initialize() {
		assert tiposEvento != null : "fx:id=\"tiposEvento\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert nomeEvento != null : "fx:id=\"nomeEvento\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert nrEmpresa != null : "fx:id=\"nrEmpresa\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert datas != null : "fx:id=\"datas\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert horaInicio != null : "fx:id=\"horaInicio\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert horaFim != null : "fx:id=\"horaFim\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert adicionarDataButton != null : "fx:id=\"adicionarDataButton\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert criarEventoButton != null : "fx:id=\"criarEventoButton\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert showDates != null : "fx:id=\"showDates\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert listaInstalacoes != null : "fx:id=\"listaInstalacoes\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert nomeEventoInstalacao != null : "fx:id=\"nomeEventoInstalacao\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert precoBilheteInd != null : "fx:id=\"precoBilheteInd\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert precoBilhetePass != null : "fx:id=\"precoBilhetePass\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert dataBilhete != null : "fx:id=\"dataBilhete\" was not injected: check your FXML file 'bilgesStage.fxml'.";
		assert criarEventoButton1 != null : "fx:id=\"criarEventoButton1\" was not injected: check your FXML file 'bilgesStage.fxml'.";

	}

	public void setEventService(IEventServiceRemote eventService) {
		this.eventService = eventService;
	}

	public void setVenueService(IVenueServiceRemote venueService) {
		this.venueService = venueService;
	}
}
