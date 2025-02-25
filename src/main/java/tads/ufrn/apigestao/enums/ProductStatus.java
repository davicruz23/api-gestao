package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {

    DISPONIVEL(1),
    INDISPONIVEL(2),
    RESERVADA(3),
    ENTREGUE(4),
    PERDIDO(5);

    private final int value;

    ProductStatus(int value){
        this.value = value;
    }

    public static ProductStatus fromValue(int value){
        for (ProductStatus type : ProductStatus.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
}
