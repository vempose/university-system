package university.service;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import university.domain.academic.School;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.domain.user.Student;
import university.domain.user.User;
import university.enums.CitationFormat;
import university.system.UniversitySystem;

public class ResearchService {

    private final UniversitySystem system;

    public ResearchService(UniversitySystem system) {
        this.system = system;
    }

    public void printAllPapers(Comparator<ResearchPaper> comparator) {
        system
            .getUsers()
            .stream()
            .map(User::getResearchProfile)
            .filter(Objects::nonNull)
            .flatMap(profile -> profile.getPapers().stream())
            .sorted(comparator)
            .forEach(p ->
                System.out.println(p.getCitation(CitationFormat.PLAIN_TEXT))
            );
    }

    public Optional<ResearchProfile> getTopCitedResearcherBySchool(
        School school
    ) {
        return system
            .getUsers()
            .stream()
            .filter(u -> u instanceof Student s && school.equals(s.getSchool()))
            .map(User::getResearchProfile)
            .filter(Objects::nonNull)
            .max(Comparator.comparingInt(ResearchProfile::getHIndex));
    }

    public Optional<ResearchProfile> getTopCitedResearcherOfYear(int year) {
        return system
            .getUsers()
            .stream()
            .map(User::getResearchProfile)
            .filter(Objects::nonNull)
            .max(
                Comparator.comparingInt(profile ->
                    profile
                        .getPapers()
                        .stream()
                        .filter(p -> p.getPublishDate().getYear() == year)
                        .mapToInt(ResearchPaper::getCitations)
                        .sum()
                )
            );
    }
}
