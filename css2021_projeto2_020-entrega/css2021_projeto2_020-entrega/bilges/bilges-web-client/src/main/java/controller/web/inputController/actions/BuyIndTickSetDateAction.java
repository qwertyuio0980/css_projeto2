package controller.web.inputController.actions;

import java.io.IOException;
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
public class BuyIndTickSetDateAction extends Action {

	@EJB private IReserveIndTicServiceRemote indTicService;

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BuyIndTicModel model = createHelper(request);
		request.setAttribute("model", model);

		if (validInput(model)) {

			try {

				List<String> seatChoices = indTicService.getSeatsOfEventDate(model.getChosenDate(), model.getEventName());

				model.setSeatChoices(seatChoices);

				request.getRequestDispatcher("/buyIndTic/buyTicketSeatChooseSeat.jsp").forward(request, response);

				return;
				
			} catch (ApplicationException e) {
				model.addMessage(e.getMessage());
			}
		}

		request.getRequestDispatcher("/buyIndTic/buyTicketSeatChoiceDates.jsp").forward(request, response);
	}

	private BuyIndTicModel createHelper(HttpServletRequest request) {
		// Create the object model
		BuyIndTicModel model = new BuyIndTicModel();
		model.setIndTicService(indTicService);

		// fill it with data from the request
		model.setEventName(request.getParameter("eventName"));
		
		String date = request.getParameter("chosenDate").split(" ")[1].replaceAll("-", "/");
		String [] dateChecker = date.split("/");
		
		if(dateChecker[0].length() == 1) {
			dateChecker[0] = "0" + dateChecker[0];
		}
		if(dateChecker[1].length() == 1) {
			dateChecker[1] = "0" + dateChecker[1];
		}
		date = dateChecker[0] + "/" + dateChecker[1] + "/" + dateChecker[2];
		model.setChosenDate(date);

		return model;
	}

	private boolean validInput(BuyIndTicModel model) {

		// check if eventName is filled		
		boolean ret = isFilled(model, model.getEventName(), "O nome do Evento tem de estar preenchido.");

		//Verificar que escolheu data e que e valida
		ret &= isFilled(model, model.getChosenDate(), "Tem que escolher uma data do evento.");
		
		return ret;
	}
}
