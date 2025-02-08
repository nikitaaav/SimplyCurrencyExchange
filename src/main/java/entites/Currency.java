package entites;


import lombok.Data;

@Data
public class Currency {
    private int id;
    private String code;
    private String fullName;
    private String sign;
}
