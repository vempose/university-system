package university.service;

import university.domain.academic.School;
import university.domain.news.News;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.domain.research.ResearchProject;
import university.domain.user.Employee;
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

    /// Returns all research papers from a given year, sorted by citations descending.
    ///
    /// @param year the publication year to filter by
    /// @return sorted list of papers
    public List<ResearchPaper> getPapersByYearSorted(int year) {
        return system
                .getUsers()
                .stream()
                .map(User::getResearchProfile)
                .filter(Objects::nonNull)
                .flatMap(p -> p.getPapers().stream())
                .filter(p -> p.publishDate().getYear() == year)
                .sorted(Comparator.comparingInt(ResearchPaper::citations).reversed())
                .toList();
    }

    /// Holds a researcher's name and profile for display.
    public record ResearcherInfo(String name, ResearchProfile profile) {}

    /// Returns all researchers in a school, sorted by h-index descending.
    ///
    /// Checks both students and employees for school membership.
    ///
    /// @param school the school to search in
    /// @return sorted list of researcher info
    public List<ResearcherInfo> getResearchersBySchoolSorted(School school) {
        return system
                .getUsers()
                .stream()
                .filter(u -> {
                    if (u instanceof Student s) return school.equals(s.getSchool());
                    if (u instanceof Employee e) return school.equals(e.getSchool());
                    return false;
                })
                .map(u -> {
                    ResearchProfile p = u.getResearchProfile();
                    if (p == null) return null;
                    return new ResearcherInfo(u.getName(), p);
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt((ResearcherInfo ri) -> ri.profile().calculateHIndex()).reversed())
                .toList();
    }

    /// Finds the researcher with the highest h-index in a specific school.
    ///
    /// @param school the school to search in
    /// @return the top research profile, if any
    public Optional<ResearchProfile> getTopCitedResearcherBySchool(
            School school
    ) {
        return getResearchersBySchoolSorted(school).stream()
                .findFirst()
                .map(ResearcherInfo::profile);
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
                .filter(profile -> profile.getPapers().stream()
                        .anyMatch(p -> p.publishDate().getYear() == year))
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

    /// Returns every research project from all users.
    ///
    /// @return list of all projects (no duplicates)
    public List<ResearchProject> getAllProjects() {
        return system
                .getUsers()
                .stream()
                .map(User::getResearchProfile)
                .filter(Objects::nonNull)
                .flatMap(p -> p.getProjects().stream())
                .distinct()
                .toList();
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
