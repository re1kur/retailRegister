package project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table (name="support_reports")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String subject;

    private String content;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;
}
