package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name="name")
    private String name;
    @Column(nullable = false, name="email")
    private String email;
    @Column(nullable = false, name="password")
    private String password;

    @Column(nullable = false, name="active")
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }
    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Profile profile;

}
