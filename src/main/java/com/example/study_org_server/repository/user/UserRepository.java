package com.example.study_org_server.repository.user;

import lombok.Value;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

@Mapper
public interface UserRepository {

    @Select("SELECT * FROM T_USER WHERE email = #{email}")
    Optional<UserRecord> findByEmail(String email);

    @Select("SELECT count(*) FROM T_USER WHERE email = #{email}")
    int isExistEmail(String email);

    @Update("UPDATE T_USER SET password=#{hashedPassword} WHERE email = #{email}")
    void update(String email,String hashedPassword);

}
