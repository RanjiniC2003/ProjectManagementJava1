package jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import classess.User;
import common.UserSingleton;
import org.json.JSONArray;
import org.json.JSONObject;

import classess.Team;
import common.Application;
import common.TeamSingleton;

public class TeamJDBC {
    public static JSONObject addTeam(String teamName,String teamLead,String[] teamUsers,HttpServletRequest request) {

        JSONObject json=new JSONObject();

        int ownerId=(int) request.getAttribute("userId");

        PreparedStatement psmt1;
        try {
            psmt1 = Application.dbConnection.prepareStatement("select * from teamList where teamName=? and createBy=?");
            psmt1.setString(1, teamName);
            psmt1.setInt(2, ownerId);
            ResultSet rs1=psmt1.executeQuery();
            if (rs1.next()) {
                json.put("statusCode", 500);
                json.put("message", "Failed");
                json.put("detailedMessage","This team was already excited.please create new team!");
                return json;
            }
            else {

                int teamLeadId=0;
                PreparedStatement psmt6=Application.dbConnection.prepareStatement("select * from users where email=?");
                psmt6.setString(1,teamLead);
                ResultSet rs5=psmt6.executeQuery();
                if(rs5.next()) {
                    teamLeadId=rs5.getInt("userId");
                }

                try {
                    PreparedStatement psmt2=Application.dbConnection.prepareStatement("insert into teamList(teamName,teamLead,createBy) values(?,?,?)");
                    psmt2.setString(1, teamName);
                    psmt2.setInt(2, teamLeadId);
                    psmt2.setInt(3, ownerId);
                    psmt2.executeUpdate();

                    int teamId=0;
                    PreparedStatement psmt3=Application.dbConnection.prepareStatement("select * from teamList where teamName=? and createBy=?");
                    psmt3.setString(1, teamName);
                    psmt3.setInt(2, ownerId);
                    ResultSet rs2=psmt3.executeQuery();
                    if (rs2.next()) {
                        teamId=rs2.getInt("teamId");

                    }
                    int userId=0;

                    PreparedStatement psmt4=Application.dbConnection.prepareStatement("select * from companyMembers where invitedBy=?");
                    psmt4.setInt(1, ownerId);
                    ResultSet rs3=psmt4.executeQuery();
                    if(rs3.next()) {
                        for(int i=0;i<teamUsers.length;i++) {
                            if (!teamLead.equals(teamUsers[i])) {
                                PreparedStatement psmt5=Application.dbConnection.prepareStatement("select * from users where email=?");
                                psmt5.setString(1,teamUsers[i]);
                                ResultSet rs4=psmt5.executeQuery();
                                if(rs4.next()) {
                                    userId=rs4.getInt("userId");

                                    if (userId != ownerId) {
                                        PreparedStatement psmt7=Application.dbConnection.prepareStatement("insert into teamMembers(teamId,userId) values(?,?)");
                                        psmt7.setInt(1, teamId);
                                        psmt7.setInt(2, userId);
                                        psmt7.executeUpdate();
                                    }
                                }
                            }
                        }
                    }

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        json.put("statusCode", 200);
        json.put("message", "Success");
        json.put("detailedMessage", "Successfully team created");
        return json;

    }

    public static JSONObject getTeamDetails(HttpServletRequest request) {

        int userId = (int) request.getAttribute("userId");
        String role=(String) request.getAttribute("companyRole");
        
//        System.out.println(role);
        JSONArray arr = new JSONArray();

        HashSet<Integer> ceoTeamLIst = (HashSet<Integer>) request.getAttribute("owner");
        HashSet<Integer> teamLeadLIst = (HashSet<Integer>) request.getAttribute("teamLead");

        try {

            

            if (ceoTeamLIst != null) {
                for (int teamId: ceoTeamLIst) {
                    Team team = TeamSingleton.getInstance().getTeamById(teamId);
                    arr.put(team.toJSON());
                }
            }

            if (teamLeadLIst != null) {
                for (int teamId: teamLeadLIst) {
                    Team team = TeamSingleton.getInstance().getTeamById(teamId);
                    arr.put(team.toJSON());
                }
            }

            JSONObject json = new JSONObject();

            json.put("role", role);
            json.put("statusCode", 200);
            json.put("message", "success");
            json.put("arr1", arr);

            return json;

        } catch (SQLException e) {
            JSONObject json = new JSONObject();

            json.put("role", role);
            json.put("statusCode", 500);
            json.put("message","not successFully");
            json.put("arr1", arr);
            System.out.println(e.getMessage());
            return json;
        }

    }


    public static JSONObject SaveUpdateDatasInTeam(int teamId,HttpServletRequest request) {
        JSONObject json=new JSONObject();
        try {
            Team team=TeamSingleton.getInstance().getTeamById(teamId);
            if (team!=null) {
                JSONArray arr = new JSONArray();

                Iterator<User> member=TeamSingleton.getInstance().getTeamMembers(teamId).iterator();
                while(member.hasNext()) {
                	arr.put(member.next().toJSON());
                }
                
//                arr.put(TeamSingleton.getInstance().getTeamMembers(teamId).iterator());

                
                json=team.toJSON();
               
                json.put("teamMembers", arr);
              
                
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject updateTeams(String teamId, String teamLead, String[] teamUsers,HttpServletRequest request) {
        JSONObject json = new JSONObject();

        try {
            int teamIdInt = Integer.parseInt(teamId);
            
           
//            int teamLeadInt = UserSingleton.getInstance().getByEmail(teamLead).getUserId();

            Team team = TeamSingleton.getInstance().getTeamById(teamIdInt);
            int teamLeadInt=UserSingleton.getInstance().getByEmail(teamLead.trim()).getUserId();
         

            if (team.getTeamLead().getUserId() != teamLeadInt) {
                PreparedStatement psmt = Application.dbConnection.prepareStatement("update teamList set teamLead=? where teamId=?");
                psmt.setInt(1, teamLeadInt);
                psmt.setInt(2, teamIdInt);

                psmt.executeUpdate();
            }

            ArrayList<User> teamMembers = TeamSingleton.getInstance().getTeamMembers(teamIdInt);

            HashSet<Integer> allMembersSet = new HashSet<>();

            HashSet<Integer> commonMembersSet = new HashSet<>();
            HashSet<Integer> addedMembersSet = new HashSet<>();

            for (User user: teamMembers) {
                allMembersSet.add(user.getUserId());
            }

            for (String userEmail: teamUsers) {
                if (!userEmail.isBlank()) {
                    User user = UserSingleton.getInstance().getByEmail(userEmail);

                    if (allMembersSet.contains(user.getUserId())) {
                        commonMembersSet.add(user.getUserId());
                    } else if (user.getUserId() != team.getCreateBy().getUserId() && user.getUserId() != teamLeadInt) {
                        addedMembersSet.add(user.getUserId());
                    }
                }
            }

            allMembersSet.removeAll(commonMembersSet);

            PreparedStatement psmt2 = Application.dbConnection.prepareStatement("delete from teamMembers where teamId=? and userId=?");

            for (int removeUserId: allMembersSet) {
                psmt2.setInt(1, teamIdInt);
                psmt2.setInt(2, removeUserId);

                psmt2.executeUpdate();
            }

            PreparedStatement psmt3 = Application.dbConnection.prepareStatement("insert into teamMembers(teamId,userId) values(?,?)");

            for (int addUserId: addedMembersSet) {
                psmt3.setInt(1, teamIdInt);
                psmt3.setInt(2, addUserId);

                psmt3.executeUpdate();
            }

            TeamSingleton.getInstance().updateTeam(teamIdInt);

            json.put("statusCode", 200);
            json.put("statusMessage", "SUCCESS");
            json.put("detailedMessage", "update Successful");

        } catch (SQLException e) {
            json.put("statusCode", 500);
            json.put("message", "Failed");
            json.put("detailedMessage", "Internal server error");

            e.printStackTrace();
        }

        return json;
    }
}
