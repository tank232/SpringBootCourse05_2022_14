package com.av.repository;

import com.av.db.Book;
import com.av.db.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    CommentRepository commentRepository;
    
    @Test
    void findAll() {
        var book = new Book();
        book.setTitle("t");
        book.setIsbn("t");
        var a = bookRepository.save(book);
        Comment comment=new Comment();
        comment.setBook(book);
        comment.setText("c");
        commentRepository.save(comment);
        assertNotNull(comment.getId(), "comment nor added");
        List<Comment> all = commentRepository.findAll();
        assertEquals(1, all.size(), "comment not fetched");
    }

    @Test
    void findCommentByBook() {
        var book = new Book();
        book.setTitle("t");
        book.setIsbn("t");
        var a = bookRepository.save(book);
        Comment comment=new Comment();
        comment.setBook(book);
        comment.setText("c");
        commentRepository.save(comment);
        assertNotNull(comment.getId(), "comment nor added");
        Optional<Comment> commentByBook = commentRepository.findCommentByBook(book);
        assertEquals(true, commentByBook.isPresent(), "comment not fetched");
    }

}