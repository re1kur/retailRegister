package project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Collection;

@Data
@Entity
@Table(name = "units_measure")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class MeasureUnit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String symbol;

    @ManyToOne
    @JoinColumn(name = "entp_id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "measureUnit")
    @ToString.Exclude
    private Collection<Goods> goods;

    public String toString() {
        return symbol + "|" + name;
    }
}
