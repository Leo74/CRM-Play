package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
//import play.db.jpa.*;

@Entity
public class Feedback extends Model{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
    
    public Customer customer;

    public Merchant merchant;
    
    public Date date;

    public String FeedbackData;
    
    @Required
    public String status;

	public static Finder<Long,Feedback> find = new Finder<Long,Feedback>(Long.class, Feedback.class);

	public static List<Feedback> findAll() {
		return find.all();
	}
    
}


//SR.No 	Name 	Email 	Phone Number 	Date 	Message 	Status


// SR.No 	Name 	Email 	Phone Number 	Date
