package com.objectcomputing.pulsesurvey.repositories;

import com.objectcomputing.pulsesurvey.model.UserComments;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface UserCommentsRepository extends CrudRepository<UserComments, UUID> {

    @Override
    <S extends UserComments> List<S> saveAll(@Valid @NotNull Iterable<S> entities);

    @Override
    <S extends UserComments> S save(@Valid @NotNull @NonNull S entity);

}
