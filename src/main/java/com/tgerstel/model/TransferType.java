package com.tgerstel.model;

public enum TransferType {

    IN_TRANSFER("In transfer"), OUT_TRANSFER("Out transfer"), VAT_OUT_TRANSFER("Vat out transfer"),
    TAX_OUT_TRANSFER("Tax out transfer"), SALARY("Salary transfer");

    TransferType(String value) {
    }

}
