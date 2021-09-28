package business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Seat {
	
	//Id for JPA Persistence
	@Id @GeneratedValue private int id;
	
	@Column(nullable = false) private String row;
	
	@Column(nullable = false) private int chairNumber;
	
	//Empty Constructor for JPA Persistence
	Seat(){}
	
	@Override
	/**
	 * toString de Seat
	 */
	public String toString() {
		return this.row + "-" + this.chairNumber;
	}
	
	/**
	 * getter de fila de cadeiras
	 * @return a fila da cadeira
	 */
	public String getRow() {
		return this.row;
	}
	

	/**
	 * getter de numero de cadeira
	 * @return chairNumber numero da cadeira
	 */
	public int getChairNumber() {
		return this.chairNumber;
	}	
}