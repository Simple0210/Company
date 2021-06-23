package uz.pdp.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.company.entity.Company;
import uz.pdp.company.entity.Department;
import uz.pdp.company.payload.ApiResponse;
import uz.pdp.company.payload.DepartmentDto;
import uz.pdp.company.repository.CompanyRepository;
import uz.pdp.company.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    CompanyRepository companyRepository;


    public ApiResponse getAllDepartment(){
        List<Department> departmentList = departmentRepository.findAll();
       return new ApiResponse("Departmentlar ro`yhati:",true,departmentList);
    }

    public ApiResponse getDepartmentById(Integer id){
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()){
            Department department = optionalDepartment.get();
            return new ApiResponse("Siz tanlagan department ma`lumotlari:",true,department);
        }else {
            return new ApiResponse("Siz tanlagan department mavjud emas",false);
        }
    }

    public ApiResponse getDepartmentPageable(Integer companyId,int pageNumber){
        Pageable pageable= PageRequest.of(pageNumber,10);
        Page<Department> byCompanyId = departmentRepository.findDepartmentsByCompanyId(companyId,pageable);
        return new ApiResponse("OK!",true,byCompanyId);
    }

    public ApiResponse deleteDepartment(Integer id){
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
        if (optionalDepartment.isPresent()){
            departmentRepository.deleteById(id);
            return new ApiResponse("Department ma`lumotlari o`chirildi",true);
        }else {
            return new ApiResponse("Siz tanlagan department mavjud emas",false);
        }
    }

    public ApiResponse saveNewDepartment(DepartmentDto departmentDto){
        boolean nameAndCompanyId = departmentRepository.existsByNameAndCompanyId(departmentDto.getName(), departmentDto.getCompanyId());
        if (nameAndCompanyId){
            return new ApiResponse("Bunday department mavjud",false);
        }else {
            Optional<Company> optionalCompany = companyRepository.findById(departmentDto.getCompanyId());
            if (optionalCompany.isPresent()){
                Company company = optionalCompany.get();
                Department department=new Department();
                department.setName(departmentDto.getName());
                department.setCompany(company);
                departmentRepository.save(department);
                return new ApiResponse("Department ma`lumotlari muvaffaqiyatli saqlandi!",true);
            }else {
                return new ApiResponse("Kompaniya mavjud emas",false);
            }
        }
    }

    public ApiResponse editDepartment(Integer id,DepartmentDto departmentDto){
        boolean nameAndCompanyIdAndIdNot = departmentRepository.existsByNameAndCompanyIdAndIdNot(departmentDto.getName(), departmentDto.getCompanyId(), id);
        if (nameAndCompanyIdAndIdNot){
            return new ApiResponse("Bunday department mavjud",false);
        }else {
            Optional<Department> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isPresent()){
                Optional<Company> optionalCompany = companyRepository.findById(departmentDto.getCompanyId());
                if (optionalCompany.isPresent()){
                    Company company = optionalCompany.get();
                    Department department = optionalDepartment.get();
                    department.setName(departmentDto.getName());
                    department.setCompany(company);
                    departmentRepository.save(department);
                    return new ApiResponse("Department ma`lumotlari o`zgartirildi",true);
                }else {
                    return new ApiResponse("Kompaniya xato kiritildi",false);
                }
            }else {
                return new ApiResponse("Department xato kiritildi",false);
            }
        }
    }



}
