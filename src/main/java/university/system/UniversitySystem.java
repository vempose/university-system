package university.system;

import university.domain.academic.Course;
import university.domain.news.News;
import university.domain.news.UniversityJournal;
import university.domain.support.LogEntry;
import university.domain.user.Student;
import university.domain.user.Teacher;
import university.domain.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/// Central hub of the university management system.
///
/// Singleton that holds all users, courses, journals, and major data.
/// Every part of the app goes through this to get or change stuff.
public final class UniversitySystem {

    private static final String DEFAULT_PATH = "university_system.dat";
    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<News> newsList = new ArrayList<>();
    private final List<UniversityJournal> journals = new ArrayList<>();
    private final List<LogEntry> logs = new ArrayList<>();
    private final UserFactory userFactory = new UserFactory();
    private UniversitySystem() {
    }

    /// Returns the singleton instance.
    ///
    /// @return the one and only UniversitySystem
    public static UniversitySystem getInstance() {
        return Holder.INSTANCE;
    }

    /// Logs in a user by email and password.
    ///
    /// @param email    the user's email
    /// @param password the user's password
    /// @return the user if credentials match
    public Optional<User> authenticate(String email, String password) {
        return users
                .stream()
                .filter(u -> u.login(email, password))
                .findFirst();
    }

    /// Adds a user to the system.
    ///
    /// @param user the user to add
    public void addUser(User user) {
        users.add(user);
    }

    /// Removes a user from the system.
    ///
    /// @param user the user to remove
    public void removeUser(User user) {
        users.remove(user);
    }

    /// Adds a course.
    ///
    /// @param course the course to add
    public void addCourse(Course course) {
        courses.add(course);
    }

    /// Removes a course.
    ///
    /// @param course the course to remove
    public void removeCourse(Course course) {
        courses.remove(course);
    }

    /// Adds a news item.
    ///
    /// @param news the news to add
    public void addNews(News news) {
        newsList.add(news);
    }

    /// Removes a news item.
    ///
    /// @param news the news to remove
    public void removeNews(News news) {
        newsList.remove(news);
    }

    /// Adds a journal.
    ///
    /// @param journal the journal to add
    public void addJournal(UniversityJournal journal) {
        journals.add(journal);
    }

    /// Adds a log entry.
    ///
    /// @param entry the log entry
    public void addLog(LogEntry entry) {
        logs.add(entry);
    }

    /// Returns an unmodifiable list of all users.
    ///
    /// @return all users
    public List<User> getUsers() {
        return List.copyOf(users);
    }

    /// Returns an unmodifiable list of all courses.
    ///
    /// @return all courses
    public List<Course> getCourses() {
        return List.copyOf(courses);
    }

    /// Returns an unmodifiable list of all news items.
    ///
    /// @return all news
    public List<News> getNewsList() {
        return List.copyOf(newsList);
    }

    /// Returns an unmodifiable list of all journals.
    ///
    /// @return all journals
    public List<UniversityJournal> getJournals() {
        return List.copyOf(journals);
    }

    /// Returns an unmodifiable list of all log entries.
    ///
    /// @return all logs
    public List<LogEntry> getLogs() {
        return List.copyOf(logs);
    }

    /// Returns the user factory for creating new users.
    ///
    /// @return the factory
    public UserFactory getUserFactory() {
        return userFactory;
    }

    /// Returns all students in the system.
    ///
    /// @return list of students
    public List<Student> getAllStudents() {
        return users
                .stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .toList();
    }

    /// Returns all teachers in the system.
    ///
    /// @return list of teachers
    public List<Teacher> getAllTeachers() {
        return users
                .stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .toList();
    }

    /// Saves everything to the default file.
    public void save() {
        save(DEFAULT_PATH);
    }

    /// Saves the system state to a file.
    ///
    /// Serializes users, courses, news, journals, and logs.
    ///
    /// @param path the file path to save to
    public void save(String path) {
        UniversityData data = new UniversityData(
                users,
                courses,
                newsList,
                journals,
                logs
        );
        try (
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(path)
                )
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to save system state to " + path,
                    e
            );
        }
    }

    /// Loads everything from the default file.
    public void load() {
        load(DEFAULT_PATH);
    }

    /// Loads the system state from a file.
    ///
    /// Replaces all current data with what's in the file.
    ///
    /// @param path the file path to load from
    public void load(String path) {
        try (
                ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(path)
                )
        ) {
            UniversityData data = (UniversityData) ois.readObject();
            users.clear();
            users.addAll(data.users);
            courses.clear();
            courses.addAll(data.courses);
            newsList.clear();
            newsList.addAll(data.newsList);
            journals.clear();
            journals.addAll(data.journals);
            logs.clear();
            logs.addAll(data.logs);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to load system state from " + path,
                    e
            );
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Incompatible serialized data", e);
        }
    }

    /// Returns a summary of the system state.
    ///
    /// @return string with user/course/news/journal counts
    @Override
    public String toString() {
        return "UniversitySystem{users=%d, courses=%d, news=%d, journals=%d}".formatted(
                users.size(),
                courses.size(),
                newsList.size(),
                journals.size()
        );
    }

    private static final class Holder {

        private static final UniversitySystem INSTANCE = new UniversitySystem();
    }
}
