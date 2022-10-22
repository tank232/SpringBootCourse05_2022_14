package com.av.repository;

import com.av.db.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
@ActiveProfiles("test")
class MongoAuthorRepositoryTest {

    @Autowired
    AuthorRepository authorRepository;

    @Test
    void findAll() {
        var author = new Author("fake2");
        var a = authorRepository.save(author);
        assertNotNull(a.getId(), "author nor added");
        var authorList = authorRepository.findAll();
        assertEquals(1, authorList.size(), "author not fetched");
    }

    @Test
    void findAuthorByName() {
        var author = new Author("fake2");
        var a = authorRepository.save(author);
        assertNotNull(a.getId(), "author nor added");
        var author1 = authorRepository.findAuthorByName("fake2");
        assertEquals(true, author1.isPresent(), "author not fetched");
    }


}