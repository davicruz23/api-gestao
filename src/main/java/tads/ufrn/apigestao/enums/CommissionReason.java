package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum CommissionReason {
    ADIANTAMENTO(1),
    FECHAMENTO_MENSAL(2),
    CONSULTA(3),
    OUTRO(4);

    private final int value;

    CommissionReason(int value){
        this.value = value;
    }

    public static CommissionReason fromValue(int value){
        for (CommissionReason type : CommissionReason.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
}
