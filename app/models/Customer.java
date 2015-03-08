package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


//import play.db.jpa.*;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Customer extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
    
    @Required
    @MinLength(value = 4)
    public String first_name;

    @Required
    @MinLength(value = 4)
    public String last_name;


    @Required
    @MinLength(value = 6)
    public String contact;

    @Required
    @Email
    @Column(unique=true)
    public String email;

    @Required
    @MinLength(value = 6)
    public String password;

    
	/**
	 * Generic query helper for entity Nationality with id Long
	 */
	public static Finder<Long,Customer> find = new Finder<Long,Customer>(Long.class, Customer.class);



/**
	 * Retrieve all Nationalities.
	 */
	public static List<Customer> findAll() {
		return find.all();
	}

}

