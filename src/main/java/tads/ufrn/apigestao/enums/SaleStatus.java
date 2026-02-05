package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum SaleStatus {

        ATIVO(1),
        DEFEITO_PRODUTO(2),
        DEVOLVIDO_CLIENTE(3),
        DESISTENCIA(4);

        private final int value;

    SaleStatus(int value){
            this.value = value;
        }

        public static SaleStatus fromValue(int value){
            for (SaleStatus type : SaleStatus.values()){
                if (type.getValue() == value){
                    return type;
                }
            }
            throw new RuntimeException("Unknown value: "+ value);
        }
    }
