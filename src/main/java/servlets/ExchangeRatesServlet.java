package servlets;


import models.CurrenciesModel;
import models.ExchangeRatesModel;
import com.google.gson.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static services.RatesService.injectRate;


@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeRatesModel model = new ExchangeRatesModel();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/json");
            model.init();

            CurrenciesModel currenciesModel = new CurrenciesModel();
            currenciesModel.init();
            String exchangeRates = model.getExchangeRates();
            JsonArray rates = JsonParser.parseString(exchangeRates).getAsJsonArray();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (int i = 0; i < rates.size(); i++) {
                JsonObject currencyObject = rates.get(i).getAsJsonObject();
                String BaseCurrencyId = currencyObject
                        .get("BASECURRENCYID")
                        .getAsString();
                String TargetCurrencyId = currencyObject
                        .get("TARGETCURRENCYID")
                        .getAsString();

                currencyObject.add("BASECURRENCYID",
                        JsonParser.parseString(currenciesModel.getCurrencyById(BaseCurrencyId)));
                currencyObject.add("TARGETCURRENCYID",
                        JsonParser.parseString(currenciesModel.getCurrencyById(TargetCurrencyId)));
            }
            currenciesModel.close();
            exchangeRates = gson.toJson(rates);
            PrintWriter out = resp.getWriter();
            out.print(exchangeRates);
            out.flush();
            model.close();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rate = req.getParameter("rate");

            String injectedRate = injectRate(baseCurrencyCode, targetCurrencyCode, rate);

            PrintWriter out = resp.getWriter();
            out.print(injectedRate);
            out.flush();
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
