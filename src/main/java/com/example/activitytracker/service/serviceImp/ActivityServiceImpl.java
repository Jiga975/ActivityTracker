package com.example.activitytracker.service.serviceImp;

import com.example.activitytracker.dto.requestDto.ActivityDto;
import com.example.activitytracker.dto.responseDto.ActivityDtoResponse;
import com.example.activitytracker.exception.ActivityAlreadyExist;
import com.example.activitytracker.exception.ActivityNotFoundException;
import com.example.activitytracker.exception.UnauthorizedUserException;
import com.example.activitytracker.exception.UserNotFoundException;
import com.example.activitytracker.model.Activity;
import com.example.activitytracker.model.User;
import com.example.activitytracker.repository.ActivityRepository;
import com.example.activitytracker.repository.UserRepository;
import com.example.activitytracker.service.ActivityService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository,UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ActivityDtoResponse> getAllActivities(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + userId + "' not found"));

        List<Activity> activities = user.getTasks();

        return activities.stream()
                .map(activity -> new ActivityDtoResponse(
                        activity.getId(),
                        activity.getName(),
                        activity.getDescription(),
                        activity.getStatus(),
                        activity.getCreatedDate(),
                        activity.getDoneDate(),
                        activity.getUpdatedDate(),
                        activity.getUser()
                ))
                .collect(Collectors.toList());
    }



    @Override
    public ActivityDtoResponse getActivityByIdAndName(Long id,String name) {
       // LOGGER.info("Searching for activity with name: " + name);
        Optional<Activity> activityOptional = activityRepository.findActivityByIdAndName(id,name);
        if (activityOptional.isPresent()) {

          //  LOGGER.info("Activity found: " + name);
            Activity activity = activityOptional.get();
            return new ActivityDtoResponse(
                    activity.getId(),
                    activity.getName(),
                    activity.getDescription(),
                    activity.getStatus(),
                    activity.getCreatedDate(),
                    activity.getDoneDate(),
                    activity.getUpdatedDate(),
                    activity.getUser()
            );
        } else {
            //LOGGER.warn("Activity not found with name: " + name);
            throw new ActivityNotFoundException("Activity not found with name: " + name);
        }
    }

    @Override
    public void createActivity(ActivityDto activityDto, Long userId) {
        String activityName = activityDto.getName();

        // Check if an activity with the same name already exists
        if (activityRepository.existsByName(activityName)) {
            throw new ActivityAlreadyExist("Activity with the name '" + activityName + "' already exists.");
        }

        // Retrieve the user from the database using the user's ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID '" + userId + "' not found"));

        // Create a new activity
        Activity activity = new Activity();
        activity.setName(activityDto.getName());
        activity.setDescription(activityDto.getDescription());
        activity.setStatus(activityDto.getStatus());
        activity.setUser(user);

        // Add the activity to the user's task list
        user.addTask(activity);

        // Save the activity and the user
        activityRepository.save(activity);
        userRepository.save(user);
    }



    @Override
    public void updateActivity(Long id, String name, Activity activity, Long userId) {
        Optional<Activity> activityOptional = activityRepository.findActivityByIdAndName(id, name);
        if (activityOptional.isPresent()) {
            Activity existingActivity = activityOptional.get();

            // Ensure that the user trying to update the activity is the same user who created it
            if (!existingActivity.getUser().getId().equals(userId)) {
                throw new UnauthorizedUserException("You are not authorized to update this activity.");
            }

            existingActivity.setName(activity.getName());
            existingActivity.setDescription(activity.getDescription());
            existingActivity.setDoneDate(activity.getDoneDate());
            existingActivity.setCreatedDate(activity.getCreatedDate());
            existingActivity.setUpdatedDate(activity.getUpdatedDate());
            activityRepository.save(existingActivity);
        } else {
            throw new ActivityNotFoundException("Activity not found with ID " + id + " and name " + name);
        }
    }



    @Override
    public void deleteActivity(Long id, String name) {
        Optional<Activity> activityOptional = activityRepository.deleteByIdAndName(id, name);
        if(activityOptional.isPresent()){
            activityRepository.deleteByIdAndName(id, name);
        }else {
            throw new ActivityNotFoundException("Activity not find with "+ id +"and "+ name);
        }

    }

}
