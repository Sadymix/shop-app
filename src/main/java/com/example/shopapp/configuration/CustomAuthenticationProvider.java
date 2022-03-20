package com.example.shopapp.configuration;

import com.example.shopapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final Set<? extends AuthenticationProvider> authenticationProviders;

    @Autowired
    public CustomAuthenticationProvider(UserService userDetailsService, PasswordEncoder passwordEncoder) {

        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(userDetailsService);

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        authenticationProviders = Set.of(preAuthenticatedAuthenticationProvider,daoAuthenticationProvider);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Class<? extends Authentication> authenticationClass = authentication.getClass();

        return authenticationProviders.stream()
                .filter(provider -> provider.supports(authenticationClass))
                .findAny()
                .map(provider -> provider.authenticate(authentication))
                .orElse(null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authenticationProviders.stream()
                .anyMatch(provider -> provider.supports(authentication));
    }
}
