package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.CurrenciesModel;
import models.ExchangeRatesModel;

import static services.CurrenciesService.getIdByCode;

public class RatesService {
    public static String injectRate(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        String baseCurrencyId = getIdByCode(baseCurrencyCode);
        String targetCurrencyId = getIdByCode(targetCurrencyCode);

        ExchangeRatesModel eModel = new ExchangeRatesModel();
        String exchangeRate = eModel.putRate(baseCurrencyId, targetCurrencyId, rate);
        eModel.close();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = gson.fromJson(exchangeRate, JsonObject.class);


        CurrenciesModel cModel = new CurrenciesModel();
        cModel.init();
        object.add("BASECURRENCYID",
                JsonParser.parseString(cModel.getCurrencyById(baseCurrencyId)));
        object.add("TARGETCURRENCYID",
                JsonParser.parseString(cModel.getCurrencyById(targetCurrencyId)));
        cModel.close();

        exchangeRate = gson.toJson(object);

        return exchangeRate;
    }
}
