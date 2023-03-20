package classess;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

//import com.mysql.cj.protocol.Resultset;

import common.ProjectSingleton;
import common.UserSingleton;


public class Issue {
	private int issueId;
	private String issueName;
	private String description;
	private Project project;
	private User assignTo;
	private Date startDate;
	private Date dueDate;
	private String Severity; 
	private String classification;
	private String reproducible;
	private String flag;
	private String status;
	
	
	public Issue(int issueId, String issueName, String description, Project projectName, User assignTo, Date startDate, Date dueDate, String Severity, String classification, String reproducible, String flag,String status) {
		this.issueId=issueId;
		this.issueName=issueName;
		this.description=description;
		this.project =projectName;
		this.assignTo=assignTo;
		this.startDate=startDate;
		this.dueDate=dueDate;
		this.Severity=Severity;
		this.classification=classification;
		this.reproducible=reproducible;
		this.flag=flag;
		this.status=status;
	}


	public int getIssueId() {
		return issueId;
	}


	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}


	public String getIssueName() {
		return issueName;
	}


	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Project getProject() {
		return project;
	}


	public void setProject(Project project) {
		this.project = project;
	}


	public User getAssignTo() {
		return assignTo;
	}


	public void setAssignTo(User assignTo) {
		this.assignTo = assignTo;
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


	public String getSeverity() {
		return Severity;
	}


	public void setSeverity(String severity) {
		Severity = severity;
	}


	public String getClassification() {
		return classification;
	}


	public void setClassification(String classification) {
		this.classification = classification;
	}


	public String getReproducible() {
		return reproducible;
	}


	public void setReproducible(String reproducible) {
		this.reproducible = reproducible;
	}


	public String getFlag() {
		return flag;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public static Issue fromResult(ResultSet rs) throws SQLException {
		int issueId=rs.getInt("issueId");
		String issueName=rs.getString("issueName");
		Project project=ProjectSingleton.getInstance().getProjectById(rs.getInt("projectId"));
		String desciption=rs.getString("description");
		User assignedTo=UserSingleton.getInstance().getUserById(rs.getInt("issueAssignTo"));
		Date startDate=rs.getDate("startDate");
		Date dueDate=rs.getDate("dueDate");
		String Severity=rs.getString("Severity");
		String classification=rs.getString("classification");
		String reproducible=rs.getString("reproducible");
		String flag=rs.getString("flag");
		String status=rs.getString("status");
		
		return new Issue(issueId,issueName,desciption,project,assignedTo,startDate,dueDate,Severity,classification,reproducible,flag,status);
		
	}

    public JSONObject toJSON() {
		JSONObject json=new JSONObject();
		json.put("issueId",this.issueId);
		json.put("issueName",this.issueName);
		json.put("project", this.project.toJSON());
		json.put("description", this.description);
		json.put("assignTo",this.assignTo.toJSON());
		json.put("startDate",this.startDate);
		json.put("dueDate",this.dueDate);
		json.put("severity", this.Severity);
		json.put("classification", this.classification);
		json.put("reproducible", this.reproducible);
		json.put("flag", this.flag);
		json.put("status", this.status);
		
		return json;
		
	}

     public String toString() {
    	 return this.toJSON().toString();
     }

}




































