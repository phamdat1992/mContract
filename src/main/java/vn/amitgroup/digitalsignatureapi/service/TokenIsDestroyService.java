package vn.amitgroup.digitalsignatureapi.service;

import java.util.List;

import vn.amitgroup.digitalsignatureapi.entity.TokenIsDestroy;

public interface TokenIsDestroyService {
    List<TokenIsDestroy> get(String token);
    void add(TokenIsDestroy entity);
    Integer clear();
}
