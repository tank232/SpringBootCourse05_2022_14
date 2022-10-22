package com.av.mongoDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "Author")
public class MongoAuthor {
    @Id
    private String name;
    private List<MongoBook> mongoBooks = new ArrayList<>();


    public MongoAuthor(String name) {
        this();
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Author{name='%s'}", name);
    }


}
