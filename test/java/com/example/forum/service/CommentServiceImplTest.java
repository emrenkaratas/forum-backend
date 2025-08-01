package com.example.forum.service;

import com.example.forum.dto.CommentRequest;
import com.example.forum.dto.CommentResponse;
import com.example.forum.model.Comment;
import com.example.forum.model.Thread;
import com.example.forum.model.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock private CommentRepository commentRepository;
    @Mock private ThreadRepository threadRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Thread thread;
    private User author, inserter;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setId(1L);
        thread = new Thread();
        thread.setId(2L);
        inserter = new User();
        inserter.setId(3L);
    }

    @Test
    void createComment_success() {

        CommentRequest req = new CommentRequest();
        req.setContent("Nice!");
        req.setThreadId(thread.getId());
        req.setUserId(author.getId());
        req.setInsertedById(inserter.getId());

        when(threadRepository.findById(thread.getId()))
                .thenReturn(Optional.of(thread));
        when(userRepository.findById(author.getId()))
                .thenReturn(Optional.of(author));
        when(userRepository.findById(inserter.getId()))
                .thenReturn(Optional.of(inserter));
        when(commentRepository.save(any(Comment.class)))
                .thenAnswer(inv -> inv.getArgument(0));


        CommentResponse resp = commentService.createComment(req);


        assertThat(resp.getContent()).isEqualTo("Nice!");
        assertThat(resp.getThreadId()).isEqualTo(thread.getId());
        assertThat(resp.getUserId()).isEqualTo(author.getId());
        verify(commentRepository).save(any(Comment.class));
    }


    @Test
    void createComment_threadMissing_throws() {
        CommentRequest req = new CommentRequest();
        req.setThreadId(99L);
        when(threadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.createComment(req))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Thread not found");
    }

    @Test
    void getCommentsByThread_returnsList() {

        Comment c1 = new Comment();
        c1.setThread(thread);
        c1.setUser(author);
        Comment c2 = new Comment();
        c2.setThread(thread);
        c2.setUser(author);

        when(commentRepository.findByThreadId(thread.getId()))
                .thenReturn(List.of(c1, c2));

        List<CommentResponse> all = commentService.getCommentsByThread(thread.getId());
        assertThat(all).hasSize(2);

        assertThat(all.get(0).getThreadId()).isEqualTo(thread.getId());
    }

    @Test
    void updateComment_success() {

        Comment existing = new Comment();
        existing.setId(5L);
        existing.setContent("old");
        existing.setThread(thread);
        existing.setUser(author);

        when(commentRepository.findById(5L))
                .thenReturn(Optional.of(existing));

        CommentRequest req = new CommentRequest();
        req.setContent("new");
        req.setUpdatedById(null);

        when(commentRepository.save(existing)).thenReturn(existing);


        var updated = commentService.updateComment(5L, req);


        assertThat(updated.getContent()).isEqualTo("new");
    }

}
