package com.example.todolist.controller;

import com.example.todolist.DTO.TodoResponseDTO;
import com.example.todolist.DTO.TodoStatusUpdateDTO;
import com.example.todolist.entity.Todo;
import com.example.todolist.entity.TodoStatus;
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

    /**
     * 获取所有没有被逻辑删除的todo
     * @return 没有被逻辑删除的todo
     */
    @GetMapping
    public List<TodoResponseDTO> getAllTodos(){
        return todoService.getTodoList();
    }

    /**
     * 创建新事项
     * @param todo 接收前端的todo
     * @return 保存的新事项
     */
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo){
        return todoService.save(todo);
    }

    // 修改字段
    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo){

        Todo todo = todoService.findById(id);
        if (updatedTodo.getText() != null){
            todo.setText(updatedTodo.getText());
        }
        todoService.save(todo);
        return ResponseEntity.ok(todo);
    }

    // 修改状态
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody TodoStatusUpdateDTO requestData){
        // 获取前端传来的布尔值
        Boolean finished = requestData.getFinished();
        // 前端的布尔值翻译为后端对应的枚举
        TodoStatus targetStatus = finished ? TodoStatus.COMPLETED : TodoStatus.PENDING;
        todoService.updateStatus(id, targetStatus);
        return ResponseEntity.ok().build();
    }

    /**
     * 逻辑删除
     * @param id 被逻辑删除的事项的 id
     */
    @DeleteMapping("{id}")
    public void deleteTodo(@PathVariable Long id){
        todoService.deleteById(id);
    }
}
