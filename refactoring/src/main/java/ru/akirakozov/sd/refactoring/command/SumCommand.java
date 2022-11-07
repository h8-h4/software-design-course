package ru.akirakozov.sd.refactoring.command;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

import java.util.function.Consumer;

public class SumCommand extends AbstractFormattedCommand {
    public SumCommand(Consumer<HtmlBuilder> formatPrefix) {
        super(formatPrefix);
    }

    @Override
    public String name() {
        return "sum";
    }

    @Override
    public void doFormatResult(ProductDao dao, HtmlBuilder builder) {
        long sum = dao.sum();
        builder.addLine(String.valueOf(sum));
    }
}
