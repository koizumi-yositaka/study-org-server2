package com.example.study_org_server.service.meeting;

import com.example.study_org_server.exception.MeetingNotFoundException;
import com.example.study_org_server.exception.ReservationConflict;
import com.example.study_org_server.repository.meeting.MeetingRecord;
import com.example.study_org_server.repository.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.example.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public List<MeetingResponseDTO> findAllMeeting( Pagination pagination,OrderProp orderProp){
//        MeetingResponseDTOList meetings=
//        return meetings.map(x -> new MeetingResponseDTO(x.id(),x.title())).toList();
        return meetingRepository.findAllMeetings(pagination,orderProp).stream().map(MeetingRecord::repack).toList();
    }

    public List<MeetingResponseDTO> findMeetingsByOpenerId(int openerId){
        return meetingRepository.findMeetingsByOpenerId(openerId).stream().map(MeetingRecord::repack).toList();
    }

    public MeetingResponseDTO findMeetingById(int id){
        return meetingRepository.findMeetingById(id).map(MeetingRecord::repack).orElseThrow(()-> new MeetingNotFoundException("AA"));
    }


    //create
    @Transactional
    public void reserveMeeting(MeetingForm meetingForm){
        var sameDateList= meetingRepository.findMeetingByEventDateForUpdate(meetingForm.getEventDate());
        //予約の重複チェック
        if(!sameDateList.isEmpty()){
            throw new ReservationConflict(meetingForm.getEventDate());
        }
        meetingRepository.create(new MeetingRecord(meetingForm));
    }

    //delete
    @Transactional
    public void deleteMeetingById(int id){
        meetingRepository.deleteMeetingById(id);
    }

    //update
    @Transactional
    public void updateMeeting(int id, MeetingForm meetingForm){
        var updateData=new MeetingRecord(meetingForm);
        meetingRepository.update(id,updateData);
    }

    //search
    public List<MeetingResponseDTO> searchMeeting(MeetingSearchForm meetingSearchForm, Pagination pagination, OrderProp orderProp){
        //チェック
        //日付が前後
        return meetingRepository.search(meetingSearchForm,pagination,orderProp).stream().map(MeetingRecord::repack).toList();
    }



}