package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.CurrenciesModel;
import models.ExchangeRatesModel;

import static services.CurrenciesService.getIdByCode;

public class ExchangeService {

    public static String countExchangeRate(String from, String to, Double amount) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ExchangeRatesModel eModel = new ExchangeRatesModel();
        eModel.init();
        String strRate = eModel.getRate(from, to);
        Double rate;
        if (strRate.isEmpty()) {
            strRate = eModel.getRate(to, from);
            rate = 1 / Double.parseDouble(String.valueOf(gson
                    .fromJson(strRate, JsonObject.class).get("RATE")));
        } else {
            rate = Double.parseDouble(String.valueOf(gson
                    .fromJson(strRate, JsonObject.class).get("RATE")));
        }
        eModel.close();
        Double convertedAmount = rate * amount;

        CurrenciesModel cModel = new CurrenciesModel();
        cModel.init();
        JsonObject jsonResp = new JsonObject();
        jsonResp.addProperty("BASECURRENCYID", gson.toJson(cModel
                .getCurrenciesMaps(from)
                .getFirst()));
        jsonResp.addProperty("TARGETCURRENCYID", gson.toJson(cModel
                .getCurrenciesMaps(from)
                .getFirst()));

        jsonResp.add("BASECURRENCYID",
                JsonParser.parseString(cModel.getCurrencyById(getIdByCode(from))));
        jsonResp.add("TARGETCURRENCYID",
                JsonParser.parseString(cModel.getCurrencyById(getIdByCode(to))));

        cModel.close();
        jsonResp.addProperty("rate", rate);
        jsonResp.addProperty("amount", amount);
        jsonResp.addProperty("convertedAmount", convertedAmount);

        return gson.toJson(jsonResp);
    }

    public static String makePatchAndGetResult(String from, String to, String rate) {
        ExchangeRatesModel model = new ExchangeRatesModel();
        model.init();
        model.patchRate(from, to, rate);
        String result = model.getExchangeRateByCurrencyPair(from + to);
        model.close();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject currencyObject = gson.fromJson(result, JsonObject.class);

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

        result = gson.toJson(currencyObject);
        return result;
    }
}
