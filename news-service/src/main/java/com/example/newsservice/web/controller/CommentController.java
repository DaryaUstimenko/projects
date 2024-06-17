package com.example.newsservice.web.controller;

import com.example.newsservice.aop.AuthorizeAction;
import com.example.newsservice.entity.Comment;
import com.example.newsservice.mapper.CommentMapper;
import com.example.newsservice.service.CommentService;
import com.example.newsservice.web.model.request.PaginationRequest;
import com.example.newsservice.web.model.request.UpsertCommentRequest;
import com.example.newsservice.web.model.response.CommentResponse;
import com.example.newsservice.web.model.response.ModelListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ModelListResponse<CommentResponse>> getByNews(@Valid PaginationRequest request,
                                                                        @RequestParam UUID newsId) {
        Page<Comment> comments = commentService.findAllByNewsId(newsId, request.pageRequest());

        return ResponseEntity.ok(
                ModelListResponse.<CommentResponse>builder()
                        .totalCount(comments.getTotalElements())
                        .data(comments.stream().map(commentMapper::commentToResponse).toList())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CommentResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(commentMapper.commentToResponse(commentService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<CommentResponse> addComment(@RequestBody UpsertCommentRequest request,
                                                      @RequestParam UUID postId,
                                                      @RequestParam UUID userId) {
        Comment newComment = commentService.addComment(commentMapper.upsertRequestToComment(request), userId, postId);

        return ResponseEntity.ok(commentMapper.commentToResponse(newComment));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "update")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable UUID id,
                                                         @RequestBody UpsertCommentRequest request) {
        Comment updatedComment = commentService.update(id, commentMapper.upsertRequestToComment(request));

        return ResponseEntity.ok(commentMapper.commentToResponse(updatedComment));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @AuthorizeAction(actionType = "delete")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        commentService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
