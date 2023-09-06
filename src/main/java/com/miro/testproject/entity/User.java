package com.miro.testproject.entity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
       uniqueConstraints = {
        @UniqueConstraint(columnNames = "username")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // username имя пользователя
    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 35, message = "Name should be between 2 and 35 characters")
    @Column(nullable = false)
    private String username;

    // Возраст пользователя
    @Digits(integer = 3, fraction = 0)
    @Column(nullable = false)
    private Integer age;

    // Хэшированный пароль пользователя
    @NotEmpty(message = "Password should not be empty")
    @Column(name = "hash_password", nullable = false)
    private String hashPassword;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, Integer age, String password){
        this.username = username;
        this.age = age;
        this.hashPassword = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
