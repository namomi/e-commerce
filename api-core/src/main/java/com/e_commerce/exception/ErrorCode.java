package com.e_commerce.exception;

public record ErrorCode(int status, String message) {
    public static final ErrorCode DUPLICATE_USER = new ErrorCode(400, "This is an ID or email that has already been subscribed.");
    public static final ErrorCode ALREADY_LOGGED_IN = new ErrorCode(403, "You are already logged in.");
    public static final ErrorCode INVALID_OR_INACTIVE_TOKEN = new ErrorCode(403, "Invalid or inactive token.");

}
