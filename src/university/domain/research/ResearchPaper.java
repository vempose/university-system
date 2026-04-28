package university.domain.research;

import university.enums.CitationFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public record ResearchPaper(String title, String authors, String journalName, String pages, int pageCount,
                            LocalDate publishDate, String doi, int citations) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResearchPaper {
        if (pageCount < 1) throw new IllegalArgumentException(
                "pageCount must be >= 1, got: " + pageCount
        );
        if (citations < 0) throw new IllegalArgumentException(
                "citations must be non-negative, got: " + citations
        );
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
        return "@article{%s,\n  author={%s}, title={%s}, journal={%s}, year={%d}, pages={%s}, doi={%s}\n}".formatted(
                citationKey,
                authors,
                title,
                journalName,
                publishDate.getYear(),
                pages,
                doi
        );
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
        return "ResearchPaper{doi='%s', title='%s', citations=%d}".formatted(
                doi,
                title,
                citations
        );
    }
}
