package com.av.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"title", "edition"}, name = "book_title_edition_u1")})
@NamedQueries({
        @NamedQuery(name = Book.FIND_ALL, query = "select    b from Book b"),
        @NamedQuery(name = Book.FIND_BY_NAME, query = "select    b from Book b  where b.title = :title")
})
@Data
@NoArgsConstructor
public class Book {
    public static final String FIND_ALL = "Book.findAll";
    public static final String FIND_BY_NAME= "Book.byName";
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @ManyToMany(targetEntity = Author.class,fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "author_fk"))
    private List<Author> authors=new ArrayList<>();
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "isbn", length = 50, nullable = false)
    private String isbn;
    @Column(name = "edition", length = 1)
    private short edition = 1;

    @OneToMany(targetEntity = Comment.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private List<Comment> comments =new ArrayList<>();



    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", authors=" + authors +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", edition=" + edition +
                ", comments=" + comments +
                '}';
    }
}
