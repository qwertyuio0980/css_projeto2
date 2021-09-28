package business;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import facade.exceptions.ApplicationException;

@Stateless
public class EventCatalog {

	@PersistenceContext
	private EntityManager em;

//	/**
//	 * Construtor catalogo de eventos
//	 * @param em entity manager para persistir dados
//	 */
//	public EventCatalog(EntityManager em) {
//		this.em = em;
//	}

	/**
	 * Busca event com dado nome
	 * @param name nome do evento
	 * @return evento
	 */
	public Event getEvent(String name) {
		try {
			TypedQuery<Event> query = em.createNamedQuery(Event.FIND_BY_NAME, Event.class);
			query.setParameter(Event.EVENT_NAME, name);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Cria e persiste evento
	 * @param name nome do evento
	 * @param dates datas do evento
	 * @param company empresa do evento
	 * @param type tipo do evento
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void addEvent(String name, List<EventDate> dates, Company company, EventType type) {
		Event event = new Event(name, type, dates, company);
		em.persist(event);
	}

	
	/**
	 * Busca todos eventos com instalacao com dado id
	 * @param id id da instalacao
	 * @return Lista de eventos com dada instalacao
	 * @throws ApplicationException
	 */
	public List<Event> getAllEventsWithGivenVenue(Venue venue) throws ApplicationException {
		try {
			TypedQuery<Event> query = em.createNamedQuery(Event.FIND_BY_VENUE_ID, Event.class);
			query.setParameter(Event.VENUE, venue);
			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException ("Event with venue: " + venue.getName() + " not found.", e);
		}
	}

	/**
	 * Verifica se evento existe
	 * @param name
	 * @return
	 */
	public boolean eventExists(String name) {
		TypedQuery<Long> query = em.createNamedQuery(Event.EVENT_EXISTS, Long.class);
		query.setParameter(Event.EVENT_NAME, name);
		return query.getSingleResult() > 0;
	}


}