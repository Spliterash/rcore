package com.rcore.commons.utils;

import lombok.Value;

public interface PhoneNumberFormatter {

    FormattedPhoneNumber formatPhone(PhoneNumberValidator.PhoneNumber phone);

    class PhoneNumberFormattingException extends RuntimeException {}

    @Value(staticConstructor = "of")
    class FormattedPhoneNumber {
        Long number;
        String formatted;
    }

}
