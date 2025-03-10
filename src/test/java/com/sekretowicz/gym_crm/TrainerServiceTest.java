import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sekretowicz.gym_crm.model.Trainer;
import com.sekretowicz.gym_crm.model.User;
import com.sekretowicz.gym_crm.repo.TrainerRepo;
import com.sekretowicz.gym_crm.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;

class TrainerServiceTest {
    @Mock
    private TrainerRepo trainerRepo;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer testTrainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testTrainer = new Trainer();
        testTrainer.setId(1L);
        testTrainer.setUser(new User());
        testTrainer.setSpecialization("Fitness");
    }

    @Test
    void createTrainer_ShouldSaveTrainer() {
        when(trainerRepo.save(any(Trainer.class))).thenReturn(testTrainer);
        trainerService.create(testTrainer);
        verify(trainerRepo, times(1)).save(testTrainer);
    }
}
