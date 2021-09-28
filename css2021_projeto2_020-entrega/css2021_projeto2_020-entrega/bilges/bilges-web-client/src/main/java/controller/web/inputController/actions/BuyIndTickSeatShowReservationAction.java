package controller.web.inputController.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.exceptions.ApplicationException;
import facade.services.IReserveIndTicServiceRemote;
import presentation.web.model.BuyIndTicModel;

@Stateless
public class BuyIndTickSeatShowReservationAction extends Action {

	@EJB private IReserveIndTicServiceRemote indTicService;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BuyIndTicModel model = createHelper(request);
		request.setAttribute("model", model);

		if (validInput(model)) {
			try {

				List<String> chosenSeats = new ArrayList<>((List<String>) model.getChosenSeats());
				String email = model.getEmail();
				
				model.setReservation(this.indTicService.reserveSeats(chosenSeats, email, model.getEventName(), model.getChosenDate()));

				request.getRequestDispatcher("/buyIndTic/buyTicketSeatShowReservation.jsp").forward(request, response);

				return;

			} catch (ApplicationException e) {
				model.addMessage(e.getMessage());
			}
		}

		
		request.getRequestDispatcher("/buyIndTic/buyTicketSeatChooseSeat.jsp").forward(request, response);
	}

	private BuyIndTicModel createHelper(HttpServletRequest request) {
		// Create the object model
		BuyIndTicModel model = new BuyIndTicModel();
		model.setIndTicService(indTicService);

		// fill it with data from the request
		model.setEventName(request.getParameter("eventName"));

		model.setChosenDate(request.getParameter("chosenDate"));
		model.setEmail(request.getParameter("email"));
	
		String [] chosenSeatsVec = request.getParameterValues("chosenTickets");
		if(chosenSeatsVec != null) {
			model.setChosenSeats(Arrays.asList(chosenSeatsVec));	
		}
		
		return model;
	}

	private boolean validInput(BuyIndTicModel model) {

		// check if eventName is filled		
		boolean ret = isFilled(model, model.getEventName(), "O nome do Evento tem de estar preenchido.");

		//Verificar que escolheu data e que e valida
		ret &= isFilled(model, model.getChosenDate(), "Tem que escolher uma data do evento.");

		ret &= isFilled(model, model.getEmail(), "O campo do seu email tem que estar preenchido.");


		Object seats = model.getChosenSeats();
		boolean emptySeats = seats == null || ((List<String>) seats).isEmpty();
		ret &= !emptySeats;
		
		if(emptySeats) {
			model.addMessage("Escolha pelo menos um lugar");			
		}
		
		return ret;
	}

}
