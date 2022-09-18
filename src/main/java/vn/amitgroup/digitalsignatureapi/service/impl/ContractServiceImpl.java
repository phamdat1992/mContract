package vn.amitgroup.digitalsignatureapi.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.spi.x509.tsp.TSPSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import eu.europa.esig.dss.alert.ExceptionOnStatusAlert;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import lombok.extern.slf4j.Slf4j;
import vn.amitgroup.digitalsignatureapi.dto.*;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.entity.*;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;
import vn.amitgroup.digitalsignatureapi.exception.ErrorCodeException;
import vn.amitgroup.digitalsignatureapi.repository.*;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.CompanyService;
import vn.amitgroup.digitalsignatureapi.service.ContractService;
import vn.amitgroup.digitalsignatureapi.service.EmailSenderService;
import vn.amitgroup.digitalsignatureapi.service.S3FileService;
import vn.amitgroup.digitalsignatureapi.utils.ConvertUtil;
import vn.amitgroup.digitalsignatureapi.utils.DateUtil;
import vn.amitgroup.digitalsignatureapi.utils.S3Utils;
import vn.amitgroup.digitalsignatureapi.utils.SignatureUtils;
import vn.amitgroup.digitalsignatureapi.utils.SignerUtil;
import vn.amitgroup.digitalsignatureapi.utils.UploadUtils;
import vn.amitgroup.digitalsignatureapi.utils.UserUtil;

@Service
@Slf4j
public class ContractServiceImpl implements ContractService {
    @Value("${s3.Properties.bucketName}")
    private String bucketName;
    @Autowired
    private CustomContractRepository customContractRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private SignerContractRepository signerContractRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ContractTagRepository contractTagRepository;
    @Autowired
    private SignerRepository signerRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RootCaRepository rootCaRepository;
    @Autowired
    private S3FileService s3FileService;
    @Autowired
    private TSPSource tspSource;
    @Autowired
    private CommonsDataLoader commonsDataLoader;
    @Autowired
    private OnlineCRLSource onlineCRLSource;
    @Autowired
    private OnlineOCSPSource onlineOCSPSource;
    @Autowired
    S3Utils s3Utils;
    @Autowired
    ResourceLoader resourceLoader;
    @Value("${s3.Properties.expiredMilliSecondTime}")
    private Long expiredMilliSecondTime;
    @Autowired
    private JwtProvider jwtProvider;
    @Value("${my.email}")
    private String sendFrom;
    @Value("${my.url.contract}")
    private String urlContract;
    @Value("${my.domain.upload}")
    private String uploadPath;
    @Value("${my.domain.download}")
    private String downloadUrl;
    private static String outdir = System.getProperty("user.dir") + "/upload";
    @Autowired
    CompanyService companyService;
    @Autowired
    private S3FileRepository s3FileRepository;

    @Override
    @Transactional
    public Page<ContractDto> search(Map<String, String> map, Pageable page, String email) {
        Page<ContractDto> list = customContractRepository.search(map, page, email);
        return list;
    }

    @Override
    public List<StatisticalData> getStatisticalData(String email) {
        log.info("get statistical data ");
        try {
            Map<String, Integer> map = new HashMap<>();
            map.put(ContractStatus.AUTHENTICATIONFAIL.name(), 0);
            map.put(ContractStatus.PROCESSING.name(), 0);
            map.put(ContractStatus.WAITINGFORPARTNER.name(), 0);
            map.put(ContractStatus.COMPLETE.name(), 0);
            map.put(ContractStatus.EXPIRED.name(), 0);
            map.put(ContractStatus.CANCEL.name(), 0);
            List<Object[]> list = contractRepository.getStatisticalData(email);
            List<StatisticalData> result = new ArrayList<>();
            list.stream().forEach(o -> {
                map.put(o[1].toString(), Integer.valueOf(o[0].toString()));
            });
            map.forEach((k, v) -> {
                StatisticalData data = new StatisticalData();
                data.setStatus(k);
                data.setCount(v);
                result.add(data);
            });
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return null;
    }

    private void sendMail(String email, Contract contract, Integer numberOFExpirationDate, User userCreated) {
        
        CompanyDto companyOfCreator = companyService.getByUser(userCreated.getEmail());
        Signer signer = signerRepository.findByEmailAndContractId(email, contract.getId());
        companyService.getByUser(signer.getEmail());
        Mail mail = new Mail();
        mail.setFrom(sendFrom);
        mail.setMailTo(email);
        mail.setSubject(companyOfCreator.getName() + " - " + contract.getTitle());
        mail.setTemplateName("newctr");
        Map<String, Object> model = new HashMap<String, Object>();
        SimpleDateFormat DateFor = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        String logo =null;
        if (companyOfCreator.getLogoPath() != null) {
            logo = companyOfCreator.getLogoPath();
        }
        model.put("logo", logo);
        model.put("fullname", signer.getFullName()==null?" ":String.valueOf(signer.getFullName()));
        model.put("title", String.valueOf(contract.getTitle()));
        model.put("companyName", String.valueOf(companyOfCreator.getName()));
        model.put("address", companyOfCreator.getAddress()==null? " ": String.valueOf(companyOfCreator.getAddress()));
        model.put("taxCode", String.valueOf("MST " + companyOfCreator.getTaxCode()));
        model.put("createdTime", String.valueOf(DateFor.format(contract.getCreatedTime())));
        int totalMonth = DateUtil.differenceInMonths(contract.getCreatedTime(), contract.getExpirationTime());
        if (totalMonth < 599) {
            model.put("expiredTime", String.valueOf(DateFor.format(contract.getExpirationTime())));
        }
        String token = jwtProvider.generateForSigner(email, contract.getId());
        String url = urlContract + token;
        model.put("urlContract", String.valueOf(url));
        mail.setProps(model);
        try {
            emailSenderService.sendEmail(mail);
        } catch (MessagingException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ContractAddDto add(ContractFrom contract, String email,MultipartFile file) throws Exception {
        Contract entity = new Contract();
        entity.setTitle(contract.getTitle());
        entity.setNormalTitle(ConvertUtil.convertStringToNormalized(contract.getTitle()));
        entity.setContent(contract.getContent());
        entity.setNormalContent(ConvertUtil.convertStringToNormalized(contract.getContent()));
        entity.setIsSign(false);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, contract.getNumberOFExpirationDate());
        entity.setExpirationTime(cal.getTime());
        entity.setCreatedTime(new Date());
        entity.setUpdateTime(new Date());
        boolean validTaxcode = true;
        for (SignerForm s : contract.getSigners()) {
            User user = userRepository.findByEmailAndInvalidTaxCode(s.getEmail(), s.getTaxCode());
            if (user != null) {
                validTaxcode = false;
                break;
            }
        }
        entity.setIsValidTaxcode(validTaxcode);
        Contract result = contractRepository.save(entity);
        User userCreate = userRepository.findByEmail(email);
        for (SignerForm s : contract.getSigners()) {
            Signer signerUser = new Signer();
            if (s.getEmail().equals(email)) {
                signerUser = signerRepository.findByEmailAndTaxCodeAndFullName(s.getEmail(), s.getTaxCode(),
                        s.getFullName());
                if (signerUser != null) {
                    Company company = companyRepository.findByUser_Email(s.getEmail());
                    if (signerUser.getUser() == null) {
                        if (company != null) {
                            User user = userRepository.findByEmail(s.getEmail());
                            signerUser.setCompanyName(company.getName());

                            signerUser.setUser(user);
                            signerRepository.save(signerUser);
                        }
                    } else {
                        if (signerUser.getCompanyName() == null) {
                            signerUser.setCompanyName(company.getName());
                            signerRepository.save(signerUser);
                        }
                    }
                } else {
                    Company company = companyRepository.findByUser_Email(s.getEmail());
                    if (company == null) {
                        throw ErrorCodeException.NullException();
                    }
                    User user = userRepository.findByEmail(s.getEmail());
                    signerUser = new Signer();
                    signerUser.setEmail(s.getEmail());
                    signerUser.setFullName(s.getFullName());
                    signerUser.setNormalName(ConvertUtil.convertStringToNormalized(s.getFullName()));
                    signerUser.setTaxCode(s.getTaxCode());
                    signerUser.setUser(user);
                    signerUser.setCompanyName(company.getName());
                    signerRepository.save(signerUser);
                }
                SignerContract signerContract = new SignerContract();
                if (validTaxcode) {
                    signerContract.setContractStatus(ContractStatus.WAITINGFORPARTNER);
                } else {
                    signerContract.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
                }
                signerContract.setIsDelected(false);
                signerContract.setIsSigned(false);
                signerContract.setIsWatched(false);
                signerContract.setX(s.getX());
                signerContract.setY(s.getY());
                signerContract.setPage(s.getPage());
                signerContract.setSigner(signerUser);
                signerContract.setContract(result);
                signerContract.setContractRole(ContractRole.CREATER);
                signerContract.setCreatedTime(new Date());
                signerContract.setEmailSigner(s.getEmail());
                signerContractRepository.save(signerContract);
            } else {
                signerUser = signerRepository.findByEmailAndTaxCodeAndFullName(s.getEmail(), s.getTaxCode(),
                        s.getFullName());
                if (signerUser != null) {
                    SignerContract signerContract = new SignerContract();
                    if (validTaxcode) {
                        signerContract.setContractStatus(ContractStatus.WAITINGFORPARTNER);
                    } else {
                        signerContract.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
                    }
                    signerContract.setIsDelected(false);
                    signerContract.setIsSigned(false);
                    signerContract.setIsWatched(false);
                    signerContract.setX(s.getX());
                    signerContract.setY(s.getY());
                    signerContract.setPage(s.getPage());
                    User user = userRepository.findByEmail(s.getEmail());
                    if (user != null) {
                        Company company = companyRepository.findByUser_Email(s.getEmail());
                        if (company != null) {
                            signerUser.setCompanyName(company.getName());
                            if (validTaxcode) {
                                signerUser.setUser(user);
                            }
                            signerRepository.save(signerUser);
                        }
                    } else {
                        if (StringUtils.isNotBlank(signerUser.getCompanyName())) {
                            signerUser.setCompanyName(signerUser.getCompanyName());
                            signerRepository.save(signerUser);
                        }
                    }
                    signerContract.setSigner(signerUser);
                    signerContract.setContract(result);
                    signerContract.setContractRole(ContractRole.GUEST);
                    signerContract.setCreatedTime(new Date());
                    signerContract.setEmailSigner(s.getEmail());
                    signerContractRepository.save(signerContract);
                } else {
                    Signer signer = new Signer();

                    signer.setEmail(s.getEmail());
                    signer.setFullName(s.getFullName());
                    signer.setNormalName(ConvertUtil.convertStringToNormalized(s.getFullName()));
                    signer.setTaxCode(s.getTaxCode());

                    Company company = companyRepository.findByUser_Email(s.getEmail());
                    if (company != null) {
                        User user = userRepository.findByEmail(s.getEmail());
                        signer.setCompanyName(company.getName());
                        if (validTaxcode) {
                            signer.setUser(user);

                        }
                    }
                    signerRepository.save(signer);
                    SignerContract signerContract = new SignerContract();
                    if (validTaxcode) {
                        signerContract.setContractStatus(ContractStatus.WAITINGFORPARTNER);
                    } else {
                        signerContract.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
                    }
                    signerContract.setIsDelected(false);
                    signerContract.setIsSigned(false);
                    signerContract.setIsWatched(false);
                    signerContract.setX(s.getX());
                    signerContract.setY(s.getY());
                    signerContract.setPage(s.getPage());
                    signerContract.setSigner(signer);
                    signerContract.setContract(result);
                    signerContract.setEmailSigner(s.getEmail());
                    signerContract.setContractRole(ContractRole.GUEST);
                    signerContract.setCreatedTime(new Date());
                    signerContractRepository.save(signerContract);
                }
            }
        }
        Contract newContract = contractRepository.findById(result.getId()).get();
        uploadForContract(file, new ModelMapper().map(newContract, ContractAddDto.class), UserUtil.email());
        if (newContract!=null && result.getIsValidTaxcode()) {
            List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(entity.getId());
            List<String> emailAccept = new ArrayList<>();
            for (Signer signerAccept : signerAcceptEmail) {
                emailAccept.add(signerAccept.getEmail());
            }
            for (SignerForm s : contract.getSigners()) {
                if (!userCreate.getEmail().equals(s.getEmail()) && emailAccept.contains(s.getEmail())) {
                    sendMail(s.getEmail(), result, contract.getNumberOFExpirationDate(), userCreate);
                }
            }
        }
        return new ModelMapper().map(newContract, ContractAddDto.class);

    }

    @Override
    public ContractAddDto getById(String id) {
        log.info("get a contract by id ");
        try {
            return new ModelMapper().map(contractRepository.findById(id).get(), ContractAddDto.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public ContractDetailDto getContractById(String id) {
        try {
            return new ModelMapper().map(contractRepository.findById(id).get(), ContractDetailDto.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    @Transactional
    public void updateWatched(String email, WatchedForm form) throws ApiException {
        log.info("update is watch for user ");
        List<SignerContract> signerContracts = signerContractRepository
                .findBySignerContractList(form.getContractIdList(), email);
        if (signerContracts.size() <= 0) {
            log.error(ERROR.RESULTNOTFOUND.getMessage());
            throw ErrorCodeException.NullException();
        }
        signerContracts.stream().forEach(sc -> {
            sc.setIsWatched(form.getStatus());
        });
        signerContractRepository.saveAll(signerContracts);
    }

    @Override
    @Transactional
    public void delete(List<String> ids, String email) throws ApiException {
        log.info("delete contract for user ");
        List<SignerContract> signerContracts = signerContractRepository.findBySignerContractList(ids, email);
        if (signerContracts.size() <= 0) {
            log.error(ERROR.RESULTNOTFOUND.getMessage());
            throw ErrorCodeException.NullException();
        }
        signerContracts.stream().forEach(uc -> {
            uc.setIsDelected(true);
        });
        signerContractRepository.saveAll(signerContracts);

    }

    @Override
    public FileInformation uploadPdf(MultipartFile file) throws IOException {
        FileInformation information = UploadUtils.upload(file, outdir);
        information.setPath(uploadPath + information.getPath());
        return information;
    }

    @Override
    public void addTagForContract(TagContractForm form) throws ApiException {
        for (String contractId : form.getContractListId()) {
            for (Integer tagId : form.getTagListId()) {
                ContractTag contractTagValid = contractTagRepository.findByContract_IdAndTag_Id(contractId, tagId);
                if (contractTagValid != null) {
                    continue;
                }
                ContractTag contractTag = new ContractTag();
                Optional<Tag> optionalTag = tagRepository.findById(tagId);
                Optional<Contract> optionalContract = contractRepository.findById(String.valueOf(contractId));
                if (!optionalContract.isPresent()) {
                    throw ErrorCodeException.NullException();
                }
                contractTag.setContract(optionalContract.get());

                if (!optionalTag.isPresent()) {
                    throw ErrorCodeException.NullException();
                }
                contractTag.setTag(optionalTag.get());
                contractTag.setCreatedTime(new Date());
                contractTagRepository.save(contractTag);
            }
        }

    }

    @Override
    public void removeTagForContract(TagContractForm form) throws ApiException {

        for (String contractId : form.getContractListId()) {
            for (Integer tagId : form.getTagListId()) {
                ContractTag contractTag = contractTagRepository.findByContract_IdAndTag_Id(contractId, tagId);
                if (contractTag == null) {
                    continue;
                }
                contractTagRepository.delete(contractTag);
            }
        }

    }

    @Override
    public ContractDetailDto getByUser(String contractId, String email) throws ApiException {
        Contract contract = contractRepository.getByIdAndUserEmail(contractId, email);
        if (contract == null) {
            throw ErrorCodeException.NullException();
        }
        User userCreate = signerContractRepository.getUserCreateContract(contractId);
        if (userCreate == null) {
            throw ErrorCodeException.NullException();
        }
        Company company = companyRepository.findByUser_Email(userCreate.getEmail());
        ContractDetailDto contractDto = new ModelMapper().map(contract, ContractDetailDto.class);
        String fileUrl = downloadUrl + jwtProvider.generateForDownload(contract.getFileId()); //tao link download chứa id file
        contractDto.setFileUrl(fileUrl);
        String signFileUrl = downloadUrl + jwtProvider.generateForDownload(contract.getSignFileId()); //tao link download chứa id file đã kí
        contractDto.setSignFileUrl(signFileUrl);
        contractDto.setUserCreate(new ModelMapper().map(userCreate, UserDto.class));
        contractDto.setCompany(new ModelMapper().map(company, CompanyDto.class));
        List<SignerContract> signerContracts = signerContractRepository
                .findByContract_IdOrderByCreatedTimeAsc(contractDto.getId());
        if (signerContracts.size() > 0) {
            List<SignerDto> signerDtos = signerContracts.stream().map(s -> {
                SignerDto signerDto = new ModelMapper().map(s, SignerDto.class);
                signerDto.setId(s.getSigner().getId());
                signerDto.setCompanyName(s.getSigner().getCompanyName());
                signerDto.setEmail(s.getSigner().getEmail());
                signerDto.setFullName(s.getSigner().getFullName());
                signerDto.setTaxCode(s.getSigner().getTaxCode());
                if (s.getSigner().getUser() != null) {
                    signerDto.setAvatarPath(s.getSigner().getUser().getAvatarPath());
                }
                if (s.getSigner().getEmail().equals(email)) {
                    if (s.getContractStatus().name().equals("WAITINGFORPARTNER") && !(s.getIsSigned())) {
                        contractDto.setStatus(ContractStatus.PROCESSING);
                    } else {
                        contractDto.setStatus(s.getContractStatus());
                    }

                }
                return signerDto;
            }).collect(Collectors.toList());
            contractDto.setSignerDtos(signerDtos);

        }
        List<Tag> tags = tagRepository.getAllByContractIdAndEmail(contractDto.getId(), email);
        if (tags.size() > 0) {
            List<TagDto> tagDtos = tags.stream().map(t -> {
                TagDto tagDto = new ModelMapper().map(t, TagDto.class);
                return tagDto;
            }).collect(Collectors.toList());
            contractDto.setTagDtos(tagDtos);
        }
        List<Comment> comments = commentRepository.findByParentIdIsNullAndContract_Id(contractDto.getId());
        if (comments.size() > 0) {
            List<ParentComment> parentComments = new ArrayList<>();
            comments.stream().forEach(c -> {
                ParentComment parentComment = new ModelMapper().map(c, ParentComment.class);
                if (c.getUser() != null) {
                    UserForComment userForComment = new ModelMapper().map(c.getUser(), UserForComment.class);
                    parentComment.setUser(userForComment);
                }
                if (c.getSigner() != null) {
                    SignerForComment signerForComment = new ModelMapper().map(c.getSigner(), SignerForComment.class);
                    parentComment.setSigner(signerForComment);
                }
                List<Comment> childComments = commentRepository.findByParentId(parentComment.getId());
                if (childComments.size() > 0) {
                    List<ChildCommentDto> childCommentDtos = childComments.stream().map(child -> {
                        ChildCommentDto childCommentDto = new ModelMapper().map(child, ChildCommentDto.class);
                        if (child.getUser() != null) {
                            UserForComment userForComment = new ModelMapper().map(child.getUser(),
                                    UserForComment.class);
                            childCommentDto.setUser(userForComment);
                        }
                        if (child.getSigner() != null) {
                            SignerForComment signerForComment = new ModelMapper().map(child.getSigner(),
                                    SignerForComment.class);
                            childCommentDto.setSigner(signerForComment);
                        }
                        return childCommentDto;
                    }).collect(Collectors.toList());
                    parentComment.setChildCommentDtos(childCommentDtos);
                }
                parentComments.add(parentComment);
            });
            contractDto.setCommentDtos(parentComments);
        }
        return contractDto;
    }

    @Override
    public ContractDetailDto getBySigner(String token) {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        Contract contract = contractRepository.findByIdAndSignersEmail(signerInfo.getContracId(),
                signerInfo.getSignerEmail());
        if (contract == null) {
            throw ErrorCodeException.NullException();
        }
        User userCreate = signerContractRepository.getUserCreateContract(signerInfo.getContracId());
        if (userCreate == null) {
            throw ErrorCodeException.NullException();
        }
        Company company = companyRepository.findByUser_Email(userCreate.getEmail());
        ContractDetailDto contractDto = new ModelMapper().map(contract, ContractDetailDto.class);
        String fileUrl = downloadUrl + jwtProvider.generateForDownload(contract.getFileId());
        contractDto.setFileUrl(fileUrl);
        String signFileUrl = downloadUrl + jwtProvider.generateForDownload(contract.getSignFileId());
        contractDto.setSignFileUrl(signFileUrl);
        contractDto.setUserCreate(new ModelMapper().map(userCreate, UserDto.class));
        contractDto.setCompany(new ModelMapper().map(company, CompanyDto.class));
        List<SignerContract> signerContracts = signerContractRepository
                .findByContract_IdOrderByCreatedTimeAsc(contractDto.getId());
        if (signerContracts.size() > 0) {
            List<SignerDto> signerDtos = signerContracts.stream().map(s -> {
                SignerDto signerDto = new ModelMapper().map(s, SignerDto.class);
                signerDto.setId(s.getSigner().getId());
                signerDto.setCompanyName(s.getSigner().getCompanyName());
                signerDto.setEmail(s.getSigner().getEmail());
                signerDto.setFullName(s.getSigner().getFullName());
                signerDto.setTaxCode(s.getSigner().getTaxCode());
                if (s.getSigner().getUser() != null) {
                    signerDto.setAvatarPath(s.getSigner().getUser().getAvatarPath());
                }
                if (s.getSigner().getEmail().equals(signerInfo.getSignerEmail())) {
                    if (s.getContractStatus().name().equals("WAITINGFORPARTNER") && !(s.getIsSigned())) {
                        contractDto.setStatus(ContractStatus.PROCESSING);
                    } else {
                        contractDto.setStatus(s.getContractStatus());
                    }

                }
                return signerDto;
            }).collect(Collectors.toList());
            contractDto.setSignerDtos(signerDtos);

        }
        List<Tag> tags = tagRepository.getAllByContractIdAndEmail(contractDto.getId(), signerInfo.getSignerEmail());
        if (tags.size() > 0) {
            List<TagDto> tagDtos = tags.stream().map(t -> {
                TagDto tagDto = new ModelMapper().map(t, TagDto.class);
                return tagDto;
            }).collect(Collectors.toList());
            contractDto.setTagDtos(tagDtos);
        }
        List<Comment> comments = commentRepository.findByParentIdIsNullAndContract_Id(contractDto.getId());
        if (comments.size() > 0) {
            List<ParentComment> parentComments = new ArrayList<>();
            comments.stream().forEach(c -> {
                ParentComment parentComment = new ModelMapper().map(c, ParentComment.class);
                if (c.getUser() != null) {
                    UserForComment userForComment = new ModelMapper().map(c.getUser(), UserForComment.class);
                    parentComment.setUser(userForComment);
                }
                if (c.getSigner() != null) {
                    SignerForComment signerForComment = new ModelMapper().map(c.getSigner(), SignerForComment.class);
                    parentComment.setSigner(signerForComment);
                }
                List<Comment> childComments = commentRepository.findByParentId(parentComment.getId());
                if (childComments.size() > 0) {
                    List<ChildCommentDto> childCommentDtos = childComments.stream().map(child -> {
                        ChildCommentDto childCommentDto = new ModelMapper().map(child, ChildCommentDto.class);
                        if (child.getUser() != null) {
                            UserForComment userForComment = new ModelMapper().map(child.getUser(),
                                    UserForComment.class);
                            childCommentDto.setUser(userForComment);
                        }
                        if (child.getSigner() != null) {
                            SignerForComment signerForComment = new ModelMapper().map(child.getSigner(),
                                    SignerForComment.class);
                            childCommentDto.setSigner(signerForComment);
                        }
                        return childCommentDto;
                    }).collect(Collectors.toList());
                    parentComment.setChildCommentDtos(childCommentDtos);
                }
                parentComments.add(parentComment);
            });
            contractDto.setCommentDtos(parentComments);
        }
        return contractDto;
    }

    @Override
    @Transactional
    public void cancelContractForUser(String contractId) throws IOException, MessagingException {
        try {
            Contract contract = new Contract();
            CompanyDto company = new CompanyDto();
            User userCreatedContract = signerContractRepository.getUserCreateContract(contractId);
            CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());
            Map<String, Object> model = new HashMap<String, Object>();
            Mail mail = new Mail();
            mail.setFrom(sendFrom);
            String fullName = " ";
            String companyName = "";
            int numberOFExpirationDate = 0;
            if (UserUtil.email() != null) {
                contract = contractRepository.getByIdAndUserEmail(contractId, UserUtil.email());
                SignerContract signerContractCheck = signerContractRepository
                        .findByContract_IdAndSigner_Email(contract.getId(), UserUtil.email());
                if (signerContractCheck.getIsSigned()
                        || signerContractCheck.getContractStatus() == ContractStatus.AUTHENTICATIONFAIL
                        || signerContractCheck.getContractStatus() == ContractStatus.COMPLETE
                        || signerContractCheck.getContractStatus() == ContractStatus.EXPIRED) {
                    throw new ApiException(ERROR.CANNOTCANCEL);
                }
                User user = userRepository.findByEmail(UserUtil.email());
                company = companyService.getByUser(UserUtil.email());
                fullName = (StringUtils.isBlank(user.getFullName()) || user.getFullName() == null) ? " "
                        : String.valueOf(user.getFullName())+ "-";
                companyName =company.getName();
            }
            model.put("signerCancel", fullName + companyName);
            mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.getTitle()));
            List<SignerContract> signerContract = signerContractRepository.findByContract_Id(contractId);
            signerContract.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.CANCEL);
            });
            signerContractRepository.saveAll(signerContract);
            contract.setUpdateTime(new Date());
            contractRepository.save(contract);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
            Date date = new Date(System.currentTimeMillis());
            model.put("title", String.valueOf(contract.getTitle()));
            model.put("companyName", String.valueOf(companyCreated.getName()));
            model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
            model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
            model.put("cancelTime", String.valueOf(formatter.format(date)));
            List<Signer> signers = signerRepository.findByAcceptEmail(contract.getId());
            List<String> emailAccept = new ArrayList<>();
            for (Signer signerAccept : signers) {
                emailAccept.add(signerAccept.getEmail());
            }
            for (Signer mailRecipient : signers) {
                if(emailAccept.contains(mailRecipient.getEmail()) && emailAccept.size()>0)
                {
                    model.put("logo", String.valueOf(companyCreated.getLogoPath()));
                    model.put("username",
                            (StringUtils.isBlank(mailRecipient.getFullName()) || mailRecipient.getFullName() == null) ? " "
                                    : String.valueOf(mailRecipient.getFullName()));
                    String token = jwtProvider.generateForSigner(mailRecipient.getEmail(), contract.getId());
                    String url = urlContract + token;
                    model.put("urlContract", String.valueOf(url));
                    mail.setProps(model);
                    if (mailRecipient.getEmail().equals(UserUtil.email())) {
                        mail.setTemplateName("delete_1");
                    } else {
                        mail.setTemplateName("delete");
                    }
                    mail.setMailTo(mailRecipient.getEmail());
                    emailSenderService.sendEmail(mail);
                }
                if (!(UserUtil.email().equals(mailRecipient.getEmail()))) {
                    SignerContract signerContractUpdateStatus = signerContractRepository
                            .findByContract_IdAndSigner_Email(contractId, mailRecipient.getEmail());
                    signerContractUpdateStatus.setIsWatched(false);
                    signerContractRepository.save(signerContractUpdateStatus);
                }
            }
        } catch (

        MessagingException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            log.info(e.getMessage());
        }

    }

    @Override
    @Transactional
    public void cancelContractForSigner(HttpServletRequest request) throws IOException, MessagingException {
        try {
            Contract contract = new Contract();
            CompanyDto company = new CompanyDto();

            Map<String, Object> model = new HashMap<String, Object>();
            Mail mail = new Mail();
            mail.setFrom(sendFrom);

            int numberOFExpirationDate = 0;

            SignerInfo signerInfo = jwtProvider.getSignerInfo(SignerUtil.getToken(request));
            SignerContract signerContractCheck = signerContractRepository
                    .findByContract_IdAndSigner_Email(signerInfo.getContracId(), signerInfo.getSignerEmail());
            if (signerContractCheck.getIsSigned()
                    || signerContractCheck.getContractStatus() == ContractStatus.AUTHENTICATIONFAIL
                    || signerContractCheck.getContractStatus() == ContractStatus.COMPLETE
                    || signerContractCheck.getContractStatus() == ContractStatus.EXPIRED) {
                throw new ApiException(ERROR.CANNOTCANCEL);
            }
            Signer signer = signerRepository.findByEmailAndContractId(signerInfo.getSignerEmail(),
                    signerInfo.getContracId());
            User userCreatedContract = signerContractRepository.getUserCreateContract(signerInfo.getContracId());
            CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());

            String fullName = (StringUtils.isBlank(signer.getFullName()) || signer == null) ? " "
                    : String.valueOf(signer.getFullName());
            contract = contractRepository.findByIdAndSignersEmail(signerInfo.getContracId(),
                    signerInfo.getSignerEmail());
            mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.getTitle()));
            company = companyService.getByUser(signerInfo.getSignerEmail());
            String companyName = " ";
            if (company == null) {
                companyName = " ";
            } else {
                companyName ="-"+ company.getName();
            }

            model.put("signerCancel", fullName  + companyName);

            numberOFExpirationDate = (int) TimeUnit.MILLISECONDS
                    .toDays(contract.getExpirationTime().getTime() - contract.getCreatedTime().getTime());
            List<SignerContract> signerContract = signerContractRepository.findByContract_Id(signerInfo.getContracId());
            signerContract.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.CANCEL);
            });
            signerContractRepository.saveAll(signerContract);
            contract.setUpdateTime(new Date());
            contractRepository.save(contract);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
            Date date = new Date(System.currentTimeMillis());
            if (companyCreated.getLogoPath() != null) {
                model.put("logo", String.valueOf(companyCreated.getLogoPath()));
            }
            model.put("title", String.valueOf(contract.getTitle()));
            model.put("companyName", String.valueOf(companyCreated.getName()));
            model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
            model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
            model.put("cancelTime", String.valueOf(formatter.format(date)));
            List<Signer> signers = signerRepository.findByAcceptEmail(contract.getId());
            List<String> emailAccept = new ArrayList<>();
            for (Signer signerAccept : signers) {
                emailAccept.add(signerAccept.getEmail());
            }
            for (Signer mailRecipient : signers) {
                if(emailAccept.contains(mailRecipient.getEmail()) && emailAccept.size()>0)
                {
                    model.put("username",
                            (StringUtils.isBlank(mailRecipient.getFullName()) || mailRecipient.getFullName() == null) ? " "
                                    : String.valueOf(mailRecipient.getFullName()));
                    String token = jwtProvider.generateForSigner(mailRecipient.getEmail(), contract.getId());
                    String url = urlContract + token;
                    model.put("urlContract", String.valueOf(url));
                    mail.setProps(model);
                    mail.setMailTo(mailRecipient.getEmail());
                    if (!(signerInfo.getSignerEmail().equals(mailRecipient.getEmail()))) {
                        SignerContract signerContractUpdateStatus = signerContractRepository
                                .findByContract_IdAndSigner_Email(signerInfo.getContracId(), mailRecipient.getEmail());
                        signerContractUpdateStatus.setIsWatched(false);
                        signerContractRepository.save(signerContractUpdateStatus);
                    }
                    if (mailRecipient.getEmail().equals(signerInfo.getSignerEmail())) {
                        mail.setTemplateName("delete_1");
                    } else {
                        mail.setTemplateName("delete");

                    }
                    emailSenderService.sendEmail(mail);
                }
            }

        } catch (MessagingException e) {
            log.info(e.getMessage());
        } catch (IOException e) {
            log.info(e.getMessage());
        }

    }

    @Override
    public void sign(SignForm signForm, String email) throws Exception {
        Signer signer = signerRepository.findByEmailAndContractId(email, signForm.getContractId());
        if (signer == null) {
            throw ErrorCodeException.NullException();
        }
        CompanyDto companySigner = companyService.getByUser(signer.getEmail());
        CompanyDto company = companyService.getByUser(UserUtil.email());
        User userCreatedContract = signerContractRepository.getUserCreateContract(signForm.getContractId());
        CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());
        Optional<Contract> contract = contractRepository.findById(signForm.getContractId());
        SignerContract signerContract = signerContractRepository
                .findByContract_IdAndSigner_Email(contract.get().getId(), email);
        if (signerContract.getIsSigned() || signerContract.getContractStatus() == ContractStatus.AUTHENTICATIONFAIL
                || signerContract.getContractStatus() == ContractStatus.COMPLETE
                || signerContract.getContractStatus() == ContractStatus.EXPIRED
                || signerContract.getContractStatus() == ContractStatus.CANCEL) {
            throw new ApiException(ERROR.CANNOTSIGN);
        }
        List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(contract.get().getId());
        List<String> emailAccept = new ArrayList<>();
        String signerId = "";
        for (Signer signerAccept : signerAcceptEmail) {
            emailAccept.add(signerAccept.getEmail());
        }
        if (!contract.isPresent()) {
            throw ErrorCodeException.NullException();
        }
        if (contract.get().getExpirationTime().before(new Date())) {
            throw new ApiException(ERROR.EXPIRED);
        }
        String emailCreator = signerContractRepository.getUserCreateContract(contract.get().getId()).getEmail();
        List<Signer> signers = signerRepository.findByContractId(contract.get().getId());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        User currentUser = userRepository.findByEmail(UserUtil.email());

        Mail mail = new Mail();
        Map<String, Object> model = new HashMap<String, Object>();
        if (companyCreated.getLogoPath() != null) {
            model.put("logo", String.valueOf(companyCreated.getLogoPath()));
        }
        model.put("companyName", String.valueOf(companyCreated.getName()));
        model.put("title", String.valueOf(contract.get().getTitle()));
        model.put("fullname", userCreatedContract.getFullName()==null? " ":String.valueOf(userCreatedContract.getFullName()));
        model.put("signerName", StringUtils.isBlank(currentUser.getFullName()) || currentUser == null ? " "
                : currentUser.getFullName());
        model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
        model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
        model.put("signerCompanyName", String.valueOf(companySigner.getName()));
        Date date = new Date(System.currentTimeMillis());
        model.put("cancelTime", String.valueOf(formatter.format(date)));
        String token = jwtProvider.generateForSigner(emailCreator, contract.get().getId());
        String url = urlContract + token;
        model.put("urlContract", String.valueOf(url));

        mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.get().getTitle()));
        mail.setFrom(sendFrom);

        X509Certificate certificate = convertToX509Cert(signForm.getPemCert());
        if (!signer.getTaxCode().equals(getTaxCode(certificate.getSubjectDN().getName()))) {
            throw new ApiException(ERROR.TAXCODEINVALID);
        }
        List<CertificateToken> certificates = new ArrayList<>();
        List<String> base64certificates = signForm.getBase64CertificateChain();
        for (String c : base64certificates) {
            certificates.add(new CertificateToken(convertToX509Cert(c)));
        }
        // sai xác thực
        if (!(certificate.getNotAfter().after(new Date()) && certificate.getNotBefore().before(new Date()))) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contract2.getId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi do chữ kí quá hạn");
            mail.setProps(model);
            for (Signer signerNotify : signers) {
                    mail.setMailTo(signerNotify.getEmail());
                    if (emailAccept.contains(signerNotify.getEmail())) {
                        try {
                            if(signerNotify.getEmail().equals(UserUtil.email()))
                            {
                                mail.setTemplateName("accuracy");
                            }
                            else
                            {
                                mail.setTemplateName("accuracy_1");
                            }
                            emailSenderService.sendEmail(mail);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
            throw new ApiException(ERROR.CERTIFICATEEXPIRED);
        }
        RootCa rootCa = rootCaRepository.findByName(getCN(certificate.getIssuerDN().getName()));
        // sai xác thực
        if (rootCa == null) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contract2.getId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi không thuộc root CA");
            mail.setProps(model);
            for (Signer signerNotify : signers) {
                if(signerNotify.getEmail().equals(emailCreator) || signerNotify.getEmail().equals(UserUtil.email()))
                {
                    mail.setMailTo(signerNotify.getEmail());
                    if (emailAccept.contains(signerNotify.getEmail())) {
                        try {
                            if(signerNotify.getEmail().equals(UserUtil.email()))
                            {
                                mail.setTemplateName("accuracy");
                            }
                            else
                            {
                                mail.setTemplateName("accuracy_1");
                            }
                            emailSenderService.sendEmail(mail);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            throw new ApiException(ERROR.CERTIFICATENOTFOUND);
        }

        Optional<S3File> s3FileOptional = s3FileRepository.findById(contract.get().getSignFileId());
        String s3Url = "";
        S3File s3File = null;
        if (!s3FileOptional.isPresent()) {
            throw ErrorCodeException.NullException();
        }
        try {
            s3File = s3FileOptional.get();
            s3Url = s3Utils.getPresignedUrl(bucketName, s3File.getKeyName(), expiredMilliSecondTime);
        } catch (Exception e) {
            throw new ApiException((ERROR.S3PresignedUrl));
        }
        if (!s3File.getKeyName().equals(signForm.getFileName())) {
            throw new ApiException(ERROR.FILECHANGED);
        }
        URL fileUrl = new URL(s3Url);
        String keyName = DateUtil.getStringDateFormat("yyyyMMdd-") + UUID.randomUUID().toString() + '.' + "pdf";
        File targetFile = new File("temp.pdf");

        InputStream inputStream = fileUrl.openStream();
        FileUtils.copyInputStreamToFile(inputStream, targetFile);

        int countAllSigner = signers.size();
        if (StringUtils.isBlank(signer.getCompanyName())) {
            signer.setCompanyName(getCompany(certificate.getSubjectDN().getName()));
        }
        String logoPath = "";
        File imgFile = null;
        if (StringUtils.isNotBlank(company.getLogoPath())) {
            URL imgUrl = new URL(company.getLogoPath());
            logoPath = FilenameUtils.getName(imgUrl.getPath());
            imgFile = new File(outdir + "/" + logoPath);
        } else {
            imgFile = null;
        }

        PAdESSignatureParameters parameters = SignatureUtils.createParameter(certificate, targetFile, signerContract,
                imgFile, certificates,companyCreated.getTaxCode());
        SignatureValue value = SignatureUtils.getSignatureValue(signForm.getSigned());
        PAdESService service = new PAdESService(getCertificateVerifier(certificate));
        service.setTspSource(tspSource);
        DSSDocument toSignDocument = new FileDocument(targetFile);
        ToBeSigned toBeSigned = service.getDataToSign(toSignDocument, parameters);
        if (!verify(signForm.getSigned(), toBeSigned, certificate)) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contract2.getId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi xác nhận");
            mail.setProps(model);
            for (Signer signerNotify : signers) {
                mail.setMailTo(signerNotify.getEmail());
                if (emailAccept.contains(signerNotify.getEmail())) {
                    try {
                        if(signerNotify.getEmail().equals(UserUtil.email()))
                        {
                            mail.setTemplateName("accuracy");
                        }
                        else
                        {
                            mail.setTemplateName("accuracy_1");
                        }
                        emailSenderService.sendEmail(mail);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            throw new ApiException(ERROR.VALIDATEFAIL);
        }
        DSSDocument signedDocument = null;
        try {
            signedDocument = service.signDocument(toSignDocument, parameters, value);
        } catch (Exception e) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contract2.getId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi do sai xác thực");
            mail.setProps(model);
            for (Signer signerNotify : signers) {
                mail.setMailTo(signerNotify.getEmail());
                if (emailAccept.contains(signerNotify.getEmail())) {
                    try {
                        if(signerNotify.getEmail().equals(UserUtil.email()))
                        {
                            mail.setTemplateName("accuracy");
                        }
                        else
                        {
                            mail.setTemplateName("accuracy_1");
                        }
                        emailSenderService.sendEmail(mail);
                    } catch (MessagingException ex) {
                        e.printStackTrace();
                    } catch (IOException ex) {
                        e.printStackTrace();
                    }
                }
            }
            throw new ApiException(ERROR.VALIDATEFAIL);
        }
        s3Utils.uploadFile(keyName, signedDocument.openStream());
        s3Utils.deleteFileOnS3(s3File.getKeyName());
        s3File.setKeyName(keyName);
        s3FileRepository.save(s3File);
        signerContract.setIsSigned(true);
        signerContract.setPemCert(signForm.getPemCert());
        signerContract.setSignedTime(signerContract.getPdfSignTime());
        signerContract.setSignedFile(signForm.getSigned());
        signerContractRepository.save(signerContract);
        signerRepository.save(signer);
        Contract contract2 = contract.get();
        contract2.setUpdateTime(new Date());
        contractRepository.save(contract2);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        int countAllSigned = signerRepository.findAllByIdAndStatus(contract.get().getId()).size();

        signerId = "?signerId=" + signer.getId();
        for (Signer signerReceiver : signers) {
            model.put("signerName", StringUtils.isBlank(currentUser.getFullName()) || currentUser == null ? " "
                    : currentUser.getFullName());
            if (companyCreated.getLogoPath() != null) {
                model.put("logo", String.valueOf(companyCreated.getLogoPath()));
            }
            model.put("companyName", String.valueOf(companyCreated.getName()));
            model.put("title", String.valueOf(contract.get().getTitle()));
            model.put("fullname", signerReceiver.getFullName()==null ? " ": String.valueOf(signerReceiver.getFullName()));
            model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
            model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));

            model.put("signerCompanyName", String.valueOf(companySigner.getName()));
            model.put("cancelTime", String.valueOf(formatter.format(date)));
            mail.setMailTo(signerReceiver.getEmail());
            if (countAllSigner == countAllSigned) {
                mail.setTemplateName("newctr_signer");
                mail.setProps(model);
            } else {
                mail.setTemplateName("newctr_signer_1");
                model.put("numberSigned", String.valueOf(countAllSigned + "/" + countAllSigner));
                mail.setProps(model);
            }
            String tokenSigner = jwtProvider.generateForSigner(signerReceiver.getEmail(), contract.get().getId());
            String urlSigner = urlContract + tokenSigner + signerId;
            model.put("urlContract", String.valueOf(urlSigner));
            if (emailAccept.contains(signerReceiver.getEmail()) && !signerReceiver.getEmail().equals(UserUtil.email())) {
                emailSenderService.sendEmail(mail);
            }
            if (!(UserUtil.email().equals(signerReceiver.getEmail()))) {
                SignerContract signerContractUpdateStatus = signerContractRepository
                        .findByContract_IdAndSigner_Email(contract.get().getId(), signer.getEmail());
                signerContractUpdateStatus.setIsWatched(false);
                signerContractRepository.save(signerContractUpdateStatus);
            }
        }
        if (countAllSigner == countAllSigned) {
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contract.get().getId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.COMPLETE);
            });
            signerContractRepository.saveAll(signerContracts);
        }

    }

    private X509Certificate convertToX509Cert(String certificateString) throws CertificateException {
        X509Certificate certificate = null;
        CertificateFactory cf = null;
        try {
            if (certificateString != null && !certificateString.trim().isEmpty()) {
                certificateString = certificateString.replace("-----BEGIN CERTIFICATE-----\n", "")
                        .replace("-----END CERTIFICATE-----", "");
                byte[] certificateData = Base64.getDecoder().decode(certificateString);
                cf = CertificateFactory.getInstance("X509");
                certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateData));
            }
        } catch (CertificateException e) {
            throw new CertificateException(e);
        }
        return certificate;
    }

    private boolean verify(String signed, ToBeSigned toBeSigned, X509Certificate certificate)
            throws SignatureException, InvalidKeyException, IOException, NoSuchAlgorithmException, ApiException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(certificate.getPublicKey());
        sign.update(toBeSigned.getBytes());
        boolean status = sign.verify(Base64.getDecoder().decode(signed.getBytes()));
        return status;
    }

    private String getCN(String info) {
        StringTokenizer st = new StringTokenizer(info, "=,", false);
        Map<String, String> map = new HashMap<>();
        while (st.hasMoreTokens()) {
            map.put(st.nextToken().trim(), st.nextToken().trim());
        }
        return map.get("CN");
    }

    private String getCompany(String info) {
        StringTokenizer st = new StringTokenizer(info, "=,", false);
        Map<String, String> map = new HashMap<>();
        while (st.hasMoreTokens()) {
            map.put(st.nextToken().trim().replaceAll("MST:", ""), st.nextToken().trim().replaceAll("MST:", ""));
        }
        return map.get("CN");
    }

    private String getTaxCode(String info) {
        StringTokenizer st = new StringTokenizer(info, "=,", false);
        Map<String, String> map = new HashMap<>();
        while (st.hasMoreTokens()) {
            map.put(st.nextToken().trim().replaceAll("MST:", ""), st.nextToken().trim().replaceAll("MST:", ""));
        }
        return map.get("UID");
    }

    @Override
    public void checkSigner(SignForSignerForm signForm, String token2) throws Exception {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token2);
        User userCreatedContract = signerContractRepository.getUserCreateContract(signerInfo.getContracId());
        CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());

        Signer signer = signerRepository.findByEmailAndContractId(signerInfo.getSignerEmail(),
                signerInfo.getContracId());
        if (signer == null) {
            throw ErrorCodeException.NullException();
        }
        CompanyDto companySigner = companyService.getByUser(signer.getEmail());
        Optional<Contract> contract = contractRepository.findById(signerInfo.getContracId());
        SignerContract signerContract = signerContractRepository
                .findByContract_IdAndSigner_Email(signerInfo.getContracId(), signerInfo.getSignerEmail());
        if (signerContract.getIsSigned() || signerContract.getContractStatus() == ContractStatus.AUTHENTICATIONFAIL
                || signerContract.getContractStatus() == ContractStatus.COMPLETE
                || signerContract.getContractStatus() == ContractStatus.EXPIRED
                || signerContract.getContractStatus() == ContractStatus.CANCEL) {
            throw new ApiException(ERROR.CANNOTSIGN);
        }
        if (!contract.isPresent()) {
            throw ErrorCodeException.NullException();
        }
        if (contract.get().getExpirationTime().before(new Date())) {
            throw new ApiException(ERROR.EXPIRED);
        }
        String emailCreator = userCreatedContract.getEmail();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        List<Signer> signers = signerRepository.findByContractId(contract.get().getId());
        Map<String, Object> model = new HashMap<String, Object>();
        List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(contract.get().getId());
        String signerId = "";
        List<String> emailAccept = new ArrayList<>();
        for (Signer signerAccept : signerAcceptEmail) {
            emailAccept.add(signerAccept.getEmail());
        }
        Mail mail = new Mail();
        mail.setFrom(sendFrom);
        if (companyCreated.getLogoPath() != null) {
            model.put("logo", String.valueOf(companyCreated.getLogoPath()));
        }
        model.put("companyName", String.valueOf(companyCreated.getName()));
        model.put("title", String.valueOf(contract.get().getTitle()));
        model.put("fullname", userCreatedContract.getFullName()==null? " ":String.valueOf(userCreatedContract.getFullName()));
        model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
        model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
        Date date = new Date(System.currentTimeMillis());
        model.put("signerName",
                StringUtils.isBlank(signer.getFullName()) || signer == null ? " " : signer.getFullName());
        model.put("cancelTime", String.valueOf(formatter.format(date)));

        String token = jwtProvider.generateForSigner(signer.getEmail(), contract.get().getId());
        String url = urlContract + token;
        model.put("urlContract", String.valueOf(url));

        mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.get().getTitle()));

        X509Certificate certificate = convertToX509Cert(signForm.getPemCert());
        if (!signer.getTaxCode().equals(getTaxCode(certificate.getSubjectDN().getName()))) {
            throw new ApiException(ERROR.TAXCODEINVALID);
        }
        List<CertificateToken> certificates = new ArrayList<>();
        List<String> base64certificates = signForm.getBase64CertificateChain();
        for (String c : base64certificates) {
            certificates.add(new CertificateToken(convertToX509Cert(c)));
        }
        // kí sai xác thực
        if (!(certificate.getNotAfter().after(new Date()) && certificate.getNotBefore().before(new Date()))) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(signerInfo.getContracId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi do sai xác thực");
            mail.setProps(model);
            for (Signer signerNotify : signers) {
                if(signerNotify.getEmail().equals(emailCreator) || signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                {
                    mail.setMailTo(signerNotify.getEmail());
                    if (emailAccept.contains(signerNotify.getEmail())) {
                        try {
                            if(signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                            {
                                mail.setTemplateName("accuracy");
                            }
                            else
                            {
                                mail.setTemplateName("accuracy_1");
                            }
                            emailSenderService.sendEmail(mail);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            throw new ApiException(ERROR.CERTIFICATEEXPIRED);
        }
        RootCa rootCa = rootCaRepository.findByName(getCN(certificate.getIssuerDN().getName()));
        int countAllSigner = signers.size();

        // sai xác thực
        if (rootCa == null) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(signerInfo.getContracId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi do không thuộc root CA");
            mail.setProps(model);
            for (Signer signerNotify : signers) {
                if(signerNotify.getEmail().equals(emailCreator) || signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                {
                    mail.setMailTo(signerNotify.getEmail());
                    if (emailAccept.contains(signerNotify.getEmail())) {
                        try {
                            if(signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                            {
                                mail.setTemplateName("accuracy");
                            }
                            else
                            {
                                mail.setTemplateName("accuracy_1");
                            }
                            emailSenderService.sendEmail(mail);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            throw new ApiException(ERROR.CERTIFICATENOTFOUND);
        }

        Optional<S3File> s3FileOptional = s3FileRepository.findById(contract.get().getSignFileId());
        String s3Url = "";
        S3File s3File = null;
        if (!s3FileOptional.isPresent()) {
            throw ErrorCodeException.NullException();
        }
        try {
            s3File = s3FileOptional.get();
            s3Url = s3Utils.getPresignedUrl(bucketName, s3File.getKeyName(), expiredMilliSecondTime);
        } catch (Exception e) {
            throw new ApiException((ERROR.S3PresignedUrl));
        }
        if (!s3File.getKeyName().equals(signForm.getFileName())) {
            throw new ApiException(ERROR.FILECHANGED);
        }
        URL fileUrl = new URL(s3Url);
        String keyName = DateUtil.getStringDateFormat("yyyyMMdd-") + UUID.randomUUID().toString() + '.' + "pdf";
        File targetFile = new File("temp.pdf");

        InputStream inputStream = fileUrl.openStream();
        FileUtils.copyInputStreamToFile(inputStream, targetFile);

        if (StringUtils.isBlank(signer.getCompanyName())) {
            signer.setCompanyName(getCompany(certificate.getSubjectDN().getName()));
        }
        String logoPath = "";
        File imgFile = null;
        if (companySigner != null && StringUtils.isNotBlank(companySigner.getLogoPath())) {
            URL imgUrl = new URL(companySigner.getLogoPath());
            logoPath = FilenameUtils.getName(imgUrl.getPath());
            imgFile = new File(outdir + "/" + logoPath);
        } else {
            imgFile = null;
        }

        PAdESSignatureParameters parameters = SignatureUtils.createParameter(certificate, targetFile, signerContract,
                imgFile, certificates,companyCreated.getTaxCode());

        SignatureValue value = SignatureUtils.getSignatureValue(signForm.getSigned());
        PAdESService service = new PAdESService(getCertificateVerifier(certificate));
        service.setTspSource(tspSource);
        DSSDocument toSignDocument = new FileDocument(targetFile);
        ToBeSigned toBeSigned = service.getDataToSign(toSignDocument, parameters);
        // sai xác thực
        if (!verify(signForm.getSigned(), toBeSigned, certificate)) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository
                    .findByContract_Id(signerInfo.getContracId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi do sai xác thực");
            mail.setProps(model);

            for (Signer signerNotify : signers) {
                if(signerNotify.getEmail().equals(emailCreator) || signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                {
                    mail.setMailTo(signerNotify.getEmail());
                    if (emailAccept.contains(signerNotify.getEmail())) {
                        try {
                            if(signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                            {
                                mail.setTemplateName("accuracy");
                            }
                            else
                            {
                                mail.setTemplateName("accuracy_1");
                            }
                            emailSenderService.sendEmail(mail);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            throw new ApiException(ERROR.VALIDATEFAIL);
        }
        DSSDocument signedDocument = null;
        try {
            signedDocument = service.signDocument(toSignDocument, parameters, value);
        } catch (Exception e) {
            Contract contract2 = contract.get();
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contract2.getId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
            });
            signerContractRepository.saveAll(signerContracts);
            contract2.setUpdateTime(new Date());
            contractRepository.save(contract2);
            model.put("usbTokenFail", "Lỗi do sai xác thực");
            mail.setProps(model);
            for (Signer signerNotify : signers) {
                if(signerNotify.getEmail().equals(emailCreator) || signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                {
                    mail.setMailTo(signerNotify.getEmail());
                    if (emailAccept.contains(signerNotify.getEmail())) {
                        try {
                            if(signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
                            {
                                mail.setTemplateName("accuracy");
                            }
                            else
                            {
                                mail.setTemplateName("accuracy_1");
                            }
                            emailSenderService.sendEmail(mail);
                        } catch (MessagingException ex) {
                            e.printStackTrace();
                        } catch (IOException ex) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            throw new ApiException(ERROR.VALIDATEFAIL);
        }
        s3Utils.uploadFile(keyName, signedDocument.openStream());
        s3Utils.deleteFileOnS3(s3File.getKeyName());
        s3File.setKeyName(keyName);
        s3FileRepository.save(s3File);
        signerContract.setIsSigned(true);
        signerContract.setPemCert(signForm.getPemCert());
        signerContract.setSignedTime(new Date());
        signerContract.setSignedFile(signForm.getSigned());
        signerContractRepository.save(signerContract);
        signerRepository.save(signer);
        Contract contract2 = contract.get();
        contract2.setUpdateTime(new Date());
        contractRepository.save(contract2);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        // kí thành công
        signerId = "?signerId=" + signer.getId();
        int countAllSigned = signerRepository.findAllByIdAndStatus(contract.get().getId()).size();
        for (Signer signerReceiver : signers) {
            if (companyCreated.getLogoPath() != null) {
                model.put("logo", String.valueOf(companyCreated.getLogoPath()));
            }
            model.put("companyName", String.valueOf(companyCreated.getName()));
            model.put("title", String.valueOf(contract.get().getTitle()));
            model.put("fullname", signerReceiver.getFullName()==null? " ":String.valueOf(signerReceiver.getFullName()));
            model.put("signerName",
                    StringUtils.isBlank(signer.getFullName()) || signer == null ? " " : signer.getFullName());
            model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
            model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
            model.put("cancelTime", String.valueOf(formatter.format(date)));
            String tokenSigner = jwtProvider.generateForSigner(signerReceiver.getEmail(), contract.get().getId());
            String urlSigner = urlContract + tokenSigner + signerId;
            model.put("urlContract", String.valueOf(urlSigner));
            mail.setMailTo(signerReceiver.getEmail());

            if (countAllSigner == countAllSigned) {
                mail.setTemplateName("newctr_signer");
                mail.setProps(model);
            } else {
                mail.setTemplateName("newctr_signer_1");
                model.put("numberSigned", String.valueOf(countAllSigned + "/" + countAllSigner));
                mail.setProps(model);
            }
            if (emailAccept.contains(signerReceiver.getEmail()) && !signerReceiver.getEmail().equals(signerInfo.getSignerEmail())) {
                emailSenderService.sendEmail(mail);
            }
            if (!(signerReceiver.getEmail().equals(signerInfo.getSignerEmail()))) {
                SignerContract signerContractUpdateStatus = signerContractRepository
                        .findByContract_IdAndSigner_Email(contract.get().getId(), signer.getEmail());
                signerContractUpdateStatus.setIsWatched(false);
                signerContractRepository.save(signerContractUpdateStatus);
            }
        }
        if (countAllSigner == countAllSigned) {
            List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contract.get().getId());
            signerContracts.stream().forEach(sc -> {
                sc.setContractStatus(ContractStatus.COMPLETE);
            });
            signerContractRepository.saveAll(signerContracts);
        }
    }

    @Override
    public void uploadForContract(MultipartFile file, ContractAddDto contractAddDto, String email) {
        if (contractAddDto == null) {
            throw ErrorCodeException.NullException();
        }
        S3FileDto s3FileDto = s3FileService.uploadFile(file, email);
        contractAddDto.setFileId(s3FileDto.getId());
        contractAddDto.setFileName(s3FileDto.getFileName());
        S3FileDto s3FileSigned = s3FileService.uploadFile(file, email);
        contractAddDto.setSignFileId(s3FileSigned.getId());
        Contract contract = new ModelMapper().map(contractAddDto, Contract.class);
        contractRepository.save(contract);
    }

    @Override
    public long getFileByContractId(String contractId, String email) {
        Contract contract = contractRepository.getByIdAndUserEmail(contractId, email);
        if (contract == null) {
            throw ErrorCodeException.NullException();
        }
        return contract.getFileId();
    }

    @Override
    public DownloadS3FileUrlListDto getMultipleUrl(DownloadS3FileUrlListForm downloadS3FileUrlListForm) {// trả danh sách url của nhiều file bằng id contract
        List<Contract> contracts = contractRepository.findAllById(downloadS3FileUrlListForm.getIds());
        if (contracts == null || contracts.size() < 1) {
            throw ErrorCodeException.NullException();
        }
        List<FileInformation> informations = contracts.stream().map(c -> {
            FileInformation fileInformation = new FileInformation();
            String fileUrl = downloadUrl + jwtProvider.generateForDownload(c.getSignFileId());
            fileInformation.setName(c.getFileName());
            fileInformation.setPath(fileUrl);
            return fileInformation;
        }).collect(Collectors.toList());
        DownloadS3FileUrlListDto dto = new DownloadS3FileUrlListDto();
        dto.setFileInformations(informations);
        return dto;
    }

    @Override
    public void checkSigning(String token) throws ApiException { //kiểm tra có người đang kí không
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        Contract contract = contractRepository.findByIdAndSignersEmail(signerInfo.getContracId(),
                signerInfo.getSignerEmail());
        if (contract == null) {
            throw ErrorCodeException.NullException();
        }
        if (contract.getIsSign()) {
            throw new ApiException(ERROR.ISSIGNING);
        }
    }

    @Override
    public void checkSigning(String contractId, String email) {
        Contract contract = contractRepository.getByIdAndUserEmail(contractId, email);
        if (contract == null) {
            throw ErrorCodeException.NullException();
        }
        if (contract.getIsSign()) {
            throw new ApiException(ERROR.ISSIGNING);
        }
    }

    @Override
    public void updateSigning(String contractId, String email, Boolean status) { //set trạng thái đang kí
        Contract contract = contractRepository.getByIdAndUserEmail(contractId, email);
        contract.setIsSign(status);
        contractRepository.save(contract);
    }

    @Override
    public void updateSigning(String token, Boolean status) {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        Contract contract = contractRepository.findByIdAndSignersEmail(signerInfo.getContracId(),
                signerInfo.getSignerEmail());
        contract.setIsSign(status);
        contractRepository.save(contract);

    }

    private DataToSignDto getDataSign(Contract contract, X509Certificate certificate, SignerContract signerContract,
            File img, List<CertificateToken> certificateTokens) throws Exception {
        Optional<S3File> s3FileOptional = s3FileRepository.findById(contract.getSignFileId());
        String s3Url = "";
        S3File s3File = null;
        if (!s3FileOptional.isPresent()) {
            throw ErrorCodeException.NullException();
        }
        try {
            s3File = s3FileOptional.get();
            s3Url = s3Utils.getPresignedUrl(bucketName, s3File.getKeyName(), expiredMilliSecondTime);
        } catch (Exception e) {
            throw new ApiException((ERROR.S3PresignedUrl));
        }
        URL fileUrl = new URL(s3Url);
        File targetFile = new File("tempSign.pdf");
        InputStream inputStream = fileUrl.openStream();
        FileUtils.copyInputStreamToFile(inputStream, targetFile);
        inputStream.close();
        DSSDocument toSignDocument = new FileDocument(targetFile);
        PAdESSignatureParameters parameters = SignatureUtils.createParameter(certificate, targetFile, signerContract,
                img, certificateTokens,signerContract.getSigner().getTaxCode());
        PAdESService service = new PAdESService(getCertificateVerifier(certificate));
        service.setTspSource(tspSource);
        ToBeSigned toBeSigned = service.getDataToSign(toSignDocument, parameters);
        targetFile.delete();
        DataToSignDto dataToSignDto = new DataToSignDto();
        dataToSignDto.setDataToSign(toBeSigned);
        dataToSignDto.setFileName(s3File.getKeyName());
        return dataToSignDto;
    }

    @Override
    public DataToSignResponse getDatoToSign(DataToSignForm form, String email) throws Exception {
        Contract contract = contractRepository.getByIdAndUserEmail(form.getContractId(), email);
        if (contract.getExpirationTime().before(new Date())) {
            throw new ApiException(ERROR.EXPIRED);
        }
        SignerContract signerContract = signerContractRepository.findByContract_IdAndSigner_Email(form.getContractId(),
                email);
        if (signerContract.getIsSigned() || signerContract.getContractStatus() == ContractStatus.AUTHENTICATIONFAIL
                || signerContract.getContractStatus() == ContractStatus.COMPLETE
                || signerContract.getContractStatus() == ContractStatus.EXPIRED
                || signerContract.getContractStatus() == ContractStatus.CANCEL) {
            throw new ApiException(ERROR.CANNOTSIGN);
        }
        signerContract.setPdfSignTime(new Date());
        signerContractRepository.save(signerContract);
        Company company = companyRepository.findByUser_Email(email);
        if (company == null) {
            throw ErrorCodeException.NullException();
        }
        X509Certificate certificate = convertToX509Cert(form.getPemCert());
        List<CertificateToken> certificates = new ArrayList<>();
        List<String> base64certificates = form.getBase64CertificateChain();
        for (String c : base64certificates) {
            certificates.add(new CertificateToken(convertToX509Cert(c)));
        }
        File imgFile = null;
        if (StringUtils.isNotBlank(company.getLogoPath())) {
            URL imgUrl = new URL(company.getLogoPath());
            String logoPath = FilenameUtils.getName(imgUrl.getPath());
            imgFile = new File(outdir + "/" + logoPath);
        } else {
            imgFile = null;
        }
        DataToSignDto dataToSignDto = getDataSign(contract, certificate, signerContract, imgFile, certificates);
        DataToSignResponse dataToSignResponse = new DataToSignResponse();
        dataToSignResponse.setDataToSign(Base64.getEncoder().encodeToString(dataToSignDto.getDataToSign().getBytes()));
        dataToSignResponse.setFileName(dataToSignDto.getFileName());
        return dataToSignResponse;
    }

    @Override
    public DataToSignResponse getDatoToSignForSigner(DataToSignForm form, String token) throws Exception {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        Contract contract = contractRepository.findByIdAndSignersEmail(signerInfo.getContracId(),
                signerInfo.getSignerEmail());
        if (contract.getExpirationTime().before(new Date())) {
            throw new ApiException(ERROR.EXPIRED);
        }
        SignerContract signerContract = signerContractRepository
                .findByContract_IdAndSigner_Email(signerInfo.getContracId(), signerInfo.getSignerEmail());
        if (signerContract.getIsSigned() || signerContract.getContractStatus() == ContractStatus.AUTHENTICATIONFAIL
                || signerContract.getContractStatus() == ContractStatus.COMPLETE
                || signerContract.getContractStatus() == ContractStatus.EXPIRED
                || signerContract.getContractStatus() == ContractStatus.CANCEL) {
            throw new ApiException(ERROR.CANNOTSIGN);
        }
        signerContract.setPdfSignTime(new Date());
        signerContractRepository.save(signerContract);
        Company company = companyRepository.findByUser_Email(signerInfo.getSignerEmail());
        X509Certificate certificate = convertToX509Cert(form.getPemCert());
        List<CertificateToken> certificates = new ArrayList<>();
        List<String> base64certificates = form.getBase64CertificateChain();
        for (String c : base64certificates) {
            certificates.add(new CertificateToken(convertToX509Cert(c)));
        }
        File imgFile = null;
        if (company != null) {
            if (StringUtils.isNotBlank(company.getLogoPath())) {
                URL imgUrl = new URL(company.getLogoPath());
                String logoPath = FilenameUtils.getName(imgUrl.getPath());
                imgFile = new File(outdir + "/" + logoPath);
            } else {
                imgFile = null;
            }
        }
        DataToSignDto dataToSignDto = getDataSign(contract, certificate, signerContract, imgFile, certificates);
        DataToSignResponse dataToSignResponse = new DataToSignResponse();
        dataToSignResponse.setDataToSign(Base64.getEncoder().encodeToString(dataToSignDto.getDataToSign().getBytes()));
        dataToSignResponse.setFileName(dataToSignDto.getFileName());
        return dataToSignResponse;
    }

    private CertificateVerifier getCertificateVerifier(X509Certificate certificate) throws Exception {
        CommonCertificateVerifier certificateVerifier = new CommonCertificateVerifier();
        certificateVerifier.setCrlSource(onlineCRLSource);
        certificateVerifier.setOcspSource(onlineOCSPSource);
        certificateVerifier.setDataLoader(commonsDataLoader);

        List<X509Certificate> x509CertificatesTrust = getTrustCertificates(certificate.getSigAlgName());
        CommonTrustedCertificateSource certificateSource = new CommonTrustedCertificateSource();

        x509CertificatesTrust.stream().forEach(x -> {
            certificateSource.addCertificate(new CertificateToken(x));
        });
        certificateVerifier.setTrustedCertSources(certificateSource);
        certificateVerifier.setAlertOnMissingRevocationData(new ExceptionOnStatusAlert());
        certificateVerifier.setCheckRevocationForUntrustedChains(false);
        return certificateVerifier;
    }

    private List<X509Certificate> getTrustCertificates(String key) throws Exception { //lấy trusted certificate từ file
        Resource resource = null;
        if (key.equals("SHA256withRSA")) {
            resource = resourceLoader.getResource("classpath:static/vnrca256.p7b");
        } else {
            resource = resourceLoader.getResource("classpath:static/micnrca.crt");
        }
        File file = resource.getFile();
        Certificate[] certificates = readCertificatesFromPKCS7(FileUtils.readFileToByteArray(file));
        List<X509Certificate> x509Certificates = new ArrayList<>();
        for (Certificate c : certificates) {
            x509Certificates.add((X509Certificate) c);
        }
        return x509Certificates;
    }

    private final Certificate[] readCertificatesFromPKCS7(byte[] binaryPKCS7Store) throws Exception { //chuyển từ file sang certificate
        try (ByteArrayInputStream bais = new ByteArrayInputStream(binaryPKCS7Store);) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection<?> c = cf.generateCertificates(bais);

            List<Certificate> certList = new ArrayList<Certificate>();

            if (c.isEmpty()) {
                throw ErrorCodeException.NullException();
            } else {

                Iterator<?> i = c.iterator();

                while (i.hasNext()) {
                    certList.add((Certificate) i.next());
                }
            }

            java.security.cert.Certificate[] certArr = new java.security.cert.Certificate[certList.size()];

            return certList.toArray(certArr);
        }
    }

    @Override
    public void cancelContract(List<String> contractIds, String email) throws ApiException {
        List<Contract> contracts = contractRepository.getByListIdAndUserEmail(contractIds, email);
        if (CollectionUtils.isEmpty(contracts)) {
            throw ErrorCodeException.NullException();
        }
        for (Contract contract : contracts) {
            SignerContract signerContractCheck = signerContractRepository
                    .findByContract_IdAndSigner_Email(contract.getId(), email);
            if (signerContractCheck.getIsSigned()
                    || signerContractCheck.getContractStatus() == ContractStatus.AUTHENTICATIONFAIL
                    || signerContractCheck.getContractStatus() == ContractStatus.COMPLETE
                    || signerContractCheck.getContractStatus() == ContractStatus.EXPIRED) {
                throw new ApiException(ERROR.CANNOTCANCEL);
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);
        User currentUser = userRepository.findByEmail(UserUtil.email());
        CompanyDto  company = companyService.getByUser(UserUtil.email());
        for (Contract contract : contracts) {
            executor.execute(() -> {
                try {
                    User userCreate = signerContractRepository.getUserCreateContract(contract.getId());
                    CompanyDto companyCreated = companyService.getByUser(userCreate.getEmail());
                    Map<String, Object> model = new HashMap<String, Object>();
                    Mail mail = new Mail();
                    String companyName = " ";
                    if (company == null) {
                        companyName = " ";
                    } else {
                        companyName = company.getName();
                    }

                    mail.setFrom(sendFrom);
                    String fullName = (StringUtils.isBlank(currentUser.getFullName()) || currentUser.getFullName() == null) ? " "
                            : String.valueOf(currentUser.getFullName())+ "-";
//                    companyName =company.getName();
                    model.put("signerCancel", fullName  + companyName);
                    mail.setSubject(companyName + " - " + contract.getTitle());
                    List<SignerContract> signerContract = signerContractRepository.findByContract_Id(contract.getId());
                    signerContract.stream().forEach(sc -> {
                        sc.setContractStatus(ContractStatus.CANCEL);
                    });
                    signerContractRepository.saveAll(signerContract);
                    contract.setUpdateTime(new Date());
                    contractRepository.save(contract);
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
                    Date date = new Date(System.currentTimeMillis());
                    String logo =null;
                    if (companyCreated.getLogoPath() != null) {
                        logo = companyCreated.getLogoPath();
                    }
                    model.put("logo", logo);
                    model.put("title", String.valueOf(contract.getTitle()));
                    model.put("companyName", String.valueOf(companyCreated.getName()));
                    model.put("address", String.valueOf(companyCreated.getAddress()));
                    model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
                    model.put("cancelTime", String.valueOf(formatter.format(date)));
                    List<Signer> signers = signerRepository.findByAcceptEmail(contract.getId());
                    for (Signer mailRecipient : signers) {
                        model.put("username",
                                (StringUtils.isBlank(mailRecipient.getFullName())
                                        || mailRecipient.getFullName() == null) ? " "
                                                : String.valueOf(mailRecipient.getFullName()));
                        String token = jwtProvider.generateForSigner(mailRecipient.getEmail(), contract.getId());
                        String url = urlContract + token;
                        model.put("urlContract", String.valueOf(url));
                        mail.setProps(model);
                        if (mailRecipient.getEmail().equals(currentUser.getEmail())) {
                            mail.setTemplateName("delete_1");
                            mail.setMailTo(mailRecipient.getEmail());
                            emailSenderService.sendEmail(mail);
                        } else {
                            mail.setTemplateName("delete");
                            mail.setMailTo(mailRecipient.getEmail());
                            emailSenderService.sendEmail(mail);
                        }

                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }

    @Override
    public void updateAuthenticationFail(String contractId, String email) throws ApiException {
       Contract contract = contractRepository.getByIdAndUserEmail(contractId, email);
        User userCreatedContract = signerContractRepository.getUserCreateContract(contractId);
        List<Signer> signers = signerRepository.findByContractId(contract.getId());
        if(contract == null) {
            throw ErrorCodeException.NullException();
        }
        List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(contractId);
        signerContracts.stream().forEach(sc -> {
            sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
        });
        signerContractRepository.saveAll(signerContracts);
        contract.setUpdateTime(new Date());
        contractRepository.save(contract);
        CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());
        String emailCreator = signerContractRepository.getUserCreateContract(contractId).getEmail();
        User currentUser = userRepository.findByEmail(UserUtil.email());
        Signer signer = signerRepository.findByEmailAndContractId(email, contractId);
        if (signer == null) {
            throw ErrorCodeException.NullException();
        }
        CompanyDto companySigner = companyService.getByUser(signer.getEmail());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        Mail mail = new Mail();
        Map<String, Object> model = new HashMap<String, Object>();
        if (companyCreated.getLogoPath() != null) {
            model.put("logo", String.valueOf(companyCreated.getLogoPath()));
        }
        model.put("companyName", String.valueOf(companyCreated.getName()));
        model.put("title", String.valueOf(contract.getTitle()));
        model.put("fullname", userCreatedContract.getFullName()==null? "": " " + String.valueOf(userCreatedContract.getFullName()));
        model.put("signerName", StringUtils.isBlank(currentUser.getFullName()) || currentUser == null ? " "
                : currentUser.getFullName());
        model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
        model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
        model.put("signerCompanyName", String.valueOf(companySigner.getName()));
        Date date = new Date(System.currentTimeMillis());
        model.put("cancelTime", String.valueOf(formatter.format(date)));
        String token = jwtProvider.generateForSigner(emailCreator, contract.getId());
        String url = urlContract + token;
        model.put("urlContract", String.valueOf(url));

        model.put("usbTokenFail", "Lỗi do sai mã số thuế");
        mail.setProps(model);
        mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.getTitle()));
        mail.setFrom(sendFrom);
        List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(contractId);
        List<String> emailAccept = new ArrayList<>();
        for (Signer signerAccept : signerAcceptEmail) {
            emailAccept.add(signerAccept.getEmail());
        }
        for (Signer signerNotify : signers) {
            if(signerNotify.getEmail().equals(emailCreator) || signerNotify.getEmail().equals(UserUtil.email()))
            {
                mail.setMailTo(signerNotify.getEmail());
                if (emailAccept.contains(signerNotify.getEmail())) {
                    try {
                        if(signerNotify.getEmail().contains(UserUtil.email()))
                        {
                            mail.setTemplateName("accuracy");
                        }
                        else
                        {
                            mail.setTemplateName("accuracy_1");
                        }
                        emailSenderService.sendEmail(mail);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    @Override
    public void updateAuthenticationFail(String token) {
        SignerInfo signerInfo = jwtProvider.getSignerInfo(token);
        Contract contract= contractRepository.findByIdAndSignersEmail(signerInfo.getContracId(), signerInfo.getSignerEmail());
        List<Signer> signers = signerRepository.findByContractId(contract.getId());
        if(contract == null) {
            throw ErrorCodeException.NullException();
        }
        List<SignerContract> signerContracts = signerContractRepository.findByContract_Id(signerInfo.getContracId());
        signerContracts.stream().forEach(sc -> {
            sc.setContractStatus(ContractStatus.AUTHENTICATIONFAIL);
        });
        signerContractRepository.saveAll(signerContracts);
        contract.setUpdateTime(new Date());
        contractRepository.save(contract);

        User userCreatedContract = signerContractRepository.getUserCreateContract(signerInfo.getContracId());
        CompanyDto companyCreated = companyService.getByUser(userCreatedContract.getEmail());

        Signer signer = signerRepository.findByEmailAndContractId(signerInfo.getSignerEmail(),
                signerInfo.getContracId());
        if (signer == null) {
            throw ErrorCodeException.NullException();
        }
        String emailCreator = userCreatedContract.getEmail();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        Map<String, Object> model = new HashMap<String, Object>();
        List<Signer> signerAcceptEmail = signerRepository.findByAcceptEmail(signerInfo.getContracId());
        List<String> emailAccept = new ArrayList<>();
        for (Signer signerAccept : signerAcceptEmail) {
            emailAccept.add(signerAccept.getEmail());
        }
        Mail mail = new Mail();
        mail.setFrom(sendFrom);
        if (companyCreated.getLogoPath() != null) {
            model.put("logo", String.valueOf(companyCreated.getLogoPath()));
        }
        model.put("companyName", String.valueOf(companyCreated.getName()));
        model.put("title", String.valueOf(contract.getTitle()));
        model.put("fullname", userCreatedContract.getFullName() == null? "": " " + String.valueOf(userCreatedContract.getFullName()));
        model.put("address", companyCreated.getAddress()==null? " ": String.valueOf(companyCreated.getAddress()));
        model.put("taxCode", String.valueOf("MST " + companyCreated.getTaxCode()));
        Date date = new Date(System.currentTimeMillis());
        model.put("signerName",
                StringUtils.isBlank(signer.getFullName()) || signer == null ? " " : signer.getFullName());
        model.put("cancelTime", String.valueOf(formatter.format(date)));

        String tokenUrl = jwtProvider.generateForSigner(signer.getEmail(), signerInfo.getContracId());
        String url = urlContract + tokenUrl;
        model.put("urlContract", String.valueOf(url));
        model.put("usbTokenFail", "Lỗi do sai mã số thuế");
        mail.setProps(model);
        mail.setSubject(companyCreated.getName() + " - " + String.valueOf(contract.getTitle()));


        for (Signer signerNotify : signers) {
            if(signerNotify.getEmail().equals(emailCreator) || signerNotify.getEmail().equals(signerInfo.getSignerEmail()))
            {
                mail.setMailTo(signerNotify.getEmail());
                if (emailAccept.contains(signerNotify.getEmail())) {
                    try {
                        if(signerNotify.getEmail().contains((signerInfo.getSignerEmail())))
                        {
                            mail.setTemplateName("accuracy");
                        }
                        else
                        {
                            mail.setTemplateName("accuracy_1");
                        }
                        emailSenderService.sendEmail(mail);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
