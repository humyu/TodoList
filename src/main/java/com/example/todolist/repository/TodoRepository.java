package com.example.todolist.repository;

import com.example.todolist.entity.Todo;
import com.example.todolist.entity.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByStatusNot(TodoStatus status);

}
