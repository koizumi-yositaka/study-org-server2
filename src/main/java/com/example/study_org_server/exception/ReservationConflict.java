package com.example.study_org_server.exception;

import java.time.LocalDate;

public class ReservationConflict extends RuntimeException {

    private LocalDate date;
    public ReservationConflict(LocalDate localDate) {
        super(localDate.toString()+"が重複しています");
    }
}
