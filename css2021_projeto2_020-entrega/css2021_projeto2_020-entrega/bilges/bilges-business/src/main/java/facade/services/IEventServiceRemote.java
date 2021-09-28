package facade.services;

import java.util.List;

import javax.ejb.Remote;

import facade.exceptions.ApplicationException;

@Remote
public interface IEventServiceRemote {

	public void giveEventDetails(String name, List<String> days, List<String> startAndEndHours, String type, int companyNr) throws ApplicationException;
	
	public List<String> createNewEvent();
}
