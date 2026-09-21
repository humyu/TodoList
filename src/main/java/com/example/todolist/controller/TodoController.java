package com.example.todolist.controller;

import com.example.todolist.DTO.UpdateStatusRequest;
import com.example.todolist.entity.Todo;
import com.example.todolist.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }


    @GetMapping
    public List<Todo> getAllTodos(){
        return todoService.findAll();
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo){
        return todoService.save(todo);
    }

    // 修改字段
    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo){
        Todo todo = todoService.findByid(id);
        if (updatedTodo.getText() != null){
            todo.setText(updatedTodo.getText());
        }
        todoService.save(todo);
        return ResponseEntity.ok(todo);
    }

    // 修改状态
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request){
        todoService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public void deleteTodo(@PathVariable Long id){
        todoService.deleteById(id);
    }
}
