package com.neyugntuan.borrowingservice.command.event;

import com.neyugntuan.borrowingservice.command.data.Borrowing;
import com.neyugntuan.borrowingservice.command.data.BorrowingRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BorrowingEventsHandler {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @EventHandler
    public void on(BorrowingCreatedEvent event){
        Borrowing model = new Borrowing();
        model.setId(event.getId());
        model.setBookId(event.getBookId());
        model.setEmployeeId(event.getEmployeeId());
        model.setBorrowingDate(event.getBorrowingDate());
        borrowingRepository.save(model);
    }

    @EventHandler
    public void on(BorrowingDeletedEvent event){
        Optional<Borrowing> oldEntity = borrowingRepository.findById(event.getId());
        oldEntity.ifPresent(borrowing -> borrowingRepository.delete(borrowing));
    }
}
