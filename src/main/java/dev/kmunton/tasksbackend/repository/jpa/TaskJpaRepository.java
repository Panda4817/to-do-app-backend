package dev.kmunton.tasksbackend.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByUsername(String username);

    void deleteByUsername(String username);
}
