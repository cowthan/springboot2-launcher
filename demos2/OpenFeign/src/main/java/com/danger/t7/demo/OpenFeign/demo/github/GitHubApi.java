package com.danger.t7.demo.OpenFeign.demo.github;

import java.util.List;
import java.util.stream.Collectors;

import com.danger.t7.demo.OpenFeign.demo.github.models.Contributor;
import com.danger.t7.demo.OpenFeign.demo.github.models.Issue;
import com.danger.t7.demo.OpenFeign.demo.github.models.Repository;
import feign.Param;
import feign.RequestLine;

/**
 * GitHub
 */
public interface GitHubApi {

    @RequestLine("GET /users/{username}/repos?sort=full_name")
    List<Repository> repos(@Param("username") String owner);

    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);

    @RequestLine("POST /repos/{owner}/{repo}/issues")
    void createIssue(Issue issue, @Param("owner") String owner, @Param("repo") String repo);

    /** Lists all contributors for all repos owned by a user. */
    default List<String> contributors(String owner) {
        return repos(owner).stream()
                .flatMap(repo -> contributors(owner, repo.name).stream())
                .map(c -> c.login)
                .distinct()
                .collect(Collectors.toList());
    }


}
