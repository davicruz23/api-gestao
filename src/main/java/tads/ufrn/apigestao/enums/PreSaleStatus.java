package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum PreSaleStatus {

    PENDENTE(1),
    APROVADA(2),
    RECUSADA(3);

    private final int value;

    PreSaleStatus(int value){
        this.value = value;
    }

    public static PreSaleStatus fromValue(int value){
        for (PreSaleStatus type : PreSaleStatus.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
}
