package org.example.client.cbr;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.example.exception.CbrApiException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int statusCode = response.status();
        if (statusCode >= 500) {
            return new CbrApiException("Ошибка при работе с сервером ЦБР. Code: " + statusCode);
        }
        return new FeignException.FeignClientException(statusCode, "Ошибка при вызове: " + methodKey, response.request(), null, null);
    }
}
