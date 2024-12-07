package com.example.study_org_server.controller.meeting;

import com.example.study_org_server.StudyOrgServerApplication;
import com.example.study_org_server.security.PasswordEncoderConfig;
import com.example.study_org_server.security.SecurityConfig;
import com.example.study_org_server.service.meeting.MeetingService;
import com.example.study_org_server.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openapitools.example.model.MeetingForm;
import org.openapitools.example.model.MeetingResponseDTO;
import org.openapitools.example.model.MeetingResponseDTOList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.example.study_org_server.util.TestUtil.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class MeetingControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @Test
    @Order(1)
    public void givenWac_whenServletContext_thenItProvidesGreetController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        //assertNotNull(webApplicationContext.getBean("UserController"));
    }
    @Test
    @Order(2)
    public void reserve() throws Exception{
        //追加前の一覧を取得
        MeetingResponseDTOList meetingList_1=objectMapper.readValue(this.mockMvc.perform(get("/meeting")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), MeetingResponseDTOList.class);

        LocalDate datatime_1= LocalDate.of(2020,12,23);
        var createMeeting = new MeetingForm();
        createMeeting.setTitle("AAA");
        createMeeting.setDetail("BBB");
        createMeeting.setEventDate(datatime_1);

        createMeeting.setOpenerId(1L);
        //更新
        this.mockMvc.perform(post("/meeting")
                        .contentType(MediaType.APPLICATION_JSON)

                        .content("""
                        {
                            "title": "AAABBB",
                            "detail": "abcdefghijh",
                            "openerId": 1,
                            "eventDate": "2024-12-05",
                            "startTime":"1700",
                            "endTime":"1800"
                        }
                        """)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();

        //再度一覧を取得する
        MeetingResponseDTOList meetingList_2=objectMapper.readValue(this.mockMvc.perform(get("/meeting")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), MeetingResponseDTOList.class);
        //１増えていることの検証
        Assertions.assertEquals(1,meetingList_2.getResults().size()-meetingList_1.getResults().size());

    }
    @Test
    @Order(3)
    public void cancel() throws Exception{
        //追加前の一覧を取得
        MeetingResponseDTOList meetingList_1=objectMapper.readValue(this.mockMvc.perform(get("/meeting")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), MeetingResponseDTOList.class);
        //最新のデータ
        var lastestData =meetingList_1.getResults().get(meetingList_1.getResults().size()-1);



        //削除
        this.mockMvc.perform(delete("/meeting/"+lastestData.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


        //再度一覧を取得する
        MeetingResponseDTOList meetingList_2=objectMapper.readValue(this.mockMvc.perform(get("/meeting")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), MeetingResponseDTOList.class);
        //１増えていることの検証
        Assertions.assertEquals(-1,meetingList_2.getResults().size()-meetingList_1.getResults().size());

    }

    @Test
    @Order(4)
    public void update() throws Exception{
        //追加前の一覧を取得
        MeetingResponseDTOList meetingList_1=objectMapper.readValue(this.mockMvc.perform(get("/meeting")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), MeetingResponseDTOList.class);
        //最新のデータ
        var latestData =meetingList_1.getResults().get(meetingList_1.getResults().size()-1);

        var updateMeeting = new MeetingForm();
        updateMeeting.setTitle("AAA");
        updateMeeting.setDetail("BBB");
        updateMeeting.setOpenerId(1L);

        //更新
        MeetingResponseDTO meetingResponseDTO=objectMapper.readValue(this.mockMvc.perform(put("/meeting/"+latestData.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "AAABBB",
                                    "detail": "abcdefghijh",
                                    "openerId": 1,
                                    "eventDate": "2024-12-01",
                                    "startTime":"1700",
                                    "endTime":"1800"
                                }
                                """)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(),MeetingResponseDTO.class);


        //再度一覧を取得する
        MeetingResponseDTOList meetingList_2=objectMapper.readValue(this.mockMvc.perform(get("/meeting")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(), MeetingResponseDTOList.class);
        //数が変わっていないことの検証
        Assertions.assertEquals(0,meetingList_2.getResults().size()-meetingList_1.getResults().size());

        MeetingResponseDTO updatedData=objectMapper.readValue(this.mockMvc.perform(put("/meeting/"+latestData.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "AAA",
                                    "detail": "abcdefghijh",
                                    "openerId": 1,
                                    "eventDate": "2024-12-01",
                                    "startTime":"1700",
                                    "endTime":"1800"
                                }
                                """)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(),MeetingResponseDTO.class);

        //更新されていることの確認
        Assertions.assertEquals("AAA",updatedData.getTitle());


    }

}
