
"use strict";
//# sourceURL=main.js

$(function() {
	
	var _pageSize; 
	
	function getUersByName(pageIndex, pageSize) {
		 $.ajax({ 
			 url: "/users", 
			 contentType : 'application/json',
			 data:{
				 "async":true, 
				 "pageIndex":pageIndex,
				 "pageSize":pageSize,
				 "name":$("#searchName").val()
			 },
			 success: function(data){
				 $("#mainContainer").html(data);
		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	}

	$.tbpage("#mainContainer", function (pageIndex, pageSize) {
		getUersByName(pageIndex, pageSize);
		_pageSize = pageSize;
	});
  
	$("#searchNameBtn").click(function() {
		getUersByName(0, _pageSize);
	});
	
	$("#addUser").click(function() {
		$.ajax({ 
			 url: "/signup", 
			 success: function(data){
				 $("#userFormContainer").html(data);
		     },
		     error : function(data) {
		    	 toastr.error("error!");
		     }
		 });
	});
	
	$("#rightContainer").on("click",".blog-edit-user", function () { 
		$.ajax({ 
			 url: "/editeUseur/" + $(this).attr("userId"), 
			 success: function(data){
				 $("#userFormContainer").html(data);
		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	});
	
	$("#submitEdit").click(function() {
		$.ajax({ 
			 url: "/signup", 
			 type: 'POST',
			 data:$('#userForm').serialize(),
			 success: function(data){
				 $('#userForm')[0].reset();  
				 
				 if (data.success) {
					
					 getUersByName(0, _pageSize);
				 } else {
					 toastr.error(data.message);
				 }
 
		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	});
	
	$("#rightContainer").on("click",".blog-delete-user", function () { 
		
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		
		
		$.ajax({ 
			 url: "/users/" + $(this).attr("userId") , 
			 type: 'DELETE', 
			 beforeSend: function(request) {
                 request.setRequestHeader(csrfHeader, csrfToken); 
             },
			 success: function(data){
				 if (data.success) {
					
					 getUersByName(0, _pageSize);
				 } else {
					 toastr.error(data.message);
				 }
		     },
		     error : function() {
		    	 toastr.error("error!");
		     }
		 });
	});
});
