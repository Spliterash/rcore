package com.rcore.database.mongo.auth.token.port;

import com.rcore.database.mongo.auth.token.mapper.AccessTokenDocMapper;
import com.rcore.database.mongo.auth.token.model.AccessTokenDoc;
import com.rcore.database.mongo.auth.token.query.DeactivateAllAccessTokenByRefreshTokenIdQuery;
import com.rcore.database.mongo.auth.token.query.ExpireAccessTokenByRefreshTokenQuery;
import com.rcore.database.mongo.auth.token.query.ExpireAccessTokenQuery;
import com.rcore.database.mongo.auth.token.query.FindAccessTokensQuery;
import com.rcore.database.mongo.commons.utils.CollectionNameUtils;
import com.rcore.domain.auth.token.entity.AccessTokenEntity;
import com.rcore.domain.auth.token.port.AccessTokenRepository;
import com.rcore.domain.auth.token.port.filter.AccessTokenFilters;
import com.rcore.domain.commons.port.dto.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class MongoAccessTokenRepository implements AccessTokenRepository {

    private final static String collectionName = CollectionNameUtils.getCollectionName(AccessTokenDoc.class);
    private final MongoTemplate mongoTemplate;
    private final AccessTokenDocMapper mapper;

    @Override
    public void expireAccessToken(String accessTokenId) {
        ExpireAccessTokenQuery expireAccessTokenQuery = ExpireAccessTokenQuery.of(accessTokenId);
        mongoTemplate.updateFirst(expireAccessTokenQuery.getQuery(), expireAccessTokenQuery.getUpdate(), AccessTokenDoc.class);
    }

    @Override
    public void expireAllAccessTokenByRefreshTokenId(String refreshTokenId) {
        ExpireAccessTokenByRefreshTokenQuery expireAccessTokenByRefreshTokenQuery = ExpireAccessTokenByRefreshTokenQuery.of(refreshTokenId);
        mongoTemplate.updateFirst(expireAccessTokenByRefreshTokenQuery.getQuery(), expireAccessTokenByRefreshTokenQuery.getUpdate(), AccessTokenDoc.class);
    }

    @Override
    public void deactivateAllAccessTokenByRefreshTokenId(String refreshTokenId) {
        DeactivateAllAccessTokenByRefreshTokenIdQuery deactivateAllAccessTokenByRefreshTokenIdQuery = DeactivateAllAccessTokenByRefreshTokenIdQuery.of(refreshTokenId);
        mongoTemplate.updateFirst(deactivateAllAccessTokenByRefreshTokenIdQuery.getQuery(), deactivateAllAccessTokenByRefreshTokenIdQuery.getUpdate(), AccessTokenDoc.class);
    }

    @Override
    public AccessTokenEntity save(AccessTokenEntity entity) {
        mongoTemplate.save(mapper.map(entity), collectionName);
        return entity;
    }

    @Override
    public Boolean delete(String s) {
        return mongoTemplate.remove(Query.query(Criteria.where("_id").is(s)), collectionName).getDeletedCount() > 0;
    }

    @Override
    public Optional<AccessTokenEntity> findById(String s) {
        return Optional.ofNullable(mongoTemplate.findById(s, AccessTokenDoc.class))
                .map(mapper::inverseMap);
    }

    @Override
    public SearchResult<AccessTokenEntity> find(AccessTokenFilters filters) {
        Query query = new FindAccessTokensQuery(filters).getQuery();
        return SearchResult.withItemsAndCount(
                mongoTemplate.find(query, AccessTokenDoc.class)
                        .stream()
                        .map(mapper::inverseMap)
                        .collect(Collectors.toList()),
                mongoTemplate.count(query.limit(0).skip(0), AccessTokenDoc.class)
        );
    }

    @Override
    public Long count() {
        return mongoTemplate.count(new Query(), AccessTokenDoc.class);
    }
}