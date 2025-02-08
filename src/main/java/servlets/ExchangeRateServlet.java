package servlets;


import jakarta.servlet.ServletException;
import models.CurrenciesModel;
import models.ExchangeRatesModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

import static services.ExchangeService.makePatchAndGetResult;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            ExchangeRatesModel model = new ExchangeRatesModel();
            model.init();
            resp.setContentType("application/json");
            String currencyPair = pathInfo.substring(1);
            String rate = model.getExchangeRateByCurrencyPair(currencyPair);
            if (rate.length() <= 1) {
                model.close();
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            model.close();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject currencyObject = gson.fromJson(rate, JsonObject.class);

            String BaseCurrencyId = currencyObject
                    .get("BASECURRENCYID")
                    .getAsString();
            String TargetCurrencyId = currencyObject
                    .get("TARGETCURRENCYID")
                    .getAsString();

            CurrenciesModel currenciesModel = new CurrenciesModel();
            currenciesModel.init();
            currencyObject.add("BASECURRENCYID",
                    JsonParser.parseString(currenciesModel.getCurrencyById(BaseCurrencyId)));
            currencyObject.add("TARGETCURRENCYID",
                    JsonParser.parseString(currenciesModel.getCurrencyById(TargetCurrencyId)));
            currenciesModel.close();

            rate = gson.toJson(currencyObject);
            PrintWriter out = resp.getWriter();
            out.print(rate);
            out.flush();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        if (req.getPathInfo().length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            String pathInfo = req.getPathInfo().substring(1);

            String rate = req.getParameter("rate");
            String from = pathInfo.substring(0, 3);
            String to = pathInfo.substring(3, 6);

            PrintWriter out = resp.getWriter();
            String result = makePatchAndGetResult(from, to, rate);
            if (Objects.equals(result, "")) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            out.print(result);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }
}
