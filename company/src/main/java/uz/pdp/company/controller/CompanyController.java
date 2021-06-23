package uz.pdp.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.company.payload.ApiResponse;
import uz.pdp.company.payload.CompanyDto;
import uz.pdp.company.service.CompanyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @GetMapping("/getAllCompany")
    public ResponseEntity<ApiResponse> getAllCompany(){
        ApiResponse allCompany = companyService.getAllCompany();
        return ResponseEntity.status(allCompany.isSuccess()? HttpStatus.OK:HttpStatus.NO_CONTENT).body(allCompany);
    }

    @GetMapping("/getCompanyById/{id}")
    public ResponseEntity<ApiResponse> getCompanyById(@PathVariable Integer id){
        ApiResponse companyById = companyService.getCompanyById(id);
        if (companyById.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(companyById);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(companyById);
        }
    }


    @DeleteMapping("/deleteCompany/{id}")
    public ResponseEntity<ApiResponse> deleteCompany(@PathVariable Integer id){
        ApiResponse apiResponse = companyService.deleteCompany(id);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
    }

    @PostMapping("/saveNewCompany")
    public ResponseEntity<ApiResponse> saveNewCompany(@Valid @RequestBody CompanyDto companyDto){
        ApiResponse apiResponse = companyService.saveNewCompany(companyDto);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        }
    }

    @PutMapping("/editCompany/{id}")
    public ResponseEntity<ApiResponse> editCompany(@PathVariable Integer id,@Valid @RequestBody CompanyDto companyDto){
        ApiResponse apiResponse = companyService.editCompany(id, companyDto);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
