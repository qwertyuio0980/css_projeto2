package facade.services;

import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import facade.exceptions.ApplicationException;

@Remote
public interface IReserveIndTicServiceRemote {

	public Set<String> getEventDates(String eventName) throws ApplicationException;

	public List<String> getSeatsOfEventDate(String chosenDay, String eventName) throws ApplicationException;
		
	public List<String> reserveSeats(List<String> chosenSeats, String emailAddress, String eventName, String chosenDay) throws ApplicationException;
	
}
