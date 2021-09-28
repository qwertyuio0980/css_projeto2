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
public class AddEventHandler {

//	/**
//	 * Entity manager factory for accessing the persistence service 
//	 */
//	private EntityManagerFactory emf;

	/**
	 * To parse Strings to LocalDate
	 */
	private DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");
	
	@EJB
	private EventCatalog ec;
	
	@EJB
	private CompanyCatalog cc;
	
	
//	/**
//	 * Creates a handler for the add event use case given
//	 * the application's entity manager factory
//	 *  
//	 * @param emf The entity manager factory of tha application
//	 */
//	public AddEventHandler(EntityManagerFactory emf) {
//		this.emf = emf;
//	}

	/**
	 * Adiciona um novo event dado o nome, lista de dias do event,
	 * horas de comeco e fim de cada dia, o tipo de evento e o numero de registo de uma empresa
	 * 
	 * Para cada dia temos duas horas. Se tivermos os dias 1/1/2021 - (14:00 - 16:00) e 2/1/2021 - (10:00 - 19:00)
	 * as listas days e startAndEndHours devem ser do tipo:
	 * [1/1/2021, 2/1/2021] e [14:00, 16:00, 10:00, 19:00] respetivamente
	 * @param name nome do novo evento
	 * @param days dias em que vai ocorrer
	 * @param startAndEndHours horas inicio/fim para cada dia
	 * @param type tipo do evento
	 * @param companyNr numero de registo de uma empresa
	 * @throws ApplicationException
	 */
	public void addEvent(String name, List<String> days, List<String> startAndEndHours, String type,
			int companyNr) throws ApplicationException {

//		EntityManager em = emf.createEntityManager();
//		EventCatalog ec = new EventCatalog(em);
//		CompanyCatalog cc = new CompanyCatalog(em);

		//Verifica se String corresponde a Tipo de Evento
		EventType typeEnum = null;
		try {

			typeEnum = EventType.valueOf(type);

		} catch(IllegalArgumentException e) {
			throw new ApplicationException("Esse Tipo de Evento nao e valido.");
		}

		//Verifica se cada dia tem 2 horas (inicio e fim)
		if(days.size() * 2 != startAndEndHours.size()) {
			throw new ApplicationException("Especificacao de datas incorreta. Para cada dia especifica uma hora de inicio e uma de fim.");
		}

		//Da parse de horas para LocalDate
		ArrayList<LocalTime> hoursDate = new ArrayList<LocalTime>();
		for (String s : startAndEndHours) {
			//Verifica se formato de horas esta correto (1 a 24 : 0 a 59)
			if(!s.matches("(((([0-1][0-9])|(2[0-3])):([0-5][0-9]))|24:00)")) {
				throw new ApplicationException("O formato incorreto para as horas.\n");
			}		
			
			if(s.equals("24:00")) {
				s = "23:59";
			}
			
			LocalTime hour = LocalTime.parse(s, this.formatterHour);
			hoursDate.add(hour);
		}

		//Da parse de dias para LocalDate
		ArrayList<Date> daysDate = new ArrayList<Date>();
		ArrayList<LocalDate> daysLocalDate = new ArrayList<LocalDate>();
		for (String s : days) {
			//Verifica formato de dias (01 a 31/1 a 12/0 - ...)
			if(!s.matches("((0[1-9])|([1-2][0-9])|(30|31))/((0[0-9])|(11|12))/[0-9]+")) {
				throw new ApplicationException("O formato incorreto para os dias.\n");
			}

			LocalDate date = LocalDate.parse(s, this.formatterDay);
			daysDate.add(java.sql.Date.valueOf(date));
			daysLocalDate.add(date);
		}
		
		LocalDate lastDay = null;
		for (LocalDate ld : daysLocalDate) {
			if(lastDay != null) {
				
				if(!lastDay.plusDays(1).equals(ld)) {
					throw new ApplicationException("Datas não são consecutivas.\n");
				}
				lastDay = ld;
				
			} else {
				lastDay = ld;
			}
			
		}

		//Verifica se existe hora de fim antes de hora de inicio correspondente
		List<EventDate> eventDates = new ArrayList<EventDate>();
		for (int i = 0; i < daysDate.size(); i++) {
			if(hoursDate.get(i * 2).isAfter(hoursDate.get(i * 2 + 1))) {
				throw new ApplicationException("As horas de inicio tem que ser antes da hora de fim.\n");
			}

			eventDates.add(new EventDate(localTimeToDate(hoursDate.get(i * 2)), localTimeToDate(hoursDate.get(i * 2 + 1)), daysDate.get(i)));
		}

		//Verifica se evento existe
		if(ec.eventExists(name)) {
			throw new ApplicationException("Ja existe um Evento com o nome: " + name + ".\n");
		}

		//Verifica se empresa nao existe
		Company company = cc.getCompany(companyNr);
		if(company == null) {
			throw new ApplicationException("A empresa com o numero de registo " + companyNr + " nao existe.");
		}

		//Verifica se company suporta tipo de evento
		if(!company.getTypes().contains(typeEnum)) {
			throw new ApplicationException("A empresa com o numero de registo " + companyNr + " nao faz eventos do tipo " + type);
		}


		try {
//			em.getTransaction().begin();

			//Persiste evento
			ec.addEvent(name, eventDates, company, typeEnum);

//			em.getTransaction().commit();

		} catch (Exception e) {
//			if (em.getTransaction().isActive())
//				em.getTransaction().rollback();
			throw new ApplicationException("Error adding event with name " + name, e);
		} 
//	    } finally {
//			em.close();
//		}
	}

	//Funcao para conversao LocalTime -> Date retirada de:
	//https://www.logicbig.com/how-to/java-8-date-time-api/to-date-conversion.html
	private static Date localTimeToDate(LocalTime localTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(0, 0, 0, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
		return calendar.getTime();
	}

	/**
	 * Comeco da criacao de eventos, retorna a lista de tipos de eventos possiveis
	 * @return Lista com todos os tipos de eventos
	 */
	public List<String> getEventTypes() {
		EventType [] ev = EventType.values();
		List<String> evs = new ArrayList<String>();

		for (int i = 0; i < ev.length; i++) {
			evs.add(ev[i].name());
		}

		return evs;
	}

}
