package com.example.study_org_server.service.user;

import com.example.study_org_server.repository.user.UserRecord;
import com.example.study_org_server.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.example.model.LoginUserForm;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "getUser", key = "'users/' + #email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserRecord user= userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        // UserDetailsを返却
        return new org.springframework.security.core.userdetails.User(
                user.email(),
                user.password(),
                Collections.singleton(new SimpleGrantedAuthority(user.authorityId())) // 権限を設定
        );
    }
    @Transactional
    public String signUp(LoginUserForm loginUserForm){
        String email=loginUserForm.getEmail();
        UserRecord user= userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        if(user.password().isBlank()){
            String hashedPassword = new BCryptPasswordEncoder().encode(loginUserForm.getPassword());
            userRepository.update(email,hashedPassword);
            return email;
        }else{
            throw new RuntimeException("すでに初期化済みのアカウントです");
        }
    }
    @Transactional
    @CacheEvict(value = "getUser",key = "'users/' +#loginUserForm.email")
    public String changePassword(LoginUserForm loginUserForm){
        String email=loginUserForm.getEmail();
        UserRecord user= userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        if(!user.password().isBlank()){
            String hashedPassword = new BCryptPasswordEncoder().encode(loginUserForm.getPassword());
            userRepository.update(email,hashedPassword);
            return email;
        }else{
            throw new RuntimeException("不正な操作です");
        }
    }

    public boolean isExistUser(String email){
        return userRepository.isExistEmail(email)>0;
    }

    //キャッシュの更新
    //@CachePut(value = "users", key = "#user.id")

    //キャッシュの削除
    //@CacheEvict(value = "users", key = "#id")

    //キャッシュ全体のクリア
    //@CacheEvict(value = "users", allEntries = true)


}
