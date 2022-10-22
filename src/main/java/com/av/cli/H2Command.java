package com.av.cli;

import com.av.repository.AuthorRepository;
import com.av.repository.BookRepository;
import com.av.repository.CommentRepository;
import com.av.db.Author;
import com.av.db.Book;
import com.av.db.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@ShellComponent
@Slf4j
public class H2Command {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CommentRepository commentRepository;



    public H2Command(BookRepository bookRepository, AuthorRepository authorRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.commentRepository = commentRepository;

    }

    @ShellMethod("add author")
    public void add_author(String name) {
        authorRepository.save(new Author(name));
    }

    @ShellMethod("show authors")
    public void show_authors() {
        authorRepository.findAll().forEach(a -> log.info(MessageFormat.format("author:{0}", a.getName())));
    }


    @ShellMethod("create new book")
    public void add_book(String title, short edition, String isbn) {
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setEdition(edition);
        newBook.setIsbn(isbn);
        bookRepository.save(newBook);
    }

    @ShellMethod("show all books")
    public void show_books() {
        bookRepository.findAll().forEach(book -> log.info(MessageFormat.format("Book:{0}", book.getTitle())));
    }

    @ShellMethod("add comment")
    public void add_comment(String bookTitle, String commentAuthor, String commentData) {
        bookRepository.findBookByTitle(bookTitle).ifPresentOrElse(
                book -> {
                    var comment = new Comment();
                    comment.setText(commentData);
                    comment.setUserName(commentAuthor);
                    comment.setBook(book);
                    commentRepository.save(comment);
                },
                () -> log.error("You mast init book first")
        );
    }


    @ShellMethod("show comment")
    public void show_comment(String bookTitle) {
        bookRepository.findBookByTitle(bookTitle).ifPresentOrElse(
                book -> log.info(MessageFormat.format("Book.comment:{0}", book.getComments().stream().map(b -> b.getText()).collect(Collectors.joining(",")))),
                () -> log.error("You mast init book & comment")
        );
    }


    @ShellMethod("set author for new book")
    public void set_author_to_book(String bookTitle, String authorName) {
        bookRepository.findBookByTitle(bookTitle).ifPresentOrElse(
                book -> {
                    authorRepository.findAuthorByName(authorName).ifPresentOrElse(
                            author ->
                            {
                                author.getBooks().add(book);
                                authorRepository.save(author);
                            },
                            () -> log.error("You mast init new author first")
                    );
                },
                () -> log.error("You mast init new book first")

        );
    }


}
