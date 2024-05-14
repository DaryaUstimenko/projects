package com.example.newsservice.mapper;

import com.example.newsservice.entity.News;
import com.example.newsservice.web.model.request.UpsertNewsRequest;
import com.example.newsservice.web.model.response.BriefNewsResponse;
import com.example.newsservice.web.model.response.NewsResponse;
import org.mapstruct.*;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NewsMapper {

    News upsertRequestToNews(UpsertNewsRequest request);

    @Mapping(source = "author.username", target = "username")
    BriefNewsResponse newsToBriefResponse(News news);

    @Mapping(source = "author.username", target = "username")
    NewsResponse newsToResponse(News news);

}
