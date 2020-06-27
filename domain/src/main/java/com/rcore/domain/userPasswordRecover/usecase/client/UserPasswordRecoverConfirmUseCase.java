package com.rcore.domain.userPasswordRecover.usecase.client;

import com.rcore.domain.user.entity.UserEntity;
import com.rcore.domain.user.exception.UserNotFoundException;
import com.rcore.domain.user.port.PasswordGenerator;
import com.rcore.domain.user.port.UserRepository;
import com.rcore.domain.userPasswordRecover.entity.UserPasswordRecoverEntity;
import com.rcore.domain.userPasswordRecover.exception.UserPasswordRecoverNotFoundException;
import com.rcore.domain.userPasswordRecover.port.EmailSender;
import com.rcore.domain.userPasswordRecover.port.UserPasswordRecoverIdGenerator;
import com.rcore.domain.userPasswordRecover.port.UserPasswordRecoverRepository;

import java.util.Optional;

public class UserPasswordRecoverConfirmUseCase {

    protected final UserPasswordRecoverRepository userPasswordRecoverRepository;
    protected final UserRepository userRepository;
    protected final PasswordGenerator passwordGenerator;

    public UserPasswordRecoverConfirmUseCase(UserPasswordRecoverRepository userPasswordRecoverRepository, UserRepository userRepository, PasswordGenerator passwordGenerator) {
        this.userPasswordRecoverRepository = userPasswordRecoverRepository;
        this.userRepository = userRepository;
        this.passwordGenerator = passwordGenerator;
    }

    public UserPasswordRecoverEntity confirm(String email, String code, String newClearPassword) throws UserPasswordRecoverNotFoundException, UserNotFoundException {
        email = email.toLowerCase();
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        Optional<UserPasswordRecoverEntity> userPasswordCurrent = userPasswordRecoverRepository.findActiveByEmail(userEntity.getId());

        if(userPasswordCurrent.isPresent() == false){
            throw new UserPasswordRecoverNotFoundException();
        }

        if(userPasswordCurrent.get().getCode().equals(code) == false){
            throw new UserPasswordRecoverNotFoundException();
        }

        UserPasswordRecoverEntity userPasswordRecoverEntity = userPasswordCurrent.get();
        userPasswordRecoverEntity.setIsRecovered(true);
        userPasswordRecoverRepository.save(userPasswordRecoverEntity);

        userEntity.setPassword(passwordGenerator.generate(userEntity.getId(), newClearPassword));
        userRepository.save(userEntity);

        return userPasswordRecoverEntity;
    }

}
