package uz.pdp.company.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CompanyDto {


    @NotNull(message = "corpName bo`sh bo`lmasligi kerak")
    private String corpName;
    @NotNull(message = "directorName bo`sh bo`lmasligi kerak")
    private String directorName;
    @NotNull(message = "street bo`sh bo`lmasligi kerak")
    private String street;
    @NotNull(message = "homeNumber bo`sh bo`lmasligi kerak")
    private String homeNumber;
}
