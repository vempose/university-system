package university.domain.research;

import java.time.LocalDate;
import java.util.Objects;
import university.enums.CitationFormat;

public final class ResearchPaper {

    private final String title;
    private final String authors;
    private final String journalName;
    private final String pages;
    private final LocalDate publishDate;
    private final String doi;
    private final int citations;

    public ResearchPaper(
        String title,
        String authors,
        String journalName,
        String pages,
        LocalDate publishDate,
        String doi,
        int citations
    ) {
        this.title = title;
        this.authors = authors;
        this.journalName = journalName;
        this.pages = pages;
        this.publishDate = publishDate;
        this.doi = doi;
        if (citations < 0) throw new IllegalArgumentException(
            "citations must be non-negative, got: " + citations
        );
        this.citations = citations;
    }

    public String getCitation(CitationFormat format) {
        return switch (format) {
            case PLAIN_TEXT -> buildPlainTextCitation();
            case BIBTEX -> buildBibtexCitation();
        };
    }

    private String buildPlainTextCitation() {
        return "%s (%d). %s. %s, %s. DOI: %s".formatted(
            authors,
            publishDate.getYear(),
            title,
            journalName,
            pages,
            doi
        );
    }

    private String buildBibtexCitation() {
        String citationKey = doi.replaceAll("[^A-Za-z0-9]", "_");
        return """
        @article{%s,
          author  = {%s},
          title   = {%s},
          journal = {%s},
          year    = {%d},
          pages   = {%s},
          doi     = {%s}
        }""".formatted(
                citationKey,
                authors,
                title,
                journalName,
                publishDate.getYear(),
                pages,
                doi
            );
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getJournalName() {
        return journalName;
    }

    public String getPages() {
        return pages;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public String getDoi() {
        return doi;
    }

    public int getCitations() {
        return citations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResearchPaper other)) return false;
        return Objects.equals(doi, other.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(doi);
    }

        @Override
    public String toString() {
        return "ResearchPaper{doi='%s', title='%s', citations=%d}".formatted(doi, title, citations);
    }
}
