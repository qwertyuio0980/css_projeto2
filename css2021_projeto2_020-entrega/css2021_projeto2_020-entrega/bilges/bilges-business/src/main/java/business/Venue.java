package business;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import business.EventType;

@Entity
@NamedQueries({
	@NamedQuery(name=Venue.FIND_BY_NAME, query="SELECT v FROM Venue v WHERE v.name= :" + 
		Venue.EVENT_NAME),
	@NamedQuery(name=Venue.FIND_ALL, query="SELECT v FROM Venue v")
})
public class Venue {

	public static final String FIND_ALL = "Venue.findAll";
	public static final String FIND_BY_NAME = "Venue.findByName";
	public static final String EVENT_NAME = "name";

	//Id for JPA Persistence
	@Id @GeneratedValue private int id;
	
	@Column(nullable = false) private String name;
	
	@Column(nullable = false) private int capacity;
	
	@OneToMany
	private List<Seat> seats;
		
	//Empty Constructor for JPA Persistence
	Venue(){}
	
	/**
	 * Busca nome de instalacao
	 * @return nome
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Busca capacidade de instalacao
	 * @return capacidade
	 */
	public int getCapacity() {
		return this.capacity;
	}
	
	/**
	 * Retorna se instalacao e do tipo sentado ou nao
	 * @return true se for sentado, false se nao
	 */
	public boolean isSeated() {
		return !seats.isEmpty();
	}

	/**
	 * Busca id da instalacao
	 * @return id
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Busca lugares da instalacao
	 * @return lugares
	 */
	public List<Seat> getSeats() {
		return this.seats;
	}
}
