package com.solvedge.solveIt;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = OAuth2ExampleApp.class)
@AutoConfigureMockMvc
public class MockMvcOAuth2Test {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAuthenticationInfo() throws Exception {
        OAuth2AuthenticationToken principal = buildPrincipal();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            new SecurityContextImpl(principal));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                .session(session))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("priyaforsolvedge@gmail.com"))
            .andExpect(jsonPath("$.id").value("my-id"));
    }

    private static OAuth2AuthenticationToken buildPrincipal() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "my-id");
        attributes.put("email", "priyaforsolvedge@gmail.com");

        List<GrantedAuthority> authorities = Collections.singletonList(
                new OAuth2UserAuthority("ROLE_USER", attributes));
        OAuth2User user = new DefaultOAuth2User(authorities, attributes, "sub");
        return new OAuth2AuthenticationToken(user, authorities, "whatever");
    }
}