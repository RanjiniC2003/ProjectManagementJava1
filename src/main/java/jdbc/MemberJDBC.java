package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import classess.CompanyMembers;
import common.Application;
import common.CookieManagement;
import common.UserSingleton;

public class MemberJDBC {
	public static JSONObject getTheirTeamMembers(HttpServletRequest request) {
        JSONArray arr1=new JSONArray();
		
		JSONObject json2=new JSONObject();
		
		String role=(String) request.getAttribute("companyRole");
		System.out.println(role);
		HashSet<Integer> companyMembers=(HashSet<Integer>) request.getAttribute("companyMembers");
		
		if (companyMembers!=null) {
			for(int memberId:companyMembers) {
				try {
					
					
					
//					System.out.println(memberId);
					CompanyMembers companyMemberDetails=CompanyMembers.companyMember(memberId);
					
					/* System.out.println(joinDate); */
					
					JSONObject json=new JSONObject();
					json=UserSingleton.getInstance().getUserById(memberId).toJSON();
					json.put("companyMemberDetails", companyMemberDetails.toJSON());
					
					
					arr1.put(json);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					json2.put("statusCode", 500);
					json2.put("message","not successFully");
					return json2;
				}
			}
			
			
//			System.out.println(arr1);
			json2.put("role", role);
			json2.put("statusCode", 200);
            json2.put("messege", "success");
            json2.put("arr1", arr1);
            return json2;
		}
		    json2.put("role", role);
			json2.put("statusCode", 500);
			json2.put("message","Failed");
			json2.put("arr1", arr1);
			return json2;
	}
		
	
//	public static JSONObject updateMemberDetails() {
//		JSONObject json=new JSONObject();
//		
//		return json;
//	}
}
