package ru.osiptsoff.newspaper.api.test;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import ru.osiptsoff.newspaper.api.environment.CommentsServiceTestEnvironment;
import ru.osiptsoff.newspaper.api.model.Comment;

@SpringBootTest
public class CommentsServiceTests {
    private final CommentsServiceTestEnvironment env;

    private final Integer commentsPerPage;

    @Autowired
    public CommentsServiceTests(
        CommentsServiceTestEnvironment env,
        @Value("${app.config.commentPageSize}") Integer commentsperPage) {
        this.env = env;
        this.commentsPerPage = commentsperPage;
    }

    @Test
    public void saveCommentTest() {
        Comment comment = new Comment();
        comment.setText("First comment");
        comment.setNews(env.getTestNews());

        comment = env.getCommentService().saveComment(comment);

        Optional<Comment> dbComment = env.getCommentRepository().findById(comment.getId());

        Assert.isTrue(dbComment.isPresent(), "Must be present");
        Assert.isTrue(dbComment.get().getText().equals(comment.getText()), "Texts must be equal");
    }

    @Test
    public void getAuthorLoginTest() {
        Comment comment = new Comment();
        comment.setText("Another comment");
        comment.setNews(env.getTestNews());
        comment.setAuthor(env.getTestUser());

        comment = env.getCommentService().saveComment(comment);

        Assert.isTrue(env
                        .getCommentRepository()
                        .getAuthorLogin(comment.getId())
                        .equals(env.getTestUser().getLogin()),
                         "Author must be set");
    }

    @Test
    public void deleteCommentTest() {
        Comment comment = new Comment();
        comment.setText("First comment");
        comment.setNews(env.getTestNews());

        comment = env.getCommentRepository().save(comment);

        Assert.isTrue(env.getCommentRepository().existsById(comment.getId()), "Must be in db");
        
        env.getCommentService().deleteComment(comment);

        Assert.isTrue(comment.getId() != null, "Id must not be null");

        Optional<Comment> dbComment = env.getCommentRepository().findById(comment.getId());

        Assert.isTrue(!dbComment.isPresent(), "Must not be present");
    }

    // WILL NOT PASS if configured comment page size is less then 2
    // this will not be fixed because task is set for 3 comments per page
    @Test
    public void findPageTest() {
        for(int i = 0; i < commentsPerPage + 1; i++) {
            Comment comment = new Comment();
            comment.setText("Comment " + i);
            comment.setNews(env.getTestNews());

            comment = env.getCommentRepository().save(comment);
        }

        Page<Comment> firstPage = env.getCommentService().findNthPageOfCommentsByNews(env.getTestNews(), 0);

        for(Comment comment : firstPage.getContent())
            System.out.println(comment.getText());

        Assert.isTrue(!firstPage.isLast(), "there must be more pages");

        Page<Comment> secondPage = env.getCommentService().findNthPageOfCommentsByNews(env.getTestNews(), 1);

        for(Comment comment : secondPage.getContent())
            System.out.println(comment.getText());

        Assert.isTrue(secondPage.isLast(), "this page must be last");
    }
}
