package vn.inspiron.mcontract.modules.Contract.model;

import java.util.HashMap;
import java.util.Map;

public enum ContractUserRole {
    CREATER(1),
    SIGNER(2),
    APPROVER(3);

    private int value;
    private static Map map = new HashMap<>();

    ContractUserRole(int value) {
        this.value = value;
    }

    static {
        for (ContractUserRole status : ContractUserRole.values()) {
            ContractUserRole.map.put(status.value, status);
        }
    }

    public static ContractUserRole valueOf(int contractUserRole) {
        return (ContractUserRole)ContractUserRole.map.get(contractUserRole);
    }

    public int getValue() {
        return value;
    }
}
