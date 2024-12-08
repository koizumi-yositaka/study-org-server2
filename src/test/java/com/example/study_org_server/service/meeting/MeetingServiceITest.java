package com.example.study_org_server.service.meeting;

import com.example.study_org_server.Config.TestMyCacheManagerConfiguration;
import com.example.study_org_server.exception.MeetingNotFoundException;
import com.example.study_org_server.repository.meeting.MeetingRecord;
import com.example.study_org_server.repository.meeting.MeetingRepository;
import com.example.study_org_server.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.example.model.MeetingForm;
import org.openapitools.example.model.MeetingResponseDTO;
import org.openapitools.example.model.OrderProp;
import org.openapitools.example.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableCaching
@SpringBootTest(classes = {MeetingService.class, TestMyCacheManagerConfiguration.class})
class MeetingServiceITest {
    static LocalDate time_1 = LocalDate.of(2020,12,23);
    static LocalDate time_2 =LocalDate.of(2020,12,24);
    static LocalDate time_3 =LocalDate.of(2020,12,25);


    private static final MeetingRecord meetingRecord_1 = new MeetingRecord(null,"TITLE_1","DETAIL_1",1L,time_1,"1700","1700" ,"0");
    private static final MeetingRecord meetingRecord_2 = new MeetingRecord(null,"TITLE_2","DETAIL_2",1L,time_2,"1700","1800" ,"0");
    private static final MeetingRecord meetingRecord_3 = new MeetingRecord(null,"TITLE_3","DETAIL_3",1L,time_3,"1700","1800","0" );

    private static final MeetingResponseDTO meetingsDTO_1 = new MeetingResponseDTO(1,"TITLE_1","DETAIL_1",1L,time_1,"1700","1700");
    private static final MeetingResponseDTO meetingsDTO_2 = new MeetingResponseDTO(1,"TITLE_2","DETAIL_2",1L,time_2,"1700","1700");
    private static final MeetingResponseDTO meetingsDTO_3 = new MeetingResponseDTO(1,"TITLE_3","DETAIL_3",1L,time_3,"1700","1700");


    @Autowired
    private MeetingService meetingService;

    @MockBean
    private MeetingRepository meetingRepository;
    private OrderProp orderProp;
    private Pagination pagination;

    @BeforeEach
    void setup(){
        pagination= new Pagination();
        orderProp = new OrderProp();

    }
//    @Test
//    public void testConcurrentMeetingCreation() throws InterruptedException {
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        CountDownLatch latch = new CountDownLatch(2);
//
//        Runnable task = () -> {
//            try {
//                MeetingForm form = new MeetingForm();
//                form.setTitle("AAA");
//                form.setDetail("DETAIL");
//                form.setOpenerId(1L);
//                form.setEventDate(time_1);
//                meetingService.reserveMeeting(form);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//
//            } finally {
//                var meetings = meetingService.findAllMeeting(null,null);
//                latch.countDown();
//            }
//        };
//
//        executor.submit(task);
//        executor.submit(task);
//
//        latch.await();
//        executor.shutdown();
//        var meetings = meetingService.findAllMeeting(null,null);
//        assertEquals(1, meetingRepository.findMeetingByEventDate(time_1) .size()); // 重複登録されていないことを確認
//    }



}