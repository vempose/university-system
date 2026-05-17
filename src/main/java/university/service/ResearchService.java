package university.service;

import university.domain.academic.School;
import university.domain.news.News;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.domain.user.Student;
import university.domain.user.User;
import university.enums.NewsTopic;
import university.system.UniversitySystem;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/// Handles research stuff — papers, citations, h-index calculations.
///
/// Works with the system to find researchers, sort papers, and figure out
/// who the top-cited people are each year.
public class ResearchService {

    private final UniversitySystem system;

    /// Creates a ResearchService.
    ///
    /// @param system the system holding all user and paper data
    public ResearchService(UniversitySystem system) {
        this.system = system;
    }

    /// Returns every research paper from all users, sorted however you want.
    ///
    /// @param comparator defines the sort order
    /// @return sorted list of papers
    public List<ResearchPaper> getAllPapersSorted(Comparator<ResearchPaper> comparator) {
        return system
                .getUsers()
                .stream()
                .map(User::getResearchProfile)
                .filter(Objects::nonNull)
                .flatMap(p -> p.getPapers().stream())
                .sorted(comparator)
                .toList();
    }

    /// Finds the researcher with the highest h-index in a specific school.
    ///
    /// Only looks at students for now.
    ///
    /// @param school the school to search in
    /// @return the top research profile, if any
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
                .max(Comparator.comparingInt(ResearchProfile::calculateHIndex));
    }

    /// Finds the researcher with the most citations in a given year.
    ///
    /// @param year the year to check
    /// @return the top research profile, if any
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
                                        .filter(p -> p.publishDate().getYear() == year)
                                        .mapToInt(ResearchPaper::citations)
                                        .sum()
                        )
                );
    }

    /// Generates a news article about the top-cited researcher of a year.
    ///
    /// @param year the year
    /// @return a news item
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
