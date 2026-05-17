package university.tui;

import university.domain.support.TechSupportRequest;
import university.domain.user.*;
import university.enums.RequestStatus;
import university.system.UniversitySystem;

import java.util.LinkedHashMap;
import java.util.List;

class TechSupportView {

    private final Session session;

    TechSupportView(Session session) {
        this.session = session;
    }

    void show() {
        TechSupportSpecialist specialist = (TechSupportSpecialist) session.getCurrentUser();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "View New Requests");
            options.put(2, "View All Assigned Requests");
            options.put(3, "Accept Request");
            options.put(4, "Reject Request");
            options.put(5, "Complete Request");
            options.put(6, "Create New Request (as User)");

            int choice = ConsoleMenu.showMenu("Tech Support Panel", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> viewNewRequests(specialist);
                case 2 -> viewAllRequests(specialist);
                case 3 -> acceptRequest(specialist);
                case 4 -> rejectRequest(specialist);
                case 5 -> completeRequest(specialist);
                case 6 -> createRequest();
            }
        }
    }

    private void viewNewRequests(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection("New Requests");
        List<TechSupportRequest> requests = specialist.viewNewRequests();
        printRequests(requests);
        ConsoleInput.waitForEnter();
    }

    private void viewAllRequests(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection("All Assigned Requests");
        List<TechSupportRequest> requests = specialist.getAssignedRequests();
        printRequests(requests);
        ConsoleInput.waitForEnter();
    }

    private void printRequests(List<TechSupportRequest> requests) {
        if (requests.isEmpty()) {
            ConsoleMenu.printInfo("No requests.");
            return;
        }
        for (int i = 0; i < requests.size(); i++) {
            TechSupportRequest r = requests.get(i);
            System.out.printf(
                    "  [%d]  %s | Status: %s | From: %s%n",
                    i + 1, r.getDescription(), r.getStatus(), r.getRequester().getName()
            );
        }
    }

    private void acceptRequest(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection("Accept Request");
        List<TechSupportRequest> newRequests = specialist.viewNewRequests();
        if (newRequests.isEmpty()) {
            ConsoleMenu.printInfo("No new requests to accept.");
            ConsoleInput.waitForEnter();
            return;
        }
        printRequests(newRequests);
        int ri = ConsoleInput.readInt("\n  Select request: ", 1, newRequests.size()) - 1;
        TechSupportRequest request = newRequests.get(ri);
        try {
            specialist.acceptRequest(request);
            ConsoleMenu.printSuccess("Request accepted.");
        } catch (Exception e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void rejectRequest(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection("Reject Request");
        List<TechSupportRequest> newRequests = specialist.viewNewRequests();
        if (newRequests.isEmpty()) {
            ConsoleMenu.printInfo("No new requests to reject.");
            ConsoleInput.waitForEnter();
            return;
        }
        printRequests(newRequests);
        int ri = ConsoleInput.readInt("\n  Select request: ", 1, newRequests.size()) - 1;
        TechSupportRequest request = newRequests.get(ri);
        try {
            specialist.rejectRequest(request);
            ConsoleMenu.printSuccess("Request rejected.");
        } catch (Exception e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void completeRequest(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection("Complete Request");
        List<TechSupportRequest> accepted = specialist.getAssignedRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.ACCEPTED)
                .toList();
        if (accepted.isEmpty()) {
            ConsoleMenu.printInfo("No accepted requests to complete.");
            ConsoleInput.waitForEnter();
            return;
        }
        printRequests(accepted);
        int ri = ConsoleInput.readInt("\n  Select request: ", 1, accepted.size()) - 1;
        TechSupportRequest request = accepted.get(ri);
        try {
            specialist.completeRequest(request);
            ConsoleMenu.printSuccess("Request completed.");
        } catch (Exception e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void createRequest() {
        ConsoleMenu.printSection("Create Tech Support Request");
        User currentUser = session.getCurrentUser();
        String description = ConsoleInput.readLine("  Describe the issue: ");
        TechSupportRequest request = new TechSupportRequest(currentUser, description);

        List<TechSupportSpecialist> specialists = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof TechSupportSpecialist)
                .map(u -> (TechSupportSpecialist) u)
                .toList();

        if (!specialists.isEmpty()) {
            for (int i = 0; i < specialists.size(); i++) {
                System.out.printf("  [%d]  %s%n", i + 1, specialists.get(i).getName());
            }
            int si = ConsoleInput.readInt("\n  Assign to specialist: ", 1, specialists.size()) - 1;
            specialists.get(si).assignRequest(request);
        }
        ConsoleMenu.printSuccess("Request created: " + request.getId());
        ConsoleInput.waitForEnter();
    }
}
