
package jdbc;

import classess.*;
import common.Application;
import common.IssueSingleton;
import common.ProjectSingleton;
import common.TaskSingleton;
import common.UserSingleton;
import common.getCompanyId;

import org.apache.catalina.tribes.tipis.AbstractReplicatedMap.MapEntry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class HomeJDBC {
    public static JSONObject getDetails(int userId) throws SQLException {
        JSONObject json = new JSONObject();

        PreparedStatement psmt1 = Application.dbConnection.prepareStatement(
                "select * from project inner join teamList on project.teamId=teamList.teamId inner join teamMembers on project.teamId=teamMembers.teamId " +
                        "where project.status not in ('cancelled', 'completed') and (teamList.teamLead=? or teamList.createBy=? or teamMembers.userId=?)"
        );
        
        psmt1.setInt(1, userId);
        psmt1.setInt(2, userId);
        psmt1.setInt(3, userId);
        ResultSet rs1 = psmt1.executeQuery();
        JSONArray arr1=new JSONArray();

        HashSet<Integer> set1=new HashSet<>();
        
        while (rs1.next()) {
            Project project = Project.fromResultSet(rs1);

            if(!set1.contains(project.getProjectId())) {
                set1.add(project.getProjectId());
                arr1.put(project.toJSON());
            }
                 
        }
//        System.out.println(set1);

        json.put("OpenProjects",arr1);
        PreparedStatement psmt2 = Application.dbConnection.prepareStatement(
                "select * from project inner join teamList on project.teamId=teamList.teamId inner join teamMembers on project.teamId=teamMembers.teamId where project.status in ('completed') and (teamList.teamLead=? or teamList.createBy=? or teamMembers.userId=?)"
        );

        psmt2.setInt(1, userId);
        psmt2.setInt(2, userId);
        psmt2.setInt(3, userId);
        ResultSet rs2 = psmt2.executeQuery();
        JSONArray arr2=new JSONArray();

        HashSet<Integer> set2=new HashSet<>();

        while(rs2.next()) {
            Project project = Project.fromResultSet(rs2);

            if(!set2.contains(project.getProjectId())) {
                set2.add(project.getProjectId());
                arr2.put(project.toJSON());
            }
       
        }
//        System.out.println(arr2);
//        System.out.println(arr2.length());
        json.put("ClosedProjects",arr2);

        PreparedStatement psmt3 = Application.dbConnection.prepareStatement(
                "select * from taskSummary inner join teamList on taskSummary.teamId=teamList.teamId inner join teamMembers on taskSummary.teamId=teamMembers.teamId " +
                        "where taskSummary.taskStatus not in ('closed', 'inCancelled') and (teamList.teamLead=? or teamList.createBy=? or teamMembers.userId=?)"
        );

        psmt3.setInt(1, userId);
        psmt3.setInt(2, userId);
        psmt3.setInt(3, userId);
        ResultSet rs3 = psmt3.executeQuery();

        JSONArray arr3=new JSONArray();

        HashSet<Integer> set3=new HashSet<>();
        while(rs3.next()) {
            Task task = Task.fromResultSet(rs3);

            if (!set3.contains(task.getTaskId())) {
//                arr3.put(TaskSingleton.getInstance().getTaskById(rs3.getInt("taskId")));
                arr3.put(task.toJSON());
                set3.add(task.getTaskId());
            }
        }
        
        json.put("OpenTasks",arr3);

        PreparedStatement psmt4 = Application.dbConnection.prepareStatement(
                "select * from taskSummary inner join teamList on taskSummary.teamId=teamList.teamId inner join teamMembers on taskSummary.teamId=teamMembers.teamId " +
                        "where taskSummary.taskStatus in ('closed') and (teamList.teamLead=? or teamList.createBy=? or teamMembers.userId=?)"
        );

        psmt4.setInt(1, userId);
        psmt4.setInt(2, userId);
        psmt4.setInt(3, userId);
        ResultSet rs4 = psmt4.executeQuery();

        JSONArray arr4=new JSONArray();

        HashSet<Integer> set4= new HashSet<>();
        
        while(rs4.next()) {
            Task task = Task.fromResultSet(rs4);

            if (!set4.contains(task.getTaskId())) {
                arr4.put(task.toJSON());
                set4.add(task.getTaskId());
            }

        }
        json.put("ClosedTasks", arr4);

        PreparedStatement psmt5 = Application.dbConnection.prepareStatement(
                "select * from issue inner join project on issue.projectId=project.projectId inner join teamList on project.teamId=teamList.teamId inner join teamMembers on project.teamId=teamMembers.teamId " +
                        "where issue.status='Open' and (teamList.teamLead=? or teamList.createBy=? or teamMembers.userId=?)"
        );

        psmt5.setInt(1, userId);
        psmt5.setInt(2, userId);
        psmt5.setInt(3, userId);
        ResultSet rs5 = psmt5.executeQuery();
        
        JSONArray arr5=new JSONArray();

        HashSet<Integer> set5 = new HashSet<>();
        while (rs5.next()) {
            Issue issue = Issue.fromResult(rs5);

            if (!set5.contains(issue.getIssueId())) {
                arr5.put(issue.toJSON());
                set5.add(issue.getIssueId());
            }
//            arr5.put(IssueSingleton.getInstance().getIssueById(rs5.getInt("issueId")));
        }
        
        json.put("OpenIssues", arr5);

        PreparedStatement psmt6 = Application.dbConnection.prepareStatement(
                "select * from issue inner join project on issue.projectId=project.projectId inner join teamList on project.teamId=teamList.teamId inner join teamMembers on project.teamId=teamMembers.teamId " +
                        "where issue.status='Closed' and (teamList.teamLead=? or teamList.createBy=? or teamMembers.userId=?)"
        );

        psmt6.setInt(1, userId);
        psmt6.setInt(2, userId);
        psmt6.setInt(3, userId);
        ResultSet rs6 = psmt6.executeQuery();

        JSONArray arr6=new JSONArray();

        HashSet<Integer> set6 = new HashSet<>();
        while (rs6.next()) {
            Issue issue = Issue.fromResult(rs6);

            if (!set6.contains(issue.getIssueId())) {
                set6.add(issue.getIssueId());
                arr6.put(issue.toJSON());
            }
//            arr6.put(IssueSingleton.getInstance().getIssueById(rs6.getInt("issueId")));
           
        }
        json.put("ClosedIssues", arr6);
        
        
        
        PreparedStatement psmt7=Application.dbConnection.prepareStatement("select * from project inner join teamList on project.teamId=teamList.teamId inner join teamMembers on project.teamId=teamMembers.teamId " +
                        "where (teamList.teamLead=? or teamList.createBy=? or teamMembers.userId=?) ");
        psmt7.setInt(1, userId);
        psmt7.setInt(2, userId);
        psmt7.setInt(3, userId);
        ResultSet rs7=psmt7.executeQuery();
        
        
        JSONArray arr7=new JSONArray();
        JSONArray arr8=new JSONArray();
        while(rs7.next()) {
                Date date=new Date();
                Date dueDate=rs7.getDate("endDate");
                if(date.getTime()==dueDate.getTime()) {
                        arr7.put(ProjectSingleton.getInstance().getProjectById(rs7.getInt("projectId")));
                }
                else if(date.getTime()>dueDate.getTime()) {
                        arr8.put(ProjectSingleton.getInstance().getProjectById(rs7.getInt("projectId")));
                }
        }
        json.put("todayDueProject", arr7);
        json.put("dueProject", arr8);
        
        
        User user=UserSingleton.getInstance().getUserById(userId);
        int companyId=getCompanyId.getCompanyId(userId);
        Company company=Company.fromCompanyId(companyId);
        
        json.put("userDetails",user.toJSON());
        json.put("CompanyDetails",company.getCompanyName());
        return json;
    }

}