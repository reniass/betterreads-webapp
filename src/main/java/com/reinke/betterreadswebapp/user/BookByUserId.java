package com.reinke.betterreadswebapp.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDate;
import java.util.List;

@Table
public class BookByUserId {

    @Id
    @PrimaryKeyColumn(name = "book_by_user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Indexed(value = "user_id")
    private String userId;

    @Column("book_id")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String bookId;

    @Column("title")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String title;

    @Column("cover_id")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String coverId;

    @Column("start_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate startDate;
    @Column("end_date")
    @CassandraType(type = CassandraType.Name.DATE)
    private LocalDate endDate;
    @Column("reading_status")
    @CassandraType(type = CassandraType.Name.TEXT)
    private  String readingStatus;
    @Column("rating")
    @CassandraType(type = CassandraType.Name.INT)
    private int rating;
    @Column("authors_name")
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> authorsName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<String> getAuthorsName() {
        return authorsName;
    }

    public void setAuthorsName(List<String> authorsName) {
        this.authorsName = authorsName;
    }
}
