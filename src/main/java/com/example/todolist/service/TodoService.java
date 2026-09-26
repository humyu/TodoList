package com.example.todolist.service;

import com.example.todolist.DTO.TodoResponseDTO;
import com.example.todolist.entity.Todo;
import com.example.todolist.entity.TodoStatus;
import com.example.todolist.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public Todo findById(Long id){
        return todoRepository.findById(id).orElseThrow(() -> new RuntimeException("不存在"));
    }

    public Todo save(Todo todo){
        return todoRepository.save(todo);
    }

    /**
     * 逻辑删除
     * @param id 被逻辑删除的事项的 id
     */
    public void deleteById(Long id){
        Todo todo = todoRepository.getById(id);
        todo.setStatus(TodoStatus.DELETED);
        todoRepository.save(todo);
    }

    public List<TodoResponseDTO> getTodoList(){
        // 先查询没有被逻辑删除的 todo
        List<Todo> todos = todoRepository.findByStatusNot(TodoStatus.DELETED);

        return todos.stream().map(todo -> {
            TodoResponseDTO dto = new TodoResponseDTO();
            dto.setId(todo.getId());
            dto.setText(todo.getText());
            dto.setFinished(todo.getStatus() == TodoStatus.COMPLETED);
            return dto;
        }).collect(Collectors.toList());
    }
}
