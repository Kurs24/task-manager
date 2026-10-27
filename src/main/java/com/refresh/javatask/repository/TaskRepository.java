package com.refresh.javatask.repository;

import com.refresh.javatask.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

 /*
 actually si @Repository ini ga perlu karena interface ini extends JpaRepository
 yang mana secara otomatis dia akan diubah menjadi bean oleh SpringData
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(Task.Status status);
}
