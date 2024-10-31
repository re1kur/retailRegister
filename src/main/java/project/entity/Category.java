package project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (name = "categories")
@ToString
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @JoinColumn(name = "entp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Enterprise enterprise;

    @OneToMany
    @ToString.Exclude
    private Collection<Goods> goods;

    public String toString() {
        return name;
    }

//    public Integer getNumber() {
//        return goods.size();
//    }
}
