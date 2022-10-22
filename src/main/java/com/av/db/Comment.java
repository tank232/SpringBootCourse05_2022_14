package com.av.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column(name = "user_name", length = 50)
    private String userName;
    @Column(name = "comment_data", length = 1000, nullable = false)
    private String text;

    @ManyToOne(targetEntity = Book.class,fetch = FetchType.LAZY)
    private Book book;


    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
