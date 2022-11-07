package ru.akirakozov.sd.refactoring.command;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

import java.util.function.Consumer;

public abstract class AbstractFormattedCommand implements Command {
    private final Consumer<HtmlBuilder> formatPrefix;

    protected AbstractFormattedCommand(Consumer<HtmlBuilder> formatPrefix) {
        this.formatPrefix = formatPrefix;
    }

    @Override
    public void formatResult(ProductDao dao, HtmlBuilder builder) {
        formatPrefix.accept(builder);
        doFormatResult(dao, builder);
    }

    protected abstract void doFormatResult(ProductDao dao, HtmlBuilder builder);
}
