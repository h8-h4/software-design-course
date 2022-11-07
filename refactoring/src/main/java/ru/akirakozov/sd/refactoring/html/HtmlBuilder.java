package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.model.Product;

public class HtmlBuilder {
    private static final String HEADER = "<html><body>";
    private static final String FOOTER = "</body></html>";
    private static final String LINE_BREAK = "</br>";
    private final StringBuilder builder;

    public HtmlBuilder() {
        this.builder = new StringBuilder(HEADER).append(System.lineSeparator());
    }

    public HtmlBuilder addProduct(Product product) {
        builder.append(product.getName())
                .append("\t")
                .append(product.getPrice())
                .append(LINE_BREAK)
                .append(System.lineSeparator());
        return this;
    }

    public HtmlBuilder addHeader(String header) {
        builder.append("<h1>")
                .append(header)
                .append("</h1>")
                .append(System.lineSeparator());
        return this;
    }

    public HtmlBuilder addLine(String line) {
        builder.append(line)
                .append(System.lineSeparator());
        return this;
    }

    public String build() {
        return builder.append(FOOTER).toString();
    }

}
