package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.DailyListEntry;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-14
 */
public interface DailyListEntryService extends IService<DailyListEntry> {
    // Get Which Entry This Is Of Day Today
    Map<String, Object> GetEntryOfDay(int Year, int Month, int Day);

    // Set Entry of Today as +1
    Map<String, Object> SetEntryOfDay(int Year, int Month, int Day);
}
