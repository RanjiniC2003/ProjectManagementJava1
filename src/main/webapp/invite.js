function inviteUsers(){
	 var fn=document.getElementById("firstName").value;
	 var ln=document.getElementById("lastName").value;
	 var pd=document.getElementById("password").value;
	 var pn=document.getElementById("phoneNumber").value;
	 const urlParams = new URLSearchParams(window.location.search);
     const el = urlParams.get("email");
     console.log("urlParams "+urlParams);
	 console.log("el "+el);
	 
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(this.status==200){
				var resp = JSON.parse(this.responseText); 
				if(resp.statusCode == 200){
					if(resp.statusMessage=="SUCCESS"){
						 console.log("successfuly send");
	                
	                     window.location.href="./taskSummary.html";
					}
	                
	             }	
		      }
		   }
		}
		
		let namePattern=/^[A-Za-z\. ]+$/;
		
		let lastPattern=/^[A-Za-z\ ]+$/;
		let emailPattern=/^[a-z0-9]+(?:\.[a-z0-9]+)*@[a-z]+(?:\.[a-z]+)*$/;
		let pattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%\^\&*\ )\(+=._-])(?=.*[0-9])[A-Za-z0-9!@#\$%\^\&*\ )\(+=._-]{8,}$/
		/*non capturing group--?:*/
		let phNum=/^[0-9]{10}$/;
	

			if(fn.trim()=="" || fn.replaceAll("."," ").trim()==""){
			     alert("first name must be filled out");
			}
			else if(fn.match(namePattern)){
				 if(ln.match(lastPattern)){
					 if(el==""){
						 alert("Email must be filled out");
					 }
					 else if(el.match(emailPattern)){
						 if(pd==""){
							 alert("password must be filled out");
						 }
						 else if(pd.match(pattern)){
					        if(pn==""){
								alert("Phone number must be filled out");
							}
							else if(pn.match(phNum)){
								xhr.open("POST","invite/accept");
								xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
								xhr.send("firstName="+fn+"&lastName="+ln+"&email="+el+"&password="+pd+"&phoneNumber="+pn);
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