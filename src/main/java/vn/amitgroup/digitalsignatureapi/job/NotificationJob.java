package vn.amitgroup.digitalsignatureapi.job;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import vn.amitgroup.digitalsignatureapi.consummer.ChatModule;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.entity.*;
import vn.amitgroup.digitalsignatureapi.job.info.TimerInfo;
import vn.amitgroup.digitalsignatureapi.repository.*;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.CompanyService;
import vn.amitgroup.digitalsignatureapi.service.ContractService;
import vn.amitgroup.digitalsignatureapi.service.EmailSenderService;
import vn.amitgroup.digitalsignatureapi.service.UserService;
import vn.amitgroup.digitalsignatureapi.service.impl.TaskSchedulerService;
import vn.amitgroup.digitalsignatureapi.timerservice.NotificationService;
import vn.amitgroup.digitalsignatureapi.timerservice.SchedulerService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationJob.class);
    @Value("${my.email}")
    private String sendFrom;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    TaskSchedulerService taskSchedulerService;
    @Autowired
    private SignerContractRepository signerContractRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    SignerRepository signerRepository;
    @Value("${my.url.contract}")
    private String urlContract;
    @Autowired
    CompanyService companyService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    ChatModule chatModule;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserService userService;
    @Autowired
    ContractService contractService;
    @Autowired
    private SchedulerService scheduler;
    @Autowired
    MarkAsReadRepository markAsReadRepository;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
        TaskScheduler taskScheduler =  taskSchedulerService.getAll().get(0);//get tash thực hiện gần nhất dưới db
        Contract contract = contractRepository.findById(taskScheduler.getContractId()).get();
        List<Signer> signers =  signerRepository.findByContractId(contract.getId());
        int countAllSigner = signers.size();
        List<String> signerSigned = signerRepository.findAllByIdAndStatus(contract.getId());
        int countAllSigned = signerSigned.size();
        String emailCreator = signerContractRepository.getUserCreateContract(contract.getId()).getEmail();
        CompanyDto companyCreated = companyService.getByUser(emailCreator);
        Mail mail = new Mail();
        mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.getTitle()));
        mail.setFrom(sendFrom);
        Map<String, Object> model = new HashMap<String, Object>();
        SimpleDateFormat DateFor = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        model.put("title", String.valueOf(contract.getTitle()));

        ContractAddDto contractAddDto = new ModelMapper().map(contract,ContractAddDto.class);
        User user = userService.findByEmail(emailCreator);
        NotifyRealTime notify = new NotifyRealTime();

        notify.setType("EXPIRE");
        notify.setStatus(false);
        notify.setId(UUID.randomUUID());
        notify.setContractId(contractAddDto.getId());
        notify.setContractName(contractAddDto.getTitle());
        notify.setContractContent(contractAddDto.getContent());
        notify.setUserId(user.getId());
        notify.setUserName(user.getFullName());
        notify.setUserEmail(user.getEmail());
        notify.setUserAvatar(user.getAvatarPath());
        notify.setCreatedTime(new Date());
        notify.setExpiredTime(contract.getExpirationTime());

        List<String> listClient = new ArrayList<>();

        List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(contract.getId());
        List<String> emailAccept = new ArrayList<>();
        for (Signer signerAccept : signerAcceptEmail) {
            emailAccept.add(signerAccept.getEmail());
        }
        for (Signer mailRecipient:signers) {
            if(companyCreated!=null)
            {
                if(companyCreated.getLogoPath()!=null)
                {
                    model.put("logo", String.valueOf(companyCreated.getLogoPath()));
                }
                model.put("companyName", String.valueOf(companyCreated.getName()));
                model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
                model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
            }
            model.put("title", String.valueOf(contract.getTitle()));
            model.put("fullname", StringUtils.isBlank(mailRecipient.getFullName()) ?"":String.valueOf(" "+mailRecipient.getFullName()));
            model.put("createdTime", String.valueOf(DateFor.format(contract.getCreatedTime())));
            model.put("expiredTime", String.valueOf(DateFor.format(contract.getExpirationTime())));
            model.put("signerNumber", String.valueOf(countAllSigned +"/"+ countAllSigner));
            String token = jwtProvider.generateForSigner(mailRecipient.getEmail(), contract.getId());
            String url = urlContract + token;
            model.put("urlContract", String.valueOf(url));
            mail.setMailTo(mailRecipient.getEmail());

            SignerContract signerContract1 = signerContractRepository.findByContract_IdAndSigner_Email(contract.getId(),mailRecipient.getEmail());
            Boolean signerContractCancel = signerContract1.getContractStatus().equals(ContractStatus.CANCEL);
            Boolean signerContractFailed = signerContract1.getContractStatus().equals(ContractStatus.AUTHENTICATIONFAIL);
            if(emailAccept.contains(mailRecipient.getEmail()) && emailAccept.size()>0 && !(countAllSigner == countAllSigned) && !signerContractCancel && !signerContractFailed)
            {
                if(countAllSigned<1)
                {
                    mail.setTemplateName("date");
                }
                else if(countAllSigned>0) {
                    if (signerSigned.contains(mailRecipient.getEmail())) {
                        model.put("signedTime", String.valueOf(DateFor.format(signerContract1.getSignedTime())));
                        mail.setTemplateName("date_2");
                    } else {
                        mail.setTemplateName("date_1");
                    }
                }
                mail.setProps(model);
                emailSenderService.sendEmail(mail);
            }
//            chatModule.joinRoom(mailRecipient.getEmail(),mailRecipient.getEmail());
            listClient.add(mailRecipient.getEmail());
            SignerContract signerContractUpdateStatus = signerContractRepository.findByContract_IdAndSigner_Email(contract.getId(),mailRecipient.getEmail());
            signerContractUpdateStatus.setIsWatched(false);
            if(!(countAllSigner == countAllSigned) && !signerContract1.getContractStatus().equals(ContractStatus.CANCEL)) {
                signerContractRepository.save(signerContractUpdateStatus);
            }

            MarkAsRead markAsRead = new MarkAsRead();
            markAsRead.setNotificationId(notify.getId());
            markAsRead.setType("EXPIRE");
            markAsRead.setSignerId(mailRecipient.getId());
            markAsRead.setEmail(mailRecipient.getEmail());
            markAsRead.setContractId(contract.getId());
            markAsRead.setIsRead(false);
            if(!(countAllSigner == countAllSigned) && !signerContract1.getContractStatus().equals(ContractStatus.CANCEL))
            {
                markAsReadRepository.save(markAsRead);
            }

        }
        Boolean signerContractStatusCancel = signerContractRepository.findByContract_IdAndSigner_Email(contract.getId(),emailCreator).equals(ContractStatus.CANCEL);
        if(!(countAllSigner == countAllSigned) && !signerContractStatusCancel) {
            chatModule.sendNotify(listClient, notify);
        }

        taskSchedulerService.delete(taskScheduler);// xóa tash hiện tại dưới db vì đã gửi mail thông báo xong
        TaskScheduler taskSchedulerContinuous = taskSchedulerService.getAll().get(0);// get tash mới nhất dưới db tiếp theo
        notificationService.deleteTimer("NotificationJob");
        Notification notification = new ModelMapper().map(notify,Notification.class);
        if(!(countAllSigner == countAllSigned) && !signerContractStatusCancel) {
            notificationRepository.save(notification);
        }
        if(notificationService.getAllRunningTimers().size()<1)
        {
            Date now = new Date();
            long millisecond = taskSchedulerContinuous.getTime().getTime() - now.getTime();
            if(millisecond<0)
            {
                millisecond=5000;
            }
            final TimerInfo info = new TimerInfo();
            info.setTotalFireCount(1);
            info.setInitialOffsetMs(millisecond);
            info.setCallbackData("My callback");
            ContractDetailDto contractTaskContinuous = contractService.getContractById(taskSchedulerContinuous.getContractId());
            if(taskSchedulerContinuous.getTime().compareTo(contractTaskContinuous.getExpirationTime())<0)
            {
                scheduler.schedule(NotificationJob.class,info);
            }
            else
            {
                scheduler.schedule(NotificationJobExpired.class,info);
            }
        }
        LOG.info("task tiếp theo: "+ notificationService.getAllRunningTimers().get(0).getInitialOffsetMs());
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            applicationContext.getBean("timeTaskCurrent");
        }
    }
}
