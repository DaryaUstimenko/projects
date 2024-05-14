package com.example.newsservice.mapper;

import com.example.newsservice.entity.News;
import com.example.newsservice.web.model.response.BriefNewsResponse;
import com.example.newsservice.web.model.response.NewsResponse;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper {

    @Autowired
    private NewsMapper delegate;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public BriefNewsResponse newsToBriefResponse(News news) {
        BriefNewsResponse response = delegate.newsToBriefResponse(news);
        response.setUsername(news.getAuthor().getUsername());
        response.setCommentsCount(news.getComments().size());

        return response;
    }

    @Override
    public NewsResponse newsToResponse(News news) {
        NewsResponse response = delegate.newsToResponse(news);
        response.setUsername(news.getAuthor().getUsername());
        response.setComments(news.getComments().stream()
                .map(it -> commentMapper.commentToResponse(it))
                .toList());

        return response;
    }
}
