package business;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Access;
import static javax.persistence.AccessType.FIELD;

/**
 * A reservation
 * 
 * @author css020
 *
 */

@Entity
public class Reservation {

	//Id for JPA Persistence
	@Id @GeneratedValue private int id;
	
	@Column(nullable = false) private double totalPrice;
	
	@Column(nullable = false) private String email;
	
	@OneToMany
	private List<Ticket> tickets;

	@Embedded
	private BankPayment bankPayment;
	
	/**
	 * Constructor needed by JPA.
	 */
	Reservation(){
	}
	
	/**
	 * Cria uma nova reserva
	 * 
	 * @param totalPrice o preço total da reserva
	 * @param email O email para onde vai ser enviada toda a informaçao dos bilhetes
	 * @param bankPayment os dados de pagamento da reserva
	 */
	public Reservation(double totalPrice, String email, BankPayment bankPayment, List<Ticket> tickets) {
		this.totalPrice = totalPrice;
		this.email = email;
		this.bankPayment = bankPayment;
		this.tickets = tickets;
	}
}