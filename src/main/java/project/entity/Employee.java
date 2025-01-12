package project.entity;

import jakarta.persistence.*;
import lombok.*;
import project.other.ApplicationRights;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "employees")
public class Employee implements Serializable {
    @Id
    private String email;

    private String firstname;

    private String lastname;

    private String position;

    @Enumerated(EnumType.STRING)
    private ApplicationRights rights;

    private String password;

    @JoinColumn(name = "entp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Enterprise enterprise;

}