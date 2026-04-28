package university.users;

import university.enums.MemberRole;

import java.io.Serializable;
import java.util.Date;

public class OrganizationMembership implements Serializable {
    private StudentOrganization organization;
    private MemberRole role;
    private Date joinDate;
    public OrganizationMembership(StudentOrganization organization, MemberRole role){
        this.organization = organization;
        this.role = role;
        this.joinDate =new Date();
    }
    public StudentOrganization getOrganization() { 
    	return organization; }
    public MemberRole getRole() { 
    	return role; }
    public Date getJoinDate() { 
    	return joinDate; 
    	}
    public void setRole(MemberRole r) { 
    	this.role = r; 
    	}

    @Override
    public String toString() {
        return String.format(
            "Membership { org='%s', role=%s, since=%s }",
            organization.getName(), role.getDisplayName(), joinDate.toString()
        );
    }
}
