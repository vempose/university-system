package university.tui;

import university.domain.academic.*;
import university.domain.research.ResearchProfile;
import university.domain.user.*;
import university.enums.*;
import university.system.UserFactory;
import university.system.UniversitySystem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

class AdminView {

    private final Session session;

    AdminView(Session session) {
        this.session = session;
    }

    void show() {
        Admin admin = (Admin) session.getCurrentUser();
        UniversitySystem system = session.getSystem();
        UserFactory factory = system.getUserFactory();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "Add New User");
            options.put(2, "Remove User");
            options.put(3, "Update User");
            options.put(4, "List All Users");
            options.put(5, "View System Logs");

            int choice = ConsoleMenu.showMenu("Admin Panel", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> addUser(admin, system, factory);
                case 2 -> removeUser(admin, system);
                case 3 -> updateUser(admin);
                case 4 -> listAllUsers(system);
                case 5 -> viewLogs(admin, system);
            }
        }
    }

    private void addUser(Admin admin, UniversitySystem system, UserFactory factory) {
        ConsoleMenu.printSection("Add New User");
        LinkedHashMap<Integer, String> typeOptions = new LinkedHashMap<>();
        typeOptions.put(1, "Admin");
        typeOptions.put(2, "Manager");
        typeOptions.put(3, "Teacher");
        typeOptions.put(4, "Student");
        typeOptions.put(5, "Graduate Student (Master/PhD)");
        typeOptions.put(6, "Tech Support Specialist");

        int type = ConsoleMenu.showMenu("Select User Type", typeOptions, true, false);
        if (type == 0) return;

        String name = ConsoleInput.readLine("  Name: ");
        String email = ConsoleInput.readEmail("  Email: ");
        String password = ConsoleInput.readPassword("  Password: ");

        User newUser = null;
        switch (type) {
            case 1 -> {
                double salary = ConsoleInput.readDouble("  Salary: ", 0, 1_000_000);
                newUser = factory.createAdmin(name, email, password);
                if (newUser instanceof Employee e) e.setSalary(salary);
            }
            case 2 -> {
                ConsoleMenu.printSection("Manager Type");
                LinkedHashMap<Integer, String> mTypes = new LinkedHashMap<>();
                mTypes.put(1, "OR");
                mTypes.put(2, "DEPARTMENT");
                mTypes.put(3, "DEAN");
                int mt = ConsoleMenu.showMenu("Select Type", mTypes, false, false);
                ManagerType managerType = switch (mt) {
                    case 2 -> ManagerType.DEPARTMENT;
                    case 3 -> ManagerType.DEAN;
                    default -> ManagerType.OR;
                };
                double salary = ConsoleInput.readDouble("  Salary: ", 0, 1_000_000);
                newUser = factory.createManager(name, email, password, managerType);
                if (newUser instanceof Employee e) e.setSalary(salary);
            }
            case 3 -> {
                ConsoleMenu.printSection("Teacher Position");
                LinkedHashMap<Integer, String> tTypes = new LinkedHashMap<>();
                tTypes.put(1, "TUTOR");
                tTypes.put(2, "LECTOR");
                tTypes.put(3, "SENIOR_LECTOR");
                tTypes.put(4, "PROFESSOR");
                int tp = ConsoleMenu.showMenu("Select Position", tTypes, false, false);
                TeacherPosition position = switch (tp) {
                    case 1 -> TeacherPosition.TUTOR;
                    case 2 -> TeacherPosition.LECTOR;
                    case 3 -> TeacherPosition.SENIOR_LECTOR;
                    case 4 -> TeacherPosition.PROFESSOR;
                    default -> TeacherPosition.LECTOR;
                };
                double salary = ConsoleInput.readDouble("  Salary: ", 0, 1_000_000);
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
                DegreeType degree = ConsoleInput.readYesNo("  PhD student?")
                        ? DegreeType.PHD : DegreeType.MASTER;
                Major major = pickOrCreateMajor(session.getSystem());
                newUser = factory.createGraduateStudent(name, email, password, degree, major);
            }
            case 6 -> {
                double salary = ConsoleInput.readDouble("  Salary: ", 0, 1_000_000);
                newUser = factory.createTechSupport(name, email, password);
                if (newUser instanceof Employee e) e.setSalary(salary);
            }
        }

        if (newUser != null) {
            admin.addUser(newUser, system);
            ConsoleMenu.printSuccess("User created: " + newUser.getName() + " (" + newUser.getId() + ")");
        }
        ConsoleInput.waitForEnter();
    }

    private void removeUser(Admin admin, UniversitySystem system) {
        ConsoleMenu.printSection("Remove User");
        List<User> users = new ArrayList<>(system.getUsers());
        if (users.isEmpty()) {
            ConsoleMenu.printInfo("No users in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, users.get(i));
        }
        int idx = ConsoleInput.readInt("\n  Select user to remove: ", 1, users.size()) - 1;
        User toRemove = users.get(idx);
        if (ConsoleMenu.confirm("Remove " + toRemove.getName() + "?")) {
            admin.removeUser(toRemove, system);
            ConsoleMenu.printSuccess("User removed.");
        }
        ConsoleInput.waitForEnter();
    }

    private void updateUser(Admin admin) {
        ConsoleMenu.printSection("Update User");
        List<User> users = new ArrayList<>(session.getSystem().getUsers());
        if (users.isEmpty()) {
            ConsoleMenu.printInfo("No users in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, users.get(i));
        }
        int idx = ConsoleInput.readInt("\n  Select user to update: ", 1, users.size()) - 1;
        User toUpdate = users.get(idx);
        String newName = ConsoleInput.readLineOrBlank("  New name (leave blank to keep): ");
        String newEmail = ConsoleInput.readLineOrBlank("  New email (leave blank to keep): ");
        if (!newName.isEmpty() || !newEmail.isEmpty()) {
            admin.updateUser(
                    toUpdate,
                    !newName.isEmpty() ? newName : toUpdate.getName(),
                    !newEmail.isEmpty() ? newEmail : toUpdate.getEmail()
            );
            ConsoleMenu.printSuccess("User updated.");
        } else {
            ConsoleMenu.printInfo("No changes made.");
        }
        ConsoleInput.waitForEnter();
    }

    private void listAllUsers(UniversitySystem system) {
        ConsoleMenu.printSection("All Users");
        List<User> users = system.getUsers();
        if (users.isEmpty()) {
            ConsoleMenu.printInfo("No users registered.");
        } else {
            for (User u : users) {
                System.out.printf(
                        "  %-10s | %-20s | %s%n",
                        u.getClass().getSimpleName(),
                        u.getName(),
                        u.getEmail()
                );
            }
            System.out.printf("%n  Total: %d users%n", users.size());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewLogs(Admin admin, UniversitySystem system) {
        ConsoleMenu.printSection("System Logs");
        List<university.domain.support.LogEntry> logs = admin.viewLogs(system);
        if (logs.isEmpty()) {
            ConsoleMenu.printInfo("No log entries.");
        } else {
            for (var log : logs) {
                System.out.println("  " + log);
            }
        }
        ConsoleInput.waitForEnter();
    }

    private DegreeType pickDegree() {
        LinkedHashMap<Integer, String> degOptions = new LinkedHashMap<>();
        degOptions.put(1, "BACHELOR");
        degOptions.put(2, "MASTER");
        degOptions.put(3, "PHD");
        int d = ConsoleMenu.showMenu("Select Degree", degOptions, false, false);
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
            String schoolName = ConsoleInput.readLine("  New school name: ");
            School school = new School(schoolName);
            String majorName = ConsoleInput.readLine("  New major name: ");
            Major major = new Major(majorName, school);
            school.addMajor(major);
            return major;
        }

        System.out.println("  Schools:");
        for (int i = 0; i < schools.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, schools.get(i).getName());
        }
        int si = ConsoleInput.readInt("\n  Select school: ", 1, schools.size()) - 1;
        School selectedSchool = schools.get(si);

        List<Major> majors = selectedSchool.getMajors();
        if (majors.isEmpty()) {
            String majorName = ConsoleInput.readLine("  New major name: ");
            Major major = new Major(majorName, selectedSchool);
            selectedSchool.addMajor(major);
            return major;
        }
        System.out.println("  Majors:");
        for (int i = 0; i < majors.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, majors.get(i).getName());
        }
        System.out.printf("  [%d]  Create new major%n", majors.size() + 1);
        int mi = ConsoleInput.readInt("\n  Select major: ", 1, majors.size() + 1);
        if (mi == majors.size() + 1) {
            String majorName = ConsoleInput.readLine("  New major name: ");
            Major major = new Major(majorName, selectedSchool);
            selectedSchool.addMajor(major);
            return major;
        }
        return majors.get(mi - 1);
    }
}
