package com.av.repository;


import com.av.db.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Test
    void findAll() {
        var book = new Book();
        book.setTitle("t");
        book.setIsbn("t");
        var a = bookRepository.save(book);
        assertNotNull(a.getId(), "book nor added");
        List<Book> all = bookRepository.findAll();
        assertEquals(1, all.size(), "book not fetched");
    }

    @Test
    void findBookByTitle() {
        var book = new Book();
        book.setTitle("t");
        book.setIsbn("t");
        var a = bookRepository.save(book);
        assertNotNull(a.getId(), "book nor added");
        var book1 = bookRepository.findBookByTitle("t");
        assertEquals(true, book1.isPresent(), "book not fetched");
    }

}