package repository;

import jakarta.transaction.Transactional;
import model.OpeningTime;
import model.ServiceLength;
import model.ServiceProvider;
import model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
@Transactional
@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long> {
    OpeningTime findByOpeningTimeId(Long openingTimeId);
}
