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
import classess.Project;
import classess.Task;
import classess.User;
import common.UserSingleton;
import common.ProjectSingleton;
import common.TeamSingleton;
import org.json.JSONArray;
import org.json.JSONObject;
import common.Application;
import common.MailService;

public class ProjectJDBC {
	public static JSONObject addProject(String projectName,String team,String startDate,String endDate,String strict,String publicType,String description,String status,HttpServletRequest request) {
		
//		System.out.println(strict);
		JSONObject json=new JSONObject();
		int userId=(int)request.getAttribute("userId");
		String role=(String) request.getAttribute("companyRole");
		
		if(role.equals("CEO")){
			int teamId=0;
            int teamCreateBy=0;
//			System.out.println(team);
			PreparedStatement psmt1;
			try {
				psmt1 = Application.dbConnection.prepareStatement("select * from teamList where teamName=?");
				psmt1.setString(1, team);
				ResultSet rs1=psmt1.executeQuery();
				if(rs1.next()) {
					teamId=rs1.getInt("teamId");
					teamCreateBy=rs1.getInt("createBy");
				}
				PreparedStatement p=Application.dbConnection.prepareStatement("select * from project where teamId=? and  projectName=? and createdBy=?");
				p.setInt(1, teamId);
				p.setString(2, projectName);
				p.setInt(3,teamCreateBy);
				ResultSet r1=p.executeQuery();
				if(r1.next()) {
					json.put("statusCode", 500);
					json.put("message", "Failure");
					json.put("detailedMessage", "This project is already create your company.Please create new project");
					
					return json;
				}
				else {
					String userName=null;
					User user = UserSingleton.getInstance().getUserById(userId);

					if(user != null) {
						userName=user.getFirstName();
					}
					
					PreparedStatement psmt2=Application.dbConnection.prepareStatement("insert into project(projectName,teamId,startDate,endDate,strict,public,description,status,createdOn,modifiedOn,createdBy,modifiedBy) values(?,?,?,?,?,?,?,?,?,?,?,?)");
					psmt2.setString(1, projectName);
					psmt2.setInt(2, teamId);
					
		            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
		            
		            if(endDate.equals("null")) {
		            	Date sd= sdf.parse(startDate);
		            	java.sql.Date stDate = new java.sql.Date(sd.getTime());
						psmt2.setDate(3, stDate);
						psmt2.setDate(4, null);
		            }
		            else if(!endDate.equals("null")) {
		            	Date sd=sdf.parse(startDate);
						
						Date ed=sdf.parse(endDate);
					
		            	java.sql.Date stDate = new java.sql.Date(sd.getTime());
		            	java.sql.Date dDate = new java.sql.Date(ed.getTime());
						psmt2.setDate(3, stDate);
						psmt2.setDate(4,dDate );
		            }
		          
		          
		            
		            
					
					
					psmt2.setBoolean(5, Boolean.parseBoolean(strict));
					psmt2.setBoolean(6, Boolean.parseBoolean(publicType));
					psmt2.setString(7, description);
					psmt2.setString(8, status);
					
					Date createdOn=new Date();
					Date modifiedOn=new Date();
		            java.sql.Timestamp creDate = new java.sql.Timestamp(createdOn.getTime());
		            java.sql.Timestamp modiDate=new java.sql.Timestamp(modifiedOn.getTime());
					psmt2.setTimestamp(9, creDate);
					psmt2.setTimestamp(10,modiDate);
					
					
					psmt2.setInt(11, userId);
					psmt2.setInt(12, userId);
					psmt2.executeUpdate();
					
					
					for (User member: TeamSingleton.getInstance().getTeamMembers(teamId)) {
						String message = "Hello, " + member.getFirstName() + "\n\t" + user.getFirstName() + " was added you in a new project\n\tProject name: " + projectName ;;
						MailService.getInstance().sendText(member.getEmail(), "You're added in a new project", message);
					}
					
					
					
					
					json.put("statusCode", 200);
					json.put("message", "Successfully");
					json.put("detailedMessage", "project successfully created");
					return json;
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				json.put("statusCode", 500);
				json.put("message", "failure");
				json.put("detailedMessage", "SQLException");
				return json;
			}
			catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				json.put("statusCode", 500);
				json.put("message", "failure");
				json.put("detailedMessage", "ParseException");
				return json;
			}
			
		}
		else {
			json.put("statusCode", 500);
			json.put("message", "Failure");
			json.put("detailedMessage", "You are not company owner");
			return json;
		}
	}
		
	public static JSONObject updateProjectDetails(int projectid,String projectName,String startDate,String endDate,String description,String status,HttpServletRequest request) {
		//		JSONObject json2=new JSONObject();

//		String role=(String) request.getAttribute("companyRole");
		JSONObject json=new JSONObject();
		
		try {
			int userId=(int) request.getAttribute("userId");
			
			PreparedStatement psmt4=Application.dbConnection.prepareStatement("update project set projectName=?,startDate=?,endDate=?,description=?,status=?,modifiedOn=?,modifiedBy=? where projectId=?");
			psmt4.setString(1, projectName);
			SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
			
//			System.out.println(endDate);
			
			
			Date sd=null;
			Date ed=null;
			if(endDate==null) {
            	sd= sdf.parse(startDate);
            	java.sql.Date stDate = new java.sql.Date(sd.getTime());
				psmt4.setDate(2, stDate);
				psmt4.setDate(3, null);
//				System.out.println(stDate);
            }
            else if(endDate!=null) {
            	sd=sdf.parse(startDate);
				
				ed=sdf.parse(endDate);
				
				if (ed.compareTo(sd) < 0) {
					json.put("statusCode", 400);
					json.put("message", "failure");
					json.put("detailedMessage","End date should be greater than start date");
					return json;
				}
				
			
            	java.sql.Date stDate = new java.sql.Date(sd.getTime());
            	java.sql.Date dDate = new java.sql.Date(ed.getTime());
				psmt4.setDate(2, stDate);
				psmt4.setDate(3,dDate );
//				System.out.println(stDate);
            }
			
			
			
			
//			psmt4.setBoolean(4,Boolean.parseBoolean(strict));
//			psmt4.setBoolean(5, Boolean.parseBoolean(publicType));
			psmt4.setString(4, description);
			psmt4.setString(5, status);
			
//			SimpleDateFormat stf=new SimpleDateFormat("dd/MM/yyyy");
//			Date modifyOnDate=stf.parse(modifiedOn);

			Date modifyOnDate = new Date();
			
			java.sql.Timestamp modifyDate=new java.sql.Timestamp(modifyOnDate.getTime());
			psmt4.setTimestamp(6, modifyDate);
			psmt4.setInt(7,userId);
	//		psmt4.setInt(10, teamId);
			psmt4.setInt(8, projectid);
			
			psmt4.executeUpdate();
			
			ProjectSingleton.getInstance().updateProject(projectid);
			
			
			PreparedStatement psmt3=Application.dbConnection.prepareStatement("update companyMembers set lastUpdatedOn=? where userId=?");
            Date currDate=new Date();
            java.sql.Timestamp curDate = new java.sql.Timestamp(currDate.getTime());
            psmt3.setTimestamp(1, curDate);
            psmt3.setInt(2, userId);
            psmt3.executeUpdate();
            
            
            boolean projectStrict=ProjectSingleton.getInstance().getProjectById(projectid).isStrict();
            
//            System.out.println(projectStrict+" projectStrict");
            
            if(projectStrict) {
            	PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from taskSummary where projectId=?");
                psmt.setInt(1, projectid);
                ResultSet rs=psmt.executeQuery();
                while(rs.next()) {
//                	System.out.println(sd.getTime());
//                	System.out.println(rs.getDate("startDate").getTime());
//                	System.out.println(rs.getDate("startDate").getTime()<sd.getTime());
//                	System.out.println(rs.getDate("dueDate").getTime()>ed.getTime());
                	if(rs.getDate("startDate").getTime()<sd.getTime()) {
                		PreparedStatement ps1=Application.dbConnection.prepareStatement("update taskSummary set startDate=? where taskId=?");
                		java.sql.Date stDate = new java.sql.Date(sd.getTime());
                		ps1.setDate(1,stDate);
                		ps1.setInt(2, rs.getInt("taskId"));
                		ps1.executeUpdate();
                	}
                	if (rs.getDate("dueDate").getTime()>ed.getTime()) {
                		PreparedStatement ps1=Application.dbConnection.prepareStatement("update taskSummary set dueDate=? where taskId=?");
                		java.sql.Date etDate = new java.sql.Date(ed.getTime());
                		ps1.setDate(1,etDate);
                		ps1.setInt(2, rs.getInt("taskId"));
                		ps1.executeUpdate();
					}
                }
            }
            
            
			
			
			json.put("statusCode", 200);
			json.put("message", "successfully");
			json.put("detailedMessage","Successfully Project updated");
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			json.put("statusCode", 500);
			json.put("message", "failure");
			json.put("detailedMessage", "project not updated");
			return json;
		}
	}
	
	

	
	
	public static JSONObject getProjectDetails(HttpServletRequest request) {
		JSONObject json=new JSONObject();
		int userId=(int) request.getAttribute("userId");
//		String role=(String) request.getAttribute("companyRole");
		
		HashSet<Integer> ownerProject=(HashSet<Integer>) request.getAttribute("projectOwner");
//		HashSet<Integer> teamLeadProject=(HashSet<Integer>) request.getAttribute("projectTeamLead");
		HashSet<Integer> memberProject=(HashSet<Integer>) request.getAttribute("projectNormalMember");
		
		
//		HashSet<User> teamMembers=new HashSet<>();
		
		JSONArray arr = new JSONArray();
		try {
			if (ownerProject!=null) {
				for (int projectId:ownerProject) {
					Project project=ProjectSingleton.getInstance().getProjectById(projectId);
					JSONObject json1=project.toJSON();
					ArrayList<User> teamMember=TeamSingleton.getInstance().getTeamMembers(ProjectSingleton.getInstance().getProjectById(projectId).getTeam().getTeamId());
					
//					for (User user: teamMember) {
//		                teamMembers.add(user);
//		            }
					
					json1.put("teamMembers", teamMember);
					arr.put(json1);
					
				}
			}

			if (memberProject!=null) {
				for (int projectId:memberProject) {
					Project project=ProjectSingleton.getInstance().getProjectById(projectId);
					JSONObject json1=project.toJSON();
					ArrayList<User> teamMember=TeamSingleton.getInstance().getTeamMembers(ProjectSingleton.getInstance().getProjectById(projectId).getTeam().getTeamId());
//					for (User user: teamMember) {
//		                teamMembers.add(user);
//		            }
					
					json1.put("teamMembers", teamMember);
					arr.put(json1);
				}
			}
			
//			System.out.println(arr);
			
			User user=UserSingleton.getInstance().getUserById(userId);
//			System.out.println(arr.length()+" length");
			json.put("user",user.getEmail());
			json.put("statusCode",200);
//			json.put("companyRole",role);
//			json.put("MemberRole", );
			json.put("message","success");
			json.put("arr", arr);
			return json;
		}
		catch (SQLException e) {
			// TODO: handle exception
			json.put("statusCode", 500);
			json.put("message","not successFully");
			System.out.println(e.getMessage());
			return json;
		}
		
//		return json;
	}
	
	

	
	
	
	public static JSONObject getSmallListProject(String projectid,HttpServletRequest request){
		JSONArray arr=new JSONArray();
//		JSONObject json2=new JSONObject();

//		String role=(String) request.getAttribute("companyRole");
		JSONObject json=new JSONObject();
//		int userId=(int) request.getAttribute("userId");
		
		HashSet<Integer> ownerProject=(HashSet<Integer>) request.getAttribute("projectOwner");
		HashSet<Integer> teamLeadProject=(HashSet<Integer>) request.getAttribute("projectTeamLead");
		HashSet<Integer> memberProject=(HashSet<Integer>) request.getAttribute("projectNormalMember");
		

		try {
			if (ownerProject!=null) {
				for (int projectId:ownerProject) {
					Project project=ProjectSingleton.getInstance().getProjectById(projectId);
					arr.put(project.toJSON());
					
				}
			}
			if(teamLeadProject!=null) {
				for (int projectId:teamLeadProject) {
					Project project=ProjectSingleton.getInstance().getProjectById(projectId);
					arr.put(project.toJSON());
				}
			}
			if (memberProject!=null) {
				for (int projectId:memberProject) {
					Project project=ProjectSingleton.getInstance().getProjectById(projectId);
					arr.put(project.toJSON());
				}
			}
			
			json.put("statusCode",200);
			json.put("message","success");
			json.put("arr", arr);
			return json;
		}
		catch (SQLException e) {
			// TODO: handle exception
			json.put("statusCode", 500);
			json.put("message","not successFully");
			System.out.println(e.getMessage());
			return json;
		}
    }

	
	
	public static JSONObject saveUpdateProject(int projectId,HttpServletRequest request) {
		JSONObject json=new JSONObject();

		try {
			PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from project where projectId=?");
			psmt1.setInt(1, projectId);
			ResultSet rs1=psmt1.executeQuery();
			if(rs1.next()) {


				json=Project.fromResultSet(rs1).toJSON();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("statusCode", 500);
			json.put("message", "failure");
			return json;
		}

		return json;
	}
}
	
