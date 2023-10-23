package com.example.activitytracker.controller;

import com.example.activitytracker.dto.requestDto.ActivityDto;
import com.example.activitytracker.dto.responseDto.ActivityDtoResponse;
import com.example.activitytracker.exception.ActivityNotFoundException;
import com.example.activitytracker.exception.UnauthorizedUserException;
import com.example.activitytracker.model.Activity;
import com.example.activitytracker.service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class ActivityController {
    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/user_page")
    public String showUserPage(Model model) {
        ActivityDto activityDto = new ActivityDto(); // Create an instance of ActivityDto
        model.addAttribute("activityDto", activityDto); // Add it to the model
        model.addAttribute("message", ""); // Initialize an empty message
        return "user_page";
    }

    @PostMapping("/create_activity")
    public String createActivity(@ModelAttribute @Validated ActivityDto activityDto, HttpSession session, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Handle validation errors here
            model.addAttribute("message", "Activity creation failed. Please check your input.");
            return "user_page";
        }

        try {
            activityService.createActivity(activityDto, Long.parseLong(String.valueOf(session.getAttribute("userId"))));
            model.addAttribute("message", "Activity created successfully.");
        } catch (Exception e) {
            model.addAttribute("message", "Activity creation failed. An error occurred.");
        }

        return "user_page";
    }
    @GetMapping("/view_all_tasks")
    public String viewAllTasks(Model model, HttpSession session) {
        // Retrieve the userId from the session
        Long userId = (Long) session.getAttribute("userId");

        // Fetch tasks associated with the current user
        List<ActivityDtoResponse> tasks = activityService.getAllActivities(userId);

        model.addAttribute("tasks", tasks);
        return "activitiesView_page";
    }

    @GetMapping("/view_activity_by_Id_And_name")
    public String viewActivityByName(@RequestParam("task_name") String name, @RequestParam("id") Long id, Model model) {
        System.out.println("Name before trimming: " + name);
        String cleanedName = name.trim();
        ActivityDtoResponse activityDtoResponse = activityService.getActivityByIdAndName(id,cleanedName);
        System.out.println(activityDtoResponse);
        System.out.println("Name after trimming: " + cleanedName);
        model.addAttribute("activity", activityDtoResponse);
        return "activityView_page";
    }

    @PostMapping("/activityUpdate")
    public String updateActivity(@RequestParam("id") Long id,
                                 @RequestParam(value = "task_name", required = false) String name,
                                 @ModelAttribute @Validated Activity activity,
                                 BindingResult result,
                                 Model model,
                                 HttpServletRequest request) {
        if (result.hasErrors()) {
            // Handle validation errors here
            model.addAttribute("message", "Activity update failed. Please check your input.");
            return "activityUpdate_page";
        }

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                Long userId = (Long) session.getAttribute("userId");
                if (userId != null) {
                    activityService.updateActivity(id, name, activity, userId);
                    model.addAttribute("message", "Activity updated successfully.");
                } else {
                    throw new UnauthorizedUserException("Unauthorized user");
                }
            } else {
                throw new UnauthorizedUserException("Unauthorized user");
            }
        } catch (ActivityNotFoundException | UnauthorizedUserException e) {
            model.addAttribute("message", e.getMessage());
        }

        return "activityUpdate_page";
    }

    @GetMapping("/activityUpdate_page")
    public String showUpdateActivityPage(@RequestParam("id") Long id, @RequestParam("task_name") String name, Model model) {
        try {
            // Add logic to fetch the activity based on the provided ID and name
            ActivityDtoResponse activity = activityService.getActivityByIdAndName(id, name);
            model.addAttribute("activity", activity);
        } catch (ActivityNotFoundException e) {
            model.addAttribute("message", e.getMessage());
        }

        return "activityUpdate_page";
    }


}
