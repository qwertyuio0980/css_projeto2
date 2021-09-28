package business;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name=IndividualTicket.FIND_AVAILABLE_BY_EVENT_AND_DAY, query="SELECT it FROM IndividualTicket it, Ticket t WHERE it.event=:"
    		+ "" + IndividualTicket.EVENT + " AND it.date=:" + IndividualTicket.DATE_DAY + " AND t.status=:" + IndividualTicket.TICKET_STATUS + ""
    				+ " AND t.id=it.id ORDER BY it.seat.row, it.seat.chairNumber"),
	
	@NamedQuery(name=IndividualTicket.FIND_AVAILABLE_BY_EVENT_AND_DAY_AND_SEAT, query="SELECT it FROM IndividualTicket it, Ticket t WHERE it.event=:"
    		+ "" + IndividualTicket.EVENT + " AND it.date=:" + IndividualTicket.DATE_DAY + " AND t.status=:" + IndividualTicket.TICKET_STATUS 
    				+ " AND it.seat=:" + IndividualTicket.SEAT + " AND t.id=it.id ORDER BY it.seat.row, it.seat.chairNumber"),
	
	@NamedQuery(name=IndividualTicket.COUNT_AVAILABLE_BY_EVENT_AND_DAY, query="SELECT COUNT(it) FROM IndividualTicket it, Ticket t WHERE it.event=:"
    		+ "" + IndividualTicket.EVENT + " AND it.date=:" + IndividualTicket.DATE_DAY + " AND t.status=:" + IndividualTicket.TICKET_STATUS + ""
    				+ " AND t.id=it.id ORDER BY it.seat.row, it.seat.chairNumber")
})
public class IndividualTicket extends Ticket {
		
	
	public static final String FIND_AVAILABLE_BY_EVENT_AND_DAY = "findAvailableByEventAndDay";
	public static final String EVENT = "event";
	public static final String DATE_DAY = "day";
	public static final String INDIVIDUAL_TYPE = "individualticket";
	public static final String TICKET_STATUS = "status";
	public static final String SEAT = "seat";
	public static final String FIND_AVAILABLE_BY_EVENT_AND_DAY_AND_SEAT = "findAvailableByEventAndDayAndSeat";
	public static final String COUNT_AVAILABLE_BY_EVENT_AND_DAY = "countAvailableByEventAndDay";

	@ManyToOne
	private Seat seat;	
	
	@Embedded
	private EventDate date;
	
	//Empty Constructor for JPA Persistence
	IndividualTicket(){}
	
	/**
	 * Construtor para bilhete individual
	 * @param price preco do bilhete
	 * @param seat lugar do bilhete
	 * @param date data
	 * @param event evento
	 */
	public IndividualTicket(double price, Seat seat, EventDate date, Event event) {
		super(price, event);
		this.status = TicketStatus.ONSALE;
		this.seat = seat;
		this.date = date;
	}

	/**
	 * Busca data deste bilhete
	 * @return data
	 */
	public EventDate getDate() {
		return this.date;		
	}

	/**
	 * Busca lugar deste bilhete
	 * @return lugar 
	 */
	public Seat getSeat() {
		return this.seat;		
	}
	
}