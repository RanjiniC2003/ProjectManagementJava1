//package common;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import classess.Team;
//import classess.User;
//
//public class TeamSingleton {
//	 private final HashMap<Integer, Team> map = new HashMap<>();
//	 private final HashMap<Integer,ArrayList<User>> teamMembersList=new HashMap<>();
//	private static TeamSingleton object;
//	
//	private TeamSingleton() {}
//	
//	public static TeamSingleton getInstance() {
//		if (object==null) {
//			object=new TeamSingleton();
//		}
//		return object;
//	}
//	
//	public Team getTeamById(int teamId) throws SQLException {
//		if (map.containsKey(teamId)) {
//			return map.get(teamId);
//		}
//		PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from teamList where teamId=?");
//		psmt.setInt(1,teamId);
//		ResultSet rs=psmt.executeQuery();
//		if (rs.next()) {
//			Team team= Team.fromResultSet(rs);
//			map.put(teamId, team);
//			return team;
//		}
//		
//		return null;
//	}
//	
//	
//   public ArrayList<User> getTeamMember(int teamId) throws SQLException {
//		
//		ArrayList<User> members = new ArrayList<>();
//		
//		if(teamMembersList.containsKey(teamId)) {
//			return teamMembersList.get(teamId);
//		}
//		
//		PreparedStatement psmt5=Application.dbConnection.prepareStatement("select * from teamMembers where teamId=?");
//		psmt5.setInt(1, teamId);
//		
//		ResultSet rs5=psmt5.executeQuery();
//		while(rs5.next()) {
//			int userId=rs5.getInt("userId");
//			User mem=UserSingleton.getInstance().getUserById(userId);
//			members.add(mem);
//			teamMembersList.put(teamId, members);
//		}
//		
//		return members;
//	}
//	
//}


package common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import classess.Team;
import classess.User;

public class TeamSingleton {
    private final HashMap<Integer, Team> map = new HashMap<>();
    private final HashMap<Integer,ArrayList<User>> teamMembersList=new HashMap<>();
    private static TeamSingleton object;

    private TeamSingleton() {}

    public static TeamSingleton getInstance() {
        if (object==null) {
            object=new TeamSingleton();
        }
        return object;
    }

    public Team getTeamById(int teamId) throws SQLException {
        if (map.containsKey(teamId)) {
            return map.get(teamId);
        }
        PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from teamList where teamId=?");
        psmt.setInt(1,teamId);
        ResultSet rs=psmt.executeQuery();
        if (rs.next()) {
            Team team= Team.fromResultSet(rs);
            map.put(teamId, team);
            return team;
        }

        return null;
    }


    public ArrayList<User> getTeamMembers(int teamId) throws SQLException {

        if(teamMembersList.containsKey(teamId)) {
            return teamMembersList.get(teamId);
        }

        ArrayList<User> members = new ArrayList<>();

        PreparedStatement psmt5=Application.dbConnection.prepareStatement("select * from teamMembers where teamId=?");
        psmt5.setInt(1, teamId);

        ResultSet rs5=psmt5.executeQuery();
        while(rs5.next()) {
            int userId=rs5.getInt("userId");
            User mem=UserSingleton.getInstance().getUserById(userId);
            members.add(mem);
            teamMembersList.put(teamId, members);
        }

        return members;
    }


    public void updateTeam(int teamId) throws SQLException {
        map.remove(teamId);
        teamMembersList.remove(teamId);

        getTeamById(teamId);
        getTeamMembers(teamId);
    }
}

