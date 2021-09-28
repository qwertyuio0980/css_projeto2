package business;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * An event
 * @author css020
 *
 */

@Entity
@NamedQueries({
	@NamedQuery(name=Event.FIND_BY_NAME, query="SELECT e FROM Event e WHERE e.name= :" + 
			Event.EVENT_NAME),
	@NamedQuery(name=Event.FIND_BY_VENUE_ID, query="SELECT e FROM Event e WHERE e.venue= :" + 
			Event.VENUE),
	@NamedQuery(name=Event.EVENT_EXISTS, query="SELECT COUNT(e) FROM Event e WHERE e.name= :" + 
			Event.EVENT_NAME)
})
public class Event {
	
	public static final String FIND_BY_NAME = "Event.findByName";
	public static final String EVENT_NAME = "name";
	public static final String FIND_BY_VENUE_ID = "Event.findByVenueId";
	public static final String VENUE = "venue";
	public static final String EVENT_EXISTS = "Event.eventExists";

	//Id for JPA Persistence
	@Id @GeneratedValue private int id;
	
	/**
	 * Nome do Evento
	 */
	@Column(nullable = false) private String name;
	
	@Column(nullable = true) private double passTicketPrice;
	
	@Temporal(TemporalType.DATE) @Column(nullable = true)
	private Date startSellingDate;
	
	/**
	 * Todas os dias do evento
	 */
	@ElementCollection
	private List<EventDate> dates;
	
	@ManyToOne
	private Company company;
	
	@Enumerated(EnumType.STRING) private EventType type;
	
	@OneToMany (mappedBy="event") //@JoinColumn
	private List<Ticket> tickets;
	
	@ManyToOne
	private Venue venue;

	
	
	
	//Empty Constructor for JPA Persistence
	Event(){}
	
	/**
	 * Constroi novo evento
	 * @param name nome do evento
	 * @param type tipo do evento
	 * @param dates Datas(dias e horas de inicio e fim) em que o evento vai dar
	 * @param company
	 */
	public Event(String name, EventType type, List<EventDate> dates, Company company) {
		this.name = name;
		this.type = type;
		this.dates = dates;
		this.company = company;
	}
	
	/**
	 * Atribui instalacao a este evento
	 * @param venue instalacao
	 */
	public void atributeVenue(Venue venue) {
		this.venue = venue;
	}
	
	/**
	 * atribui data de comeco de venda de bilhetes
	 * @param d data
	 */
	public void setTicketSellDate(Date d) {
		this.startSellingDate = d; 
	}
	
	/**
	 * Diz se evento tem instalacao atribuida ou nao
	 * @return true caso tenha instalacao atribuida, false caso nao
	 */
	public boolean hasVenue() {
		return this.venue != null;
	}

	/**
	 * Verifica se existe data de evento antes de dada data
	 * @param dateD data a verificar
	 * @return
	 */
	public boolean existsEventDateBefore(Date dateD) {
		for(EventDate ed : this.dates) {
			if(ed.isBefore(dateD)) {
				return true;
			}
		} 
		
		return false;
	}

	/**
	 * retorna tipo de evento
	 * @return tipo de evento
	 */
	public EventType getType() {
		return this.type;
	}
	
	/**
	 * verifica se dias de outro evento coincidem com este
	 * @param e outro evento
	 * @return true caso coincidam, false caso nao
	 */
	public boolean daysCoincide(Event e) {
		for (EventDate dThis : this.dates) {
			for (EventDate d : e.dates) {
				if(dThis.coincides(d)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * retorna bilhetes deste evento
	 * @return bilhetes deste evento
	 */
	public List<Ticket> getTickets() {
		return this.tickets;
	}
	
	/**
	 * retorna bilhetes deste evento
	 * @return datas deste evento
	 */
	public List<EventDate> getDates() {
		return this.dates;
	}

	/**
	 * Atribui preco bilhete passe
	 * @param price preco
	 */
	public void setPassTicketPrice(double price) {
		this.passTicketPrice = price;		
	}
	/**
	 * getter do preco de bilhete passe
	 * @return passTicketPrice preco do bilhetePasse
	 */
	public double getPassTicketPrice() {
		return this.passTicketPrice;
	}

	/**
	 * 
	 * @param newTickets
	 */
	public void setTickets(List<Ticket> newTickets) {
		this.tickets = newTickets;		
	}

	/**
	 * getter do id
	 * @return id
	 */	
	public int getId() {
		return this.id;
	}
}