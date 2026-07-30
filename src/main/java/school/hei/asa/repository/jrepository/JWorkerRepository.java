package school.hei.asa.repository.jrepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import school.hei.asa.repository.model.JWorker;

@Repository
public interface JWorkerRepository extends JpaRepository<JWorker, String> {
  @Override
  List<JWorker> findAll();

  JWorker findByCode(String code);

  Optional<JWorker> findByEmail(String email);

  @Query(
      """
SELECT distinct w FROM JWorker w
JOIN JContract c ON c.worker = w
WHERE ((c.endInstant >= :from) or (c.endInstant is null))
AND (c.entranceInstant < :to)
""")
  List<JWorker> findByYearBetween(Instant from, Instant to);
}
