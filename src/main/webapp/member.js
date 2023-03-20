/**
 * 
 */

var memberAdd=document.getElementById("memberDetails");
var memberAddPopUp=document.getElementById("memberAddPopUp");

function addUserPopup(){
	memberAddPopUp.style.transform="translateX(185%)";
	memberDetails.style.filter="blur(3px)";
	memberDetails.style.pointerEvents="none";
}

function empAddCancelButton(){
	memberAddPopUp.style.transform="translateX(330%)";
	memberDetails.style.filter="blur(0px)";
	memberDetails.style.pointerEvents="auto";
	viewMembers();
	
}

function empAddButton(){
	var email=document.getElementById("requestEmail").value;
	

	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				var resp=JSON.parse(this.responseText);
				if(resp.statusCode==200){
					alert(resp.detailedMessage);
				}
					
			}
		}
	}
	xhr.open("POST","invite/inviteNew");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("email="+email);
	memberAddPopUp.style.transform="translateX(330%)";
	memberDetails.style.filter="blur(0px)";
	memberDetails.style.pointerEvents="auto";
}
	
	
function viewMembers(){
	var xhr=new XMLHttpRequest();
	
	var addMemberInTeam=document.getElementById("addMemberInTeam");
	
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var resp=JSON.parse(this.responseText);
				
			
			
			   if(resp.statusCode==200){
				   
				   
				 if(resp.role=="CEO"){
					 console.log("df");
					 var addUserPopup=document.getElementById("addUserPopup");
					 addUserPopup.style.display="block";
				 }  
				 
				   
				 if(resp.arr1.length>0){
				   addMemberInTeam.innerHTML="";
				   for(var i=0;i<resp.arr1.length;i++){
					   var memberBox=document.createElement("div");
					   addMemberInTeam.appendChild(memberBox);
					   memberBox.classList.add("memberBox");
					   
					   
					   var empName=document.createElement("h1");
					   memberBox.appendChild(empName);
					   empName.classList.add("empName");
					   empName.innerHTML=resp.arr1[i].firstName+" "+resp.arr1[i].lastName;
					   empName.title=resp.arr1[i].firstName+" "+resp.arr1[i].lastName;
					   
					   
					   var empEmail=document.createElement("p");
					   memberBox.appendChild(empEmail);
					   empEmail.classList.add("empEmail");
					   empEmail.innerText=resp.arr1[i].email;
					   empEmail.title=resp.arr1[i].email;
					    
					   
					   var empNumber=document.createElement("p");
					   memberBox.appendChild(empNumber);
					   empNumber.classList.add("empNumber");
					   empNumber.textContent=resp.arr1[i].phoneNumber;
					   empNumber.title=resp.arr1[i].phoneNumber;
					   
					   
					   var joinDate=document.createElement("p");
					   memberBox.appendChild(joinDate);
					   joinDate.classList.add("joinDate");
					   joinDate.textContent="Join on : "+resp.arr1[i].companyMemberDetails.joinDate;
					   joinDate.title=resp.arr1[i].companyMemberDetails.joinDate;
					   
					   
					   var lastAccess=document.createElement("p");
					   memberBox.appendChild(lastAccess);
					   lastAccess.classList.add("lastAccess");
					   lastAccess.textContent="Last updated on : "+resp.arr1[i].companyMemberDetails.lastUpdatedOn;
					   lastAccess.title=resp.arr1[i].companyMemberDetails.lastUpdatedOn;
				   }  
				   
			     }
			   }

			}
			else if(xhr.status==301){
				let location = "/zohoProjectManagement/index.html"

				if (location) {
					window.location.href = location;
				}
			}
		}
	}
	xhr.open("POST","member/getTheirTeamMembers");
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    xhr.send();
	
}


