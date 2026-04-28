package university.service;

import java.util.*;
import java.util.stream.Collectors;
import university.domain.academic.School;
import university.domain.news.News;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.domain.user.Student;
import university.domain.user.User;
import university.enums.CitationFormat;
import university.enums.NewsTopic;
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
            .flatMap(p -> p.getPapers().stream())
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
            .filter(u -> {
                if (u instanceof Student s) return school.equals(s.getSchool());
                return false;
            })
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

    public void printTopCitedResearcherOfYear(int year) {
        Optional<ResearchProfile> top = getTopCitedResearcherOfYear(year);
        if (top.isPresent()) {
            ResearchProfile profile = top.get();
            System.out.println(
                "Top cited researcher of " +
                    year +
                    ": h-index=" +
                    profile.getHIndex() +
                    ", papers=" +
                    profile.getPapers().size()
            );
            profile.printPapers(
                Comparator.comparingInt(ResearchPaper::getCitations).reversed()
            );
        } else {
            System.out.println("No researchers found for year " + year + ".");
        }
    }

    public void printTopCitedResearcherBySchool(School school) {
        Optional<ResearchProfile> top = getTopCitedResearcherBySchool(school);
        if (top.isPresent()) {
            ResearchProfile profile = top.get();
            System.out.println(
                "Top cited researcher of " +
                    school.getName() +
                    ": h-index=" +
                    profile.getHIndex()
            );
        } else {
            System.out.println(
                "No researchers found for school: " + school.getName()
            );
        }
    }

    public News generateTopCitedResearcherNews(int year) {
        Optional<ResearchProfile> top = getTopCitedResearcherOfYear(year);
        String content = top
            .map(
                p ->
                    "Top cited researcher of " +
                    year +
                    " has h-index: " +
                    p.calculateHIndex() +
                    " with " +
                    p.getPapers().size() +
                    " paper(s)."
            )
            .orElse("No research data available for year " + year + ".");
        return new News(
            "Top Cited Researcher of " + year,
            content,
            NewsTopic.RESEARCH
        );
    }
}
