package university.domain.research;

import university.exception.NonResearcherJoinProjectException;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/// A research project with a topic and list of participants.
///
/// Tracks published papers that came out of the project.
public final class ResearchProject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String topic;
    private final List<Researcher> participants;
    private final List<ResearchPaper> publishedPapers;

    /// Creates a project with the given ID and topic.
    public ResearchProject(String id, String topic) {
        this.id = id;
        this.topic = topic;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    /// Adds a researcher to the project.
    ///
    /// @throws NonResearcherJoinProjectException if researcher is null
    public void addParticipant(Researcher researcher)
            throws NonResearcherJoinProjectException {
        if (researcher == null) {
            throw new NonResearcherJoinProjectException(
                    "Cannot add a null participant to project '%s'.".formatted(id)
            );
        }
        participants.add(researcher);
    }

    /// Adds a paper that was produced by this project (if not null).
    public void addPublishedPaper(ResearchPaper paper) {
        if (paper != null) publishedPapers.add(paper);
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public List<Researcher> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public List<ResearchPaper> getPublishedPapers() {
        return Collections.unmodifiableList(publishedPapers);
    }

    /// Two projects are equal if they have the same ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchProject other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /// Returns a summary of the project.
    @Override
    public String toString() {
        return "ResearchProject{id='%s', topic='%s', participants=%d, papers=%d}".formatted(
                id,
                topic,
                participants.size(),
                publishedPapers.size()
        );
    }
}
