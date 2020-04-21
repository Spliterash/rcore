package com.rcore.domain.token.usecase;

import com.rcore.commons.utils.DateTimeUtils;
import com.rcore.domain.access.entity.Access;
import com.rcore.domain.token.entity.AccessTokenEntity;
import com.rcore.domain.token.entity.RefreshTokenEntity;
import com.rcore.domain.token.exception.AuthenticationException;
import com.rcore.domain.token.port.AccessTokenStorage;
import com.rcore.domain.token.port.AuthorizationPort;
import com.rcore.domain.token.port.RefreshTokenRepository;
import com.rcore.domain.user.entity.UserEntity;
import com.rcore.domain.user.entity.UserStatus;
import com.rcore.domain.user.exception.TokenExpiredException;
import com.rcore.domain.user.exception.UserBlockedException;
import com.rcore.domain.user.exception.UserNotExistException;
import com.rcore.domain.user.port.UserRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AuthorizationByTokenUseCase implements AuthorizationPort {

    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenStorage accessTokenStorage;
    private final UserRepository userRepository;

    public AuthorizationByTokenUseCase(RefreshTokenRepository refreshTokenRepository, AccessTokenStorage accessTokenStorage, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenStorage = accessTokenStorage;
        this.userRepository = userRepository;
    }

    @Override
    public Boolean checkAccess(AccessTokenEntity accessToken, Set<Access> userAccesses) {
        if (LocalDateTime.now().isAfter(accessToken.getExpireAt())) return false;

        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findById(accessToken.getCreateFromRefreshTokenId());
        if (!refreshTokenEntity.isPresent()) return false;

        if (!refreshTokenEntity.get().isActive()) return false;

        if (!AccessTokenEntity.sign(accessToken.getId(), DateTimeUtils.getMillis(accessToken.getExpireAt()), refreshTokenEntity.get()).equals(accessToken.getSign()))
            return false;

        return true;
    }

    public void putInStorage(AccessTokenEntity accessTokenEntity) {
        accessTokenStorage.put(accessTokenEntity);
    }

    public AccessTokenEntity currentAccessToken() throws AuthenticationException {
        AccessTokenEntity accessTokenEntity = accessTokenStorage.current()
                .orElseThrow(() -> new AuthenticationException());

        if (!checkAccess(accessTokenEntity, new HashSet<>())) {
            throw new AuthenticationException();
        }

        return accessTokenEntity;
    }

    public UserEntity getUserByAccessToken(AccessTokenEntity accessToken) throws UserNotExistException, UserBlockedException, TokenExpiredException {
        UserEntity user = userRepository.findById(accessToken.getUserId())
                .orElseThrow(UserNotExistException::new);
        if (user.getStatus().equals(UserStatus.BLOCK))
            throw new UserBlockedException();
        else if (!accessToken.isActive())
            throw new TokenExpiredException();
        return user;
    }

    public UserEntity currentUser() throws AuthenticationException {
        AccessTokenEntity accessTokenEntity = currentAccessToken();

        return userRepository.findById(accessTokenEntity.getUserId())
                .orElseThrow(() -> new AuthenticationException());
    }

}
