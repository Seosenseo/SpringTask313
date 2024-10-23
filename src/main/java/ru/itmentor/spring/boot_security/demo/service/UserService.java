package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.model.EnumRole;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.repository.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public void createUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Set<Role> roles = new HashSet<>();
        roles.add(findByRoleName(EnumRole.ROLE_USER));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void clearUsers() {
        userRepository.deleteAll();
    }

    public User updateUser(User user) {
        userRepository.save(user);
        return user;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        User user = null;
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        return user;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Role findByRoleName(EnumRole roleName) {
        return roleRepository.findRoleByName(roleName);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username + "not found!");
        else
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), rolesToAuthority(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> rolesToAuthority(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}