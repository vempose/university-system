package university.users;

import university.enums.MemberRole;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StudentOrganization implements Serializable {
    private String name;
    private String description;
    private List<OrganizationMembership> memberships;
    public StudentOrganization(String name, String description){
        this.name = name;
        this.description = description;
        this.memberships =new ArrayList<>();
    }
    public OrganizationMembership addMember(Student student, MemberRole role) {
        OrganizationMembership membership = new OrganizationMembership(this, role);
        memberships.add(membership);
        student.addMembership(membership);
        System.out.printf("[Org:%s] %s joined as %s%n", name, student.getName(), role.getDisplayName());
        return membership;
    }
    public String getName() { 
    	return name; }
    public String getDescription() { 
    	return description; }
    public List<OrganizationMembership> getMemberships() { 
    	return new ArrayList<>(memberships); 
    	}

    @Override
    public String toString() {
        return String.format(
            "StudentOrganization { name='%s', members=%d }",name, memberships.size()
        );
    }
}
