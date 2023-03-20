

var findTaskId=0;
var addTaskPopUp=document.getElementById("addTaskPopUp");
var newTaskUpdates=document.getElementById("newTaskUpdates");

function updatePopUpClose(){
	
	newTaskUpdates.style.transform="translateY(-200%)";
	taskSummary.style.filter="blur(0px)";
	taskSummary.style.pointerEvents="auto";
	
}

function updateDatasSave(){
	/*console.log(findTaskId);*/
	var newTaskHeads=document.getElementById("newTaskHeads").value;
	var updateDescription=document.getElementById("updateDescription").value;
	var assignedTodeails=document.getElementById("assignedTodeails").value;
	var changeStatus=document.getElementById("changeStatus").value;
	var startDate=document.getElementById("startda").value;
	var dueDate=document.getElementById("dueda").value;
	var priority=document.getElementById("priority").value;
	var comPer=document.getElementById("comPer").value;	
	
	
	/*console.log(newTaskHeads+","+updateDescription+","+assignedTodeails+","+changeStatus+","+startDate+","+dueDate+","+priority+","+comPer);
	
	
	console.log("start date : "+startDate+" end date : "+dueDate);*/
	var taskPattern=/^(?=.*[A-Za-z])[A-Za-z0-9!@#\$%\^\&*\ )\(+=._-]+$/;
	 
	 var dueDateCheck=false;
	
	 
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(xhr.status==200){
				 var json=JSON.parse(this.responseText);
				 if(json.statusCode==200){
					 viewTaskList();
					 alert(json.detailedMessage);
					 taskSummary.style.pointerEvents="auto";
				     newTaskUpdates.style.transform="translateY(-200%)";
					 taskSummary.style.filter="blur(0px)";
					 
				 }
				 else{
					 alert(json.detailedMessage);
				 }
				 
			 }
		 }
	 }
	 
	 
	 if(newTaskHeads.trim()=="" && newTaskHeads==""){
		 alert("Task name must be filled out");
	 }
	 else if( newTaskHeads.match(taskPattern)){
		var curDate=new Date();
		var curSec=curDate.getTime();
		
		if(startDate=="" && dueDate==""){
			startDate=new Date();
			dueDate=new Date();
			dueDateCheck=true;
		}
		else if(startDate!="" && dueDate==""){
			startDate=new Date(startDate);
			if(startDate.getTime()<=curSec){
			   dueDate=new Date();
			   dueDateCheck=true;
			}
			else{
				alert("Please enter due date");
			}
		}
		
		else if(startDate=="" && dueDate!=""){
			if(dueDate.getTime()>=curSec){
				startDate=new Date();
				dueDate=new Date(dueDate);
				dueDateCheck=true;
			}
			else{
				alert("Please enter start date");
			}
		}
		else if(startDate!="" && dueDate!=""){
			var due=new Date(dueDate);
		    var dueSec=due.getTime();
		    startDate=new Date(startDate);
			if(startDate.getTime()<=dueSec){
				startDate=new Date(startDate);
				dueDate=new Date(dueDate);
				dueDateCheck=true;
			}
			else{
				alert("Invalid date,end date should be greater than start date");
			}
		}
	 }
	 else{
		 alert("Invalid task name");
	 }
	 
	 if(dueDateCheck){
		    xhr.open("POST","task/UpdateTaskData");
			xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			xhr.send("taskId="+findTaskId+"&newTask="+newTaskHeads+"&description="+updateDescription+"&taskStatus="+changeStatus+"&assignTo="+assignedTodeails+"&startDate="+startDate.toUTCString()+"&dueDate="+dueDate.toUTCString()+"&priority="+priority+"&completePercentage="+comPer);
			
			
	 } 
}

var newtaskAssignedTo=document.getElementById("newtaskAssignedTo");
var projectDropDown=document.getElementById("projectDropDown");

var jsonObject="";





function projectDropDowns(){
	if(jsonObject.statusCode==200){
		newtaskAssignedTo.innerHTML="";
		
		
		var projectName=projectDropDown.value;
		
		
		var val=false;
		            
        for(var i=0;i<jsonObject.arr.length;i++){
			if(projectName==jsonObject.arr[i].projectName){
	            for(var j=0;j<jsonObject.arr[i].teamMembers.length;j++){
					if(jsonObject.user==jsonObject.arr[i].teamMembers[j].email){
						val=true;
						console.log(val)
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

function taskAssign(){
/*	console.log("hgj");
	console.log(projectDropDown.value);*/
	if(projectDropDown.value==""){
		alert("please first assign any project in task");
	}
}

function openPopup(){
	var projectDropDown=document.getElementById("projectDropDown");
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				projectDropDown.innerHTML="";
				newtaskAssignedTo.innerHTML=""
				jsonObject=JSON.parse(xhr.responseText);
				var json=jsonObject;
				if(json.statusCode==200){
					
					for(var i=0;i<json.arr.length;i++){
						var option1=document.createElement("option");
						projectDropDown.appendChild(option1);
						option1.setAttribute("value",json.arr[i].projectName);
						option1.textContent=json.arr[i].projectName;
					}
					var projectName=projectDropDown.value;
		            
		            
		            var val=false;
		            
		            for(var i=0;i<jsonObject.arr.length;i++){
						if(projectName==jsonObject.arr[i].projectName){
				            for(var j=0;j<jsonObject.arr[i].teamMembers.length;j++){
								if(jsonObject.user==jsonObject.arr[i].teamMembers[j].email){
									val=true;
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
					
					
					addTaskPopUp.style.transform="translateX(100%)";
					taskSummary.style.filter="blur(3px)";	
					taskSummary.style.pointerEvents="none";
				}
			}
		}
	}
	
	xhr.open("POST","project/getProjectDetails");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send();
}

function addTaskPopup(){
	 var taskName=document.getElementById("newTask").value;
	 var projectName=document.getElementById("projectDropDown").value;
	 var description=document.getElementById("newDesciption").value;
	 var assignTo=document.getElementById("newtaskAssignedTo").value;
	 var startDate=document.getElementById("newTaskstartDate").value;
	 var dueDate=document.getElementById("newTaskDueDate").value;
	 var priority=document.getElementById("PriorityLis").value;
	/* console.log(taskName+" "+description+" "+assignTo+" "+startDate+" "+dueDate+" "+priority);*/
	
	 var taskPattern=/^(?=.*[A-Za-z])[A-Za-z0-9!@#\$%\^\&*\ )\(+=._-]+$/;
	 
	 var dueDateCheck=false;
	 
	 var xhr=new XMLHttpRequest();
	 xhr.onreadystatechange=function(){
		 if(xhr.readyState==4){
			 if(xhr.status==200){
				 var json=JSON.parse(this.responseText);
				/* console.log(json)*/
				 if(json.statusCode==200){
					 alert(json.detailedMessage);
				      viewTaskList();
				      	addTaskPopUp.style.transform="translateX(-100%)";
						taskSummary.style.filter="blur(0px)";
						taskSummary.style.pointerEvents="auto";
				 }
				 else{
					alert(json.detailedMessage);
					 
				 }
				 
			 }
		 }
	 }
	 
	 
	 if(taskName==""){
		 alert("Task name must be filled out");
	 }
	 else if(taskName.match(taskPattern)){
		var curDate=new Date();
		var curSec=curDate.getTime();
		
		if(startDate=="" && dueDate==""){
			startDate=new Date();
			dueDate=new Date();
			dueDateCheck=true;
		}
		else if(startDate!="" && dueDate==""){
			startDate=new Date(startDate);
			if(startDate.getTime()<=curSec){
			   dueDate=new Date();
			   dueDateCheck=true;
			}
			else{
				alert("Please enter due date");
			}
		}
		
		else if(startDate=="" && dueDate!=""){
			if(dueDate.getTime()>=curSec){
				startDate=new Date();
				dueDate=new Date(dueDate);
				dueDateCheck=true;
			}
			else{
				alert("Please enter start date");
			}
		}
		else if(startDate!="" && dueDate!=""){
			var due=new Date(dueDate);
		    var dueSec=due.getTime();
		    startDate=new Date(startDate);
			if(startDate.getTime()<=dueSec){
				startDate=new Date(startDate);
				dueDate=new Date(dueDate);
				dueDateCheck=true;
			}
			else{
				alert("Invalid date,end date should be greater than start date");
			}
		}
	 }
	 else{
		 alert("Invalid task name");
	 }
	 
	 if(dueDateCheck){
		 xhr.open("POST","task/addTask",false);
		 xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		 xhr.send("newTask="+taskName+"&projectName="+projectName+"&description="+description+"&assignTo="+assignTo+"&startDate="+startDate.toUTCString()+"&dueDate="+dueDate.toUTCString()+"&priority="+priority);
		 
	
	 } 
}

function cancelTaskPopup(){
	addTaskPopUp.style.transform="translateX(-100%)";
	taskSummary.style.filter="blur(0px)";
	taskSummary.style.pointerEvents="auto";
}
 

  
function viewTaskList(){
	
	/* createTaskListView("closed,inProcess,inCancelled,inReview,Open,toBeTested,onHold,delayed");*/
	 createTaskListView("closed");
	 createTaskListView("inProcess");
	 createTaskListView("inCancelled");
	 createTaskListView("inReview");
	 createTaskListView("Open");
	 createTaskListView("toBeTested");
	 createTaskListView("onHold");
	 createTaskListView("delayed");
}

function createTaskListView(status){
/*	console.log(status)*/
	var id=document.getElementById("add"+status+"Task");
	id.innerHTML="";
    
	var img=document.getElementById(status+"TaskDetails");
	var taskCount=document.getElementById(""+status+"taskCount");
	
	taskCount.textContent="";
	img.style.display="block";
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				if(this.responseText!=""){
					var resp=JSON.parse(xhr.responseText);
					console.log("length "+resp.arr.length);
						for(var j=0;j<resp.arr.length;j++){
						   if(resp.arr[j].length != 0 && resp.arr!=null){
							
							   img.style.display="none";
							   var containerBox=document.createElement("div");
								id.appendChild(containerBox);
								containerBox.classList.add("containerBox");
								containerBox.setAttribute("onclick","updateDatasPopUp('"+resp.arr[j][0].taskId+"')");
	                     
								
								/*var projectDiv=document.createElement("div");
								containerBox.appendChild(projectDiv);
								projectDiv.classList.add("projectDiv");
								projectDiv.textContent=resp.arr[j][0].*/
								
								
								var taskNamediv=document.createElement("div");
								containerBox.appendChild(taskNamediv);
								taskNamediv.classList.add("taskNameDiv");
								taskNamediv.innerHTML=resp.arr[j][0].taskName;
								taskNamediv.title=resp.arr[j][0].taskName;
								
								
								var descriptionId=document.createElement("div");
								containerBox.appendChild(descriptionId);
								descriptionId.classList.add("descriptionId");
								descriptionId.innerHTML=resp.arr[j][0].description;
								descriptionId.title=resp.arr[j][0].description;
								
								
								var priorityDateDiv=document.createElement("div");
								containerBox.appendChild(priorityDateDiv);
								priorityDateDiv.classList.add("priorityDateDiv");
							    
							    
							    var priorityImg=document.createElement("div");
							    priorityDateDiv.appendChild(priorityImg);
							    priorityImg.classList.add(resp.arr[j][0].priority+"Img");
							    priorityImg.title=resp.arr[j][0].priority;
							    
							    var startDateDiv=document.createElement("div");
							    priorityDateDiv.appendChild(startDateDiv);
							    startDateDiv.classList.add("startDateDiv");
							    startDateDiv.innerHTML=resp.arr[j][0].startDate.toString().split("-").reverse().join("-");
							    startDateDiv.title=resp.arr[j][0].startDate.toString().split("-").reverse().join("-");
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
	xhr.open("POST","task/Taskget");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("Status="+status);
	
}



function updateDatasPopUp(taskId){
	findTaskId=taskId;
	var newTaskUpdatesList=document.getElementById("newTaskUpdatesList");
	var smallListHead=document.getElementById("smallListHead");
	var smallListCount=document.getElementById("smallListCount");
	
	newTaskUpdatesList.innerHTML="";
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var resp = JSON.parse(this.responseText);
				/*console.log(resp.tasks.arr.length);
				console.log(resp.tasks.statusCode);*/
				if(resp.tasks.statusCode==200){
					var count=1;
					/*console.log(resp.arr.length);*/
					
					if(resp.tasks.arr.length>0){
						
						for(var j=0;j<resp.tasks.arr.length;j++){
						   if(resp.tasks.arr[j].length != 0){
							   if(resp.statusType==resp.tasks.arr[j][0].taskStatus){
								   smallListHead.textContent=resp.tasks.arr[j][0].taskStatus;
								   smallListCount.textContent="( "+count+" )";
									var smallListDiv=document.createElement("div");
								    newTaskUpdatesList.appendChild(smallListDiv);
								    smallListDiv.classList.add("newTaskUpdatesList");
								    smallListDiv.setAttribute("onclick","updateDatasPopUp('"+resp.tasks.arr[j][0].taskId+"')");
								    
								    var taskHead=document.createElement("div");
								    smallListDiv.append(taskHead);
								    taskHead.classList.add("taskHead");
								    taskHead.innerHTML=resp.tasks.arr[j][0].taskName;
								    taskHead.title=resp.tasks.arr[j][0].taskName;
								    
								    
								    var userDate=document.createElement("div");
								    smallListDiv.appendChild(userDate);
								    userDate.classList.add("userDate");
								    
								    var userEmail=document.createElement("div");
								    userDate.appendChild(userEmail);
								    userEmail.classList.add("userEmail");
								 /*   console.log(typeof(resp.arr[i].taskAssignTo));*/
								    
								    userEmail.innerHTML=resp.tasks.arr[j][0].taskAssignTo.email;
								    userEmail.title=resp.tasks.arr[j][0].taskAssignTo.email;
								    
								    var priority=document.createElement("div");
								    userDate.appendChild(priority);
								    priority.classList.add(resp.tasks.arr[j][0].priority+"Img");
								    priority.title=resp.tasks.arr[j][0].priority;
								    count++;
							   }
								    
						    }	
						  
					   }
				   }
					savedatasGet(taskId);
				}
				
			}
		}
	}
	
	
	xhr.open("POST","task/getSmallLIstTask");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("taskId="+taskId);
	
	newTaskUpdates.style.transform="translateY(1%)";
	taskSummary.style.filter="blur(3px)";
	taskSummary.style.pointerEvents="none";
}
var jh="";
function savedatasGet(taskId){
	/*console.log("taskId : "+taskId);*/
	var dur=document.getElementById("duration");
	var assignedTodeails=document.getElementById("assignedTodeails");
	var xh=new XMLHttpRequest();
	xh.onreadystatechange=function(){
		if(xh.readyState==4){
			if(xh.status==200){
				
				
				 jh=JSON.parse(xh.responseText);
				
					 var json=jh;
					 document.getElementById("newTaskHeads").value=json.taskName;
					 document.getElementById("pn").textContent=json.project.projectName;
					 var option=document.createElement("option");
					 assignedTodeails.appendChild(option);
					 option.setAttribute("value",json.taskAssignTo.email);
					 option.textContent=json.taskAssignTo.email;
					 
					/* console.log(json.taskAssignTo.email+"hj")*/
					 
					 for(var i=0;i<json.teamMembers.length;i++){
						/*console.log(json.taskAssignTo.email!=json.teamMembers[i].email+"hjk  "+json.taskAssignTo.email.trim(),json.teamMembers[i].email)*/
						 if(json.taskAssignTo.email!=json.teamMembers[i].email){
							 /*console.log(json.teamMembers[i].email);*/
							 var option1=document.createElement("option");
							 assignedTodeails.appendChild(option1);
							 option1.setAttribute("value",json.teamMembers[i].email);
							 option1.textContent=json.teamMembers[i].email;
						 }
					 }
					 
					
					 
				     document.getElementById("currentStatus").textContent=json.taskStatus;
					 document.getElementById("updateDescription").value=json.description;
					/* document.getElementById("assignedTodeails").value=json.taskAssignTo.email;*/
					 document.getElementById("changeStatus").value=json.taskStatus;
					 document.getElementById("startda").value=json.startDate;
					 document.getElementById("dueda").value=json.dueDate;
					 document.getElementById("priority").value=json.priority;
					 document.getElementById("comPer").value=json.completePercentage;
					 dur.textContent=json.duration;
				 }
				 	
		   
	    }
	}
	xh.open("POST","task/SaveUpdateDatas");
	xh.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xh.send("taskId="+taskId);
}
	
	





