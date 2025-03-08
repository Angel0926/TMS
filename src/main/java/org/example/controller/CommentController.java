package org.example.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.example.models.Comment;
import org.example.service.CommentService;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    JwtUtil jwtUtil=new JwtUtil();

    @PostMapping("/{taskId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId,
                                              @RequestBody Comment comment,
                                              @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        Comment createdComment = (Comment) commentService.addComment(taskId, comment.getText(), email);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<org.example.models.Comment>> getComments(@PathVariable Long taskId) {
        List<Comment> comments = commentService.getCommentsByTask(taskId);
        return ResponseEntity.ok(comments);
    }
}

