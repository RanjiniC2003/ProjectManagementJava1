package classess;

public class Invite {
	private User invitedBy;
	private Company companyId;
	private User email;
	
	
	public Invite(User invitedBy, Company companyId, User email) {
		this.invitedBy=invitedBy;
		this.companyId=companyId;
		this.email=email;
	}
	
	public User getInvitedBy() {
		return invitedBy;
	}
	public void setInvitedBy(User invitedBy) {
		this.invitedBy = invitedBy;
	}
	public Company getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Company companyId) {
		this.companyId = companyId;
	}
	public User getEmail() {
		return email;
	}
	public void setEmail(User email) {
		this.email = email;
	}
	
	
}
