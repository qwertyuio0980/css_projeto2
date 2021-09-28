package controller.web.inputController.actions;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import facade.services.IReserveIndTicServiceRemote;
import presentation.web.model.BuyIndTicModel;

@Stateless
public class BuyTicketSeatChoiceAction extends Action {

	@EJB private IReserveIndTicServiceRemote indTicService;
	
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		BuyIndTicModel model = new BuyIndTicModel();
		request.setAttribute("model", model);		
		
		model.setIndTicService(indTicService);

		request.getRequestDispatcher("/buyIndTic/buyTicketSeatChoice.jsp").forward(request, response);
	}
	
}
