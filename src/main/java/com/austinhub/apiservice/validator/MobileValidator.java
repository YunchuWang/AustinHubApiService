package com.austinhub.apiservice.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileValidator implements ConstraintValidator<Mobile, String> {

	public MobileValidator() {
	}

	private boolean required = false;

	@Override
	public void initialize(Mobile constraintAnnotation) {
		required = constraintAnnotation.isRequired();
	}

	@Override
	public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
		Pattern mobile_pattern = Pattern.compile("[1]?\\d{10}");
		// 是否为手机号的实现
		if (required) {
			if (StringUtils.isEmpty(phone)) {
				return false;
			}
			Matcher m = mobile_pattern.matcher(phone);
			return m.matches();

		} else {
			return StringUtils.isEmpty(phone);
		}
	}
}
