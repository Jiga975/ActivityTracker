package com.example.activitytracker.repository;

import com.example.activitytracker.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Optional<Activity> findActivityByIdAndName(Long id, String name);
    Optional<Activity> deleteByIdAndName(Long id, String name);

    Boolean existsByName(String activityName);
}