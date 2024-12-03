package com.example.study_org_server.repository.user;



import lombok.Value;

import java.sql.Timestamp;

public record UserRecord(
        Long id,
        String email,
        String password,
        String authorityId,
        Timestamp createdAt,
        Timestamp updatedAt
) {

}
