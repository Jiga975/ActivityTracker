package com.example.activitytracker.dto.requestDto;

import com.example.activitytracker.enums.Status;
import com.example.activitytracker.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ActivityDto {
//    @NotEmpty
//    @NotBlank
//    @NotNull
//    @Size(min = 3, max = 15)
    private String name;
//    @NotEmpty @NotBlank @NotNull
//    @Size(min = 20, max = 1000)
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    @CreationTimestamp
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp upDateDate;
    private Timestamp doneDate;
    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,
            CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;
}
