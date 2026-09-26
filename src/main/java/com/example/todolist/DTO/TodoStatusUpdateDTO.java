package com.example.todolist.DTO;


/**
 * 将前端传入的布尔值 转化为 后端的布尔值
 */
public class TodoStatusUpdateDTO{

    private Boolean finished;

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
