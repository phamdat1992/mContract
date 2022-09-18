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
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
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
public class NotificationJobExpired implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationJob.class);
    @Value("${my.email}")
    private String sendFrom;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    TaskSchedulerService taskSchedulerService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    SignerRepository signerRepository;
    @Autowired
    SignerContractRepository signerContractRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Value("${my.url.contract}")
    private String urlContract;
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
            TaskScheduler taskScheduler = taskSchedulerService.getAll().get(0);
            Contract contract = contractRepository.findById(taskScheduler.getContractId()).get();
            List<Signer> signers = signerRepository.findByContractId(contract.getId());
            int countAllSigner = signers.size();
            int countAllSigned = signerRepository.findAllByIdAndStatus(contract.getId()).size();
            List<SignerContract> signerContract = signerContractRepository.findByContract_Id(taskScheduler.getContractId());
            signerContract.stream().forEach(sc -> {
                if(!sc.getContractStatus().equals(ContractStatus.CANCEL) && !sc.getContractStatus().equals(ContractStatus.AUTHENTICATIONFAIL))
                {
                    sc.setContractStatus(ContractStatus.EXPIRED);
                }
            });
            contract.setUpdateTime(new Date());
            if(!(countAllSigner == countAllSigned)) {
                signerContractRepository.saveAll(signerContract);
                contractRepository.save(contract);
            }
            String emailCreator = signerContractRepository.getUserCreateContract(contract.getId()).getEmail();
            CompanyDto companyCreated = companyService.getByUser(emailCreator);
            CompanyDto company = new CompanyDto();
            Mail mail = new Mail();
            mail.setFrom(sendFrom);
            mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.getTitle()));
            Map<String, Object> model = new HashMap<String, Object>();
            SimpleDateFormat DateFor = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
            mail.setTemplateName("expired");
            model.put("title", String.valueOf(contract.getTitle()));

            ContractAddDto contractAddDto = new ModelMapper().map(contract, ContractAddDto.class);

            User user = userService.findByEmail(emailCreator);
            NotifyRealTime notify = new NotifyRealTime();

            notify.setType("EXPIRED");
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
            for (Signer mailRecipient : signers) {
                if (companyCreated != null) {
                    if(companyCreated.getLogoPath()!=null)
                    {
                        model.put("logo", String.valueOf(companyCreated.getLogoPath()));
                    }
                    model.put("companyName", String.valueOf(companyCreated.getName()));
                    model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
                    model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
                }
                model.put("title", String.valueOf(contract.getTitle()));
                model.put("fullname", StringUtils.isBlank(mailRecipient.getFullName()) ? " " : String.valueOf(mailRecipient.getFullName()));
                model.put("createdTime", String.valueOf(DateFor.format(contract.getCreatedTime())));
                model.put("expiredTime", String.valueOf(DateFor.format(contract.getExpirationTime())));
                String token = jwtProvider.generateForSigner(mailRecipient.getEmail(), contract.getId());
                String url = urlContract + token;
                model.put("urlContract", String.valueOf(url));
                mail.setMailTo(mailRecipient.getEmail());
                mail.setProps(model);
                SignerContract signerContract1 = signerContractRepository.findByContract_IdAndSigner_Email(contract.getId(),mailRecipient.getEmail());
                Boolean signerContractCancel = signerContract1.getContractStatus().equals(ContractStatus.CANCEL);
                Boolean signerContractFailed = signerContract1.getContractStatus().equals(ContractStatus.AUTHENTICATIONFAIL);
                try {

                    if(emailAccept.contains(mailRecipient.getEmail()) && emailAccept.size()>0 && !(countAllSigner == countAllSigned) && !signerContractCancel && !signerContractFailed)
                    {
                        emailSenderService.sendEmail(mail);
                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                chatModule.joinRoom(mailRecipient.getEmail(), mailRecipient.getEmail());
                listClient.add(mailRecipient.getEmail());
                MarkAsRead markAsRead = new MarkAsRead();
                markAsRead.setNotificationId(notify.getId());
                markAsRead.setType("EXPIRED");
                markAsRead.setSignerId(mailRecipient.getId());
                markAsRead.setEmail(mailRecipient.getEmail());
                markAsRead.setContractId(contract.getId());
                markAsRead.setIsRead(false);
                if(!(countAllSigner == countAllSigned) && !signerContractCancel) {
                markAsReadRepository.save(markAsRead);
                }
                SignerContract signerContractUpdateStatus = signerContractRepository.findByContract_IdAndSigner_Email(contract.getId(),mailRecipient.getEmail());
                signerContractUpdateStatus.setIsWatched(false);
                if(!(countAllSigner == countAllSigned) && !signerContractCancel) {
                    signerContractRepository.save(signerContractUpdateStatus);
                }
            }
            Boolean signerContractStatusCancel = signerContractRepository.findByContract_IdAndSigner_Email(contract.getId(),emailCreator).getContractStatus().equals(ContractStatus.CANCEL);
            if(!(countAllSigner == countAllSigned) && !signerContractStatusCancel) {
                chatModule.sendNotify(listClient, notify);
            }
            Notification notification = new ModelMapper().map(notify, Notification.class);
            if(!(countAllSigner == countAllSigned) && !signerContractStatusCancel) {
                notificationRepository.save(notification);
            }

            taskSchedulerService.delete(taskScheduler);//xoa db vì đã thực thi xong
            TaskScheduler taskSchedulerContinuous = taskSchedulerService.getAll().get(0);// get tash tiếp theo dưới db
            notificationService.deleteTimer("NotificationJobExpired");
            if (notificationService.getAllRunningTimers().size() < 1) {
                Date now = new Date();
                long millisecond = taskSchedulerContinuous.getTime().getTime() - now.getTime();
                if (millisecond < 0) {
                    millisecond = 5000;
                }
                final TimerInfo info = new TimerInfo();
                info.setTotalFireCount(1);
                info.setInitialOffsetMs(millisecond);
                info.setCallbackData("My callback");
                ContractDetailDto contractTaskContinuous = contractService.getContractById(taskSchedulerContinuous.getContractId());
                if (taskSchedulerContinuous.getTime().compareTo(contractTaskContinuous.getExpirationTime()) < 0) {
                    scheduler.schedule(NotificationJob.class, info);
                } else {
                    scheduler.schedule(NotificationJobExpired.class, info);
                }
            }
            LOG.info("task tiếp theo: " + notificationService.getAllRunningTimers().get(0).getInitialOffsetMs());
        }catch (ApiException e) {
            applicationContext.getBean("timeTaskCurrent");
        }
    }
}
