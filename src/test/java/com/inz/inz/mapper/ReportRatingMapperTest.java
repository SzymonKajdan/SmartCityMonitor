package com.inz.inz.mapper;

import com.inz.inz.entity.ReportRatingEntity;
import com.inz.inz.resoruce.reportresource.ReportRatingResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ReportRatingMapperTest {

    @Autowired
    ReportRatingMapper reportRatingMapper;

    @Test
    public void mapToReportRatingResourceTest(){
        ReportRatingEntity entity=new ReportRatingEntity();
        assertNull(reportRatingMapper.mapToReportRatingResource(null));

       ReportRatingResource resource=reportRatingMapper.mapToReportRatingResource(entity);

       assertTrue(resource.getIsActive());
       assertEquals(0.0,resource.getMark());

       entity.setNotActiveCounter(1);
       entity.setFalseReportQuantity(10);
       entity.setMarks(10);
       entity.setQuantity(2);
       resource=reportRatingMapper.mapToReportRatingResource(entity);
       assertEquals(5.0,resource.getMark());
       assertFalse(resource.getIsActive());



    }

}
