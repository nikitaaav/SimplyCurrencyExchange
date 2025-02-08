package servlets;


import models.CurrenciesModel;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    CurrenciesModel model = new CurrenciesModel();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        model.init();
        try {
            out.print(model.getCurrencies());
            model.close();
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        try {
            model.init();
            if (Objects.equals(name, null) || Objects.equals(code, null) || Objects.equals(sign, null)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } else if (model.checkIsCurrencyAlreadyExists(code)) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
            model.putCurrency(name, code, sign);

            PrintWriter out = resp.getWriter();
            out.print(model.getCurrency(code));
            out.flush();
            model.close();
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
