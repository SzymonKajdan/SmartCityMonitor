package com.inz.inz.scheduler;

import com.inz.inz.repository.BanEntityRepository;
import com.inz.inz.repository.UserRepository;
import com.inz.inz.entity.User;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
@Log4j2
public class BanScheduler {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BanEntityRepository banEntityRepository;


    //0 0 12 * * ?
    @Scheduled(cron = "0 * * ? * *")
    public void checkBans() {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            users.forEach(x -> {
                if (x.getBanEntity().isBanned()) {
                    Date date = x.getBanEntity().getDate();
                    DateTime dateTime = new DateTime(date);

                    if (dateTime.isBeforeNow()) {
                        x.getBanEntity().setDate(null);
                        x.getBanEntity().setBanned(false);
                        banEntityRepository.save(x.getBanEntity());
                        userRepository.save(x);
                    }
                }
            });
        }

        log.info(" banned user "+users.stream().filter(x->x.getBanEntity().isBanned()).count());
    }
}
