package com.example.todolist.DTO;

/**
 * 为了不暴露后端的字段，后端字段需要转化给前端使用
 */
public class TodoResponseDTO {
    private Long id;
    private String text;
    private Boolean isCompleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
