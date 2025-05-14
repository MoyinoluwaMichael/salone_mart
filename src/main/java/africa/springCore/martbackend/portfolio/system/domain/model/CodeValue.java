package africa.springCore.martbackend.portfolio.system.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Table(name = "code_value")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = "id")
public class CodeValue implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_id", nullable = false)
    private Long codeId;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "system_generated", nullable = false)
    private boolean systemGenerated;

    public CodeValue(String name, Code code) {
        this.name = name;
        this.codeId = code.getId();
        this.systemGenerated = code.isSystemGenerated();
    }
}
