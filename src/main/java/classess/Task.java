package classess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.json.JSONObject;

import common.ProjectSingleton;
import common.UserSingleton;

public class Task {
	private int taskId;
	private String taskName;
	private String description;
	private String status;
	private User taskAssignTo;
	private User taskCreateBy;
	private String priority;
	private Date startDate;
	private Date dueDate;
	private int duration;
	private int comPer;
	private Project project;
	
	
	public Task(int taskId, String taskName,Project project, String description, String status, User taskAssignTo, User taskCreateBy, String priority, Date startDate, Date dueDate, int duration, int comPer) {
		this.taskId=taskId;
		this.taskName=taskName;
		this.project=project;
		this.description=description;
		this.status=status;
		this.taskAssignTo=taskAssignTo;
		this.taskCreateBy=taskCreateBy;
		this.priority=priority;
		this.startDate=startDate;
		this.dueDate=dueDate;
		this.duration=duration;
		this.comPer=comPer;
	}


	public int getTaskId() {
		return taskId;
	}


	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public User getTaskAssignTo() {
		return taskAssignTo;
	}


	public void setTaskAssignTo(User taskAssignTo) {
		this.taskAssignTo = taskAssignTo;
	}


	public User getTaskCreateBy() {
		return taskCreateBy;
	}


	public void setTaskCreateBy(User taskCreateBy) {
		this.taskCreateBy = taskCreateBy;
	}


	public String getPriority() {
		return priority;
	}


	public void setPriority(String priority) {
		this.priority = priority;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getDueDate() {
		return dueDate;
	}


	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public int getComPer() {
		return comPer;
	}


	public void setComPer(int comPer) {
		this.comPer = comPer;
	}
	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}


	public static Task fromResultSet(ResultSet rs) throws SQLException {
	   int taskId=rs.getInt("taskId");
	   String taskName=rs.getString("taskName");
	   Project project=ProjectSingleton.getInstance().getProjectById(rs.getInt("projectId"));
	   String description=rs.getString("description");
	   String taskStatus=rs.getString("taskStatus");
	   User taskAssignTo=UserSingleton.getInstance().getUserById(rs.getInt("taskAssignedTo"));
	   User taskCreatedBy=UserSingleton.getInstance().getUserById(rs.getInt("taskCreatedBy"));
	   String priority=rs.getString("priority");
	   Date startDate=rs.getDate("startDate");
	   Date dueDate=rs.getDate("dueDate");
	   int duration=rs.getInt("duration");
	   int completePercentage=rs.getInt("completePercentage");
	   
	   Task task =  new Task(taskId, taskName,project, description, taskStatus,taskAssignTo, taskCreatedBy, priority, startDate, dueDate, duration, completePercentage);
	  
	   return task;
	}
  
	
	public JSONObject toJSON() {
		JSONObject json=new JSONObject();
		json.put("taskId",this.taskId);
		json.put("taskName", this.taskName);
		json.put("project", this.project.toJSON());
		json.put("description", this.description);
		json.put("taskStatus", this.status);
		json.put("taskAssignTo", this.taskAssignTo.toJSON());
		json.put("taskCreatedBy", this.taskCreateBy.toJSON());
		json.put("priority", this.priority);
		json.put("startDate",this.startDate);
		json.put("dueDate", this.dueDate);
		json.put("duration",this.duration);
		json.put("completePercentage",this.comPer);
		
		return json;

	}
	
	public String toString() {
		return this.toJSON().toString();
	}
	
}
