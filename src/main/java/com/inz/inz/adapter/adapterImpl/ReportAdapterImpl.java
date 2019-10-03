package com.inz.inz.adapter.adapterImpl;

import com.inz.inz.ExcpetionHandler.DbException;
import com.inz.inz.ExcpetionHandler.EnumExcpetion;
import com.inz.inz.adapter.ReportAdapter;
import com.inz.inz.entity.CityEntity;
import com.inz.inz.entity.ReportEntity;
import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.mapper.ReportMapper;
import com.inz.inz.repository.CityEntityRepository;
import com.inz.inz.repository.ReportEntityRepository;
import com.inz.inz.repository.ReportRatingEntityRepository;
import com.inz.inz.repository.UserRepository;
import com.inz.inz.resoruce.ReportResourcePost;
import com.inz.inz.seciurity.model.User;
import com.inz.inz.seciurity.service.UserTokenReciver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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

    @Override
    public ReportEntity createReport(HttpServletRequest request, ReportResourcePost reportResourcePost) throws DbException, EnumExcpetion {
        Long id = getUserId(request);
        ReportEntity reportEntity = reportMapper.mapToEntity(reportResourcePost);
        reportEntity.setUser(userRepository.getOne(id));
        reportEntity.setReportRating(reportRatingEntityRepository.save(new ReportRatingEntity()));

        try {
            reportEntity=save(reportEntity,id);
        }
        catch (DataIntegrityViolationException ex){
            DbException dbException = new DbException();
            dbException.setCaused("Report save error");
            dbException.setCode("D1");
        }
        return reportEntity;
    }

    private ReportEntity save(ReportEntity reportEntity,long id ) {
        CityEntity cityEntity=cityEntityRepository.findByName(reportEntity.getCity().getName()).get();
        reportEntityRepository.save(reportEntity);
        cityEntity.getReportList().add(reportEntity);
        cityEntityRepository.save(cityEntity);
        User u=userRepository.getOne(id);
        u.getReportsList().add(reportEntity);
        userRepository.save(u);
        return reportEntity;
    }

    private Long getUserId(HttpServletRequest request) {
        return userTokenRecvier.getUserId(request);
    }
}
