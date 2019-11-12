# SpringSecurity

[백기선님]: https://docs.google.com/document/d/19DzeBekximHRXCaeN-UhFewrb9KTo65xj7Rrt9jO_Sk/edit#



spring security 의존성 추가시 모든 요청은 인증을 필요로 한다.

기본유저 생성 - username - user / password - 콘솔에 출력



테스트시

@Transactional

완료 후 롤백 함.



#### SecurityContextHolder와 Authentication

##### SecurityContextHolder

- SecurityContext 제공, 기본적으로 ThreadLocal을 사용

  ThreadLocal이란?

  Thread내에서 share하는 저장소

  Methodparameter를 사용하지 않아도 ThreadLocal에 있는 데이터를 접근 할 수 있다.

- SecurityContext

  Autentication제공



##### Autentication

- Principal과 GrantAuthority 제공.

##### Principal

- UserDetails interface의 구현체에서 설정한 user정보가 저장
- “누구"에 해당하는 정보.
-  **UserDetailsService에서 리턴한 그 객체.**
- 객체는 UserDetails 타입.

##### GrantAuthority

- “ROLE_USER”, “ROLE_ADMIN”등 Principal이 가지고 있는 “권한”을 나타낸다.
- 인증 이후, 인가 및 권한 확인할 때 이 정보를 참조한다.

```java
return User.builder()
        .username(account.getUsername())
        .password(account.getPassword())
        .roles(account.getRole())
        .build();
```

##### UserDetails

- 애플리케이션이 가지고 있는 유저 정보와 스프링 시큐리티가 사용하는 Authentication 객체 사이의 어댑터.

##### UserDetailsService

- 유저 정보를 UserDetails 타입으로 가져오는 DAO (Data Access Object) 인터페이스.



#### AuthenticationManager와 Authentication

스프링 시큐리티에서 인증(Authentication)은 AuthenticationManager가 한다.

````java
Authentication authenticate(Authentication authentication) throws AuthenticationException;
````

- 인자로 받은 Authentication이 유효한 인증인지 확인하고 Authentication 객체를 리턴한다.

- 인증을 확인하는 과정에서 비활성 계정- DisabledException, 잘못된 비번 - BadCredentialException, 잠긴 계정- LockedException 등의 에러를 던질 수 있다.

  

##### ProviderMananger

DaoAuthenticationProvider.java

getUserDetailsService().loadUserByUsername();

##### 인자로 받은 Authentication

- 사용자가 입력한 인증에 필요한 정보(username, password)로 만든 객체. (폼 인증인 경우)

- Authentication

  ​	Principal: “mkhwang”, Credentials: “123”

##### 유효한 인증인지 확인

- 사용자가 입력한 password가 UserDetailsService를 통해 읽어온 UserDetails 객체에 들어있는 password와 일치하는지 확인

- 해당 사용자 계정이 잠겨 있진 않은지, 비활성 계정은 아닌지 등 확인

  

##### Authentication 객체를 리턴

- Authentication
  - Principal: UserDetailsService에서 리턴한 그 객체 (User)
  - Credentials: GrantedAuthorities



#### ThreadLocal

Java.lang 패키지에서 제공하는 쓰레드 범위 변수. 즉, 쓰레드 수준의 데이터 저장소.

- 같은 쓰레드 내에서만 공유.
- 따라서 같은 쓰레드라면 해당 데이터를 메소드 매개변수로 넘겨줄 필요 없음.
- SecurityContextHolder의 기본 전략.

```java
public class AccountContext {

    private static final ThreadLocal<Account> ACCOUNT_THREAD_LOCAL
            = new ThreadLocal<>();

    public static void setAccount(Account account) {
        ACCOUNT_THREAD_LOCAL.set(account);
    }

    public static Account getAccount(){
        return ACCOUNT_THREAD_LOCAL.get();
    }
}
```

SecurityContext에서 사용과 동일



#### Authentication과 SecurityContextHolder

AuthenticationManager가 인증을 마친 뒤 리턴 받은 Authentication 객체의 행방은?

매번 인증해야하는가??

UsernamePasswordAuthenticationFilter를 이용히서 폼인증시 시큐리티 처리

SecurityContextPersistenceFilter를 이용해 HTTP session에 캐시하여 여러 요청에 Authentication을 공유하는 필터

##### UsernamePasswordAuthenticationFilter

- 폼 인증을 처리하는 시큐리티 필터

- 인증된 Authentication 객체를 SecurityContextHolder에 넣어주는 필터- 
- SecurityContextHolder.getContext().setAuthentication(authentication)

##### SecurityContextPersistenceFilter

- SecurityContext를 HTTP session에 캐시(기본 전략)하여 여러 요청에서 Authentication을 공유하는 필터.
- SecurityContextRepository를 교체하여 세션을 HTTP session이 아닌 다른 곳에 저장하는 것도 가능하다.
  - HttpSessionSecurityContextRepository  < ==



인증이 완료된 Authentication을 AbstractAuthenticationProcessingFilter에서

securityContextHolder에 Authentication저장



#### 스프링 시큐리티 Filter와 FilterChainProxy

스프링 시큐리티가 제공하는 필터들

1. WebAsyncManagerIntergrationFilter

2. **SecurityContextPersistenceFilter**

3. HeaderWriterFilterCsrfFilter

4. LogoutFilter

5. **UsernamePasswordAuthenticationFilter**

6. DefaultLoginPageGeneratingFilter

7. DefaultLogoutPageGeneratingFilter

8. BasicAuthenticationFilter

9. RequestCacheAwareFtiler

10. SecurityContextHolderAwareReqeustFilter

11. AnonymouseAuthenticationFilter

12. SessionManagementFilter

13. ExeptionTranslationFilterFilter

14. SecurityInterceptor

이 모든 필터는 **FilterChainProxy**가 호출한다.

FilterChainProxy getFilters