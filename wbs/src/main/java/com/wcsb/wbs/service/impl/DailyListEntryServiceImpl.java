package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcsb.wbs.domain.DailyListEntry;
import com.wcsb.wbs.mapper.DailyListEntryMapper;
import com.wcsb.wbs.service.DailyListEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-14
 */
@Service
public class DailyListEntryServiceImpl extends ServiceImpl<DailyListEntryMapper, DailyListEntry> implements DailyListEntryService {

    @Autowired
    DailyListEntryMapper dailyListEntryMapper;

    public Map<String, Object> GetEntryOfDay(int Year, int Month, int Day){
        LocalDateTime DateTime = LocalDateTime.of(Year, Month, Day, 0, 0);
        QueryWrapper<DailyListEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("currentDate", DateTime);
        // The Logic Here is Weird, Choose The Last Entry
        List<DailyListEntry> EntryListArray=dailyListEntryMapper.selectList(queryWrapper);
        DailyListEntry EntryList;

        if(EntryListArray.size()==1){
            // Has One Entry
            EntryList=EntryListArray.get(0);
        }
        else if(EntryListArray.size()>1){
            // Has Multiple Entries, Choose Last Entry
            EntryList=EntryListArray.get(0);
        }
        else{
            // Has No Entry
            EntryList=null;
        }

        Map<String, Object> MapInfo=new HashMap<>();
        if(EntryList==null){
            // None Existence, Create Entry & Return 0
            DailyListEntry dailyListEntry=new DailyListEntry();
            dailyListEntry.setCurrentDate(DateTime);
            dailyListEntry.setEntryToday(0);
            dailyListEntryMapper.insert(dailyListEntry);
            MapInfo.put("HTTPInfo",200);
            MapInfo.put("Num",1);
        }
        else{
            Integer EntryToday=EntryList.getEntryToday();
            MapInfo.put("HTTPInfo",200);
            MapInfo.put("Num",EntryToday+1);
        }

        //DailyListEntry EntryList=dailyListEntryMapper.selectOne(queryWrapper);
        return MapInfo;
    }

    public Map<String, Object> SetEntryOfDay(int Year, int Month, int Day){
        LocalDateTime DateTime = LocalDateTime.of(Year, Month, Day, 0, 0);
        QueryWrapper<DailyListEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("currentDate", DateTime);
        List<DailyListEntry> EntryDateList=dailyListEntryMapper.selectList(queryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(EntryDateList.size()==0){
            // None Existence, Create Entry & Return 0
            DailyListEntry dailyListEntry=new DailyListEntry();
            dailyListEntry.setCurrentDate(DateTime);
            dailyListEntry.setEntryToday(1);
            dailyListEntryMapper.insert(dailyListEntry);
            MapInfo.put("HTTPInfo",200);
        }
        else{
            DailyListEntry EntryList=EntryDateList.get(0);
            UpdateWrapper<DailyListEntry> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("currentDate", DateTime);
            updateWrapper.set("entryToday", EntryList.getEntryToday()+1);
            dailyListEntryMapper.update(EntryList,updateWrapper);
            MapInfo.put("HTTPInfo",200);
        }
        return MapInfo;
    }
}
