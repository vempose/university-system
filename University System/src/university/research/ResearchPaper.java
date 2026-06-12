package university.research;
import university.enums.CitationFormat;
import university.interfaces.Researcher;
import university.users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ResearchPaper implements Serializable{
    private String title;
    private List<Researcher> authors;
    private String journal;
    private int pages;
    private Date date;
    private int citations;
    private String doi;
    public ResearchPaper(String title, String journal, int pages, Date date, String doi){
        this.title = title;
        this.journal = journal;
        this.pages = pages;
        this.date = date;
        this.doi = doi;
        this.citations =0;
        this.authors =new ArrayList<>();
    }
    public String getCitation(CitationFormat format) {
        int year = getYear(date);
        String authorStr = authorsString();
        if (format == CitationFormat.BIBTEX) {
            return "@article{" + doi + ",\n  title={" + title + "},\n  author={"+ authorStr + "},\n  journal={" + journal + "},\n  year={" + year + "}\n}";
        } else if (format == CitationFormat.APA) {
            return authorStr + " (" + year + "). " + title + ". " + journal + ". doi:" + doi;
        } else if (format == CitationFormat.IEEE) {
            return authorStr + ", \"" + title + ",\" " + journal + "," + year + ". doi:" + doi;
        } else {
            return authorStr + ". \"" + title + "\". " + journal + ". " + year + ". DOI: " + doi;
        }
    }
    private int getYear(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal.get(Calendar.YEAR);
    }
    private String authorsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            if (i > 0) sb.append(",");
            if (authors.get(i) instanceof User u) sb.append(u.getName());
        }
        return sb.isEmpty() ? "N/A" : sb.toString();
    }

    public void addAuthor(Researcher author) { authors.add(author); }
    public void addCitation() { 
    	this.citations++;
}
    public String getTitle() { 
    	return title; }
    public void setTitle(String title) { 
    	this.title = title; }
    public List<Researcher> getAuthors() { 
    	return authors; }
    public String getJournal() { 
    	return journal; }
    public void setJournal(String journal) { 
    	this.journal = journal; }
    public int getPages() { 
    	return pages; }
    public void setPages(int pages) {
    	this.pages = pages; }
    public Date getDate() { 
    	return date; }
    public void setDate(Date date) { 
    	this.date = date; }
    public int getCitations() { 
    	return citations; }
    public void setCitations(int citations) { 
    	this.citations = citations; }
    public String getDoi() { 
    	return doi; }
    public void setDoi(String doi) { 
    	this.doi = doi; }

    @Override
    public String toString() {
        return "ResearchPaper{title=" + title + ", journal=" + journal + ", citations=" + citations + ", doi=" + doi + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper p)) return false;
        return doi != null && doi.equals(p.doi);
    }

    @Override
    public int hashCode() {
        return doi != null ? doi.hashCode() :0;
    }
}
