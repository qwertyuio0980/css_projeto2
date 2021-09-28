package presentation.web.model;

import java.util.List;

import facade.exceptions.ApplicationException;
import facade.services.IReserveIndTicServiceRemote;

public class BuyIndTicModel extends Model {

	private String eventName;
	private Iterable<String> eventDates;
	private String chosenDate;
	private String email;
	private List<String> seatChoices = null;
	private List<String> chosenSeats;
	private List<String> reservation;
	
	private IReserveIndTicServiceRemote indTicService;

	public void setIndTicService(IReserveIndTicServiceRemote indTicService) {
		this.indTicService = indTicService;		
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;	
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventDates(Iterable<String> dates) {
		this.eventDates = dates;
	}

	public Iterable<String> getEventDates() {
		return this.eventDates;
	}
	
	public void setChosenDate(String date) {
		this.chosenDate = date;
	}
	
	public String getChosenDate() {
		return this.chosenDate;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setSeatChoices(List<String> seatChoices) {
		this.seatChoices = seatChoices;		
	}
	
	public Iterable<String> getSeatChoices() {
		try {
			return this.indTicService.getSeatsOfEventDate(this.chosenDate, this.eventName);
			
		} catch (ApplicationException e) {
			return null;
		}
	}

	public void setChosenSeats(List<String> chosenSeats) {
		this.chosenSeats = chosenSeats;		
	}
	
	public Iterable<String> getChosenSeats() {
		return this.chosenSeats;
	}

	public void setReservation(List<String> reservation) {
		this.reservation = reservation;
	}

	public List<String> getReservation() {
		return this.reservation;
	}
}
