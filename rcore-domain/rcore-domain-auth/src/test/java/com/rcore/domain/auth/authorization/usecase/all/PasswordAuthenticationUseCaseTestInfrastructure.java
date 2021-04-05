package com.rcore.domain.auth.authorization.usecase.all;

import com.rcore.domain.auth.authorization.usecase.AuthorizationUseCaseTestInfrastructure;
import com.rcore.domain.auth.credential.entity.CredentialEntity;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;

class PasswordAuthenticationUseCaseTestInfrastructure extends AuthorizationUseCaseTestInfrastructure {

    protected static final String id = UUID.randomUUID().toString();
    protected static final String username = "mail@mail";
    protected static final String password = "password";
    protected final String encodedPassword = passwordCryptographer.encrypt(password, id);

    protected final CredentialEntity credential;


    public PasswordAuthenticationUseCaseTestInfrastructure() {
        initCredentialMocks();
        CredentialEntity defaultCredential = new CredentialEntity();
        defaultCredential.setId(id);
        defaultCredential.setUsername(username);
        defaultCredential.setPassword(encodedPassword);
        this.credential = defaultCredential;
    }

    private void initCredentialMocks() {
        Mockito.when(credentialRepository.findByUsername(anyString()))
                .thenAnswer(a -> {
                    if (a.getArgument(0).toString().equals(username))
                        return Optional.of(credential);
                    else return Optional.empty();
                });
    }



}