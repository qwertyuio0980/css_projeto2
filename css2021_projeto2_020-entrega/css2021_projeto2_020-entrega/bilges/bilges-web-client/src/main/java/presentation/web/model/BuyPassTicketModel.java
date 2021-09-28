package presentation.web.model;

import facade.services.IReservePassTicServiceRemote;

public class BuyPassTicketModel extends Model {

	private String eventName;
	private String numTickets;
	private String numMaxTickets;
	private String emailAddress;
	private String entity;
	private String reference;
	private String totalPrice;
	
	private IReservePassTicServiceRemote passTicService;
	
	public void setPassTicService(IReservePassTicServiceRemote passTicService) {
		this.passTicService = passTicService;
	}
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getEventName() {
		return this.eventName;
	}
	
	public void setNumTickets(String numTickets) {
		this.numTickets = numTickets;
	}
	
	public String getNumTickets() {
		return this.numTickets;
	}
	
	public void setNumMaxTickets(String numMaxTickets) {
		this.numMaxTickets = numMaxTickets;
	}
	
	public String getNumMaxTickets() {
		return this.numMaxTickets;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public String getEntity() {
		return this.entity;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getReference() {
		return this.reference;
	}
	
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public String getTotalPrice() {
		return this.totalPrice;
	}
}
