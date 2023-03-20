package classess;

//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

//import common.Application;

public class User {
	private int userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private long phoneNumber;
	
	
	
	public User(int userId, String firstName, String lastName, String email, String password, long phoneNumber) {
		this.userId=userId;
		this.firstName=firstName;
		this.lastName=lastName;
		this.email=email;
		this.password=password;
		this.phoneNumber=phoneNumber;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getlastName() {
		return lastName;
	}


	public void setlastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public long getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
//
//	public static userClass fromUserId(int userId) throws SQLException {
//		PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from users where userId=?");
//		psmt.setInt(1, userId);
//		ResultSet rs=psmt.executeQuery();
//		if (rs.next()) {
//			return fromResultSet(rs);
//		}
//
//		return null;
//	}

//	public static userClass fromEmail(String email) throws SQLException {
//		PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from users where email=?");
//		psmt.setString(1, email);
//		ResultSet rs=psmt.executeQuery();
//		if (rs.next()) {
//			return fromResultSet(rs);
//		}
//
//		return null;
//	}
	
	public static User fromResultSet(ResultSet rs) throws SQLException {
		
			int userId=rs.getInt("userId");
			String firstName=rs.getString("firstName");
			String lastName=rs.getString("lastName");
			String email=rs.getString("email");
			String password=rs.getString("password");
			long phoneNumber=rs.getLong("phoneNumber");
//			CompanyMembers companyMember=CompanyMembers.companyMember(rs.getInt("userId"));
	
		return new User(userId, firstName, lastName, email, password, phoneNumber);
		
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("userId", this.userId);
	    json.put("firstName", this.firstName);
	    json.put("lastName", this.lastName);
	    json.put("email", this.email);
//	    json.put("password", this.password);
	    json.put("phoneNumber", this.phoneNumber);
	    
	    
	    return json;
	}
	
	public String toString() {
		return this.toJSON().toString();
	}
	
}
