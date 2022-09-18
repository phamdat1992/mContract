package vn.amitgroup.digitalsignatureapi.api;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.amazonaws.util.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import vn.amitgroup.digitalsignatureapi.consummer.ChatModule;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.*;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.*;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.*;
import vn.amitgroup.digitalsignatureapi.utils.SignerUtil;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    ContractService contractService;
    @Autowired
    ChatModule chatModule;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ContractRepository contractRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    SignerRepository signerRepository;
    @Value("${my.key}")
    private String key;
    @Autowired
    SignerService signerService;
    @Autowired
    MarkAsReadRepository markAsReadRepository;
    @Autowired
    MarkAsReadCommentRepository markAsReadCommentRepository;
    @Autowired
    JwtProvider jwtProvider;


    @PostMapping()
    public ResponseEntity<BaseResponse<CommentDto>> add(@RequestBody @Valid CommentForm dto){
        CommentDto newComment = commentService.addComment(dto);

        NotifyRealTime notify = new NotifyRealTime();
        Comment comment = commentRepository.getById(newComment.getId());
        if(comment==null)
        {
            throw ErrorCodeException.NullException();
        }

        UserDto user = new ModelMapper().map(comment.getUser(), UserDto.class);
        if(user==null)
        {
            notify.setUserAvatar(null);
        }
        else
        {
            notify.setUserAvatar(user.getAvatarPath());
        }
        ContractAddDto contractAddDto =new ModelMapper().map(comment.getContract(), ContractAddDto.class);

        if(comment.getContract().getIsValidTaxcode())
        {
            notify.setType("COMMENT");
            notify.setStatus(false);
            notify.setId(UUID.randomUUID());
            notify.setCommentId(newComment.getId());
            notify.setCommentContent(newComment.getContent());
            notify.setCommentX(newComment.getX());
            notify.setCommentY(newComment.getY());
            notify.setCommentParentId(newComment.getParentId());
            notify.setCommentPage(newComment.getPage());
            notify.setContractId(contractAddDto.getId());
            notify.setContractName(contractAddDto.getTitle());
            notify.setContractContent(contractAddDto.getContent());
            notify.setUserId(user.getId());
            notify.setCommentCreatedTime(comment.getCreatedTime());
            notify.setUserName(user.getFullName());
            notify.setUserEmail(user.getEmail());
            notify.setUserAvatar(user.getAvatarPath());
            notify.setCreatedTime(new Date());

            List<Signer> signers =signerRepository.findByContractId(contractAddDto.getId());
            if(signers.size()<1|| signers==null)
            {
                throw ErrorCodeException.NullException();
            }
            if(CollectionUtils.isNullOrEmpty(signers))
            {
                throw ErrorCodeException.NullException();
            }
            List<String> listClient = new ArrayList<>();
            for (Signer signer :signers) {
                if(!(signer.getEmail().equals(UserUtil.email())))
                {
//                    chatModule.joinRoom(signer.getEmail()+key+dto.getContractId(),signer.getEmail()+key+dto.getContractId());
//                    chatModule.joinRoom(signer.getEmail(),signer.getEmail());
                    listClient.add(signer.getEmail()+key+dto.getContractId());
                    listClient.add(signer.getEmail());
                    MarkAsRead markAsRead = new MarkAsRead();
                    markAsRead.setNotificationId(notify.getId());
                    markAsRead.setType("COMMENT");
                    markAsRead.setEmail(signer.getEmail());
                    markAsRead.setCommentId(newComment.getId());
                    markAsRead.setSignerId(signer.getId());
                    markAsRead.setContractId(dto.getContractId());
                    markAsRead.setIsRead(false);
                    markAsRead.setAvatarPath(notify.getUserAvatar());
                    markAsReadRepository.save(markAsRead);
                }
            }
            Notification notification = new ModelMapper().map(notify,Notification.class);
            notificationRepository.save(notification);
            if(listClient.size()>0)
            {
                chatModule.sendNotify(listClient,notify);
            }
        }
        return ResponseEntity.ok().body(new BaseResponse<>(newComment,HttpStatus.OK.value()) );
    }
    @PostMapping("/signer")
    public ResponseEntity<BaseResponse<CommentDto>> add(@RequestBody @Valid CommentForm dto, HttpServletRequest request){
        CommentDto newComment = commentService.addCommentByLink(dto,SignerUtil.getToken(request));
        NotifyRealTime notify = new NotifyRealTime();
        Comment comment = commentRepository.getById(newComment.getId());

        Signer signer1  = comment.getSigner();
        SignerInfo signerInfo = jwtProvider.getSignerInfo(SignerUtil.getToken(request));
        User user = userService.findByEmail(signerInfo.getSignerEmail());
        if(user==null)
        {
            notify.setUserAvatar(null);
        }
        else
        {
            notify.setUserAvatar(user.getAvatarPath());
        }
        ContractAddDto contractAddDto =new ModelMapper().map(comment.getContract(), ContractAddDto.class);

        notify.setId(UUID.randomUUID());
        notify.setType("COMMENT");
        notify.setStatus(false);
        notify.setCommentId(newComment.getId());
        notify.setCommentContent(newComment.getContent());
        notify.setCommentX(newComment.getX());
        notify.setCommentY(newComment.getY());
        notify.setCommentParentId(newComment.getParentId());
        notify.setCommentPage(newComment.getPage());
        notify.setContractId(contractAddDto.getId());
        notify.setContractName(contractAddDto.getTitle());
        notify.setContractContent(contractAddDto.getContent());
        notify.setSignerId(signer1.getId());
        notify.setCommentCreatedTime(comment.getCreatedTime());
        notify.setSignerFullName(signer1.getFullName());
        notify.setSignerEmail(signer1.getEmail());
        notify.setCreatedTime(new Date());

        ContractDetailDto contractDetailDto = contractService.getContractById(contractAddDto.getId());
        List<Signer> signers =signerRepository.findByContractId(contractAddDto.getId());
        if(signers.size()<1|| signers==null)
        {
            throw ErrorCodeException.NullException();
        }
        if(contractDetailDto==null)
        {
            throw ErrorCodeException.NullException();
        }
        List<String> listClient = new ArrayList<>();
        for (Signer signer :signers) {
            if(!(signer.getEmail().equals(signerService.getSigner(SignerUtil.getToken(request)).getEmail())))
            {
//                chatModule.joinRoom(signer.getEmail()+key+dto.getContractId(),signer.getEmail()+key+dto.getContractId());
//                chatModule.joinRoom(signer.getEmail(),signer.getEmail());
                listClient.add(signer.getEmail()+key+dto.getContractId());
                listClient.add(signer.getEmail());
                MarkAsRead markAsRead = new MarkAsRead();
                markAsRead.setNotificationId(notify.getId());
                markAsRead.setType("COMMENT");
                markAsRead.setEmail(signer.getEmail());
                markAsRead.setCommentId(newComment.getId());
                markAsRead.setSignerId(signer.getId());
                markAsRead.setContractId(dto.getContractId());
                markAsRead.setIsRead(false);
                markAsRead.setAvatarPath(notify.getUserAvatar());
                markAsReadRepository.save(markAsRead);
            }
        }
        Notification notification = new ModelMapper().map(notify,Notification.class);
        notificationRepository.save(notification);
        if(listClient.size()>0)
        {
            chatModule.sendNotify(listClient,notify);
        }
        return ResponseEntity.ok().body(new BaseResponse<>(newComment,HttpStatus.OK.value()));
    }
    @GetMapping(value = "/contract/{contractId}")
    public ResponseEntity<BaseResponse<List<ParentComment>>> get(@PathVariable("contractId") String contractId){
        return ResponseEntity.ok().body(new BaseResponse<>(commentService.getComment(contractId),HttpStatus.OK.value()) );
    }
    @Operation(summary = "get comment for signer")
    @GetMapping("/signer")
    public ResponseEntity<BaseResponse<List<ParentComment>>> getContractDetailForSigner(HttpServletRequest request) {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(commentService.getBySigner(SignerUtil.getToken(request)), HttpStatus.OK.value()));
    }

    @Operation(summary = "Mark comment as read for user")
    @PutMapping("/mark/{commentId}")
    public ResponseEntity<BaseResponse<Boolean>> markCommentAsReadForUser(@PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(commentService.markAsReadComment(UserUtil.email(),commentId), HttpStatus.OK.value()));
    }

    @Operation(summary = "Mark comment as read for user")
    @PutMapping("/signer/mark/{commentId}")
    public ResponseEntity<BaseResponse<Boolean>> markCommentAsReadForSigner(HttpServletRequest request,@PathVariable("commentId") Integer commentId) {
        return ResponseEntity.ok()
                .body(new BaseResponse<>(commentService.markAsReadComment(signerService.getEmaiByTokenSigner(SignerUtil.getToken(request)),commentId), HttpStatus.OK.value()));
    }

}
