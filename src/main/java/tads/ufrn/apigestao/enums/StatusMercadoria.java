package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum StatusMercadoria {

    DISPONIVEL(1),
    INDISPONIVEL(2),
    RESERVADA(3),
    ENTREGUE(4),
    PERDIDO(5);

    private final int value;

    StatusMercadoria(int value){
        this.value = value;
    }

    /*public static StatusMercadoria fromValue(int value){
        for (StatusMercadoria type : StatusMercadoria.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
     */
}
