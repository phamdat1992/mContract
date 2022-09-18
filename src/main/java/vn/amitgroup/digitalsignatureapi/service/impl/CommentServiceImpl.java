package vn.amitgroup.digitalsignatureapi.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.*;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.*;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.*;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;


@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    SignerService signerService;
    @Autowired
    UserService userService;
    @Autowired
    private SignerContractRepository signerContractRepository;
    @Autowired
    private SignerRepository signerRepository;
    @Autowired
    EmailSenderService emailSenderService;
    @Value("${my.email}")
    private String sendFrom;
    @Autowired
    private CompanyService companyService;
    @Value("${my.url.contract}")
    private String urlContract;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    ContractService contractService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    MarkAsReadCommentRepository markAsReadCommentRepository;
   

    @Override
    public List<ParentComment> getComment(String contractId) throws ApiException{
        List<ParentComment> parentComments= new ArrayList<>();
        List<Comment> comments= commentRepository.findByParentIdIsNullAndContract_Id(contractId);
        comments.sort((d1,d2) -> d1.getCreatedTime().compareTo(d2.getCreatedTime()));
        if(CollectionUtils.isEmpty(comments)){
            return new ArrayList<>() ;
        }
        if(comments.size()>0){
               
                comments.stream().forEach(c->{
                    ParentComment parentComment= new ModelMapper().map(c, ParentComment.class);
                    if(c.getUser()!=null){
                        UserForComment userForComment= new ModelMapper().map(c.getUser(), UserForComment.class);
                        userForComment.setAvatarPath(c.getUser().getAvatarPath());
                        parentComment.setUser(userForComment);
                    }
                    if(c.getSigner()!=null){
                        SignerForComment signerForComment= new ModelMapper().map(c.getSigner(), SignerForComment.class);
                        Signer currentSigner = signerRepository.getById(signerForComment.getId());
                        if(currentSigner.getUser()==null)
                        {
                            signerForComment.setAvatarPath(null);
                        }
                        else
                        {
                            signerForComment.setAvatarPath(currentSigner.getUser().getAvatarPath());
                        }
                        parentComment.setSigner(signerForComment);
                    }
                    MarkAsReadComment markAsReadComment = markAsReadCommentRepository.getMarkAsReadCommentByCommentId(c.getId(),UserUtil.email());
                    if (markAsReadComment==null)
                    {
                        parentComment.setIsRead(true);
                    }
                    else {
                        parentComment.setIsRead(markAsReadComment.getIsRead());
                    }
                    List<Comment> childComments= commentRepository.findByParentId(parentComment.getId());
                    if(childComments.size()>0){
                        List<ChildCommentDto> childCommentDtos=childComments.stream().map(child->{
                            ChildCommentDto childCommentDto= new ModelMapper().map(child, ChildCommentDto.class);
                            if(child.getUser()!=null){
                                UserForComment userForComment= new ModelMapper().map(child.getUser(), UserForComment.class);
                                userForComment.setAvatarPath(child.getUser().getAvatarPath());
                                childCommentDto.setUser(userForComment);
                            }
                            if(child.getSigner()!=null){
                                SignerForComment signerForComment= new ModelMapper().map(child.getSigner(), SignerForComment.class);
                                Signer currentSigner = signerRepository.getById(signerForComment.getId());
                                if(currentSigner.getUser()==null)
                                {
                                    signerForComment.setAvatarPath(null);
                                }
                                else
                                {
                                    signerForComment.setAvatarPath(currentSigner.getUser().getAvatarPath());
                                }
                                childCommentDto.setSigner(signerForComment);
                            }
                            MarkAsReadComment markAsReadCommentChild = markAsReadCommentRepository.getMarkAsReadCommentByCommentId(child.getId(),UserUtil.email());
                            if (markAsReadCommentChild==null)
                            {
                                childCommentDto.setIsRead(true);
                            }
                            else {
                                childCommentDto.setIsRead(markAsReadCommentChild.getIsRead());
                            }

                            return childCommentDto;
                        }).collect(Collectors.toList());
                        childCommentDtos.sort((d1,d2) -> d1.getCreatedTime().compareTo(d2.getCreatedTime()));
                        parentComment.setChildCommentDtos(childCommentDtos);
                    }
                    parentComments.add(parentComment);
                });
            }
            return parentComments;
    }

    @Override
    public CommentDto addCommentByLink(CommentForm commentForm, String token) throws ApiException{
        try {
            User userCreatedContract = signerContractRepository.getUserCreateContract(commentForm.getContractId());
            CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());

            Optional<Contract> contract = contractRepository.findById(commentForm.getContractId());
            if(!contract.isPresent()){
                throw ErrorCodeException.NullException();
            }
            Comment comment= new Comment();
            comment.setContent(commentForm.getContent());
            comment.setCreatedTime(new Date());
            comment.setX(commentForm.getX());
            comment.setParentId(commentForm.getParentId());
            comment.setY(commentForm.getY());
            comment.setPage(commentForm.getPage());
            List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(contract.get().getId());
            List<String> emailAccept = new ArrayList<>();
            for (Signer signerAccept : signerAcceptEmail) {
                emailAccept.add(signerAccept.getEmail());
            }
            Mail mail = new Mail();
            mail.setFrom(sendFrom);
            Map<String, Object> model = new HashMap<String, Object>();

            if(StringUtils.isNotBlank(token))
            {
                Signer signer = signerService.getSignerByToken(token);
                User user = userService.findByEmail(UserUtil.email());
                if(user==null)
                {
                    comment.setUser(null);
                }
                else {
                    comment.setUser(user);
                }
                comment.setSigner(signer);
                CompanyDto company = companyService.getByUser(signer.getEmail());
                if(companyCreated.getLogoPath()!=null)
                {
                    model.put("logo", String.valueOf(companyCreated.getLogoPath()));
                }
                model.put("companyName", String.valueOf(companyCreated.getName()));
                model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
                model.put("taxCode", String.valueOf(companyCreated.getTaxCode()));
                if(company!=null)
                {
                    model.put("userComment",String.valueOf((StringUtils.isBlank(signer.getFullName())|| signer.getFullName()==null)? " " : signer.getFullName()+" - "+company.getName()));
                }
                else
                {
                    model.put("userComment",String.valueOf((StringUtils.isBlank(signer.getFullName())|| signer.getFullName()==null)? " " : signer.getFullName()));
                }
                model.put("title", String.valueOf(contract.get().getTitle()));
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
                model.put("createdTime",String.valueOf(formatter.format(date)));
            }
            Optional<Integer> parentId = Optional.ofNullable(commentForm.getParentId());
            if(parentId!=null || parentId.isPresent())
            {
                comment.setParentId(commentForm.getParentId());
            }
            if(contract.isPresent()){
                comment.setContract(contract.get());
            }
            Comment entity= commentRepository.save(comment);
            mail.setSubject(companyCreated.getName()+ " - "+String.valueOf(contract.get().getTitle()));
            mail.setTemplateName("note");
            if(contract.get().getIsValidTaxcode())
            {
                for (Signer signer: signerRepository.findByContractId(commentForm.getContractId())) {
                    String tokenLink = jwtProvider.generateForSigner(signer.getEmail(), contract.get().getId());
                    SignerContract signerContract = signerContractRepository.findByContract_IdAndSigner_Email(contract.get().getId(),signer.getEmail());
                    if(!(signerService.getSignerByToken(token).getEmail().equals(signer.getEmail())))
                    {
                        MarkAsReadComment markAsReadComment = new MarkAsReadComment();
                        markAsReadComment.setEmail(signer.getEmail());
                        markAsReadComment.setCommentId(entity.getId());
                        markAsReadComment.setIsRead(false);
                        markAsReadComment.setParentId(entity.getParentId());
                        markAsReadComment.setContractId(contract.get().getId());
                        markAsReadCommentRepository.save(markAsReadComment);
                        String commentId = "?commentId="+entity.getId();
                        String url = urlContract + tokenLink + commentId;
                        model.put("urlContract", String.valueOf(url));
                        model.put("fullname",signer.getFullName()==null? " ": String.valueOf(signer.getFullName()));
                        mail.setProps(model);
                        mail.setMailTo(signer.getEmail());
                        if(emailAccept.contains(signer.getEmail())&& !signer.getEmail().equals(signerService.getSignerByToken(token).getEmail()) && !signerContract.getContractStatus().equals(ContractStatus.AUTHENTICATIONFAIL))
                        {
                            emailSenderService.sendEmailAsync(mail,true);
                        }
                    }
                    if(!(signerService.getSignerByToken(token).getEmail().equals(signer.getEmail())))
                    {
                        signerContract.setIsWatched(false);
                        signerContractRepository.save(signerContract);
                    }
                }
            }
            Contract result = contract.get();
            result.setUpdateTime(new Date());
            contractRepository.save(result);
            return  new ModelMapper().map(entity, CommentDto.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public CommentDto addComment(CommentForm commentForm) {
		try {
            User userCreatedContract = signerContractRepository.getUserCreateContract(commentForm.getContractId());
            CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());

            Contract contract = contractRepository.getByIdAndUserEmail(commentForm.getContractId(),UserUtil.email());
//            ContractDetailDto contract = contractService.getByUser(commentForm.getContractId(), UserUtil.email());
            if(contract==null){
                throw ErrorCodeException.NullException();
            }
			Comment comment= new Comment();
            comment.setContent(commentForm.getContent());
            comment.setCreatedTime(new Date());
            comment.setParentId(commentForm.getParentId());
            comment.setX(commentForm.getX());
            comment.setY(commentForm.getY());
            comment.setPage(commentForm.getPage());
            List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(contract.getId());
            List<String> emailAccept = new ArrayList<>();
            for (Signer signerAccept : signerAcceptEmail) {
                emailAccept.add(signerAccept.getEmail());
            }
            Mail mail = new Mail();
            mail.setFrom(sendFrom);

            Map<String, Object> model = new HashMap<String, Object>();

            if(UserUtil.email()!=null)
            {
                User user = userService.findByEmail(UserUtil.email());
                comment.setUser(user);

                CompanyDto companyUserComment = companyService.getByUser(UserUtil.email());
                if(companyCreated.getLogoPath()!=null)
                {
                    model.put("logo", String.valueOf(companyCreated.getLogoPath()));
                }
                model.put("companyName", String.valueOf(companyCreated.getName()));
                model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
                model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
                model.put("title", String.valueOf(contract.getTitle()));
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
                model.put("createdTime",String.valueOf(formatter.format(date)));
                String fullName = (StringUtils.isBlank(user.getFullName())|| user==null)? " " : user.getFullName();
                String companyNameComment  = " ";
                if(companyUserComment==null || companyUserComment.getName()==null)
                {
                    companyNameComment = " ";
                }
                else
                {
                    companyNameComment = companyUserComment.getName();
                }
                model.put("userComment",String.valueOf(fullName+" - "+companyNameComment));
            }
            Optional<Integer> parentId = Optional.ofNullable(commentForm.getParentId());
            if(parentId!=null || parentId.isPresent())
            {
                comment.setParentId(commentForm.getParentId());
            }
            if(contract!=null){
                comment.setContract(contract);
            }
            Comment entity= commentRepository.save(comment);
            mail.setSubject(companyCreated.getName() + " - "+String.valueOf(contract.getTitle()));
            mail.setTemplateName("note");
            SignerContract signerContract = signerContractRepository.findByContract_Id(contract.getId()).get(0);
            if(contract.getIsValidTaxcode())
            {
                for (Signer signer: signerRepository.findByContractId(commentForm.getContractId())) {
                    model.put("fullname",signer.getFullName()==null? " ": String.valueOf(signer.getFullName()));
                    String token = jwtProvider.generateForSigner(signer.getEmail(), contract.getId());
                    if(!(UserUtil.email().equals(signer.getEmail())))
                    {
                        MarkAsReadComment markAsReadComment = new MarkAsReadComment();
                        markAsReadComment.setEmail(signer.getEmail());
                        markAsReadComment.setCommentId(entity.getId());
                        markAsReadComment.setIsRead(false);
                        markAsReadComment.setParentId(entity.getParentId());
                        markAsReadComment.setContractId(contract.getId());
                        markAsReadCommentRepository.save(markAsReadComment);
                        String commentId = "?commentId="+entity.getId();
                        String url = urlContract + token + commentId;
                        model.put("urlContract", String.valueOf(url));
                        mail.setMailTo(signer.getEmail());
                        mail.setProps(model);
                        if(emailAccept.contains(signer.getEmail()) && !signer.getEmail().equals(UserUtil.email()) && !signerContract.getContractStatus().equals(ContractStatus.AUTHENTICATIONFAIL))
                        {
                            emailSenderService.sendEmailAsync(mail,true);
                        }
                    }
                    if(!(UserUtil.email().equals(signer.getEmail())))
                    {
                        signerContract.setIsWatched(false);
                        signerContractRepository.save(signerContract);
                    }
                }
            }
            Contract result = contract;
            result.setUpdateTime(new Date());
            contractRepository.save(result);
            return  new ModelMapper().map(entity, CommentDto.class);
		} catch (Exception e) {
			log.error(e.getMessage());
            throw ErrorCodeException.NullException();
		}
    }

    @Override
    public List<ParentComment> getBySigner(String token) {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        List<ParentComment> parentComments= new ArrayList<>();
        List<Comment> comments= commentRepository.findByParentIdIsNullAndContract_Id(signerInfo.getContracId());
        if(CollectionUtils.isEmpty(comments)){
            return new ArrayList<>() ;
        }
        if(comments.size()>0){
               
                comments.stream().forEach(c->{
                    ParentComment parentComment= new ModelMapper().map(c, ParentComment.class);
                    if(c.getUser()!=null){
                        UserForComment userForComment= new ModelMapper().map(c.getUser(), UserForComment.class);
                        userForComment.setAvatarPath(c.getUser().getAvatarPath());
                        parentComment.setUser(userForComment);
                    }
                    if(c.getSigner()!=null){
                        SignerForComment signerForComment= new ModelMapper().map(c.getSigner(), SignerForComment.class);
                        Signer currentSigner = signerRepository.getById(signerForComment.getId());
                        if(currentSigner.getUser()==null)
                        {
                            signerForComment.setAvatarPath(null);
                        }
                        else
                        {
                            signerForComment.setAvatarPath(currentSigner.getUser().getAvatarPath());
                        }
//                        User currentUser = userService.findByEmail(currentSigner.getEmail());
//                        if(currentUser==null)
//                        {
//                        }
//                        if(c.getUser()==null && currentUser!=null)
//                        {
//                            UserForComment userForComment= new ModelMapper().map(currentUser, UserForComment.class);
//                            userForComment.setAvatarPath(currentUser.getAvatarPath());
//                            parentComment.setUser(userForComment);
//                        }
                        parentComment.setSigner(signerForComment);
                    }
                    MarkAsReadComment markAsReadComment = markAsReadCommentRepository.getMarkAsReadCommentByCommentId(c.getId(),signerInfo.getSignerEmail());
                    if (markAsReadComment==null)
                    {
                        parentComment.setIsRead(true);
                    }
                    else {
                        parentComment.setIsRead(markAsReadComment.getIsRead());
                    }
                    List<Comment> childComments= commentRepository.findByParentId(parentComment.getId());
                    if(childComments.size()>0){
                        List<ChildCommentDto> childCommentDtos=childComments.stream().map(child->{
                            ChildCommentDto childCommentDto= new ModelMapper().map(child, ChildCommentDto.class);
                            if(child.getUser()!=null){
                                UserForComment userForComment= new ModelMapper().map(child.getUser(), UserForComment.class);
                                childCommentDto.setUser(userForComment);
                            }
                            if(child.getSigner()!=null){
                                SignerForComment signerForComment= new ModelMapper().map(child.getSigner(), SignerForComment.class);
                                Signer currentSigner = signerRepository.getById(signerForComment.getId());
                                if(currentSigner.getUser()==null)
                                {
                                    signerForComment.setAvatarPath(null);
                                }
                                else
                                {
                                    signerForComment.setAvatarPath(currentSigner.getUser().getAvatarPath());
                                }
                                childCommentDto.setSigner(signerForComment);
                            }
                            MarkAsReadComment markAsReadCommentChild = markAsReadCommentRepository.getMarkAsReadCommentByCommentId(child.getId(),signerInfo.getSignerEmail());
                            if (markAsReadCommentChild==null)
                            {
                                childCommentDto.setIsRead(true);
                            }
                            else {
                                childCommentDto.setIsRead(markAsReadCommentChild.getIsRead());
                            }
                            return childCommentDto;
                        }).collect(Collectors.toList());
                        parentComment.setChildCommentDtos(childCommentDtos);
                    }
                    parentComments.add(parentComment);
                });
            }
            return parentComments;
    }

    @Override
    public Boolean markAsReadComment(String email,Integer commentId) throws ApiException{
        MarkAsReadComment markAsReadComment = markAsReadCommentRepository.getMarkAsReadCommentByCommentId(commentId, email);
        if(markAsReadComment==null)
        {
            throw ErrorCodeException.NullException();
        }
        markAsReadComment.setIsRead(true);
        markAsReadCommentRepository.save(markAsReadComment);
        return true;
    }
}
