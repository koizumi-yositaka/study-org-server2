package com.example.study_org_server.repository.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.openapitools.example.model.MeetingForm;
import org.openapitools.example.model.MeetingResponseDTO;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;



public record MeetingRecord(
        Integer id,
        String title,
        String detail,
        Long openerId,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate eventDate,
        String startTime,
        String endTime,
        String deleteFlg,
        Timestamp createdAt,
        Timestamp updatedAt
) {
    public MeetingRecord(Integer id, String title, String detail, Long openerId, LocalDate eventDate,String deleteFlg,String startTime,String endTime) {
        this(id, title, detail, openerId, eventDate,startTime,endTime,deleteFlg,null, null);
    }

    public MeetingRecord(MeetingForm form){
        this(
                null,
                form.getTitle(),
                form.getDetail(),
                form.getOpenerId(),
                form.getEventDate(),
                form.getStartTime(),
                form.getEndTime(),
                "0"
        );
    }

    public MeetingResponseDTO repack(){
        MeetingResponseDTO meeting = new MeetingResponseDTO();
        meeting.setTitle(this.title());
        meeting.setId(this.id());
        meeting.setDetail(this.detail());
        meeting.setOpenerId(this.openerId());
        meeting.setEventDate(this.eventDate());
        meeting.setStartTime(this.startTime());
        meeting.setEndTime(this.endTime());
        return meeting;
    }
}