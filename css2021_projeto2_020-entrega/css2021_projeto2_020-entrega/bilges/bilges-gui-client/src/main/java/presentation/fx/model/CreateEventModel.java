package presentation.fx.model;

import java.time.LocalDate;

import facade.exceptions.ApplicationException;
import facade.services.IEventServiceRemote;
import facade.services.IVenueServiceRemote;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CreateEventModel {
	
	//CreateEvent
	private final StringProperty nome;
	private final IntegerProperty companyNr;
	
	private final StringProperty horaInicio;
	private final StringProperty horaFim;
	private final ObjectProperty<LocalDate> data;
	
	private final ObjectProperty<EventType> selectedEventType;
	private final ObservableList<EventType> eventTypes;
	
	
	//AtributeVenue
	private final ObjectProperty<String> selectedVenue;
	private final ObservableList<String> availableVenues;
	private final StringProperty nomeEventInstalacao;
	private final StringProperty precoBilheteInd;
	private final StringProperty precoBilhetePass;
	private final ObjectProperty<LocalDate> dataBilhete;

	public CreateEventModel(IEventServiceRemote eventService, IVenueServiceRemote venueService) {
		//CreateEvent
		nome = new SimpleStringProperty();
		companyNr = new SimpleIntegerProperty();
		horaInicio = new SimpleStringProperty();
		horaFim = new SimpleStringProperty();
		data = new SimpleObjectProperty<>(this, "date");	
		
		this.eventTypes = FXCollections.observableArrayList();
		eventService.createNewEvent().forEach(d -> eventTypes.add(new EventType(d)));
		selectedEventType = new SimpleObjectProperty<>(null);
		

		//AtributeVenue
		nomeEventInstalacao = new SimpleStringProperty();
		precoBilheteInd = new SimpleStringProperty();
		precoBilhetePass = new SimpleStringProperty();
		dataBilhete = new SimpleObjectProperty<>(this, "dateTicket");
		
		this.availableVenues = FXCollections.observableArrayList();
		try {
			venueService.chooseVenue().forEach(d -> availableVenues.add(d));
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		selectedVenue = new SimpleObjectProperty<>(null);
	}

	public ObservableList<EventType> getEventTypes() {
		return eventTypes;
	}
	
	public final EventType getSelectedEventType() {
		return selectedEventType.get();
	}
	
	public final void setSelectedEventType(EventType e) {
		selectedEventType.set(e);
	}
	
	public StringProperty horaInicioProperty() {
		return horaInicio;
	}

	public StringProperty horaFimProperty() {
		return horaFim;
	}
	
    public final LocalDate getDate() {
        return data.get();
    }

    public final ObjectProperty<LocalDate> clientDateProperty() {
        return data;
    }

	public StringProperty nomeProperty() {
		return nome;
	}
	
	public IntegerProperty companyNrProperty() {
		return companyNr;
	}

	public void clearProperties() {
		nome.set("");
		companyNr.set(0);
		
		horaInicio.set("");
		horaFim.set("");
		data.set(null);
	}


	public ObservableList<String> getAvailableVenues() {
		return availableVenues;
	}

	public String getSelectedVenue() {
		return selectedVenue.getValue();
	}
	
	public final void setSelectedVenue(String s) {
		selectedVenue.set(s);
	}

	public StringProperty nomeEventInstalacaoProperty() {
		return nomeEventInstalacao;
	}

	public StringProperty precoBilheteIndProperty() {
		return precoBilheteInd;
	}

	public StringProperty precoBilhetePassProperty() {
		return precoBilhetePass;
	}

    public final LocalDate getTicketDate() {
        return dataBilhete.get();
    }
    
    public final ObjectProperty<LocalDate> ticketDateProperty() {
        return dataBilhete;
    }

	public void clearPropertiesVenue() {
//		selectedVenue.set(null);
		nomeEventInstalacao.set(null);
		precoBilheteInd.set(null);
		precoBilhetePass.set(null);
		dataBilhete.set(null);
	}
}
