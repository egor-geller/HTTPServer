package com.egor.httpserver.http;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;
    private String httpVersion;

    HttpRequest() {
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (methodName.equals(httpMethod.name())) {
                this.method = httpMethod;
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_METHOD_NOT_IMPLEMENTED);
    }
}
