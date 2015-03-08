
$(function() {
    console.log( "ready!" );
	$('.crm-main-content').hide();
	$('#page-genstats').show();
	
	$('#menu-dashboard').click(function(){
		$('.crm-main-content').hide();
		$('#page-genstats').show();
	});
	
	
	$('#menu-home-crm').click(function(){
		$('.crm-main-content').hide();
		$('#page-genstats').show();
	});
	
	$('#menu-newmerchant').click(function(){
		$('.crm-main-content').hide();
		$('#page-newMerchant').show();
	});
	
	$('#menu-viewmerchant').click(function(){
		
			$.ajax({
				url: "fetchAllMerchant",
				type: "GET",
				dataType: "json",
				success: function(merchantArray){

					var merchantTableBoiler = '<table class="table table-striped table-bordered" id="sample_1">'+
												'<thead>'+
												'<tr>'+
													'<th style="width:8px;">SR.No</th>'+
													'<th>Merchant Name</th>'+
													'<th class="hidden-phone">Business Name</th>'+
													'<th class="hidden-phone">Contact</th>'+
													'<th class="hidden-phone">Email</th>'+
													'<th class="hidden-phone">Address</th>'+
													'<th class="hidden-phone">Action</th>'+
												'</tr>'+
												'</thead>'+
												'<tbody>';
												
					for (var i = 0; i < merchantArray.length; i++)
					{
						var row = '<tr class="odd gradeX">'+
													'<td>'+ i +'</td>'+				
													'<td><a href="#myModal1" data-toggle="modal" role="button">'+ merchantArray[i]["full_name"] +'</a></td>'+
													'<td class="hidden-phone">' + merchantArray[i]["business_name"] +'</td>'+
													'<td class="hidden-phone">'+ merchantArray[i]["contact"] +'</td>'+
													'<td class="center hidden-phone">'+ merchantArray[i]["email"] +'</td>'+
													'<td class="center hidden-phone">'+ merchantArray[i]["address"]+'</td>'+
													'<td class="hidden-phone"><button class="btn btn-mini btn-primary"><i class="icon-pencil icon-white"></i> Edit</button>  <button class="btn btn-mini btn-danger"><i class="icon-remove icon-white"></i> Delete</button>  <button class="btn btn-mini"><i class="icon-eye-open"></i> View</button></td>'+
												'</tr>';
						merchantTableBoiler += row;
					}
					
					merchantTableBoiler += '<tbody></table>';
					$('#viewMerchantContent').html(merchantTableBoiler);
				},
				error : function(){
					alert("Failed to add merchant.!!");
				}
			});

		$('.crm-main-content').hide();
		$('#page-viewMerchant').show();
	});


///// BEGIN flot-chart 
	
	$('#menu-flot-chart-today').click(function(){
		$('.crm-main-content').hide();
		$('#page-flot-chart-today').show();
	});
	
	$('#menu-flot-chart-week').click(function(){
		$('.crm-main-content').hide();
		$('#page-flot-chart-week').show();
	});
	
	$('#menu-flot-chart-month').click(function(){
		$('.crm-main-content').hide();
		$('#page-flot-chart-month').show();
	});
	
	$('#menu-flot-chart-year').click(function(){
		$('.crm-main-content').hide();
		$('#page-flot-chart-year').show();
	});


///// END flot-chart 
	
	
///////	Table - Today


	$('#menu-table-total').click(function(){
		
		$("#pageTitle").html("Total Transaction");
		$("#pagePath-1").html("Home");
		$("#pagePath-2").html("Total");
		$('#tranStatusType').val("all");
		$('#Tran-TableHead').html("Transaction Details");
		
		$('.crm-main-content').hide();
		fetchTransactionTable();
		$('#page-TransactionTable').show();
	});

	$('#menu-table-success').click(function(){
		
		$("#pageTitle").html("Successful Transaction");
		$("#pagePath-1").html("Home");
		$("#pagePath-2").html("Sucess");
		$('#tranStatusType').val("success");
		$('#Tran-TableHead').html("Transaction Details");
		
		$('.crm-main-content').hide();
		fetchTransactionTable();
		$('#page-TransactionTable').show();
	});

	$('#menu-table-pending').click(function(){
		
		$("#pageTitle").html("Pending Transaction");
		$("#pagePath-1").html("Home");
		$("#pagePath-2").html("Pending");
		$('#tranStatusType').val("pending");
		$('#Tran-TableHead').html("Transaction Details");
		
		$('.crm-main-content').hide();
		fetchTransactionTable();
		$('#page-TransactionTable').show();
	});

	$('#menu-table-unsuccess').click(function(){
		
		$("#pageTitle").html("Unsucessful Transaction");
		$("#pagePath-1").html("Home");
		$("#pagePath-2").html("Unsuccessful");
		$('#tranStatusType').val("unsuccess");
		$('#Tran-TableHead').html("Transaction Details");
		
		$('.crm-main-content').hide();

		fetchTransactionTable();
		$('#page-TransactionTable').show();
	});

	 $('#appendedInputButton').on('keypress', function (event) {
         if(event.which === 13){
			fetchTransactionTable();
         }
   });
	
	
	$('#menu-notification-feedback').click(function(){
		$('.crm-main-content').hide();
		$("#pageTitle").html("Feedback");
		$("#pagePath-1").html("Home");
		$("#pagePath-2").html("feedback");

		fetchCustomerFeedback();
	
		$('#page-TransactionTable').show();

	});	
	
	$('#menu-notification-complaints').click(function(){
		$('.crm-main-content').hide();
		$('#page-Complaints').show();
	});
	 
	
/////		Add New Merchant
	$('#btnSubmitNewMerchant').click(function(){
		
		var fullName = $('#newm-fullname').val();
		var busName = $('#newm-businessname').val(); 
		var contact = $('#newm-contact').val();
		var email = $('#newm-email').val();
		var address = $('#newm-address').val();

		var query = 'fullName=' + fullName + '&busName='+ busName + '&contact=' + contact + '&email=' + email + '&address=' + address;

		$.ajax({
			url: "addNewMerchant",
			type: "GET",
			data: query,
			success: function(){
				$('#newm-fullname').val("");
				$('#newm-businessname').val(""); 
				$('#newm-contact').val("");
				$('#newm-email').val("");
				$('#newm-address').val("");					
				$('#myModal2 .modal-body').html('<p>Merchant added successfully \n SMS will delivered shortly.!! </p>');
			},
			error : function(){
				$('#myModal2 .modal-body').html('<p>Failed to add merchant.!! </p>');
			}
		});
	})

		
///////////////      fetching Data
	
	function fetchTransactionTable()
	{
		var status = $('#tranStatusType').val();
		var query = 'status=' + status;
		
	
		$.ajax({
				url: "fetchTransactionTable",
				type: "GET",
				data: query,
				success: function(tranArray){
					var tab = '<table class="table table-striped table-bordered">'+
										'<tr>'+
											'<td>SR.No</td>'+
											'<td>Product Name</td>'+
											'<td>Transaction ID</td>'+
											'<td>Amount</td>'+
											'<td>Date</td>'+
											'<td>Customer Name</td>';
								if (status == "all")
								{
									tab += '<td>Status</td>';
								}							
							tab += '</tr>';

							for (var i = 0; i < tranArray.length; i++)
							{
								var row = tranArray[i];
								tab += '<tr>'+
												'<td>'+ (i + 1) +'</td>'+
												'<td>'+ row.product.name +'</td>'+
												'<td>'+ row.transationId +'</td>'+
												'<td>'+ row.amount +'</td>'+
												'<td>'+ row.date +'</td>'+
												'<td>'+ row.customer.Customername +'</td>';
	
								if (status == "all")
								{
									if  (row.status == "pending")
									{
										tab += '<td class="hidden-phone"><span class="btn  btn-danger">Pending</span></td>';
									}
									else if (row.status == "success")
									{
										tab += '<td class="hidden-phone"><span class="btn  btn-success">Completed</span></td>';
                            		}
									else if (row.status == "unsuccess")
									{
										tab += '<td class="hidden-phone"><span class="btn  btn-success">Completed</span></td>';
                            		}	
								}			
								tab += '</tr>';
							}
							
//[{"transationId":1,"date":null,"amount":1000.0,"status":"pending","  ":{"id":10,"Customername":null,"email":"abc1@gmail.com","name":"abcd","password":"123456"},"product":{"id":21,"name":"Pepsi1","type":"COD","amount":1.0},"merchant":{"id":5,"full_name":"ABC XYZ1","business_name":"Shri ganesh 2","contact":"98756451","email":null,"website":null,"address":"Address 3","faxno":"FAX-1234561","rank":null,"password":"123456"}},{"transationId":2,"date":null,"amount":1000.0,"status":"success","customer":{"id":11,"Customername":null,"email":"abc2@gmail.com","name":"abcde","password":"123456"},"product":{"id":22,"name":"Pepsi2","type":"COD","amount":10.0},"merchant":{"id":6,"full_name":"ABC XYZ1","business_name":"Shri ganesh 3","contact":"98756451","email":null,"website":null,"address":"Address 4","faxno":"FAX-1234561","rank":null,"password":"123456"}},{"transationId":3,"date":null,"amount":1000.0,"status":"unsuccess","customer":{"id":12,"Customername":null,"email":"abc3@gmail.com","name":"abcdef","password":"123456"},"product":{"id":23,"name":"Pepsi3","type":"COD","amount":100.0},"merchant":{"id":7,"full_name":"ABC XYZ1","business_name":"Shri ganesh 4","contact":"98756451","email":null,"website":null,"address":"Address 5","faxno":"FAX-1234561","rank":null,"password":"123456"}},{"transationId":4,"date":null,"amount":1000.0,"status":"success","customer":{"id":13,"Customername":null,"email":"abc4@gmail.com","name":"abcdefg","password":"123456"},"product":{"id":24,"name":"Pepsi4","type":"COD","amount":1000.0},"merchant":{"id":8,"full_name":"ABC XYZ1","business_name":"Shri ganesh 5","contact":"98756451","email":null,"website":null,"address":"Address 6","faxno":"FAX-1234561","rank":null,"password":"123456"}}]
							
						tab += '</table>';
						$('#TransactionTable').html(tab);
				},
				error : function(){
					alert("Failed to add merchant.!!");
				}
			});
		
	}
	
	function fetchCustomerFeedback()
	{
		var status = $('#tranStatusType').val();
		var query = 'status=' + status;

		$.ajax({
				url: "fetchCustomerFeedback",
				type: "GET",
				data: query,
				success: function(tranArray){

					var tab = '<table class="table table-striped table-bordered">'+
								'<tr>'+
									'<th style="width:8px;">SR.No</th>'+
									'<th>Customer Name</th>'+
									'<th class="hidden-phone">Email</th>'+
									'<th class="hidden-phone">Phone Number</th>'+
									'<th class="hidden-phone">Date</th>'+
									'<th class="hidden-phone"></th>';
								'</tr>';

								if (status == "all")
								{
									tab += '<td>Status</td>';
								}							
							tab += '</tr>';

							for (var i = 0; i < tranArray.length; i++)
							{
								var row = tranArray[i];
								tab += '<tr>'+
												'<td>'+ (i + 1) +'</td>'+
												'<td>'+ row.product.name +'</td>'+
												'<td>'+ row.transationId +'</td>'+
-												'<td>'+ row.amount +'</td>'+
												'<td>'+ row.date +'</td>'+
												'<td>'+ row.customer.Customername +'</td>';
	
								if (status == "all")
								{
									if  (row.status == "pending")
									{
										tab += '<td class="hidden-phone"><span class="btn  btn-danger">Pending</span></td>';
									}
									else if (row.status == "success")
									{
										tab += '<td class="hidden-phone"><span class="btn  btn-success">Completed</span></td>';
                            		}
									else if (row.status == "unsuccess")
									{
										tab += '<td class="hidden-phone"><span class="btn  btn-success">Completed</span></td>';
                            		}	
								}			
								tab += '</tr>';
							}
							
//[{"transationId":1,"date":null,"amount":1000.0,"status":"pending","  ":{"id":10,"Customername":null,"email":"abc1@gmail.com","name":"abcd","password":"123456"},"product":{"id":21,"name":"Pepsi1","type":"COD","amount":1.0},"merchant":{"id":5,"full_name":"ABC XYZ1","business_name":"Shri ganesh 2","contact":"98756451","email":null,"website":null,"address":"Address 3","faxno":"FAX-1234561","rank":null,"password":"123456"}},{"transationId":2,"date":null,"amount":1000.0,"status":"success","customer":{"id":11,"Customername":null,"email":"abc2@gmail.com","name":"abcde","password":"123456"},"product":{"id":22,"name":"Pepsi2","type":"COD","amount":10.0},"merchant":{"id":6,"full_name":"ABC XYZ1","business_name":"Shri ganesh 3","contact":"98756451","email":null,"website":null,"address":"Address 4","faxno":"FAX-1234561","rank":null,"password":"123456"}},{"transationId":3,"date":null,"amount":1000.0,"status":"unsuccess","customer":{"id":12,"Customername":null,"email":"abc3@gmail.com","name":"abcdef","password":"123456"},"product":{"id":23,"name":"Pepsi3","type":"COD","amount":100.0},"merchant":{"id":7,"full_name":"ABC XYZ1","business_name":"Shri ganesh 4","contact":"98756451","email":null,"website":null,"address":"Address 5","faxno":"FAX-1234561","rank":null,"password":"123456"}},{"transationId":4,"date":null,"amount":1000.0,"status":"success","customer":{"id":13,"Customername":null,"email":"abc4@gmail.com","name":"abcdefg","password":"123456"},"product":{"id":24,"name":"Pepsi4","type":"COD","amount":1000.0},"merchant":{"id":8,"full_name":"ABC XYZ1","business_name":"Shri ganesh 5","contact":"98756451","email":null,"website":null,"address":"Address 6","faxno":"FAX-1234561","rank":null,"password":"123456"}}]
							
						tab += '</table>';
						$('#TransactionTable').html(tab);
				},
				error : function(){
					alert("Failed to add merchant.!!");
				}
			});
		
	}
	
});




