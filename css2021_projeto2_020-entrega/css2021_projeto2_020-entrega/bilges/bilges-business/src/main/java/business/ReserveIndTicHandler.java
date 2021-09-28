package business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import facade.exceptions.ApplicationException;

@Stateless
public class ReserveIndTicHandler {

//	/**
//	 * Entity manager factory for accessing the persistence service 
//	 */
//	private EntityManagerFactory emf;
	
//	/**
//	 * Creates a handler for the reservation of individual tickets use case given
//	 * the application's entity manager factory
//	 * 
//	 * @param emf The entity manager factory of the application
//	 */
//	public ReserveIndTicHandler(EntityManagerFactory emf) {
//		this.emf = emf;
//	}
	
	@EJB
	private EventCatalog ec;
	
	@EJB
	private TicketCatalog tc;
	
	@EJB
	private ReservationCatalog rc;
	

//	private Event currentEvent;
//	private EventDate currentEventDate;
	
	/**
	 * To parse Strings to LocalDate
	 */
	private DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");
	
	/**
	 * Verifica se o nome do evento existe e se este evento também suporta lugares marcados
	 * 
	 * @param eventName nome do evento
	 * @return set com as datas do evento que têm bilhetes disponiveis
	 * @throws ApplicationException
	 */
	public Set<String> getEventDates(String eventName) throws ApplicationException {
//		EntityManager em = emf.createEntityManager();
//		EventCatalog ec = new EventCatalog(em);
		
		//validar se o evento existe
		Event event = ec.getEvent(eventName);
		if(event == null) {
			throw new ApplicationException ("Event with name: " + eventName + " not found");
		}
		
		//validar se o tipo do evento e de lugares marcados 
		if(!event.getType().isSeated()) {
			throw new ApplicationException ("Cannot reserve tickets for marked seats for an event without marked seats");
		}
		
		//devolver todas as datas do evento que tenham bilhetes disponiveis
		Set<String> setEventDates = new HashSet<>();
		List<Ticket> listTickets = event.getTickets();
		for(Ticket t : listTickets) {
			if(t instanceof IndividualTicket && (((IndividualTicket) t).getDate() != null) && t.getStatus().equals(TicketStatus.ONSALE)) {
				setEventDates.add(((IndividualTicket) t).getDate().toString());
			}
		}
		
//		// guardar evento
//		this.currentEvent = event;
		
		return setEventDates;
	}

	/**
	 * devolve os lugares disponiveis de acordo com a data que escolhemos previamente
	 * 
	 * @param chosenDate a data de onde o cliente quer reservar bilhetes
	 * @return lista de strings que representa os lugares disponiveis no dia que o cliente escolheu
	 * @throws ApplicationException
	 */
	public List<String> getSeatsOfEventDate(String chosenDay, String eventName) throws ApplicationException {
//		EntityManager em = emf.createEntityManager();
//        TicketCatalog tc = new TicketCatalog(em);
		
		Event event = ec.getEvent(eventName);

        //Verifica formato de dias (01 a 31/1 a 12/0 - ...)
        if(!chosenDay.matches("((0[1-9])|([1-2][0-9])|(30|31))/((0[0-9])|(11|12))/[0-9]+")) {
            throw new ApplicationException("O formato incorreto para os dias.\n");
        }

        //Da parse de dias para LocalDate
        Date dayDate = java.sql.Date.valueOf(LocalDate.parse(chosenDay, this.formatterDay));
        EventDate correspondingED = null;
        
        for (EventDate ed : event.getDates()) {
        	if(ed.sameDay(dayDate)) {
        		correspondingED = ed;
        		break;
        	}
        }        

        List<IndividualTicket> indTickets = tc.getTicketByDayAndEvent(event, correspondingED);
        List<String> ticketsToString = new ArrayList<>();
        
        for(IndividualTicket it : indTickets) {
        	ticketsToString.add(it.getSeat().toString());
        }
        
//        this.currentEventDate = correspondingED;
                
        return ticketsToString;
	}

	/**
	 * Efetua uma reserva para os lugares escolhidos
	 * @param chosenSeats os lugares escolhidos pelo cliente para reserva
	 * @param emailAddress o email para onde a informaçao da reserva vai ser enviada
	 * @return lista de strings contendo a informaçao para pagamento : referencia, entidade e valor.
	 * @throws ApplicationException
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public List<String> reserveSeats(List<String> chosenSeats, String emailAddress, String eventName, String chosenDay) throws ApplicationException {
//		EntityManager em = emf.createEntityManager();
//		ReservationCatalog rc = new ReservationCatalog(em);
//		TicketCatalog tc = new TicketCatalog(em);
		
		Event event = ec.getEvent(eventName);
		
        Date dayDate = java.sql.Date.valueOf(LocalDate.parse(chosenDay, this.formatterDay));
        EventDate chosenDate = null;
        
        for (EventDate ed : event.getDates()) {
        	if(ed.sameDay(dayDate)) {
        		chosenDate = ed;
        		break;
        	}
        } 
		
		
		for(String s : chosenSeats) {
			if(!s.matches("[A-Z]+-[0-9]+")) {
	            throw new ApplicationException("O formato incorreto para os lugares.\n");
	        }
		}
		
		List<Seat> chosenSeatsList = new ArrayList<>();
		for(Ticket t : event.getTickets()) {
			if(t instanceof IndividualTicket) {
				
				Seat seat = ((IndividualTicket) t).getSeat();
				
				for(String st : chosenSeats) {
					String [] sp = st.split("-");
					
					if(seat.getRow().equals(sp[0]) && seat.getChairNumber() == Integer.parseInt(sp[1]) && !chosenSeatsList.contains(seat)) {
						chosenSeatsList.add(seat);
					}
				}					
			}
		}
		
		List<Ticket> ticketList = new ArrayList<>();
		for (Seat seat : chosenSeatsList) {
			for(IndividualTicket t : tc.getTicketsByEventAndDateAndSeat(event, chosenDate, seat)) {
				t.setSold();
				ticketList.add(t);
			}
		}
		
		
		if(ticketList.isEmpty()) {
            throw new ApplicationException("Nenhum dos lugares escolhidos esta disponivel.");
        }
		
		List<String> ret = new ArrayList<>();
		//reservar esses tickets com o email
		try {
//			em.getTransaction().begin();

			double totalPrice = 0;
			for(Ticket t : ticketList) {
				totalPrice += t.getPrice();
//				em.merge(t);
			}
			
			String entity = "Santander Totta";
			BankPayment bp = new BankPayment(entity);
			rc.addReservation(totalPrice, emailAddress, bp, ticketList);
			
			ret.add("Entidade: "  + entity);
			ret.add("Referencia: " + Long.toString(bp.getPaymentReference()));
			ret.add("Valor total: " + Double.toString(totalPrice));
			
//			em.getTransaction().commit();
		} catch (Exception e) {
//			if (em.getTransaction().isActive())
//				em.getTransaction().rollback();
			throw new ApplicationException("Error adding tickets to reservation", e);
		} 
//	    } finally {
//			em.close();
//		}
		
		
		return ret;
	}

}