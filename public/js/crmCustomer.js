

$(function() {
    console.log( "ready!" );
	
	$('#menu-dashboard').click(function(){
		$('.crm-main-content').hide();
		$('#page-newTransaction').show();
	});

	$('#menu-TranHistory').click(function(){
		$('.crm-main-content').hide();
		$('#page-viewTransaction').show();
	});

	
	$('#menu-addComplaints').click(function(){
		$('.crm-main-content').hide();
		$('#page-addComplaints').show();
	});
	
	$('#menu-addFeedback').click(function(){
		$('.crm-main-content').hide();
		$('#page-addFeedback').show();
	});

	

	$('#btnProceedToPay').click(function(){
		var payMode = $('#cmbPaymentMode').val();
		var payBankId = $('#cmbBank').val();
		var payMerchantId = $('#cmbMerchant').val();
		var payProductId = $('#cmbProduct').val();
		var payCountryId = $('#cmbAccessLocation').val();
		var payAddress = $('#txtAddress').val()
		
		var query = 'payMode=' + payMode + '&payBankId=' + payBankId + '&payMerchantId=' + payMerchantId + '&payProductId=' + payProductId + '&payCountryId='+ payCountryId;
		
		$.ajax({
				url: "proceedToPay",
				type: "GET",
				data: query,
				dataType: "text",
				success: function(statusTranId){					
					$('#cmbPaymentMode').val(0);
					$('#cmbBank').val(0);
					$('#cmbMerchant').val(0);
					$('#cmbProduct').val(0);
					$('#cmbAccessLocation').val(0);
					$('#txtAddress').val("");
		
					var res = statusTranId.split("-"); 
					alert(res.length);
					if (res.length != 2)
					{
						$('#myModalPayMessage .modal-body').html('<p> Transaction if failed..!!\n Please login and try again.</p>');
						return;
					}
					
					if (res[0] == "FAIL")
					{
						$('#myModalPayMessage .modal-body').html('<p> Transaction if failed..!!\n Please login and try again.</p>');
					}
					else
					if (res[0] == "OK")
					{
						$('#myModalPayMessage .modal-body').html('<p> Transaction is verified by Merchant.</p>');
					}
					else
					if (res[0] == "OTP")
					{
						$('#myModalPayMessage .modal-body').html('<p> Merchant needs to verify this transaction\n The OPT has been sent to your mobile.</p> '+
								'<form action="#" class="form-horizontal">' +
									'<div class="control-group ">'+
										'<label for="cemail" class="control-label">Delivery Address</label>'+
										'<div class="controls">'+
											'<input type ="text" id="txtOPT" required class="span6 ">'+
											'<input type ="hidden" id="txtOPTTranId" required class="span6" value='+ res[1] +'>'+
											'<input type ="hidden" id="txtTryCount" required class="span6" value=0>'+
										'</div>'+
										'<div class="controls">'+
											'<input type="button"  onclick ="submitOPT()"  class="btn-success" value="Submit OTP">'+ 
										'</div>'+
									'</div>'+
								'</form>');
					}
				},
				error: function(){
					$('#myModalPayMessage .modal-body').html('<p>Error in Transaction</p>\n'); 
				}
		});
		
		
		
		
	});
	
	// /fetchAllProducts			controllers.Application.fetchAllProducts()
// GET		/fetchAllMerchants			controllers.Application.getAllMerchants()
// GET		/fetchAllBanks
	
	$.ajax({
			url: "fetchAllMerchants",
			type: "GET",
			dataType: "json",
			success: function(merchantArray){
				var options = '<option value="0">-- Select --</option>';
				for (var i = 0; i < merchantArray.length; i++)
				{
					options += '<option value='+ merchantArray[i].id +'>' + merchantArray[i].full_name + '</option>';
				}
				$('#cmbMerchant').html(options);
			},
			error: function(){
				alert("Error in fetching Products");
			}
	});
	
	$.ajax({
			url: "fetchAllCountries",
			type: "GET",
			dataType: "json",
			success: function(countryArray){
				var options = '<option value="0">-- Select --</option>';
				for (var i = 0; i < countryArray.length; i++)
				{
					options += '<option value='+countryArray[i].id +'>' + countryArray[i].name + '</option>';
				}
				$('#cmbCountry').html(options);
			},
			error: function(){
				alert("Error in fetching Products");
			}
	});
	
	
	$.ajax({
			url: "fetchAllProducts",
			type: "GET",
			dataType: "json",
			success: function(productArray){
				var options = '<option value="0">-- Select --</option>';
				for (var i = 0; i < productArray.length; i++)
				{
					options += '<option value='+ productArray[i].id +'>' +  productArray[i].name + '</option>';
				}
				$('#cmbProduct').html(options);
			},
			error: function(){
				alert("Error in fetching Products");
			}
	});
			
	$.ajax({
			url: "fetchAllBanks",
			type: "GET",
			dataType: "json",
			success: function(bankArray){
				var options = '<option value="0">-- Select --</option>';
				for (var i = 0; i < bankArray.length; i++)
				{
					options += '<option value='+ bankArray[i].id +'> [ ' + bankArray[i].name + ' ] '+ bankArray[i].full_name +'</option>';
				}
				$('#cmbBank').html(options);
			},
			error: function(){
				alert("Error in fetching Products");
			}
	});
			

	$.ajax({
		url: "fetchAllCountries",
		type: "GET",
		dataType: "json",
		success: function(countryArray){
			var options = '<option value="0">-- Select --</option>';
			for (var i = 0; i < countryArray.length; i++)
			{
				options += '<option value='+ countryArray[i].id +'> ' + countryArray[i].name + '</option>';
			}
			$('#cmbAccessLocation').html(options);
		},
		error: function(){
			alert("Error in fetching Countries");
		}
	});

	$('.crm-main-content').hide();
	$('#page-newTransaction').show();
	
});

function submitOPT()
{
	var otp = $('#txtOPT').val();
	var tranId = $('#txtOPTTranId').val();
	var trialCount = $('#txtTryCount').val()
	
	var query = 'otp='+otp+'&tranId='+tranId+'&trialCount='+ trialCount;

	$.ajax({
		url: "submitOPT",
		type: "GET",
		data:query,
		success: function(status){
			if (status.toUpperCase() == "OK")
			{
				$('#myModalPayMessage .modal-body').html('<p>Transaction is successfully completed.</p>');
			}
			else
			if (status.toUpperCase() == "FAIL")
			{
				$('#myModalPayMessage .modal-body').html('<p>Transaction is Failed. Please try again..</p>');
			}
			else
			{
				$('#myModalPayMessage .modal-body').html(status);
			}
			
			var payPal = '<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">'+
							'<input type="hidden" name="cmd" value="_s-xclick">'+
							'<input type="hidden" name="hosted_button_id" value="RYR656R9JXY34">'+
							'<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_buynow_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">'+
							'<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">'+
							'</form>';

			$('#myModalPayMessage .modal-body').html(payPal);
			
		},
		error: function(){
			alert("Error in fetching Countries");
		}
	});

	
	
}





