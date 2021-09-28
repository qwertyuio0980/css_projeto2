package controller.web.inputController.actions;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.exceptions.ApplicationException;
import facade.services.IReservePassTicServiceRemote;
import presentation.web.model.BuyPassTicketModel;

@Stateless
public class BuyPassTicketInsertEventNameAction extends Action {

	@EJB private IReservePassTicServiceRemote passTicService;
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		BuyPassTicketModel model = createHelper(request);
		request.setAttribute("model", model);
		
		if (validInput(model)) {
			
			try {
				String nrMaxPassTickets = String.valueOf(passTicService.indicatePassTickets(model.getEventName()));
				
				model.setNumMaxTickets(nrMaxPassTickets);

				//caso mudar para pagina seguinte
				request.getRequestDispatcher("/buyPassTic/buyPassTicketPurchaseTicket.jsp").forward(request, response);
				
				return;
			} catch (ApplicationException e) {
				model.addMessage(e.getMessage());
			}
		}

		//caso manter na mesma pagina
		request.getRequestDispatcher("/buyPassTic/buyPassTicket.jsp").forward(request, response);
	}
	
	private BuyPassTicketModel createHelper(HttpServletRequest request) {
		// Create the object model
		BuyPassTicketModel model = new BuyPassTicketModel();
		model.setPassTicService(passTicService);

		// fill it with data from the request
		model.setEventName(request.getParameter("eventName"));
		
		
		return model;
	}
	
	private boolean validInput(BuyPassTicketModel model) {
		
		// check if eventName is filled
		return isFilled(model, model.getEventName(), "O nome do evento tem de estar preenchido.");
	}
}
