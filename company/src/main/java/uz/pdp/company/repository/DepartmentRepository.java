package uz.pdp.company.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.company.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department,Integer> {

    boolean existsByNameAndCompanyId(String name, Integer company_id);

    boolean existsByNameAndCompanyIdAndIdNot(String name, Integer company_id, Integer id);

    Page<Department> findDepartmentsByCompanyId(Integer company_id, Pageable pageable);
}
