package business;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Parameter;

@Entity
@NamedQuery(name=Company.FIND_BY_COMPANYNR, query="SELECT c FROM Company c WHERE c.number = :" + 
		Company.COMPANY_NUMBER)
public class Company {
	
	// Named query name constants
	public static final String  FIND_BY_COMPANYNR = "Company.findByCompanyNr";
	public static final String COMPANY_NUMBER = "number";
	
	
	@Id @GeneratedValue private int id;
//	@Id private int id;
	
	@Column(nullable = false, unique = true) private int number;
	
	@Column(nullable = false) private String name;
	
	@Enumerated(EnumType.STRING)
	@ElementCollection
	private List<EventType> typesCertificate;
	
	//Empty Constructor for JPA Persistence
	Company(){}

	/**
	 * Retorna lista com tipos de evento que empresa esta certificada a fazer
	 * @return lista com tipos de evento que empresa esta certificada a fazer
	 */
	public List<EventType> getTypes() {
		return this.typesCertificate;
	}
}