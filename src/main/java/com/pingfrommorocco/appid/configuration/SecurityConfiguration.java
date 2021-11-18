package com.pingfrommorocco.appid.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.HashSet;
import java.util.Set;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.oauth2.client.provider.appid.issuer-uri}")
    private String issuer;

    Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/login**").authenticated()
            .antMatchers("/api/private").authenticated()
            .antMatchers("/api/public").permitAll()
            .antMatchers("/api/admin").hasAuthority("ADMIN")
            .and()
            .oauth2Login().userInfoEndpoint().userService(this.userService());
    }

    OAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return new OAuth2UserService<OAuth2UserRequest, OAuth2User>() {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
                logger.debug("Loading user from AppIdOAuth2Service");
                OAuth2AccessToken token = request.getAccessToken();
                OAuth2User user = delegate.loadUser(request);
                Jwt jwt = JwtDecoders.fromIssuerLocation(issuer).decode(token.getTokenValue());
                Set<GrantedAuthority> authorities = new HashSet<>();
                for(String role: jwt.getClaimAsStringList(("authorities"))) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                return new DefaultOAuth2User(authorities, user.getAttributes(), "preferred_username");
            }
        };
    }
}

