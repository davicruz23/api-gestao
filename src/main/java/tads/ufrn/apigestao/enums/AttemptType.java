package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum AttemptType {
    ATTEMPT(1),   // só tentativa de cobrança
    PAYMENT(2),   // pagamento efetuado
    PROMISE(3),   // promessa de pagamento futuro
    ADJUSTMENT(4); // ajuste de valor ou data

    private final int value;

    AttemptType(int value){
        this.value = value;
    }

    public static AttemptType fromValue(int value){
        for (AttemptType type : AttemptType.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
}