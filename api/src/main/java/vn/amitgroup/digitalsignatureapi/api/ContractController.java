package vn.amitgroup.digitalsignatureapi.api;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import vn.amitgroup.digitalsignatureapi.consummer.ChatModule;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.*;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.job.NotificationJob;
import vn.amitgroup.digitalsignatureapi.job.NotificationJobExpired;
import vn.amitgroup.digitalsignatureapi.job.info.TimerInfo;
import vn.amitgroup.digitalsignatureapi.repository.*;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.ContractService;
import vn.amitgroup.digitalsignatureapi.service.MarkAsReadService;
import vn.amitgroup.digitalsignatureapi.service.SignerService;
import vn.amitgroup.digitalsignatureapi.service.UserService;
import vn.amitgroup.digitalsignatureapi.service.impl.TaskSchedulerService;
import vn.amitgroup.digitalsignatureapi.timerservice.NotificationService;
import vn.amitgroup.digitalsignatureapi.timerservice.SchedulerService;
import vn.amitgroup.digitalsignatureapi.utils.SignerUtil;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/contracts")
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Value("${my.email}")
    private String sendFrom;
    @Value("${contract.email.subject}")
    private String subject;
    @Value("${my.key}")
    private String key;
    @Autowired
    NotificationService notificationService;
    @Autowired
    TaskSchedulerService taskSchedulerService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ChatModule chatModule;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    UserService userService;
    @Autowired
    SignerContractRepository signerContractRepository;
    @Autowired
    SignerService signerService;
    @Autowired
    SignerRepository signerRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    private SchedulerService scheduler;
    @Autowired
    MarkAsReadRepository markAsReadRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    MarkAsReadService markAsReadService;

    @Operation(summary = "search list contract", description = "it has dynamic filter, you can add or remove filter,defaultValue page size Integer.MAX_VALUE")
    @GetMapping()
    public ResponseEntity<BaseResponse<List<ContractDto>>> getList(@RequestParam(value = "search",defaultValue = "") String search,
            @RequestParam(value = "partner",defaultValue = "") String partner,
            @RequestParam(value = "tagId",defaultValue = "0") Integer tagId,
            @RequestParam(value = "topic",defaultValue = "") String topic,
            @Parameter(description = "format yyyy-mm-dd", example = "2021-12-26") @RequestParam(value = "fromDate",defaultValue = "") String fromDate,
            @Parameter(description = "format yyyy-mm-dd", example = "2021-12-26") @RequestParam(value = "toDate",defaultValue = "") String toDate,
            @Parameter(description = "there are 2 options CREATER , GUEST", example = "CREATER") @RequestParam(value = "type",defaultValue = "") String type,
            @Parameter(description = "there are 6 options PROCESSING,WAITINGFORPARTNER,AUTHENTICATIONFAIL,COMPLETE,EXPIRED,CANCEL", example = "WAITINGFORPARTNER") @RequestParam(value = "status",defaultValue = "") String status,
            @Parameter(description = "there are 2 options ASC , DESC", example = "ASC") @RequestParam(value = "sortByDate",defaultValue = "") String sortByDate,
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "size", defaultValue = "0") Integer size) {
        Pageable paging = null;
        if (size == 0) {
            paging = PageRequest.of(currentPage, Integer.MAX_VALUE);
        } else {
            paging = PageRequest.of(currentPage, size);
        }
        Map<String, String> map = new HashMap<>();
        map.put("search", search);
        map.put("partner", partner);
        map.put("topic", topic);
        map.put("status", status);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        map.put("type", type);
        map.put("sortByDate", sortByDate);
        if(tagId!=0){
            map.put("tagId", tagId.toString());
        }
        Page<ContractDto> page = contractService.search(map, paging, UserUtil.email());
        BaseResponse<List<ContractDto>> result = new BaseResponse<>();
        result.setData(page.getContent());
        result.setPageIndex(page.getNumber());
        result.setTotal(page.getTotalElements());
        result.setTotalPage(page.getTotalPages());
        result.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "statistics by contract status")
    @GetMapping("/statistical-datas")
    public ResponseEntity<BaseResponse<List<StatisticalData>>> getStatisticalData() {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(contractService.getStatisticalData(UserUtil.email()), HttpStatus.OK.value()));
    }
    @Operation(summary = "get detail contract")
    @GetMapping("/{contractId}/user")
    public ResponseEntity<BaseResponse<ContractDetailDto>> getContractDetail(@PathVariable("contractId") String contractId) {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(contractService.getByUser(contractId,UserUtil.email()), HttpStatus.OK.value()));
    }
    @Operation(summary = "get detail contract")
    @GetMapping("/signer")
    public ResponseEntity<BaseResponse<ContractDetailDto>> getContractDetailForSigner(HttpServletRequest request) {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(contractService.getBySigner(SignerUtil.getToken(request)), HttpStatus.OK.value()));
    }
    @Operation(summary = "add a new contract")
    @PostMapping()
    public ResponseEntity<BaseResponse<ContractAddDto>> add(@RequestPart @Valid List<SignerForm> signers,@ModelAttribute ContractRequest contractReq) throws Exception {
        ContractFrom contract = new ContractFrom();
        contract.setTitle(contractReq.getTitle());
        contract.setContent(contractReq.getContent());
        contract.setNumberOFExpirationDate(contractReq.getNumberOFExpirationDate());
        contract.setFile(contractReq.getFile());
        contract.setSigners(signers);
        ContractAddDto contractAddDto =  contractService.add(contract, UserUtil.email(),contract.getFile());
//        contractService.uploadForContract(contract.getFile(), contractAddDto, UserUtil.email());
        if(contractAddDto.getIsValidTaxcode())
        {
            notificationService.setTime(contractAddDto.getId(),contract.getNumberOFExpirationDate());
            TaskScheduler taskScheduler =  taskSchedulerService.getAll().get(0);//task som nhất dưới db

            if(!CollectionUtils.isEmpty(notificationService.getAllRunningTimers()))
            {
                notificationService.deleteTimer("NotificationJob");
                notificationService.deleteTimer("NotificationJobExpired");
                Date now = new Date();
                long millisecond = taskScheduler.getTime().getTime() - now.getTime();
                if(millisecond<0)
                {
                    millisecond=5000;
                }
                final TimerInfo info = new TimerInfo();
                info.setTotalFireCount(1);
                info.setInitialOffsetMs(millisecond);
                info.setCallbackData("My callback");
                try {
                    ContractDetailDto contractTask = contractService.getContractById(taskScheduler.getContractId());
                    if(taskScheduler.getTime().compareTo(contractTask.getExpirationTime())<0)
                    {
                        scheduler.schedule(NotificationJob.class,info);
                    }
                    else
                    {
                        scheduler.schedule(NotificationJobExpired.class,info);
                    }
                }catch(Exception e)
                {
                    e.getMessage();
                }
            }

            UserDto user = new ModelMapper().map(userService.findByEmail(UserUtil.email()), UserDto.class);


            NotifyRealTime notify = new NotifyRealTime();
            notify.setId(UUID.randomUUID());
            notify.setType("NEWCONTRACT");
            notify.setContractId(contractAddDto.getId());
            notify.setStatus(false);
            notify.setContractName(contractAddDto.getTitle());
            notify.setContractContent(contractAddDto.getContent());
            notify.setUserId(user.getId());
            notify.setUserName(user.getFullName());
            notify.setUserEmail(user.getEmail());
            notify.setUserAvatar(user.getAvatarPath());
            notify.setCreatedTime(new Date());
            List<Signer> listSigner = signerRepository.findByContractId(contractAddDto.getId());
            List<String> listClient = new ArrayList<>();
            for (Signer signer :listSigner) {
                if(!(signer.getEmail().equals(UserUtil.email())))
                {
//                    chatModule.joinRoom(signer.getEmail(),signer.getEmail());
                    listClient.add(signer.getEmail());
                    MarkAsRead markAsRead = new MarkAsRead();
                    markAsRead.setNotificationId(notify.getId());
                    markAsRead.setType("NEWCONTRACT");
                    markAsRead.setEmail(signer.getEmail());
                    markAsRead.setSignerId(signer.getId());
                    markAsRead.setContractId(contractAddDto.getId());
                    markAsRead.setIsRead(false);
                    markAsReadRepository.save(markAsRead);
                }
            }
            Notification notification = new ModelMapper().map(notify,Notification.class);
            notificationRepository.save(notification);
            chatModule.sendNotify(listClient,notify);
        }
        return ResponseEntity.ok().body(new BaseResponse<>(contractAddDto, HttpStatus.OK.value()));
    }

    // @Operation(summary = "get contract by id")
    // @GetMapping("/{id}")
    // public ResponseEntity<BaseResponse<ContractAddDto>> getById(@PathVariable("id") Integer id) {
    //     return ResponseEntity.ok().body(new BaseResponse<>(contractService.getById(id), HttpStatus.OK.value()));
    // }

    @Operation(summary = "update is watched status for contract", description = "status = true is watched")
    @PutMapping("/watched")
    public ResponseEntity<BaseResponse<String>> updateWatched(@RequestBody @Valid WatchedForm form) {
        contractService.updateWatched(UserUtil.email(), form);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseResponse<String>> delete(@RequestBody @Valid DeleteContractForm form) {
        contractService.delete(form.getContractIdList(),UserUtil.email());
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @Operation(summary = "delete contract by ")
    @PostMapping("/sign")
    public ResponseEntity<BaseResponse<String>> sign(@RequestBody @Valid SignForm form) {
        contractService.checkSigning(form.getContractId(),UserUtil.email());
        contractService.updateSigning(form.getContractId(),UserUtil.email(),true);
       
        try {
            contractService.sign(form,UserUtil.email());
        } catch (Exception e) {
            throw new ApiException(e);
        }
       finally{
        contractService.updateSigning(form.getContractId(),UserUtil.email(),false);
       }

        ContractDetailDto contractDetailDto =  contractService.getContractById(form.getContractId());
        SignerContract signerContract = signerContractRepository.findByContract_Id(form.getContractId()).get(0);

        if(contractDetailDto==null)
        {
            throw ErrorCodeException.NullException();
        }
        User user = userService.findByEmail(UserUtil.email());
        Signer currentSigner  = signerRepository.findByEmailAndContractId(UserUtil.email(),form.getContractId());

        NotifyRealTime notify = new NotifyRealTime();
        notify.setId(UUID.randomUUID());
        if(signerContract.getContractStatus().equals(ContractStatus.AUTHENTICATIONFAIL))
        {
            notify.setType("AUTHENTICATIONFAIL");
        }
        else
        {
            notify.setType("SIGN");
        }
        notify.setStatus(false);
        notify.setContractId(contractDetailDto.getId());
        notify.setContractName(contractDetailDto.getTitle());
        notify.setContractContent(contractDetailDto.getContent());
        notify.setUserId(user.getId());
        notify.setUserName(user.getFullName());
        notify.setUserEmail(user.getEmail());
        notify.setUserAvatar(user.getAvatarPath());
        notify.setCreatedTime(new Date());
        notify.setSignerId(currentSigner.getId());

        List<Signer> signers = signerRepository.findByContractId(contractDetailDto.getId());
        List<String> listClient = new ArrayList<>();
        for (Signer signer : signers) {
            if(!(signer.getEmail().equals(UserUtil.email()))) {
//                chatModule.joinRoom(signer.getEmail()+key+contractDetailDto.getId(),signer.getEmail()+key+contractDetailDto.getId());
                listClient.add(signer.getEmail()+key+contractDetailDto.getId());
                MarkAsRead markAsRead = new MarkAsRead();
                markAsRead.setNotificationId(notify.getId());
                markAsRead.setType(notify.getType());
                markAsRead.setEmail(signer.getEmail());
                markAsRead.setSignerId(signer.getId());
                markAsRead.setContractId(form.getContractId());
                markAsRead.setAvatarPath(notify.getUserAvatar());
                markAsRead.setIsRead(false);
                markAsReadRepository.save(markAsRead);
            }
//            chatModule.joinRoom(signer.getEmail(), signer.getEmail());
            listClient.add(signer.getEmail());
        }
        Notification notification = new ModelMapper().map(notify,Notification.class);
        notificationRepository.save(notification);
        chatModule.sendNotify(listClient,notify);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PostMapping("/sign/signer")
    public ResponseEntity<BaseResponse<String>> signForSigner(@RequestBody @Valid SignForSignerForm form,HttpServletRequest request) throws Exception {

        contractService.checkSigning(SignerUtil.getToken(request));
        contractService.updateSigning(SignerUtil.getToken(request),true);
        try {
            contractService.checkSigner(form,SignerUtil.getToken(request));
        } catch (Exception e) {
            throw new ApiException(e);
        }
       finally{
        contractService.updateSigning(SignerUtil.getToken(request),false);
       }
        ContractDetailDto contractDetailDto = contractService.getBySigner(SignerUtil.getToken(request));

        if(contractDetailDto==null)
        {
            throw ErrorCodeException.NullException();
        }

        SignerDto signerDto = signerService.getSigner(SignerUtil.getToken(request));
        User user = userService.findByEmail(signerDto.getEmail());
        if(user==null){}
        SignerContract signerContract = signerContractRepository.findByContract_Id(contractDetailDto.getId()).get(0);

        NotifyRealTime notify = new NotifyRealTime();
        notify.setId(UUID.randomUUID());
        if(!signerContract.getContractStatus().equals(ContractStatus.AUTHENTICATIONFAIL))
        {
//            notify.setType("AUTHENTICATIONFAIL");
//        }
//        else
//        {
            notify.setType("SIGN");
            notify.setStatus(false);
            notify.setContractId(contractDetailDto.getId());
            notify.setContractName(contractDetailDto.getTitle());
            notify.setContractContent(contractDetailDto.getContent());
            notify.setSignerId(signerDto.getId());
            notify.setSignerFullName(signerDto.getFullName());
            notify.setSignerEmail(signerDto.getEmail());
            notify.setCreatedTime(new Date());
            if(user!=null)
            {
                notify.setUserAvatar(user.getAvatarPath());
            }

            List<Signer> signers = signerRepository.findByContractId(contractDetailDto.getId());
            List<String> listClient = new ArrayList<>();
            Signer currentSigner = signerService.getSignerByToken(SignerUtil.getToken(request));
            for (Signer signer : signers) {
                if(!(signer.getEmail().equals(currentSigner.getEmail()))) {
//                chatModule.joinRoom(signer.getEmail() + key + contractDetailDto.getId(), signer.getEmail() + key + contractDetailDto.getId());
                    listClient.add(signer.getEmail()+key+contractDetailDto.getId());
                    MarkAsRead markAsRead = new MarkAsRead();
                    markAsRead.setNotificationId(notify.getId());
                    markAsRead.setType(notify.getType());
                    markAsRead.setSignerId(signer.getId());
                    markAsRead.setEmail(signer.getEmail());
                    markAsRead.setContractId(contractDetailDto.getId());
                    markAsRead.setIsRead(false);
                    markAsRead.setAvatarPath(notify.getUserAvatar());
                    markAsReadRepository.save(markAsRead);
                }
//            chatModule.joinRoom(signer.getEmail(), signer.getEmail());
                listClient.add(signer.getEmail());
            }
            Notification notification = new ModelMapper().map(notify,Notification.class);
            notificationRepository.save(notification);
            chatModule.sendNotify(listClient,notify);
        }
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PostMapping("/tags")
    public ResponseEntity<BaseResponse<String>> addTagForContract(@RequestBody @Valid TagContractForm form) {
        contractService.addTagForContract(form);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PostMapping("/data-to-sign")
    public ResponseEntity<BaseResponse<DataToSignResponse>> getDataToSign(@RequestBody @Valid DataToSignForm form) throws Exception {
        contractService.checkSigning(form.getContractId(),UserUtil.email());
        return ResponseEntity.ok().body(new BaseResponse<>(contractService.getDatoToSign(form,UserUtil.email()), HttpStatus.OK.value()));
    }
    @PostMapping("/data-to-sign/signer")
    public ResponseEntity<BaseResponse<DataToSignResponse>> getDataToSignSigner(@RequestBody @Valid DataToSignForm form,HttpServletRequest request) throws Exception {
        contractService.checkSigning(SignerUtil.getToken(request));
        return ResponseEntity.ok().body(new BaseResponse<>(contractService.getDatoToSignForSigner(form,SignerUtil.getToken(request)), HttpStatus.OK.value()));
    }
    @DeleteMapping("/tags")
    public ResponseEntity<BaseResponse<String>> removeTagForContract(@RequestBody @Valid TagContractForm form) {
        contractService.removeTagForContract(form);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PutMapping("/{contractId}/cancel")
    public ResponseEntity<BaseResponse<String>> cancelContractForUser(@PathVariable("contractId") String contractId) throws IOException, MessagingException {
        contractService.cancelContractForUser(contractId);

        User user = userService.findByEmail(UserUtil.email());
        ContractDetailDto contractCancel = contractService.getContractById(contractId);

        NotifyRealTime notify = new NotifyRealTime();
        notify.setId(UUID.randomUUID());
        notify.setType("CANCEL");
        notify.setStatus(false);
        notify.setContractId(contractCancel.getId());
        notify.setContractName(contractCancel.getTitle());
        notify.setContractContent(contractCancel.getContent());
        notify.setUserId(user.getId());
        notify.setUserName(user.getFullName());
        notify.setUserEmail(user.getEmail());
        notify.setUserAvatar(user.getAvatarPath());
        notify.setCreatedTime(new Date());

        List<Signer> signers = signerRepository.findByContractId(contractCancel.getId());
        List<String> listClient = new ArrayList<>();
        for (Signer signer : signers) {
            if(!(signer.getEmail().equals(UserUtil.email()))) {
//                chatModule.joinRoom(signer.getEmail()+key+contractCancel.getId(),signer.getEmail()+key+contractCancel.getId());
//                chatModule.joinRoom(signer.getEmail(), signer.getEmail());
                listClient.add(signer.getEmail()+key+contractCancel.getId());
                listClient.add(signer.getEmail());
                MarkAsRead markAsRead = new MarkAsRead();
                markAsRead.setNotificationId(notify.getId());
                markAsRead.setType("CANCEL");
                markAsRead.setEmail(signer.getEmail());
                markAsRead.setSignerId(signer.getId());
                markAsRead.setContractId(contractId);
                markAsRead.setIsRead(false);
                markAsReadRepository.save(markAsRead);
            }
        }
        Notification notification = new ModelMapper().map(notify,Notification.class);
        notificationRepository.save(notification);
        chatModule.sendNotify(listClient,notify);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PutMapping("/signer/cancel")
    public ResponseEntity<BaseResponse<String>> cancelContractForSigner(HttpServletRequest request) throws IOException, MessagingException {
        contractService.cancelContractForSigner(request);
        SignerDto signerDto = signerService.getSigner(SignerUtil.getToken(request));
        ContractDetailDto contractCancel = contractService.getBySigner(SignerUtil.getToken(request));

        NotifyRealTime notify = new NotifyRealTime();
        notify.setId(UUID.randomUUID());
        notify.setType("CANCEL");
        notify.setStatus(false);
        notify.setContractId(contractCancel.getId());
        notify.setContractName(contractCancel.getTitle());
        notify.setContractContent(contractCancel.getContent());
        notify.setSignerId(signerDto.getId());
        notify.setSignerFullName(signerDto.getFullName());
        notify.setSignerEmail(signerDto.getEmail());
        notify.setCreatedTime(new Date());

        List<Signer> signers = signerRepository.findByContractId(contractCancel.getId());
        List<String> listClient = new ArrayList<>();
        for (Signer signer : signers) {
            if(!(signer.getEmail().equals(signerService.getSigner(SignerUtil.getToken(request)).getEmail()))) {
//                chatModule.joinRoom(signer.getEmail() + key + contractCancel.getId(), signer.getEmail() + key + contractCancel.getId());
//                chatModule.joinRoom(signer.getEmail(), signer.getEmail());
                listClient.add(signer.getEmail());
                listClient.add(signer.getEmail()+key+contractCancel.getId());
                MarkAsRead markAsRead = new MarkAsRead();
                markAsRead.setNotificationId(notify.getId());
                markAsRead.setType("CANCEL");
                markAsRead.setSignerId(signer.getId());
                markAsRead.setEmail(signer.getEmail());
                markAsRead.setContractId(contractCancel.getId());
                markAsRead.setIsRead(false);
                markAsReadRepository.save(markAsRead);
            }
        }
        Notification notification = new ModelMapper().map(notify,Notification.class);
        notificationRepository.save(notification);
        chatModule.sendNotify(listClient,notify);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PutMapping("/cancel")
    public ResponseEntity<BaseResponse<String>> cancelMultiple(@RequestBody @Valid ContractCancelForm form) throws IOException, MessagingException {
        contractService.cancelContract(form.getContractList(),UserUtil.email());
        User user = userService.findByEmail(UserUtil.email());
        for (String contractId : form.getContractList()) {
            ContractDetailDto contractCancel = contractService.getContractById(contractId);
            NotifyRealTime notify = new NotifyRealTime();
            notify.setId(UUID.randomUUID());
            notify.setType("CANCEL");
            notify.setStatus(false);
            notify.setContractId(contractCancel.getId());
            notify.setContractName(contractCancel.getTitle());
            notify.setContractContent(contractCancel.getContent());
            notify.setUserId(user.getId());
            notify.setUserName(user.getFullName());
            notify.setUserEmail(user.getEmail());
            notify.setUserAvatar(user.getAvatarPath());
            notify.setCreatedTime(new Date());

            List<Signer> signers = signerRepository.findByContractId(contractCancel.getId());
            List<String> listClient = new ArrayList<>();
            for (Signer signer : signers) {
                if(!(signer.getEmail().equals(UserUtil.email()))) {
//                    chatModule.joinRoom(signer.getEmail()+key+contractCancel.getId(),signer.getEmail()+key+contractCancel.getId());
//                    chatModule.joinRoom(signer.getEmail(), signer.getEmail());
                    listClient.add(signer.getEmail()+key+contractCancel.getId());
                    listClient.add(signer.getEmail());
                    MarkAsRead markAsRead = new MarkAsRead();
                    markAsRead.setNotificationId(notify.getId());
                    markAsRead.setType("CANCEL");
                    markAsRead.setEmail(signer.getEmail());
                    markAsRead.setSignerId(signer.getId());
                    markAsRead.setContractId(contractId);
                    markAsRead.setIsRead(false);
                    markAsReadRepository.save(markAsRead);
                }
            }
            Notification notification = new ModelMapper().map(notify,Notification.class);
            notificationRepository.save(notification);
            chatModule.sendNotify(listClient,notify);
        }
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PutMapping("/{contractId}/invalid-taxcode")
    public ResponseEntity<BaseResponse<String>> updateAuthenticationFail(@PathVariable("contractId") String contractId) {
        contractService.updateAuthenticationFail(contractId,UserUtil.email());
        ContractDetailDto contractDetailDto =  contractService.getContractById(contractId);

        if(contractDetailDto==null)
        {
            throw ErrorCodeException.NullException();
        }
        User user = userService.findByEmail(UserUtil.email());
        String emailCreator = signerContractRepository.getUserCreateContract(contractId).getEmail();
        Contract checkExistContract = contractRepository.getByIdAndUserEmail(contractDetailDto.getId(),UserUtil.email());
        if(checkExistContract==null)
        {
        }
        else {
            List<Notification> listNotificationDel =notificationRepository.getByContractId(checkExistContract.getId());
            if(listNotificationDel!=null)
            {
                notificationRepository.deleteAll(listNotificationDel);
            }
            List<MarkAsRead> listMarkAsReadDel = markAsReadRepository.getMarkAsReadByContractId(checkExistContract.getId());
            for (MarkAsRead del : listMarkAsReadDel) {
                if(del.getType()=="NEWCONTRACT")
                {
                    markAsReadRepository.delete(del);
                }
            }
        }

        NotifyRealTime notify = new NotifyRealTime();
        notify.setId(UUID.randomUUID());
        notify.setType("AUTHENTICATIONFAIL");
        notify.setStatus(false);
        notify.setContractId(contractDetailDto.getId());
        notify.setContractName(contractDetailDto.getTitle());
        notify.setContractContent(contractDetailDto.getContent());
        notify.setUserId(user.getId());
        notify.setUserName(user.getFullName());
        notify.setUserEmail(user.getEmail());
        notify.setUserAvatar(user.getAvatarPath());
        notify.setCreatedTime(new Date());

        List<Signer> signers = signerRepository.findByContractId(contractDetailDto.getId());
        List<String> listClient = new ArrayList<>();
        for (Signer signer : signers) {
            if(!(signer.getEmail().equals(UserUtil.email()))) {
//                chatModule.joinRoom(signer.getEmail()+key+contractDetailDto.getId(),signer.getEmail()+key+contractDetailDto.getId());
                listClient.add(signer.getEmail()+key+contractDetailDto.getId());
                MarkAsRead markAsRead = new MarkAsRead();
                markAsRead.setNotificationId(notify.getId());
                markAsRead.setType(notify.getType());
                markAsRead.setEmail(signer.getEmail());
                markAsRead.setSignerId(signer.getId());
                markAsRead.setContractId(contractId);
                markAsRead.setIsRead(false);
                markAsReadRepository.save(markAsRead);
            }
//            chatModule.joinRoom(signer.getEmail(), signer.getEmail());
            listClient.add(signer.getEmail());
        }
        Notification notification = new ModelMapper().map(notify,Notification.class);
        notificationRepository.save(notification);
        chatModule.sendNotify(listClient,notify);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
    @PutMapping("/signer/invalid-taxcode")
    public ResponseEntity<BaseResponse<String>> updateAuthenticationFail(HttpServletRequest request) {
        contractService.updateAuthenticationFail(SignerUtil.getToken(request));
        ContractDetailDto contractDetailDto = contractService.getBySigner(SignerUtil.getToken(request));
        SignerInfo signerInfo = jwtProvider.getSignerInfo(SignerUtil.getToken(request));
        Contract checkExistContract = contractRepository.findByIdAndSignersEmail(contractDetailDto.getId(),signerInfo.getSignerEmail());
        if(checkExistContract==null)
        {
        }
        else {
                List<Notification> listNotificationDel =notificationRepository.getByContractId(checkExistContract.getId());
                if(listNotificationDel!=null)
                {
                    notificationRepository.deleteAll(listNotificationDel);
                }
                List<MarkAsRead> listMarkAsReadDel = markAsReadRepository.getMarkAsReadByContractId(checkExistContract.getId());
                for (MarkAsRead del : listMarkAsReadDel) {
                    if(del.getType()=="NEWCONTRACT")
                    {
                        markAsReadRepository.delete(del);
                    }
                }
        }
        if(contractDetailDto==null)
        {
            throw ErrorCodeException.NullException();
        }
        SignerDto signerDto = signerService.getSigner(SignerUtil.getToken(request));

        NotifyRealTime notify = new NotifyRealTime();
        notify.setId(UUID.randomUUID());
        notify.setType("AUTHENTICATIONFAIL");
        notify.setStatus(false);
        notify.setContractId(contractDetailDto.getId());
        notify.setContractName(contractDetailDto.getTitle());
        notify.setContractContent(contractDetailDto.getContent());
        notify.setSignerId(signerDto.getId());
        notify.setSignerFullName(signerDto.getFullName());
        notify.setSignerEmail(signerDto.getEmail());
        notify.setCreatedTime(new Date());

        List<Signer> signers = signerRepository.findByContractId(contractDetailDto.getId());
        List<String> listClient = new ArrayList<>();
        for (Signer signer : signers) {
            if(!(signer.getEmail().equals(signerInfo.getSignerEmail()))) {
                listClient.add(signer.getEmail()+key+contractDetailDto.getId());
                MarkAsRead markAsRead = new MarkAsRead();
                markAsRead.setNotificationId(notify.getId());
                markAsRead.setType(notify.getType());
                markAsRead.setSignerId(signer.getId());
                markAsRead.setEmail(signer.getEmail());
                markAsRead.setContractId(contractDetailDto.getId());
                markAsRead.setIsRead(false);
                markAsReadRepository.save(markAsRead);
            }
            listClient.add(signer.getEmail());
        }
        Notification notification = new ModelMapper().map(notify,Notification.class);
        notificationRepository.save(notification);
        chatModule.sendNotify(listClient,notify);
        return ResponseEntity.ok().body(new BaseResponse<>());
    }
}
