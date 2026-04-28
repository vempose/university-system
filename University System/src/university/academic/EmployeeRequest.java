package university.academic;

import university.enums.RequestStatus;

import java.io.Serializable;
import java.util.Date;

public class EmployeeRequest implements Serializable {
    private String requestContent;
    private RequestStatus status;
    private boolean isSigned;
    private Date createdAt;
    public EmployeeRequest(String requestContent){
        this.requestContent = requestContent;
        this.status = RequestStatus.PENDING;
        this.isSigned = false;
        this.createdAt = new Date();
    }

    public void sign() {
        this.status = RequestStatus.SIGNED;
        this.isSigned = true;
    }

    public void reject() {
        this.status = RequestStatus.REJECTED;
    }

    public String getRequestContent() { return requestContent; }
    public RequestStatus getStatus() { return status; }
    public boolean isSigned() { return isSigned; }
    public Date getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "EmployeeRequest{content=" + requestContent + ", status=" + status + ", signed=" + isSigned + "}";
    }
}
