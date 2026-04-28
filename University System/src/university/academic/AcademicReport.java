package university.academic;

import university.research.ResearchPaper;
import university.users.Student;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class AcademicReport implements Serializable{
    private String reportTitle;
    private Date generatedDate;
    public AcademicReport(String reportTitle){
        this.reportTitle = reportTitle;
        this.generatedDate = new Date();
    }
    public String generateResearchReport(List<ResearchPaper> papers, Comparator<ResearchPaper> c){
        List<ResearchPaper> sorted = papers.stream().sorted(c).toList();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("RESEARCH REPORT%n"));
        sb.append(String.format("Title: %s%n", reportTitle));
        sb.append(String.format("Date: %s%n", generatedDate.toString()));
        sb.append(String.format("Papers: %d%n", sorted.size()));
        for (ResearchPaper p : sorted) {
            sb.append(String.format(" %s (%d citations)%n", p.getTitle(), p.getCitations()));
        }
        return sb.toString();
    }

    public String generateStudentReport(List<Student> students, Comparator<Student> c){
        List<Student> sorted = students.stream().sorted(c).toList();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("STUDENT REPORT%n"));
        sb.append(String.format("Title: %s%n", reportTitle));
        sb.append(String.format("Date: %s%n", generatedDate.toString()));
        for (Student s : sorted) {
            sb.append(String.format(" %-20s  GPA: %.2f%n", s.getName(), s.getGpa()));
        }
        return sb.toString();
    }
    public String getReportTitle() { 
    	return reportTitle; }
    public Date   getGeneratedDate() { 
    	return generatedDate; }

    @Override
    public String toString(){
        return String.format(
            "AcademicReport { title='%s', generated=%s }",
            reportTitle, generatedDate.toString()
        );
    }
}
