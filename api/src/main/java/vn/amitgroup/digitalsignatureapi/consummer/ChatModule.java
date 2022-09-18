package vn.amitgroup.digitalsignatureapi.consummer;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import vn.amitgroup.digitalsignatureapi.dto.NotifyRealTime;
import vn.amitgroup.digitalsignatureapi.repository.ContractRepository;
import vn.amitgroup.digitalsignatureapi.security.jwt.JwtProvider;
import vn.amitgroup.digitalsignatureapi.service.ContractService;
import vn.amitgroup.digitalsignatureapi.service.UserService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ChatModule implements ConnectListener, DisconnectListener, DataListener<ChatMessage> {

    private Logger logger = LogManager.getLogger(ChatModule.class);

    private final SocketIONamespace namespace;

    ConcurrentMap<Long, List<UUID>> channelModelHashMap = new ConcurrentHashMap<>();

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    ContractService contractService;

    @Value("${vn.icheck.distributed-lock.wait:1000}")
    private int lockTimeWait;

    @Value("${vn.icheck.distributed-lock.release-after:1000}")
    private int lockRelease;

    @Value("${my.key}")
    private String key;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserService userService;
    @Autowired
    ContractRepository contractRepository;

    private static HashMap<String,WSConnectSession> session = new HashMap<>();

    @Autowired
    public ChatModule(SocketIOServer server) {
        this.namespace = server.addNamespace("/notify");
        this.namespace.addConnectListener(this);
        this.namespace.addDisconnectListener(this);
        this.namespace.addEventListener("notify", ChatMessage.class, this);
    }
    @Override
    public void onConnect(SocketIOClient client) {
        HandshakeData handshakeData = client.getHandshakeData();
        String token = handshakeData.getSingleUrlParam("token");
        String contractId = handshakeData.getSingleUrlParam("contractId");
        if (!token.equals("null")) {
            try {
                String emailLogin = jwtProvider.getUserAccount(token);
                if (StringUtils.isBlank(emailLogin)) {
                    client.disconnect();
                } else if (emailLogin != null) {
                    if (StringUtils.isBlank(contractId) || contractId.equals("null")) {
                        logger.info("emaildashboard: " + emailLogin);
                        client.set("email", emailLogin);
                    }
                } else {
                    client.set("email", null);
                    client.disconnect();
                }
                logger.info("listCurrentClient is : " + namespace.getAllClients().size());
            }catch (Exception e)
            {
                client.set("email",null);
                client.disconnect();
                logger.info("Token expired");
            }
        }
    }


    @Override
    public void onDisconnect(SocketIOClient client) {
        client.set("email",null);
        client.disconnect();
    }

    @Override
    public void onData(SocketIOClient client, ChatMessage data, AckRequest ackRequest) throws Exception {
        if(data.getType()==EnumSocket.JOINDASHBOARD)
        {
            if(client.get("email")!=null) {
                client.joinRoom(client.get("email"));
                client.set("room","DASHBOARD");
                namespace.getClient(client.getSessionId()).sendEvent("notify", "JOIN" + client.get("room").toString() + client.get("email").toString());
            }
        }
        else if( data.getType()==EnumSocket.JOINCONTRACTDETAIL)
        {
            if(data.getContractId()!=null)
            {
                client.joinRoom(client.get("email") + key + data.getContractId());
                client.set("room",key);
                namespace.getClient(client.getSessionId()).sendEvent("notify", "JOIN" + client.get("room").toString() + client.get("email").toString() + key + data.getContractId());
            }
        }
        else if(data.getType()==EnumSocket.LEAVEDASHBOARD)
        {
            try {
                if(data.getContractId()!=null) {
                    client.leaveRoom(client.get("email"));
                    namespace.getClient(client.getSessionId()).sendEvent("notify", "LEAVE" + client.get("room").toString() + client.get("email").toString());
                }
            }catch (Exception e)
            {
                logger.error("User does not exist in room");
            }
        }
        else if(data.getType()==EnumSocket.LEAVECONTRACTDETAIL)
        {
            if(data.getContractId()!=null)
            {
                try {
                    if(data.getContractId()!=null) {
                        client.leaveRoom(client.get("email") + key + data.getContractId());
                        namespace.getClient(client.getSessionId()).sendEvent("notify", "LEAVE" + client.get("room").toString() + client.get("email") + key + data.getContractId());
                    }
                }
                catch (Exception e)
                {
                    logger.error("User does not exist in room");
                }
            }
        }
    }
    public void joinRoom(String email,String room)
    {
        for (SocketIOClient client :namespace.getAllClients()) {
            if (client.get("email").equals(email)) {
                client.joinRoom(room);
            }
        }
        logger.info("listCurrentClient is : " + namespace.getAllClients().size());
    }
    public void sendNotify(List<String> room,NotifyRealTime data)
    {
        Set<String> roomSet = Sets.newHashSet(room);
        for (String roomJoin : roomSet) {
            BroadcastOperations roomBoard  =  namespace.getRoomOperations(roomJoin);
            if(roomBoard==null)
            {
                continue;
            }
            for (SocketIOClient client : roomBoard.getClients()) {
                if (roomJoin.contains(key)&&client.get("room").equals(key)) {
                    data.setIsDashboard(false);
                    client.sendEvent("notify",data);
                } else if(!roomJoin.contains(key)&&client.get("room").equals("DASHBOARD")) {
                    data.setIsDashboard(true);
                    client.sendEvent("notify",data);
                } else {
                    logger.info(client.get("email")+"-----" + client.get("room").toString() +"-----"+ roomJoin + "-----" + "client not in room");
                }
            }

//            namespace.getRoomOperations(roomJoin).sendEvent("notify", data);
        }
    }
}
