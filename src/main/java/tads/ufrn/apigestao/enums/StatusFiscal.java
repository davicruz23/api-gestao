package tads.ufrn.apigestao.enums;

import lombok.*;

@Getter
public enum StatusFiscal {

    PENDENTE(1),
    APROVADA(2),
    RECUSADA(3);

    private final int value;

    StatusFiscal(int value){
        this.value = value;
    }

    /*public static StatusFiscal fromValue(int value){
        for (StatusFiscal type : StatusFiscal.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }*/
}
