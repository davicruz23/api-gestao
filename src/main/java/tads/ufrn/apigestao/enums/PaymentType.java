package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum PaymentType {

    CASH(1, "DINHEIRO"),
    PARCEL(2, "PARCELADO"),
    CREDIT(3, "CRÉDITO"),
    DEBIT(4, "DÉBITO"),
    PIX(5, "PIX");

    private final int value;
    private final String description;

    PaymentType(int value, String description) {
        this.value = value;
        this.description = description;
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
