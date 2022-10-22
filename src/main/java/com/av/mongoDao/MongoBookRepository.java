package com.av.mongoDao;

import com.av.mongoDomain.MongoBook;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoBookRepository extends MongoRepository<MongoBook, String> {

    List<MongoBook> findAll();


    List<MongoBook> findBooksByTitle(String title);

    List<MongoBook> findBooksByAuthors(String author);


}
