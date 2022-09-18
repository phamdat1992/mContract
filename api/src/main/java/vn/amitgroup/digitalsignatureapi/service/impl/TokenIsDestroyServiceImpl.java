package vn.amitgroup.digitalsignatureapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.amitgroup.digitalsignatureapi.entity.TokenIsDestroy;
import vn.amitgroup.digitalsignatureapi.repository.TokenIsDestroyRepository;
import vn.amitgroup.digitalsignatureapi.service.TokenIsDestroyService;
@Service
public class TokenIsDestroyServiceImpl implements TokenIsDestroyService{
    @Autowired
    private TokenIsDestroyRepository tokenIsDestroyRepository;

    @Override
    public List<TokenIsDestroy> get(String token) {
        return tokenIsDestroyRepository.findByToken(token);
    }

    @Override
    public void add(TokenIsDestroy entity) {
        tokenIsDestroyRepository.save(entity);  
    }

    @Override
    public Integer clear() {
        try {
            tokenIsDestroyRepository.clear();
            return 1;
        } catch (Exception e) {
            throw e;
        }
    }
    
}
