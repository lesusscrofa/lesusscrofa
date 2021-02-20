package be.susscrofa.api.repository;

import be.susscrofa.api.model.Remark;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RemarkRepository extends CrudRepository<Remark, Long> {

    Optional<Remark> findByClientIdAndDay(Long clientId, LocalDate day);

    void deleteByClientIdAndDay(Long clientId, LocalDate day);
}
