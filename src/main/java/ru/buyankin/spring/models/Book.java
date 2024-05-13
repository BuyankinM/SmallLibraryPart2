package ru.buyankin.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Title should not be empty!")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author should not be empty!")
    @Column(name = "author")
    private String author;

    @Min(value = 1, message = "Year should be greater than 0")
    @Column(name = "year")
    private int year;

    @Column(name = "issue_date")
    private Date issueDate;

    @Transient
    private boolean isOverdue;

    @ManyToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "id")
    private Reader owner;

    public Book(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Reader getOwner() {
        return owner;
    }

    public void setOwner(Reader owner) {
        this.owner = owner;
    }

    public boolean isOverdue() {
        return isOverdue;
    }
}
