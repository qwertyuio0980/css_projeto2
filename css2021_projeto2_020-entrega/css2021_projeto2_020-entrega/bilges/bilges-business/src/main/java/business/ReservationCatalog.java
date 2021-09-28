package business;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import facade.exceptions.ApplicationException;

@Stateless
public class ReservationCatalog {

	/**
	 * Entity manager factory for accessing the persistence service 
	 */
	@PersistenceContext
	private EntityManager em;

//	/**
//	 * Constructs a individual ticket's reservation catalog giving a entity manager factory
//	 */
//	public ReservationCatalog(EntityManager em) {
//		this.em = em;
//	}
	
	/**
	 * Adiciona uma reserva
	 * @param totalPrice preço total da reserva
	 * @param email email para reserva
	 * @param bankPayment dados de pagamento
	 * @param ticketList lista dos bilhetes que vao ser reservados
	 * 
	 * @throws ApplicationException
	 */
//	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void addReservation(double totalPrice, String email, BankPayment bankPayment, List<Ticket> ticketList) {
		Reservation reservation = new Reservation(totalPrice, email, bankPayment, ticketList);
		em.persist(reservation);
	}

}
