package ru.akirakozov.sd.refactoring.command;


import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;

public interface Command {
    String name();

    void formatResult(ProductDao dao, HtmlBuilder builder);
}
