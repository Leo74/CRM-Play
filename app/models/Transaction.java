package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Transaction extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public long transationId;
	
	@Required
	public Date date;
	
	@Required
	public double amount;
	
	@Required
	public String status;


    @ManyToOne
    public Customer customer;

    @ManyToOne
    public Product product;
    
    @ManyToOne
    public Merchant merchant;
    
    // transaction type
	//
	
	
	
	
	
	
	public static Finder<Long,Transaction> find = new Finder<Long,Transaction>(Long.class, Transaction.class);

	public static List<Transaction> findAll() {
		return find.all();
	}

}


