package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import models.Bank;
import models.Complaints;
import models.Country;
import models.Customer;
import models.Feedback;
import models.Merchant;
import models.Product;
import models.Transaction;
import play.data.Form;
import play.db.ebean.Model;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.Way2SMS;
import views.html.*;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Application extends Controller {

    public static Result index() {
    	if (session("userType") != null && session("userType").equalsIgnoreCase("customer"))
    	{
    		return ok(customerPage.render(""));	
    	}
    	else
    	if (session("userType") != null && session("userType").equalsIgnoreCase("admin"))
    	{
 		   return ok(dashboardMetro.render("Admin"));    		
    	}
    	else
    	if (session("userType") != null && session("userType").equalsIgnoreCase("merchant"))
    	{
 		   return ok(dashboardMetro.render("Merchant"));    		
    	}
    	else
    	{
    		return ok(index.render(""));	
    	}
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
				//return redirect(routes.Application.index())
				return ok(customerPage.render(custUser.first_name));
			}		
		}
		return ok(index.render("Authentication failed.!"));
	}

    public static Result logOut(){
    	session().clear();
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

	public static Result getAllProducts(){
		List<Product> listProduct = new Model.Finder(String.class, Product.class).all(); 
		return ok(Json.toJson(listProduct));
	}

	public static Result getAllBanks(){
  	//List<Transaction> listUser = new Model.Finder(String.class, Transaction.class).all();
		List<Bank> listBank = new Model.Finder(String.class, Bank.class).all(); 
		return ok(Json.toJson(listBank));
	}
	
	public static Result getAllCountries(){
			List<Country> listCountry = new Model.Finder(String.class, Country.class).all(); 
			return ok(Json.toJson(listCountry));
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


public static Result fetchDashboardStat()
	{
		Date todaysDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(todaysDate); 
		
		formatter.applyPattern("w"); 
		String formattedWeek = formatter.format(todaysDate);
		
		formatter.applyPattern("MM");
		String formattedMonth = formatter.format(todaysDate);
		
		formatter.applyPattern("yyyy");
		String formattedYear = formatter.format(todaysDate); 
		
		System.out.println("=====================================================================");
		System.out.println(formattedWeek );
		System.out.println(formattedMonth); 
		System.out.println(formattedYear); 
		
		if (session("userType") == null)
		{
    		return ok(index.render(""));
		}
		
		ObjectNode result = Json.newObject(); 
		
		if (session("userType").equals("merchant"))
		{
			Merchant loggedMerchant = (Merchant) Merchant.find.where().eq("email", session("email").toString()).findUnique(); 
//			ExpressionList<Transaction> a = Transaction.find.where().eq("merchant", loggedMerchant);

			
			result.put("successTran", Transaction.find.where().eq("status", "success").findRowCount());
			result.put("pendingTran", Transaction.find.where().eq("status", "pending").findRowCount());
			result.put("unsuccessTran", Transaction.find.where().eq("status", "unsuccess").findRowCount());
			 
			result.put("successToday", Transaction.find.where().eq("status", "success").eq("Date(date)", formattedDate).findRowCount());
			result.put("successWeek" , Transaction.find.where().eq("status", "success").eq("Week(date)", formattedMonth).eq("Year(date)", formattedYear).findRowCount());
			result.put("successMonth", Transaction.find.where().eq("status", "success").eq("Month(date)", formattedMonth).eq("Year(date)", formattedYear).findRowCount());
			result.put("successYear" , Transaction.find.where().eq("status", "success").eq("Year(date)", formattedYear).eq("Year(date)", formattedYear).findRowCount());

			result.put("pendingToday", Transaction.find.where().eq("status", "pending").eq("Date(date)", formattedDate).findRowCount());
			result.put("pendingWeek" , Transaction.find.where().eq("status", "pending").eq("Week(date)", formattedWeek).eq("Year(date)", formattedYear).findRowCount());
			result.put("pendingMonth", Transaction.find.where().eq("status", "pending").eq("Month(date)", formattedMonth).eq("Year(date)", formattedYear).findRowCount());
			result.put("pendingYear" , Transaction.find.where().eq("status", "pending").eq("Year(date)", formattedYear).eq("Year(date)", formattedYear).findRowCount());

			result.put("unsuccessToday", Transaction.find.where().eq("status", "unsuccess").eq("Date(date)", formattedDate).findRowCount());
			result.put("unsuccessWeek" , Transaction.find.where().eq("status", "unsuccess").eq("Week(date)", formattedWeek).eq("Year(date)", formattedYear).findRowCount());
			result.put("unsuccessMonth", Transaction.find.where().eq("status", "unsuccess").eq("Month(date)", formattedMonth).eq("Year(date)", formattedYear).findRowCount());
			result.put("unsuccessYear" , Transaction.find.where().eq("status", "unsuccess").eq("Year(date)", formattedYear).eq("Year(date)", formattedYear).findRowCount());			
			
			result.put("unseenTranCount", Transaction.find.where().eq("merchant", loggedMerchant).eq("merchantRead", 1).findRowCount());
			result.put("unseenNotificationCount", Feedback.find.where().eq("merchant", loggedMerchant).eq("merchantRead", 1).findRowCount());					
		
		}
		else
		if (session("userType").equals("admin"))
		{
			
			result.put("successTran", Transaction.find.where().eq("status", "success").findRowCount());
			result.put("pendingTran", Transaction.find.where().eq("status", "pending").findRowCount());
			result.put("unsuccessTran", Transaction.find.where().eq("status", "unsuccess").findRowCount());
			 
			result.put("successToday", Transaction.find.where().eq("status", "success").eq("Date(date)", formattedDate).findRowCount());
			result.put("successWeek" , Transaction.find.where().eq("status", "success").eq("Week(date)", formattedWeek).findRowCount());
			result.put("successMonth", Transaction.find.where().eq("status", "success").eq("Month(date)", formattedMonth).findRowCount());
			result.put("successYear" , Transaction.find.where().eq("status", "success").eq("Year(date)", formattedYear).findRowCount());

			result.put("pendingToday", Transaction.find.where().eq("status", "pending").eq("Date(date)", formattedDate).findRowCount());
			result.put("pendingWeek" , Transaction.find.where().eq("status", "pending").eq("Week(date)", formattedWeek).findRowCount());
			result.put("pendingMonth", Transaction.find.where().eq("status", "pending").eq("Month(date)", formattedMonth).findRowCount());
			result.put("pendingYear" , Transaction.find.where().eq("status", "pending").eq("Year(date)", formattedYear).findRowCount());

			result.put("unsuccessToday", Transaction.find.where().eq("status", "unsuccess").eq("Date(date)", formattedDate).findRowCount());
			result.put("unsuccessWeek" , Transaction.find.where().eq("status", "unsuccess").eq("Week(date)", formattedWeek).findRowCount());
			result.put("unsuccessMonth", Transaction.find.where().eq("status", "unsuccess").eq("Month(date)", formattedMonth).findRowCount());
			result.put("unsuccessYear" , Transaction.find.where().eq("status", "unsuccess").eq("Year(date)", formattedYear).findRowCount());
		
			
			result.put("unseenTranCount", Transaction.find.where().eq("adminRead", 1).findRowCount());
			result.put("unseenNotificationCount", Feedback.find.where().eq("adminRead", 1).findRowCount());
			
		}
		
		
		
		return ok(result);
	}
	
/////// Comet
  /***
   * 
   
	final static ActorRef clock = Clock.instance;
	
 	public static Result liveClock()
	{
		return ok(new Comet("parent.clockChanged") {  
            public void onConnected() {
               clock.tell(this, null); 
            } 
        });
	}


  
    public static class Clock extends UntypedActor {
		
        
        static ActorRef instance = Akka.system().actorOf(Props.create(Clock.class));
        
        // Send a TICK message every 100 millis
        static {
            Akka.system().scheduler().schedule(
                Duration.Zero(),
                Duration.create(100, MILLISECONDS),
                instance, "TICK",  Akka.system().dispatcher(),
                // sender // null
            );
        }
        
        List<Comet> sockets = new ArrayList<Comet>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH mm ss");
        
        public void onReceive(Object message) {

            // Handle connections
            if(message instanceof Comet) {
                final Comet cometSocket = (Comet)message;
                
                if(sockets.contains(cometSocket)) {
                    
                    // Brower is disconnected
                    sockets.remove(cometSocket);
                    Logger.info("Browser disconnected (" + sockets.size() + " browsers currently connected)");
                    
                } else {
                    
                    // Register disconnected callback 
                    cometSocket.onDisconnected(new Callback0() {
                        public void invoke() {
                            getContext().self().tell(cometSocket, null);
                        }
                    });
                    
                    // New browser connected
                    sockets.add(cometSocket);
                    Logger.info("New browser connected (" + sockets.size() + " browsers currently connected)");
                    
                }
            } 
            
            // Tick, send time to all connected browsers
            if("TICK".equals(message)) {
                
                // Send the current time to all comet sockets
                List<Comet> shallowCopy = new ArrayList<Comet>(sockets); //prevent ConcurrentModificationException
                for(Comet cometSocket: shallowCopy) {
                    cometSocket.sendMessage(dateFormat.format(new Date()));
                }
                
            }

        }
    }
  
*/

	
	public static Result fetchTransactionTable(String status)
	{
		List<Transaction>transactionList;
		ExpressionList<Transaction>tranExpantionList = Transaction.find.where();

		if (session("userType") != null && session("userType").equals("merchant"))
		{
			Merchant loggedMerchant = Merchant.find.where().eq("email", session("email")).findUnique(); 
			tranExpantionList = Transaction.find.where().eq("merchant", loggedMerchant);
		}
		
		if (!status.equalsIgnoreCase("all")){
			tranExpantionList.eq("status", status);
		}
		
		transactionList = tranExpantionList.findList();
		
		return ok(Json.toJson(transactionList));
	}
		
	public static Result fetchCustomerFeedback()
	{
		List<Feedback>feedbackList;
		if (session("userType") != null && session("userType").equals("merchant"))
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
		if (session("userType") != null && session("userType").equals("merchant"))
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
