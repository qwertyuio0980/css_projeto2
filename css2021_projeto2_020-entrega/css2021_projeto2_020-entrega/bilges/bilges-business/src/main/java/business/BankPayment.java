package business;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * A bank payment
 * 
 * @author css020
 *
 */


@Embeddable
public class BankPayment {

	
	@Column(nullable = false) private String entity;
	
	@Column(nullable = false) private long paymentReference;

	/**
	 * Constructor needed by JPA.
	 */
	BankPayment() {
	}
	
	/**
	 * Constroi o bankPayment atraves de uma entidade entity
	 * @param entity para pagamento
	 */
	public BankPayment(String entity) {
		this.entity = entity;
		this.paymentReference = generateReference();
	}
	
	/**
	 * getter para Entity
	 * @return entity do pagamento
	 */
	public String getEntity() {
		return this.entity;
	}
	
	/**
	 * getter para referencia do pagamento paymentReference
	 * @return referencia do pagamento
	 */
	public long getPaymentReference() {
		return this.paymentReference;
	}
	
	/**
	 * Metodo que gera uma referencia random para pagamento
	 * @return a referencia para pagamento
	 */
	private long generateReference() {
		long l = new Random().nextLong();
		if(l < 0) {
			l*= -1;
		}
		return l;
	}
}
