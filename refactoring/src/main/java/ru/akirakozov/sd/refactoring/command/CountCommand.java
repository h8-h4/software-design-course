package ru.akirakozov.sd.refactoring.command;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

import java.util.function.Consumer;

public class CountCommand extends AbstractFormattedCommand {
    public CountCommand(Consumer<HtmlBuilder> formatPrefix) {
        super(formatPrefix);
    }

    @Override
    public String name() {
        return "count";
    }

    @Override
    public void doFormatResult(ProductDao dao, HtmlBuilder builder) {
        int count = dao.count();
        builder.addLine(String.valueOf(count));
    }
}
