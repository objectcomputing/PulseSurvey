package com.objectcomputing.pulsesurvey.repositories;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.objectcomputing.pulsesurvey.model.ResponseKey;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface ResponseKeyRepository extends CrudRepository<ResponseKey, UUID> {

    @Override
    <S extends ResponseKey> List<S> saveAll(@Valid @NotNull Iterable<S> entities);
}