package com.danger.t7.demo.OpenFeign.demo.github.models;

import java.util.List;

/**
 * Issue
 */
public class Issue {

    public Issue() {

    }

    public String title;
    public String body;
    public List<String> assignees;
    public int milestone;
    public List<String> labels;
}
