package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.command.*;
import ru.akirakozov.sd.refactoring.dao.ProductDao;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;
import ru.akirakozov.sd.refactoring.util.ServletUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private static final List<Command> availableCommands = List.of(
            new MaxCommand(html -> html.addHeader("Product with max price: ")),
            new MinCommand(html -> html.addHeader("Product with min price: ")),
            new CountCommand(html -> html.addLine("Summary price: ")),
            new SumCommand(html -> html.addLine("Number of products: "))
    );
    private static final Map<String, Command> commands =
            availableCommands.stream()
                    .collect(Collectors.toMap(Command::name, Function.identity()));
    private final ProductDao productDao;

    public QueryServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String commandParam = request.getParameter("command");
        Command command = commands.get(commandParam);

        if (command == null) {
            response.getWriter().println("Unknown command: " + commandParam);
            ServletUtils.responseOk(response);
            return;
        }

        HtmlBuilder builder = new HtmlBuilder();

        command.formatResult(productDao, builder);

        response.getWriter().println(builder.build());
        ServletUtils.responseOk(response);
    }

}
