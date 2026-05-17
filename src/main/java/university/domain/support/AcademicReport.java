package university.domain.support;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// Academic performance report for a student.
///
/// Stores entries (e.g. grades) and can generate formatted reports
/// and basic statistics like average and pass rate.
public class AcademicReport implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Pattern TRAILING_NUMBER_PATTERN = Pattern.compile(
            "(\\d+(?:\\.\\d+)?)\\s*$"
    );

    private final String id;
    private final LocalDate createdDate;
    private final List<String> entries = new ArrayList<>();

    /// Creates an empty report with a random ID.
    public AcademicReport() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = LocalDate.now();
    }

    /// Adds a new entry (e.g. a grade) to the report.
    ///
    /// @param entry must not be null or blank
    public void addEntry(String entry) {
        if (entry == null || entry.isBlank()) {
            throw new IllegalArgumentException(
                    "Report entry must not be null or blank."
            );
        }
        entries.add(entry);
    }

    /// Generates a formatted report listing all entries.
    public String generateMarksReport() {
        if (entries.isEmpty()) {
            return "Academic Report [%s] — No entries recorded.".formatted(id);
        }

        String divider = "=".repeat(60);
        List<String> indexedEntries = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            indexedEntries.add("  [%3d] %s".formatted(i + 1, entries.get(i)));
        }
        String body = String.join("\n", indexedEntries);

        return "%s\n  ACADEMIC MARKS REPORT\n  Report ID   : %s\n  Generated   : %s\n  Total entries: %d\n%s\n%s\n%s".formatted(
                divider,
                id,
                createdDate,
                entries.size(),
                divider,
                body,
                divider
        );
    }

    /// Computes statistics (min, max, avg, pass rate) from numeric
    /// scores found at the end of each entry line.
    public String generateStatistics() {
        if (entries.isEmpty()) {
            return "Academic Report [%s] — Statistics unavailable: no entries.".formatted(
                    id
            );
        }

        DoubleSummaryStatistics stats = entries
                .stream()
                .map(this::extractTrailingScore)
                .flatMap(Optional::stream)
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        String scoreDetails =
                stats.getCount() > 0
                        ? "  Minimum score   : %.2f\n  Maximum score   : %.2f\n  Average score   : %.2f\n  Pass rate (>=50): %.1f%%\n".formatted(
                        stats.getMin(),
                        stats.getMax(),
                        stats.getAverage(),
                        computePassRate(50.0)
                )
                        : "  No numeric scores found in entries.\n";

        return "%s\n  ACADEMIC REPORT — STATISTICS\n  Report ID       : %s\n  Generated       : %s\n%s\n  Total entries   : %d\n  Scored entries  : %d\n%s%s".formatted(
                "=".repeat(60),
                id,
                createdDate,
                "-".repeat(60),
                entries.size(),
                stats.getCount(),
                scoreDetails,
                "=".repeat(60)
        );
    }

    private Optional<Double> extractTrailingScore(String entry) {
        Matcher m = TRAILING_NUMBER_PATTERN.matcher(entry.stripTrailing());
        if (m.find()) {
            try {
                return Optional.of(Double.parseDouble(m.group(1)));
            } catch (NumberFormatException ignored) {
            }
        }
        return Optional.empty();
    }

    private double computePassRate(double threshold) {
        double[] scored = entries
                .stream()
                .map(this::extractTrailingScore)
                .flatMap(Optional::stream)
                .mapToDouble(Double::doubleValue)
                .toArray();

        if (scored.length == 0) return 0.0;

        long passed = 0;
        for (double score : scored) {
            if (score >= threshold) passed++;
        }
        return (passed * 100.0) / scored.length;
    }

    public String getId() {
        return id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /// Returns a summary of the report.
    @Override
    public String toString() {
        return "AcademicReport{id='%s', createdDate=%s, entryCount=%d}".formatted(
                id,
                createdDate,
                entries.size()
        );
    }
}
