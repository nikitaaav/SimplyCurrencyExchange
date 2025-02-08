package models;

import java.util.List;
import java.util.Map;

public interface Model {
    void init();
    void close();
    List<Map<String, Object>> getList(String query);
}
