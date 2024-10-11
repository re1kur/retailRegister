package project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table (name = "employees")
public class Employee implements Serializable {
    @Id
    private String email;

    private String firstname;

    private String lastname;

    @Enumerated(EnumType.STRING)
    private ApplicationRights position;

    @Column (name = "pin_code")
    private Integer pinCode;

    @JoinColumn(name = "entp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Enterprise enterprise;

}