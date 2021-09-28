package application;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import business.ReservePassTicketHandler;
import facade.exceptions.ApplicationException;
import facade.services.IReservePassTicServiceRemote;

@Stateless
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class ReservePassTicketService implements IReservePassTicServiceRemote {
	
	/**
	 * O objeto da facade que trata o caso de uso de comprar bilhetesPasse com escolha
	 * ou não de lugares.
	 */
	@EJB
    private ReservePassTicketHandler passTicketHandler;
    
//    /**
//     * Constroi um Serviço de bilhetePasse através de um handlerBilhetesPasse.
//     * @param passTicketHandler passTicketHandler o handler de bilhetePasse
//     */
//    public ReservePassTicketService(ReservePassTicketHandler passTicketHandler) {
//        this.passTicketHandler = passTicketHandler;
//    }

    /**
     * Metodo que verifica quantos bilhetes existem em cada dia do evento e devolve o numero de bilhetesPasse disponiveis
     * @param eventName nome do evento para o qual queremos comprar bilhetesPasse
     * @return o numero de bilhetesPasse disponiveis para compra
     * @throws ApplicationException caso não existam bilhetesPasse disponiveis 
     */
    public int indicatePassTickets(String eventName) throws ApplicationException {
    	return passTicketHandler.getPassTicketValidated(eventName);
    	
    	//Versao mock
    	//return 2;
    }

    /**
     * Metodo que efetua o processo de reservar os passTicketsCount que o cliente pretende, podendo o evento ser com lugares marcados ou não.
     * @param passTicketsCount numero de bilhetes passe que o cliente quer comprar
     * @param emailAddress emailAddress o email para onde a informaçao sobre os bilhetesPasse vai ser enviada ( Não esta implementado nesta fase)
     * @return Uma lista com 3 elementos: Referencia, Entidade e Valor para pagamento.
     * @throws ApplicationException caso não consiga reservar os bilhetes
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public List<String> reservePassTickets(int passTicketsCount, String emailAddress, String eventName, int currentNumberOfPassTickets) throws ApplicationException {
        return passTicketHandler.buyPassTickets(passTicketsCount, emailAddress, eventName, currentNumberOfPassTickets);
        
        //Versao mock
//		List<String> ret = new ArrayList<>();
//		ret.add("Entidade: "  + "Santander Totta");
//		ret.add("Referencia: " + Long.toString(15000000000L));
//		ret.add("Valor total: " + Double.toString(50.90));
//		return ret;
    }
	
}
