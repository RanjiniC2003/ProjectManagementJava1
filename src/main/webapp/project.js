
var productAddPopUp=document.getElementById("productAddPopUp");
var productDetails=document.getElementById("productDetails");
var findProjectId=0;


function addproductPopup(){
	
	
	productDetails.style.filter="blur(3px)";	
	productAddPopUp.style.transform="translateX(146%)";
	productDetails.style.pointerEvents="none";
}



function productAddCancelButton(){
	productDetails.style.filter="blur(0px)";	
	productDetails.style.pointerEvents="auto";
	productAddPopUp.style.transform="translateX(300%)";
}




var selectTeamName=document.getElementById("selectTeamName");
function viewProjectList(){
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
		/*	console.log(xhr.status)*/
			if(xhr.status==200){
				/*console.log(xhr.responseText)*/
				var json=JSON.parse(xhr.responseText);
				if(json.statusCode==200){
					if(json.role=="CEO"){
						var addproductpopup=document.getElementById("addproductpopup");
						addproductpopup.style.display="block";
					}
					/*console.log(json.arr1.length);*/
					if(json.arr1.length>0){
						for(var i=0;i<json.arr1.length;i++){
							 var option=document.createElement("option");
						     selectTeamName.appendChild(option);
						  
						     option.setAttribute("value",json.arr1[i].teamName);
						     
						    /* console.log(json.arr1[i].email);*/
						     option.textContent=json.arr1[i].teamName;
						}
						new MultiSelectTag('selectTeamName')
					}
					else{
						alert("No team in your company");
					}
					viewProjects();
				}
			}
			else if (xhr.status==301) {
				let location = "/zohoProjectManagement/index.html"

				if (location) {
					window.location.href = location;
				}
			}
		}
	}
	xhr.open("POST","team/getTeamDetails");
	xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xhr.send();
}

function viewProjects(){
	var viewProjectList=document.getElementById("viewProjectList");
	viewProjectList.innerHTML="";
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var resp=JSON.parse(xhr.responseText);
				if(resp.statusCode==200){
					if(resp.arr.length>0){
						for(var i=0;i<resp.arr.length;i++){
							var projectBox=document.createElement("div");
							viewProjectList.appendChild(projectBox);
							projectBox.classList.add("projectBox");
							projectBox.setAttribute("onclick","UpdateProject('"+resp.arr[i].projectId+"')");
							
							var productName=document.createElement("p");
							projectBox.appendChild(productName);
							productName.classList.add("productName");
							productName.textContent=resp.arr[i].projectName;
							productName.title= resp.arr[i].projectName;
							
							
							var productDescription=document.createElement("p");
							projectBox.appendChild(productDescription);
							productDescription.classList.add("productDescription");
							
							if(resp.arr[i].description=="" || resp.arr[i].description.trim()==""){
								productDescription.textContent="-";
							    productDescription.title="-";
							}
							else{
								productDescription.textContent=resp.arr[i].description;
							    productDescription.title=resp.arr[i].description;
							}
							
							
							var productPer=document.createElement("p");
							projectBox.appendChild(productPer);
							productPer.classList.add("productPer");
						
							
							
							var productTeamName=document.createElement("p");
							projectBox.appendChild(productTeamName);
							productTeamName.classList.add("productTeamName");
							productTeamName.textContent=resp.arr[i].team.teamName;
							productTeamName.title=resp.arr[i].team.teamName;
							
							
							var productStatus=document.createElement("p");
							projectBox.appendChild(productStatus);
							productStatus.classList.add("productStatus");
							productStatus.textContent=resp.arr[i].status;
							productStatus.title=resp.arr[i].status;
							
							
							var productStartDate=document.createElement("p");
							projectBox.appendChild(productStartDate);
							productStartDate.classList.add("productStartDate");
							productStartDate.textContent=resp.arr[i].startDate.toString().split("-").reverse().join("-");
							productStartDate.title=resp.arr[i].startDate.toString().split("-").reverse().join("-");
							
							var productDueDate=document.createElement("p");
							projectBox.appendChild(productDueDate);
							productDueDate.classList.add("productDueDate");
							
							
						/*	
						console.log(resp.arr[i].endDate)*/
							if(resp.arr[i].endDate!=undefined){
								productDueDate.textContent=resp.arr[i].endDate.toString().split("-").reverse().join("-");
							    productDueDate.title=resp.arr[i].endDate.toString().split("-").reverse().join("-");
							}
							else if(resp.arr[i].endDate==undefined){
								productDueDate.textContent="-";
							    productDueDate.title="-";
							}
							
							var productCreateOn=document.createElement("p");
							projectBox.appendChild(productCreateOn);
							productCreateOn.classList.add("productCreateOn");
							productCreateOn.textContent=resp.arr[i].createdOn;
							productCreateOn.title=resp.arr[i].createdOn;
							
							
							var productModifiedOn=document.createElement("p");
							projectBox.appendChild(productModifiedOn);
							productModifiedOn.classList.add("productModifiedOn");
							productModifiedOn.textContent=resp.arr[i].modifiedOn;
							productModifiedOn.title=resp.arr[i].modifiedOn;
							
							
							var productcreatedBy=document.createElement("p");
							projectBox.appendChild(productcreatedBy);
							productcreatedBy.classList.add("productcreatedBy");
							productcreatedBy.textContent=resp.arr[i].createdBy.firstName+" "+resp.arr[i].createdBy.lastName;
							productcreatedBy.title=resp.arr[i].createdBy.firstName+" "+resp.arr[i].createdBy.lastName;
							
							
							var productmodifiedBy=document.createElement("p");
							projectBox.appendChild(productmodifiedBy);
							productmodifiedBy.classList.add("productmodifiedBy");
							productmodifiedBy.title=resp.arr[i].modifiedBy.firstName+" "+resp.arr[i].modifiedBy.lastName;
							productmodifiedBy.textContent=resp.arr[i].modifiedBy.firstName+" "+resp.arr[i].modifiedBy.lastName;
						}
					}
					
				}
			}
		}
	}
	
	xhr.open("post","project/getProjectDetails");
	xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xhr.send();
}

var updateProjectDatas=document.getElementById("updateProjectDatas");

function UpdateProject(projectId){
	findProjectId=projectId;

	
	
	saveProjectDatas(projectId);
	getSmallListProject(projectId);

	
	updateProjectDatas.style.transform ="translateY(1%)";
	productDetails.style.filter="blur(3px)";
	productDetails.style.pointerEvents="none";
}


function getSmallListProject(projectId){

	var updateProjectDatasLeft=document.getElementById("updateProjectDatasLeft");
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var json=JSON.parse(xhr.responseText);
				if(json.statusCode==200){
					updateProjectDatasLeft.innerHTML="";
					for(var i=0;i<json.arr.length;i++){
						
						var newProjectSmallList=document.createElement("div");
						updateProjectDatasLeft.appendChild(newProjectSmallList);
						newProjectSmallList.classList.add("newProjectSmallList");
						newProjectSmallList.setAttribute("onclick","UpdateProject('"+json.arr[i].projectId+"')");
						
						
						var smallListProjectName=document.createElement("p");
						newProjectSmallList.appendChild(smallListProjectName);
						smallListProjectName.classList.add("smallListProjectName");
						smallListProjectName.textContent="projectName : "+json.arr[i].projectName;
						smallListProjectName.title=json.arr[i].projectName;
						
						var smallListTeamName=document.createElement("p");
						newProjectSmallList.appendChild(smallListTeamName);
						smallListTeamName.classList.add("smallListTeamName");
						smallListTeamName.textContent="Team Name : "+json.arr[i].team.teamName;
						smallListTeamName.title=json.arr[i].team.teamName;
						
						
						var smallListStartDate=document.createElement("p");
						newProjectSmallList.appendChild(smallListStartDate);
						smallListStartDate.classList.add("smallListStartDate");
						smallListStartDate.textContent="Start date : "+json.arr[i].startDate.toString().split("-").reverse().join("-");
						smallListStartDate.title=json.arr[i].startDate;
						
						
					}
					
					
				}
			}
		}
	}
	
	xhr.open("POST","project/getSmallListProject");
	xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xhr.send("projectId="+projectId);
}



function saveProjectDatas(ProjectId){
	/*console.log(ProjectId);*/
	var xh=new XMLHttpRequest();
	xh.onreadystatechange=function(){
		if(xh.readyState==4){
			if(xh.status==200){
				var json=JSON.parse(xh.responseText);
				
				document.getElementById("teamNameInProj").textContent=json.team.teamName;
				document.getElementById("updateproductName").value=json.projectName;
				document.getElementById("cStatus").textContent=json.status;
			    document.getElementById("updateDescription").value=json.description;
			    document.getElementById("upStaDate").value=json.startDate;
			    document.getElementById("upduDate").value=json.endDate;
			    document.getElementById("updateStatus").value=json.status;
	
		   }
	    }
	}
	xh.open("POST","project/saveUpdateProject");
	xh.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xh.send("projectId="+ProjectId);
}



function productAddButton(){
	var productName=document.getElementById("productName").value;
	var teamName="";
	var startDate=document.getElementById("startDate").value;
	var dueDate=document.getElementById("dueDate").value;
	var strict=document.getElementById("strict").checked;
	var description=document.getElementById("description").value;
	var privateType=document.getElementById("privateType").checked;
	var publicType=document.getElementById("publicType").checked;
	
	
	
	var dueDateCheck=false;
	
	 var projectPattern=/^[A-Za-z\ ]+$/;  
	 if(productName.trim()=="" && productName==""){
		 
		alert("Project name must be filled out");
	 }
	 else if(productName.match(projectPattern)){
		var inputContainer=document.getElementsByClassName("input-container");
		var childNodes=inputContainer[0].childNodes;
		if(strict && dueDate==""){
			alert("This project was Strict project.Kindly provide a due date to project")
		}
		else{
			if(childNodes.length==0){
				alert("please assign any team in project");
			}
			else if(childNodes.length==1){
				teamName=childNodes[childNodes.length-1].childNodes[0].textContent;
				
				
				if(teamName=="" && publicType==false){
					alert("please assign a team or publicType ");
				}
				else if(teamName!="" && publicType){
					alert("please assign any one.don't assign teamName and publicType");
				}
				else{
					
					if(startDate=="" && dueDate==""){
						startDate=new Date();
						dueDate=null;
						dueDateCheck=true;
					}
					else if(startDate!="" && dueDate==""){
						startDate=new Date(startDate);
						dueDate=null;
						dueDateCheck=true;
					}
					
					else if(startDate=="" && dueDate!=""){
					
						dueDate=new Date(dueDate);
						var curSec=new Date();
						if(dueDate.getTime()>=curSec.getTime()){
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
			}
			else{
				alert("please assign one team in project");
			}
		}
			
	}
	else{
		alert("Invalid project Name");
	}
	   	
	
	if(teamName!="" && !publicType){
		privateType=true;
	}
	else if(teamName=="" && publicType){
		privateType=false;
	}
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
			/*	console.log(xhr.responseText);*/
				var resp=JSON.parse(xhr.responseText);
				if(resp.statusCode==200){
					alert(resp.detailedMessage);
					viewProjectList()
					productDetails.style.filter="blur(0px)";	
					productDetails.style.pointerEvents="auto";
		            productAddPopUp.style.transform="translateX(300%)";
				}
				else{
					alert(resp.detailedMessage);
				}
			}
		}
	}
	/*console.log(strict+" , "+privateType+" , "+publicType+" , "+startDate);*/
	if(dueDateCheck){
		xhr.open("POST","project/addProduct");
		xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		if(publicType && dueDate!=null){
			xhr.send("projectName="+productName+"&team="+teamName+"&startDate="+startDate.toUTCString()+"&dueDate="+dueDate.toUTCString()+"&strict="+strict+"&publicType="+publicType+"&description="+description);
		}
		else if(publicType && dueDate==null){
			xhr.send("projectName="+productName+"&team="+teamName+"&startDate="+startDate.toUTCString()+"&dueDate="+dueDate+"&strict="+strict+"&publicType="+publicType+"&description="+description);
		}
		else if(!publicType && dueDate!=null){
			xhr.send("projectName="+productName+"&team="+teamName+"&startDate="+startDate.toUTCString()+"&dueDate="+dueDate.toUTCString()+"&strict="+strict+"&publicType="+privateType+"&description="+description);
		}
		else if(!publicType && dueDate==null){
			xhr.send("projectName="+productName+"&team="+teamName+"&startDate="+startDate.toUTCString()+"&dueDate="+dueDate+"&strict="+strict+"&publicType="+privateType+"&description="+description);
		}
		
		 												
		
	}
	
}


function updateDatasSave(){
	var projectId=findProjectId;
	var productName=document.getElementById("updateproductName").value;
	var productDes=document.getElementById("updateDescription").value;
	var startDate=document.getElementById("upStaDate").value;
	var dueDate=document.getElementById("upduDate").value;
	var status=document.getElementById("updateStatus").value;
	
	/*console.log(projectId,productName,productDes,startDate,dueDate,status);*/
	
	var updateProjectObject={};
	
	
	
	var dueDateCheck=false;
	
	 var projectPattern=/^[A-Za-z\ ]+$/;  
	 if(productName.trim()=="" && productName==""){
		alert("Project name must be filled out");
	 }
	 else if(productName.match(projectPattern)){		
		if(startDate=="" && dueDate==""){
			startDate=new Date();
			dueDate=null;
			dueDateCheck=true;
		}
		else if(startDate!="" && dueDate==""){
			startDate=new Date(startDate);
			dueDate=null;
			dueDateCheck=true;
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
		alert("Invalid project Name");
	}
	
	var xhr=new XMLHttpRequest();
	xhr.onreadystatechange=function(){
		if(xhr.readyState==4){
			if(xhr.status==200){
				var json=JSON.parse(xhr.responseText);
				if(json.statusCode==200){
					alert(json.detailedMessage);
					viewProjectList();
					updateProjectDatas.style.transform="translateY(-200%)";
					productDetails.style.filter="blur(0px)";
					productDetails.style.pointerEvents="auto";
					
				}
				else{
					alert(json.detailedMessage);
				}
			}
		}
	}
	
	if(dueDateCheck){
		if(dueDate!=null){
			updateProjectObject.projectId=projectId;
			updateProjectObject.projectName=productName;
			updateProjectObject.description=productDes;
			updateProjectObject.startDate=startDate.toUTCString();
			updateProjectObject.endDate=dueDate.toUTCString();
			updateProjectObject.statusType=status;
		}
		else{
			updateProjectObject.projectId=projectId;
			updateProjectObject.projectName=productName;
			updateProjectObject.description=productDes;
			updateProjectObject.startDate=startDate.toUTCString();
			updateProjectObject.endDate=dueDate;
			updateProjectObject.statusType=status;
		}

		xhr.open("POST","project/updateProjectDetails");
		xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xhr.send(JSON.stringify(updateProjectObject));
	}
	
	
	
}

function updatePopUpClose(){
	updateProjectDatas.style.transform="translateY(-200%)";
	productDetails.style.filter="blur(0px)";
	productDetails.style.pointerEvents="auto";
}



