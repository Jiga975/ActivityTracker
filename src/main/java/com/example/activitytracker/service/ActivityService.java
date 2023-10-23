package com.example.activitytracker.service;


import com.example.activitytracker.dto.requestDto.ActivityDto;
import com.example.activitytracker.dto.responseDto.ActivityDtoResponse;
import com.example.activitytracker.model.Activity;

import java.util.List;

public interface ActivityService {
    List<ActivityDtoResponse> getAllActivities(Long userId);
    ActivityDtoResponse getActivityByIdAndName(Long id,String name);
    void createActivity(ActivityDto activityDto, Long id);
    void updateActivity(Long id, String name, Activity activity, Long userId);
    void deleteActivity(Long id, String name);
}
