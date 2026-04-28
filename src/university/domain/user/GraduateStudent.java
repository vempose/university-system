package university.domain.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import university.domain.academic.Major;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.domain.research.Researcher;
import university.enums.DegreeType;
import university.enums.Language;
import university.exception.InvalidSupervisorException;

public class GraduateStudent extends Student {

    public static final int MIN_SUPERVISOR_H_INDEX = 3;

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<ResearchPaper> diplomaPapers = new ArrayList<>();
    private Researcher supervisor;

    public GraduateStudent(
        String id,
        String name,
        String email,
        String password,
        Language language,
        DegreeType degreeType,
        Major major
    ) {
        super(id, name, email, password, language, degreeType, major);
        if (degreeType == DegreeType.MASTER || degreeType == DegreeType.PHD) {
            setResearchProfile(new ResearchProfile());
        }
    }

    public Researcher getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Researcher supervisor)
        throws InvalidSupervisorException {
        if (
            supervisor == null ||
            supervisor.calculateHIndex() < MIN_SUPERVISOR_H_INDEX
        ) {
            throw new InvalidSupervisorException(
                "Supervisor must have an H-index of at least " +
                    MIN_SUPERVISOR_H_INDEX
            );
        }
        this.supervisor = supervisor;
    }

    public void addDiplomaPaper(ResearchPaper paper) {
        diplomaPapers.add(paper);
    }

    public List<ResearchPaper> getDiplomaPapers() {
        return List.copyOf(diplomaPapers);
    }

    @Override
    public String toString() {
        return "GraduateStudent{id='%s', name='%s'}".formatted(
            getId(),
            getName()
        );
    }
}
