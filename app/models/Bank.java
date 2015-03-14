package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;


@Entity
public class Bank extends Model{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
    
    @Required
    public String full_name;

    @Required
    public String name;

    @Required
    public String contact;

    @Required
    public String address;

    
	public static Finder<Long, Bank> find = new Finder<Long, Bank>(Long.class, Bank.class);

/**
	 * Retrieve all Nationalities.
	 */
	public static List<Bank> findAll() {
		return find.all();
	}
}
