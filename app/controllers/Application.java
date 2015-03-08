package controllers;

import java.util.List;
import java.util.Random;

import models.Complaints;
import models.Customer;
import models.Feedback;
import models.Merchant;
import models.Transaction;
import play.data.Form;
import play.db.ebean.Model;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.Way2SMS;
import views.html.*;

import com.avaje.ebean.ExpressionList;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(""));
    }

	public static Result authenticateUser() {
		
		String userName = Form.form().bindFromRequest().get("email");
		String password = Form.form().bindFromRequest().get("password");
		if (userName.equals("admin@gmail.com") && password.equals("123456"))
		{
		   session("connected", userName);
		   session("userName", "Admin");
		   session("userType", "admin");
		   session("email", "admin@gmail.com");
		   return ok(dashboardMetro.render("Admin"));
		}
		else
		{
			Merchant  merUser = Merchant.find.where().eq("email", userName).eq("password", password).findUnique();
		    if (merUser != null)
		    {
			   session("connected", userName);
			   session("userName", merUser.full_name);
			   session("userType", "merchant");
			   session("email", merUser.email);
			   return ok(dashboardMetro.render(merUser.full_name));
		    }
		    
			System.out.println("user");
		    Customer  custUser = Customer.find.where().eq("email", userName).eq("password", password).findUnique();
			if (custUser != null)
			{
				session("userType", "customer");
				session("connected", custUser.first_name);
				session("userName", custUser.first_name);
				session("email", custUser.email);
				//return redirect("/customerPage");
				return ok(customerPage.render(custUser.first_name));
			}		
		}
		return ok(index.render("Authentication failed.!"));
	}

    public static Result logOut(){
    	session("connected", "logout");
		System.out.println(session("connected"));
    	return redirect(routes.Application.index());
	}

	public static Result addCustomerUser()
    {
    	System.out.println("saved sucessfuly");
		Customer newUser =  new Customer();
		newUser.first_name = Form.form().bindFromRequest().get("first_name");
		newUser.last_name = Form.form().bindFromRequest().get("last_name");
		newUser.contact = Form.form().bindFromRequest().get("contact");
		newUser.email =	Form.form().bindFromRequest().get("email");
		newUser.password =  Form.form().bindFromRequest().get("password");

    	Way2SMS wayTomsg = new Way2SMS();
    	wayTomsg.sendSMS(newUser.contact, "Thank you for registraction");
		newUser.save();
    	System.out.println("saved sucessfuly");
    	return redirect(routes.Application.index());
    }

	
	
    public static Result forgotPassword(){
    	String contact = Form.form().bindFromRequest().get("contact");
		if (contact != null)
		{
			Customer cusUser = Customer.find.where().eq("contact", contact).findUnique();
			if (cusUser == null)
			{
				Merchant marUser = Merchant.find.where().eq("contact", contact).findUnique();
				if (marUser == null)
				{
					return ok(index.render(contact + " not exists in database..!"));
				}
			}
			}
		return ok(index.render("Mobile not exists in Database"));
    }



///////	Merchant
	
	public static Result addMerchant(String fullName, String busName, String contact, String email, String address){
		Merchant newMeachant = new Merchant();
		newMeachant.address = address;
		newMeachant.business_name = busName;
		newMeachant.contact = contact;
		newMeachant.email = email;
		newMeachant.full_name = fullName;
		newMeachant.password = randomString(5);

		// send sms
		Way2SMS wayTomsg = new Way2SMS();
    	wayTomsg.sendSMS(newMeachant.contact, "Your password : " + newMeachant.password + "- CRM");

    	newMeachant.save();
		return ok("SuccessFully Saved..!!");
	}
	
	
	public static Result getAllMerchants(){
  	//List<Transaction> listUser = new Model.Finder(String.class, Transaction.class).all();
		List<Merchant> listMerchant = new Model.Finder(String.class, Merchant.class).all(); 
		return ok(Json.toJson(listMerchant));
  }


	public static String randomString( int len ) 
	{
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
			
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		return sb.toString();
	}


	public static Result fetchTransactionTable(String status)
	{
		List<Transaction>transactionList;
		if (session("userType").equals("merchant"))
		{
			ExpressionList<Transaction>tranExpantionList;
			Merchant loggedMerchant = Merchant.find.where().eq("email", session("email")).findUnique(); 
			tranExpantionList = Transaction.find.where().eq("merchant", loggedMerchant);

			System.out.println("status =========================================== v" + status);

			if (!status.equalsIgnoreCase("all")){
				tranExpantionList.eq("status", status);
			}
			transactionList = tranExpantionList.findList();
		}
		else
		{
			transactionList = Transaction.find.all();
		}
		
		return ok(Json.toJson(transactionList));
	}
		
	public static Result fetchCustomerFeedback()
	{
		List<Feedback>feedbackList;
		if (session("userType").equals("merchant"))
		{
			ExpressionList<Feedback>expantionList;
			Merchant loggedMerchant = Merchant.find.where().eq("email", session("email")).findUnique(); 
			expantionList = Feedback.find.where().eq("merchant", loggedMerchant);
			feedbackList = expantionList.orderBy("date desc").findList();
		}
		else
		{
			feedbackList = Feedback.find.orderBy("date desc").findList();
		}
		
		return ok(Json.toJson(feedbackList));
	}

	public static Result fetchComplaints()
	{
		List<Complaints>ComplaintsList;
		if (session("userType").equals("merchant"))
		{
			ExpressionList<Complaints>expantionList;
			Merchant loggedMerchant = Merchant.find.where().eq("email", session("email")).findUnique(); 
			expantionList = Complaints.find.where().eq("merchant", loggedMerchant);
			ComplaintsList = expantionList.orderBy("date desc").findList();
		}
		else
		{
			ComplaintsList = Complaints.find.orderBy("date desc").findList();
		}
		
		return ok(Json.toJson(ComplaintsList));
	}
}
