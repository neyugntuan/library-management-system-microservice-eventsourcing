package com.neyugntuan.bookservice.query.projection;


import com.neyugntuan.bookservice.command.data.Book;
import com.neyugntuan.bookservice.command.data.BookRepository;
import com.neyugntuan.bookservice.query.model.BookResponseModel;
import com.neyugntuan.bookservice.query.queries.GetAllBookQuery;
import com.neyugntuan.bookservice.query.queries.GetBookDetailQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookProjection {

    @Autowired
    private BookRepository bookRepository;

    @QueryHandler
    public List<BookResponseModel> handle(GetAllBookQuery query){
        List<Book> list = bookRepository.findAll();
        List<BookResponseModel> listBookResponse = new ArrayList<>();

        list.forEach(book -> {
            BookResponseModel model = new BookResponseModel();
            BeanUtils.copyProperties(book, model);
            listBookResponse.add(model);
        });
        return listBookResponse;
    }

    @QueryHandler
    public BookResponseModel handle(GetBookDetailQuery query){
        BookResponseModel bookResponseModel = new BookResponseModel();

        Book book = bookRepository.findById(query.getId()).orElseThrow(() -> new RuntimeException("Not Found Book with Book id"+ query.getId()));

        BeanUtils.copyProperties(book, bookResponseModel);

        return bookResponseModel;
    }

}
