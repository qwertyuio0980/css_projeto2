package business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

import facade.exceptions.ApplicationException;

@Stateless
public class ReservePassTicketHandler {


//	private EntityManagerFactory emf;


//	public ReservePassTicketHandler(EntityManagerFactory emf) {
//		this.emf = emf;
//	}
	//	private Reservation currentReservation;
	
	@EJB
	private EventCatalog ec;
	
	@EJB
	private TicketCatalog tc;
	
	@EJB
	private ReservationCatalog rc;
	
//	private Event currentEvent;
//	private int currentNumberOfPassTickets;		

	/**
	 * Metodo auxiliar que retorna o minimo de um hashmap 
	 * Taken from https://beginnersbook.com/2014/07/java-finding-minimum-and-maximum-values-in-an-array/
	 * @param map
	 * @return min
	 */
	private static int getMin(HashMap<EventDate, Long> map){
		long min = -1;
		for (EventDate e : map.keySet()) {
			if(min != -1) {
				long num = map.get(e);
				if (num < min) {
					min = num;
				}
				
			} else {
				min = map.get(e);
			}
		}

		return (int) min;
	} 

	/**
	 * Metodo que verifica se e possivel comprar bilhetesPasse e devolve quantos bilhetes 
	 * passe estão disponiveis para comprar
	 * @param eventName o nome do evento para qual queremos comprar bilhetesPasse
	 * @return o numero de bilhetesPasse disponiveis para comprar
	 * @throws ApplicationException
	 */
	public int getPassTicketValidated(String eventName) throws ApplicationException {
//		EntityManager em = emf.createEntityManager();
//		EventCatalog ec = new EventCatalog(em);
//		TicketCatalog tc = new TicketCatalog(em);

		Event event = ec.getEvent(eventName);
		
		if(event == null) {
			throw new ApplicationException("Evento nao existe.\n");
		}

		if (event.getPassTicketPrice() <= 0) {
			throw new ApplicationException("Nao existem bilhetes passe disponiveis para este evento.\n");
		}

		List<EventDate> dateList = event.getDates();
		int numberOfDays = dateList.size();
		HashMap<EventDate, Long> numberAvailableTicketsByEvent = new HashMap<>();


		for (EventDate ed : dateList) {
			long num = tc.numberOfTicketsByDateAndEvent(event, ed);
			numberAvailableTicketsByEvent.put(ed, num);
		}

		int nrPassTicket = getMin(numberAvailableTicketsByEvent);
//		this.currentEvent = event;
//		this.currentNumberOfPassTickets = nrPassTicket;

		return nrPassTicket;
	}

	/**
	 * metodo que trata de "reservar" os bilhetesPasse e coloca-los como vendidos, ou seja não disponiveis
	 * 
	 * @param passTicketsCount o numero de bilhetesPasse que o cliente quer comprar
	 * @param emailAddress o email para o qual vai ser enviada a informaçao acerca da reserva dos bilhetes
	 * @return uma lista com os dados de pagamento : referencia, entidade e valor.
	 * @throws ApplicationException
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public List<String> buyPassTickets(int passTicketsCount, String emailAddress, String eventName, int currentNumberOfPassTickets) throws ApplicationException {
//		EntityManager em = emf.createEntityManager();
//		TicketCatalog tc = new TicketCatalog(em);
//		ReservationCatalog rc = new ReservationCatalog(em);
		
		Event event = ec.getEvent(eventName);
		
		if(passTicketsCount > currentNumberOfPassTickets) {
			throw new ApplicationException("Existem apenas " + currentNumberOfPassTickets + " bilhetes passe disponiveis.");
		}
		
		
		List<String> ret = new ArrayList<>();
		//reservar esses tickets com o email
		try {
//			em.getTransaction().begin();

			List<IndividualTicket> indTicketsToBuy = new ArrayList<>();
			for (int i = 0; i < passTicketsCount; i++) {
				for(EventDate ed : event.getDates()) {
					List <IndividualTicket> ticketsInDate = tc.getTicketByDayAndEvent(event, ed);
					if(!ticketsInDate.isEmpty()) {
						IndividualTicket t = ticketsInDate.get(0);
						t.setSold();
//						em.merge(t);
						indTicketsToBuy.add(t);
					}		
				}
			}
			
			double totalPrice = event.getPassTicketPrice() * ((double) passTicketsCount);
			
			List<Ticket> passTicket = new ArrayList<>();
			passTicket.add(tc.addPassTicket(totalPrice, event, indTicketsToBuy));
			
			String entity = "Santander Totta";
			BankPayment bp = new BankPayment(entity);
			rc.addReservation(totalPrice, emailAddress, bp, passTicket);
			
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
