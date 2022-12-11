package statistic;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface EventsStatistic {
    void incEvent(String name);

    @Nullable
    Statistic getEventStatisticByName(String name);

    List<Statistic> getAllEventStatistic();

    void printStatistic(Writer writer) throws IOException;
}
