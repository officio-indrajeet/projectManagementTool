package com.indrajeet.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.indrajeet.spring.model.ProjectTask;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask , Long>{
	
	List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);
	
	ProjectTask findByProjectSequence(String sequence);

}
