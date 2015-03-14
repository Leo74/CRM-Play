package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model.Finder;


@Entity
public class Country {


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
    
    @Required
    public String name;

    
	public static Finder<Long, Country> find = new Finder<Long, Country>(Long.class, Country.class);

/**
	 * Retrieve all Nationalities.
	 */
	public static List<Country> findAll() {
		return find.all();
	}
}
