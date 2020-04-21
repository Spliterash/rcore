package com.rcore.domain.role.usecase.admin;

import com.rcore.domain.role.entity.RoleEntity;
import com.rcore.domain.role.port.RoleIdGenerator;
import com.rcore.domain.role.port.RoleRepository;
import com.rcore.domain.role.access.AdminRoleCreateAccess;
import com.rcore.domain.token.exception.AuthorizationException;
import com.rcore.domain.user.entity.UserEntity;


public class RoleCreateUseCase  extends RoleAdminBaseUseCase {
    private final RoleIdGenerator idGenerator;

    public RoleCreateUseCase(UserEntity actor, RoleRepository roleRepository, RoleIdGenerator idGenerator) throws AuthorizationException {
        super(actor, roleRepository, new AdminRoleCreateAccess());
        this.idGenerator = idGenerator;
    }

    public RoleEntity create(RoleEntity roleEntity) {
        roleEntity.setId(idGenerator.generate());

        roleEntity = roleRepository.save(roleEntity);

        return roleEntity;
    }


}