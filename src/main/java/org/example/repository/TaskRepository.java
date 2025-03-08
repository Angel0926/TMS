package org.example.repository;

import org.example.models.Task;
import org.example.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByAssignee(User assignee);

    void delete(org.example.models.Task task);

    List<Task> findByAuthor(User user, Pageable pageable);

    List<Task> findByAssignee(User user, Pageable pageable);
}

