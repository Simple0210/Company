package uz.pdp.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.company.payload.AddressDto;
import uz.pdp.company.payload.ApiResponse;
import uz.pdp.company.service.AddressService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping("/getAllAddress")
    public ResponseEntity<ApiResponse> getAllAddress(){
        ApiResponse allAddress = addressService.getAllAddress();
        return ResponseEntity.status(200).body(allAddress);
    }

    @GetMapping("/getAddressById/{id}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Integer id){
        ApiResponse addressById = addressService.getAddressById(id);
        if (addressById.isSuccess()){
            return ResponseEntity.status(200).body(addressById);
        }else {
            return ResponseEntity.status(404).body(addressById);
        }
    }

    @DeleteMapping("/deleteAddress/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Integer id){
        ApiResponse apiResponse = addressService.deleteAddress(id);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(200).body(apiResponse);
        }else {
            return ResponseEntity.status(404).body(apiResponse);
        }
    }

    @PostMapping("/saveNewAddress")
    public ResponseEntity<ApiResponse> saveNewAddress(@Valid @RequestBody AddressDto addressDto){
        ApiResponse apiResponse = addressService.saveNewAddress(addressDto);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @PutMapping("/editAddress/{id}")
    public ResponseEntity<ApiResponse> editAddress(@PathVariable Integer id, @Valid @RequestBody AddressDto addressDto){
        ApiResponse apiResponse = addressService.editAddress(id, addressDto);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(201).body(apiResponse);
        }else {
            return ResponseEntity.status(404).body(apiResponse);
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
