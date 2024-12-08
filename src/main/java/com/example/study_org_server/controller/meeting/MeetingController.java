package com.example.study_org_server.controller.meeting;

import com.example.study_org_server.controller.MeetingApi;
import com.example.study_org_server.exception.ReservationConflict;
import com.example.study_org_server.service.meeting.MeetingService;
import lombok.RequiredArgsConstructor;
import org.openapitools.example.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MeetingController implements MeetingApi {
    private final MeetingService meetingService;
//    @Override
//    public ResponseEntity<MeetingResponseDTO> meetingGet() {

    @Override
    public ResponseEntity<MeetingResponseDTO> meetingMeetingIdGet(Integer meetingId) {
        var target = meetingService.findMeetingById(meetingId);
        return ResponseEntity.ok().body(target);
    }

    @Override
    public ResponseEntity<MeetingResponseDTO> meetingMeetingIdPut(Integer meetingId, MeetingForm meetingForm) {
        var target = meetingService.findMeetingById(meetingId);
        meetingService.updateMeeting(meetingId,meetingForm);
        var updatedTarget = meetingService.findMeetingById(meetingId);
        return ResponseEntity.ok(updatedTarget);
    }

    @Override
    public ResponseEntity<Void> meetingMeetingIdDelete(Integer meetingId) {
        var target = meetingService.findMeetingById(meetingId);
        meetingService.deleteMeetingById(meetingId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MeetingResponseDTOList> meetingGet(MeetingSearchForm searchForm, Pagination pagination,OrderProp orderProp) {
        List<MeetingResponseDTO> meetings = new ArrayList<>();
        if(searchForm !=null){
            meetings=meetingService.searchMeeting(searchForm,pagination,orderProp);
        }else{
            meetings = meetingService.findAllMeeting(pagination,orderProp);
        }
        return ResponseEntity.ok().body(new MeetingResponseDTOList(meetings));
    }

    @Override
    public ResponseEntity<MeetingResponseDTO> meetingPost(MeetingForm meetingForm) {

        meetingService.reserveMeeting(meetingForm);
        return ResponseEntity.created(URI.create("meeting/"+meetingForm.getTitle())).build();
    }

    //offsetなどの件数調整
    //order
}