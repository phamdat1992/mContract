package vn.amitgroup.digitalsignatureapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.entity.TaskScheduler;
import vn.amitgroup.digitalsignatureapi.repository.TaskSchedulerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskSchedulerService {
    @Autowired
    TaskSchedulerRepository taskSchedulerRepository;
    public List<TaskScheduler> getAll()
    {
        if(taskSchedulerRepository.getAllTask().size()<1)
        {
            return  new ArrayList<>();
        }
        return taskSchedulerRepository.getAllTask();
    }
    public void delete(TaskScheduler taskScheduler)
    {
        taskSchedulerRepository.delete(taskScheduler);
    }
    public  void save(TaskScheduler taskScheduler)
    {
        taskSchedulerRepository.save(taskScheduler);
    }
}
