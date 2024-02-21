package com.danger.t7.demo.OpenFeign.demo.github;

/**
 * GitHubExample
 */

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.danger.t7.demo.OpenFeign.demo.github.models.Issue;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

/**
 * Inspired by {@code com.example.retrofit.GitHubClient}
 */
public class GitHubExample {

    private static GitHubApi getGithubClient() {
        final Decoder decoder = new GsonDecoder();
        final Encoder encoder = new GsonEncoder();
        return Feign.builder()
                .encoder(encoder)
                .decoder(decoder)
                .errorDecoder(new GitHubErrorDecoder(decoder))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .requestInterceptor(template -> {
                    template.header(
                            // not available when building PRs...
                            // https://docs.travis-ci.com/user/environment-variables/#defining-encrypted-variables-in-travisyml
                            "Authorization",
                            "token 383f1c1b474d8f05a21e7964976ab0d403fee071");
                })
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(GitHubApi.class, "https://api.github.com");
    }


    public static void main(String... args) {
        final GitHubApi github = getGithubClient();

        System.out.println("Let's fetch and print a list of the contributors to this org.");
        final List<String> contributors = github.contributors("openfeign");
        for (final String contributor : contributors) {
            System.out.println(contributor);
        }

        System.out.println("Now, let's cause an error.");
        try {
            github.contributors("openfeign", "some-unknown-project");
        } catch (final GitHubClientError e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Now, try to create an issue - which will also cause an error.");
        try {
            final Issue issue = new Issue();
            issue.title = "The title";
            issue.body = "Some Text";
            github.createIssue(issue, "OpenFeign", "SomeRepo");
        } catch (final GitHubClientError e) {
            System.out.println(e.getMessage());
        }
    }

}
