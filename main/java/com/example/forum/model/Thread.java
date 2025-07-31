package com.example.forum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "threads")
@Getter
@Setter
@ToString(exclude = "comments")
@NoArgsConstructor
public class Thread extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int viewCount = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "inserted_by")
    private User insertedBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    public Thread(String title,
                  String content,
                  User user,
                  User insertedBy,
                  User updatedBy) {
        this.title      = title;
        this.content    = content;
        this.user       = user;
        this.insertedBy = insertedBy;
        this.updatedBy  = updatedBy;
    }


    public void incrementViewCount() {
        this.viewCount++;
    }
}
