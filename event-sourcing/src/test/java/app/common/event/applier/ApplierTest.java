package app.common.event.applier;

import app.common.CommonTestConfiguration;
import app.common.event.TestEvent;
import app.common.event.TestEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase
@ContextConfiguration(classes = CommonTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApplierTest {
    @Autowired
    private ConcatDataApplier concatDataApplier;

    @Autowired
    private TestEventRepository testEventRepository;


    @Test
    public void testApplier() {
        final List<TestEvent> testEvents = testEventRepository.saveAll(
                List.of(
                        TestEvent.builder().data("a").build(),
                        TestEvent.builder().data("b").build(),
                        TestEvent.builder().data("c").build()
                )
        );

        final String apply = concatDataApplier.apply("", testEvents);

        Assertions.assertEquals("abc", apply);
    }
}
