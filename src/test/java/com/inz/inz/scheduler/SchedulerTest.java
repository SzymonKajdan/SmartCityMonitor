package com.inz.inz.scheduler;

import com.inz.inz.entity.BanEntity;
import com.inz.inz.repository.BanEntityRepository;
import com.inz.inz.repository.UserRepository;
import com.inz.inz.seciurity.model.User;
import com.inz.inz.service.InitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulerTest {
    @MockBean
    InitService initService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    BanEntityRepository banEntityRepository;
    @Autowired
    BanScheduler banScheduler;

    public static User getUsers() {
        BanEntity banEntity = new BanEntity();
        banEntity.setBanCounter(4);
        banEntity.setDate(new Date());
        banEntity.setBanned(true);

        User user = new User();
        user.setBanEntity(banEntity);
        return user;
    }

    @Test
    public void schedulerTest() {
        List<User> users = new ArrayList<>();
        users.add(getUsers());

        when(userRepository.findAll()).thenReturn(users);
        banScheduler.checkBans();

    }
}
