package classess;

public class TeamMembers {
	private Team team;
	private User user;
	
	public TeamMembers(Team team, User user) {
		// TODO Auto-generated constructor stub
		this.team=team;
		this.user=user;
		
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
