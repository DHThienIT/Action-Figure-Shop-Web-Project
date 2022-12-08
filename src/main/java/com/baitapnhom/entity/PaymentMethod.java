package com.baitapnhom.entity;

import javax.validation.constraints.NotNull;

public class PaymentMethod {
    private PaymentMethodEnum paymentMethodEnum;
    
    public PaymentMethod(PaymentMethodEnum paymentMethodEnum, String option) {
        this.paymentMethodEnum = paymentMethodEnum;
    }

    public PaymentMethod() {
    }

    public PaymentMethodEnum getPaymentMethodEnum() {
        return paymentMethodEnum;
    }

    public void setPaymentMethodEnum(PaymentMethodEnum paymentMethodEnum) {
        this.paymentMethodEnum = paymentMethodEnum;
    }
}
