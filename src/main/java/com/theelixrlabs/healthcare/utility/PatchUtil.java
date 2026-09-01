package com.theelixrlabs.healthcare.utility;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PatchUtil {

    public boolean hasValueForUpdate(String newValue, String existingValue) {
        return !StringUtils.isBlank(newValue) && !newValue.equals(existingValue);
    }

    public String formatAadhaarNumber(String aadhaarNumber) {
        return aadhaarNumber.substring(0, 4) + StringUtils.SPACE +
                aadhaarNumber.substring(4, 8) + StringUtils.SPACE +
                aadhaarNumber.substring(8, 12);
    }
}
