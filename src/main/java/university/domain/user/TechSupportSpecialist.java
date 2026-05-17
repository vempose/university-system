package university.domain.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import university.domain.support.TechSupportRequest;
import university.enums.Language;
import university.enums.RequestStatus;

/// Handles incoming tech support requests.
///
/// Can view new requests, accept, reject, or mark them complete.
public class TechSupportSpecialist extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<TechSupportRequest> assignedRequests = new ArrayList<>();

    /// Creates a tech support specialist (same params as any employee).
    public TechSupportSpecialist(
        String id,
        String name,
        String email,
        String password,
        Language language,
        double salary
    ) {
        super(id, name, email, password, language, salary);
    }

    /// Returns requests that haven't been touched yet (status = NEW).
    public List<TechSupportRequest> viewNewRequests() {
        return assignedRequests
            .stream()
            .filter(r -> r.getStatus() == RequestStatus.NEW)
            .toList();
    }

    /// Marks a request as viewed and accepted by this specialist.
    public void acceptRequest(TechSupportRequest request) {
        request.view();
        request.accept(this);
    }

    /// Rejects a request after viewing it.
    public void rejectRequest(TechSupportRequest request) {
        request.view();
        request.reject();
    }

    /// Marks a request as completed.
    public void completeRequest(TechSupportRequest request) {
        request.complete();
    }

    /// Assigns a request to this specialist's queue.
    public void assignRequest(TechSupportRequest request) {
        assignedRequests.add(request);
    }

    public List<TechSupportRequest> getAssignedRequests() {
        return List.copyOf(assignedRequests);
    }

    @Override
    public String toString() {
        return "TechSupportSpecialist{id='%s', name='%s'}".formatted(
            getId(),
            getName()
        );
    }
}
