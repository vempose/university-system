package university.tui;

import university.domain.academic.*;
import university.domain.research.ResearchProfile;
import university.domain.support.LogEntry;
import university.domain.user.*;
import university.enums.*;
import university.system.UserFactory;
import university.system.UniversitySystem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/// Admin panel — CRUD for users and system log viewing.
///
/// Lets the admin add/remove/update users by type, list everyone,
/// and browse activity logs.
class AdminView {

    private final Session session;

    AdminView(Session session) {
        this.session = session;
    }

    /// Shows the admin menu and handles user choices.
    void show() {
        Admin admin = (Admin) session.getCurrentUser();
        UniversitySystem system = session.getSystem();
        UserFactory factory = system.getUserFactory();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, Messages.get("admin.add_user"));
            options.put(2, Messages.get("admin.remove_user"));
            options.put(3, Messages.get("admin.update_user"));
            options.put(4, Messages.get("admin.list_users"));
            options.put(5, Messages.get("admin.view_logs"));
            options.put(6, Messages.get("admin.make_researcher"));

            int choice = ConsoleMenu.showMenu(Messages.get("admin.title"), options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> addUser(admin, system, factory);
                case 2 -> removeUser(admin, system);
                case 3 -> updateUser(admin);
                case 4 -> listAllUsers(system);
                case 5 -> viewLogs(admin, system);
                case 6 -> manageResearcherStatus(admin, system);
            }
        }
    }

    private void addUser(Admin admin, UniversitySystem system, UserFactory factory) {
        ConsoleMenu.printSection(Messages.get("admin.add_user"));
        LinkedHashMap<Integer, String> typeOptions = new LinkedHashMap<>();
        typeOptions.put(1, Messages.get("admin.type_admin"));
        typeOptions.put(2, Messages.get("admin.type_manager"));
        typeOptions.put(3, Messages.get("admin.type_teacher"));
        typeOptions.put(4, Messages.get("admin.type_student"));
        typeOptions.put(5, Messages.get("admin.type_gradstudent"));
        typeOptions.put(6, Messages.get("admin.type_techsupport"));

        int type = ConsoleMenu.showMenu(Messages.get("admin.select_type"), typeOptions, true, false);
        if (type == 0) return;

        String name = ConsoleInput.readLine("  " + Messages.get("common.name") + ": ");
        String email = ConsoleInput.readEmail("  " + Messages.get("common.email") + ": ");
        String password = ConsoleInput.readPassword("  " + Messages.get("common.password") + ": ");

        User newUser = null;
        switch (type) {
            case 1 -> {
                double salary = ConsoleInput.readDouble("  " + Messages.get("common.salary") + ": ", 0, 1_000_000);
                newUser = factory.createAdmin(name, email, password);
                if (newUser instanceof Employee e) e.setSalary(salary);
            }
            case 2 -> {
                ConsoleMenu.printSection(Messages.get("admin.manager_type"));
                LinkedHashMap<Integer, String> mTypes = new LinkedHashMap<>();
                mTypes.put(1, Messages.get("admin.mgr_or"));
                mTypes.put(2, Messages.get("admin.mgr_department"));
                mTypes.put(3, Messages.get("admin.mgr_dean"));
                int mt = ConsoleMenu.showMenu(Messages.get("admin.manager_type"), mTypes, false, false);
                ManagerType managerType = switch (mt) {
                    case 2 -> ManagerType.DEPARTMENT;
                    case 3 -> ManagerType.DEAN;
                    default -> ManagerType.OR;
                };
                double salary = ConsoleInput.readDouble("  " + Messages.get("common.salary") + ": ", 0, 1_000_000);
                newUser = factory.createManager(name, email, password, managerType);
                if (newUser instanceof Employee e) e.setSalary(salary);
            }
            case 3 -> {
                ConsoleMenu.printSection(Messages.get("admin.teacher_position"));
                LinkedHashMap<Integer, String> tTypes = new LinkedHashMap<>();
                tTypes.put(1, Messages.get("admin.pos_tutor"));
                tTypes.put(2, Messages.get("admin.pos_lector"));
                tTypes.put(3, Messages.get("admin.pos_senior_lector"));
                tTypes.put(4, Messages.get("admin.pos_professor"));
                int tp = ConsoleMenu.showMenu(Messages.get("admin.teacher_position"), tTypes, false, false);
                TeacherPosition position = switch (tp) {
                    case 1 -> TeacherPosition.TUTOR;
                    case 2 -> TeacherPosition.LECTOR;
                    case 3 -> TeacherPosition.SENIOR_LECTOR;
                    case 4 -> TeacherPosition.PROFESSOR;
                    default -> TeacherPosition.LECTOR;
                };
                double salary = ConsoleInput.readDouble("  " + Messages.get("common.salary") + ": ", 0, 1_000_000);
                newUser = factory.createTeacher(name, email, password, position);
                if (newUser instanceof Employee e) e.setSalary(salary);
                if (position == TeacherPosition.PROFESSOR) {
                    newUser.setResearchProfile(new ResearchProfile());
                }
            }
            case 4 -> {
                DegreeType degree = pickDegree();
                Major major = pickOrCreateMajor(session.getSystem());
                newUser = factory.createStudent(name, email, password, degree, major);
            }
            case 5 -> {
                DegreeType degree = ConsoleInput.readYesNo("  " + Messages.get("admin.phd_student") + "?")
                        ? DegreeType.PHD : DegreeType.MASTER;
                Major major = pickOrCreateMajor(session.getSystem());
                newUser = factory.createGraduateStudent(name, email, password, degree, major);
            }
            case 6 -> {
                double salary = ConsoleInput.readDouble("  " + Messages.get("common.salary") + ": ", 0, 1_000_000);
                newUser = factory.createTechSupport(name, email, password);
                if (newUser instanceof Employee e) e.setSalary(salary);
            }
        }

        if (newUser != null) {
            if (newUser.getResearchProfile() == null
                    && ConsoleInput.readYesNo("  " + Messages.get("admin.prompt_researcher") + " ")) {
                newUser.setResearchProfile(new ResearchProfile());
            }
            admin.addUser(newUser, system);
            ConsoleMenu.printSuccess(Messages.get("admin.user_created", newUser.getName(), newUser.getId()));
        }
        ConsoleInput.waitForEnter();
    }

    private void removeUser(Admin admin, UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("admin.remove_user"));
        List<User> users = new ArrayList<>(system.getUsers());
        if (users.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("admin.no_users"));
            ConsoleInput.waitForEnter();
            return;
        }
        User toRemove = ConsoleMenu.pickFromList(users, Object::toString,
                Messages.get("admin.remove_user"));
        if (ConsoleMenu.confirm(Messages.get("admin.remove_user") + " " + toRemove.getName() + "?")) {
            admin.removeUser(toRemove, system);
            ConsoleMenu.printSuccess(Messages.get("admin.user_removed"));
        }
        ConsoleInput.waitForEnter();
    }

    private void updateUser(Admin admin) {
        ConsoleMenu.printSection(Messages.get("admin.update_user"));
        List<User> users = new ArrayList<>(session.getSystem().getUsers());
        if (users.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("admin.no_users"));
            ConsoleInput.waitForEnter();
            return;
        }
        User toUpdate = ConsoleMenu.pickFromList(users, Object::toString,
                Messages.get("admin.update_user"));
        String newName = ConsoleInput.readLineOrBlank("  " + Messages.get("common.name") + " ("
                + Messages.get("admin.no_changes").toLowerCase() + "): ");
        String newEmail = ConsoleInput.readLineOrBlank("  " + Messages.get("common.email") + " ("
                + Messages.get("admin.no_changes").toLowerCase() + "): ");
        if (!newName.isEmpty() || !newEmail.isEmpty()) {
            admin.updateUser(
                    toUpdate,
                    !newName.isEmpty() ? newName : toUpdate.getName(),
                    !newEmail.isEmpty() ? newEmail : toUpdate.getEmail()
            );
            ConsoleMenu.printSuccess(Messages.get("admin.user_updated"));
        } else {
            ConsoleMenu.printInfo(Messages.get("admin.no_changes"));
        }
        ConsoleInput.waitForEnter();
    }

    private void listAllUsers(UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("admin.list_users"));
        List<User> users = system.getUsers();
        if (users.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("admin.no_users"));
        } else {
            for (User u : users) {
                System.out.printf(
                        "  %-10s | %-20s | %s%n",
                        u.getClass().getSimpleName(),
                        u.getName(),
                        u.getEmail()
                );
            }
            System.out.println("  " + Messages.get("admin.total_users", users.size()));
        }
        ConsoleInput.waitForEnter();
    }

    private void viewLogs(Admin admin, UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("admin.view_logs"));
        List<LogEntry> logs = admin.viewLogs(system);
        if (logs.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("admin.no_logs"));
        } else {
            for (var log : logs) {
                System.out.println("  " + log);
            }
        }
        ConsoleInput.waitForEnter();
    }

    private DegreeType pickDegree() {
        LinkedHashMap<Integer, String> degOptions = new LinkedHashMap<>();
        degOptions.put(1, Messages.get("admin.degree_bachelor"));
        degOptions.put(2, Messages.get("admin.degree_master"));
        degOptions.put(3, Messages.get("admin.degree_phd"));
        int d = ConsoleMenu.showMenu(Messages.get("admin.select_degree"), degOptions, false, false);
        return switch (d) {
            case 2 -> DegreeType.MASTER;
            case 3 -> DegreeType.PHD;
            default -> DegreeType.BACHELOR;
        };
    }

    private Major pickOrCreateMajor(UniversitySystem system) {
        List<School> schools = system.getUsers().stream()
                .filter(u -> u instanceof Student s && s.getSchool() != null)
                .map(u -> ((Student) u).getSchool())
                .distinct()
                .toList();

        if (schools.isEmpty()) {
            String schoolName = ConsoleInput.readLine("  " + Messages.get("admin.new_school_name") + ": ");
            School school = new School(schoolName);
            String majorName = ConsoleInput.readLine("  " + Messages.get("admin.new_major_name") + ": ");
            Major major = new Major(majorName, school);
            school.addMajor(major);
            return major;
        }

        System.out.println("  " + Messages.get("admin.select_school") + ":");
        for (int i = 0; i < schools.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, schools.get(i).getName());
        }
        int si = ConsoleInput.readInt("\n  " + Messages.get("admin.select_school") + ": ", 1, schools.size()) - 1;
        School selectedSchool = schools.get(si);

        List<Major> majors = selectedSchool.getMajors();
        if (majors.isEmpty()) {
            String majorName = ConsoleInput.readLine("  " + Messages.get("admin.new_major_name") + ": ");
            Major major = new Major(majorName, selectedSchool);
            selectedSchool.addMajor(major);
            return major;
        }
        System.out.println("  " + Messages.get("admin.select_major") + ":");
        for (int i = 0; i < majors.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, majors.get(i).getName());
        }
        System.out.printf("  [%d]  %s%n", majors.size() + 1, Messages.get("admin.create_new_major"));
        int mi = ConsoleInput.readInt("\n  " + Messages.get("admin.select_major") + ": ", 1, majors.size() + 1);
        if (mi == majors.size() + 1) {
            String majorName = ConsoleInput.readLine("  " + Messages.get("admin.new_major_name") + ": ");
            Major major = new Major(majorName, selectedSchool);
            selectedSchool.addMajor(major);
            return major;
        }
        return majors.get(mi - 1);
    }

    private void manageResearcherStatus(Admin admin, UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("admin.make_researcher"));
        List<User> candidates = system.getUsers().stream()
                .filter(u -> u.getResearchProfile() == null)
                .toList();
        if (candidates.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("admin.no_researcher_candidates"));
            ConsoleInput.waitForEnter();
            return;
        }
        User selected = ConsoleMenu.pickFromList(candidates, Object::toString,
                Messages.get("admin.make_researcher"));
        if (ConsoleMenu.confirm(Messages.get("admin.make_researcher") + " — " + selected.getName() + "?")) {
            selected.setResearchProfile(new ResearchProfile());
            ConsoleMenu.printSuccess(Messages.get("admin.researcher_added", selected.getName()));
        }
        ConsoleInput.waitForEnter();
    }
}
