package app.common.event.publisher;

import app.common.CommonTestConfiguration;
import app.common.event.GenericEvent;
import app.common.event.TestEvent;
import app.common.event.TestEventRepository;
import app.common.event.spring.annotation.EventHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class EventPublisherTest {
    @Autowired
    private TestEventRepository eventRepository;

    @Autowired
    private EventPublisher eventPublisher;

    private int handled;


    @BeforeEach
    public void init() {
        handled = 0;
    }


    @Test
    public void testHandles() {
        final TestEvent event = TestEvent.builder()
                .data("test")
                .build();

        eventPublisher.publish(event);

        Assertions.assertEquals(1, handled);

        final List<TestEvent> all = eventRepository.findAll();

        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(event, all.get(0));
    }


    @Test
    public void testHandlesList() {
        final List<GenericEvent> events = List.of(
                TestEvent.builder()
                        .data("test")
                        .build(),
                TestEvent.builder()
                        .data("test2")
                        .build()
        );
        eventPublisher.publish(events);

        final List<TestEvent> all = eventRepository.findAll();

        Assertions.assertEquals(2, handled);
        Assertions.assertEquals(2, all.size());
        Assertions.assertEquals(events, all);
    }


    @EventHandler
    public void handle(TestEvent event) {
        Assertions.assertNotNull(event);
        Assertions.assertNotNull(event.getCreatedAt());
        Assertions.assertNotNull(event.getId());
        handled++;
    }
}
