package com.example.todolist.DTO;

import com.example.todolist.entity.TodoStatus;

public class UpdateStatusRequest {

    private TodoStatus status;

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }
}
