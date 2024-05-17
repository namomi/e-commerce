package com.e_commerce.exception;

public record ErrorCode(int status, String message) {
    public static final ErrorCode DUPLICATE_USER = new ErrorCode(400,
            "This is an ID or email that has already been subscribed.");
    public static final ErrorCode ALREADY_LOGGED_IN = new ErrorCode(403, "You are already logged in.");
    public static final ErrorCode INVALID_OR_INACTIVE_TOKEN = new ErrorCode(403, "Invalid or inactive token.");
    public static final ErrorCode NOT_AUTHORIZED = new ErrorCode(403, "You do not have permission.");
    public static final ErrorCode NO_MATCHING_USER = new ErrorCode(403, "No matching user");
    public static final ErrorCode NO_MATCHING_ADDRESS = new ErrorCode(403, "No matching addresses");
    public static final ErrorCode DUPLICATE_ADDRESS = new ErrorCode(403, "Duplicate address.");
    public static final ErrorCode DUPLICATE_PAYMENT = new ErrorCode(403, "Duplicate payment.");
    public static final ErrorCode MISSING_INFORMATION = new ErrorCode(403, "Please enter your information");
    public static final ErrorCode PRODUCT_REGISTRATION_FAILED = new ErrorCode(403,
            "An error occurred while registering the product");
    public static final ErrorCode NO_PRODUCT_INFORMATION = new ErrorCode(403, "There is no product information.");
    public static final ErrorCode UPLOAD_FILE_IS_EMPTY = new ErrorCode(403, "Cannot upload an empty file.");
    public static final ErrorCode ERROR_UPDATE_ITEM = new ErrorCode(403, "Error updating item.");
    public static final ErrorCode SEARCH_PHRASE_ANALYSIS_FAILED = new ErrorCode(403, "Failed to parse search results.");
    public static final ErrorCode URL_CREATION_FAILED = new ErrorCode(403, "Failed to generate URL for the uploaded image.");
}
