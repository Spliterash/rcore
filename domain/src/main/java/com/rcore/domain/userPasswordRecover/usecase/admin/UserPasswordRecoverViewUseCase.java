package com.rcore.domain.userPasswordRecover.usecase.admin;

import com.rcore.domain.userPasswordRecover.entity.UserPasswordRecoverEntity;
import com.rcore.domain.userPasswordRecover.port.UserPasswordRecoverRepository;
import com.rcore.domain.userPasswordRecover.access.AdminUserPasswordRecoverViewAccess;
import com.rcore.domain.token.exception.AuthorizationException;
import com.rcore.domain.user.entity.UserEntity;
import com.rcore.domain.base.port.SearchResult;

public class UserPasswordRecoverViewUseCase extends UserPasswordRecoverAdminBaseUseCase {

    public UserPasswordRecoverViewUseCase(UserEntity actor, UserPasswordRecoverRepository userPasswordRecoverRepository) throws AuthorizationException {
        super(actor, userPasswordRecoverRepository, new AdminUserPasswordRecoverViewAccess());
    }

    public UserPasswordRecoverEntity findById(String id) {
        return userPasswordRecoverRepository.findById(id).get();
    }

    public UserPasswordRecoverEntity search(String id) {
        return userPasswordRecoverRepository.findById(id).get();
    }

    public SearchResult<UserPasswordRecoverEntity> find(Long size, Long skip) {
        return userPasswordRecoverRepository.find(size, skip);
    }

}