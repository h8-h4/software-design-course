package app.common.event.applier;

import app.common.event.TestEvent;
import app.common.event.spring.annotation.Applier;

@Applier
public class ConcatDataApplier extends ApplierContainer<String> {

    @Applier
    public String concat(TestEvent event, String str) {
        return str.concat(event.getData());
    }
}
