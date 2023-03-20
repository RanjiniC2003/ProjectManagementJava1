package classess;

//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.json.JSONObject;

//import common.Application;
import common.TeamSingleton;
import common.UserSingleton;

public class Project {
   private int projectId;
   private String projectName;
   private Team team;
   private Date startDate;
   private Date dueDate;
   private boolean strict;
   private boolean publicType;
   private String description;
   private String Status;
   private Timestamp createOn;
   private Timestamp modifiedOn;
   private User createdBy;
   private User modifiedBy;
   
   
   public Project(int projectId, String projectName, Team team, Date startDate, Date dueDate, boolean strict, boolean publicType, String description, String Status, Timestamp createOn, Timestamp modifiedOn, User createdBy, User modifiedBy) {
	   this.projectId=projectId;
	   this.projectName=projectName;
	   this.team=team;
	   this.startDate=startDate;
	   this.dueDate=dueDate;
	   this.strict=strict;
	   this.publicType=publicType;
	   this.description=description;
	   this.Status=Status;
	   this.createOn=createOn;
	   this.modifiedOn=modifiedOn;
	   this.createdBy=createdBy;
	   this.modifiedBy=modifiedBy;
   }


	public int getProjectId() {
		return projectId;
	}
	
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	
	public String getProjectName() {
		return projectName;
	}
	
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	public Team getTeam() {
		return team;
	}
	
	
	public void setTeam(Team team) {
		this.team = team;
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
	
	
	public boolean isStrict() {
		return strict;
	}
	
	
	public void setStrict(boolean strict) {
		this.strict = strict;
	}
	
	
	public boolean isPublicType() {
		return publicType;
	}
	
	
	public void setPublicType(boolean publicType) {
		this.publicType = publicType;
	}
	
	
	public String getDescription() {
		return description;
	}
	
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getStatus() {
		return Status;
	}
	
	
	public void setStatus(String status) {
		Status = status;
	}
	
	
	public Timestamp getCreateOn() {
		return createOn;
	}
	
	
	public void setCreateOn(Timestamp createOn) {
		this.createOn = createOn;
	}
	
	
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}
	
	
	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	
	public User getCreatedBy() {
		return createdBy;
	}
	
	
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	
	public User getModifiedBy() {
		return modifiedBy;
	}
	
	
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	 
	
//	public static projectClass fromProjectId(int projectId) throws SQLException {
//		PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from project where projectId=?");
//		psmt1.setInt(1, projectId);
//		ResultSet rs=psmt1.executeQuery();
//		if(rs.next()) {
//			return fromResultSet(rs);
//		}
//		return null;
//	}
    
	public static Project fromResultSet(ResultSet rs) throws SQLException {
	   int projectId=rs.getInt("projectId");
	   String projectName=rs.getString("projectName");
	   Team team=TeamSingleton.getInstance().getTeamById(rs.getInt("teamId"));
	   Date startDate=rs.getDate("startDate");
	   Date dueDate=rs.getDate("endDate");
	   boolean strict=rs.getBoolean("strict");
	   boolean publicType=rs.getBoolean("public");
	   String description=rs.getString("description");
	   String Status=rs.getString("status");
	   Timestamp createdOn=rs.getTimestamp("createdOn");
	   Timestamp modifiedOn=rs.getTimestamp("modifiedOn");
	   User createdBy=UserSingleton.getInstance().getUserById(rs.getInt("createdBy"));
	   User modifiedBy=UserSingleton.getInstance().getUserById(rs.getInt("modifiedBy"));
	   

	   return new Project(projectId, projectName,team,startDate,dueDate, strict, publicType, description, Status,createdOn,modifiedOn,createdBy,modifiedBy);
		
	}
  
	
	public JSONObject toJSON() {
		JSONObject json=new JSONObject();
		json.put("projectId",this.projectId);
		json.put("projectName", this.projectName);
		json.put("team", this.team.toJSON());
		json.put("startDate", this.startDate);
		json.put("endDate", this.dueDate);
		json.put("strict", strict);
		json.put("publicType", this.publicType);
		json.put("description",this.description);
		json.put("status", this.Status);
		json.put("createdOn",this.createOn);
		json.put("modifiedOn",this.modifiedOn);
		json.put("createdBy", this.createdBy.toJSON());
		json.put("modifiedBy",this.modifiedBy.toJSON());
		
		return json;

	}
	
	public String toString() {
		return this.toJSON().toString();
	}
}

