package controller.web.inputController.actions;

import java.util.List;
import java.io.IOException;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.exceptions.ApplicationException;
import facade.services.IReservePassTicServiceRemote;
import presentation.web.model.BuyIndTicModel;
import presentation.web.model.BuyPassTicketModel;

@Stateless
public class BuyPassTicketPurchaseTicketAction extends Action {

	@EJB private IReservePassTicServiceRemote passTicService;
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		BuyPassTicketModel model = createHelper(request);
		request.setAttribute("model", model);
		
		if (validInput(model)) {

			try {
				
				List<String> entRefPri = passTicService.reservePassTickets(Integer.parseInt(model.getNumTickets()), model.getEmailAddress(),
						model.getEventName(), Integer.parseInt(model.getNumTickets()));

				model.setEntity(entRefPri.get(0));
				model.setReference(entRefPri.get(1));
				model.setTotalPrice(entRefPri.get(2));
				
				request.getRequestDispatcher("/buyPassTic/buyPassTicketShowReservation.jsp").forward(request, response);
				
				return;

			} catch (ApplicationException e) {				
				model.addMessage(e.getMessage());
			}
		}
		
		request.getRequestDispatcher("/buyPassTic/buyPassTicketPurchaseTicket.jsp").forward(request, response);
	}
	
	private BuyPassTicketModel createHelper(HttpServletRequest request) {
		// Create the object model
		BuyPassTicketModel model = new BuyPassTicketModel();
		model.setPassTicService(passTicService);

		// fill it with data from the request
		model.setEventName(request.getParameter("eventName"));
		model.setNumTickets(request.getParameter("numTickets"));
		model.setEmailAddress(request.getParameter("emailAddress"));
		
		return model;
	}
	
	private boolean validInput(BuyPassTicketModel model) {
		
		// check if numTickets is filled
		boolean result = isFilled(model, model.getNumTickets(), "Tem de ser escolhido um número de bilhetes.");

		// check if emailAddress is filled
		result &= isFilled(model, model.getEmailAddress(), "Tem de ser fornecido um endereço de email.");

		return result;
	}
}
