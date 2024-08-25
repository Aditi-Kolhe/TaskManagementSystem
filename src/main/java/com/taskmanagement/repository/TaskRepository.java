package com.taskmanagement.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.taskmanagement.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByStatusAndUserId(String status, Long userId);
    @Query("SELECT t FROM Task t WHERE " +
            "( :status IS NULL OR t.status = :status ) AND " +
            "( :priority IS NULL OR t.priority = :priority ) AND " +
            "( :dueDateStart IS NULL OR t.dueDate >= :dueDateStart ) AND " +
            "( :dueDateEnd IS NULL OR t.dueDate <= :dueDateEnd ) AND " +
            "( :searchTitle IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTitle, '%')) ) AND " +
            "( :searchDescription IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchDescription, '%')) )")
     List<Task> filterAndSearchTasks(
             @Param("status") String status,
             @Param("priority") String priority,
             @Param("dueDateStart") Date dueDateStart,
             @Param("dueDateEnd") Date dueDateEnd,
             @Param("searchTitle") String searchTitle,
             @Param("searchDescription") String searchDescription);
    
    Page<Task> findAll(Pageable pageable);

}


