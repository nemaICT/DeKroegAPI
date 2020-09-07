package dekroeg.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Member {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotEmpty(message = "Firstname can't be empty")
    private String firstname;
    @NotNull
    @NotEmpty(message = "Lastname can't be empty")
    private String lastname;
    @NotNull
    @NotEmpty(message = "Email can't be empty")
    private String email;
    //@NotNull
    private Date birthday;

    //@Embedded
    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private Address address;

}
