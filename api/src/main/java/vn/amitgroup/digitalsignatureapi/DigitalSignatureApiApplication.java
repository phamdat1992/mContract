package vn.amitgroup.digitalsignatureapi;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import vn.amitgroup.digitalsignatureapi.dto.ContractDetailDto;
import vn.amitgroup.digitalsignatureapi.entity.RootCa;
import vn.amitgroup.digitalsignatureapi.entity.TaskScheduler;
import vn.amitgroup.digitalsignatureapi.job.NotificationJob;
import vn.amitgroup.digitalsignatureapi.job.NotificationJobExpired;
import vn.amitgroup.digitalsignatureapi.job.info.TimerInfo;
import vn.amitgroup.digitalsignatureapi.repository.RootCaRepository;
import vn.amitgroup.digitalsignatureapi.service.ContractService;
import vn.amitgroup.digitalsignatureapi.service.impl.TaskSchedulerService;
import vn.amitgroup.digitalsignatureapi.timerservice.NotificationService;
import vn.amitgroup.digitalsignatureapi.timerservice.SchedulerService;

@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition
public class DigitalSignatureApiApplication {
	@Value("${my.origins}")
	private List<String> origins;
	@Value("${root.ca.list}")
	private List<String> rootCaList;
	@Autowired
	private RootCaRepository rootCaRepository;
	@Autowired
	private TaskSchedulerService taskSchedulerService;
	@Autowired
	private NotificationService notificationService;

	@Value("${rt-server.host}")
	private String host;

	@Value("${rt-server.port}")
	private Integer port;
	@Autowired
	ContractService contractService;
	@Autowired
	private SchedulerService scheduler;


	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname(host);
		config.setPort(port);
//		config.setRandomSession(true);
		SocketConfig socketConfig = new SocketConfig();
		socketConfig.setReuseAddress(true);
		config.setSocketConfig(socketConfig);
		return new SocketIOServer(config);
	}

	public static void main(String[] args) {
		SpringApplication.run(DigitalSignatureApiApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				String[] arrayFromList = origins.stream().toArray(String[]::new);
				registry.addMapping("/**")
						.allowedOrigins(arrayFromList)
						.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS")
						.allowCredentials(true);
			}
		};
	}

	@Bean
	@Transactional
	InitializingBean sendDatabase() {
		return () -> {
			List<RootCa> list = rootCaRepository.findAll();
			if (CollectionUtils.isEmpty(list)) {
				List<RootCa> result = rootCaList.stream().map(r -> {
					RootCa rootCa = new RootCa();
					rootCa.setName(r);
					return rootCa;
				}).collect(Collectors.toList());
				rootCaRepository.saveAll(result);
			}
		};
	}

	@Bean("timeTaskCurrent")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public void getTaskService() {
		try {
			if (taskSchedulerService.getAll().size() > 1) {
				TaskScheduler taskSchedulerContinuous = taskSchedulerService.getAll().get(0);
				notificationService.deleteTimer("NotificationJob");
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
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
