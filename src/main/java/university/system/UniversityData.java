package university.system;

import university.domain.academic.Course;
import university.domain.news.News;
import university.domain.news.UniversityJournal;
import university.domain.support.LogEntry;
import university.domain.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public final class UniversityData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    final List<User> users;
    final List<Course> courses;
    final List<News> newsList;
    final List<UniversityJournal> journals;
    final List<LogEntry> logs;

    UniversityData(List<User> users, List<Course> courses, List<News> newsList,
                   List<UniversityJournal> journals, List<LogEntry> logs) {
        this.users = List.copyOf(users);
        this.courses = List.copyOf(courses);
        this.newsList = List.copyOf(newsList);
        this.journals = List.copyOf(journals);
        this.logs = List.copyOf(logs);
    }
}
