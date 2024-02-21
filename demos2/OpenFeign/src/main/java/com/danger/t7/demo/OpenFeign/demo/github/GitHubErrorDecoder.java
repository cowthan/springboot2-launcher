package com.danger.t7.demo.OpenFeign.demo.github;

import java.io.IOException;

import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;

/**
 * GitHubErrorDecoder
 */
public class GitHubErrorDecoder implements ErrorDecoder {

    final Decoder decoder;
    final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    public GitHubErrorDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // must replace status by 200 other GSONDecoder returns null
            response = response.toBuilder().status(200).build();
            return (Exception) decoder.decode(response, GitHubClientError.class);
        } catch (final IOException fallbackToDefault) {
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
