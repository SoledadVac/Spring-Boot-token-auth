# 本项目使用Spring Boot+Spring security+Jwt实现Restful接口的安全性访问demo


# 一，协议
## token验证未通过
返回：

```
{
    "header": {
        "errorinfo": "无效的token",
        "errorcode": "8001"
    }
}
```
页面上对这种情况的处理，都跳转到登陆页面；

## 登陆验证未通过
返回：

```
{
    "header": {
        "errorinfo": "用户名或密码错误，请重新输入！",
        "errorcode": "8002"
    }
}
```
前端页面对这种情况的处理，清空用户名和密码，重新输入；
## 其他正常情况
按照数据接口的定义，正常交互，参考系统接口协议定义；

# JWT
参考文档[JWT文档](https://jwt.io/introduction/)

# 后端配置

## application.yml

```
# JWT 认证配置
jwt:
  header: Authorization
  secret: w-oasis123456
  expiration: 604800 #token七天不过期
  tokenHead: "Bearer "
  exceptUrl: "/auth/**"
```

## 用户认证相关：
 
 自定义JwtUser，实现spring security 的UserDetails类，用于用户的认证：
 
 
```
/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午10:32
 * \* Description: 为了安全服务的User
 * \
 */
public class JwtUser  implements UserDetails {

    private final Long id;
    private final String username; //设置为account
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(Long id, String username, String password,  Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * 返回分配给用户的角色列表
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * 账户是否未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否未过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否激活
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }



}

```

自定义类，实现UserDetailsService 的认证方法：

```
/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午10:54
 * \* Description: 提供一种从用户名可以查到用户并返回的方
 * \
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    /**
     * 提供一种从用户名可以查到用户并返回的方法【本系统使用手机号account进行唯一用户验证】
     * @param account
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        /**TODO:此处需要写明从用户表里面跟根据用户account查询用户的方法**/
        User user =new User();
        user.setAccount("17319237587");
        user.setPwd("123");
        user.setUserId(1L);
        List<String> roles=new ArrayList<>();
        roles.add("ADMIN");
        user.setRoles(roles);
        return JwtUserFactory.create(user);
    }
}

```
配置数据库实体类跟认证类：

```
/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午10:43
 * \* Description: factory:根据User创建JwtUser
 * \
 */
public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getUserId(),
                user.getAccount(),//account是唯一的
                user.getPwd(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}

```
## token

token操作类：

```
/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/27
 * \* Time: 下午3:12
 * \* Description:
 * \
 */
@Component
public class JwtUtil {


    private static final String CLAIM_KEY_USER_ACCOUNT = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret; //秘钥

    @Value("${jwt.expiration}")
    private Long expiration; //过期时间

    /**
     * 从token中获取用户account
     * @param token
     * @return
     */
    public String getUserAccountFromToken(String token) {
        String useraccount;
        try {
            final Claims claims = getClaimsFromToken(token);
            useraccount = claims.getSubject();
        } catch (Exception e) {
            useraccount = null;
        }
        return useraccount;
    }

    /**
     * 从token中获取创建时间
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 获取token的过期时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 从token中获取claims
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生存token的过期时间
     * @return
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        Boolean result= expiration.before(new Date());
        return result;
    }



    /**
     * 生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ACCOUNT, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * token 是否可刷新
     * @param token
     * @return
     */
    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证token
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final String useraccount = getUserAccountFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        Boolean result= (
                useraccount.equals(user.getUsername())
                        && !isTokenExpired(token)
        );
        return result;
    }
}


```

验证token的filter配置：


```
/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午11:23
 * \* Description: 验证token
 * \
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            String useraccount = jwtUtil.getUserAccountFromToken(authToken);
            logger.info("JwtAuthenticationTokenFilter[doFilterInternal] checking authentication " + useraccount);

            if (useraccount != null && SecurityContextHolder.getContext().getAuthentication() == null) {//token校验通过

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(useraccount);//根据account去数据库中查询user数据，足够信任token的情况下，可以省略这一步

                if (jwtUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            httpServletRequest));
                    logger.info("JwtAuthenticationTokenFilter[doFilterInternal]  authenticated user " + useraccount + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}

```
配置filter以及拦截url：


```
/**
 * \* Created: liuhuichao
 * \* Date: 2017/10/30
 * \* Time: 上午11:01
 * \* Description:spring security 的安全配置类
 * \
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.exceptUrl}")
    private String exceptUrl;


    /**
     * 用户名密码认证方法
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.userDetailsService);
    }

    /**
     * 装载BCrypt密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * token请求授权
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()//未授权处理

                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()


                // 对于获取token的rest api要允许匿名访问
                .antMatchers(exceptUrl).permitAll()

                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 添加JWT filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);  //将token验证添加在密码验证前面

        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

}

```
处理异常：

```
/**
 * jwt 未授权
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject result=new JSONObject();
        JSONObject header=new JSONObject();
        if(authException instanceof BadCredentialsException){ /**身份认证未通过*/
            header.put("errorcode","8002");
            header.put("errorinfo","用户名或密码错误，请重新输入！");
            result.put("header",header);
        }else{
            header.put("errorcode","8001");
            header.put("errorinfo","无效的token");
            result.put("header",header);
        }
        response.getWriter().write(JSONObject.toJSONString(result));
    }
}
```


接口测试：

![postman测试图](postman_test.jpeg)
