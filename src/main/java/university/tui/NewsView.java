package university.tui;

import university.tui.Messages;
import university.domain.news.News;
import university.domain.news.NewsComment;
import university.domain.user.User;
import university.system.UniversitySystem;

import java.util.Comparator;
import java.util.List;

/// News menu — browse, read, pin, and comment on news posts
/// with topic filtering.
class NewsView {

    private final Session session;

    NewsView(Session session) {
        this.session = session;
    }

    /// Shows the news list and detail view with comments.
    void show() {
        UniversitySystem system = session.getSystem();

        while (true) {
            List<News> allNews = system.getNewsList().stream()
                    .sorted(Comparator.comparing(News::isPinned).reversed())
                    .toList();
            ConsoleMenu.printHeader(Messages.get("news.title"));

            if (allNews.isEmpty()) {
                ConsoleMenu.printInfo(Messages.get("news.no_news"));
                System.out.println("\n  [0]  " + Messages.get("news.back"));
                ConsoleInput.readInt("\n  " + Messages.get("menu.choose") + ": ", 0, 0);
                return;
            }

            for (int i = 0; i < allNews.size(); i++) {
                News n = allNews.get(i);
                String pinned = n.isPinned() ? Messages.get("news.pinned") + " " : "";
                System.out.printf("  [%d]  %s%s | %s%n", i + 1, pinned, n.getTopic(), n.getTitle());
            }
            System.out.println();
            System.out.println("  [0]  " + Messages.get("news.back"));

            int choice = ConsoleInput.readInt("\n  " + Messages.get("news.select_read") + ": ", 0, allNews.size());
            if (choice == 0) return;

            News selected = allNews.get(choice - 1);
            showNewsDetail(selected);
        }
    }

    private void showNewsDetail(News news) {
        ConsoleMenu.printSection(news.getTitle());
        System.out.println("  " + Messages.get("news.topic") + ": " + news.getTopic() + (news.isPinned() ? " " + Messages.get("news.pinned") : ""));
        System.out.println("  " + Messages.get("news.date") + ": " + news.getCreatedDate());
        System.out.println();
        System.out.println("  " + news.getContent());
        System.out.println();

        List<NewsComment> comments = news.getComments();
        if (!comments.isEmpty()) {
            ConsoleMenu.printDivider();
            System.out.println("  " + Messages.get("news.comments_header", String.valueOf(comments.size())) + ":");
            for (NewsComment c : comments) {
                System.out.printf("    %s (%s): %s%n",
                        c.getAuthor().getName(),
                        c.getCreatedDate().toLocalDate(),
                        c.getText()
                );
            }
        }

        System.out.println();
        System.out.println("  [1]  " + Messages.get("news.add_comment"));
        System.out.println("  [0]  " + Messages.get("news.back"));

        int choice = ConsoleInput.readInt("\n  " + Messages.get("menu.choose") + ": ", 0, 1);
        if (choice == 1) {
            addComment(news);
        }
    }

    private void addComment(News news) {
        User user = session.getCurrentUser();
        String text = ConsoleInput.readLine("  " + Messages.get("news.your_comment") + ": ");
        if (!text.isEmpty()) {
            NewsComment comment = new NewsComment(text, user);
            news.addComment(comment);
            ConsoleMenu.printSuccess(Messages.get("news.comment_added"));
        }
        ConsoleInput.waitForEnter();
    }
}
