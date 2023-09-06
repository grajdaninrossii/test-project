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
@Table(name = "houses",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "address")
})
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    // адрес
    @NotEmpty(message = "Address should not be empty")
    @Size(min = 2, max = 55, message = "Name should be between 2 and 55 characters")
    @Column(nullable = false)
    private String address;

    // id хозяина(пользователя)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User host;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "residents",
            joinColumns = @JoinColumn(name = "house_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> residents = new HashSet<>();


    public House(String address, User host){
        this.address = address;
        this.host = host;
//        this.residents = residents;
    }

    public House(String address, User host, Set<User> residents){
        this.address = address;
        this.host = host;
        this.residents = residents;
    }


    public Boolean addResidents(User resident){
        return residents.add(resident);
    }

    public Boolean deleteResidents(User resident) {
        return residents.remove(resident);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        House house = (House) o;
        return id != null && Objects.equals(id, house.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
