package com.indrajeet.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.indrajeet.spring.model.Backlog;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog , Long>{
	
	 Backlog findByProjectIdentifier(String Identifier);

}
