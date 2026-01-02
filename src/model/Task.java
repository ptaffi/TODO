package model;

import java.sql.Date;

public class Task {
    private int id;
    private String title;
    private String note;
    private boolean completed;
    private Date dueDate;  //có thể null
    private int listId;

    public Task() {}

    public Task(int id, String title, String note, boolean completed, Date dueDate, int listId) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.completed = completed;
        this.dueDate = dueDate;
        this.listId = listId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public int getListId() { return listId; }
    public void setListId(int listId) { this.listId = listId; }

    @Override
    public String toString() {
        return (completed ? "✅ " : "⬜ ") + title;
    }
}
