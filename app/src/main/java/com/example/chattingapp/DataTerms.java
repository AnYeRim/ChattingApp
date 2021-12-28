package com.example.chattingapp;

public class DataTerms {
    private String title, contents;
    private boolean required, checked;

    public DataTerms(String title, String contents, boolean required, boolean checked) {
        this.title = title;
        this.contents = contents;
        this.checked = checked;
        this.required = required;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
