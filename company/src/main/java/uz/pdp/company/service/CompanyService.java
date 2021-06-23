package uz.pdp.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.company.entity.Address;
import uz.pdp.company.entity.Company;
import uz.pdp.company.payload.ApiResponse;
import uz.pdp.company.payload.CompanyDto;
import uz.pdp.company.repository.AddressRepository;
import uz.pdp.company.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    AddressRepository addressRepository;

    public ApiResponse getAllCompany(){
        List<Company> companyList = companyRepository.findAll();
        if (!companyList.isEmpty()) {
            return new ApiResponse("Barcha Kompaniyalar ro`yxati", true, companyList);
        }else {
            return new ApiResponse("Xozircha bironta ham Kompaniya mavjud emas",false);
        }
    }

    public ApiResponse getCompanyById(Integer id){
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()){
            Company company = optionalCompany.get();
            return new ApiResponse("Siz so`ragan kompaniya ma`lumotlari quyidagilar:",true,company);
        }else {
            return new ApiResponse("Bunday kompaniya mavjud emas!",false);
        }
    }

    public ApiResponse deleteCompany(Integer id){
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()){
            companyRepository.deleteById(id);
            return new ApiResponse("OK! Kompaniya ma`lumotlari o`chirildi!",true);
        }else {
            return new ApiResponse("ERROR! Bunday kompaniya mavjud emas!",false);
        }
    }

    public ApiResponse saveNewCompany(CompanyDto companyDto){
        boolean byCorpName = companyRepository.existsByCorpName(companyDto.getCorpName());
        if (byCorpName){
            return new ApiResponse("ERROR! Bunday Kompaniya mavjud!",false);
        }else {
            Company company=new Company();
            company.setCorpName(companyDto.getCorpName());
            company.setDirectorName(companyDto.getDirectorName());
            Address address=new Address();
            address.setStreet(companyDto.getStreet());
            address.setHomeNumber(companyDto.getHomeNumber());
            Address savedAddress = addressRepository.save(address);
            company.setAddress(savedAddress);
            companyRepository.save(company);
            return new ApiResponse("OK! Kompaniya ma`lumotlari muvaffaqiyatli saqlandi!",true);
        }
    }


    public ApiResponse editCompany(Integer id, CompanyDto companyDto){
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()){
            boolean byCorpNameAndIdNot = companyRepository.existsByCorpNameAndIdNot(companyDto.getCorpName(), id);
            if (byCorpNameAndIdNot){
                return new ApiResponse("ERROR! Bunday nomdagi kompaniya mavjud!",false);
            }else {
                Company company = optionalCompany.get();
                company.setCorpName(company.getCorpName());
                company.setDirectorName(companyDto.getDirectorName());
                Address address = company.getAddress();
                address.setStreet(companyDto.getStreet());
                address.setHomeNumber(companyDto.getHomeNumber());
                Address savedAddress = addressRepository.save(address);
                company.setAddress(savedAddress);
                companyRepository.save(company);
                return new ApiResponse("OK! Kompaniya ma`lumotlari muvaffaqiyatli o`zgartirildi!",true);
            }
        }else {
            return new ApiResponse("ERROR! Bunday Kompaniya mavjud emas!",false);
        }
    }


}
