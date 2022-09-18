package vn.amitgroup.digitalsignatureapi.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.amitgroup.digitalsignatureapi.entity.UserOtp;
import vn.amitgroup.digitalsignatureapi.repository.UserOtpRepository;

@Service
public class OtpCodeService {
    private static final Integer EXPIRE_MINS = 10;
    @Autowired
    private UserOtpRepository otpRepository;

    public int generateOTP(String key) {

        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        UserOtp otpUserOtp = new UserOtp();
        otpUserOtp.setCode(otp);
        otpUserOtp.setKey(key);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, EXPIRE_MINS);
        otpUserOtp.setExpirationTime(cal.getTime());
        otpRepository.save(otpUserOtp);
        return otp;
    }
    public int generateLink(String key,Integer expireTime) {
        Random random = new Random();
        int otp = 10000000 + random.nextInt(90000000);
        UserOtp otpUserOtp = new UserOtp();
        otpUserOtp.setCode(otp);
        otpUserOtp.setKey(key);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, expireTime);
        otpUserOtp.setExpirationTime(cal.getTime());
        otpRepository.save(otpUserOtp);
        return otp;
    }

    // This method is used to return the OPT number against Key->Key values is
    // username
    public int getOtp(String key) {
        try {
            List<UserOtp> list= otpRepository.findCustom(key);
            if(list.size()>0){
                return list.get(0).getCode();
            }
            else{
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    @Transactional
    public void clearOTP(String key) {
       otpRepository.deleteByKey(key);
    }
}
