package com.mysite.Ayoplanner.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserDataIntegrityViolationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final ErrorCode errorCode;

	@Override
	public String getMessage() {
		return errorCode.getMessage();
	}
}