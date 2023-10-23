package com.example.activitytracker.dto.responseDto;

import com.example.activitytracker.enums.Status;
import com.example.activitytracker.model.User;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor

public class ActivityDtoResponse {
    private Long id;
    private String name;
    private String description;
    private Status status;
    private Timestamp createdDate;
    private Timestamp upDateDate;
    private Timestamp doneDate;
    private User user;
}
