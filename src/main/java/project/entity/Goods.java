package project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goods")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_id")
    private Integer id;

    @Column(name = "goods_name")
    private String name;

    private Integer number;

    private String category;

    @JoinColumn(name = "entp_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Enterprise enterprise;
}
