package models;


import com.google.gson.Gson;

import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.String.format;
import static services.CurrenciesService.getIdByCode;

public class ExchangeRatesModel extends ModelImpl{

    public ExchangeRatesModel() {
        super();
    }

    public String getExchangeRates() {
        String query = "SELECT * FROM ExchangeRates";
        return new Gson().toJson(getList(query));
    }

    public String getExchangeRateByCurrencyPair(String currencyPair) {
        String firstCurrency = currencyPair.substring(0, 3);
        String secondCurrency = currencyPair.substring(3);
        return getRate(firstCurrency ,secondCurrency);
    }

    public String getRate(String firstCurrency, String secondCurrency) {
        init();
        CurrenciesModel model = new CurrenciesModel();
        model.init();
        String  firstCurrencyId  = String.valueOf(model.getCurrenciesMaps(firstCurrency).getFirst().get("Id"));
        String  secondCurrencyId  = String.valueOf(model.getCurrenciesMaps(secondCurrency).getFirst().get("Id"));
        model.close();

        String query = format("SELECT * FROM ExchangeRates WHERE BaseCurrencyId=%s AND TargetCurrencyId=%s",
                firstCurrencyId, secondCurrencyId);
        String result = new Gson().toJson(getList(query).getFirst());
        close();
        return result;
    }

    public String putRate(String baseCurrencyId, String targetCurrencyId, String rate) {
        String query = format("INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (%s, %s, %s)",
                baseCurrencyId, targetCurrencyId, rate);

        try {
            init();
            Statement statement = conn.createStatement();
            statement.execute(query);
            statement.close();
            close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        query = format("SELECT * FROM ExchangeRates WHERE BaseCurrencyId=%s AND TargetCurrencyId=%s",
                baseCurrencyId, targetCurrencyId);

        String resp = new Gson().toJson(getList(query).getFirst());
        return resp;
    }

    public void patchRate(String from, String to, String rate) {
        String baseId = getIdByCode(from);
        String targetId = getIdByCode(to);
        String query = format("UPDATE ExchangeRates SET Rate=%s WHERE BaseCurrencyId=%s AND TargetCurrencyId=%s",
                rate, baseId, targetId);

        try {
            init();
            Statement statement = conn.createStatement();
            statement.execute(query);
            statement.close();
            close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
