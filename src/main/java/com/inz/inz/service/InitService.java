package com.inz.inz.service;

import com.inz.inz.entity.*;
import com.inz.inz.repository.*;
import com.inz.inz.seciurity.model.Authority;
import com.inz.inz.seciurity.model.AuthorityName;
import com.inz.inz.seciurity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Service
public class InitService implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    BanEntityRepository banEntityRepository;

    @Autowired
    UserRatingRepository userRatingRepository;

    @Autowired
    CityEntityRepository cityEntityRepository;

    @Autowired
    ReportRatingEntityRepository reportRatingEntityRepository;

    @Autowired
    ReportEntityRepository reportEntityRepository;


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (authorityRepository.findByName(AuthorityName.ROLE_MANAGER) == null) {
            Authority adminRole = new Authority();
            adminRole.setName(AuthorityName.ROLE_MANAGER);

            Authority userRole = new Authority();
            userRole.setName(AuthorityName.ROLE_WORKER);

            authorityRepository.save(adminRole);
            authorityRepository.save(userRole);

            adminRole = authorityRepository.findByName(AuthorityName.ROLE_MANAGER);//.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            userRole = authorityRepository.findByName(AuthorityName.ROLE_WORKER);//.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));

            User admin = new User();
            admin.setEmail("admin@email.pl");
            admin.setUsername("admin");
            admin.setLastname("admin");
            admin.setFirstname("admin");
            admin.setLastPasswordResetDate(new Date());
            admin.setEnabled(true);
            admin.setPassword(encoder.encode("qwerty"));
            admin.setAuthorities(new ArrayList<Authority>(Arrays.asList(adminRole)));


            CityEntity cityEntity = new CityEntity();

            cityEntity.setName("Lodz");
            cityEntity.setReportList(new ArrayList<>());

            cityEntityRepository.save(cityEntity);

            BanEntity banEntity = new BanEntity();

            banEntity.setBanCounter(0);
            banEntity.setBanned(false);
            banEntityRepository.save(banEntity);

            UserRatingEntity userRatingEntity = new UserRatingEntity();
            userRatingRepository.save(userRatingEntity);


            User user = new User();
            user.setEmail("user@email.pl");
            user.setUsername("user");
            user.setLastname("user");
            user.setFirstname("user");
            user.setLastPasswordResetDate(new Date());
            user.setEnabled(true);
            user.setPassword(encoder.encode("qwerty"));
            user.setAuthorities(new ArrayList<Authority>(Arrays.asList(userRole)));
            user.setReportsList(new ArrayList<>());


            user.setBanEntity(banEntity);

            user.setUserRatingEntity(userRatingEntity);

            ReportRatingEntity reportRatingEntity = new ReportRatingEntity();
            reportRatingEntity.setMarks(5);
            reportRatingEntity.setQuantity(1);
            reportRatingEntityRepository.save(reportRatingEntity);

            ReportEntity entity = new ReportEntity();
            entity.setReportRating(reportRatingEntity);
            entity.setDateReport(new Date());
            entity.setDescription("description");
            entity.setPhoto("https://firebasestorage.googleapis.com/v0/b/montoring-b23b3.appspot.com/o/IMG_20190301_161631.jpg?alt=media&token=0c9de513-8e47-40b6-b309-b17548449b4e");
            // entity.setCity(cityEntity);
            reportEntityRepository.save(entity);


            cityEntity.getReportList().add(entity);
            cityEntityRepository.save(cityEntity);
            user.getReportsList().add(entity);
            userRepository.save(user);

            userRepository.save(admin);


            entity.setCity(cityEntity);
            entity.setUser(user);
            reportEntityRepository.save(entity);
            //.save(cityEntity);
            // userRepository.save(user);
            ReportRatingEntity reportRatingEntity1=new ReportRatingEntity();


        }
    }
}
