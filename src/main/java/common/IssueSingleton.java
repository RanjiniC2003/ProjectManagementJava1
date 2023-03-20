package common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import classess.Issue;

public class IssueSingleton {
	private final HashMap<Integer, Issue> map=new HashMap<>();
	private static IssueSingleton issueSingleton;
	
	private IssueSingleton() {}
	
	public static IssueSingleton getInstance() {
		if(issueSingleton==null) {
			issueSingleton=new IssueSingleton();
		}
		return issueSingleton;
	}
	
	
	public Issue getIssueById(int issueId) throws SQLException {
		
		if(map.containsKey(issueId)) {
			return map.get(issueId);
		}
		
		PreparedStatement psmt=Application.dbConnection.prepareStatement("select * from issue where issueId=?");
		psmt.setInt(1, issueId);
		ResultSet rs=psmt.executeQuery();
		if(rs.next()) {
			Issue issues= Issue.fromResult(rs);
			map.put(issueId, issues);
			return issues;
		}
		return null;
		
	}

	public void updateIssue(int issueId) throws SQLException {
		map.remove(issueId);
		getIssueById(issueId);
	}
}
