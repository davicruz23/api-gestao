package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum TipoDePagamento {

    AVISTA(1),
    PARCELADO(2),
    CREDITO(3),
    DEBITO(4),
    PIX(5);

    private final int value;

    TipoDePagamento(int value){
        this.value = value;
    }

    /*public static TipoDePagamento fromValue(int value){
        for (TipoDePagamento type : TipoDePagamento.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }

     */
}
