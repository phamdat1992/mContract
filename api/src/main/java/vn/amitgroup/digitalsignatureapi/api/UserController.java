package vn.amitgroup.digitalsignatureapi.api;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.dto.response.BaseResponse;
import vn.amitgroup.digitalsignatureapi.entity.MarkAsRead;
import vn.amitgroup.digitalsignatureapi.entity.Notification;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.repository.ContractRepository;
import vn.amitgroup.digitalsignatureapi.service.MarkAsReadService;
import vn.amitgroup.digitalsignatureapi.service.NotificationService;
import vn.amitgroup.digitalsignatureapi.service.UserService;
import vn.amitgroup.digitalsignatureapi.utils.SignerUtil;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
        @Autowired
        private UserService userService;
        @Autowired
        NotificationService notificationService;
        @Autowired
        ContractRepository contractRepository;
        @Autowired
        MarkAsReadService markAsReadService;
      
        @PutMapping()
        public ResponseEntity<BaseResponse<UserDto>> update(@Valid @RequestBody UserProfile user) {
                return ResponseEntity.ok().body(new BaseResponse<>(userService.update(user,UserUtil.email()), HttpStatus.OK.value()));
        }
        @Operation(summary = "get profile user is signing")
        @GetMapping("/profile")
        public ResponseEntity<BaseResponse<UserJwt>> getUserByToken() {
                return ResponseEntity.ok().body(new BaseResponse<>(userService.getUserByToken(UserUtil.email()), HttpStatus.OK.value()));
        }
        @Operation(summary = "accept email notification", description = "status=true is accept")
        @PutMapping(value = "/notification")
        public ResponseEntity<BaseResponse<UserDto>> acceptEmailNotification(@RequestBody @Valid EmailNotificationStatus emailNotificationStatus) {
                return ResponseEntity.ok().body(new BaseResponse<>(userService.updateAcceptEmailNo(emailNotificationStatus.getStatus(),UserUtil.email()), HttpStatus.OK.value()));
        }
        @GetMapping("/notification")
        public ResponseEntity<BaseResponse<List<Notification>>> getAllNotifyByUser()
        {
                User user = userService.findByEmail(UserUtil.email());
                List<String> listContractId = contractRepository.getAllContractByUser(user.getId());
                return ResponseEntity.ok().body(new BaseResponse<>(notificationService.getAllNotificationByContract(listContractId), HttpStatus.OK.value()));
        }
        @PutMapping("/notification/status")
        public ResponseEntity<BaseResponse<Boolean>> checkWatchedNotification(@RequestParam String type)
        {
                User user = userService.findByEmail(UserUtil.email());
                notificationService.updateStatusWatched(user.getId(),type);
                return ResponseEntity.ok().body(new BaseResponse<>(Boolean.TRUE, HttpStatus.OK.value()));
        }
        @PutMapping("/notification/{notificationId}")
        public ResponseEntity<BaseResponse<Boolean>> checkWatchedNotification(@PathVariable UUID notificationId)
        {
                notificationService.updateWatched(notificationId);
                return ResponseEntity.ok().body(new BaseResponse<>(Boolean.TRUE, HttpStatus.OK.value()));
        }

        @Operation(summary = "Get the list of notifications (type is COMMENT and SIGN) for user")
        @GetMapping("/notification/{contractId}")
        public  ResponseEntity<BaseResponse<List<Notification>>> getAllNotifyUnReadForUser(@PathVariable("contractId") String contractId, @RequestParam(required = false) Boolean isRead)
        {
                return ResponseEntity.ok()
                        .body(new BaseResponse<>(markAsReadService.getMarkAsByUser(UserUtil.email(),contractId,isRead), HttpStatus.OK.value()));
        }

        @Operation(summary = "Get the list of notifications (type is COMMENT and SIGN) for signer")
        @GetMapping("/notification/signer")
        public  ResponseEntity<BaseResponse<List<Notification>>> getAllNotifyUnReadForSigner(HttpServletRequest request,@RequestParam(required = false) Boolean isRead)
        {
                return ResponseEntity.ok()
                        .body(new BaseResponse<>(markAsReadService.getMarkAsBySigner(SignerUtil.getToken(request),isRead), HttpStatus.OK.value()));
        }

        @Operation(summary = "Mark notification as read for signer")
        @PostMapping("/notification/signer/{notificationId}")
        public ResponseEntity<BaseResponse<Boolean>> markReadNotificationForSigner(HttpServletRequest request, @PathVariable("notificationId") UUID notificationId) {
                return ResponseEntity.ok()
                        .body(new BaseResponse<>(markAsReadService.markReadNotificationBySigner(SignerUtil.getToken(request),notificationId), HttpStatus.OK.value()));
        }

        @Operation(summary = "Mark notification as read for user")
        @PostMapping("/notification/{contractId}/{notificationId}")
        public ResponseEntity<BaseResponse<Boolean>> markReadNotificationForUser(@PathVariable("contractId") String contractId, @PathVariable("notificationId") UUID notificationId) {
                return ResponseEntity.ok()
                        .body(new BaseResponse<>(markAsReadService.markReadNotificationByUser(UserUtil.email(),contractId,notificationId), HttpStatus.OK.value()));
        }

        @Operation(summary = "Mark all notification as read for user")
        @PostMapping("/notification")
        public ResponseEntity<BaseResponse<Boolean>> markAllReadNotificationForUser(@RequestBody TypeRequest type) {
                return ResponseEntity.ok()
                        .body(new BaseResponse<>(markAsReadService.markAllNotificationByUser(UserUtil.email(),type.getType()), HttpStatus.OK.value()));
        }
        @Operation(summary = "Get all notification for user")
        @GetMapping("/notify")
        public ResponseEntity<BaseResponse<List<Notification>>> getAllNotificationForUser(@RequestParam(required = false) String type, @RequestParam(required = false) Integer currentPage, @RequestParam(required = false) Integer size) {
                return ResponseEntity.ok()
                        .body(markAsReadService.getAllNotification(UserUtil.email(),type,currentPage,size));
        }

        @Operation(summary = "Count all notification for user")
        @GetMapping("/notificationNumbers")
        public ResponseEntity<BaseResponse<List<MarkAsRead>>> countAllNotificationForUser() {
                return ResponseEntity.ok()
                        .body(markAsReadService.countAllNotification(UserUtil.email()));
        }


}
