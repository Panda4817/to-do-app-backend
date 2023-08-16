package dev.kmunton.tasksbackend.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMongoRepository extends MongoRepository<TaskDocument, Long> {

    List<TaskDocument> findByUsername(String username);

    void deleteByUsername(String username);
}
