package com.example.slugbooks.slugbooks;

import java.util.List;

public class BookObject {

    String bookname;
    String author;
    String descriptionStr;
    String classStr;
    String edition;
    String condition;
    String price;

    List<String> imges ;

    public BookObject(){

    }

    public BookObject(String bookname, String author, String descriptionStr, String classStr, String edition, String condition, String price, List<String> imges ) {
        this.bookname = bookname;
        this.author = author;
        this.descriptionStr = descriptionStr;
        this.classStr = classStr;
        this.edition = edition;
        this.condition = condition;
        this.price = price;
        this.imges = imges;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescriptionStr() {
        return descriptionStr;
    }

    public void setDescriptionStr(String descriptionStr) {
        this.descriptionStr = descriptionStr;
    }

    public String getClassStr() {
        return classStr;
    }

    public void setClassStr(String classStr) {
        this.classStr = classStr;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getImges() {
        return imges;
    }

    public void setImges(List<String> imges) {
        this.imges = imges;
    }

}
