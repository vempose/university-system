package university.domain.support;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcademicReport {

    private static final Pattern TRAILING_NUMBER_PATTERN = Pattern.compile(
        "(\\d+(?:\\.\\d+)?)\\s*$"
    );

    private final String id;
    private final LocalDate createdDate;
    private final List<String> entries = new ArrayList<>();

    public AcademicReport() {
        this.id = UUID.randomUUID().toString();
        this.createdDate = LocalDate.now();
    }

    public void addEntry(String entry) {
        if (entry == null || entry.isBlank()) {
            throw new IllegalArgumentException(
                "Report entry must not be null or blank."
            );
        }
        entries.add(entry);
    }

    public String generateMarksReport() {
        if (entries.isEmpty()) {
            return "Academic Report [" + id + "] — No entries recorded.";
        }

        var sb = new StringBuilder();
        sb.append("=".repeat(60)).append('\n');
        sb.append("  ACADEMIC MARKS REPORT").append('\n');
        sb.append("  Report ID   : ").append(id).append('\n');
        sb.append("  Generated   : ").append(createdDate).append('\n');
        sb.append("  Total entries: ").append(entries.size()).append('\n');
        sb.append("=".repeat(60)).append('\n');

        var indexedEntries = new ArrayList<String>();
        for (int i = 0; i < entries.size(); i++) {
            indexedEntries.add(
                String.format("  [%3d] %s", i + 1, entries.get(i))
            );
        }
        sb.append(String.join("\n", indexedEntries));
        sb.append('\n').append("=".repeat(60));

        return sb.toString();
    }

    public String generateStatistics() {
        if (entries.isEmpty()) {
            return (
                "Academic Report [" +
                id +
                "] — Statistics unavailable: no entries."
            );
        }

        var stats = entries
            .stream()
            .map(this::extractTrailingScore)
            .flatMap(Optional::stream)
            .mapToDouble(Double::doubleValue)
            .summaryStatistics();

        var sb = new StringBuilder();
        sb.append("=".repeat(60)).append('\n');
        sb.append("  ACADEMIC REPORT — STATISTICS").append('\n');
        sb.append("  Report ID       : ").append(id).append('\n');
        sb.append("  Generated       : ").append(createdDate).append('\n');
        sb.append("-".repeat(60)).append('\n');
        sb.append(String.format("  Total entries   : %d%n", entries.size()));
        sb.append(String.format("  Scored entries  : %d%n", stats.getCount()));

        if (stats.getCount() > 0) {
            sb.append(
                String.format("  Minimum score   : %.2f%n", stats.getMin())
            );
            sb.append(
                String.format("  Maximum score   : %.2f%n", stats.getMax())
            );
            sb.append(
                String.format("  Average score   : %.2f%n", stats.getAverage())
            );
            sb.append(
                String.format(
                    "  Pass rate (>=50): %.1f%%%n",
                    computePassRate(50.0)
                )
            );
        } else {
            sb.append("  No numeric scores found in entries.\n");
        }
        sb.append("=".repeat(60));

        return sb.toString();
    }

    private Optional<Double> extractTrailingScore(String entry) {
        Matcher m = TRAILING_NUMBER_PATTERN.matcher(entry.stripTrailing());
        if (m.find()) {
            try {
                return Optional.of(Double.parseDouble(m.group(1)));
            } catch (NumberFormatException ignored) {}
        }
        return Optional.empty();
    }

    private double computePassRate(double threshold) {
        var scored = entries
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

        @Override
    public String toString() {
        return (
            "AcademicReport{" +
            "id='" +
            id +
            '\'' +
            ", createdDate=" +
            createdDate +
            ", entryCount=" +
            entries.size() +
            '}'
        );
    }
}
