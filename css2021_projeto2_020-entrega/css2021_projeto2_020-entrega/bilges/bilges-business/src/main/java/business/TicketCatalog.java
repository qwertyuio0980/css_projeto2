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
public class TicketCatalog {

	@PersistenceContext
	private EntityManager em;

//	public TicketCatalog(EntityManager em) {
//		this.em = em;
//	}

	/**
	 * Adiciona ticket com preco, um dado lugar e uma data
	 * @param price preco
	 * @param s lugar
	 * @param d data
	 * @return o bilhete adicionado
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public Ticket addIndividualTicket(double price, Seat s, EventDate d, Event e) {
		IndividualTicket ticket = new IndividualTicket(price, s, d, e);
		em.persist(ticket);
		return ticket;
	}


	/**
	 * metodo para devolver uma lista com os IndividualTickers de acordo com um respetivo evento e Data
	 * @param event o evento em questao
	 * @param ed a data do evento que pretendemos saber os bilhetes
	 * @return a lista dos IndividualTickets
	 * @throws ApplicationException
	 */
	public List<IndividualTicket> getTicketByDayAndEvent(Event event, EventDate ed) throws ApplicationException {
		try {
			TypedQuery<IndividualTicket> query = em.createNamedQuery(IndividualTicket.FIND_AVAILABLE_BY_EVENT_AND_DAY, IndividualTicket.class);
			query.setParameter(IndividualTicket.EVENT, event);
			query.setParameter(IndividualTicket.DATE_DAY, ed);
			query.setParameter(IndividualTicket.TICKET_STATUS, TicketStatus.ONSALE);

			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException ("Individual Tickets with date " + ed.toString() + " not found.", e);
		}
	}
	/**
	 * este metodo faz um query na base de dados e recebe a lista dos individualTickets de acordo 
	 * com o evento, data e lugar
	 * @param event o evento em questao
	 * @param eventDate a data do evento que pretendemos saber os bilhetes
	 * @param seat o lugar em questao
	 * @return a lista dos individualTickets
	 * @throws ApplicationException
	 */
	public List<IndividualTicket> getTicketsByEventAndDateAndSeat(Event event, EventDate eventDate, Seat seat) throws ApplicationException {
		try {
			TypedQuery<IndividualTicket> query = em.createNamedQuery(IndividualTicket.FIND_AVAILABLE_BY_EVENT_AND_DAY_AND_SEAT, IndividualTicket.class);
			query.setParameter(IndividualTicket.EVENT, event);
			query.setParameter(IndividualTicket.DATE_DAY, eventDate);
			query.setParameter(IndividualTicket.TICKET_STATUS, TicketStatus.ONSALE);
			query.setParameter(IndividualTicket.SEAT, seat);

			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException ("Seat " + seat.toString() + " not found.", e);
		}
	}
	/**
	 * metodo que retorna o numero de bilhetes de um evento q existem numa data ed
	 * @param event o evento
	 * @param ed a data
	 * @return o numero bilhetes 
	 * @throws ApplicationException
	 */
	public Long numberOfTicketsByDateAndEvent(Event event, EventDate ed) throws ApplicationException {

		TypedQuery<Long> query = em.createNamedQuery(IndividualTicket.COUNT_AVAILABLE_BY_EVENT_AND_DAY, Long.class);
		query.setParameter(IndividualTicket.EVENT, event);
		query.setParameter(IndividualTicket.DATE_DAY, ed);
		query.setParameter(IndividualTicket.TICKET_STATUS, TicketStatus.ONSALE);

		return query.getSingleResult();
	}


	/**
	 * Gera o passTicket de acordo com o preco, evento e bilhetes individuais
	 * @param totalPrice o preco do bilhetePasse
	 * @param currentEvent o evento
	 * @param indTicketsToBuy o numero de bilhetes individuais 
	 * @return
	 */
//	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public Ticket addPassTicket(double totalPrice, Event currentEvent, List<IndividualTicket> indTicketsToBuy) {
		PassTicket ticket = new PassTicket(totalPrice, currentEvent, indTicketsToBuy);
		em.persist(ticket);
		return ticket;
	}
}