package com.av.repository;


import com.av.db.Book;
import com.av.db.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Override
    List<Comment> findAll();


    Optional<Comment> findCommentByBook(Book book);


}
