package models;

import com.google.gson.Gson;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class CurrenciesModel extends ModelImpl{

    public CurrenciesModel() {
        super();
    }

    public String getCurrencies() {
        String query = "SELECT * FROM Currencies";
        return new Gson().toJson(getList(query));
    }

    public String getCurrency(String code) {
        List<Map<String, Object>> listOfMaps = getCurrenciesMaps(code);
        return new Gson().toJson(listOfMaps.getFirst());
    }

    public void putCurrency(String name, String code, String sign) {
        String query = format("INSERT INTO Currencies (Code, FullName, Sign) VALUES\n" +
                "    ('%s', '%s', '%s')", code, name, sign);
        try (Statement statement = conn.createStatement();){
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkIsCurrencyAlreadyExists(String code) {
        final List<Map<String, Object>> listOfMaps = getCurrenciesMaps(code);
        return !listOfMaps.isEmpty();
    }

    public List<Map<String, Object>> getCurrenciesMaps(String code) {
        String query = format("SELECT * FROM Currencies WHERE Code='%s'", code);
        List<Map<String, Object>> listOfMaps;
        try {
            QueryRunner queryRunner = new QueryRunner();
            listOfMaps = queryRunner.query(conn, query, new MapListHandler());
        } catch (SQLException e) {
            System.out.println("Такой валюты нет бд!");
            throw new RuntimeException(e);
        }
        return listOfMaps;
    }

    public String getCurrencyById(String id) {
        String query = format("SELECT * FROM Currencies WHERE Id=%s", id);
        return new Gson().toJson(getList(query).getFirst());
    }

}
