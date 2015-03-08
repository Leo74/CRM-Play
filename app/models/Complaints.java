package models;

import java.sql.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Complaints extends Model{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
    
    public Customer customer;

    public Merchant merchant;
    
    public Date date;

    public String ComplaintData;
    
    @Required
    public String status;

	public static Finder<Long,Complaints> find = new Finder<Long,Complaints>(Long.class, Complaints.class);

	public static List<Complaints> findAll() {
		return find.all();
	}
    
}

