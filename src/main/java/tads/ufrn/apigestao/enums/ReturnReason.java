package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum ReturnReason {

    DEFEITO(1),
    DESISTENCIA(2),
    ATRASO(3),
    AVARIA(4),
    OUTROS(5);

    private final int value;

    ReturnReason(int value){
        this.value = value;
    }

    public static ReturnReason fromValue(int value){
        for (ReturnReason type : ReturnReason.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
}
