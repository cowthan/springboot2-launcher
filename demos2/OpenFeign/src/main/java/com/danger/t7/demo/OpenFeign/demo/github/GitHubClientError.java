package com.danger.t7.demo.OpenFeign.demo.github;

/**
 * GitHubClientError
 */
public  class GitHubClientError extends RuntimeException {
    private String message; // parsed from json

    @Override
    public String getMessage() {
        return message;
    }
}
