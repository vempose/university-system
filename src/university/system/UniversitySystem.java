package university.system;

import java.io.*;
import java.util.*;
import university.domain.academic.Course;
import university.domain.news.News;
import university.domain.news.UniversityJournal;
import university.domain.support.LogEntry;
import university.domain.user.Student;
import university.domain.user.Teacher;
import university.domain.user.User;

public final class UniversitySystem {

    private static final String DEFAULT_PATH = "university_system.dat";

    private static final class Holder {

        private static final UniversitySystem INSTANCE = new UniversitySystem();
    }

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<News> newsList = new ArrayList<>();
    private final List<UniversityJournal> journals = new ArrayList<>();
    private final List<LogEntry> logs = new ArrayList<>();
    private final UserFactory userFactory = new UserFactory();

    private UniversitySystem() {}

    public static UniversitySystem getInstance() {
        return Holder.INSTANCE;
    }

    public Optional<User> authenticate(String email, String password) {
        return users
            .stream()
            .filter(u -> u.login(email, password))
            .findFirst();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    public void addNews(News news) {
        newsList.add(news);
    }

    public void removeNews(News news) {
        newsList.remove(news);
    }

    public void addJournal(UniversityJournal journal) {
        journals.add(journal);
    }

    public void addLog(LogEntry entry) {
        logs.add(entry);
    }

    public List<User> getUsers() {
        return List.copyOf(users);
    }

    public List<Course> getCourses() {
        return List.copyOf(courses);
    }

    public List<News> getNewsList() {
        return List.copyOf(newsList);
    }

    public List<UniversityJournal> getJournals() {
        return List.copyOf(journals);
    }

    public List<LogEntry> getLogs() {
        return List.copyOf(logs);
    }

    public UserFactory getUserFactory() {
        return userFactory;
    }

    public List<Student> getAllStudents() {
        return users
            .stream()
            .filter(u -> u instanceof Student)
            .map(u -> (Student) u)
            .toList();
    }

    public List<Teacher> getAllTeachers() {
        return users
            .stream()
            .filter(u -> u instanceof Teacher)
            .map(u -> (Teacher) u)
            .toList();
    }

    public void save() {
        save(DEFAULT_PATH);
    }

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

    public void load() {
        load(DEFAULT_PATH);
    }

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

    @Override
    public String toString() {
        return "UniversitySystem{users=%d, courses=%d, news=%d, journals=%d}".formatted(
            users.size(),
            courses.size(),
            newsList.size(),
            journals.size()
        );
    }
}
