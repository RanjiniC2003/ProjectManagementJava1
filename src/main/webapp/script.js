/**
 * 
 */


//---------------------------------------------------------------sign in ,sign up-----------------------------------------------------------------


function  UsersignUp(){
	 var cn=document.getElementById("companyName").value;
	 var fn=document.getElementById("firstName").value;
	 var ln=document.getElementById("lastName").value;
	 var el=document.getElementById("email").value;
	 var pd=document.getElementById("password").value;
	 var pn=document.getElementById("phoneNumber").value;
	 console.log("companyname="+cn+"&firstName="+fn+"&lastName="+ln+"&email="+el+"&password="+pd+"&phoneNumber="+pn);
	 
	 
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(this.status==200){
				var resp = JSON.parse(this.responseText); 
				if(resp.statusCode == 200){
	                 console.log("successfuly send");
	                
	                 window.location.href="./taskSummary.html";
	             }	
		      }
		   }
		}
		
		let namePattern=/^[A-Za-z\. ]+$/;
		let comPattern=/^[A-Za-z\ ]+$/;
		let lastPattern=/^[A-Za-z]+$/;
		let emailPattern=/^[a-z0-9]+(?:\.[a-z0-9]+)*@[a-z]+(?:\.[a-z]+)*$/;
		let pattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%\^\&*\ )\(+=._-])(?=.*[0-9])[A-Za-z0-9!@#\$%\^\&*\ )\(+=._-]{8,}$/
		/*non capturing group--?:*/
		let phNum=/^[0-9]{10}$/;
		if(cn.trim()==""){
			alert("company must be filled out");
		}
		else if(cn.match(comPattern)){
			if(fn.trim()=="" || fn.replaceAll("."," ").trim()==""){
			     alert("first name must be filled out");
			}
			else if(fn.match(namePattern)){
				 if(ln.match(lastPattern) || ln.trim==""){
					 if(el==""){
						 alert("Email must be filled out");
					 }
					 else if(el.match(emailPattern)){
						 if(pd=="" || pd.trim()==""){
							 alert("password must be filled out");
						 }
						 else if(pd.match(pattern)){
					        if(pn==""){
								alert("Phone number must be filled out");
							}
							else if(pn.match(phNum)){
								xhr.open("POST","signUp");
								xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
								xhr.send("companyname="+cn+"&firstName="+fn+"&lastName="+ln+"&email="+el+"&password="+pd+"&phoneNumber="+pn);
							}
							else{
								alert("Invalid phone number");
							}
					        
				         }
						 else{
							 alert("Invalid Password...");
						 }
					 }
					 else{
						 alert("Invalid email");
					 }
				 }
				 else{
					 alert("Invalid last name")
				 }
			}
			else{
				alert("Invalid first name");
			}
		}
		else{
			alert("Invalid company");
		
		}
		 
			 
		
		 
}
	
 
 
 function userSignIn(){
	 var name=document.getElementById("email1").value;
	 var password=document.getElementById("password1").value;
	  console.log("&email="+name+"&password="+password);
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(this.status==200){
				var resp = JSON.parse(this.responseText);
				if(resp.statusCode == 200){
					 console.log(resp.detailedMessage);	
					 window.location.href="./taskSummary.html";
				}
				else if(name=="" && password==""){
					alert("Email and password must be filled out");
				}
				else if(name==""){
					alert("Email must be filled out");
				}
				else if(password==""){
					alert("password must be filled out");
				}
				
				else{
					
					alert("Invalid email");
					document.getElementById("email1").value="";
					document.getElementById("password1").value="";
				}
				/*console.log(name+" "+password);*/
				
			 }
		 }
	 }
	 xhr.open("POST","signIn");
	 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	 xhr.send("email="+name+"&password="+password);
	 
 }
 
 

 
 
 



 