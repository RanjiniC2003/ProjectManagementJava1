package common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import classess.Project;

public class ProjectSingleton {
    private final HashMap<Integer, Project> map=new HashMap<>();
	
	private static ProjectSingleton object;
	
	private ProjectSingleton() {
		// TODO Auto-generated constructor stub
	}
	
	public static ProjectSingleton getInstance() {
		if(object==null) {
			object=new ProjectSingleton();
		}
		return object;
	}
	
	
	public Project getProjectById(int projectId) throws SQLException {
		
		if (map.containsKey(projectId)) {
			return map.get(projectId);
		}
		
		PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from project where projectId=?");
		psmt1.setInt(1, projectId);
		ResultSet rs=psmt1.executeQuery();
		if(rs.next()) {
			Project project= Project.fromResultSet(rs);
			map.put(projectId, project);
			return project;
		}
		return null;
	}
	
	public void updateProject(int projectId) throws SQLException {
		map.remove(projectId);
		getProjectById(projectId);
	}
}
