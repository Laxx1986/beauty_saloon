package repository;

import jakarta.transaction.Transactional;
import model.ServiceLength;
import model.ServiceProvider;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@Transactional
@Repository
public interface ServiceLengthRepository extends JpaRepository<ServiceLength, Long> {
    ServiceLength findByServiceLengthId(Long serviceLengthId);
}
