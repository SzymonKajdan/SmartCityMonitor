package com.inz.inz.adapter.adapterimpl;

import com.inz.inz.adapter.UserAdapter;
import com.inz.inz.emailservice.EmailService;
import com.inz.inz.entity.*;
import com.inz.inz.exceptionhandler.AuthenticationException;
import com.inz.inz.exceptionhandler.DbException;
import com.inz.inz.exceptionhandler.ErrorSpecifcation;
import com.inz.inz.exceptionhandler.Field;
import com.inz.inz.mapper.UserMapper;
import com.inz.inz.repository.AuthorityRepository;
import com.inz.inz.repository.BanEntityRepository;
import com.inz.inz.repository.UserRatingRepository;
import com.inz.inz.repository.UserRepository;
import com.inz.inz.resoruce.UserBanResource;
import com.inz.inz.resoruce.UsersLight;
import com.inz.inz.resoruce.userresource.*;
import com.inz.inz.security.jwt.JwtTokenUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAdapterImpl implements UserAdapter {

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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    EmailService emailService;

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public User createUser(UserResourcePost userResourcePost) throws DbException {
        User user;
        try {
            user = userMapper.mapUserResourcePostToUser(userResourcePost);
            createRealtions(user);
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {

            DbException dbException = new DbException();
            dbException.setCaused(ErrorSpecifcation.USEREXIST.getDetails());
            dbException.setCode(ErrorSpecifcation.USEREXIST.getCode());
            throw dbException;
        }
        return user;
    }

    @Override
    public UserAuthResoruce mapUserAuthResource(User user) {
        UserAuthResoruce userAuthResoruce;

        userAuthResoruce = userMapper.mapToUserAuthResource(user);


        return userAuthResoruce;
    }

    @Override
    public List<UserRank> getRank() {
        List<User> userRanks = userRepository.findAll();

        userRanks = userRanks.stream().limit(100).filter(x -> x.getUserRatingEntity().getQuantity() >= 100).collect(Collectors.toList());

        List<UserRank> userRankList = userRanks.stream().map(userMapper::mapToUserRank).collect(Collectors.toList());


        return userRankList.stream().sorted(Comparator.comparingDouble(UserRank::getAvgMark)).collect(Collectors.toList());

    }

    @Override
    public void sendNewPassword(String email) throws DbException {
        emailService.resetPassword(email);
    }

    @Override
    public void editUser(EditUserResource editUserResource) throws DbException {
        Optional<User> user = userRepository.findById(editUserResource.getId());
        if (user.isPresent()) {
            try {
                userRepository.save(mapUser(editUserResource, user.get()));
            } catch (DataIntegrityViolationException ex) {
                throw new DbException(ErrorSpecifcation.CREATINGERROR.getDetails() + " updadet user", ErrorSpecifcation.CREATINGERROR.getCode(), new Field());
            }
        } else {
            throw new DbException(ErrorSpecifcation.USERNOTEXIST.getDetails(), ErrorSpecifcation.USERNOTEXIST.getCode(), new Field());
        }


    }

    @Override
    public void changePassword(PasswordResourcePost password) throws DbException {
        Optional<User> user = userRepository.findById(password.getId());
        if (user.isPresent()) {

            user.get().setPassword(passwordEncoder().encode(password.getNewPassword()));

            userRepository.save(user.get());
        } else {
            throw new DbException(ErrorSpecifcation.USERNOTEXIST.getDetails(), ErrorSpecifcation.USERNOTEXIST.getCode(), null);
        }


    }

    @Override
    public LogInUserResponse authUser(LogInResourcePost authenticationRequest) throws AuthenticationException {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final User user = userRepository.findByUsername(authenticationRequest.getUsername());


        UserAuthResoruce userAuthResoruce = mapUserAuthResource(user);

        return new LogInUserResponse(token, jwtTokenUtil.getExpirationDateFromToken(token), userAuthResoruce);

    }

    @Override
    public List<UsersLight> getUsers() {

        return userRepository.findAll().stream().map(userMapper::mapToUserLigth).collect(Collectors.toList());
    }

    @Override
    public void banUser(UserBanResource userBanResource) throws DbException {
        Optional<User> user=userRepository.findById(userMapper.mapToUser(userBanResource).getId());

        if(user.isPresent()){
            user.get().getBanEntity().setBanned(true);
            DateTime dt = new DateTime()
                    .withHourOfDay(12)
                    .withMinuteOfHour(00)
                    .withSecondOfMinute(0);
            dt = dt.plusDays(1);

            user.get().getBanEntity().setDate(dt.toDate());
            banEntityRepository.save(user.get().getBanEntity());
        }else {
            throw new DbException(ErrorSpecifcation.USERNOTEXIST.getDetails(),ErrorSpecifcation.USERNOTEXIST.getCode(),null, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void unBanUser(UserBanResource userBanResource) throws DbException {
        Optional<User> user=userRepository.findById(userMapper.mapToUser(userBanResource).getId());

        if(user.isPresent()){
            user.get().getBanEntity().setBanned(false);

            user.get().getBanEntity().setDate(null);
            banEntityRepository.save(user.get().getBanEntity());
        }else {
            throw new DbException(ErrorSpecifcation.USERNOTEXIST.getDetails(),ErrorSpecifcation.USERNOTEXIST.getCode(),null, HttpStatus.NOT_FOUND);
        }
    }

    private void createRealtions(User user) throws DbException {
        BanEntity banEntity = new BanEntity();
        UserRatingEntity userRatingEntity = new UserRatingEntity();
        try {
            banEntity.setBanCounter(0);
            banEntity.setBanned(false);
            banEntityRepository.save(banEntity);

            userRatingRepository.save(userRatingEntity);

        } catch (DataIntegrityViolationException ex) {

            DbException dbException = new DbException();
            dbException.setCaused(ErrorSpecifcation.CREATINGERROR.getDetails() + "User");
            dbException.setCode(ErrorSpecifcation.CREATINGERROR.getCode());
            throw dbException;
        }
        user.setBanEntity(banEntity);
        user.setUserRatingEntity(userRatingEntity);
        user.setReportsList(new ArrayList<>());
        Authority userRole = authorityRepository.findByName(AuthorityName.ROLE_USER);
        user.setAuthorities(new ArrayList<Authority>(Collections.singleton(userRole)));
    }


    private User mapUser(EditUserResource editUserResource, User user) {
        if (editUserResource.getUsername() != null && !editUserResource.getUsername().isEmpty() && !user.getUsername().equals(editUserResource.getUsername()))
            user.setUsername(editUserResource.getUsername());

        if (editUserResource.getEmail() != null && !editUserResource.getEmail().isEmpty() && !user.getEmail().equals(editUserResource.getEmail()))
            user.setEmail(editUserResource.getEmail());

        if (editUserResource.getFirstname() != null && !editUserResource.getFirstname().isEmpty() && !user.getFirstname().equals(editUserResource.getFirstname()))
            user.setFirstname(editUserResource.getFirstname());
        if (editUserResource.getSurname() != null && !editUserResource.getSurname().isEmpty() && !user.getLastname().equals(editUserResource.getSurname()))
            user.setLastname(editUserResource.getSurname());

        return user;
    }


    private void authenticate(String username, String password) throws AuthenticationException {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {

            throw new AuthenticationException("User disbaled", "A1");
        } catch (BadCredentialsException e) {

            throw new AuthenticationException("Bad credintals", "A1");
        }
    }
}
