package com.example.study_org_server.repository.meeting;

import org.apache.ibatis.annotations.*;
import org.openapitools.example.model.MeetingSearchForm;
import org.openapitools.example.model.OrderProp;
import org.openapitools.example.model.Pagination;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Mapper
public interface MeetingRepository {
    @Select("""
            <script>
            SELECT * FROM T_MEETING
            <if test="orderProp.property!=null and !orderProp.property.isBlank()">
                ORDER BY ${orderProp.property}
                <choose>
                <when test="orderProp.direction == 'desc'">
                    desc
                </when>
                <otherwise>
                    asc
                </otherwise>
                </choose>
            </if>
            </script>
            """)
    List<MeetingRecord> findAllMeetings(@Param("pagination")Pagination pagination,
                                        @Param("orderProp") OrderProp orderProp);

    @Select("""
            <script>
                SELECT *
                FROM T_MEETING
                <where>
                    <if test='condition.searchWord != null and !condition.searchWord.isBlank()'>
                        (
                            LOWER(title) LIKE LOWER(CONCAT('%',#{condition.searchWord},'%'))
                            OR
                            LOWER(detail) LIKE LOWER(CONCAT('%',#{condition.searchWord},'%'))
                        )
                    </if>
                    <if test='condition.searchOpenerId != null and condition.searchOpenerId.size() > 0'>
                        AND opener_id IN
                        <foreach item = 'item' index='index' collection='condition.searchOpenerId' open='(' separator=',' close=')'>
                            #{item}
                        </foreach>
                    </if>
                    <choose>
                        <when test="condition.searchStartDate != null and condition.searchEndDate != null">
                         AND event_date between #{condition.searchStartDate} AND #{condition.searchEndDate}
                        </when>
                        <when test="condition.searchEndDate != null">
                          AND event_date &lt;= #{condition.searchEndDate}
                        </when>
                        <when test="condition.searchStartDate != null">
                          AND event_date &gt;= #{condition.searchStartDate}
                        </when>
                    </choose>
                    </where>
                    ORDER BY
                    <choose>
                        <when test="orderProp.property == 'title'">
                        #{orderProp.property}
                        </when>
                        <otherwise>
                        'event_date'
                        </otherwise>
                    </choose>
                    <choose>
                        <when test="orderProp.direction == 'desc'">
                        #{orderProp.direction}
                        </when>
                        <otherwise>
                        'asc'
                        </otherwise>
                    </choose>
            </script>
            """
    )
    List<MeetingRecord> search(@Param("condition")MeetingSearchForm condition,
                               @Param("pagination")Pagination pagination,
                               @Param("orderProp") OrderProp orderProp);


    @Select("""
            SELECT *
            FROM T_MEETING
            where event_date = #{target}
            """)
    List<MeetingRecord> findMeetingByEventDate(LocalDate target);

    @Select("SELECT * FROM T_MEETING WHERE id=#{id}")
    Optional<MeetingRecord> findMeetingById(int id);

    @Select("SELECT * FROM T_MEETING WHERE opener_id=#{openerId}")
    List<MeetingRecord> findMeetingsByOpenerId(int openerId);

    @Insert("""
            INSERT INTO T_MEETING (title, detail, opener_id,event_date,start_time,end_time)
            VALUES(#{meeting.title},#{meeting.detail},#{meeting.openerId},#{meeting.eventDate},#{meeting.startTime},#{meeting.endTime})
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
                    <if test="meeting.startTime != null">
                        start_time=#{meeting.startTime},
                    </if>
                    <if test="meeting.endTime != null">
                        end_time=#{meeting.endTime},
                    </if>
                </set>
                WHERE id=#{id}
            </script>
            """)
    void update(int id, @Param("meeting") MeetingRecord record);

    @Update("DELETE FROM T_MEETING WHERE id=#{id}")
    void deleteMeetingById(int id);


}