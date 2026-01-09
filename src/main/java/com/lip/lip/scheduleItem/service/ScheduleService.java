package com.lip.lip.scheduleItem.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lip.lip.scheduleItem.entity.ScheduleItem;
import com.lip.lip.scheduleItem.repository.ScheduleItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleItemRepository repository;

    public ScheduleItem save(ScheduleItem item) {
        return repository.save(item);
    }

    public Optional<ScheduleItem> getWeeklySchedule(Long userId) {
        return repository.findById(userId);
    }
}
