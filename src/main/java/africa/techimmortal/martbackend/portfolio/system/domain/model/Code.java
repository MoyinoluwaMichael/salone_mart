package africa.techimmortal.martbackend.portfolio.system.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter

@Table(name = "code")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@ToString
public class Code implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "system_generated", nullable = false)
    private boolean systemGenerated;

    public Code(String name, boolean systemGenerated) {
        this.name = name;
        this.systemGenerated = systemGenerated;
    }
}
