package tads.ufrn.apigestao.enums;

import lombok.Getter;

@Getter
public enum UserType {

    SUPERADMIN(1),
    FUNCIONARIO(2),
    VENDEDOR(3),
    COBRADOR(4),
    FISCAL(5);

    private final int value;

    UserType(int value){
        this.value = value;
    }

    public static UserType fromValue(int value){
        for (UserType type : UserType.values()){
            if (type.getValue() == value){
                return type;
            }
        }
        throw new RuntimeException("Unknown value: "+ value);
    }

}
