package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import classess.Task;
import classess.User;
import common.*;
import org.json.JSONArray;
import org.json.JSONObject;

import classess.Project;
import classess.Issue;


public class IssueJDBC {
	public static JSONObject addIssue(String issueName,String description,String projectName,String issueAssignTo,String std,String dDe,String severity,String classification,String reproducible,String flag,HttpServletRequest request) {
		
		JSONObject responseJSON=new JSONObject();
		
		try {
			
			int owner=0;
        	int userId=(int)request.getAttribute("userId");
        	PreparedStatement pst=Application.dbConnection.prepareStatement("select * from companyMembers where userId=?");
        	pst.setInt(1,userId);
        	ResultSet resultSet=pst.executeQuery();
        	if(resultSet.next()) {
        		owner=resultSet.getInt("invitedBy");
        	}

            int projectId=0;
         

            PreparedStatement psmt2=Application.dbConnection.prepareStatement("select * from project where createdBy=? and projectName=?");
            psmt2.setInt(1, owner);
            psmt2.setString(2, projectName);
            ResultSet rs2=psmt2.executeQuery();
            if(rs2.next()) {
                projectId=rs2.getInt("projectId");
               
            }

            Project project = ProjectSingleton.getInstance().getProjectById(projectId); 
			
			
			
			PreparedStatement psmt=Application.dbConnection.prepareStatement("insert into issue(issueName,projectId,description,issueAssignTo,startDate,dueDate,Severity,classification,reproducible,flag,status) values(?,?,?,?,?,?,?,?,?,?,?)");
			psmt.setString(1,issueName);
			psmt.setInt(2, projectId);
			psmt.setString(3,description);
			psmt.setInt(4,UserSingleton.getInstance().getByEmail(issueAssignTo).getUserId());
			
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
			
		    Date startDate=null;
		    Date dueDate=null;
			try {
				startDate = sdf.parse(std);
				dueDate= sdf.parse(dDe);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
			java.sql.Date stDate = new java.sql.Date(startDate.getTime());
            java.sql.Date duDate = new java.sql.Date(dueDate.getTime());
            psmt.setDate(5, stDate);
            psmt.setDate(6, duDate);
            psmt.setString(7,severity);
            psmt.setString(8,classification);
            psmt.setString(9, reproducible);
            psmt.setString(10, flag);
			psmt.setString(11, "Open");
            
            psmt.executeUpdate();
            
            responseJSON.put("statusCode", 200);
            responseJSON.put("statusMessage", "SUCCESS");
            responseJSON.put("detailedMessage", "Issue successfully added");
			
			return responseJSON;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			responseJSON.put("statusCode", 500);
            responseJSON.put("statusMessage", "failure");
            responseJSON.put("detailedMessage", "internal server error");
			e.printStackTrace();
			return responseJSON;
		}

	}
	
	
	public static JSONObject saveUpdateDatasInIssue(int issueId,HttpServletRequest request) {
        JSONObject json=new JSONObject();
        try {
			Issue issues= IssueSingleton.getInstance().getIssueById(issueId);
			ArrayList<User> teamMembers=TeamSingleton.getInstance().getTeamMembers(issues.getProject().getTeam().getTeamId());
			
			json=issues.toJSON();
			json.put("teamMembers", teamMembers);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return json;
        
    }


	public static JSONObject updateIssueData(String issueId,String issueName,String description,
											 String issueAssignTo,String dDe,String severity,String classification,
											 String reproducible,String flag,String status,HttpServletRequest request) {
	

		JSONObject responseJSON=new JSONObject();

		int userId=(int)request.getAttribute("userId");

		try {

			Issue issue = IssueSingleton.getInstance().getIssueById(Integer.parseInt(issueId));
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

			Date dueDate = sdf.parse(dDe);

//			if (issue.getProject().isStrict() && dueDate.getTime()>issue.getProject().getDueDate().getTime()) {
//				responseJSON.put("statusCode", 400);
//				responseJSON.put("statusMessage", "failure");
//				responseJSON.put("detailedMessage", "issue due date can't be greater than strict project due date");
//
//				return responseJSON;
//			}

			PreparedStatement psmt=Application.dbConnection.prepareStatement("update issue set issueName=?,description=?,issueAssignTo=?,dueDate=?,Severity=?,classification=?,reproducible=?,flag=?,status=? where issueId=?");
			psmt.setString(1, issueName);
			psmt.setString(2, description);
			psmt.setInt(3, UserSingleton.getInstance().getByEmail(issueAssignTo).getUserId());

			java.sql.Date duDate = new java.sql.Date(dueDate.getTime());
			psmt.setDate(4, duDate);

			psmt.setString(5,severity);
			psmt.setString(6,classification);
			psmt.setString(7, reproducible);
			psmt.setString(8, flag);
			psmt.setString(9, status);

			psmt.setInt(10, issue.getIssueId());

			psmt.executeUpdate();

			IssueSingleton.getInstance().updateIssue(issue.getIssueId());

			PreparedStatement psmt2=Application.dbConnection.prepareStatement("update companyMembers set lastUpdatedOn=? where userId=?");
			Date currDate=new Date();
			java.sql.Timestamp curDate = new java.sql.Timestamp(currDate.getTime());
			psmt2.setTimestamp(1, curDate);
			psmt2.setInt(2, userId);
			psmt2.executeUpdate();


			responseJSON.put("statusCode", 200);
			responseJSON.put("statusMessage", "SUCCESS");
			responseJSON.put("detailedMessage", "update Successful");

		} catch (Exception e) {
			responseJSON.put("statusCode", 500);
			responseJSON.put("statusMessage", "failure");
			responseJSON.put("detailedMessage", "internal server error");

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseJSON;
	}


	public static JSONObject getAllTeamIssues(HttpServletRequest request, String classification) throws SQLException {

		JSONArray arr = new JSONArray();

		JSONObject json=new JSONObject();
		HashSet<Integer> issueceoLIst = (HashSet<Integer>) request.getAttribute("ownerIssueList");
		HashSet<Integer> issueLeadLIst = (HashSet<Integer>) request.getAttribute("TeamLeadIssueList");

		if (issueceoLIst!=null) {
			for (int issueId: issueceoLIst) {
				Issue issue = IssueSingleton.getInstance().getIssueById(issueId);

				if (issue.getClassification().equals(classification)) {
					arr.put(issue.toJSON());
				}
			}
		}

		if(issueLeadLIst!=null) {
			for (int issueId: issueLeadLIst) {
				Issue issue = IssueSingleton.getInstance().getIssueById(issueId);

				if (issue.getClassification().equals(classification)) {
					arr.put(issue.toJSON());
				}
			}
		}
//        System.out.println(arr);
		
		json.put("statusCode", 200);
		json.put("message", "Success");
		json.put("arr",arr);
		return json;
	}
}
