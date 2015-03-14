package controllers;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import models.Bank;
import models.Country;
import models.Customer;
import models.Merchant;
import models.Product;
import models.Transaction;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.KMeansAlgo;
import util.Way2SMS;
import views.html.customerPage;
import views.html.index;
//import util.KMeansAlgo;

public class CustomerController extends Controller{

	public static Result customerPage()
	{
		
		return ok(customerPage.render(""));
	}
		

	  public static Result buyProductList() {
		if (session("userType").equals("customer"))
		{
			List<Product> listProduct = Product.find.findList();
			return ok(Json.toJson(listProduct));
		}
		else
		{
		  return ok(index.render(""));
		}
	  }
	
	  public static Result buyMerchantList() {
		if (session("userType").equals("customer"))
		{
			List<Merchant> listMerchant = Merchant.find.findList();
			return ok(Json.toJson(listMerchant));
		}
		else
		{
		  return ok(index.render(""));
		}
	  }

	  
	  public static Result buyBankList() {
		if (session("userType").equals("customer"))
		{
			List<Bank> listBank = Bank.find.findList();
			return ok(Json.toJson(listBank));
		}
		else
		{
		  return ok(index.render(""));
		}
	  }
	
	  public static Result proceedToPay(String payMode, Integer payBankId, Integer payMerchantId, Integer payProductId, Long payCountryId)
	  {
		  boolean cleanStatus = false;
		  String description = new String();
		  String status = new String();
		  String returnStatus = new String();
		  
		  Calendar cal = Calendar.getInstance();
		  Bank bank = Bank.find.where().eq("id", payBankId).findUnique(); 
		  Product product = Product.find.where().eq("id", payProductId).findUnique();
		  Merchant merchant = Merchant.find.where().eq("id", payMerchantId).findUnique();
		  Country country = Country.find.where().eq("id", payCountryId).findUnique();
		  
		  
		  if (session("email") == null)
		  {
			  System.out.println("Session Fail..");
			  // Check 1 	: 	transaction will be unsuccessful on expire of session
			  // status 	:	Unsuccess
			  //
			  Transaction newTransaction = new Transaction();
			  newTransaction.amount = product.amount;
			  newTransaction.buyingLocation = country;
			  newTransaction.bank = bank;
			  newTransaction.customer = null;
			  newTransaction.description = "Session Expired..!";
			  newTransaction.status = "unsuccess";
			  newTransaction.date = new Date();
			  newTransaction.adminRead = false;
			  newTransaction.merchantRead = false;
			  newTransaction.product = product;
			  newTransaction.merchant = merchant;
			  newTransaction.save();
			  
			  return ok("FAIL");
		  }

		  Customer loggedCustomer = (Customer) Customer.find.where().eq("email", session("email").toString()).findUnique(); 
		  List<Transaction>prevTransactionList = Transaction.find.where().eq("customer", loggedCustomer).findList();

		  Transaction newTransaction = new Transaction();

		  double productAmt = Product.find.where().eq("id", payProductId).findList().get(0).amount;		  
		  if (prevTransactionList.size() == 0 && productAmt >= 20000)
		  {
			  //	check	2	: First time buying amount must be above than 20,000 
			  // 	status 		:	Pending 
			  //
			  System.out.println("Case 2");
			  description = "First Transaction amount Greater than 20000..!";
			  status  =  "pending";
			  returnStatus = "OTP";
		  }
		  else
		  if (prevTransactionList.size() >= 1)
		  {
			  System.out.println("Case 4");
			  for (Transaction transaction : prevTransactionList) 
			  {
				  if (transaction.buyingLocation.id != payCountryId){
				  //	check	3	:  Buying location is different from previous location (country)
				  // 	status 		:  Pending 
				  //
					  description = "Buying locaion is different..!";
					  status  =  "pending";
					  returnStatus = "OTP"; 
				  }  
			  }
		  }
		  else
		  {
			  //	check	4	:  Validate amount of product by clustering, by using K-mean Algorithm
			  // 	status 		:  Pending 
			  //
			  int elementArray[] = new int[prevTransactionList.size()];
			  for (int i = 0; i < prevTransactionList.size(); i++) {
				  elementArray[i] = (int) prevTransactionList.get(i).amount;
			  }
			  boolean isValid;
			  //KMeansAlgo.CheckKMeans(elementArray, 4, newElement, distance);			  
			  isValid = KMeansAlgo.CheckKMeans(elementArray, 4, (int)product.amount, 5000);
			  if (isValid == false)
			  {
				  description = "Possiblity of Fraud detected by K-Means Algorithm..!";
				  status  =  "pending";
				  returnStatus = "OTP"; 
			  }
			  else
			  {
				  description = "No Need to verify";
				  status  =  "success";
				  returnStatus = "OK";
			  }
		  }

		  

		  if (returnStatus.equals(""))
		  {
			  description = "No Need to verify";
			  status  =  "success";
			  returnStatus = "OK";
		  }
		  
		  Way2SMS wayTomsg = new Way2SMS();		  
		  if (returnStatus.equalsIgnoreCase("OK"))
		  {
			  wayTomsg = new Way2SMS();
			  wayTomsg.sendSMS(loggedCustomer.contact, "Transaction is successful..");
		  }
		  else
		  if (returnStatus.equalsIgnoreCase("OTP"))
		  {
			  System.out.println("HERRRRRRRRRR............");

				newTransaction.OTP = randomString(5);
				newTransaction.optSendDate = new Date();
				
				// send sms
				wayTomsg = new Way2SMS();
				wayTomsg.sendSMS(loggedCustomer.contact, "Your OTP is " + newTransaction.OTP);
				System.out.println("Your OTP is " + newTransaction.OTP);
		  }
		  
		  
		  newTransaction.amount = product.amount;
		  newTransaction.buyingLocation = country;
		  newTransaction.bank = bank;
		  newTransaction.customer = loggedCustomer;
		  newTransaction.description = description;
		  newTransaction.status = status;
		  newTransaction.date = new Date();
		  newTransaction.adminRead = false;
		  newTransaction.merchantRead = false;
		  newTransaction.merchant = merchant;
		  newTransaction.product = product;
		  newTransaction.save();
	
	
		  returnStatus = returnStatus + "-"+ String.valueOf(newTransaction.id);
		  System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + payMode);
		  System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + payBankId);
		  System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + payMerchantId);
		  System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" + payProductId);
  	  
		  return ok(returnStatus);
	  }

	@SuppressWarnings("unused")
	public static Result submitOPT(String otp, Integer tranId, Integer trialCount)
	  {
		  Transaction transaction = Transaction.find.where().eq("id", tranId).findUnique();//.eq("OTP", otp).findUnique();

		  System.out.println("trascaction status : " + transaction.OTP.equals(otp));
		  
		  if (transaction == null)
		  {
			  return ok("FAIL");
		  }
		  else
		  if (!transaction.OTP.equals(otp) && trialCount == 3)
		  {
			  transaction.status = "unsuccess";
			  return ok("FAIL");
		  }
		  else
	      if (transaction.OTP.equals(otp))
		  {
			  transaction.status = "success";
			  return ok("<p>Merchant verified your transaction .</p>" +
				  		 "<form action=\"https://secure-test.worldpay.com/wcc/purchase\" method=POST>" +
				  		 "<input type=hidden name=\"testMode\" value=100>" +
				  		 "<input type=submit value=Continue To Pay>" +
				  		 "<input type=hidden name=name value=AUTHORISED>" + 
				  		"</form>");
			  
			  
			  //return ok("<img src=\"https://www.paypalobjects.com/webstatic/en_US/i/buttons/cc-badges-ppppcmcvdam.png\" alt=\"Pay with PayPal, PayPal Credit or any major credit card\" >");
//			  return ok("<img src=\"https://www.paypalobjects.com/webstatic/en_US/i/buttons/cc-badges-ppppcmcvdam.png\" alt=\"Pay with PayPal, PayPal Credit or any major credit card\" />");
			  
		  }
	      else
	      {
			  return ok("TRY");
	      }
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


}
