package com.inz.inz.controller;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.security.AdapterImpl.UserAdapterImpl;
import com.inz.inz.resoruce.userResource.UserResourcePost;
import com.inz.inz.security.controller.UserRestController;
import com.inz.inz.security.jwt.JwtUser;
import com.inz.inz.security.model.User;
import com.inz.inz.security.service.UserTokenReciver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRestControllerTest {
    @MockBean
    UserAdapterImpl userAdapter;

    @MockBean
    UserTokenReciver userTokenRecvier;

    @Autowired
    UserRestController userRestControlle;

    @Test
    public void getTest() {
        HttpServletRequest servletRequest = new MockHttpServletRequest();
        JwtUser jwtUser = new JwtUser(1l, "Admin", "Admin", "t", "a@A", "SADad", null, true, new Date());
        when(userTokenRecvier.getAuthenticatedUser(servletRequest)).thenReturn(jwtUser);

        assertNotNull(userRestControlle.getAuthenticatedUser(servletRequest));

    }

    @Test
    public void postTest() throws DbException {
        UserResourcePost resourcePost=new UserResourcePost();
        resourcePost.setPassword("dsa");
        resourcePost.setUsername("admin");
        resourcePost.setLastname("das");
        resourcePost.setEmail("@S2@");
        resourcePost.setFirstname("dsa");

        when(userAdapter.createUser(resourcePost)).thenReturn(new User());

        ResponseEntity<?>responseEntity=userRestControlle.addUser(resourcePost);
        assertEquals(201,responseEntity.getStatusCode().value());
    }
}
