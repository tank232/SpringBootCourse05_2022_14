package com.av.mongoDomain;

import lombok.Data;


@Data
public class MongoComment {

    private String userName;
    private String text;



    @Override
    public String toString() {
        return "Comment{" +
                ", userName='" + userName + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
