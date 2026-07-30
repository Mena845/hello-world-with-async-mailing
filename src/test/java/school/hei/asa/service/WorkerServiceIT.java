package school.hei.asa.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import school.hei.asa.conf.FacadeIT;

public class WorkerServiceIT extends FacadeIT {
  @Autowired WorkerService workerService;

  Model modelWithYearAttribute;
  Model modelWithStartAndEndDateAttribute;

  @BeforeEach
  void setUp() {
    modelWithYearAttribute = mock(Model.class);
    modelWithStartAndEndDateAttribute = mock(Model.class);

    when(modelWithYearAttribute.getAttribute("year")).thenReturn(2026);
    when(modelWithStartAndEndDateAttribute.getAttribute("startDate")).thenReturn("2024-01-01");
    when(modelWithStartAndEndDateAttribute.getAttribute("endDate")).thenReturn("2024-12-31");
  }

  @Test
  void can_get_worker_from_year() {
    var actual = workerService.getWorkersFrom(modelWithYearAttribute);

    assertTrue(
        actual.stream().anyMatch(w -> w.code().equals("W-P-2024-01")),
        "should contain W-P-2024-01 (contract entrance=2025, end=null)");
    assertTrue(
        actual.stream().noneMatch(w -> w.code().equals("W-101")),
        "should not contain W-101 (contract end=2024 < 2026)");
  }

  @Test
  void can_get_worker_from_date_range() {
    var actual = workerService.getWorkersFrom(modelWithStartAndEndDateAttribute);

    assertTrue(
        actual.stream().anyMatch(w -> w.code().equals("W-101")),
        "should contain W-101 (contract entrance=2024 < 2025, end=2024 >= 2024)");
    assertTrue(
        actual.stream().anyMatch(w -> w.code().equals("W-P-2024-01")),
        "should contain W-P-2024-01 (via V99_6 contract: entrance=2023, end=null)");
  }
}
