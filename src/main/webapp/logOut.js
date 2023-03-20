/**
 * 
 */

 function logOut(){
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				window.location.href="/zohoProjectManagement/index.html";
			}
		} 
	 }
	 
	 xhr.open("POST","logOut/logout");
	 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	 xhr.send();
 }
 