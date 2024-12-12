package com.example.cinema_back_end.security.repo;


import com.example.cinema_back_end.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    @Modifying
    @Query("Update User u set u.fullName=:fullname,u.phone=:phone WHERE u.id = (:id)")
    void updateInfo(@Param("id") Integer id,
    		@Param("fullname") String fullname,@Param("phone") String phone);
    @Modifying
    @Query("Update User u set u.password=:password WHERE u.id = (:id)")
    void changePassword(@Param("id") Integer id,@Param("password") String password);

}
