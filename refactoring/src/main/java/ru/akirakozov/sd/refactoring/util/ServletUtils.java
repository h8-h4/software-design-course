package ru.akirakozov.sd.refactoring.util;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;

@UtilityClass
public class ServletUtils {
    public void responseOk(HttpServletResponse response) {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
