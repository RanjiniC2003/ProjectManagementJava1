package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;


import javax.servlet.http.HttpServletRequest;

import classess.Project;
import classess.Task;
import classess.Team;
import classess.User;
import common.*;
import org.json.JSONArray;
import org.json.JSONObject;

//import org.json.JSONObject;

public class TaskJDBC {
    public static JSONObject addTask(String taskName,String projectName,String newDesciption,String newtaskAssignedTo,String newTaskstartDate,String newTaskDueDate,String PriorityLis,HttpServletRequest request) {
        JSONObject responseJSON = new JSONObject();

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
            int teamId=0;

            PreparedStatement psmt2=Application.dbConnection.prepareStatement("select * from project where createdBy=? and projectName=?");
            psmt2.setInt(1, owner);
            psmt2.setString(2, projectName);
            ResultSet rs2=psmt2.executeQuery();
            if(rs2.next()) {
                projectId=rs2.getInt("projectId");
                teamId=rs2.getInt("teamId");
            }

            Project project = ProjectSingleton.getInstance().getProjectById(projectId);
//            System.out.print("project "+project);
            
//            System.out.println(newTaskDueDate);
            
//            if(project.isStrict() && newTaskDueDate==null) {
//            	responseJSON.put("statusCode", 400);
//                responseJSON.put("statusMessage", "failure");
//                responseJSON.put("detailedMessage", "This project was strict.please provide due date in task");
//            }

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

            Date startDate = sdf.parse(newTaskstartDate);
            Date dueDate = sdf.parse(newTaskDueDate);
            
//            System.out.println(startDate.getTime());
//            System.out.println(project.getStartDate().getTime());
            
//            System.out.println(project.isStrict() && startDate.compareTo(project.getStartDate()) < 0 && dueDate.compareTo(project.getDueDate()) < 0);
//            System.out.println(project.isStrict() && startDate.compareTo(project.getStartDate()) > 0 && dueDate.compareTo(project.getDueDate()) > 0);
//            System.out.println(project.isStrict() && startDate.compareTo(project.getStartDate()) < 0 && dueDate.compareTo(project.getDueDate()) > 0);
//            System.out.println(project.isStrict() && startDate.compareTo(project.getStartDate()) > 0 && dueDate.compareTo(project.getDueDate()) < 0);
//            

            System.out.println((project.isStrict() && startDate.getTime()<project.getStartDate().getTime() && dueDate.getTime()>project.getDueDate().getTime()) || (project.isStrict() && startDate.getTime()<project.getStartDate().getTime()) || (project.isStrict() && dueDate.getTime()>project.getDueDate().getTime()));
            if ((project.isStrict() && startDate.getTime()<project.getStartDate().getTime() && dueDate.getTime()>project.getDueDate().getTime()) || (project.isStrict() && startDate.getTime()<project.getStartDate().getTime()) || (project.isStrict() && dueDate.getTime()>project.getDueDate().getTime())) {
                responseJSON.put("statusCode", 400);
                responseJSON.put("statusMessage", "failure");
                responseJSON.put("detailedMessage", "task start/due date can't be greater than strict project due date");

                return responseJSON;
            }

//            System.out.println("fghjk");
            PreparedStatement psmt = Application.dbConnection.prepareStatement("insert into taskSummary(taskName,description,taskStatus,taskAssignedTo,taskCreatedBy,priority,startDate,dueDate,duration,completePercentage,projectId,teamId) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            psmt.setString(1, taskName);
            psmt.setString(2, newDesciption);
            psmt.setString(3, "Open");

            
            psmt.setInt(4,UserSingleton.getInstance().getByEmail(newtaskAssignedTo).getUserId());
            psmt.setInt(5, userId);
            psmt.setString(6, PriorityLis);

            java.sql.Date stDate = new java.sql.Date(startDate.getTime());
            java.sql.Date duDate = new java.sql.Date(dueDate.getTime());
            psmt.setDate(7, stDate);
            psmt.setDate(8, duDate);

            long difference =dueDate.getTime()-startDate.getTime();
            psmt.setInt(9,(int) (difference / (1000 * 60 * 60 * 24)));

            psmt.setInt(10, 0);
            
            psmt.setInt(11,projectId);
            psmt.setInt(12, teamId);
            

            psmt.executeUpdate();
            
            
            responseJSON.put("statusCode", 200);
            responseJSON.put("statusMessage", "SUCCESS");
            responseJSON.put("detailedMessage", "Task successfully added");
            
            User user=UserSingleton.getInstance().getUserById(userId);
            User member=UserSingleton.getInstance().getByEmail(newtaskAssignedTo);
			String message = "Hello, " + member.getFirstName() + "\n\t" + user.getFirstName() + " was added you in a new Task\n\tTask name: " + taskName ;;
			MailService.getInstance().sendText(member.getEmail(), "You're added in a new Task", message);
		
			return responseJSON;
            

        } catch (Exception e) {
            responseJSON.put("statusCode", 500);
            responseJSON.put("statusMessage", "failure");
            responseJSON.put("detailedMessage", "internal server error");

            // TODO Auto-generated catch block
            e.printStackTrace();
            return responseJSON;
        }


 


    }


    public static JSONObject updateTaskData(String taskId, String taskName, String newDesciption, String taskStatus,
                                            String newtaskAssignedTo, String priorityLis, String newTaskstartDate, String newTaskDueDate,
                                            String completePercentage, HttpServletRequest request) {

        JSONObject responseJSON = new JSONObject();
        
        int userId=(int)request.getAttribute("userId");

        try {
        	
        	Task task = TaskSingleton.getInstance().getTaskById(Integer.parseInt(taskId));

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

            Date startDate = sdf.parse(newTaskstartDate);
            Date dueDate = sdf.parse(newTaskDueDate);

           
//            System.out.println((task.getProject().isStrict() && startDate.getTime()<task.getProject().getStartDate().getTime() && dueDate.getTime()>task.getProject().getDueDate().getTime()) || (task.getProject().isStrict() && startDate.getTime()<task.getProject().getStartDate().getTime()) || (task.getProject().isStrict() && dueDate.getTime()>task.getProject().getDueDate().getTime()));
            if ((task.getProject().isStrict() && startDate.getTime()<task.getProject().getStartDate().getTime() && dueDate.getTime()>task.getProject().getDueDate().getTime()) || (task.getProject().isStrict() && startDate.getTime()<task.getProject().getStartDate().getTime()) || (task.getProject().isStrict() && dueDate.getTime()>task.getProject().getDueDate().getTime())) {
                
//            	System.out.println("fghjkl");
            	
            	responseJSON.put("statusCode", 400);
                responseJSON.put("statusMessage", "failure");
                responseJSON.put("detailedMessage", "task start/due date can't be greater than strict project due date");

                return responseJSON;
            }


//        	int value=(int)request.getAttribute("userId");
            PreparedStatement psmt=Application.dbConnection.prepareStatement("update taskSummary set taskName=?, description=?,taskStatus=?,taskAssignedTo=?,priority=?,startDate=?,dueDate=?,duration=?,completePercentage=? where taskId=?");
            psmt.setString(1, taskName);
            psmt.setString(2, newDesciption);
            
            if(Integer.parseInt(completePercentage)==100) {
            	psmt.setString(3,"closed");
            }
            else {
            	psmt.setString(3, taskStatus);
            }
            
            
           int taskAssignedTo=UserSingleton.getInstance().getByEmail(newtaskAssignedTo).getUserId();
//           System.out.println(taskAssignedTo);
            psmt.setInt(4, taskAssignedTo);
//            psmt.setInt(5, value);
            psmt.setString(5, priorityLis);


            java.sql.Date stDate = new java.sql.Date(startDate.getTime());
            java.sql.Date duDate = new java.sql.Date(dueDate.getTime());
            psmt.setDate(6, stDate);
            psmt.setDate(7, duDate);


            long difference = dueDate.getTime() - startDate.getTime();

            psmt.setInt(8,(int)( difference / (1000 * 60 * 60 * 24)));

            if(taskStatus.equals("closed")) {
            	psmt.setInt(9,100);
            }
            else {
                psmt.setInt(9, Integer.parseInt(completePercentage));
            }
            psmt.setInt(10, Integer.parseInt(taskId));
            psmt.executeUpdate();

            TaskSingleton.getInstance().updateTask(Integer.parseInt(taskId));
            
            
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




    public static JSONArray getTasks(String status,int id,HttpServletRequest request) {
        JSONArray arr=new JSONArray();
        try {

            PreparedStatement pt=Application.dbConnection.prepareStatement("select * from taskSummary where taskStatus=? and taskAssignedTo=?");
            pt.setString(1, status);
            pt.setInt(2, id);
            ResultSet rs=pt.executeQuery();

            while(rs.next()) {

                Task task=TaskSingleton.getInstance().getTaskById(rs.getInt("taskId"));
                if(task!=null) {
                    arr.put(task.toJSON());
                }

            }


        }
        catch (SQLException e) {
            // TODO Auto-generated catch block

            System.out.println(e.getMessage());

        }

//		System.out.println(arr.length());
        return arr;
    }



    public static JSONObject saveUpdateDatas(int taskId,HttpServletRequest request) {
        JSONObject json=new JSONObject();
        try {
//        	TeamSingleton.getInstance().getTeamMembers(ProjectSingleton.getInstance().getProjectById(projectId).getTeam().getTeamId())
        	Task task=TaskSingleton.getInstance().getTaskById(taskId);
//        	HashSet<User> teamMembers=new HashSet<>();
        	
        	if(task!=null) {
        	    Project project=ProjectSingleton.getInstance().getProjectById(task.getProject().getProjectId());
        	    ArrayList<User> teamMembers=TeamSingleton.getInstance().getTeamMembers(project.getTeam().getTeamId());
//        	    for (User user: team) {
//	                teamMembers.add(user);
//	            }
                json=task.toJSON();
                json.put("teamMembers", teamMembers);
            }
        	else {
        		return null;
        	}
        	
//        	System.out.println(json);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        	e.printStackTrace();
        }
        return json;
    }


    public static JSONObject getSmallLIstTask(String taskId,HttpServletRequest request) {

        String status="";
        String mail="";


        String stringID = CookieManagement.getCookie(request, "user-id");
        int value=0;

        if (!stringID.isEmpty()) {
            value = Integer.parseInt(stringID);
        }

        JSONArray arr=new JSONArray();
        JSONObject json2=new JSONObject();


        try {
            PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from taskSummary where taskId=?");
            psmt.setInt(1, Integer.parseInt(taskId));
            ResultSet rs=psmt.executeQuery();

            if(rs.next()) {
                status=rs.getString("taskStatus");
            }





            PreparedStatement psmt2=Application.dbConnection.prepareStatement("select * from taskSummary where taskStatus=? and taskAssignedTo=?");
            psmt2.setString(1, status);
            psmt2.setInt(2, value);
            ResultSet rs2=psmt2.executeQuery();
            while(rs2.next()) {
//			   System.out.println("sdfg");
                Task task=TaskSingleton.getInstance().getTaskById(rs2.getInt("taskId"));
                if(task!=null) {
                    arr.put(task.toJSON());
                }
            }


//            System.out.println(arr.length());

            json2.put("statusCode", 200);
            json2.put("message", "successfully");
            json2.put("arr",arr );
            return json2;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            json2.put("statusCode", 500);
            json2.put("message", "failed");
            json2.put("arr",arr );

            return json2;

            // TODO Auto-generated catch block

        }
    }


//    ----------------------------------------------------------------------------------------- //

    public static JSONArray getTeamTasks(int taskId, String status) throws SQLException {
        JSONArray arr = new JSONArray();

        PreparedStatement psmt;

        if (status == null || status.isBlank()) {
            psmt = Application.dbConnection.prepareStatement("select * from taskSummary where teamId=?");
            psmt.setInt(1, taskId);
        } else {
//        	System.out.println("teamId : "+taskId);
//        	System.out.println("status : "+status);
            psmt = Application.dbConnection.prepareStatement("select * from taskSummary where taskId=? and taskStatus=?");
            psmt.setInt(1, taskId);
            psmt.setString(2, status);
        }

        ResultSet rs = psmt.executeQuery();

        while(rs.next()) {
            Task task = Task.fromResultSet(rs);

            arr.put(task.toJSON());
        }

        return arr;
    }
    
    


    public static JSONObject getAllTeamTasks(HttpServletRequest request, String status) throws SQLException {

    	
    	JSONArray arr = new JSONArray();
        
        JSONObject json=new JSONObject();
        HashSet<Integer> taskceoLIst = (HashSet<Integer>) request.getAttribute("ownerTaskList");
        HashSet<Integer> taskLeadLIst = (HashSet<Integer>) request.getAttribute("TeamLeadTaskList");
//        HashSet<Integer> taskMembersLIst = (HashSet<Integer>) request.getAttribute("TeamMemberTaskList");
        if (taskceoLIst!=null) {
            for (int taskId: taskceoLIst) {
                arr.put(getTeamTasks(taskId, status));
            }
        }
        if(taskLeadLIst!=null) {
            for (int taskId: taskLeadLIst) {
                arr.put(getTeamTasks(taskId, status));
            }
        }
//        if (taskMembersLIst!=null) {
//            for (int taskId: taskMembersLIst) {
//                arr.put(getTeamTasks(taskId, status));
//            }
//        }
//        for(int i=0;i<arr.length();i++) {
//        	System.out.println(arr.get(i));
//        }
        
//        System.out.println("arr length : "+arr.length());

        json.put("statusCode", 200);
        json.put("message", "Success");
        json.put("arr",arr);
        return json;
    }

    public static JSONObject getAllTeamTasks(HttpServletRequest request) throws SQLException {
        return getAllTeamTasks(request, null);
    }
}