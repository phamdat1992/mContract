package vn.amitgroup.digitalsignatureapi.scheduled;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import vn.amitgroup.digitalsignatureapi.service.TokenIsDestroyService;

@Component
public class ScheduledTasks {
    @Autowired
    private TokenIsDestroyService destroyService;
   
    @Scheduled(fixedRate = 3600*1000*24)
    @Transactional
    public void clearToken(){
        destroyService.clear();
    }
}
