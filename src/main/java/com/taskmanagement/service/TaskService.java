package com.taskmanagement.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;

@Service
public class TaskService {

	 @Autowired
	    private TaskRepository taskRepository;

	    public List<Task> filterAndSearchTasks(String status, String priority, Date dueDateStart, Date dueDateEnd, String searchTitle, String searchDescription) {
	        return taskRepository.filterAndSearchTasks(status, priority, dueDateStart, dueDateEnd, searchTitle, searchDescription);
	    }
	    
	    public Page<Task> getTasks(int page, int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        return taskRepository.findAll(pageable);
	    }
}

