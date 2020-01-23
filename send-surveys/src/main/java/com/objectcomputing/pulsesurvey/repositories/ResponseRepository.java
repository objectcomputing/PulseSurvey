package com.objectcomputing.pulsesurvey.repositories;

import com.objectcomputing.pulsesurvey.model.Response;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface ResponseRepository extends CrudRepository<Response, UUID> {

    @Override
    <S extends Response> List<S> saveAll(@Valid @NotNull Iterable<S> entities);
}