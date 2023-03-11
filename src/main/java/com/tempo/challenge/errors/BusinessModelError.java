package com.tempo.challenge.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Generic bussiness error object.
 * @version 1.0
 * @author mpasut
 */
@Data
public class BusinessModelError {
	@JsonProperty("error_code")
	private String errorCode;
	@JsonProperty("error_message")
	private String errorMessage;

	public BusinessModelError(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}