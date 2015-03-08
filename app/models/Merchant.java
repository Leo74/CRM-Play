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
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;




@Entity
public class Merchant extends Model {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	
	@Required
    public String full_name;

	@Required
    public String business_name;
	
	@Required
    public String contact;
	
    @Required
    @Email
    @Column(unique=true)
    public String email;


    @Required
    @Email
    public String website;
    
    @Required
    @Email
    public String address;

    public String faxno;
    
    public String rank;
    
    @Required
    public String password;

/*
    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
    public List<Transaction> trasactions;
*/
    
	/**
	 * Generic query helper for entity Nationality with id Long
	 */
	public static Finder<Long, Merchant> find = new Finder<Long,Merchant>(Long.class, Merchant.class);



/**
	 * Retrieve all Nationalities.
	 */
	public static List<Merchant> findAll() {
		return find.all();
	}

}

