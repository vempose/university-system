package university.tui;

import university.domain.support.TechSupportRequest;
import university.tui.Messages;
import university.domain.user.*;
import university.enums.RequestStatus;
import university.system.UniversitySystem;

import java.util.LinkedHashMap;
import java.util.List;

/// Tech support panel — accept, reject, and complete support
/// requests, plus view new and assigned requests.
class TechSupportView {

    private final Session session;

    TechSupportView(Session session) {
        this.session = session;
    }

    /// Shows the tech support menu and handles user choices.
    void show() {
        TechSupportSpecialist specialist = (TechSupportSpecialist) session.getCurrentUser();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, Messages.get("techsupport.view_new"));
            options.put(2, Messages.get("techsupport.view_all"));
            options.put(3, Messages.get("techsupport.accept"));
            options.put(4, Messages.get("techsupport.reject"));
            options.put(5, Messages.get("techsupport.complete"));
            options.put(6, Messages.get("techsupport.create"));

            int choice = ConsoleMenu.showMenu(Messages.get("techsupport.title"), options, true, false);
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
        ConsoleMenu.printSection(Messages.get("techsupport.view_new"));
        List<TechSupportRequest> requests = specialist.viewNewRequests();
        printRequests(requests);
        ConsoleInput.waitForEnter();
    }

    private void viewAllRequests(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection(Messages.get("techsupport.view_all"));
        List<TechSupportRequest> requests = specialist.getAssignedRequests();
        printRequests(requests);
        ConsoleInput.waitForEnter();
    }

    private void printRequests(List<TechSupportRequest> requests) {
        if (requests.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("techsupport.no_requests"));
            return;
        }
        for (int i = 0; i < requests.size(); i++) {
            TechSupportRequest r = requests.get(i);
            System.out.printf(
                    "  [%d]  %s | %s: %s | %s: %s%n",
                    i + 1, r.getDescription(),
                    Messages.get("techsupport.status_label"), r.getStatus(),
                    Messages.get("techsupport.from_label"), r.getRequester().getName()
            );
        }
    }

    private void acceptRequest(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection(Messages.get("techsupport.accept"));
        List<TechSupportRequest> newRequests = specialist.viewNewRequests();
        if (newRequests.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("techsupport.no_new"));
            ConsoleInput.waitForEnter();
            return;
        }
        TechSupportRequest request = ConsoleMenu.pickFromList(newRequests,
                r -> r.getDescription() + " | " + Messages.get("techsupport.status_label") + ": " + r.getStatus()
                        + " | " + Messages.get("techsupport.from_label") + ": " + r.getRequester().getName(),
                Messages.get("techsupport.accept"), Messages.get("menu.back"));
        if (request == null) return;
        try {
            specialist.acceptRequest(request);
            ConsoleMenu.printSuccess(Messages.get("techsupport.accepted"));
        } catch (Exception e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void rejectRequest(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection(Messages.get("techsupport.reject"));
        List<TechSupportRequest> newRequests = specialist.viewNewRequests();
        if (newRequests.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("techsupport.no_new"));
            ConsoleInput.waitForEnter();
            return;
        }
        TechSupportRequest request = ConsoleMenu.pickFromList(newRequests,
                r -> r.getDescription() + " | " + Messages.get("techsupport.status_label") + ": " + r.getStatus()
                        + " | " + Messages.get("techsupport.from_label") + ": " + r.getRequester().getName(),
                Messages.get("techsupport.reject"), Messages.get("menu.back"));
        if (request == null) return;
        try {
            specialist.rejectRequest(request);
            ConsoleMenu.printSuccess(Messages.get("techsupport.rejected"));
        } catch (Exception e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void completeRequest(TechSupportSpecialist specialist) {
        ConsoleMenu.printSection(Messages.get("techsupport.complete"));
        List<TechSupportRequest> accepted = specialist.getAssignedRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.ACCEPTED)
                .toList();
        if (accepted.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("techsupport.no_accepted"));
            ConsoleInput.waitForEnter();
            return;
        }
        TechSupportRequest request = ConsoleMenu.pickFromList(accepted,
                r -> r.getDescription() + " | " + Messages.get("techsupport.status_label") + ": " + r.getStatus()
                        + " | " + Messages.get("techsupport.from_label") + ": " + r.getRequester().getName(),
                Messages.get("techsupport.complete"), Messages.get("menu.back"));
        if (request == null) return;
        try {
            specialist.completeRequest(request);
            ConsoleMenu.printSuccess(Messages.get("techsupport.completed"));
        } catch (Exception e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void createRequest() {
        ConsoleMenu.printSection(Messages.get("techsupport.create"));
        User currentUser = session.getCurrentUser();
        String description = ConsoleInput.readLine("  " + Messages.get("techsupport.describe") + ": ");
        TechSupportRequest request = new TechSupportRequest(currentUser, description);

        List<TechSupportSpecialist> specialists = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof TechSupportSpecialist)
                .map(u -> (TechSupportSpecialist) u)
                .toList();

        if (!specialists.isEmpty()) {
            TechSupportSpecialist picked = ConsoleMenu.pickFromList(specialists,
                    TechSupportSpecialist::getName, Messages.get("techsupport.assign_to"));
            picked.assignRequest(request);
        }
        ConsoleMenu.printSuccess(Messages.get("techsupport.created", request.getId()));
        ConsoleInput.waitForEnter();
    }
}
