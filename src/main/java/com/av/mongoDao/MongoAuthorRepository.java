package com.av.mongoDao;

import com.av.mongoDomain.MongoAuthor;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;

public interface MongoAuthorRepository extends MongoRepository<MongoAuthor, String> {

    List<MongoAuthor> findAll();

    Optional<MongoAuthor> findAuthorByName(String name);


}
