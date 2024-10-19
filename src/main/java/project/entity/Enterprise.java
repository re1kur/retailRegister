package project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name= "enterprises")
public class Enterprise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String address;

    private String type;

    private String password;

    @Column (name = "corp_email")
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy = "enterprise")
    private Collection<Employee> employees;

    @ToString.Exclude
    @OneToMany(mappedBy = "enterprise")
    private Collection<Product> products;

}