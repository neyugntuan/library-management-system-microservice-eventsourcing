package com.neyugntuan.borrowingservice.command.saga;



import com.neyugntuan.borrowingservice.command.command.DeleteBorrowingCommand;
import com.neyugntuan.borrowingservice.command.event.BorrowingCreatedEvent;
import com.neyugntuan.commonservice.command.UpdateStatusBookCommand;
import com.neyugntuan.commonservice.event.BookUpdateStatusEvent;
import com.neyugntuan.commonservice.model.BookResponseCommonModel;
import com.neyugntuan.commonservice.queries.GetBookDetailQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class BorrowingSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    private void handle(BorrowingCreatedEvent event){
        log.info("BorrowingCreatedEvent in saga for BookId: "+ event.getBookId() + " : EmployeeId: "+event.getEmployeeId());
        try{
            GetBookDetailQuery getBookDetailQuery = new GetBookDetailQuery(event.getBookId());
            BookResponseCommonModel bookResponseCommonModel = queryGateway.query(getBookDetailQuery,
                    ResponseTypes.instanceOf(BookResponseCommonModel.class)).join();
            if(!bookResponseCommonModel.getIsReady()){
                throw new Exception("Sach da co nguoi muon");
            }else{
                SagaLifecycle.associateWith("bookId", event.getBookId());
                UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), false, event.getEmployeeId(), event.getBookId());
                commandGateway.sendAndWait(command);
            }
        }catch (Exception ex){
            rollbackBorrowingRecord(event.getId());
            log.error(ex.getMessage());
        }
    }

    @SagaEventHandler(associationProperty = "bookId")
    private void handler(BookUpdateStatusEvent event){
        log.info("BorrowingCreatedEvent in saga for BookId: "+ event.getBookId());
        SagaLifecycle.end();
    }

    private void rollbackBorrowingRecord(String id){
        DeleteBorrowingCommand command = new DeleteBorrowingCommand(id);
        commandGateway.sendAndWait(command);
        SagaLifecycle.end();
    }


}

