package business;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Classe que representa um dia do evento, tem o dia, a hora de inicio e de fim
 * @author css020
 *
 */
@Embeddable
public class EventDate {

	/**
	 * hora de inicio desta data
	 */
	@Temporal(TemporalType.TIME) @Column(nullable = true)
	private Date startHour;

	/**
	 * hora de fim desta data
	 */
	@Temporal(TemporalType.TIME) @Column(nullable = true)
	private Date endHour;
	
	/**
	 * dia da data
	 */
	@Temporal(TemporalType.DATE) @Column(nullable = true)
	private Date day;
	
	//Empty Constructor for JPA Persistence
	EventDate(){}
	
	public EventDate(Date startHour, Date endHour, Date day) {
		this.startHour = startHour;
		this.endHour = endHour;
		this.day = day;
	}

	/**
	 * Verfica se dada data e antes do dia deste EventDate
	 * @param dateD data
	 * @return true se e antes, false se nao
	 */
	public boolean isBefore(Date dateD) {
		return day.before(dateD);
	}

	/**
	 * Verifica se outra EventDate coincide com esta
	 * Coincide se dia e igual e partilham intervalo de horas
	 * @param d outro EventDate
	 * @return true se coincidem, false se nao
	 */
	public boolean coincides(EventDate d) {
		if(this.day.equals(d.day)) {
			
			if(this.endHour.after(d.startHour)) {
				return this.startHour.before(d.endHour);
			}
		}
		return false;
	}
	
	/**
	 * toString de EventDate
	 */
	public String toString() {
		return "Day: " + this.getDay() + " Start Hour: " + this.getStartHour() + " End Hour: " + this.getEndHour();
	}
	
	/**
	 * Busca dia deste evento
	 * @return dia deste evento
	 */
	public String getDay() {
		LocalDate ld = convertToLocalDateViaInstant(this.day);
		return ld.getDayOfMonth() + "-" + ld.getMonthValue() + "-" + ld.getYear();
	}

	/**
	 * Busca hora de inicio deste evento
	 * @return hora de inicio deste evento
	 */
	public String getStartHour() {
		return this.getHour(this.startHour);
	}
	
	/**
	 * Busca hora de fim deste evento
	 * @return hora de fim deste evento
	 */
	public String getEndHour() {
		return this.getHour(this.endHour);
	}
	
	//Auxiliar para retornar hora
	private String getHour(Date d) {
		Calendar cal = dateToCalendar(d);
		
		int calMinute = cal.get(Calendar.MINUTE);
		String minutes = calMinute < 10 ? "0" + calMinute : calMinute + "";
		return cal.get(Calendar.HOUR_OF_DAY) + ":" + minutes + ":00";
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof EventDate)) {
			return false;
		}
		EventDate ed = (EventDate) obj;
		
		return ed.equalHours(ed.startHour, this.startHour) && ed.equalHours(ed.endHour, this.endHour)
				&& ed.equalDay(ed.day, this.day);
	}



	/**
	 * metodo auxiliar para equals da classe EventDate
	 * @param day1 data para comparar
	 * @param day2 data para comparar
	 * @return true se os dias das datas sao iguais, falso caso contrario
	 */
	private boolean equalDay(Date day1, Date day2) {
		LocalDate d1 = convertToLocalDateViaInstant(day1);
		LocalDate d2 = convertToLocalDateViaInstant(day2);
		
		return d1.getDayOfMonth() == d2.getDayOfMonth() &&
				d1.getMonthValue() == d2.getMonthValue() &&
				d1.getYear() == d2.getYear();
	}


	/**
	 * metodo auxiliar para equals da classe EventDate
	 * @param hour1 hora de inicio para comparar
	 * @param hour2 hora de fim para comparar
	 * @return true se as horas das datas são iguais, falso caso contrario.
	 */
	private boolean equalHours(Date hour1, Date hour2) {
		Calendar h1 = dateToCalendar(hour1);
		Calendar h2 = dateToCalendar(hour2);
		
		return h1.get(Calendar.HOUR_OF_DAY) == h2.get(Calendar.HOUR_OF_DAY) && h1.get(Calendar.MINUTE) == h2.get(Calendar.MINUTE);
	}
	

	/**
	 * metodo auxiliar para equals da classe EventDate
	 * @param dayDate data para comparar
	 * @return true se os dias das datas sao iguais, falso caso contrario
	 */
	public boolean sameDay(Date dayDate) {
		return equalDay(dayDate, this.day);
	}

	//Metodo Date -> LocalDate retirado de (pois Date esta deprecated):
	//https://www.baeldung.com/java-date-to-localdate-and-localdatetime
	private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	//Metodo Date -> LocalDate retirado de (pois Date esta deprecated):
	//https://mkyong.com/java/java-convert-date-to-calendar-example/
	private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}