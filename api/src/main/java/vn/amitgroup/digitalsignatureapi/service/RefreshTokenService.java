package vn.amitgroup.digitalsignatureapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.amitgroup.digitalsignatureapi.entity.RefreshToken;
import vn.amitgroup.digitalsignatureapi.entity.User;
import vn.amitgroup.digitalsignatureapi.exception.TokenRefreshException;
import vn.amitgroup.digitalsignatureapi.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByToken(String token) throws TokenRefreshException {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(token);
        if (!refreshToken.isPresent()) {
            throw new TokenRefreshException(token);
        }
        return refreshToken.get();
    }
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }
    @Transactional
    public void deleteByToken(String token){
        refreshTokenRepository.deleteById(token);
    }
}
