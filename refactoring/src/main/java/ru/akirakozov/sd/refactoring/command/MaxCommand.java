package ru.akirakozov.sd.refactoring.command;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;
import ru.akirakozov.sd.refactoring.model.Product;

import java.util.function.Consumer;

public class MaxCommand extends AbstractFormattedCommand {
    public MaxCommand(Consumer<HtmlBuilder> formatPrefix) {
        super(formatPrefix);
    }

    @Override
    public String name() {
        return "max";
    }

    @Override
    public void doFormatResult(ProductDao dao, HtmlBuilder builder) {
        Product max = dao.max();
        builder.addProduct(max);
    }
}
