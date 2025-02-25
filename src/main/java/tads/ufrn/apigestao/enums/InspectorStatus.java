package tads.ufrn.apigestao.enums;

import lombok.*;

@Getter
public enum InspectorStatus {

    PENDENTE(1),
    APROVADA(2),
    RECUSADA(3);

    private final int value;

    InspectorStatus(int value){
        this.value = value;
    }

    public static InspectorStatus fromValue(int value){
        for (InspectorStatus type : InspectorStatus.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
}
