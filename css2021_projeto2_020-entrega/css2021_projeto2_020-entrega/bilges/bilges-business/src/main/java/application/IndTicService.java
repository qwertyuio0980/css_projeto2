package application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import business.ReserveIndTicHandler;
import facade.exceptions.ApplicationException;
import facade.services.IReserveIndTicServiceRemote;

@Stateless
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class IndTicService implements IReserveIndTicServiceRemote {

	/**
	 * O objeto da facade que trata o caso de uso de criar Eventos
	 */
	@EJB
	private ReserveIndTicHandler reserveIndTicHandler;

//    /**
//     * Constroi um Serviço de bilheteIndividual através de um handlerBilheteIndividual.
//     * 
//     * @param ReserveIndTicHandler o handler de bilheteIndividual
//     */
//	public IndTicService(ReserveIndTicHandler reserveIndividualTicketHandler) {
//		this.reserveIndTicHandler = reserveIndividualTicketHandler;
//	}
	
	
	/**
	 * Devolve as datas de um dado eventName
	 * 
	 * @param EventName o nome do evento para qual pretendemos comprar bilhetes
	 * @return Set de Strings com todas as datas do evento
	 * @throws ApplicationException no caso em que não e possivel comprar bilhetes
	 */
	public Set<String> getEventDates(String eventName) throws ApplicationException {
		return reserveIndTicHandler.getEventDates(eventName);
		
		//Versao mock
//		Set<String> setEventDates = new HashSet<>();
//		setEventDates.add("09/05/2021");
//		setEventDates.add("10/05/2021");
//		setEventDates.add("11/05/2021");
//		return setEventDates;
	}

	/** 
	 * Devolve uma lista contendo os lugares disponiveis para reservar na data especifica do evento chosenDay
	 * @param chosenDate a data escolhida
	 * @return Retorna uma lista de strings com os lugares disponiveis para o evento no dia escolhido ordenadas primeiro por fila e seguidamente por numero de cadeira
	 * @throws ApplicationException caso não tenha lugares disponiveis.
	 */
	public List<String> getSeatsOfEventDate(String chosenDay, String eventName) throws ApplicationException {
		return reserveIndTicHandler.getSeatsOfEventDate(chosenDay, eventName);
		
		//Versao mock
//		List<String> chosenSeats12 = new ArrayList<>();
//		chosenSeats12.add("A-1");
//		chosenSeats12.add("A-2");
//		chosenSeats12.add("B-1");
//		
//		return chosenSeats12;
	}

	/**
	 * Reserva os bilhetes de acordo com os lugares escolhidos e também coloca esses lugares especificos como "SOLD" (indisponiveis)
	 * @param chosenSeats os lugares escolhidos para reservar
	 * @param emailAddress o email para onde a informaçao sobre os bilhetes vai ser enviada ( Não esta implementado nesta fase)
	 * @return Uma lista com 3 elementos: Referencia, Entidade e Valor para pagamento.
	 * @throws ApplicationException
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public List<String> reserveSeats(List<String> chosenSeats, String emailAddress, String eventName, String chosenDay) throws ApplicationException {
		return reserveIndTicHandler.reserveSeats(chosenSeats, emailAddress, eventName, chosenDay); //COMPARAR COM HANDLERS DA PROF
		
		//Versao mock
//		List<String> ret = new ArrayList<>();
//		ret.add("Entidade: "  + "Santander Totta");
//		ret.add("Referencia: " + Long.toString(15000000000L));
//		ret.add("Valor total: " + Double.toString(25.45));
//		return ret;
	}
	
}
