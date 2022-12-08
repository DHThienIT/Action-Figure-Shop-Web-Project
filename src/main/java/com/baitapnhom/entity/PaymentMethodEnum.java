package com.baitapnhom.entity;

public enum PaymentMethodEnum {
    PAYPAL("Paypal", "paypal_icon.svg"),
    VNPAY("Vnpay", "vnpay_icon.svg"),
    DIRECT("Thanh Toán Trực Tiếp", "direct_payment_icon.svg");
    
    private final String displayValue;
    private final String imageName;
    
    private PaymentMethodEnum(String displayValue, String imageName) {
        this.displayValue = displayValue;
        this.imageName = imageName;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }
    
    public String getImageName() {
        return imageName;
    }
}
