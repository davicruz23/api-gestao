package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum TipoDeUsuario {

    SUPERADMIN(1),
    FUNCIONARIO(2);

    private final int value;

    TipoDeUsuario(int value){
        this.value = value;
    }

    /*public static TipoDeUsuario fromValue(int value){
        for (TipoDeUsuario type : TipoDeUsuario.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }

     */
}
