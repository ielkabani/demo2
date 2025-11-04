package com.example.demo.entities;
import lombok.*;
import jakarta.persistence.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="street", nullable = false)
    private String street;
    @Column(name="city", nullable = false)
    private String city;
    @Column(name="state", nullable = false)
    private String state;
    @Column(name="zip", nullable = false)
    private String zipCode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
