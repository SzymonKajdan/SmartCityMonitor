package com.inz.inz.adapter.adapterimpl;

import com.inz.inz.adapter.UserAdapter;
import com.inz.inz.entity.User;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.mapper.UserMapper;
import com.inz.inz.repository.AuthorityRepository;
import com.inz.inz.repository.BanEntityRepository;
import com.inz.inz.repository.UserRatingRepository;
import com.inz.inz.repository.UserRepository;
import com.inz.inz.resoruce.userresource.UserResourcePost;
import com.inz.inz.security.jwt.JwtTokenUtil;
import com.inz.inz.service.InitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserAdapterTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;


    @Autowired
    BanEntityRepository banEntityRepository;

    @Autowired
    UserRatingRepository userRatingRepository;

    @Autowired
    UserMapper userMapper;

    @MockBean
    InitService initService;


    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;


    @Autowired
    UserAdapter adapter = new UserAdapterImpl();


    @Test
    public void createUserTest() throws DbException {

        UserResourcePost resourcePost = new UserResourcePost();

        resourcePost.setFirstname("adam");
        resourcePost.setEmail("Adam@Adam");
        resourcePost.setPassword("xdsadsasdd");
        resourcePost.setLastname("dadsa");
        resourcePost.setUsername("DSadsa");

        User yu = adapter.createUser(resourcePost);

        assertEquals(Long.valueOf(1L), yu.getId());

    }

    @Test
    public void mapAuthUser() {

        User user = new User();

        assertNotNull(adapter.mapUserAuthResource(user));
    }


}
