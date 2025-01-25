package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum StatusPreVenda {

    PENDENTE(1),
    APROVADA(2),
    RECUSADA(3);

    private final int value;

    StatusPreVenda(int value){
        this.value = value;
    }

    /*public static StatusPreVenda fromValue(int value){
        for (StatusPreVenda type : StatusPreVenda.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }

     */
}
