package com.neyugntuan.employeeservice.command.event;


import com.neyugntuan.employeeservice.command.data.Employee;
import com.neyugntuan.employeeservice.command.data.EmployeeRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.DisallowReplay;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class EmployeeEventHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    @EventHandler
    public void on(EmployeeCreatedEvent event){
        Employee employee = new Employee();
        BeanUtils.copyProperties(event, employee);
        employeeRepository.save(employee);
    }

    @EventHandler
    public void on(EmployeeUpdatedEvent event) throws Exception{
        Optional<Employee> oldEmployee= employeeRepository.findById(event.getId());

        Employee employee = oldEmployee.orElseThrow(() -> new Exception("Employee Not Found"));
        employee.setFirstName(event.getFirstName());
        employee.setLastName(event.getLastName());
        employee.setKin(event.getKin());
        employee.setIsDisciplined(event.getIsDisciplined());
        employeeRepository.save(employee);
    }

    @EventHandler
    @DisallowReplay //Tránh cho event tự gọi sau khi start service(thất bại ở lần gọi trước)
    public void on(EmployeeDeletedEvent event) throws Exception{
        try{
            employeeRepository.findById(event.getId()).orElseThrow(() -> new Exception("Employee Not Found"));
            employeeRepository.deleteById(event.getId());
        }catch (Exception ex){
            log.error(ex.getMessage());
        }

    }
}
