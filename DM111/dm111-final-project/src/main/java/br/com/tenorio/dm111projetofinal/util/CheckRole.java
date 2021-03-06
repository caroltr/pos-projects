package br.com.tenorio.dm111projetofinal.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CheckRole {

    private CheckRole() {
    }

    public static boolean hasRoleAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN");
    }

    public static boolean hasRoleUser(Authentication authentication) {
        return hasRole(authentication, "USER");
    }

    private static boolean hasRole(Authentication authentication, String role) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            if (grantedAuthority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
