package uz.pdp.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.company.payload.ApiResponse;
import uz.pdp.company.payload.WorkerDto;
import uz.pdp.company.service.WorkerService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    WorkerService workerService;

    @GetMapping("/getAllWorker")
    public ResponseEntity<ApiResponse> getAllWorker(){
        ApiResponse allWorker = workerService.getAllWorker();
        return ResponseEntity.status(200).body(allWorker);
    }


    @GetMapping("/getWorkerById/{id}")
    public ResponseEntity<ApiResponse> getWorkerById(@PathVariable Integer id){
        ApiResponse workerById = workerService.getWorkerById(id);
        if (workerById.isSuccess()){
            return ResponseEntity.status(200).body(workerById);
        }else {
            return ResponseEntity.status(404).body(workerById);
        }
    }

    @GetMapping("/getWorkersPageable/{companyId}")
    public ResponseEntity<ApiResponse> getWorkersPageable(@PathVariable Integer companyId, @RequestParam int pageNumber){
        ApiResponse workerPageable = workerService.getWorkerPageable(companyId, pageNumber);
        return ResponseEntity.status(200).body(workerPageable);
    }

    @DeleteMapping("/deleteWorker/{id}")
    public ResponseEntity<ApiResponse> deleteWorker(@PathVariable Integer id){
        ApiResponse apiResponse = workerService.deleteWorker(id);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(200).body(apiResponse);
        }else {
            return ResponseEntity.status(404).body(apiResponse);
        }
    }

    @PostMapping("/saveNewWorker")
    public ResponseEntity<ApiResponse> saveNewWorker(@Valid @RequestBody WorkerDto workerDto){
        ApiResponse apiResponse = workerService.saveNewWorker(workerDto);
        if (apiResponse.isSuccess()){
            return ResponseEntity.status(201).body(apiResponse);
        }else {
            return ResponseEntity.status(404).body(apiResponse);
        }
    }

    @PutMapping("/editWorker/{id}")
    public ResponseEntity<ApiResponse> editWorker(@PathVariable Integer id,@Valid @RequestBody WorkerDto workerDto){
        ApiResponse apiResponse = workerService.editWorker(id, workerDto);
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
