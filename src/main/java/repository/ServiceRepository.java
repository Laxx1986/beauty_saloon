package repository;

import jakarta.transaction.Transactional;
import model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Service findByServiceName(String serviceName);
}
