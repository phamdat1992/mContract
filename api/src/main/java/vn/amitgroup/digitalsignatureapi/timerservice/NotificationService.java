package vn.amitgroup.digitalsignatureapi.timerservice;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.dto.ContractDetailDto;
import vn.amitgroup.digitalsignatureapi.entity.TaskScheduler;
import vn.amitgroup.digitalsignatureapi.job.NotificationJob;
import vn.amitgroup.digitalsignatureapi.job.info.TimerInfo;
import vn.amitgroup.digitalsignatureapi.service.ContractService;
import vn.amitgroup.digitalsignatureapi.service.impl.TaskSchedulerService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Slf4j
public class NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationJob.class);
    @Autowired
    private SchedulerService scheduler;
    @Autowired
    private ContractService contractService;
    @Autowired
    private TaskSchedulerService taskSchedulerService;

    public void runNotificationJob(long millisecond)
    {
//        if(millisecond==0)
//        {
//            final TimerInfo info = new TimerInfo();
//            info.setTotalFireCount(1);
//            info.setInitialOffsetMs(5000);
//            info.setCallbackData("My callback");
//            scheduler.schedule(NotificationJobExpired.class,info);
//            LOG.info("task tiếp theo: "+ getAllRunningTimers().get(0).getInitialOffsetMs());
//        }
//        else
//        {
//            final TimerInfo info = new TimerInfo();
//            info.setTotalFireCount(1);
//            info.setInitialOffsetMs(millisecond);
//            info.setCallbackData("My callback");
//            scheduler.schedule(NotificationJob.class,info);
//            LOG.info("task tiếp theo: "+ getAllRunningTimers().get(0).getInitialOffsetMs());
//        }

    }


    public List<TimerInfo> getAllRunningTimers(){
        return scheduler.getAllRunningTimers();
    }

    public Boolean deleteTimer(final String timerId) {
        return scheduler.deleteTimer(timerId);
    }

    public void setTime(String id,int numberOFExpirationDate)
    {
        try{

                ContractDetailDto contract = contractService.getContractById(id);
                long fullTime = contract.getExpirationTime().getTime() - contract.getCreatedTime().getTime();//tính bằng milliseconds

                long numberOFExpiration20per = fullTime * 80 / 100;
                long numberOFExpiration10per = fullTime * 90 / 100;
                long numberOFExpiration5per = fullTime * 95 / 100;
                long numberOFExpiration = numberOFExpirationDate;
                Date notify20per = new Date(contract.getCreatedTime().getTime() + numberOFExpiration20per);
                Date notify10per = new Date(contract.getCreatedTime().getTime() + numberOFExpiration10per);
                Date notify5per = new Date(contract.getCreatedTime().getTime() + numberOFExpiration5per);
                Date notifyExpired = new Date(contract.getExpirationTime().getTime());
                List<Date> listTask = new ArrayList<>();
                listTask.add(notify20per);
                listTask.add(notify10per);
                listTask.add(notify5per);
                listTask.add(notifyExpired);
                for (Date task : listTask) {
                    TaskScheduler taskScheduler = new TaskScheduler();
                    taskScheduler.setTime(task);
                    taskScheduler.setContractId(contract.getId());
                    taskSchedulerService.save(taskScheduler);
                }
        }catch (Exception e)
        {
            log.error(e.getMessage());
        }
    }
}
