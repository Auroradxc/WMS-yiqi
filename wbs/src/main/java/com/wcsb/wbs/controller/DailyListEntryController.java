package com.wcsb.wbs.controller;


import com.wcsb.wbs.service.DailyListEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-14
 */
@RestController
public class DailyListEntryController {
    @Autowired
    private DailyListEntryService dailyListEntryService;

    @PostMapping("/get_new_enstore_num_today")
    public Map<String, Object> getEnstoreNumToday() {
        // Get Date of Today
        LocalDateTime now = LocalDateTime.now();
        int Year=now.getYear();
        int Month=now.getMonthValue();
        int Day=now.getDayOfMonth();
        Map<String, Object> MapInfo=dailyListEntryService.GetEntryOfDay(Year, Month, Day);
        return MapInfo;
    }
}
