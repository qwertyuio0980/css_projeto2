package facade.services;

import java.util.List;

import javax.ejb.Remote;

import facade.exceptions.ApplicationException;

@Remote
public interface IReservePassTicServiceRemote {
	
    public int indicatePassTickets(String eventName) throws ApplicationException;
	
    public List<String> reservePassTickets(int passTicketsCount, String emailAddress, String eventName, int currentNumberOfPassTickets) throws ApplicationException;
	
}
