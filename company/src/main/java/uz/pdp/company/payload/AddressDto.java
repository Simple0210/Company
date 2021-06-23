package uz.pdp.company.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDto {
    @NotNull(message = "ko`cha bo`sh bo`lmasligi kerak")
    private String street;
    @NotNull(message = "homeNumber bo`sh bo`lmasligi kerak")
    private String homeNumber;
}
