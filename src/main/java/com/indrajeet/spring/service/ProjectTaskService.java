package com.indrajeet.spring.service;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indrajeet.spring.exceptions.ProjectNotFoundException;
import com.indrajeet.spring.model.Backlog;
//import com.indrajeet.spring.model.Project;
import com.indrajeet.spring.model.ProjectTask;
//import com.indrajeet.spring.repository.BacklogRepository;
//import com.indrajeet.spring.repository.ProjectRepository;
import com.indrajeet.spring.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
//	@Autowired
//	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
//	@Autowired
//	private ProjectRepository projectRepository;
	
	@Autowired
	private ProjectService projectService;
	
	
	public ProjectTask addProjectTask(String projectIdentifier , ProjectTask projectTask , String username) {
		
          // Exceptions : project Not found
		try {
            //PTs to be added to a specific project, project != null, BL exists
          //  Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
//            Backlog backlog = projectRepository.findByProjectIdentifier(projectIdentifier).getBacklog();
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();
            //set the bl to pt
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
            Integer BacklogSequence = backlog.getPTSequence();
            // Update the BL SEQUENCE
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);

            //Add Sequence to Project Task
            projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority null

            //INITIAL status when status is null
            if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            if(projectTask.getPriority()==null || projectTask.getPriority()==0){ //In the future we need projectTask.getPriority()== 0 to handle the form
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);
            
        }catch (Exception e){
            throw new ProjectNotFoundException("Project not Found");
        }
	
		
		
	}


	public Iterable<ProjectTask> findBacklogById(String id , String username) {
		
//		Project project = projectRepository.findByProjectIdentifier(id);
//		
//		if(project == null) {
//			throw new ProjectNotFoundException("Project with Id: "+ id + " does not exist");
//		}
//	
		
		projectService.findProjectByIdentifier(id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	
	public ProjectTask findPTByProjectSequence(String backlog_id , String pt_id , String username) {
		
           // make sure we are searching on existing backlog
		
//		    Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		   
		    
//		    if(backlog == null) {
//		       throw new ProjectNotFoundException("Project with Id: "+ backlog_id + " does not exist");
//		    }
		
		    projectService.findProjectByIdentifier(backlog_id, username);
		
		
	       // make sure our project task exists
		    
		    ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		    
		  
		    if(projectTask == null) {
		    	 throw new ProjectNotFoundException("Project Task: "+ pt_id + " does not exist");
		    }
			
		    
	      // make sure that the backlog/projectId in the path corresponds to right project
		    

		    if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
		    	
		    	 throw new ProjectNotFoundException("Project Task: "+ pt_id + " does not exist in project: "+backlog_id);
		    }
		
		return projectTask;
	}

	
	
	// update the project task 
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask , String backlog_id , String pt_id , String username) {
		// find existing project task
       // ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		
      // Adding validation to updateByProjectSequence , the same validation we have to perform in this case	, so we call the findPTByProjectSequence func so that we have same validation	
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id , pt_id , username);
		
		
	//  replace with existing project task
		projectTask = updatedTask;
	//  save update
		return projectTaskRepository.save(projectTask);
	}
	
	// Delete the project task 
	public void deletePTByProjectSequence(String backlog_id , String pt_id , String username) {
		
		ProjectTask projectTask = findPTByProjectSequence(backlog_id , pt_id , username);
		
//		Backlog backlog = projectTask.getBacklog();
//		List<ProjectTask> pts = backlog.getProjectTask();
//		pts.remove(projectTask);
//		backlogRepository.save(backlog);
		projectTaskRepository.delete(projectTask);
		
	}

}
