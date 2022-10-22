package com.av.cli;


import com.av.mongoDao.MongoAuthorRepository;
import com.av.mongoDao.MongoBookRepository;
import com.av.mongoDomain.MongoAuthor;
import com.av.mongoDomain.MongoBook;
import com.av.mongoDomain.MongoComment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
@Slf4j
public class MongoDefinerCommand {

    private final MongoBookRepository bookRepository;
    private final MongoAuthorRepository authorRepository;


    public MongoDefinerCommand(MongoBookRepository bookRepository, MongoAuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;

    }

    @ShellMethod("add author")
    public void add_author_mongo(String name) {
        authorRepository.save(new MongoAuthor(name));
    }


    @ShellMethod("show authors")
    public void show_authors_mongo() {
        authorRepository.findAll().forEach(a -> log.info(MessageFormat.format("author:{0}", a)));
    }

    @ShellMethod("update author name")
    public void update_author_name_mongo(String oldName, String newName) {
        authorRepository.findAuthorByName(oldName).ifPresent(
                mongoAuthor ->
                {
                    authorRepository.delete(mongoAuthor);
                    List<MongoBook> booksByAuthor = bookRepository.findBooksByAuthors(oldName);
                    mongoAuthor.setName(newName);
                    authorRepository.save(mongoAuthor);
                    booksByAuthor.stream().forEach(
                            mongoBook ->{
                                mongoBook.getAuthors().add(newName);
                                mongoBook.getAuthors().remove(oldName);
                                bookRepository.save(mongoBook);
                            }
                    );
                }
        );
    }

    @ShellMethod("create new book")
    public void add_book_mongo(String title, short edition, String isbn) {
        MongoBook newMongoBook = new MongoBook();
        newMongoBook.setTitle(title);
        newMongoBook.setEdition(edition);
        newMongoBook.setIsbn(isbn);
        bookRepository.save(newMongoBook);
    }

    @ShellMethod("show all books")
    public void show_books_mongo() {
        bookRepository.findAll().forEach(mongoBook -> log.info(MessageFormat.format("Book:{0}", mongoBook)));
    }




    @ShellMethod("add comment")
    public void add_comment_mongo(String bookTitle, String commentAuthor, String commentData) {
        bookRepository.findBooksByTitle(bookTitle).stream().findFirst().ifPresentOrElse(
                mongoBook -> {
                    var comment = new MongoComment();
                    comment.setText(commentData);
                    comment.setUserName(commentAuthor);
                    mongoBook.getMongoComments().add(comment);
                    bookRepository.save(mongoBook);
                },
                () -> log.error("You mast init book first")
        );
    }


    @ShellMethod("show comment")
    public void show_comment_mongo(String bookTitle) {
        bookRepository.findBooksByTitle(bookTitle).stream().findFirst().ifPresentOrElse(
                mongoBook -> log.info(MessageFormat.format("Book.comment:{0}", mongoBook.getMongoComments().stream().map(b -> b.getText()).collect(Collectors.joining(",")))),
                () -> log.error("You mast init book & comment")
        );
    }

    @ShellMethod("add genre")
    public void add_genre_mongo(String bookTitle, String genre) {
        bookRepository.findBooksByTitle(bookTitle).stream().findFirst().ifPresentOrElse(
                mongoBook -> {
                    mongoBook.getGenres().add(genre);
                    bookRepository.save(mongoBook);
                },
                () -> log.error("You mast init book first")
        );
    }


    @ShellMethod("show genre")
    public void show_genre_mongo(String bookTitle) {
        bookRepository.findBooksByTitle(bookTitle).stream().findFirst().ifPresentOrElse(
                mongoBook -> log.info(MessageFormat.format("Book.comment:{0}", mongoBook.getGenres().stream().collect(Collectors.joining(",")))),
                () -> log.error("You mast init book & comment")
        );
    }


    @ShellMethod("set author for  book")
    public void set_author_to_book_mongo(String bookTitle, String authorName) {
        bookRepository.findBooksByTitle(bookTitle).stream().findFirst().ifPresentOrElse(
                mongoBook -> {
                    authorRepository.findAuthorByName(authorName).ifPresentOrElse(
                            mongoAuthor ->
                            {
                                mongoAuthor.getMongoBooks().add(mongoBook);
                                authorRepository.save(mongoAuthor);
                                mongoBook.getAuthors().add(mongoAuthor.getName());
                                bookRepository.save(mongoBook);
                            },
                            () -> log.error("You mast init new author first")
                    );
                },
                () -> log.error("You mast init new book first")

        );
    }


    @ShellMethod("delete author for  book")
    public void delete_author_to_book_mongo(String bookTitle, String authorName) {
        bookRepository.findBooksByTitle(bookTitle).stream().findFirst().ifPresentOrElse(
                mongoBook -> {
                    authorRepository.findAuthorByName(authorName).ifPresentOrElse(
                            mongoAuthor ->
                            {
                                mongoAuthor.getMongoBooks().remove(mongoBook);
                                authorRepository.save(mongoAuthor);
                                mongoBook.getAuthors().remove(mongoAuthor);
                                bookRepository.save(mongoBook);
                            },
                            () -> log.error("You mast init new author first")
                    );
                },
                () -> log.error("You mast init new book first")

        );
    }

}
