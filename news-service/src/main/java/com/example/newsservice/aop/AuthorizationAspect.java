package com.example.newsservice.aop;

import com.example.newsservice.entity.Comment;
import com.example.newsservice.entity.News;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final NewsService newsService;
    private final CommentService commentService;
    private final AuthenticationFacade authenticationFacade;

    @Before("@annotation(authorizeAction)")
    public void checkAuthorization(JoinPoint joinPoint, AuthorizeAction authorizeAction) throws Throwable {
        Object[] args = joinPoint.getArgs();
        UUID newsId = (UUID) args[0];
        News news = newsService.findById(newsId);
        UUID commentId = (UUID) args[0];
        Comment comment = commentService.findById(commentId);


        if (news == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "News item not found");
        }

        String currentUserId = authenticationFacade.getCurrentUserId();
        if (!news.getAuthor().getId().equals(currentUserId) || !comment.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to " + authorizeAction.actionType() + " this news item");
        }
    }
}
