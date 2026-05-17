package university.enums;

/// Output formats for research paper citations.
///
/// Lets you pick how citations appear when exporting from
/// `ResearchProfile` or similar.
public enum CitationFormat {
    /// Readable plain text (e.g. "Author (Year). Title.")
    PLAIN_TEXT,
    /// BibTeX format for LaTeX documents
    BIBTEX,
}
