package school.hei.asa.repository.jrepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.hei.asa.repository.model.JContract;
import school.hei.asa.repository.model.JWorker;

@Repository
public interface JContractRepository extends JpaRepository<JContract, String> {

  List<JContract> findAllByWorkerOrderByEntranceInstantDesc(JWorker jWorker);

  Optional<JContract> findFirstByWorkerAndEndInstantIsNullOrderByEntranceInstantDesc(
      JWorker worker);

  @Query(
      """
      SELECT c FROM JContract c
      JOIN FETCH c.worker
      JOIN FETCH c.level
      WHERE ((c.endInstant >= :from) or (c.endInstant is null))
      AND (c.entranceInstant < :to)
      ORDER BY c.entranceInstant DESC
      """)
  List<JContract> findByYearBetween(Instant from, Instant to);

  @Query("SELECT c FROM JContract c WHERE c.endInstant IS NULL AND c.durationInDays != 0")
  List<JContract> findActiveContracts();
}
