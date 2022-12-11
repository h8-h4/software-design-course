import statistic.LastHourEventsStatistic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.Clock;

public class Main {
    public static void main(String[] args) throws IOException {
        LastHourEventsStatistic statistic = new LastHourEventsStatistic(Clock.systemUTC());

        statistic.incEvent("a");
        statistic.incEvent("a");
        statistic.incEvent("b");

        statistic.printStatistic(new BufferedWriter(new OutputStreamWriter(System.out)));
    }
}
