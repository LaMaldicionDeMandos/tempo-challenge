package com.tempo.challenge.errors;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Generic bussiness error object.
 * @version 1.0
 * @author mpasut
 */
@Data
public class BusinessModelError implements Serializable {
	@JsonProperty("error_code")
	private String errorCode;
	@JsonProperty("error_message")
	private String errorMessage;

	public BusinessModelError(final String errorCode, final String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}