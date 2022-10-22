package com.av.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "authors")
@NamedQueries(
        {@NamedQuery(name = Author.FIND_ALL, query = "select a from Author a"),
                @NamedQuery(name = Author.FIND_BY_NAME, query = "select a from Author a where a.name = :name")

        }
)
@Data
public class Author {

    public static final String FIND_ALL = "Author.findAll";
    public static final String FIND_BY_NAME = "Author.byName";
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @ManyToMany(targetEntity = Book.class,fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "book_fk"))
    private List<Book> books=new ArrayList<>();

    public Author() {

    }

    public Author(String name) {

        this();
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Author{id=%d, name='%s'}", id, name);
    }


}
