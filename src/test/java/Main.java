import models.CurrenciesModel;
import models.ExchangeRatesModel;

import static services.CurrenciesService.getIdByCode;

public class Main {
    public static void main(String[] args) throws Exception {
        ExchangeRatesModel model = new ExchangeRatesModel();
        model.init();
        model.putRate("19", "18", "0.11");
        model.close();
    }
}
