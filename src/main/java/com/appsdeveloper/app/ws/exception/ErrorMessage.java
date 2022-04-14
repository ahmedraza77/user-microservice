package com.appsdeveloper.app.ws.exception;

public enum ErrorMessage {
	MISSING_REQUIRED_FIELD("Missing required fields."),
	RECORD_ALREADY_EXISTS("Record already exists"),
	NO_RECORD_FOUND("Record is not found with provided: "),
	AUTHENTICATION_FAILED("Authentication failed"),
	COULD_NOT_UPDATE_RECORD("Could not update record"),
	COULD_NOT_DELETE_RECORD("Could not delete record");
	
	private String errorMessages;
	
	ErrorMessage(String errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}
}
