package university.users;

import university.academic.TechSupportRequest;
import university.enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;

public class TechSupportSpecialist extends Employee {
    public TechSupportSpecialist(String name, String email, String password, double salary){
        super(name, email, password, salary);
    }

    public List<TechSupportRequest> viewNewRequests(){
        return new ArrayList<>();
    }
    public void acceptRequest(TechSupportRequest request) {
        request.updateStatus(RequestStatus.VIEWED);
        request.updateStatus(RequestStatus.ACCEPTED);
    }
    public void rejectRequest(TechSupportRequest request) {
        request.updateStatus(RequestStatus.VIEWED);
        request.updateStatus(RequestStatus.REJECTED);
    }
    public void completeRequest(TechSupportRequest request){
        request.updateStatus(RequestStatus.DONE);
    }

    @Override
    public String toString() {
        return "TechSupportSpecialist{id=" + getId() + ", name=" + getName() + ", salary=" + getSalary() + "}";
    }
}
