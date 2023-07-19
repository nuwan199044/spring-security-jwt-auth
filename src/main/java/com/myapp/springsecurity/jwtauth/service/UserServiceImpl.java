package com.myapp.springsecurity.jwtauth.service;

import com.myapp.springsecurity.jwtauth.entity.Role;
import com.myapp.springsecurity.jwtauth.entity.User;
import com.myapp.springsecurity.jwtauth.repository.RoleRepository;
import com.myapp.springsecurity.jwtauth.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(userName);
        user.orElseThrow( () -> new UsernameNotFoundException("User not in database"));
        Collection<SimpleGrantedAuthority> authorities = user.get().getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
        return new org.springframework.security.core.userdetails.User(user.get().getUserName(), user.get().getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Adding new user {} to database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Adding new role {} to database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {
        log.info("Adding role {} to user {}", roleName, userName);
        User user = userRepository.findByUserName(userName).get();
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User getUser(String userName) {
        log.info("Fetching user {} from database", userName);
        return userRepository.findByUserName(userName).get();
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }
}
