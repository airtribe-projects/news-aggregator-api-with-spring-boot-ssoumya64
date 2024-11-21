package com.airtribe.employeeManagement.Service;

import com.airtribe.employeeManagement.Entity.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    Project saveProject(Project project);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
}
