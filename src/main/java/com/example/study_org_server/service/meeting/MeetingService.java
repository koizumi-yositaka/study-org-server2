package com.example.study_org_server.service.meeting;

import com.example.study_org_server.exception.MeetingNotFoundException;
import com.example.study_org_server.repository.meeting.MeetingRecord;
import com.example.study_org_server.repository.meeting.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.openapitools.example.model.MeetingForm;
import org.openapitools.example.model.MeetingResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public List<MeetingResponseDTO> findAllMeeting(){
//        MeetingResponseDTOList meetings=
//        return meetings.map(x -> new MeetingResponseDTO(x.id(),x.title())).toList();
        return meetingRepository.findAllMeetings().stream().map(x -> new MeetingResponseDTO(x.id(), x.title())).toList();
    }

    public List<MeetingResponseDTO> findMeetingsByOpenerId(int openerId){
        return meetingRepository.findMeetingsByOpenerId(openerId).stream().map(x -> new MeetingResponseDTO(x.id(), x.title())).toList();
    }

    public MeetingResponseDTO findMeetingById(int id){
        return meetingRepository.findMeetingById(id).map(record -> new MeetingResponseDTO(record.id(), record.title())).orElseThrow(()-> new MeetingNotFoundException("AA"));
    }


    //create
    @Transactional
    public void reserveMeeting(MeetingForm meetingForm){
        //予約の重複チェックが必要
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



}
