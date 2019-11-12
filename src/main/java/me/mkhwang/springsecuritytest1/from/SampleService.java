package me.mkhwang.springsecuritytest1.from;

import me.mkhwang.springsecuritytest1.account.Account;
import me.mkhwang.springsecuritytest1.account.AccountContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;

@Service
public class SampleService {

    public void dashboard() {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Object credentials = authentication.getCredentials();
        boolean authenticated = authentication.isAuthenticated();*/

        /*Account account = AccountContext.getAccount();
        System.out.println("====================");
        System.out.println(account.getUsername());*/

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("====================");
        System.out.println(userDetails.getUsername());
    }
}
