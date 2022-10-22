package com.av.config;

import com.av.db.Author;
import com.av.db.Book;
import com.av.db.Comment;
import com.av.mongoDomain.MongoAuthor;
import com.av.mongoDomain.MongoBook;
import com.av.mongoDomain.MongoComment;
import com.av.repository.AuthorRepository;
import com.av.repository.BookRepository;
import com.av.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    public final JobBuilderFactory jobBuilderFactory;
    public final StepBuilderFactory stepBuilderFactory;

    public final MongoTemplate mongoTemplate;
    public final BookRepository h2BookRepository;
    public final CommentRepository commentRepository;
    public final AuthorRepository authorRepository;


    @Bean
    public ItemReader<MongoBook> bookReader() {
        MongoItemReader<MongoBook> reader = new MongoItemReader<MongoBook>();
        reader.setTemplate(mongoTemplate);
        reader.setTargetType((Class<? extends MongoBook>) MongoBook.class);
        reader.setQuery("{}");
        Map<String, Sort.Direction> sorts = new HashMap<String, Sort.Direction>(1);
        sorts.put("title", Sort.Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }

    @Bean
    public ItemProcessor<MongoBook, Book> bookProcessor() {
        ItemProcessor<MongoBook, Book> processor = book -> {
            Book h2Book = new Book();
            h2Book.setTitle(book.getTitle());
            h2Book.setEdition(book.getEdition());
            h2Book.setIsbn(book.getIsbn());
            return h2Book;
        };
        return processor;
    }

    @Bean
    public ItemProcessor<MongoBook, List<Comment>> commentProcessor() {
        ItemProcessor<MongoBook, List<Comment>> processor = book -> {
            List<Comment> comments = book.getMongoComments().stream().map(
                    mongoComment -> {
                        Comment comment = new Comment();
                        comment.setText(mongoComment.getText());
                        comment.setUserName(mongoComment.getUserName());
                        comment.setBook(h2BookRepository.findBookByTitle(book.getTitle()).get());
                        return comment;
                    }
            ).collect(Collectors.toList());
            return comments;
        };
            return processor;
        }


    @Bean
    public ItemWriter<List<Comment>> commentWriter() {
        return comments -> {
            System.out.println("Saving Invoice Records: " + comments);
            comments.stream().forEach(
                    comment->{ commentRepository.saveAll(comment);}
            );
        };
    }

    @Bean
    public Step commentStep(ItemReader<MongoBook> bookReader, ItemProcessor<MongoBook, List<Comment>> commentProcessor, ItemWriter<List<Comment>> commentWriter) {
        return stepBuilderFactory.get("commentStep")
                .<MongoBook,  List<Comment>>chunk(6)
                .reader(bookReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .build();
    }

    @Bean
    public ItemWriter<Book> bookWriter() {
        return m2Book -> {
            System.out.println("Saving Invoice Records: " + m2Book);
            h2BookRepository.saveAll(m2Book);
        };
    }
    @Bean
    public Step bookStep(ItemReader<MongoBook> bookReader, ItemProcessor<MongoBook, Book> bookProcessor, ItemWriter<Book> bookWriter) {
        return stepBuilderFactory.get("MyStep")
                .<MongoBook, Book>chunk(6)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .build();
    }
    @Bean
    public ItemReader<MongoAuthor> authorReader() {
        MongoItemReader<MongoAuthor> reader = new MongoItemReader<MongoAuthor>();
        reader.setTemplate(mongoTemplate);
        reader.setTargetType((Class<? extends MongoAuthor>) MongoAuthor.class);
        reader.setQuery("{}");
        Map<String, Sort.Direction> sorts = new HashMap<String, Sort.Direction>(1);
        sorts.put("name", Sort.Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }

    @Bean
    public ItemProcessor<MongoAuthor, Author>authorProcessor() {
        ItemProcessor<MongoAuthor, Author> processor = mongoAuthor -> {
            Author author = new Author();
            author.setName(mongoAuthor.getName());
            List<Book> books = mongoAuthor.getMongoBooks().stream().map(mb ->
                    h2BookRepository.findBookByTitle(mb.getTitle())).map(book -> book.orElseGet(()->null)).filter(book -> book!=null).collect(Collectors.toList());
            author.setBooks(books);
            return author;
        };
        return processor;
    }

    @Bean
    public ItemWriter<Author> authorWriter() {
        return author -> {
            System.out.println("Saving Invoice Records: " + author);
            authorRepository.saveAll(author);
        };
    }



    @Bean
    public Step authorStep(ItemReader<MongoAuthor> authorReader, ItemProcessor<MongoAuthor, Author> authorProcessor, ItemWriter<Author> authorWriter) {
        return stepBuilderFactory.get("MyStep")
                .<MongoAuthor, Author>chunk(6)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .build();
    }


    @Bean
    public Job createJob(Step bookStep, Step authorStep,Step commentStep) {
        return jobBuilderFactory.get("MyJob")
                .incrementer(new RunIdIncrementer())
                .start(bookStep)
                .next(authorStep)
                .next(commentStep)
                .build();
    }


}
