package com.indrajeet.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indrajeet.spring.exceptions.ProjectIdException;
import com.indrajeet.spring.exceptions.ProjectNotFoundException;
import com.indrajeet.spring.model.Backlog;
import com.indrajeet.spring.model.Project;
import com.indrajeet.spring.model.User;
import com.indrajeet.spring.repository.BacklogRepository;
import com.indrajeet.spring.repository.ProjectRepository;
import com.indrajeet.spring.repository.UserRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private BacklogRepository backlogRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project , String username){
    	
    	if(project.getId() != null){
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if(existingProject !=null &&(!existingProject.getProjectLeader().equals(username))){
                throw new ProjectNotFoundException("Project not found in your account");
            }else if(existingProject == null){
                throw new ProjectNotFoundException("Project with ID: '"+project.getProjectIdentifier()+"' cannot be updated because it doesn't exist");
            }
        }
    	
    	
        try{
        	
        	User user = userRepository.findByUsername(username);
        	
        	project.setUser(user);
        	project.setProjectLeader(user.getUsername());
        	
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            
            if(project.getId() == null) {
            	Backlog backlog = new Backlog();
            	project.setBacklog(backlog);
            	backlog.setProject(project);
            	backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            
            if(project.getId()!=null) {
            	project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            
            return projectRepository.save(project);
            
        }
        catch (Exception e){
            throw new ProjectIdException("Project ID '"+project.getProjectIdentifier().toUpperCase()+"' already exists");
        }

    }
    
    public Project findProjectByIdentifier(String projectId , String username) {
    	
    	// Only want to return the project if the user looking for it is the owner
    	
    	Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
    	
    	if(project == null) {
    		 throw new ProjectIdException("Project ID '"+projectId.toUpperCase()+"' does not exists");
    	}
    	
    	if(!project.getProjectLeader().equals(username)) {
    		throw new ProjectNotFoundException("Project not available in your account!!");
    	}
    	
    	return project;
    }
    
    public Iterable<Project> findAllProjects(String username) {
    	return projectRepository.findAllByProjectLeader(username);
    }
    
    public void deleteProjectByIdentifier(String projectId , String username) {
    	
//    	Project project = projectRepository.findByProjectIdentifier(projectId);
//    	
//    	if(project == null) {
//    		throw new ProjectIdException("cannot delete Project ID '"+projectId.toUpperCase()+"'. This project does not exists");
//    	}
//    	
    	
    	
    	projectRepository.delete(findProjectByIdentifier(projectId,username));
    }

}