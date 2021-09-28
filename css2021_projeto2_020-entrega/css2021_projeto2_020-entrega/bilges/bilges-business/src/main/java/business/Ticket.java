package business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Entity
@Inheritance(strategy = SINGLE_TABLE)
public abstract class Ticket {

	
	
	//Id for JPA Persistence
		@Id @GeneratedValue private int id;
		
		@Enumerated(EnumType.STRING) protected TicketStatus status;
		
		@Column(nullable = false) private double price;
		
		@ManyToOne
		private Event event;
		
		//Empty Constructor for JPA Persistence
		Ticket(){}
		
		/**
		 * Construtor super de bilhete, guarda o preco
		 * @param price preco do bilhete
		 */
		Ticket(double price, Event event) {
			this.price = price;
			this.event = event;
		}
		
		/**
		 * Devolve preco
		 * @return preco
		 */
		public double getPrice() {
			return this.price;
		}
		
		/**
		 * Devolve estado do bilhete
		 * @return estado do bilhete
		 */
		public TicketStatus getStatus() {
			return this.status;
		}
		
		/**
		 * coloca o estado do bilhete como vendido
		 */
		public void setSold() {
			this.status = TicketStatus.SOLD;
		}
	
}
