package vn.amitgroup.digitalsignatureapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.amitgroup.digitalsignatureapi.entity.TaskScheduler;

import java.util.List;

public interface TaskSchedulerRepository extends JpaRepository<TaskScheduler,Integer> {
    @Query(value = "select t from TaskScheduler t order by t.time asc")
    List<TaskScheduler> getAllTask();
}
