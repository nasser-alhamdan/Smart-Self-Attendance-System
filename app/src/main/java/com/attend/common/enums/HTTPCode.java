package com.attend.common.enums;

public interface HTTPCode {
    int Success = 200;
    int NoContent = 204;
    int BadRequest = 400;
    int Unauthorized = 401;
    int Forbidden = 403;
    int NotFound = 404;
    int Exception = 417;
    int ValidatorError = 422;
}
