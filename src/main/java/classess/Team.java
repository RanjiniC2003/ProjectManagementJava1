package classess;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import common.UserSingleton;

public class Team {
	private int teamId;
	private String teamName;
	private User teamLead;
	private User createBy;
	
	public Team(int teamId, String teamName, User teamLead, User createBy) {
		// TODO Auto-generated constructor stub
		this.teamId=teamId;
		this.teamName=teamName;
		this.teamLead=teamLead;
		this.createBy=createBy;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public User getTeamLead() {
		return teamLead;
	}

	public void setTeamLead(User teamLead) {
		this.teamLead = teamLead;
	}

	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}
	
	
//	public static teamClass fromTeamId(int teamId) throws SQLException {
//		PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from teamList where teamId=?");
//		psmt1.setInt(1, teamId);
//		ResultSet rs=psmt1.executeQuery();
//		if(rs.next()) {
//			return fromResultSet(rs);
//		}
//		return null;
//	}
	
	public static Team fromResultSet(ResultSet rs) throws SQLException {
		int teamId = rs.getInt("teamId");
		String teamName = rs.getString("teamName");
		
		int teamLead = rs.getInt("teamLead");
		int createBy = rs.getInt("createBy");
		
		User teamLeadUser = UserSingleton.getInstance().getUserById(teamLead);
		User createByUser = UserSingleton.getInstance().getUserById(createBy);
		
		Team team = new Team(teamId, teamName, teamLeadUser, createByUser);
		
		return team;
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("teamId", this.teamId);
	    json.put("teamName", this.teamName);
	    json.put("teamLead", this.teamLead.toJSON());
	    json.put("createBy", this.createBy.toJSON());
	    
	    return json;
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
}
