package business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import facade.exceptions.ApplicationException;

@Stateless
public class AtributeVenueHandler {

//	/**
//	 * Entity manager factory for accessing the persistence service 
//	 */
//	private EntityManagerFactory emf;

	@EJB
	private VenueCatalog vc;
	
	@EJB
	private EventCatalog ec;
	
	@EJB
	private TicketCatalog tc;
	
	private DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
//	private Event currentEvent;


//	/**
//	 * Creates a handler for the add event use case given
//	 * the application's entity manager factory
//	 *  
//	 * @param emf The entity manager factory of tha application
//	 */
//	public AtributeVenueHandler(EntityManagerFactory emf) {
//		this.emf = emf;
//	}


	/**
	 * Comeca atribuicao de instalacao, devolve o nome de todas as instalacoes disponiveis
	 * @return Lista com o nome de todas as instalacoes disponiveis
	 * @throws ApplicationException
	 */
	public List<String> getAllVenues() throws ApplicationException {
//		EntityManager em = emf.createEntityManager();
//		VenueCatalog vc = new VenueCatalog(em);

		try {
//			em.getTransaction().begin();

			try {
				//Busca todas as instalacoes
				List<Venue> allVenues = vc.getAllVenues();
				//Passa nomes de instalacoes para lista de Strings
				List<String> allVenueNames = new ArrayList<String>();

				for (Venue v : allVenues) {
					allVenueNames.add(v.getName());
				}

//				em.getTransaction().commit();
				return allVenueNames;

			} catch(ApplicationException e) {
				throw new ApplicationException(e.getMessage());
			}	

		} catch (Exception e) {
//			if (em.getTransaction().isActive())
//				em.getTransaction().rollback();
			throw new ApplicationException("Error getting all Venues", e);
		}
//		} finally {
//			em.close();
//		}
	}


	/**
	 * Atribui instalacao, data para comeco de venda de bilhetes e preco desses bilheres a um evento
	 * @param eventName nome do evento
	 * @param venueName nome da instalacao
	 * @param date data para comeco de venda de bilhetes
	 * @param price preco dos bilhetes individuais
	 * @throws ApplicationException
	 */
	public void atributeVenue(String eventName, String venueName, String date, double price) throws ApplicationException {
//		EntityManager em = emf.createEntityManager();
//		EventCatalog ec = new EventCatalog(em);
//		VenueCatalog vc = new VenueCatalog(em);
//		TicketCatalog tc = new TicketCatalog(em);
		
		Event event = ec.getEvent(eventName);
		
		//Verifica se event existe
		if(event == null) {
			throw new ApplicationException("Evento nao existe.\n");
		}

		//Verifica se evento ja tem instalacao
		if(event.hasVenue()) {
			throw new ApplicationException("Evento ja tem instalacao atribuida.\n");
		}
		
		//Verifica se data esta no formato correto
		if(!date.matches("((0[1-9])|([1-2][0-9])|(30|31))/((0[0-9])|(11|12))/[0-9]+")) {
			throw new ApplicationException("O formato incorreto para os dias.\n");
		}

		//Da Parse de string para Date
		LocalDate dateLD = LocalDate.parse(date, this.formatterDay);
		Date dateD = java.sql.Date.valueOf(dateLD);
		
		//Verifica se data e no passado
		if(dateLD.isBefore(java.time.LocalDate.now())) {
			throw new ApplicationException("A data para comeco de venda dos bilhetes e no passado.\n");
		}

		//Verifica se data para comeco de venda e depois do comeco do evento
		if(event.existsEventDateBefore(dateD)) {
			throw new ApplicationException("A data para comeco de venda dos bilhetes e invalida pois e depois do primeiro dia do evento.\n");
		}

		//Vai buscar venue com venueName
		Venue venue = vc.getVenue(venueName);
		if(venue == null) {
			throw new ApplicationException("Instalacao nao existe.\n");
		}

		//Verifica se tipo de evento e instalacao sao compatives
		if(venue.isSeated() != event.getType().isSeated()) {
			if(event.getType().isSeated()) {
				throw new ApplicationException("Evento do tipo sentado, o que nao e permitido por esta instalacao.\n");
			} else {
				throw new ApplicationException("Evento do tipo em pe, o que nao e permitido por esta instalacao.\n");
			}

		}

		//Verifica se capacidade nao e excedida, no caso de a instalao ter lugares individuais
		if(venue.isSeated()) {
			if(venue.getCapacity() > event.getType().getAssistance()) {
				throw new ApplicationException("Capacidade da instalacao e superior a capacidade do tipo de evento.\n");
			}
		}
		
		//Verifica preco valido
		if(price < 0) {
			throw new ApplicationException("Preco invalido.\n");
		}

		//Verifica se nao instalacao nao esta ocupada nas datas do evento
		List<Event> eventsOfVenue = ec.getAllEventsWithGivenVenue(venue);
		for (Event e : eventsOfVenue) {
			if(event.daysCoincide(e)) {
				throw new ApplicationException("Instalacao ja tem as datas que esse evento necessita ocupadas.\n");
			}
		}


//		this.currentEvent = event;
		
		try {
//			em.getTransaction().begin();
			
			//Atribui instalacao e data ao evento e persiste as alteracoes
			event.atributeVenue(venue);
			event.setTicketSellDate(dateD);			

			//Gerar bilhetes
			List<EventDate> dates = event.getDates();
			List<Ticket> newTickets = new ArrayList<>();
			
			if(venue.isSeated()) {
				List<Seat> seats = venue.getSeats();
				
				for(Seat s: seats) {
					for(EventDate d : dates) {
						newTickets.add(tc.addIndividualTicket(price, s, d, event));
					}
				}
				
			} else {
				
				for(int i = 0; i < venue.getCapacity(); i++) {
					for(EventDate d : dates) {
						newTickets.add(tc.addIndividualTicket(price, null, d, event));
					}
				}
			}
				
			event.setTickets(newTickets);
//			em.merge(event);

//			em.getTransaction().commit();
		} catch (Exception e) {
//			if (em.getTransaction().isActive())
//				em.getTransaction().rollback();
			throw new ApplicationException("Error adding venue with name " + venueName + " to event " + eventName, e);
		}
//	 	} finally {
//			em.close();
//		}

	}
	
	/**
	 * Opcionalmente, define o preco de bilhetes passe
	 * @param price
	 * @throws ApplicationException
	 */
	public void indicatePassTicketPrice(double price, String eventName) throws ApplicationException {
//		EntityManager em = emf.createEntityManager();		
		
		Event event = this.ec.getEvent(eventName);
		
		try {
//			em.getTransaction().begin();
			
			//Atualiza preco de bilhete passe no evento
			event.setPassTicketPrice(price);
//			em.merge(this.currentEvent);

//			em.getTransaction().commit();
		} catch (Exception e) {
//			if (em.getTransaction().isActive())
//				em.getTransaction().rollback();
			throw new ApplicationException("Error adding pass ticket price to event." , e);
		} 
//	    } finally {
//			em.close();
//		}
	}
}