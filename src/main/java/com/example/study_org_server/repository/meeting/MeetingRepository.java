package com.example.study_org_server.repository.meeting;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MeetingRepository {
    @Select("SELECT * FROM T_MEETING WHERE delete_flg != '1'")
    List<MeetingRecord> findAllMeetings();

    @Select("SELECT * FROM T_MEETING WHERE id=#{id} and delete_flg != '1'")
    Optional<MeetingRecord> findMeetingById(int id);

    @Select("SELECT * FROM T_MEETING WHERE opener_id=#{openerId} and delete_flg != '1'")
    List<MeetingRecord> findMeetingsByOpenerId(int openerId);

    @Insert("""
            INSERT INTO T_MEETING (title, detail, opener_id,event_date, delete_flg)
            VALUES(#{meeting.title},#{meeting.detail},#{meeting.openerId},#{meeting.eventDate},'0')
            """
            )
    void create(@Param("meeting") MeetingRecord record);

    @Update("""
            <script>
                UPDATE T_MEETING
                <set>
                    <if test="meeting.title != null">
                        title=#{meeting.title},
                    </if>
                    <if test="meeting.detail != null">
                        detail=#{meeting.detail},
                    </if>
                    <if test="meeting.openerId != null">
                        opener_id=#{meeting.openerId},
                    </if>
                    <if test="meeting.eventDate != null">
                        event_date=#{meeting.eventDate},
                    </if>
                    <if test="meeting.duration != null">
                        duration=#{meeting.duration},
                    </if>
                </set>
                WHERE id=#{id}
            </script>
            """)
    void update(int id, @Param("meeting") MeetingRecord record);

    @Update("UPDATE T_MEETING SET delete_flg='1' WHERE id=#{id}")
    void deleteMeetingById(int id);


}
