// com/Ecommerce/tubes_PBO/security/CustomUserDetailsService.java
package com.Ecommerce.tubes_PBO.security;

import com.Ecommerce.tubes_PBO.model.User; // Model JPA Anda
import com.Ecommerce.tubes_PBO.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new UserPrincipal(user);
    }
    public static class UserPrincipal implements UserDetails {
        private final User user; 

        public UserPrincipal(User user) {
            this.user = user;
        }

        public User getUser() { 
            return user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
            return Collections.singletonList(authority);
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getUsername();
        }
    }
}