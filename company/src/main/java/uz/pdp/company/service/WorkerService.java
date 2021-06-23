package uz.pdp.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.company.entity.Address;
import uz.pdp.company.entity.Department;
import uz.pdp.company.entity.Worker;
import uz.pdp.company.payload.ApiResponse;
import uz.pdp.company.payload.WorkerDto;
import uz.pdp.company.repository.AddressRepository;
import uz.pdp.company.repository.DepartmentRepository;
import uz.pdp.company.repository.WorkerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    WorkerRepository workerRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    public ApiResponse getAllWorker(){
        List<Worker> workerList = workerRepository.findAll();
        return new ApiResponse("Workerlar listi:",true,workerList);
    }

    public ApiResponse getWorkerById(Integer id){
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()){
            Worker worker = optionalWorker.get();
            return new ApiResponse("Worker ma`lumotlari:",true,worker);
        }else {
            return new ApiResponse("Bunday worker mavjud emas.",false);
        }
    }

    public ApiResponse getWorkerPageable(Integer companyId,int pageNumber){
        Pageable pageable= PageRequest.of(pageNumber,10);
        Page<Worker> workersByDepartment_companyId = workerRepository.findWorkersByDepartment_CompanyId(companyId, pageable);
        return new ApiResponse("Workerlar ro`yxati:",true,workersByDepartment_companyId);
    }

    public ApiResponse deleteWorker(Integer id){
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()){
            workerRepository.deleteById(id);
            return new ApiResponse("Worker ma`lumotlari o`chirildi",true);
        }else {
            return new ApiResponse("Bunday worker mavjud emas.",false);
        }
    }

    public ApiResponse saveNewWorker(WorkerDto workerDto){
        boolean existsByPhoneNumber = workerRepository.existsByPhoneNumber(workerDto.getPhoneNumber());
        if (existsByPhoneNumber){
            return new ApiResponse("Bunday telefon raqamli worker mavjud",false);
        }else {
            Optional<Department> optionalDepartment = departmentRepository.findById(workerDto.getDepartmentId());
            if (optionalDepartment.isPresent()){
                Department department = optionalDepartment.get();
                Worker worker=new Worker();
                worker.setName(workerDto.getName());
                worker.setPhoneNumber(workerDto.getPhoneNumber());
                Address address=new Address();
                address.setStreet(workerDto.getStreet());
                address.setHomeNumber(workerDto.getHomeNumber());
                Address savedAddress = addressRepository.save(address);
                worker.setAddress(savedAddress);
                worker.setDepartment(department);
                workerRepository.save(worker);
                return new ApiResponse("Worker ma`lumotlari saqlandi",true);
            }else {
                return new ApiResponse("Department xato kiritildi",false);
            }
        }
    }

    public ApiResponse editWorker(Integer id,WorkerDto workerDto){
        Optional<Worker> optionalWorker = workerRepository.findById(id);
        if (optionalWorker.isPresent()){
            boolean existsByPhoneNumberAndIdNot = workerRepository.existsByPhoneNumberAndIdNot(workerDto.getPhoneNumber(), workerDto.getDepartmentId());
            if (existsByPhoneNumberAndIdNot){
                return new ApiResponse("Bunday telefon raqamli worker mavjud",false);
            }else {
                Optional<Department> optionalDepartment = departmentRepository.findById(workerDto.getDepartmentId());
                if (optionalDepartment.isPresent()){
                    Department department = optionalDepartment.get();
                    Worker worker = optionalWorker.get();
                    worker.setName(workerDto.getName());
                    worker.setPhoneNumber(workerDto.getPhoneNumber());
                    Address address = worker.getAddress();
                    address.setStreet(workerDto.getStreet());
                    address.setHomeNumber(workerDto.getHomeNumber());
                    Address savedAddress = addressRepository.save(address);
                    worker.setAddress(savedAddress);
                    worker.setDepartment(department);
                    workerRepository.save(worker);
                    return new ApiResponse("Worker ma`lumotlari o`zgartirildi",true);
                }else {
                    return new ApiResponse("Department id xato kiritildi",false);
                }
            }
        }else {
            return new ApiResponse("Bunday idli worker mavjud emas!",false);
        }



    }
}
