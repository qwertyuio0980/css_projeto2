package application;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import business.AddEventHandler;
import facade.exceptions.ApplicationException;
import facade.services.IEventServiceRemote;

@Stateless
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class EventService implements IEventServiceRemote {
	/**
	 * O objeto da facade que trata o caso de uso de criar Eventos
	 */
	@EJB
	private AddEventHandler eventHandler;

//	/**
//	 * Constroi um eventService atraves do eventHandler
//	 * 
//	 * @param eventHandler o Handler de adicionar evento
//	 */
//	public EventService(AddEventHandler eventHandler) {
//		this.eventHandler = eventHandler;
//	}

	/**
	 * Cria o evento com os parametros passados
	 * 
	 * @param name o nome do evento a criar
	 * @param days lista de strings com os dias do evento 
	 * @param startAndEndHours as horas de inicio e fim do evento
	 * @param type o tipo do evento
	 * @param companyNr o numero de registo da empresa
	 * @throws ApplicationException Caso não seja posivel criar o evento
	 */
	public void giveEventDetails(String name, List<String> days, List<String> startAndEndHours, String type, int companyNr) throws ApplicationException {
		eventHandler.addEvent(name, days, startAndEndHours, type, companyNr);
	}

	/**
	 * Metodo que cria uma lista com todos os tipos de eventos
	 * @return lista com todos os tipos de eventos
	 */
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public List<String> createNewEvent() {
		return eventHandler.getEventTypes();
		
		//versao mock
//		List<String> eventTypes = new ArrayList<>();
//		eventTypes.add("Coiso");
//		eventTypes.add("Yay");
//		eventTypes.add("Funengao Carrega");
//		return eventTypes;
	}

}
