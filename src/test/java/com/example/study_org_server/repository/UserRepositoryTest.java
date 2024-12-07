package com.example.study_org_server.repository;

import com.example.study_org_server.repository.user.UserRecord;
import com.example.study_org_server.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@MybatisTest
public class UserRepositoryTest {
    private final String emailA = "admin@example.com";
    private final String emailA_dummy = "dummy@example.com";


    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void findByEmail_ShouldReturnUserRecord(){
        Optional<UserRecord> record = userRepository.findByEmail(emailA);
        Assertions.assertTrue(record.isPresent());
    }
    @Test
    public void findByEmail_ShouldReturnNull(){
        Optional<UserRecord> record = userRepository.findByEmail(emailA_dummy);
        Assertions.assertTrue(record.isEmpty());
    }

    @Test
    public void isExistEmail_ShouldReturnTrue(){
        Assertions.assertEquals(1,userRepository.isExistEmail(emailA));
    }

    @Test
    public void isExistEmail_ShouldReturnFalse(){
        Assertions.assertEquals(0,userRepository.isExistEmail(emailA_dummy));
    }

    @Test
    public void update_ShouldUpdateDB(){
        userRepository.update(emailA,"ChangedPass");
        UserRecord result =userRepository.findByEmail(emailA).orElseThrow(()->new RuntimeException(""));
        Assertions.assertEquals("ChangedPass", result.password());
    }




}
