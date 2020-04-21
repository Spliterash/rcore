package com.rcore.domain.user.usecase.admin;

import com.rcore.domain.token.exception.AuthorizationException;
import com.rcore.domain.token.usecase.ExpireTokenUseCase;
import com.rcore.domain.user.entity.UserEntity;
import com.rcore.domain.user.entity.UserStatus;
import com.rcore.domain.user.port.UserRepository;
import com.rcore.domain.user.access.AdminUserBlockAccess;

public class BlockUseCase extends AdminBaseUseCase {

    private final ExpireTokenUseCase expireTokenUseCase;

    public BlockUseCase(UserEntity actor, UserRepository userRepository, ExpireTokenUseCase expireTokenUseCase) throws AuthorizationException {
        super(actor, userRepository, new AdminUserBlockAccess());
        this.expireTokenUseCase = expireTokenUseCase;
    }


    public UserEntity block(UserEntity userEntity) {
        userEntity.setStatus(UserStatus.BLOCK);
        userEntity.setFails(0);
        userEntity = userRepository.save(userEntity);

        expireTokenUseCase.logout(userEntity);

        return userEntity;
    }
}
