package business;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class PassTicket extends Ticket {
	
	@OneToMany
	List<IndividualTicket> individualTickets;
	
	//Empty Constructor for JPA Persistence
	PassTicket(){}
	
	/**
	 * Constroi um passTicket 
	 * @param price preco do passTicket
	 * @param event evento para o qual queremos criar passTicket
	 * @param individualTickets Lista de bilhetes individuais que queremos transformar em passTickets
	 */
	public PassTicket(double price, Event event, List<IndividualTicket> individualTickets) {
		super(price, event);
		this.status = TicketStatus.SOLD;
		this.individualTickets = individualTickets;
	}
}