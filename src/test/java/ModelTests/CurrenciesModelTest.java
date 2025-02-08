package ModelTests;

import models.CurrenciesModel;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;

public class CurrenciesModelTest {

    CurrenciesModel model = new CurrenciesModel();

    @BeforeEach
    public void connect() {
        model.init();
    }

    @AfterEach
    public void disconnect() throws Exception {
        model.close();
    }

    @Test
    public void testInit() {}

    @Test
    public void testGetCurrencies() throws JSONException {
        String expected = """
                [
                  {
                    "ID": 17,
                    "CODE": "EUR",
                    "FULLNAME": "Euro",
                    "SIGN": "€"
                  },
                  {
                    "ID": 18,
                    "CODE": "USD",
                    "FULLNAME": "US Dollar",
                    "SIGN": "$"
                  }
                ]
                """;
        String actual = model.getCurrencies();
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void testGetCurrency() throws JSONException {
        String expected = """
                  {
                    "ID": 18,
                    "CODE": "USD",
                    "FULLNAME": "US Dollar",
                    "SIGN": "$"
                  }
                """;
        String actual = model.getCurrency("USD");
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void testClose() {}

}
