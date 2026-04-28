package university.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchProfile implements Serializable {
    private List<String> topics;
    private int hIndex;
    private List<ResearchPaper> papers;
    public ResearchProfile() {
        this.topics = new ArrayList<>();
        this.papers = new ArrayList<>();
        this.hIndex = 0;
    }
    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
        recalculateHIndex();
    }

    public void recalculateHIndex() {
        List<Integer> citations = new ArrayList<>();
        for (ResearchPaper p : papers) {
            citations.add(p.getCitations());
        }
        citations.sort((a, b) -> b - a);
        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) h = i + 1;
            else break;
        }
        this.hIndex = h;
    }

    public void addTopic(String topic) { 
    	topics.add(topic); 
    	}
    public List<String> getTopics() {
    	return new ArrayList<>(topics); }
    public int getHIndex() { 
    	return hIndex; }
    public List<ResearchPaper> getPapers() {
    	return new ArrayList<>(papers); 
    	}

    @Override
    public String toString() {
        return "ResearchProfile{hIndex=" + hIndex + ", papers=" + papers.size() + ", topics=" + topics + "}";
    }
}
