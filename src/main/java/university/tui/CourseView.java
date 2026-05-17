package university.tui;

import university.tui.Messages;
import university.domain.academic.Course;
import university.system.UniversitySystem;

import java.util.List;

class CourseView {

    private final Session session;

    CourseView(Session session) {
        this.session = session;
    }

    void show() {
        UniversitySystem system = session.getSystem();

        while (true) {
            List<Course> courses = system.getCourses();
            ConsoleMenu.printHeader(Messages.get("course.title"));

            if (courses.isEmpty()) {
                ConsoleMenu.printInfo(Messages.get("course.no_courses"));
                System.out.println("\n  [0]  Go Back");
                int choice = ConsoleInput.readInt("\n  Choose an option: ", 0, 0);
                return;
            }

            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                System.out.printf(
                        "  [%d]  %s - %s (%d credits, %d lessons)%n",
                        i + 1,
                        c.getCourseCode(),
                        c.getTitle(),
                        c.getCredits(),
                        c.getLessons().size()
                );
            }
            System.out.println();
            System.out.println("  [0]  Go Back");

            int choice = ConsoleInput.readInt("\n  " + Messages.get("course.select_view") + ": ", 0, courses.size());
            if (choice == 0) return;

            Course selected = courses.get(choice - 1);
            ConsoleMenu.printSection(selected.getCourseCode() + " - " + selected.getTitle());
            System.out.println(selected.viewSyllabus());
            ConsoleInput.waitForEnter();
        }
    }
}
