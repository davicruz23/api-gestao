package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum PaymentType {

    CASH(1),
    PARCEL(2),
    CREDIT(3),
    DEBIT(4),
    PIX(5);

    private final int value;

    PaymentType(int value){
        this.value = value;
    }

    public static PaymentType fromValue(int value){
        for (PaymentType type : PaymentType.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }
}
