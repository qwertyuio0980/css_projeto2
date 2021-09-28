package controller.web.inputController.actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.exceptions.ApplicationException;
import facade.services.IReserveIndTicServiceRemote;
import presentation.web.model.BuyIndTicModel;

@Stateless
public class BuyIndTickSetEventNameAction extends Action {

	@EJB private IReserveIndTicServiceRemote indTicService;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BuyIndTicModel model = createHelper(request);
		request.setAttribute("model", model);

		if (validInput(model)) {

			try {
				Set<String> dates = indTicService.getEventDates(model.getEventName());
				
				model.setEventDates(dates);				
				
				request.getRequestDispatcher("/buyIndTic/buyTicketSeatChoiceDates.jsp").forward(request, response);
				
				return;

			} catch (ApplicationException e) {				
				model.addMessage(e.getMessage());
			}
		}
		
		request.getRequestDispatcher("/buyIndTic/buyTicketSeatChoice.jsp").forward(request, response);
	}


	private BuyIndTicModel createHelper(HttpServletRequest request) {
		// Create the object model
		BuyIndTicModel model = new BuyIndTicModel();
		model.setIndTicService(indTicService);

		// fill it with data from the request
		model.setEventName(request.getParameter("eventName"));

		return model;
	}

	private boolean validInput(BuyIndTicModel model) {

		// check if eventName is filled		
		return isFilled(model, model.getEventName(), "O nome do Evento tem de estar preenchido.");
	}
}
