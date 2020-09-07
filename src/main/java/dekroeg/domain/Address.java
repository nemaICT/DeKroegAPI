package dekroeg.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Builder
//@Embeddable
public class Address {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty(message = "street can't be empty")
    private String streetName;
    @NotNull
    @NotEmpty(message = "zip code can't be empty")
    private String zipCode;
    @NotNull
    @NotEmpty(message = "street can't be empty")
    private String cityName;
    @NotNull
    @NotEmpty(message = "state can't be empty")
    private String state;
    @NotNull
    @NotEmpty(message = "country can't be empty")
    private String country;
}
