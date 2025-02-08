package services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import models.CurrenciesModel;

public class CurrenciesService {
    public static String getIdByCode(String code) {
        CurrenciesModel model = new CurrenciesModel();
        model.init();

        String currency = model.getCurrency(code);

        model.close();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject object = gson.fromJson(currency, JsonObject.class);

        return String.valueOf(object.get("ID"));
    }
}
