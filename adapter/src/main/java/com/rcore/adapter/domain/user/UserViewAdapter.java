package com.rcore.adapter.domain.user;

import com.rcore.adapter.domain.role.mapper.RoleMapper;
import com.rcore.adapter.domain.user.dto.UserDTO;
import com.rcore.adapter.domain.user.mapper.UserMapper;
import com.rcore.domain.base.port.SearchRequest;
import com.rcore.domain.base.port.SearchResult;
import com.rcore.domain.token.exception.AuthenticationException;
import com.rcore.domain.token.exception.AuthorizationException;
import com.rcore.domain.user.config.UserConfig;
import com.rcore.domain.user.entity.UserEntity;
import com.rcore.domain.user.exception.TokenExpiredException;
import com.rcore.domain.user.port.filters.UserFilters;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserViewAdapter {
    private UserMapper userMapper = new UserMapper(new RoleMapper());
    private final UserConfig userConfig;

    public Optional<UserDTO> findById(String id) throws AuthorizationException, AuthenticationException, TokenExpiredException {
        return userConfig.admin.ViewUserUseCase()
                .findById(id)
                .map(userMapper::map);
    }

    public Optional<UserDTO> findByEmail(String email) throws AuthorizationException, AuthenticationException, TokenExpiredException {
        return userConfig.admin.ViewUserUseCase()
                .findByEmail(email)
                .map(userMapper::map);
    }

    public Optional<UserDTO> findByPhone(Long phone) throws AuthorizationException, AuthenticationException, TokenExpiredException {
        return userConfig.admin.ViewUserUseCase()
                .findByPhone(phone)
                .map(userMapper::map);
    }

    public SearchResult<UserDTO> find(SearchRequest request) throws AuthorizationException, AuthenticationException, TokenExpiredException {
        SearchResult<UserEntity> result = userConfig.admin.ViewUserUseCase()
                .find(request);

        return SearchResult.withItemsAndCount(
                userMapper.mapAll(result.getItems()),
                result.getCount()
        );
    }

    public SearchResult<UserDTO> findWithFilters(UserFilters userFilters) throws AuthorizationException, AuthenticationException, TokenExpiredException {
        SearchResult<UserEntity> result = userConfig.admin.ViewUserUseCase()
                .findWithFilters(userFilters);

        return SearchResult.withItemsAndCount(
                userMapper.mapAll(result.getItems()),
                result.getCount()
        );
    }

}
