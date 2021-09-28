package application;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import business.AtributeVenueHandler;
//import business.AtributeVenueHandler;
//import business.Venue;
import facade.exceptions.ApplicationException;
import facade.services.IEventServiceRemote;
import facade.services.IVenueServiceRemote;

@Stateless
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class VenueService implements IVenueServiceRemote {
	
	/**
	 *  O objeto da facade que trata o caso de uso de atribuir instalaçao para o evento x
	 */
	@EJB
	private AtributeVenueHandler venueHandler;


//    /**
//     * Constroi um Serviço de atribuirInstalaçao através de um AtributeVenueHandler.
//     * 
//     * @param venueHandler O handler de atribuir instalação
//     */
//	public VenueService(AtributeVenueHandler venueHandler) {
//		this.venueHandler = venueHandler;
//	}
	
	/**
	 * Metodo que devolve todas as instalaçoes
	 * @return uma lista com todas as instalaçoes disponiveis
	 * @throws ApplicationException caso não existam venues disponiveis
	 */
	public List<String> chooseVenue() throws ApplicationException {
		return venueHandler.getAllVenues();
		
//		List<String> ret = new ArrayList<>();
//		ret.add("Martim Coiso");
//		ret.add("Tiagvenueski");
//		ret.add("Estadio do homem da Canoa");
//		return ret;
	}
	
	/**
	 * Metodo que trata de atribuir uma instalaçao para o evento eventName
	 * @param eventName o nome do evento
	 * @param venueName a instalaçao que queremos atribuir
	 * @param date a data de começo de venda dos bilhetes
	 * @param preco o preço dos bilhetes Individuais
	 * @throws ApplicationException caso nao consiga atribuir a instalaçao para o evento
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void atributeVenue(String eventName, String venueName, String date, double preco) throws ApplicationException {
		venueHandler.atributeVenue(eventName, venueName, date, preco);
	}
	
	/**
	 * Metodo que trata de atribuir um preço aos bilhetesPasse
	 * @param price preco dos bilhetesPasse
	 * @throws ApplicationException caso nao seja possivel adicionar preço aos bilhetesPasse
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public void setPassTicketPrice(double price, String eventName) throws ApplicationException {
        venueHandler.indicatePassTicketPrice(price, eventName);
    }
	
}
