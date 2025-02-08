package entites;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rate {
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private BigDecimal rate;
}
