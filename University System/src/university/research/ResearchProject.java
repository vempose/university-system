package university.research;

import university.exceptions.NonResearcherJoinProjectException;
import university.interfaces.Researcher;
import university.users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchProject implements Serializable{
    private String topic;
    private List<Researcher> participants;
    private List<ResearchPaper> publishedPapers;
    public ResearchProject(String topic){
        this.topic = topic;
        this.participants = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }
    public void addParticipant(Object candidate) throws NonResearcherJoinProjectException {
        if (!(candidate instanceof Researcher researcher)) {
            String name = (candidate instanceof User u) ? u.getName() : candidate.toString();
            throw new NonResearcherJoinProjectException(name);
        }
        participants.add(researcher);
    }
    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }

    public String getTopic() { 
    	return topic; 
    	}
    public List<Researcher> getParticipants() { 
    	return new ArrayList<>(participants); }
    public List<ResearchPaper> getPublishedPapers() { 
    	return new ArrayList<>(publishedPapers); 
    	}

    @Override
    public String toString() {
        return "ResearchProject{topic=" + topic + ", participants=" + participants.size() + ", papers=" + publishedPapers.size() + "}";
    }
}
