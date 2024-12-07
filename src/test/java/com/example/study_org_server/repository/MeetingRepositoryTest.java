package com.example.study_org_server.repository;

import com.example.study_org_server.exception.MeetingNotFoundException;
import com.example.study_org_server.repository.meeting.MeetingRecord;
import com.example.study_org_server.repository.meeting.MeetingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.openapitools.example.model.MeetingSearchForm;
import org.openapitools.example.model.OrderProp;
import org.openapitools.example.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@MybatisTest
public class MeetingRepositoryTest {

    static LocalDate time_1 = LocalDate.of(2022,11,7);
    static LocalDate time_1_day_end =LocalDate.of(2024,11,1);
    static LocalDate time_1_month_end =LocalDate.of(2024,11,30);
    static LocalDate time_2 =LocalDate.of(2020,12,24);
    static LocalDate time_3 =LocalDate.of(2020,12,25);

//LocalDate.of(2024,11,1)
    private static MeetingRecord meetingRecord_1 =
            new MeetingRecord(null,"TITLE_1","DETAIL_1",1L,time_1,"1700","1800");
    private static MeetingRecord meetingRecord_2 =
            new MeetingRecord(null,"TITLE_2","DETAIL_2",1L,time_2,"1700","1800" );
    private static MeetingRecord meetingRecord_3 =
            new MeetingRecord(null,"TITLE_3","DETAIL_3",1L,time_3,"1700","1800");
    private static MeetingRecord meetingRecord_error =
            new MeetingRecord(null,"TITLE_3","DETAIL_3",1L,LocalDate.of(2024,11,1),"1700","1800");

    @Autowired
    private MeetingRepository meetingRepository;

    private OrderProp orderProp;
    private Pagination pagination;

    @BeforeEach
    void setup(){
        pagination= new Pagination();
        orderProp = new OrderProp();

    }


    @Test
    public void findAllMeeting_ShouldReturnAll(){
        Assertions.assertEquals(5,meetingRepository.findAllMeetings(pagination,orderProp).size());
    }


    @Test
    public void findMeetingsByOpenerId_ShouldReturnAll(){
        Assertions.assertEquals(2,meetingRepository.findMeetingsByOpenerId(1).size());
    }

    @Test
    public void findMeetingById_ShouldReturn(){
        Assertions.assertTrue(meetingRepository.findMeetingById(1).isPresent());
        Assertions.assertTrue(meetingRepository.findMeetingById(100).isEmpty());
    }
    //create
    @Test
    public void create_ShouldSuccess(){
        orderProp.property("id");
        orderProp.direction("asc");
        var currentMeetings= meetingRepository.findAllMeetings(pagination,orderProp);
        int currentSize =currentMeetings.size();
        meetingRepository.create(meetingRecord_1);
        var afterInsertData= meetingRepository.findAllMeetings(pagination,orderProp);
        Assertions.assertEquals(currentSize +1,afterInsertData.size());
        var lastData = afterInsertData.get(afterInsertData.size()-1);
        Assertions.assertEquals(meetingRecord_1.title(),lastData.title());
    }


    //delete
    @Test
    public void delete_ShouldSuccess(){
        var currentMeetings= meetingRepository.findAllMeetings(pagination,orderProp);
        int currentSize =currentMeetings.size();
        if(currentSize >0){
            meetingRepository.deleteMeetingById(currentMeetings.get(currentSize-1).id());
        }else{
            Assertions.fail();
        }
        Assertions.assertEquals(currentSize-1,meetingRepository.findAllMeetings(pagination,orderProp).size());
    }

    //update
    @Test
    public void put_shouldSuccess(){
        MeetingRecord firstElem =meetingRepository.findAllMeetings(pagination,orderProp).stream().findFirst().orElseThrow(()-> new MeetingNotFoundException(""));
        int targetId=firstElem.id();
        meetingRepository.update(targetId,meetingRecord_2);
        MeetingRecord changedRecord = meetingRepository.findMeetingById(targetId).orElseThrow(()->new MeetingNotFoundException(""));
        Assertions.assertEquals(meetingRecord_2.title(),changedRecord.title());
        Assertions.assertEquals(meetingRecord_2.detail(),changedRecord.detail());

    }
    //update
    @Test
    public void put_partUpdate_shouldSuccess(){
        MeetingRecord firstElem =meetingRepository.findAllMeetings(pagination,orderProp).stream().findFirst().orElseThrow(()-> new MeetingNotFoundException(""));
        int targetId=firstElem.id();
        MeetingRecord record = new MeetingRecord(targetId,"title_Changed",null,2L,time_3,"1700","1800",null,null);
        meetingRepository.update(targetId,record);
        MeetingRecord changedRecord = meetingRepository.findMeetingById(targetId).orElseThrow(()->new MeetingNotFoundException(""));
        Assertions.assertEquals(record.title(),changedRecord.title());
        Assertions.assertEquals(firstElem.detail(),changedRecord.detail());

    }

    //search
    @Test
    public void search_searchWord_LowerAndUpper_shouldSuccess(){
        String searchWord = "project";
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        meetingSearchForm.setSearchWord(searchWord);
        Assertions.assertEquals(2,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    public void search_searchWord_notMatch_shouldSuccess(){
        String searchWord = "XXX";
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        meetingSearchForm.setSearchWord(searchWord);
        Assertions.assertEquals(0,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    //日付
    @Test
    public void search_searchDate_sameDay_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        meetingSearchForm.setSearchStartDate(time_1_day_end);
        meetingSearchForm.setSearchEndDate(time_1_day_end);
        Assertions.assertEquals(1,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
//    @Test
//    public void search_searchDate_sameMonth_shouldSuccess(){
//        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
//        meetingSearchForm.setSearchStartDate(time_1);
//        meetingSearchForm.setSearchEndDate(time_1_month_end);
//        Assertions.assertEquals(2,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
//    }
    @Test
    public void search_searchDate_sameYear_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        LocalDate time_year_start =LocalDate.of(2024,1,1);
        LocalDate time_year_end =LocalDate.of(2024,12,31);
        meetingSearchForm.setSearchStartDate(time_year_start);
        meetingSearchForm.setSearchEndDate(time_year_end);
        Assertions.assertEquals(3,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    public void search_beyondYear_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        LocalDate start =LocalDate.of(2023,10,10);
        LocalDate end =LocalDate.of(2024,11,1);
        meetingSearchForm.setSearchStartDate(start);
        meetingSearchForm.setSearchEndDate(end);
        Assertions.assertEquals(2,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    public void search_before_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        LocalDate end =LocalDate.of(2024,11,1);
        meetingSearchForm.setSearchEndDate(end);
        Assertions.assertEquals(2,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    public void search_after_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        LocalDate start =LocalDate.of(2024,12,9);
        meetingSearchForm.setSearchStartDate(start);
        Assertions.assertEquals(2,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    public void search_OneSearchOpenerId_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        List<Long> openrIdList = List.of(1L);
        meetingSearchForm.setSearchOpenerId(openrIdList);
        Assertions.assertEquals(2,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    public void search_TwoSearchOpenerId_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        List<Long> openrIdList = List.of(1L,2L);
        meetingSearchForm.setSearchOpenerId(openrIdList);
        Assertions.assertEquals(4,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }

    //複合検索
    @Test
    //2024 11月　キーワード検索
    public void search_multi_sameMonth_word_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        meetingSearchForm.setSearchStartDate(time_1);
        meetingSearchForm.setSearchEndDate(time_1_month_end);
        meetingSearchForm.setSearchWord("team");
        Assertions.assertEquals(1,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    //2024 11月　キーワード検索
    public void search_multi_sameMonth_opener_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        meetingSearchForm.setSearchStartDate(time_1);
        meetingSearchForm.setSearchEndDate(time_1_month_end);
        meetingSearchForm.setSearchOpenerId(List.of(2L));
        Assertions.assertEquals(1,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    // opener:1　キーワード検索
    public void search_multi_word_opener_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        meetingSearchForm.setSearchWord("project");
        meetingSearchForm.setSearchOpenerId(List.of(1L));
        Assertions.assertEquals(1,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }
    @Test
    //2024 opener:1　キーワード検索
    public void search_full_word_opener_shouldSuccess(){
        MeetingSearchForm meetingSearchForm = new MeetingSearchForm();
        meetingSearchForm.setSearchWord("project");
        meetingSearchForm.setSearchStartDate(time_1);
        meetingSearchForm.setSearchEndDate(time_1_month_end);
        meetingSearchForm.setSearchOpenerId(List.of(1L));
        Assertions.assertEquals(1,meetingRepository.search(meetingSearchForm,pagination,orderProp).size());
    }

    @Test
    //重複しないようにする
    public void insert_sameDate_shouldFail(){
        orderProp.property("id");
        orderProp.direction("asc");
        var currentMeetings= meetingRepository.findAllMeetings(pagination,orderProp);
        int currentSize =currentMeetings.size();
        Assertions.assertThrows(RuntimeException.class,() -> meetingRepository.create(meetingRecord_error));

        var afterInsertData= meetingRepository.findAllMeetings(pagination,orderProp);
        Assertions.assertEquals(currentSize,afterInsertData.size());

    }
}
