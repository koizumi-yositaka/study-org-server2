package com.example.study_org_server.repository.meeting;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.openapitools.example.model.MeetingForm;

import java.sql.Timestamp;
import java.time.LocalDateTime;



public record MeetingRecord(
        Integer id,
        String title,
        String detail,
        Long openerId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime eventDate,
        Integer duration,
        String deleteFlg,
        Timestamp createdAt,
        Timestamp updatedAt
) {
    public MeetingRecord(Integer id, String title, String detail, Long openerId, LocalDateTime eventDate,Integer durationMinutes,String deleteFlg) {
        this(id, title, detail, openerId, eventDate,durationMinutes,deleteFlg,null, null);
    }

    public MeetingRecord(MeetingForm form){
        this(
                null,
                form.getTitle(),
                form.getDetail(),
                form.getOpenerId(),
                form.getEventDate(),
                form.getDurationMinutes(),
                "0"
        );
    }

}
