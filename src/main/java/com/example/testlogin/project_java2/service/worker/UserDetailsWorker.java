package com.example.testlogin.project_java2.service.worker;

import com.example.testlogin.project_java2.mapper.RoleMapper;
import com.example.testlogin.project_java2.model.UserAccount;
import com.example.testlogin.project_java2.repo.UserRepo;
import com.example.testlogin.project_java2.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsWorker implements CustomUserDetailsService {

    private final UserRepo userRepository;

    @Autowired
    public UserDetailsWorker(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserAccount user = userRepository.findByEmail(email);

        if(user != null) {

            User userDetail = new User(
                    user.getEmail(),
                    user.getPassword(),
                    RoleMapper.mapRolesToAuthorities(user.getRoles())
            );
            System.out.println("USER LOGIN DETAIL: "+userDetail);

            return userDetail;

        } else {

            throw new UsernameNotFoundException("Invalid username or password!!!");
        }
    }
}
