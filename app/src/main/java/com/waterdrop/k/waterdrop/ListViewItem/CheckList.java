package com.waterdrop.k.waterdrop.ListViewItem;

public class CheckList {
    private long id;
    private long checkListInventoryId;
    private String todo;
    private int isChecked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCheckListInventoryId() {
        return checkListInventoryId;
    }

    public void setCheckListInventoryId(long checkListInventoryId) {
        this.checkListInventoryId = checkListInventoryId;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }
}
