package models;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class ModelImpl implements Model {
    protected Connection conn;
    @Override
    public void init() {
        String url = "jdbc:h2:C:/files/CurrencyExchangerDB/local.db";
        String user = "myuser";
        String password = "1111";
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Ошибка подключения к базе данных!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> getList(String query) {
        List<Map<String, Object>> listOfMaps;
        try {
            QueryRunner queryRunner = new QueryRunner();
            listOfMaps = queryRunner.query(conn, query, new MapListHandler());
        } catch (SQLException e) {
            System.out.println("Неправильный запрос к бд!");
            throw new RuntimeException(e);
        }
        return listOfMaps;
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Не удалось закрыть соединение!");
            throw new RuntimeException(e);
        }
    }
}
