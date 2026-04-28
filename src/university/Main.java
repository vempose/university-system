package university;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import university.comparator.PaperByCitationsComparator;
import university.comparator.PaperByDateComparator;
import university.comparator.PaperByPagesComparator;
import university.comparator.StudentByGpaComparator;
import university.comparator.UserByNameComparator;
import university.domain.academic.Course;
import university.domain.academic.Enrollment;
import university.domain.academic.Lesson;
import university.domain.academic.Major;
import university.domain.academic.Mark;
import university.domain.academic.School;
import university.domain.communication.Complaint;
import university.domain.news.News;
import university.domain.news.UniversityJournal;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.domain.research.ResearchProject;
import university.domain.student.OrganizationMembership;
import university.domain.student.StudentOrganization;
import university.domain.support.TechSupportRequest;
import university.domain.user.Admin;
import university.domain.user.GraduateStudent;
import university.domain.user.Manager;
import university.domain.user.Student;
import university.domain.user.Teacher;
import university.domain.user.TechSupportSpecialist;
import university.enums.DegreeType;
import university.enums.Language;
import university.enums.LessonType;
import university.enums.ManagerType;
import university.enums.OrganizationRole;
import university.enums.TeacherPosition;
import university.enums.UrgencyLevel;
import university.exception.CreditLimitExceededException;
import university.exception.InvalidSupervisorException;
import university.exception.NonResearcherJoinProjectException;
import university.service.NewsService;
import university.service.ResearchService;
import university.system.UniversitySystem;

public class Main {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       UNIVERSITY SYSTEM DEMO             ║");
        System.out.println("╚══════════════════════════════════════════╝");

        UniversitySystem system = UniversitySystem.getInstance();
        System.out.println("System instance: " + system);

        System.out.println("\n--- [2] School & Major ---");
        School school = new School("School of Computing");
        Major major = new Major("Computer Science", school);
        school.addMajor(major);
        System.out.println("Created: " + school);
        System.out.println("Created: " + major);

        System.out.println("\n--- [3] Create Users ---");
        Admin admin = new Admin(
            UUID.randomUUID().toString(), "Alice Admin",
            "alice@uni.edu", "pass1", Language.EN, 5000.0
        );
        Manager manager = new Manager(
            UUID.randomUUID().toString(), "Bob Manager",
            "bob@uni.edu", "pass2", Language.EN, 4000.0, ManagerType.OR
        );
        Teacher lector = new Teacher(
            UUID.randomUUID().toString(), "Carol Lector",
            "carol@uni.edu", "pass3", Language.EN, 3000.0, TeacherPosition.LECTOR
        );
        Teacher professor = new Teacher(
            UUID.randomUUID().toString(), "Dave Professor",
            "dave@uni.edu", "pass4", Language.EN, 4500.0, TeacherPosition.LECTOR
        );
        professor.setPosition(TeacherPosition.PROFESSOR);
        System.out.println("PROFESSOR auto-got ResearchProfile: " + (professor.getResearchProfile() != null));

        Student bachelor = new Student(
            UUID.randomUUID().toString(), "Eve Bachelor",
            "eve@uni.edu", "pass5", Language.EN, DegreeType.BACHELOR, major
        );
        bachelor.setSchool(school);

        GraduateStudent gradStudent = new GraduateStudent(
            UUID.randomUUID().toString(), "Frank Master",
            "frank@uni.edu", "pass6", Language.EN, DegreeType.MASTER, major
        );
        gradStudent.setSchool(school);
        System.out.println("MASTER auto-got ResearchProfile: " + (gradStudent.getResearchProfile() != null));

        TechSupportSpecialist techSupport = new TechSupportSpecialist(
            UUID.randomUUID().toString(), "Grace Tech",
            "grace@uni.edu", "pass7", Language.EN, 2500.0
        );

        System.out.println("\n--- [4] Register Users ---");
        admin.addUser(manager, system);
        admin.addUser(lector, system);
        admin.addUser(professor, system);
        admin.addUser(bachelor, system);
        admin.addUser(gradStudent, system);
        admin.addUser(techSupport, system);
        system.addUser(admin);
        System.out.println("Total registered users: " + system.getUsers().size());

        System.out.println("\n--- [5] Create Courses ---");
        Course cs101 = new Course("CS101", "Intro to Programming", 5);
        Course cs201 = new Course("CS201", "Data Structures", 4);
        system.addCourse(cs101);
        system.addCourse(cs201);
        System.out.println("Total courses: " + system.getCourses().size());

        System.out.println("\n--- [6] Create Lessons ---");
        LocalDateTime time1 = LocalDateTime.of(2024, 9, 2, 9, 0);
        LocalDateTime time2 = LocalDateTime.of(2024, 9, 4, 11, 0);
        Lesson lecture1 = new Lesson("L001", LessonType.LECTURE, "Room 101", time1, lector);
        Lesson practice1 = new Lesson("P001", LessonType.PRACTICE, "Lab 201", time2, professor);
        Lesson lecture2 = new Lesson("L002", LessonType.LECTURE, "Room 202", time1, professor);
        cs101.addLesson(lecture1);
        cs101.addLesson(practice1);
        cs201.addLesson(lecture2);
        System.out.println(cs101.viewSyllabus());

        System.out.println("\n--- [7] Assign Teachers to Courses ---");
        manager.assignTeacherToCourse(lector, cs101, lecture1);
        manager.assignTeacherToCourse(professor, cs101, practice1);
        manager.assignTeacherToCourse(professor, cs201, lecture2);
        System.out.println("Lector courses: " + lector.viewCourses());
        System.out.println("Professor courses: " + professor.viewCourses());

        System.out.println("\n--- [8] Student Enrollment + Credit Limit Demo ---");
        Enrollment enrollment = bachelor.registerForCourse(cs101);
        System.out.println("Registered for CS101, status: " + enrollment.getStatus());
        Course heavyCourse = new Course("CS999", "Advanced Topics", 17);
        try {
            bachelor.registerForCourse(heavyCourse);
        } catch (CreditLimitExceededException e) {
            System.out.println("CreditLimitExceededException caught: " + e.getMessage());
        }

        System.out.println("\n--- [9] Approve & Register Enrollment ---");
        manager.approveRegistration(enrollment);
        enrollment.register();
        System.out.println("Enrollment status after register: " + enrollment.getStatus());

        System.out.println("\n--- [10] Teacher Puts Mark ---");
        Mark mark = new Mark(25, 22, 35);
        lector.putMark(enrollment, mark);
        System.out.println("Mark: " + mark);

        System.out.println("\n--- [11] Student Views Transcript ---");
        System.out.println(bachelor.getTranscript());

        System.out.println("\n--- [12] Student Rates Teacher ---");
        bachelor.rateTeacher(lector, 4, "Very clear explanations.");
        System.out.println("Lector average rating: " + lector.getAverageRating());

        System.out.println("\n--- [13] Teacher Sends Complaint ---");
        Complaint complaint = lector.sendComplaint(
            List.of(bachelor), UrgencyLevel.MEDIUM,
            "Student submitted assignments late", manager
        );
        System.out.println("Complaint filed: " + complaint);

        System.out.println("\n--- [14] Research Papers ---");
        ResearchProfile profProfile = professor.getResearchProfile();
        System.out.println("Professor's h-index before any papers: " + profProfile.calculateHIndex());

        System.out.println("\n--- [15] Supervisor Assignment (InvalidSupervisorException demo) ---");
        try {
            gradStudent.setSupervisor(profProfile);
        } catch (InvalidSupervisorException e) {
            System.out.println("InvalidSupervisorException caught: " + e.getMessage());
        }

        ResearchPaper paper1 = new ResearchPaper(
            "Deep Learning Advances", "Dave Professor",
            "IEEE Transactions on AI", "1-10", 10,
            LocalDate.of(2023, 3, 15), "10.1109/DL2023", 10
        );
        ResearchPaper paper2 = new ResearchPaper(
            "Graph Algorithm Optimization", "Dave Professor",
            "ACM Computing Surveys", "20-30", 11,
            LocalDate.of(2023, 6, 20), "10.1145/GA2023", 5
        );
        ResearchPaper paper3 = new ResearchPaper(
            "Formal Methods in Software Engineering", "Dave Professor",
            "SIAM Journal on Computing", "5-12", 8,
            LocalDate.of(2023, 9, 1), "10.1137/FM2023", 3
        );
        profProfile.publishPaper(paper1);
        profProfile.publishPaper(paper2);
        profProfile.publishPaper(paper3);
        System.out.println("Professor papers published: " + profProfile.getPapers().size());
        System.out.println("Professor h-index after 3 papers: " + profProfile.calculateHIndex());

        gradStudent.setSupervisor(profProfile);
        System.out.println("Supervisor set successfully: " + (gradStudent.getSupervisor() != null));

        System.out.println("\n--- [16] Research Project ---");
        ResearchProject project = new ResearchProject("RP-001", "AI and Education Research");
        try {
            project.addParticipant(profProfile);
        } catch (NonResearcherJoinProjectException e) {
            System.out.println("Error: " + e.getMessage());
        }
        profProfile.joinProject(project);
        System.out.println("Project: " + project);
        System.out.println("Professor projects: " + profProfile.getProjects().size());

        System.out.println("\n--- [17] University Journal + Observer Pattern ---");
        UniversityJournal journal = new UniversityJournal("IEEE Computer Science Journal");
        journal.subscribe(professor);
        journal.subscribe(bachelor);
        System.out.println("Subscribers: " + journal.getSubscriptions().size());
        System.out.println("Publishing paper2 to journal (notifications fire below):");
        journal.publishPaper(paper2);
        system.addJournal(journal);
        System.out.println("Journal published papers: " + journal.getPublishedPapers().size());

        System.out.println("\n--- [18] NewsService ---");
        ResearchService researchService = new ResearchService(system);
        NewsService newsService = new NewsService(system, researchService);
        News paperNews = newsService.announcePaperPublication(paper1);
        System.out.println("Paper news: \"" + paperNews.getTitle() + "\"");
        System.out.println("Auto-pinned (RESEARCH topic): " + paperNews.isPinned());
        News topNews = newsService.announceTopCitedResearcher();
        System.out.println("Top researcher news: \"" + topNews.getTitle() + "\", pinned: " + topNews.isPinned());
        System.out.println("Top cited researcher of 2023:");
        researchService.printTopCitedResearcherOfYear(2023);

        System.out.println("\n--- [19] Tech Support Request ---");
        TechSupportRequest request = new TechSupportRequest(bachelor, "Cannot access student portal");
        techSupport.assignRequest(request);
        System.out.println("Request created: " + request.getStatus());
        techSupport.acceptRequest(request);
        System.out.println("After accept: " + request.getStatus());
        techSupport.completeRequest(request);
        System.out.println("After complete: " + request.getStatus());

        System.out.println("\n--- [20] Student Organization ---");
        StudentOrganization org = new StudentOrganization("CS Programming Club", "Club for CS enthusiasts");
        OrganizationMembership membership = new OrganizationMembership(bachelor, org, OrganizationRole.HEAD);
        org.addMembership(membership);
        bachelor.addMembership(membership);
        System.out.println("Organization: " + org);
        System.out.println("Eve's role: " + bachelor.getMemberships().get(0).getRole());

        System.out.println("\n--- [21] Save & Load System State ---");
        system.save();
        System.out.println("System saved to disk.");
        system.load();
        System.out.println("System loaded from disk. Users: " + system.getUsers().size()
            + ", Courses: " + system.getCourses().size()
            + ", News: " + system.getNewsList().size()
            + ", Journals: " + system.getJournals().size());

        System.out.println("\n--- [22] System Summary ---");
        System.out.println(system);

        System.out.println("\n=== Comparator Demos ===");

        System.out.println("\n-- Papers sorted by citations DESC --");
        researchService.printAllPapers(new PaperByCitationsComparator());

        System.out.println("\n-- Papers sorted by publish date ASC --");
        researchService.printAllPapers(new PaperByDateComparator());

        System.out.println("\n-- Papers sorted by page count ASC --");
        researchService.printAllPapers(new PaperByPagesComparator());

        System.out.println("\n-- Students sorted by GPA DESC --");
        List<Student> sortedStudents = manager.viewStudentsSorted(
            system.getAllStudents(), new StudentByGpaComparator()
        );
        sortedStudents.forEach(s ->
            System.out.println(s.getName() + " | GPA: " + s.getGpa())
        );

        System.out.println("\n-- Teachers sorted by name ASC --");
        List<Teacher> sortedTeachers = manager.viewTeachersSorted(
            system.getAllTeachers(), new UserByNameComparator()
        );
        sortedTeachers.forEach(t ->
            System.out.println(t.getName() + " | " + t.getPosition())
        );

        System.out.println("\n-- Top cited researcher by school --");
        researchService.printTopCitedResearcherBySchool(school);

        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║          DEMO COMPLETE                   ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }
}
