/**
 * 
 */
var teamAddPopUp=document.getElementById("teamAddPopUp");

var teamDetails=document.getElementById("teamDetails");
var inputContainer=document.getElementsByClassName("input-container");
var btnContainer=document.getElementsByClassName("btn-container");

var findTeamId=0;
/*var teamUser="";*/


function createTeam(){
	viewTeams(); 
	 
}




/*
var sts="";
var option1="";
var option="";*/
/*
function dropDownListCreate(type){
	console.log(type)
	console.log(sts);
	var id=document.getElementById(type+"teamLead");
	 teamUser=document.getElementById(type+"teamUser");
	 var xhr1=new XMLHttpRequest();
	 xhr1.onreadystatechange=function(){
		 if(this.readyState==4){
			 if(this.status==200){
				 var json=JSON.parse(this.responseText);
				 if(json.statusCode==200){
					teamUser.innerHTML="";
					id.innerHTML="";
					if(json.arr1.length>0){
						
					    var user=document.getElementById("u").getElementsByClassName("mult-select-tag");
					    var lead=document.getElementById("l").getElementsByClassName("mult-select-tag");
					    var user1=document.getElementById("u1").getElementsByClassName("mult-select-tag");
					    var lead1=document.getElementById("l1").getElementsByClassName("mult-select-tag");
						console.log(user);
						console.log(user1);
						var length=user.length;
						var length1=user1.length;
						for (var i = 0; i < length; i++){
						    if ((user[i].className == "mult-select-tag" && user!=null) && (lead[i].className == "mult-select-tag" && lead!=null)) {
						        user[i].parentNode.removeChild(user[i]);
						        lead[i].parentNode.removeChild(lead[i]);
						    }
					    }
					    for(var i=0;i<length1;i++){
							if((user1[i].className == "mult-select-tag" && user1!=null) && (lead1[i].className == "mult-select-tag" && lead1!=null)){
								user1[i].parentNode.removeChild(user1[i]);
						        lead1[i].parentNode.removeChild(lead1[i]);
							}	
						}
						for(var i=0;i<json.arr1.length;i++){
							 option1=document.createElement("option"); 
						     id.append(option1);
						     option1.setAttribute("value",json.arr1[i].email);
						     
						   
						     option1.textContent=json.arr1[i].email;
						     
							 option=document.createElement("option");
						     teamUser.appendChild(option);
						  
						     option.setAttribute("value",json.arr1[i].email);
						     option.textContent=json.arr1[i].email;
						     
						  
						     
						}
					
						
						new MultiSelectTag(type+'teamUser')
						new MultiSelectTag(type+'teamLead')
						
						inputContainer[0].setAttribute("oninput","mouseDown()");
					}
					else{     
						alert("please add employess in your company");
					}
					 
					
					
				 }
			 }
		 }
	 }
	 
	 xhr1.open("post","member/dropDownMembers");
	 xhr1.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	 xhr1.send();
}




function mouseDown(){
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				 var json=JSON.parse(this.responseText);
				 if(json.statusCode==200){
					 teamUser.innerHTML="";
				     
				     if(json.arr1.length>0){
						if(inputContainer[0].innerHTML!=""){
							var user=document.getElementById("u").getElementsByClassName("mult-select-tag");
							var length=user.length;
							var user1=document.getElementById("u1").getElementsByClassName("mult-select-tag");
							var length1=user1.length;
							for (var i = 0; i < length; i++) {
								
							    if (user[i].className == "mult-select-tag" && user!=null) {
							        user[i].parentNode.removeChild(user[i]);
							    }
							}
							for (var i = 0; i < length1; i++) {
								
							    if (user1[i].className == "mult-select-tag" && user1!=null) {
							        user1[i].parentNode.removeChild(user1[i]);
							    }
							}
							
							for(var i=0;i<json.arr1.length;i++){	
								if(inputContainer[0].childNodes[0].textContent.trim()!=json.arr1[i].email){
								 var option=document.createElement("option"); 
				                 teamUser.append(option);
								 option.setAttribute("value",json.arr1[i].email);
						     	
							     option.textContent=json.arr1[i].email;
							    }
							}
							new MultiSelectTag(sts+'teamUser')
							
				        }
				       
				     }
				     
			     }
			}
		}
	}
	
	 xhr.open("post","member/dropDownMembers");
	 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	 xhr.send();
}
*/









function viewTeams(){
	var viewTeamInCompany=document.getElementById("viewTeamInCompany");
	var xhr1=new XMLHttpRequest();
	xhr1.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				var resp=JSON.parse(this.responseText);
				if(resp.statusCode==200){
					
					if(resp.role=="CEO"){
						var addTeampopup=document.getElementById("addTeampopup");
						var updateDatasSave=document.getElementById("updateDatasSave");
						var updateteamLead=document.getElementById("updateteamLead");
						
						addTeampopup.style.display="block";
						updateDatasSave.style.display="block";
						updateteamLead.style.pointerEvents="auto";
						
						
					}
					
				   if(resp.arr1.length>0){
				      viewTeamInCompany.innerHTML="";
				      for(var i=0;i<resp.arr1.length;i++){
						var teamBox=document.createElement("div");
						viewTeamInCompany.appendChild(teamBox);
						teamBox.setAttribute("onclick","updateTeam('"+resp.arr1[i].teamId+"')");
						teamBox.classList.add("teamBox");
						
						
						var teamName=document.createElement("h3");
						teamBox.appendChild(teamName);
						teamName.classList.add("teamName");
						teamName.innerHTML=resp.arr1[i].teamName;
						teamName.title=resp.arr1[i].teamName;
						
						
						var createBy=document.createElement("p");
						teamBox.appendChild(createBy);
						createBy.classList.add("createBy");
						createBy.innerHTML="create by : "+resp.arr1[i].createBy.email;
						createBy.title=resp.arr1[i].createBy.email;
						
						
						var teamLead=document.createElement("p");
						teamBox.appendChild(teamLead);
						teamLead.classList.add("teamLead");
						teamLead.innerHTML="Team Lead : "+resp.arr1[i].teamLead.email;
						teamLead.title=resp.arr1[i].teamLead.email;
						
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
	
	xhr1.open("POST","team/getTeamDetails");
	xhr1.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr1.send();
}
var updatePopup=document.getElementById("updatePopup");
var teamDetails=document.getElementById("teamDetails");


function updateTeam(teamId){
	findTeamId=teamId;
	SaveUpdateDatasInTeams(teamId)

		
	teamDetails.style.filter="blur(3px)"
	teamDetails.style.pointerEvents="none";
	updatePopup.style.transform="translateY(6%)";
}

function updateDatasSave(){
	
	
	
	
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var json=JSON.parse(xhr.responseText)
				if(json.statusCode==200){
					alert(json.detailedMessage);
					viewTeams();
					
				}
				else{
					alert(json.detailedMessage);
				}
			}
		}
	}
	
	
	
	
	
	    var childNodes=inputContainer[0].childNodes;
	    var length=inputContainer[0].children.length;
	  
	
		if(length>=2){
			   var teamMember="";
			   for(var i=0;i<childNodes.length-1;i++){
				   teamMember+=childNodes[i].childNodes[0].textContent+",";
			   }
			   teamMember+=childNodes[childNodes.length-1].childNodes[0].textContent;
			   
				var teamLead=document.getElementById("updateteamLead").value;
				
				
				xhr.open("POST","team/updateTeam");
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				xhr.send("teamId="+findTeamId+"&teamLead="+teamLead+"&teamMembers="+teamMember);
				
				updatePopup.style.transform="translateY(-150%)";
				teamDetails.style.filter="blur(0px)"
				teamDetails.style.pointerEvents="auto";
				
	    }
		else{
			alert("please select 2 members or more than 2 members");
		}
	
	
	
	
}

function updatePopUpClose(){
	updatePopup.style.transform="translateY(-150%)";
	teamDetails.style.filter="blur(0px)"
	teamDetails.style.pointerEvents="auto";
}

 function addTeamPopup(){
	
	 dropDownLeadMembers("create");
	 teamAddPopUp.style.transform="translateX(155%)";
     teamDetails.style.filter="blur(3px)";
     teamDetails.style.pointerEvents="none";
 }
 
 
 
 var dropDownMembersJson="";
 
 
 
 function dropDownLeadMembers(type){
	 dropDownMembersservlet();
	 
	 console.log(dropDownMembersJson);
	 var teamLead=document.getElementById("createteamLead");
     var teamMembers=document.getElementById("createteamUser");	 
	 
	 var json=dropDownMembersJson;
	 if(json.statusCode==200){
		 teamLead.innerHTML="";
		 teamMembers.innerHTML="";
		 
		 
		 
		 var user=document.getElementById("u").getElementsByClassName("mult-select-tag");
		 var length=user.length;
		
		
		
		for (var i = 0; i < length; i++){
		    if (user[i].className == "mult-select-tag" && user!=null) {
		        user[i].parentNode.removeChild(user[i]);						      
		    }
	    }
	    var user1=document.getElementById("u1").getElementsByClassName("mult-select-tag");
		var length1=user1.length;
		
		
		
		for (var i = 0; i < length1; i++){
		    if (user1[i].className == "mult-select-tag" && user1!=null) {
		        user1[i].parentNode.removeChild(user1[i]);						      
		    }
	    }
		 
		 
		 if(json.arr1.length>0){
			 for(var i=0;i<json.arr1.length;i++){
				 console.log(json.arr1.length+"Sdf")
					 var optionLead=document.createElement("option"); 
				     teamLead.append(optionLead);
				     optionLead.setAttribute("value",json.arr1[i].email);
				     optionLead.textContent=json.arr1[i].email;
				 
			     
			     if(teamLead.value!=json.arr1[i].email){
				     var optionTeamMember=document.createElement("option");
				     teamMembers.appendChild(optionTeamMember);
				     optionTeamMember.setAttribute("value",json.arr1[i].email);
				     optionTeamMember.textContent=json.arr1[i].email;
				 }
			     
			}
			
			new MultiSelectTag('createteamUser');
			if(json.role=="CEO"){
				var multiTag=document.getElementsByClassName("mult-select-tag");
				console.log(multiTag)
				multiTag[0].style.pointerEvents="auto";
			}
			
	     }
	 }

 }
 
 
 function dropDownMembersservlet(){
	 
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(xhr.status==200){
				 dropDownMembersJson=JSON.parse(xhr.responseText);
				 
			 }
		 }
	 } 	 	 	 
	 
	 
	 xhr.open("post","member/dropDownMembers",false);
	 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	 xhr.send();
 }
 
 
 
 function addTeamLead(type){
	 console.log(type);
	 var json=dropDownMembersJson;
	 if(json.statusCode==200){
		 var teamLead=document.getElementById(type+"teamLead");
         var teamMembers=document.getElementById(type+"teamUser");
		 
		 teamMembers.innerHtml="";
		 console.log(teamMembers.innerHTML="");
		 
	     var user=document.getElementById("u").getElementsByClassName("mult-select-tag");
		 var length=user.length;
		
		
		
		for (var i = 0; i < length; i++){
		    if (user[i].className == "mult-select-tag" && user!=null) {
		        user[i].parentNode.removeChild(user[i]);						      
		    }
	    }
	    
	    
	    var user1=document.getElementById("u1").getElementsByClassName("mult-select-tag");
		var length1=user1.length;
		
		
		
		for (var i = 0; i < length1; i++){
		    if (user1[i].className == "mult-select-tag" && user1!=null) {
		        user1[i].parentNode.removeChild(user1[i]);						      
		    }
	    }
		 
		 for(var i=0;i<json.arr1.length;i++){
			 if(teamLead.value!=json.arr1[i].email){
				 var optionTeamMember=document.createElement("option");
			     teamMembers.appendChild(optionTeamMember);
			     optionTeamMember.setAttribute("value",json.arr1[i].email);
			     optionTeamMember.textContent=json.arr1[i].email;
			 }
		 }
		 new MultiSelectTag(type+'teamUser');
		 if(json.role=="CEO"){
			var multiTag=document.getElementsByClassName("mult-select-tag");
			console.log(multiTag)
			multiTag[0].style.pointerEvents="auto";
		 }
	 }
 }
 
 
 
 
 
 
 
 function teamAddCancelButton(){
	 teamDetails.style.pointerEvents="auto";
	teamAddPopUp.style.transform="translateX(300%)";
	teamDetails.style.filter="blur(0px)";
	
	
}

function teamAddButton(){
	
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var resp=JSON.parse(this.responseText);
				if(resp.statusCode==200){
					alert(resp.detailedMessage);
					viewTeams(); 
					
				}
				else{
					alert(resp.detailedMessage);
				}
			}
		}
	 }
	
		
		
	    childNodes=inputContainer[0].childNodes;
	    var length=inputContainer[0].children.length;
	

	    
	    
    	var teamName=document.getElementById("teamName").value;

					
	    var teamPattern=/^[A-Za-z\ ]+$/;              
	  
	   if(teamName.trim()!="" && teamName.match(teamPattern)){
			if(length>=2){
					var teamMember="";
				   for(var i=0;i<childNodes.length-1;i++){
					   teamMember+=childNodes[i].childNodes[0].textContent+",";
				   }
				   teamMember+=childNodes[childNodes.length-1].childNodes[0].textContent;
				   
					var teamLead=document.getElementById("createteamLead").value;
					
				
				
					xhr.open("POST","team/addTeams");
					xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
					xhr.send("teamName="+teamName+"&teamLead="+teamLead+"&teamMembers="+teamMember);
					
					teamAddPopUp.style.transform="translateX(300%)";
					teamDetails.style.filter="blur(0px)";
					teamDetails.style.pointerEvents="auto";
					
		    }
			else{
				alert("please select 2 members or more than 2 members");
			}
	
	    }
		else{
			alert("Invalid team name");
		}
}

var teamUserEmail="";
function SaveUpdateDatasInTeams(teamId){
	var xhr=new XMLHttpRequest();
	var teamHeading=document.getElementById("teamHeading");
	var userCount=document.getElementById("userCount");
	var teamLeadDetails=document.getElementById("teamLeadDetails");
	var userInTeam=document.getElementById("userInTeam");
	var teamLogo=document.getElementById("teamLogo");
	
	var updateteamLead=document.getElementById("updateteamLead");
	var updateteamUser=document.getElementById("updateteamUser");
	
	userInTeam.innerHTML="";
	teamLeadDetails.innerHTML="";
	teamLogo.innerHTML="";
	
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var json=JSON.parse(xhr.responseText);
				
				var teamNameSplit=json.teamName.split(" ");
				if(teamNameSplit.length>=2){
					for(var i=0;i<teamNameSplit.length;i++){
						teamLogo.textContent+=teamNameSplit[i].substring(0,1).toUpperCase();
					}
				}
				else{
					teamLogo.textContent=teamNameSplit[0].substring(0,1).toUpperCase();
				}
				
				
				teamHeading.textContent=json.teamName;
				userCount.textContent=" : "+(json.teamMembers.length+1);
				userCount.title=json.teamMembers.length+1;
				
				var teamLeadfirName=document.createElement("p");
				teamLeadDetails.appendChild(teamLeadfirName);
				teamLeadfirName.classList.add("teamLeadfirName");
				teamLeadfirName.textContent=json.teamLead.firstName+" "+json.teamLead.lastName;
				teamLeadfirName.title=json.teamLead.firstName+" "+json.teamLead.lastName;
				
				var teamLeadEmail=document.createElement("p");
				teamLeadDetails.appendChild(teamLeadEmail);
				teamLeadEmail.classList.add("teamLeadEmail");
				teamLeadEmail.textContent=json.teamLead.email;
				teamLeadEmail.title=json.teamLead.email;
				
				
				
				
				
				
				var user=document.getElementById("u").getElementsByClassName("mult-select-tag");
				var length=user.length;
				
				
				
				for (var i = 0; i < length; i++){
				    if (user[i].className == "mult-select-tag" && user!=null) {
				        user[i].parentNode.removeChild(user[i]);						      
				    }
			    }
			    var user1=document.getElementById("u1").getElementsByClassName("mult-select-tag");
				var length1=user1.length;
				
				
				
				for (var i = 0; i < length1; i++){
				    if (user1[i].className == "mult-select-tag" && user1!=null) {
				        user1[i].parentNode.removeChild(user1[i]);						      
				    }
			    }
			
			
			   updateteamUser.innerHTML="";
			   updateteamLead.innerHTML="";
			   console.log(updateteamUser.innerHTML="")
			  
			
			   var option=document.createElement("option");
			   updateteamLead.appendChild(option);
			   option.setAttribute("value",json.teamLead.email);
			   option.textContent=json.teamLead.email;
			 
			 
			 var array=[];
				
				
				for(var i=0;i<json.teamMembers.length;i++){
					var teamUserDetailsInTeam=document.createElement("p");
					userInTeam.appendChild(teamUserDetailsInTeam);
					teamUserDetailsInTeam.classList.add("teamUserDetailsInTeam");
					
					
					var teamUserName=document.createElement("p");
					teamUserDetailsInTeam.appendChild(teamUserName);
					teamUserName.classList.add("teamUserName");
					teamUserName.textContent=json.teamMembers[i].firstName+" "+json.teamMembers[i].lastName;
					teamUserName.title=json.teamMembers[i].firstName+" "+json.teamMembers[i].lastName;
					
					teamUserEmail=document.createElement("p");
					teamUserDetailsInTeam.appendChild(teamUserEmail);
					teamUserEmail.classList.add("teamUserEmail");
					teamUserEmail.textContent=json.teamMembers[i].email;
					teamUserEmail.title=json.teamMembers[i].email;
						
				
					
					array.push(json.teamMembers[i].email);	
					
			   }
			   
			   
			   	dropDownMembersservlet();
						
				var json1=dropDownMembersJson;	
					
			    if(dropDownMembersJson.statusCode==200){
					
					
				   for(var j=0;j<json1.arr1.length;j++){
					if(updateteamLead.value!=json1.arr1[j].email){
					   var option2=document.createElement("option");
					   updateteamLead.appendChild(option2);
					   option2.setAttribute("value",json1.arr1[j].email);
					   option2.textContent=json1.arr1[j].email;
						  
					}
					
					    var option1=document.createElement("option");	
					  
					    option1.value=json1.arr1[j].email;
					    option1.innerHTML=json1.arr1[j].email;
					    for(var i=0;i<array.length;i++){
					        if(json1.arr1[j].email==array[i]){
							   option1.setAttribute("selected","");
							}
						}	
					    updateteamUser.appendChild(option1);
					
				
					
				
				}	
			 			   
			   
			    new MultiSelectTag('updateteamUser');
			    if(json1.role=="CEO"){
					var multiTag=document.getElementsByClassName("mult-select-tag");
					console.log(multiTag)
					multiTag[0].style.pointerEvents="auto";
				}
			    
			   }		
				
		    }
		}
	}
	xhr.open("POST","team/SaveUpdateDatasInTeams");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("teamId="+teamId);
}
