package npc.bikathi.multilogin.config;

import npc.bikathi.multilogin.config.exceptions.CustomAccessDeniedHandler;
import npc.bikathi.multilogin.config.exceptions.CustomAuthenticationEntryPoint;
import npc.bikathi.multilogin.config.filter.JwtValidationFilter;
import npc.bikathi.multilogin.config.provider.ExternalEntityAuthenticationProvider;
import npc.bikathi.multilogin.config.provider.InternalEntityAuthenticationProvider;
import npc.bikathi.multilogin.config.util.ExternalEntityUserDetailsService;
import npc.bikathi.multilogin.config.util.InternalEntityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private InternalEntityUserDetailsService internalEntityUserDetailsService;

    @Autowired
    private ExternalEntityUserDetailsService externalEntityUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(
            sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
           authorizationManagerRequestMatcherRegistry.requestMatchers(
               antMatcher("/api/**/login"),
               antMatcher("/api/**/signup"))
           .permitAll();
           authorizationManagerRequestMatcherRegistry.requestMatchers(antMatcher("/api/**/get-entities")).authenticated();
        });
        httpSecurity.addFilterAt(new JwtValidationFilter(), BasicAuthenticationFilter.class);
        httpSecurity.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        return httpSecurity.build();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        InternalEntityAuthenticationProvider internalEntityAuthenticationProvider =
            new InternalEntityAuthenticationProvider(internalEntityUserDetailsService, passwordEncoder());

        ExternalEntityAuthenticationProvider externalEntityAuthenticationProvider =
            new ExternalEntityAuthenticationProvider(externalEntityUserDetailsService, passwordEncoder());

        ProviderManager providerManager = new ProviderManager(internalEntityAuthenticationProvider, externalEntityAuthenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }
}
