package com.inz.inz.adapter.adapterimpl;

import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.mapper.UserMapper;
import com.inz.inz.repository.AuthorityRepository;
import com.inz.inz.repository.BanEntityRepository;
import com.inz.inz.repository.UserRatingRepository;
import com.inz.inz.repository.UserRepository;
import com.inz.inz.resoruce.userresource.UserResourcePost;
import com.inz.inz.adapter.UserAdapter;
import com.inz.inz.entity.User;
import com.inz.inz.service.InitService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserAdapterMockTest {



    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthorityRepository authorityRepository;



    @MockBean
    BanEntityRepository banEntityRepository;

    @MockBean
    UserRatingRepository userRatingRepository;

    @MockBean
    UserMapper userMapper;

    @MockBean
    InitService initService;

    @Autowired
    UserAdapter adapter=new UserAdapterImpl();

    @Test(expected = DbException.class)
    public void saveEexceptionTest() throws DbException {
        when(banEntityRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        adapter.createUser(new UserResourcePost());
    }
    @Test(expected = DbException.class)
    public void saveExceptionTest() throws DbException {
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        when(userMapper.mapUserResourcePostToUser(any())).thenReturn(new User());
        adapter.createUser(new UserResourcePost());
    }

}
