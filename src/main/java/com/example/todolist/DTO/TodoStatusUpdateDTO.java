package com.example.todolist.DTO;


/**
 * 将前端传入的布尔值 转化为 后端的布尔值
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
