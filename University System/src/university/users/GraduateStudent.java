package university.users;

import university.enums.DegreeType;
import university.exceptions.InvalidSupervisorException;
import university.interfaces.Researcher;
import university.research.ResearchPaper;
import university.research.ResearchProfile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GraduateStudent extends Student implements Researcher {
    private static final int MIN_SUPERVISOR_H_INDEX = 3;
    private Researcher supervisor;
    private List<ResearchPaper> diplomaPapers;
    private ResearchProfile researchProfile;
    public GraduateStudent(String name, String email, String password, int yearOfStudy, DegreeType degreeType) {
        super(name, email, password, yearOfStudy, degreeType);
        if (degreeType == DegreeType.BACHELOR) {
            throw new IllegalArgumentException("GraduateStudent must be MASTER or PHD.");
        }
        this.diplomaPapers = new ArrayList<>();
        this.researchProfile = new ResearchProfile();
    }
    public void setSupervisor(Researcher supervisor) throws InvalidSupervisorException {
        int hIndex = supervisor.calculateHIndex();
        if (hIndex < MIN_SUPERVISOR_H_INDEX) {
            String supervisorName = (supervisor instanceof User u) ? u.getName() : "Unknown";
            throw new InvalidSupervisorException(supervisorName, hIndex);
        }
        this.supervisor = supervisor;
    }

    public Researcher getSupervisor() { return supervisor; }

    public void addDiplomaPaper(ResearchPaper paper) {
        diplomaPapers.add(paper);
        researchProfile.addPaper(paper);
    }

    public List<ResearchPaper> getDiplomaPapers() { return new ArrayList<>(diplomaPapers); }

    @Override
    public int calculateHIndex() {
        return researchProfile.getHIndex();
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(researchProfile.getPapers());
        sorted.sort(comparator);
        for (ResearchPaper p : sorted) {
            System.out.println(p);
        }
    }

    @Override
    public ResearchProfile getResearchProfile() {
        return researchProfile;
    }

    @Override
    public String toString() {
        String supervisorName = (supervisor instanceof User u) ? u.getName() : "none";
        return "GraduateStudent{id=" + getId() + ", name=" + getName() + ", degree=" + getDegreeType()
                + ", gpa=" + getGpa() + ", hIndex=" + calculateHIndex() + ", supervisor=" + supervisorName + "}";
    }
}
