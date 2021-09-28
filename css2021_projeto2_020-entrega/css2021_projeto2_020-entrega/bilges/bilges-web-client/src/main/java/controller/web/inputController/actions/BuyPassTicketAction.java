package controller.web.inputController.actions;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.services.IReservePassTicServiceRemote;
import presentation.web.model.BuyPassTicketModel;

@Stateless
public class BuyPassTicketAction extends Action {

	@EJB private IReservePassTicServiceRemote passTicService;
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		BuyPassTicketModel model = new BuyPassTicketModel();
		request.setAttribute("model", model);
		
		model.setPassTicService(passTicService);

		request.getRequestDispatcher("/buyPassTic/buyPassTicket.jsp").forward(request, response);
	}
	
}
