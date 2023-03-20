package classess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import common.Application;
import common.UserSingleton;

public class Company {
	private int companyId;
	private String companyName;
	private User superUser;
	
	public Company(int companyId, String companyName, User superUser) {
		this.companyId=companyId;
		this.companyName=companyName;
		this.superUser=superUser;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public User getSuperUser() {
		return superUser;
	}

	public void setSuperUser(User superUser) {
		this.superUser = superUser;
	}
	
	
	
	public static Company fromCompanyId(int companyId) throws SQLException {
		PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from company where companyId=?");
		psmt1.setInt(1, companyId);
		ResultSet rs=psmt1.executeQuery();
		if (rs.next()) {
			return fromResultSet(rs);
		}
		return null;
	}
	
	
	public static Company fromResultSet(ResultSet rs) throws SQLException {
		int companyId=rs.getInt("companyId");
		String companyName=rs.getString("companyName");
		User superUser=UserSingleton.getInstance().getUserById(rs.getInt("superUserId"));
		
		return new Company(companyId, companyName, superUser);
		
	}
	
	
	public JSONObject toJSON() {
		JSONObject json=new JSONObject();
		json.put("companyId",this.companyId);
		json.put("companyName",this.companyName);
		json.put("superUser", this.superUser);
		
		return json;
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
	
}
