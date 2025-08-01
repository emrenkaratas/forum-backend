// src/test/java/com/example/forum/service/ThreadServiceImplTest.java
package com.example.forum.service;
import com.example.forum.model.Thread;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.dto.ThreadResponse;
import com.example.forum.model.User;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThreadServiceImplTest {

    @Mock private ThreadRepository threadRepository;
    @Mock private UserRepository   userRepository;

    @InjectMocks
    private ThreadServiceImpl threadService;

    private User user;
    private User inserter;

    @BeforeEach
    void setUp() {
        // make sure your User class builder includes 'id'
        user = User.builder()
                .id(1L)
                .username("alice")
                .password("pw")
                .email("a@a.com")
                .name("Alice")
                .surname("A")
                .birthdate(LocalDate.of(2000,1,1))
                .build();

        inserter = User.builder()
                .id(2L)
                .username("bob")
                .password("pw2")     // builder requires all fields if using @AllArgsConstructor
                .email("b@b.com")
                .name("Bob")
                .surname("B")
                .birthdate(LocalDate.of(1999,12,31))
                .build();
    }

    @Test
    void createThread_success() {
        // given
        ThreadRequest req = new ThreadRequest();
        req.setTitle("Hello");
        req.setContent("World");
        req.setUserId(user.getId());
        req.setInsertedById(inserter.getId());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(inserter.getId())).thenReturn(Optional.of(inserter));
        when(threadRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        // when
        ThreadResponse resp = threadService.createThread(req);

        // then
        assertThat(resp.getTitle()).isEqualTo("Hello");
        assertThat(resp.getContent()).isEqualTo("World");
        assertThat(resp.getUserId()).isEqualTo(user.getId());
        verify(threadRepository).save(any());
    }

    @Test
    void createThread_userNotFound_throws() {
        ThreadRequest req = new ThreadRequest();
        req.setUserId(99L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> threadService.createThread(req))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getAllThreads_returnsList() {
        // prepare two threads with non-null user/insertedBy
        Thread t1 = new Thread();
        t1.setUser(user);
        t1.setInsertedBy(inserter);

        Thread t2 = new Thread();
        t2.setUser(user);
        t2.setInsertedBy(inserter);

        when(threadRepository.findAll()).thenReturn(List.of(t1, t2));

        List<ThreadResponse> all = threadService.getAllThreads();
        assertThat(all).hasSize(2);
    }


    @Test
    void getById_notFound_throws() {
        when(threadRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> threadService.getThreadById(5L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Thread not found");
    }

    @Test
    void deleteThread_notFound_throws() {
        when(threadRepository.existsById(7L)).thenReturn(false);

        assertThatThrownBy(() -> threadService.deleteThread(7L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Thread not found");
    }
}
