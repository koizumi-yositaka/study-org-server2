package com.example.study_org_server.service.meeting;

import com.example.study_org_server.exception.MeetingNotFoundException;
import com.example.study_org_server.exception.ReservationConflict;
import com.example.study_org_server.repository.meeting.MeetingRecord;
import com.example.study_org_server.repository.meeting.MeetingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class MeetingServiceTest {
    static LocalDate time_1 = LocalDate.of(2020,12,23);
    static LocalDate time_2 =LocalDate.of(2020,12,24);
    static LocalDate time_3 =LocalDate.of(2020,12,25);


    private static final MeetingRecord meetingRecord_1 = new MeetingRecord(null,"TITLE_1","DETAIL_1",1L,time_1,"1700","1700" ,"0");
    private static final MeetingRecord meetingRecord_2 = new MeetingRecord(null,"TITLE_2","DETAIL_2",1L,time_2,"1700","1800" ,"0");
    private static final MeetingRecord meetingRecord_3 = new MeetingRecord(null,"TITLE_3","DETAIL_3",1L,time_3,"1700","1800","0" );

    private static final MeetingResponseDTO meetingsDTO_1 = new MeetingResponseDTO(1,"TITLE_1","DETAIL_1",1L,time_1,"1700","1700");
    private static final MeetingResponseDTO meetingsDTO_2 = new MeetingResponseDTO(1,"TITLE_2","DETAIL_2",1L,time_2,"1700","1700");
    private static final MeetingResponseDTO meetingsDTO_3 = new MeetingResponseDTO(1,"TITLE_3","DETAIL_3",1L,time_3,"1700","1700");


    @InjectMocks
    private MeetingService meetingService;

    @Mock
    private MeetingRepository meetingRepository;
    private OrderProp orderProp;
    private Pagination pagination;

    @BeforeEach
    void setup(){
        pagination= new Pagination();
        orderProp = new OrderProp();

    }


    @Test
    void findAllMeeting() {
        when(meetingRepository.findAllMeetings(any(Pagination.class),any(OrderProp.class))).thenReturn(List.of
                (
                        meetingRecord_1,
                        meetingRecord_2,
                        meetingRecord_3
                ));
        Assertions.assertEquals(3,meetingService.findAllMeeting(pagination,orderProp).size());
        verify(meetingRepository,times(1)).findAllMeetings(any(Pagination.class),any(OrderProp.class));
    }

    @Test
    void findMeetingsByOpenerId() {
        when(meetingRepository.findMeetingsByOpenerId(anyInt())).thenReturn(List.of
                (
                        meetingRecord_1
                )
        );
        Assertions.assertEquals(1,meetingService.findMeetingsByOpenerId(1).size());
        verify(meetingRepository,times(1)).findMeetingsByOpenerId(1);
    }

    @Test
    void findMeetingById_Success() {
        when(meetingRepository.findMeetingById(anyInt())).thenReturn(Optional.of
                (
                        meetingRecord_1
                )
        );
        MeetingResponseDTO actual =meetingService.findMeetingById(1);
        Assertions.assertEquals(meetingsDTO_1.getTitle(),actual.getTitle());
    }
    @Test
    void findMeetingById_Fail() {
        when(meetingRepository.findMeetingById(anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(MeetingNotFoundException.class,()->meetingService.findMeetingById(1));
    }

    //delete
    @Test
    void deleteMeetingByID_ShouldSuccess(){
        doNothing().when(meetingRepository).deleteMeetingById(anyInt());
        meetingService.deleteMeetingById(1);
        verify(meetingRepository,times(1)).deleteMeetingById(anyInt());
    }



    //reserveMeeting
    @Test
    void createMeeting_ShouldSuccess(){
        doNothing().when(meetingRepository).create(any(MeetingRecord.class));
        MeetingForm form = new MeetingForm();
        form.setTitle("AAA");
        form.setDetail("DETAIL");
        form.setOpenerId(1L);
        meetingService.reserveMeeting(form);
        verify(meetingRepository,times(1)).create(any(MeetingRecord.class));
    }

    @Test
    void updateMeeting_ShouldSuccess() {
        doNothing().when(meetingRepository).update(anyInt(),any(MeetingRecord.class));
        MeetingForm form = new MeetingForm();
        form.setTitle("AAA");
        form.setDetail("DETAIL");
        form.setOpenerId(1L);
        meetingService.updateMeeting(1,form);
        verify(meetingRepository,times(1)).update(anyInt(),any(MeetingRecord.class));

    }


    @Test
    //重複しないようにする
    public void insert_sameDate_shouldFail(){

        doReturn(List.of(meetingRecord_1)).when(meetingRepository).findMeetingByEventDateForUpdate(any(LocalDate.class));
        MeetingForm form = new MeetingForm();
        form.setTitle("AAA");
        form.setDetail("DETAIL");
        form.setOpenerId(1L);
        form.setEventDate(time_1);
        Assertions.assertThrows(ReservationConflict.class,() -> meetingService.reserveMeeting(form));


    }
}