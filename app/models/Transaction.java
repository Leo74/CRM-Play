package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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
	public long id;
	
	@Required
	public Date date;
	
	@Required
	public double amount;
	
	@Required
	public String status;

	
	public String description;

    @ManyToOne
    public Customer customer;

    @ManyToOne
    public Product product;
    
    @ManyToOne
    public Merchant merchant;

    @ManyToOne
    public Country buyingLocation;

    @ManyToOne
    public Bank bank;

    // transaction type
	//
	
//  @Column(columnDefinition="default 0")
	public boolean adminRead;
	
//    @Column(columnDefinition="default 0")
	public boolean merchantRead;
	
	public String OTP;
	
	public Date optSendDate;
	
	
	public static Finder<Long,Transaction> find = new Finder<Long,Transaction>(Long.class, Transaction.class);

	public static List<Transaction> findAll() {
		return find.all();
	}

}


