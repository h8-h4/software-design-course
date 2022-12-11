package statistic;

import lombok.Value;

@Value
public class Statistic {
    String eventName;
    double requestPerMinute;
}
