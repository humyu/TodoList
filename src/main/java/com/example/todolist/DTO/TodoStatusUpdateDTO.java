package com.example.todolist.DTO;

import com.example.todolist.entity.TodoStatus;

/**
 * 将前端传入的值转化为后端
 */
public class TodoStatusUpdateDTO{

    private Boolean isCompleted;

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
