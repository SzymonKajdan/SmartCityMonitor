package com.inz.inz.adapter.adapterImpl;

import com.inz.inz.exceptionhandler.*;
import com.inz.inz.adapter.ReportAdapter;
import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.entity.UserVoted;
import com.inz.inz.mapper.ReportMapper;
import com.inz.inz.repository.*;
import com.inz.inz.resoruce.MarkResourcePost;
import com.inz.inz.resoruce.NotActiveResource;
import com.inz.inz.resoruce.ReportResource;
import com.inz.inz.resoruce.ReportResourcePost;
import com.inz.inz.seciurity.model.User;
import com.inz.inz.seciurity.service.UserTokenReciver;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class ReportAdapterImpl implements ReportAdapter {

    @Autowired
    UserTokenReciver userTokenRecvier;

    @Autowired
    ReportMapper reportMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReportRatingEntityRepository reportRatingEntityRepository;

    @Autowired
    ReportEntityRepository reportEntityRepository;

    @Autowired
    CityEntityRepository cityEntityRepository;

    @Autowired
    UserVotedRepository userVotedRepository;

    @Override
    public ReportEntity createReport(HttpServletRequest request, ReportResourcePost reportResourcePost) throws DbException, EnumExcpetion, AuthenticationException {
        Long id = getUserId(request);
        ReportEntity reportEntity = reportMapper.mapToEntity(reportResourcePost);
        reportEntity.setUser(userRepository.findById(id).get());

        checkBanned(userRepository.findById(id).get());

        try {
            save(reportEntity, id);
        } catch (DataIntegrityViolationException ex) {
            DbException dbException = new DbException();
            dbException.setCaused(ErrorSpecifcation.CREATINGERROR.getDetails() + " Report");
            dbException.setCode(ErrorSpecifcation.CREATINGERROR.getCode());
            throw dbException;
        }
        return reportEntity;
    }

    @Override
    public Optional<ReportEntity> markAsFalse(NotActiveResource notActiveResource) throws AuthenticationException, DbException {
        Optional<User> user = userRepository.findById(notActiveResource.getUserId());
        Optional<ReportEntity> reportEntity = reportEntityRepository.findById(notActiveResource.getReportId());

        if (user.isPresent()) {
            checkBanned(user.get());
            checkReport(reportEntity, user.get().getId());
            checkisMarkedAsFalse(reportEntity.get(), user.get().getId());
            setFalseReport(reportEntity.get(), user.get());
            checkReportFalseActivity(reportEntity.get());
        } else {
            userNotExist(notActiveResource.getUserId());
        }
        return reportEntity;
    }


    @Override
    public Optional<ReportEntity> markAsNotActive(NotActiveResource notActiveResource) throws ExceptionModel, AuthenticationException {

        Optional<User> user = userRepository.findById(notActiveResource.getUserId());
        Optional<ReportEntity> reportEntity = reportEntityRepository.findById(notActiveResource.getReportId());

        if (user.isPresent()) {
            checkBanned(user.get());
            checkReport(reportEntity, user.get().getId());
            checkIsMarkedAsNotActive(reportEntity.get(), user.get().getId());
            setNotActive(reportEntity.get(), user.get());
            if(reportEntity.get().getReportRating().getNotActiveCounter()>10) {
                checkReportActivity(reportEntity.get());
            }

        } else {
            userNotExist(notActiveResource.getUserId());
        }
        return reportEntity;
    }



    @Override
    public ReportResource getReport(Long id) throws DbException {
        Optional<ReportEntity> reportEntity = reportEntityRepository.findById(id);

        if (reportEntity.isPresent()) {
            return reportMapper.mapToReport(reportEntity.get());
        } else {
            throw new DbException(ErrorSpecifcation.RESURCENOTEXIST.getDetails() + " report", ErrorSpecifcation.RESURCENOTEXIST.getCode(), new Field(), HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    protected ReportEntity save(ReportEntity reportEntity, long id) {

        reportEntity.setReportRating(reportRatingEntityRepository.save(new ReportRatingEntity()));

        CityEntity cityEntity = cityEntityRepository.findByName(reportEntity.getCity().getName()).get();
        reportEntityRepository.save(reportEntity);
        cityEntity.getReportList().add(reportEntity);
        cityEntityRepository.save(cityEntity);
        User u = userRepository.findById(id).get();
        u.getReportsList().add(reportEntity);
        userRepository.save(u);
        return reportEntity;
    }

    public Optional<ReportEntity> addMArk(MarkResourcePost markResourcePost) throws DbException, AuthenticationException {
        Optional<User> user = userRepository.findById(markResourcePost.getUserId());
        Optional<ReportEntity> reportEntity = reportEntityRepository.findById(markResourcePost.getReportId());

        if (user.isPresent()) {
            checkBanned(user.get());
            checkReport(reportEntity, user.get().getId());
            checkMarked(reportEntity, user.get().getId());
            setMark(reportEntity.get(), user.get().getId(), markResourcePost);
        } else {
            userNotExist(markResourcePost.getUserId());
        }
        return reportEntity;

    }

    private void checkBanned(User user) throws AuthenticationException {
        if (user.getBanEntity().isBanned()) {
            throw new AuthenticationException("You cann not add report to" + user.getBanEntity().getDate(), ErrorSpecifcation.OPERATIONNOTALLOWED.getCode());
        }
    }


    private void checkReportFalseActivity(ReportEntity reportEntity) {
        if(reportEntity.getReportRating().getFalseReportQuantity()>10){

         checkReportActivity(reportEntity);
         User user=reportEntity.getUser();

            user.getBanEntity().setBanCounter(user.getBanEntity().getBanCounter()+1);
            if(user.getBanEntity().getBanCounter()<3){
                DateTime dt = new DateTime()
                        .withHourOfDay(12)
                        .withMinuteOfHour(00)
                        .withSecondOfMinute(0);
                dt= dt.plusDays(1);
                user.getBanEntity().setDate(dt.toDate());
            }else {
                DateTime dt = new DateTime()
                        .withHourOfDay(12)
                        .withMinuteOfHour(00)
                        .withSecondOfMinute(0);
                dt=dt.plusDays(365);
                user.getBanEntity().setDate(dt.toDate());
            }
            user.getBanEntity().setBanned(true);
            userRepository.save(user);
        }
    }

    private void setFalseReport(ReportEntity reportEntity, User user) {
        UserVoted userVoted = reportEntity.getReportRating().getUsersVoted().stream().filter(x -> x.getUserId().equals(user.getId())).findAny().orElse(null);
        if (userVoted == null) {
            userVoted = new UserVoted();
            userVoted.setUserId(user.getId());
            userVoted.setFalse(true);
            userVotedRepository.save(userVoted);
            reportEntity.getReportRating().getUsersVoted().add(userVoted);
            reportEntity.getReportRating().setFalseReportQuantity(reportEntity.getReportRating().getFalseReportQuantity()+1);
            reportEntityRepository.save(reportEntity);
        } else {
            userVoted.setFalse(true);
            userVotedRepository.save(userVoted);
            reportEntity.getReportRating().setFalseReportQuantity(reportEntity.getReportRating().getFalseReportQuantity()+1);
            reportEntityRepository.save(reportEntity);
        }
    }


    @Transactional
    protected void setNotActive(ReportEntity reportEntity, User user) {
        UserVoted userVoted = reportEntity.getReportRating().getUsersVoted().stream().filter(x -> x.getUserId().equals(user.getId())).findAny().orElse(null);
        if (userVoted == null) {
            userVoted = new UserVoted();
            userVoted.setUserId(user.getId());
            userVoted.setNotActual(true);
            userVotedRepository.save(userVoted);
            reportEntity.getReportRating().getUsersVoted().add(userVoted);
            reportEntity.getReportRating().setNotActiveCounter(reportEntity.getReportRating().getNotActiveCounter()+1);
            reportEntityRepository.save(reportEntity);
        } else {
            userVoted.setNotActual(true);
            userVotedRepository.save(userVoted);
            reportEntity.getReportRating().setNotActiveCounter(reportEntity.getReportRating().getNotActiveCounter()+1);
            reportEntityRepository.save(reportEntity);
        }
    }

    private void checkReportActivity(ReportEntity reportEntity) {

            User user=reportEntity.getUser();
            ReportRatingEntity reportRatingEntity=reportEntity.getReportRating();
            user.getUserRatingEntity().setMarks(user.getUserRatingEntity().getMarks()+reportRatingEntity.getMarks());
            user.getUserRatingEntity().setQuantity(user.getUserRatingEntity().getQuantity()+reportRatingEntity.getQuantity());
            userRepository.save(user);

    }

    private void checkisMarkedAsFalse(ReportEntity reportEntity, Long userId) throws DbException {
        UserVoted userVoted = reportEntity.getReportRating()
                .getUsersVoted()
                .stream()
                .filter(
                        x -> x.getUserId().equals(userId) && x.isFalse()
                ).findAny().orElse(null);
        if (userVoted != null) {
            throw new DbException(ErrorSpecifcation.OPERATIONNOTALLOWED.getDetails(), ErrorSpecifcation.OPERATIONNOTALLOWED.getCode(), new Field(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void checkIsMarkedAsNotActive(ReportEntity reportEntity, Long userId) throws ExceptionModel {

        UserVoted userVoted = reportEntity.getReportRating()
                .getUsersVoted()
                .stream()
                .filter(
                        x -> x.getUserId().equals(userId) && x.isNotActual()
                ).findAny().orElse(null);
        if (userVoted != null) {
            throw new DbException(ErrorSpecifcation.OPERATIONNOTALLOWED.getDetails(), ErrorSpecifcation.OPERATIONNOTALLOWED.getCode(), new Field(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void userNotExist(Long userId) throws DbException {
        Field field = new Field();
        field.setDetails("User with id=" + userId + " does not exist");
        field.setField("userId");
        ErrorSpecifcation.NOTSUPPORTEDMETHOD.getDetails();
        throw new DbException(ErrorSpecifcation.RESURCENOTEXIST.getDetails(), ErrorSpecifcation.RESURCENOTEXIST.getCode(), field);
    }

    private void setMark(ReportEntity reportEntity, Long userId, MarkResourcePost markResourcePost) {
        if (!userVotedRepository.findByUserId(userId).isPresent()) {
            UserVoted userVoted = new UserVoted();

            userVoted.setUserId(userId);
            userVoted.setMarked(true);
            userVoted.setTypeOfVote(1);
            userVotedRepository.save(userVoted);
            reportEntity.getReportRating().setQuantity(reportEntity.getReportRating().getQuantity() + 1);
            reportEntity.getReportRating().setMarks(reportEntity.getReportRating().getMarks() + markResourcePost.getMark());
            reportEntity.getReportRating().getUsersVoted().add(userVoted);
            reportEntityRepository.save(reportEntity);

        } else {
            UserVoted userVoted = userVotedRepository.findByUserId(userId).get();
            userVoted.setMarked(true);
            userVotedRepository.save(userVoted);
            reportEntity.getReportRating().setQuantity(reportEntity.getReportRating().getQuantity() + 1);
            reportEntity.getReportRating().setMarks(reportEntity.getReportRating().getMarks() + markResourcePost.getMark());
            reportEntityRepository.save(reportEntity);
        }
    }

    private void checkReport(Optional<ReportEntity> reportEntity, Long id) throws DbException {
        if (!reportEntity.isPresent()) {
            Field field = new Field();
            field.setDetails("Report does not exsit");
            field.setField("reportId");
            throw new DbException(ErrorSpecifcation.RESURCENOTEXIST.getDetails(), ErrorSpecifcation.RESURCENOTEXIST.getCode(), field);
        }

        if (id!=null && reportEntity.get().getUser().getId().equals(id)) {
            Field field = new Field();
            field.setDetails("You cannot mark your report");
            throw new DbException(ErrorSpecifcation.OPERATIONNOTALLOWED.getDetails(), ErrorSpecifcation.OPERATIONNOTALLOWED.getCode(), field, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void checkMarked(Optional<ReportEntity> reportEntity, Long id) throws DbException {

        if(reportEntity.isPresent()) {
            List<UserVoted> votedList = reportEntity.get().getReportRating().getUsersVoted();
            UserVoted userVoted = votedList.stream().filter(x -> x.getUserId().equals(id) && x.isMarked()).findAny().orElse(null);

            if (userVoted != null) {
                Field field = new Field();
                field.setDetails("You cannot mark report again");
                throw new DbException(ErrorSpecifcation.OPERATIONNOTALLOWED.getDetails(), ErrorSpecifcation.OPERATIONNOTALLOWED.getCode(), field, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
    }

    private Long getUserId(HttpServletRequest request) {
        return userTokenRecvier.getUserId(request);
    }
}
