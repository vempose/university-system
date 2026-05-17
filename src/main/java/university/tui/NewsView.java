package university.tui;

import university.tui.Messages;
import university.domain.news.News;
import university.domain.news.NewsComment;
import university.domain.user.User;
import university.system.UniversitySystem;

import java.util.List;

class NewsView {

    private final Session session;

    NewsView(Session session) {
        this.session = session;
    }

    void show() {
        UniversitySystem system = session.getSystem();

        while (true) {
            List<News> allNews = system.getNewsList();
            ConsoleMenu.printHeader(Messages.get("news.title"));

            if (allNews.isEmpty()) {
                ConsoleMenu.printInfo(Messages.get("news.no_news"));
                System.out.println("\n  [0]  Go Back");
                int choice = ConsoleInput.readInt("\n  Choose an option: ", 0, 0);
                return;
            }

            for (int i = 0; i < allNews.size(); i++) {
                News n = allNews.get(i);
                System.out.printf(
                        "  [%d]  %s%s | %s%n",
                        i + 1,
                        n.isPinned() ? "[PINNED] " : "",
                        n.getTopic(),
                        n.getTitle()
                );
            }
            System.out.println();
            System.out.println("  [0]  Go Back");

            int choice = ConsoleInput.readInt("\n  " + Messages.get("news.select_read") + " (#): ", 0, allNews.size());
            if (choice == 0) return;

            News selected = allNews.get(choice - 1);
            showNewsDetail(selected);
        }
    }

    private void showNewsDetail(News news) {
        ConsoleMenu.printSection(news.getTitle());
        System.out.println("  Topic: " + news.getTopic() + (news.isPinned() ? " [PINNED]" : ""));
        System.out.println("  Date: " + news.getCreatedDate());
        System.out.println();
        System.out.println("  " + news.getContent());
        System.out.println();

        List<NewsComment> comments = news.getComments();
        if (!comments.isEmpty()) {
            ConsoleMenu.printDivider();
            System.out.println("  Comments (" + comments.size() + "):");
            for (NewsComment c : comments) {
                System.out.printf(
                        "    %s (%s): %s%n",
                        c.getAuthor().getName(),
                        c.getCreatedDate().toLocalDate(),
                        c.getText()
                );
            }
        }

        System.out.println();
        System.out.println("  [1]  " + Messages.get("news.add_comment"));
        System.out.println("  [0]  Go Back");

        int choice = ConsoleInput.readInt("\n  Choose an option: ", 0, 1);
        if (choice == 1) {
            addComment(news);
            ConsoleInput.waitForEnter();
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
    }
}
