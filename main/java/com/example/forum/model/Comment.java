package com.example.forum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString(exclude = "thread")
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    @JsonBackReference
    private Thread thread;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "inserted_by")
    private User insertedBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;


    public Comment(String content,
                   Thread thread,
                   User user,
                   User insertedBy) {
        this.content    = content;
        this.thread     = thread;
        this.user       = user;
        this.insertedBy = insertedBy;
    }
}
