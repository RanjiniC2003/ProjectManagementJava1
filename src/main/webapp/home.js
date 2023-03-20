/**
 * 
 */
var homeDetails=document.getElementById("homeDetails");
var homeJson="";
var userId=0;
 
  var updateUserProfile=document.getElementById("updateUserProfile");
  var companyUser=document.getElementById("companyUser");
 function viewAll(){
	 
	 
	 var companyName=document.getElementById("companyName");
	 var openTaskList=document.getElementById("openTaskList");
	 var closeTaskList=document.getElementById("closeTaskList");
	 var openProjectList=document.getElementById("openProjectList");
	 var closeProjectList=document.getElementById("closeProjectList");
	 var openIssueList=document.getElementById("openIssueList");
	 var closeIssueList=document.getElementById("closeIssueList");
	 var dueDateProject=document.getElementById("dueDateProject");
	 var todayDueDateProject=document.getElementById("todayDueDateProject");
	 
	
	 
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 
		 if(this.readyState==4){
			 if(this.status==200){
				 homeJson=JSON.parse(this.responseText);
				 
				 userId=homeJson.userDetails.userId;
				 
				 var json=homeJson;
				 
			     console.log(json.userDetails)
				 updateUserProfile.textContent=json.userDetails.firstName.substring(0,1);
				 companyUser.textContent+=" "+json.userDetails.firstName+" "+json.userDetails.lastName;
				 companyName.textContent+=" "+json.CompanyDetails;
				 
				 document.getElementById("openTaskCount").textContent=json.OpenTasks.length;
				 document.getElementById("closeTaskCount").textContent=json.ClosedTasks.length;
				 document.getElementById("openProjectCount").textContent=json.OpenProjects.length;
				 document.getElementById("closeProjectCount").textContent=json.ClosedProjects.length;
				 document.getElementById("openIssueCount").textContent=json.OpenIssues.length;
				 document.getElementById("closeIssueCount").textContent=json.ClosedIssues.length;
				 
				 				 
				 showDetails(json,json.OpenTasks,openTaskList,"taskName","project","projectName","taskAssignTo","","","startDate");
				 showDetails(json,json.ClosedTasks,closeTaskList,"taskName","project","projectName","taskAssignTo","","","startDate");
				 showDetails(json,json.OpenProjects,openProjectList,"projectName","team","teamName","team","teamLead","email","startDate");
				 showDetails(json,json.ClosedProjects,closeProjectList,"projectName","team","teamName","team","teamLead","email","startDate");
				 showDetails(json,json.OpenIssues,openIssueList,"issueName","project","projectName","assignTo","","","startDate");
				 showDetails(json,json.ClosedIssues,closeIssueList,"issueName","project","projectName","assignTo","","","startDate");
				 showDetails(json,json.todayDueProject,todayDueDateProject,"projectName","team","teamName","team","teamLead","email","startDate");
				 showDetails(json,json.dueProject,dueDateProject,"projectName","team","teamName","team","teamLead","email","startDate");
				 
				 
				
				 
				 
			 }
			 else if(xhr.status==301){
				let location = "/zohoProjectManagement/index.html"

				if (location) {
					window.location.href = location;
				}
			}
		 }
	 }
	 xhr.open("POST","home/getHomeDetails");
	 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	 xhr.send();
 }
 
 
 
 
 function showDetails(json,arr,List,Name,parName,parName2,user,user2,user3,sD){
			/*console.log(json,arr,List,Name,parName,user,sD);
		console.log(arr.length);*/ 
	/*	console.log(json);*/
		
		
	 for(var i=0;i<arr.length;i++){
		
		 
		 var mainDiv=document.createElement("div");
		 List.appendChild(mainDiv);
		 mainDiv.classList.add("mainDiv");
		 
		 var name=document.createElement("p");
		 mainDiv.appendChild(name);
		 name.classList.add("name");
		 name.textContent=Name+" :  "+arr[i][Name];
		 name.title=arr[i][Name];
		 
		 
		 var parentName=document.createElement("p");
		 mainDiv.appendChild(parentName);
		 parentName.classList.add("parentName");
		
		 
		 parentName.textContent=parName+" : "+arr[i][parName][parName2];
		 parentName.title=arr[i][parName][parName2]
		 
		 var assign=document.createElement("p");
		 mainDiv.appendChild(assign);
		 assign.classList.add("assign");
		 if(user2!="" && user3!=""){
			 
			 assign.textContent=user3+" : "+arr[i][user][user2][user3];
		     assign.title=arr[i][user][user2][user3];
		 }
		 else{
			
			 assign.textContent=user+" : "+arr[i][user]["email"];
		     assign.title=arr[i][user]["email"];
		 }
		 
		 
		 var startDate=document.createElement("p");
		 mainDiv.appendChild(startDate);
		 startDate.classList.add("startDate");
		
		 startDate.textContent=sD+" : "+arr[i][sD];
		 startDate.title=arr[i][sD];
		 
	 }
 }
 
 
 var updateDetails=document.getElementById("updateDetails"); 
 var updateDetailspopUp=document.getElementById("updateDetailsPopUp");
 var changeUserPasswordpopUp=document.getElementById("changeUserPasswordPopUp");
 function profile(){
	 
	 updateDetails.style.display="block";
	 homeDetails.style.filter="blur(3px)";
	 homeDetails.style.pointerEvents="none";
 }
 
 function cancelButton(){
	 updateDetails.style.display="none";
	 homeDetails.style.filter="blur(0px)";
	 homeDetails.style.pointerEvents="auto";
 }
 
 function updateDetailsPopUp(){
	 updateDetails.style.display="none";
	 updateDetailspopUp.style.display="block";
	 homeDetails.style.filter="blur(3px)";
	 homeDetails.style.pointerEvents="none";
 }
 
 function updateCancelUserDetailsButton(){
	 updateDetailspopUp.style.display="none";
	 homeDetails.style.filter="blur(0px)";
	 homeDetails.style.pointerEvents="auto";
 }
 
 function updateUserDetailsButton(){
	 var updateUserName=document.getElementById("updateUserName").value;
	 var updateLastName=document.getElementById("updateLastName").value;
	 var updatePhoneNumber=document.getElementById("updatePhoneNumber").value;
	 
	/* console.log(updateUserName+","+updateLastName+","+updatePhoneNumber);*/
	
	let namePattern=/^[A-Za-z\. ]+$/;
	let lastPattern=/^[A-Za-z]+$/;
	let phNum=/^[0-9]{10}$/;
	
	var check=false;
	
	if(updateUserName.trim()=="" || updateUserName.replaceAll("."," ").trim()==""){
	     alert("first name must be filled out");
	}
	else if(updateUserName.match(namePattern)){
		if(updateLastName.match(lastPattern) || updateLastName.trim==""){
			if(updatePhoneNumber==""){
				alert("Phone number must be filled out");
			}
			else if(updatePhoneNumber.match(phNum)){
				
			}
			else{
				alert("Invalid phone number");
			}
		}
	}
	else{
		alert("Invalid first name");
	} 
	
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(xhr.status==200){
				 var json=JSON.parse(this.responseText);
				 if(json.statusCode==200){
			         updateUserProfile.textContent=json.userDetails.substring(0,1);
				     companyUser.textContent="Welcome "+json.userDetails;
					 alert(json.detailedMessage);
					 updateDetailspopUp.style.display="none";
					 homeDetails.style.filter="blur(0px)";
					 homeDetails.style.pointerEvents="auto";
				 }
			 }
		 }
	 }
	 if(check){
		 xhr.open("POST","home/updateDetails");
	     xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	     xhr.send("firstName="+updateUserName+"&lastName="+updateLastName+"&phoneNumber="+updatePhoneNumber+"&userId="+userId);
	 }
	 
	 
	 
	 
	 
 }
 
 function changePasswordPopUp(){
	 updateDetails.style.display="none";
	 changeUserPasswordpopUp.style.display="block";
	 homeDetails.style.filter="blur(3px)";
	 homeDetails.style.pointerEvents="none";
 }
 
 function changePasswordCancelButton(){
	 changeUserPasswordpopUp.style.display="none";
	 homeDetails.style.filter="blur(0px)";
	 homeDetails.style.pointerEvents="auto";
 }
 
 function changePasswordButton(){
	 
	 
	  var xhr=new XMLHttpRequest();
	 let pattern = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%\^\&*\ )\(+=._-])(?=.*[0-9])[A-Za-z0-9!@#\$%\^\&*\ )\(+=._-]{8,}$/
	 
	 var oldPassword=document.getElementById("oldPassword").value;
	 var newPassword=document.getElementById("newPassword").value;
	 
	 
	 if(newPassword=="" || newPassword.trim()==""){
		 alert("password must be filled out");
	 }
	 else if(newPassword.match(pattern)){
		 xhr.open("POST","home/changePassword");
	     xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	     xhr.send("oldPassword="+oldPassword+"&newPassword="+newPassword+"&userId="+userId);
     }
     else{
		 alert("Invalid password");
	 }
	 
	
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(xhr.status==200){
				 var json=JSON.parse(this.responseText);
				 if(json.statusCode==200){
					 alert((json.detailedMessage));
					 changeUserPasswordpopUp.style.display="none";
					 homeDetails.style.filter="blur(0px)";
					 homeDetails.style.pointerEvents="auto";
				 }
				 else {
					 alert((json.detailedMessage));
				 }
				 
			 }
		 }
	 }
	 
	 
	 
	 
	 
 }
 