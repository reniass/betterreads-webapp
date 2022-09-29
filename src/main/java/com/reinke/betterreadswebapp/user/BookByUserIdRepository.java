package com.reinke.betterreadswebapp.user;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookByUserIdRepository extends CassandraRepository<BookByUserId, String> {


    // SELECT * FROM bookbyuserid WHERE user_id = ?
    // query for a rows where user_id = ? return a list of bookbyuserid
    //@Query("select b FROM bookbyuserid b where b.userId = :userId")
    //public Optional<List<BookByUserId>> findByUserId(@Param("userId") String userId);

    List<BookByUserId> findByUserId(String userId);

}
