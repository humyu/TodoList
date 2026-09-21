package com.example.todolist.service;

import com.example.todolist.entity.Todo;
import com.example.todolist.entity.TodoStatus;
import com.example.todolist.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public void updateStatus(Long id, TodoStatus newStatus){
        // 查出当前任务
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("任务不存在"));

        // 状态校验，如果前后台状态一致，直接忽略
        if (todo.getStatus() == newStatus) {
            return;
        }

        // 修改状态
        todo.setStatus(newStatus);
        todoRepository.save(todo);
    }

    public List<Todo> findAll(){
        return todoRepository.findAll();
    }

    public Todo findByid(Long id){
        return todoRepository.findById(id).orElseThrow(() -> new RuntimeException("不存在"));
    }

    public Todo save(Todo todo){
        return todoRepository.save(todo);
    }

    public void deleteById(Long id){
        todoRepository.deleteById(id);
    }
}
