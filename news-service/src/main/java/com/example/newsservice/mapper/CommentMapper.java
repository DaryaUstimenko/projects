package com.example.newsservice.mapper;

import com.example.newsservice.entity.Comment;
import com.example.newsservice.web.model.request.UpsertCommentRequest;
import com.example.newsservice.web.model.response.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommentMapper {

    Comment upsertRequestToComment(UpsertCommentRequest request);

    @Mapping(source = "user.username", target = "username")
    CommentResponse commentToResponse(Comment comment);

}
