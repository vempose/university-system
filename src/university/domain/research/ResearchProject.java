package university.domain.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import university.exception.NonResearcherJoinProjectException;

public final class ResearchProject {

    private final String id;
    private final String topic;
    private final List<Researcher> participants;
    private final List<ResearchPaper> publishedPapers;

    public ResearchProject(String id, String topic) {
        this.id = id;
        this.topic = topic;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    public void addParticipant(Researcher researcher)
        throws NonResearcherJoinProjectException {
        if (researcher == null) {
            throw new NonResearcherJoinProjectException(
                "Cannot add a null participant to project '%s'.".formatted(id)
            );
        }
        participants.add(researcher);
    }

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

        @Override
    public String toString() {
        return "ResearchProject{id='%s', topic='%s', participants=%d, papers=%d}".formatted(id, topic, participants.size(), publishedPapers.size());
    }

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
}
