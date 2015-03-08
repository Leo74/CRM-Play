package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Product extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@Required
	@Column(unique=true)
	public String name;
	
    @Required
    public String type;
    
    @Required
    public double amount;

 /*   
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    public List<Transaction> trasactions;
*/
     /**
	 * Generic query helper for entity Nationality with id Long
	 */
	public static Finder<Long, Product> find = new Finder<Long,Product>(Long.class, Product.class);

/**
	 * Retrieve all Nationalities.
	 */
	public static List<Product> findAll() {
		return find.all();
	}

}

