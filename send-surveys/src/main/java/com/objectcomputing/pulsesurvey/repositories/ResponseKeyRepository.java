package com.objectcomputing.pulsesurvey.repositories;

import com.objectcomputing.pulsesurvey.model.ResponseKey;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface ResponseKeyRepository extends CrudRepository<ResponseKey, UUID> {

    @Override
    <S extends ResponseKey> List<S> saveAll(@Valid @NotNull Iterable<S> entities);

    @Override
    <S extends ResponseKey> S save(@Valid @NotNull @NonNull S entity);


}