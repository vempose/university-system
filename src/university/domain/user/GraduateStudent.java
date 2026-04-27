package university.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import university.domain.academic.Major;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.domain.research.Researcher;
import university.enums.DegreeType;
import university.enums.Language;
import university.exception.InvalidSupervisorException;

public class GraduateStudent extends Student {

    public static final int MIN_SUPERVISOR_H_INDEX = 3;

    private Researcher supervisor;
    private final List<ResearchPaper> diplomaPapers = new ArrayList<>();

    public GraduateStudent(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language,
        DegreeType degreeType,
        Major major
    ) {
        super(id, name, email, passwordHash, language, degreeType, major);
    }

    public void setSupervisor(Researcher supervisor)
        throws InvalidSupervisorException {
        if (
            supervisor == null ||
            supervisor.calculateHIndex() < MIN_SUPERVISOR_H_INDEX
        ) {
            throw new InvalidSupervisorException(
                "Supervisor h-index must be >= " +
                    MIN_SUPERVISOR_H_INDEX +
                    ", got: " +
                    (supervisor == null ? "null" : supervisor.calculateHIndex())
            );
        }
        this.supervisor = supervisor;
    }

    public Researcher getSupervisor() {
        return supervisor;
    }

    public void addDiplomaPaper(ResearchPaper paper) {
        diplomaPapers.add(
            Objects.requireNonNull(paper, "paper must not be null")
        );
    }

    public List<ResearchPaper> getDiplomaPapers() {
        return List.copyOf(diplomaPapers);
    }

    // graduate students must always have an active research profile — null is not allowed here
    @Override
    public void setResearchProfile(ResearchProfile researchProfile) {
        super.setResearchProfile(
            Objects.requireNonNull(
                researchProfile,
                "GraduateStudent must have an active ResearchProfile"
            )
        );
    }

    @Override
    public String toString() {
        return "GraduateStudent{id='%s', name='%s', degree=%s, major=%s, supervisor=%s, papers=%d}".formatted(
                getId(), getName(), getDegreeType(), getMajor(), (supervisor != null ? supervisor.getClass().getSimpleName() : "none"), diplomaPapers.size()
        );
    }
}
