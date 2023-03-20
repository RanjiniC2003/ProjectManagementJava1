package common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import classess.Task;

public class TaskSingleton {
	private final HashMap<Integer, Task> map=new HashMap<>();
	
	private static TaskSingleton object;
	
	private TaskSingleton() {}
	
	public static TaskSingleton getInstance() {
		if(object==null) {
			object=new TaskSingleton();
		}
		return object;
	}
	
	
	public Task getTaskById(int taskId) throws SQLException {
		
		if (map.containsKey(taskId)) {
			return map.get(taskId);
		}
		
		PreparedStatement psmt1=Application.dbConnection.prepareStatement("select * from taskSummary where taskId=?");
		psmt1.setInt(1, taskId);
		ResultSet rs=psmt1.executeQuery();
		if(rs.next()) {
			Task task= Task.fromResultSet(rs);
			map.put(taskId, task);
			return task;
		}
		return null;
	}
	
	public void updateTask(int taskId) throws SQLException {
		map.remove(taskId);
		getTaskById(taskId);
	}
}
