package ru.akirakozov.sd.refactoring.command;

import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;
import ru.akirakozov.sd.refactoring.model.Product;

import java.util.function.Consumer;

public class MinCommand extends AbstractFormattedCommand {
    public MinCommand(Consumer<HtmlBuilder> formatPrefix) {
        super(formatPrefix);
    }

    @Override
    public String name() {
        return "min";
    }

    @Override
    public void doFormatResult(ProductDao dao, HtmlBuilder builder) {
        Product min = dao.min();
        builder.addProduct(min);
    }
}
