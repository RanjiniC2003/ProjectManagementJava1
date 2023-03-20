/**
 * 
 */
var issueDetails=document.getElementById("issueDetails");
var addIssuePopUp=document.getElementById("addIssuePopUp");
var issueUpdatePopUp=document.getElementById("issueUpdatePopUp");

var findIssueId=0;

var addissue=document.getElementById("addissue");
function viewIssues(){
	
	addissue.innerHTML="";
	viewIssuesInPage("securuty");
	viewIssuesInPage("crash");
	viewIssuesInPage("dataLoss");
	viewIssuesInPage("performance");
	viewIssuesInPage("ui");
	viewIssuesInPage("otherBug")
	viewIssuesInPage("feature");
	viewIssuesInPage("enhancement");
}


function viewIssuesInPage(classification){
	
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				var json=JSON.parse(this.responseText);
				if(json.statusCode==200){
					
					for(var i=0;i<json.arr.length;i++){
						
						var issueBox=document.createElement("div");
						addissue.appendChild(issueBox);
						issueBox.classList.add("issueBox");
						issueBox.setAttribute("onclick","updateIssue('"+json.arr[i].issueId+"')");
						
						var issueName=document.createElement("p");
						issueBox.appendChild(issueName);
						issueName.classList.add("showIssueName");
						issueName.textContent=json.arr[i].issueName;
						issueName.title=json.arr[i].issueName;
						
						var des=document.createElement("p");
						issueBox.appendChild(des);
						des.classList.add("showIssueDes");
						des.textContent=json.arr[i].description;
						des.title=json.arr[i].description;
						
						var projectName=document.createElement("p");
						issueBox.appendChild(projectName);
						projectName.classList.add("showprojectName");
						projectName.title=json.arr[i].project.projectName
						projectName.textContent=json.arr[i].project.projectName;
						
						
						var assignTo=document.createElement("p");
						issueBox.appendChild(assignTo);
						assignTo.classList.add("showassignTo");
						assignTo.textContent=json.arr[i].assignTo.firstName+" "+json.arr[i].assignTo.lastName;
						assignTo.title=json.arr[i].assignTo.firstName+" "+json.arr[i].assignTo.lastName;
						
						
						var startDate=document.createElement("p");
						issueBox.appendChild(startDate);
						startDate.classList.add("showstartDate");
						startDate.textContent=json.arr[i].startDate.toString().split("-").reverse().join("-");
						startDate.title=json.arr[i].startDate.toString().split("-").reverse().join("-");
						
						var dueDate=document.createElement("p");
						issueBox.appendChild(dueDate);
						dueDate.classList.add("showdueDate");
						dueDate.textContent=json.arr[i].dueDate.toString().split("-").reverse().join("-");
						dueDate.title=json.arr[i].dueDate.toString().split("-").reverse().join("-");
						
						
						var status=document.createElement("p");
						issueBox.appendChild(status);
						status.classList.add("showstatus");
						status.textContent=json.arr[i].status;
						status.title=json.arr[i].status;
						
						
						var severity=document.createElement("severity");
						issueBox.appendChild(severity);
						severity.classList.add("showseverity");
						severity.textContent=json.arr[i].severity;
						severity.title=json.arr[i].severity;
						
						
						var classification=document.createElement("p");
						issueBox.appendChild(classification);
						classification.classList.add("showclassification");
						classification.textContent=json.arr[i].classification;
						classification.title=json.arr[i].classification;
						
						var reproducible=document.createElement("p");
						issueBox.appendChild(reproducible);
						reproducible.classList.add("showreproducible");
						reproducible.textContent=json.arr[i].reproducible;
						reproducible.title=json.arr[i].reproducible;
						
						
						var flag=document.createElement("p");
						issueBox.appendChild(flag);
						flag.classList.add("showflag");
						flag.textContent=json.arr[i].flag;
						flag.title=json.arr[i].flag;
						
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
	
	xhr.open("POST","issue/IssueGet");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("classification="+classification);
}



function updateIssue(issueId){
	console.log(issueId)
	var issueSmallList=document.getElementById("issueSmallList");
	var classificationHead=document.getElementById("classificationHead");
	var classificationCount=document.getElementById("classificationCount");
	findIssueId=issueId;
	
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				var resp=JSON.parse(this.responseText);
			
			
				if(resp.issues.statusCode==200){
			
					if(resp.issues.arr.length>0){
						classificationHead.textContent=resp.issues.arr[0].classification;
					    classificationCount.textContent="("+resp.issues.arr.length+")";
					    
					    issueSmallList.innerHTML="";
						for(var j=0;j<resp.issues.arr.length;j++){
						   if(resp.issues.arr[j].length != 0){
							   
							   
							   var smallIssueListBox=document.createElement("div");
							   issueSmallList.appendChild(smallIssueListBox);
							   smallIssueListBox.classList.add("smallIssueListBox");
							   smallIssueListBox.setAttribute("onclick","updateIssue('"+resp.issues.arr[j].issueId+"')");
							   
							   var issueName=document.createElement("p");
							   smallIssueListBox.appendChild(issueName);
							   issueName.classList.add("smallListIssueName");
							   issueName.textContent="Issue : "+resp.issues.arr[j].issueName;
							   issueName.title=resp.issues.arr[j].issueName;
							   
							   
							   var assignTo=document.createElement("p");
							   smallIssueListBox.appendChild(assignTo);
							   assignTo.classList.add("smallListIssueAssignTo");
							   /*console.log("email "+resp.issues.arr[j].assignTo.email)*/
							   assignTo.textContent="Assign to : "+resp.issues.arr[j].assignTo.email;
							   assignTo.title=resp.issues.arr[j].assignTo.email;
							   
							   
							   var Severity=document.createElement("p");
							   smallIssueListBox.appendChild(Severity);
							   Severity.classList.add(resp.issues.arr[j].severity+"Img");
							   /*Severity.textContent=resp.issues.arr[j].severity;*/
							   Severity.title=resp.issues.arr[j].severity;
							   
							   
							   var startDate=document.createElement("p");
							   smallIssueListBox.appendChild(startDate);
							   startDate.classList.add("smallListIssueStartDate");
							   startDate.textContent="Start date : "+resp.issues.arr[j].startDate;
							   startDate.title=resp.issues.arr[j].startDate;
							   
						   }
					    }
					    saveUpdateDatasInIssue(issueId);
					   
					}    	   
				}
			}
		}
	}

	
	 issueUpdatePopUp.style.transform="translateY(0%)";
	issueDetails.style.filter="blur(3px)";
	issueDetails.style.pointerEvents="none";
	
	xhr.open("POST","issue/getSmallListIssue");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("issueId="+issueId);
	
	
}



function saveUpdateDatasInIssue(issueId){
	console.log("issueId");
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				var json=JSON.parse(this.responseText);
			
					document.getElementById("updateIssue").value=json.issueName;
					document.getElementById("updateProject").textContent=json.project.projectName;
					document.getElementById("updateDes").value=json.description;
					document.getElementById("updateAssignTo").value=json.assignTo;
					document.getElementById("updateDueDate").value=json.dueDate;
					document.getElementById("status").value=json.status;
					document.getElementById("updateSeverity").value=json.severity;
					document.getElementById("updateClassification").value=json.classification;
					document.getElementById("updateReproducible").value=json.reproducible;
					document.getElementById("updateFlag").value=json.flag;
					
					var assignedTodeails=document.getElementById("updateAssignTo");
					
					 var option=document.createElement("option");
					 assignedTodeails.appendChild(option);
					 option.setAttribute("value",json.assignTo.email);
					 option.textContent=json.assignTo.email;
					 
					/* console.log(json.taskAssignTo.email+"hj")*/
					 
					 for(var i=0;i<json.teamMembers.length;i++){
						/*console.log(json.taskAssignTo.email!=json.teamMembers[i].email+"hjk  "+json.taskAssignTo.email.trim(),json.teamMembers[i].email)*/
						 if(json.assignTo.email!=json.teamMembers[i].email){
							 /*console.log(json.teamMembers[i].email);*/
							 var option1=document.createElement("option");
							 assignedTodeails.appendChild(option1);
							 option1.setAttribute("value",json.teamMembers[i].email);
							 option1.textContent=json.teamMembers[i].email;
						 }
					 }
				
			}
		}
	}
	
	xhr.open("POST","issue/saveUpdateDatasInIssue");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("issueId="+issueId);
}



var jsonObject="";

function newIssues(){ 
	
	var projectName=document.getElementById("projectName");
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				projectName.innerHTML="";
				jsonObject=JSON.parse(xhr.responseText);
				var json=jsonObject;
				if(json.statusCode==200){
					/*var option=document.createElement("option");
					projectName.appendChild(option);*/
					for(var i=0;i<json.arr.length;i++){
						var option1=document.createElement("option");
						projectName.appendChild(option1);
						option1.setAttribute("value",json.arr[i].projectName);
						option1.textContent=json.arr[i].projectName;
					}
					var projectNameVal=projectName.value;
		
		
					var newtaskAssignedTo=document.getElementById("assignTo");
					newtaskAssignedTo.innerHTML="";
					
					var val=false;
		            
		            for(var i=0;i<jsonObject.arr.length;i++){
						if(projectNameVal==jsonObject.arr[i].projectName){
				            for(var j=0;j<jsonObject.arr[i].teamMembers.length;j++){
								if(jsonObject.user==jsonObject.arr[i].teamMembers[j].email){
									val=true;
								}
							}
						}
					}
					
					if(val!=true){
						for(var i=0;i<jsonObject.arr.length;i++){
							if(projectNameVal==jsonObject.arr[i].projectName){
								for(var j=0;j<jsonObject.arr[i].teamMembers.length;j++){
									var option=document.createElement("option");
									newtaskAssignedTo.appendChild(option);
									option.setAttribute("value",jsonObject.arr[i].teamMembers[j].email);
									option.textContent=jsonObject.arr[i].teamMembers[j].email;
								}
							}
					    }
					    var opt=document.createElement("option");
					    newtaskAssignedTo.appendChild(opt);
					    opt.setAttribute("value",jsonObject.user);
					    opt.textContent=jsonObject.user;
					}
		            else{
						var opt1=document.createElement("option");
						newtaskAssignedTo.appendChild(opt1);
						opt1.setAttribute("value",jsonObject.user);
						opt1.textContent=jsonObject.user
					}
	
					
					
					
					addIssuePopUp.style.transform="translateX(170%)";
					issueDetails.style.filter="blur(3px)";
					issueDetails.style.pointerEvents="none";
				}
			}
		}
	}
	xhr.open("POST","project/getProjectDetails");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send();
	
}




function projectDropDowns(){
	var projectName=document.getElementById("projectName");
	if(jsonObject.statusCode==200){
		var newtaskAssignedTo=document.getElementById("assignTo");
		newtaskAssignedTo.innerHTML="";
		
		
		var projectName=projectName.value;
		
		var val=false;
		            
        for(var i=0;i<jsonObject.arr.length;i++){
			if(projectName==jsonObject.arr[i].projectName){
	            for(var j=0;j<jsonObject.arr[i].teamMembers.length;j++){
					if(jsonObject.user==jsonObject.arr[i].teamMembers[j].email){
						val=true;
						/*console.log(val)*/
					}
				}
			}
		}
		
		if(val!=true){
			for(var i=0;i<jsonObject.arr.length;i++){
				if(projectName==jsonObject.arr[i].projectName){
					for(var j=0;j<jsonObject.arr[i].teamMembers.length;j++){
						var option=document.createElement("option");
						newtaskAssignedTo.appendChild(option);
						option.setAttribute("value",jsonObject.arr[i].teamMembers[j].email);
						option.textContent=jsonObject.arr[i].teamMembers[j].email;
					}
				}
		    }
		    var opt=document.createElement("option");
		    newtaskAssignedTo.appendChild(opt);
		    opt.setAttribute("value",jsonObject.user);
		    opt.textContent=jsonObject.user;
		}
        else{
			var opt1=document.createElement("option");
			newtaskAssignedTo.appendChild(opt1);
			opt1.setAttribute("value",jsonObject.user);
			opt1.textContent=jsonObject.user
		}
	}
}
 
 
 function issueAddButton(){
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				var json=JSON.parse(this.responseText);
				if(json.statusCode==200){
					
					addIssuePopUp.style.transform="translate(300%)";
				    issueDetails.style.filter="blur(0px)";
				    issueDetails.style.pointerEvents="auto";
				    
				    alert(json.detailedMessage);
				    viewIssues();
				}
			}
		}
	}
	
	
	var issueName=document.getElementById("issueName").value;
	var projectName=document.getElementById("projectName").value;
	var des=document.getElementById("description").value;
	var dueDate=document.getElementById("dueDate").value;
	var assignTo=document.getElementById("assignTo").value;
	var Severity=document.getElementById("severity").value;
	var Classification=document.getElementById("classification").value;
	var Reproducible=document.getElementById("reproducible").value;
	var Flag=document.getElementById("flag").value;
	
	
	var issuePattern=/^(?=.*[A-Za-z])[A-Za-z0-9!@#\$%\^\&*\ )\(+=._-]+$/;
	
	var check=false;
	
	if(issueName.trim()=="" || issueName==""){
		alert("issue name must be filled out");
	} 
	else if(issueName.match(issuePattern)){
		if(dueDate==""){
		   dueDate=new Date();
		   check=true;
		}
		else{
			var curDate=new Date();
			endDate=new Date(dueDate);
			if(endDate.getTime()<=curDate.getTime()){
				alert("Invalid date,end date should be greater than present date");
			}
			else{
				dueDate=new Date(dueDate);
				check=true;
			}
		}
	}
	else{
		alert("Invalid issue name");
	}
	
	
	if(check){
		
		
		var addIssue={};
		addIssue.issueName=issueName;
		addIssue.projectName=projectName;
		addIssue.description=des;
		addIssue.assignTo=assignTo;
		addIssue.startDate=new Date().toUTCString();
		addIssue.dueDate=dueDate.toUTCString();
		addIssue.Severity=Severity;
		addIssue.classification=Classification;
		addIssue.reproducible=Reproducible;
		addIssue.flag=Flag;
		
		
		
		xhr.open("POST","issue/addIssue");
		xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xhr.send(JSON.stringify(addIssue));
		
		
	}
	 
	
 }
 
 
 function issueAddCancelButton(){
	addIssuePopUp.style.transform="translateX(300%)";
	issueDetails.style.filter="blur(0px)";
	issueDetails.style.pointerEvents="auto";
 }
 
 
 var issueUpdatePopUp=document.getElementById("issueUpdatePopUp");
 
 function updateDatasSave(){
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(this.readyState==4){
			if(this.status==200){
				var json=JSON.parse(this.responseText);
				if(json.statusCode==200){
					
					issueUpdatePopUp.style.transform="translateY(-200%)";
				    issueDetails.style.filter="blur(0px)";
				    issueDetails.style.pointerEvents="auto";
				    
				    alert(json.detailedMessage);
				    viewIssues();
				}
			}
		}
	}
	
	
	var issueName=document.getElementById("updateIssue").value;
	var des=document.getElementById("updateDes").value;
	var dueDate=document.getElementById("updateDueDate").value;
	var assignTo=document.getElementById("updateAssignTo").value;
	var Severity=document.getElementById("updateSeverity").value;
	var Classification=document.getElementById("updateClassification").value;
	var Reproducible=document.getElementById("updateReproducible").value;
	var Flag=document.getElementById("updateFlag").value;
	var status=document.getElementById("status").value;
	
	
	var issuePattern=/^(?=.*[A-Za-z])[A-Za-z0-9!@#\$%\^\&*\ )\(+=._-]+$/;
	
	var check=false;
	
	if(issueName.trim()=="" || issueName==""){
		alert("issue name must be filled out");
	} 
	else if(issueName.match(issuePattern)){
		if(dueDate==""){
		   dueDate=new Date();
		   check=true;
		}
		else{
			var curDate=new Date();
			endDate=new Date(dueDate);
			
			if(endDate.getTime()<curDate.getTime()){
				alert("Invalid date,end date should be greater than present date");
			}
			else{
				dueDate=new Date(dueDate);
				check=true;
			}
		}
	}
	else{
		alert("Invalid issue name");
	}
	
	
	if(check){
		
		console.log(Severity)
		
		var updateIssue={};
		updateIssue.issueId=findIssueId;
		updateIssue.issueName=issueName;
		updateIssue.projectName=projectName;
		updateIssue.description=des;
		updateIssue.assignTo=assignTo;
		updateIssue.startDate=new Date().toUTCString();
		updateIssue.dueDate=dueDate.toUTCString();
		updateIssue.Severity=Severity;
		updateIssue.classification=Classification;
		updateIssue.reproducible=Reproducible;
		updateIssue.flag=Flag;
		updateIssue.status=status;
		
		
		xhr.open("POST","issue/UpdateIssueData");
		xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xhr.send(JSON.stringify(updateIssue));
		
		
	} 
 }
 
 function updatePopUpClose(){
	 issueUpdatePopUp.style.transform="translateY(-200%)";
     issueDetails.style.filter="blur(0px)";
     issueDetails.style.pointerEvents="auto";
 }
 
 