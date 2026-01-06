//package com.rental.PropertyRentalApi.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDateTime;
//
//
//@Entity
//@Setter
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "reviews")
//public class ReviewEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private Integer rating;
//
//    @Column(columnDefinition = "TEXT")
//    private String comment;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "property_id", nullable = false)
//    private PropertyEntity property;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private UserEntity user;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//}
