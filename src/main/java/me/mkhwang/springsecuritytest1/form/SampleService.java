package me.mkhwang.springsecuritytest1.form;

import me.mkhwang.springsecuritytest1.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    //@Async
    public void ayncService() {
        SecurityLogger.log("Async Service");
        System.out.println("Async service is called");
    }
}
