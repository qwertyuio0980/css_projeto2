package business;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import facade.exceptions.ApplicationException;

@Stateless
public class VenueCatalog {

	@PersistenceContext
	private EntityManager em;

//	/**
//	 * Construtor catalogo de instalacoes
//	 * @param em entity manager para persistir dados
//	 */
//	public VenueCatalog(EntityManager em) {
//		this.em = em;
//	}

	/**
	 * Busca instalacao com nome name
	 * @param name nome da instalacao
	 * @return instalacao
	 */
	public Venue getVenue(String name) {
		try {
			TypedQuery<Venue> query = em.createNamedQuery(Venue.FIND_BY_NAME, Venue.class);
			query.setParameter(Venue.EVENT_NAME, name);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}	
	
	/**
	 * Busca todas as instalacoes
	 * @return lista de todas as instalacoes
	 * @throws ApplicationException
	 */
	public List<Venue> getAllVenues() throws ApplicationException {
		try {
			TypedQuery<Venue> query = em.createNamedQuery(Venue.FIND_ALL, Venue.class);
			return query.getResultList();
		} catch (Exception e) {
			throw new ApplicationException ("No Venues found.", e);
		}
	}	
}
