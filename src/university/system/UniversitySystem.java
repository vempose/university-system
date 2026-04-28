package university.system;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import university.domain.academic.Course;
import university.domain.news.News;
import university.domain.news.UniversityJournal;
import university.domain.support.LogEntry;
import university.domain.user.User;

public final class UniversitySystem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    public Optional<User> authenticate(String email, String password) {
        return users
            .stream()
            .filter(u -> u.login(email, password))
            .findFirst();
    }

    public void save() {
        try (
            var oos = new ObjectOutputStream(
                new FileOutputStream("university_system.ser")
            )
        ) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to save system state", e);
        }
    }

    public void load() {
        try (
            var ois = new ObjectInputStream(
                new FileInputStream("university_system.ser")
            )
        ) {
            var loaded = (UniversitySystem) ois.readObject();
            users.clear();
            users.addAll(loaded.users);
            courses.clear();
            courses.addAll(loaded.courses);
            newsList.clear();
            newsList.addAll(loaded.newsList);
            journals.clear();
            journals.addAll(loaded.journals);
            logs.clear();
            logs.addAll(loaded.logs);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load system state", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Incompatible serialized data", e);
        }
    }

    // keeps singleton guarantee across deserialization
    @Serial
    private Object readResolve() {
        return Holder.INSTANCE;
    }

        @Override
    public String toString() {
        return "UniversitySystem{users=%d, courses=%d, news=%d}".formatted(users.size(), courses.size(), newsList.size());
    }
}
