package classess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.json.JSONObject;

import common.Application;
import common.UserSingleton;

public class CompanyMembers {
	private Company company;
	private User user;
	private Timestamp joindate;
	private Timestamp lastUpdatedOn;
	private User invitedBy;
	
	
	public CompanyMembers(Company company, User user, Timestamp joindate, Timestamp lastUpdatedOn, User invitedBy) {
		this.company=company;
		this.user=user;
		this.joindate=joindate;
		this.lastUpdatedOn=lastUpdatedOn;
		this.invitedBy=invitedBy;
	}
	
	
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Timestamp getJoinUpdate() {
		return joindate;
	}
	public void setJoinUpdate(Timestamp joinUpdate) {
		this.joindate = joinUpdate;
	}
	public Timestamp getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	public User getInvitedBy() {
		return invitedBy;
	}
	public void setInvitedBy(User invitedBy) {
		this.invitedBy = invitedBy;
	}
	
	
	public static CompanyMembers companyMember(int userId) throws SQLException {
		
//		System.out.println(userId);
		PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from companyMembers where userId=?");
		psmt.setInt(1, userId);
		ResultSet rs=psmt.executeQuery();
		
		if (rs.next()) {
			
			return fromResultSet(rs);
		}
		
		
		return null;
	}
	
	public static CompanyMembers fromResultSet(ResultSet rs) throws SQLException {
//		System.out.println(rs.getInt("userId"));
		Company company=Company.fromCompanyId(rs.getInt("companyId"));
		User userId=UserSingleton.getInstance().getUserById(rs.getInt("userId"));
		Timestamp joinDate=rs.getTimestamp("joinDate");
		Timestamp lastUpdatedOn=rs.getTimestamp("lastUpdatedOn");
		User invitedBy=UserSingleton.getInstance().getUserById(rs.getInt("invitedBy"));
		
		CompanyMembers companyMembers=new CompanyMembers(company, userId, joinDate, lastUpdatedOn, invitedBy);
//		System.out.println(companyMembers);
		return companyMembers;
	}
	
	public JSONObject toJSON() {
		JSONObject json=new JSONObject();
		json.put("company", this.company.toJSON());
		json.put("user", this.user.toJSON());
		json.put("joinDate", this.joindate);
		json.put("lastUpdatedOn",this.lastUpdatedOn);
		json.put("inviteBy",this.invitedBy.toJSON());
		return json;
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
}
