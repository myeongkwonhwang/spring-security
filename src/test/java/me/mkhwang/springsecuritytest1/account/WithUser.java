package me.mkhwang.springsecuritytest1.account;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "mkhwang", roles = "USER")
public @interface WithUser {
}
