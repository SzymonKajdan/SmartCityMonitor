package com.inz.inz.service;

import com.inz.inz.entity.*;
import com.inz.inz.entity.enums.ReportType;
import com.inz.inz.repository.*;
import com.inz.inz.security.model.Authority;
import com.inz.inz.security.model.AuthorityName;
import com.inz.inz.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @Autowired
    UserVotedRepository userVotedRepository;


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (authorityRepository.findByName(AuthorityName.ROLE_ADMIN) == null) {
            Authority adminRole = new Authority();
            adminRole.setName(AuthorityName.ROLE_ADMIN);

            Authority userRole = new Authority();
            userRole.setName(AuthorityName.ROLE_USER);

            authorityRepository.save(adminRole);
            authorityRepository.save(userRole);

            adminRole = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);//.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            userRole = authorityRepository.findByName(AuthorityName.ROLE_USER);//.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));

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


            BanEntity adminBan=new BanEntity();
            banEntityRepository.save(adminBan);

            UserRatingEntity userRatingEntity1 = new UserRatingEntity();
            UserRatingEntity userRatingEntity = new UserRatingEntity();
            userRatingEntity.setQuantity(100);
            userRatingEntity.setMarks(500);
            userRatingRepository.save(userRatingEntity);
            userRatingRepository.save(userRatingEntity1);


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


            user.setBanEntity(banEntity);

            user.setUserRatingEntity(userRatingEntity);

            UserVoted userVoted =new UserVoted();
            userVoted.setUserId(1l);
            userVotedRepository.save(userVoted);


            ReportRatingEntity reportRatingEntity = new ReportRatingEntity();
            reportRatingEntity.setMarks(5);
            reportRatingEntity.setQuantity(1);
            reportRatingEntity.setFalseReportQuantity(9);
            reportRatingEntity.setUsersVoted(Collections.singletonList(userVoted));
            reportRatingEntityRepository.save(reportRatingEntity);


            ReportEntity entity = new ReportEntity();
            entity.setReportRating(reportRatingEntity);
            entity.setDateReport(new Date());
            entity.setDescription("description");
            entity.setPhoto("https://firebasestorage.googleapis.com/v0/b/montoring-b23b3.appspot.com/o/img_165219.png?alt=media&token=706fb62b-7250-4539-a710-259b7379d7c6");
            // entity.setCity(cityEntity);
            entity.setReportType(ReportType.HOLE_IN_THE_ROAD);
            reportEntityRepository.save(entity);


            cityEntity.getReportList().add(entity);
            cityEntityRepository.save(cityEntity);
            user.getReportsList().add(entity);
            userRepository.save(user);

            admin.setBanEntity(adminBan);
            admin.setUserRatingEntity(userRatingEntity1);
            userRepository.save(admin);


            entity.setCity(cityEntity);
            entity.setUser(user);
            entity.setLatitude("51.771524");
            entity.setLongitude("19.547155");
            reportEntityRepository.save(entity);
            //.save(cityEntity);
            // userRepository.save(user);
            ReportRatingEntity reportRatingEntity1=new ReportRatingEntity();

        }
    }
}
