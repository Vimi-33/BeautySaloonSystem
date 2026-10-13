package com.WD09.BeautySaloonSystem.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "feedbacks")
public class Feedback extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int rating;

    // Default Constructor
    public Feedback() {
        super();
    }

    // Overloaded Constructor (Polymorphism / Compile-time flexibility)
    public Feedback(User user, String comment, int rating) {
        super();
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }

    // Getters and Setters (Encapsulation)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Method Overriding (Polymorphism)
    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", user=" + (user != null ? user.getFullName() : "Anonymous") +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}