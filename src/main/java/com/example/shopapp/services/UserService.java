package com.example.shopapp.services;

import com.example.shopapp.exceptions.ResourceNotFoundException;
import com.example.shopapp.mappers.UserMapperImpl;
import com.example.shopapp.models.dto.UserDto;
import com.example.shopapp.models.entity.User;
import com.example.shopapp.repositories.RoleRepo;
import com.example.shopapp.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final UserMapperImpl userMapper;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final TokenStore tokenStore;

    public UserDto getUser(Long id){
        return userMapper.toDto(getUserById(id));
    }

    public UserDto addUser(UserDto userDto) {
        var user = userMapper.toEntity(userDto);
        var roleList = userDto.getRoles().stream()
                .map(roleRepo::findByName)
                .toList();
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(roleList);
        var userSave = userRepo.save(user);
        return userMapper.toDto(userSave);
    }

    public UserDto activateUser(Long id) {
        return setUserEnabled(id, true);
    }

    public UserDto deactivateUser(Long id) {
        return setUserEnabled(id, false);
    }

    public UserDto setUserRoles(Long id, List<String> roleStringList) {
        var user = getUserById(id);
        var roleList = roleStringList.stream()
                .map(roleRepo::findByName)
                .toList();
        user.setRoles(roleList);
        var userSave = userRepo.save(user);
        return userMapper.toDto(userSave);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepo.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        return (UserDetails) Optional.of(token)
                .map(PreAuthenticatedAuthenticationToken::getPrincipal)
                .map(Object::toString)
                .map(tokenStore::readAuthentication)
                .map(OAuth2Authentication::getUserAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .orElseThrow(() -> new UsernameNotFoundException(token.getName()));
    }

    private User getUserById(Long id) {
        return userRepo.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("User with id " + id +" not found")
        );
    }

    private UserDto setUserEnabled(Long id, boolean enabled) {
        var user = getUserById(id);
        user.setEnabled(enabled);
        var userSave = userRepo.save(user);
        return userMapper.toDto(userSave);
    }

}
