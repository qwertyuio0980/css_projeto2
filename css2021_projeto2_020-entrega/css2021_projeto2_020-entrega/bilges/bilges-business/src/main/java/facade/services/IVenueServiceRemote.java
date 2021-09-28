package facade.services;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;
import facade.exceptions.ApplicationException;

@Remote
public interface IVenueServiceRemote {

	public List<String> chooseVenue() throws ApplicationException;

	public void atributeVenue(String eventName, String venueName, String date, double preco) throws ApplicationException;
	
	public void setPassTicketPrice(double price, String eventName) throws ApplicationException;
	
}
