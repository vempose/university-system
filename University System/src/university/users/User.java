package university.users;

import university.enums.Language;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private String password;
    private Language language;
    protected User(String name, String email, String password) {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = Language.EN;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }
    public void logout() {
        System.out.println(name + " logged out.");
    }
    public void changeLanguage(Language language) {
        this.language = language;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { 
    	this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) {
    	this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { 
    	this.password = password; }
    public Language getLanguage() { return language; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name=" + name + ", email=" + email + ", language=" + language + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;
        return Objects.equals(id, u.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
