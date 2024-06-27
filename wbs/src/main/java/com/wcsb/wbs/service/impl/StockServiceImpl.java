package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wcsb.wbs.domain.EnstoreList;
import com.wcsb.wbs.domain.Stock;
import com.wcsb.wbs.mapper.StockMapper;
import com.wcsb.wbs.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Autowired
    private StockMapper stockMapper;

    public Map<String, Object> CheckForStockExistence(String SupID){
        Map<String, Object> MapInfo=new HashMap<>();
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_id", SupID);
        List<Stock> stockList=stockMapper.selectList(queryWrapper); // Theoretically there should be just one
        if(stockList.size()==0){
            MapInfo.put("HTTPCode", 500);
        }
        else{
            MapInfo.put("HTTPCode", 200);
            MapInfo.put("Stock", stockList.get(0));
        }
        return MapInfo;
    }

    public Map<String, Object> CheckForStockNum(){
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        List<Stock> StockList=stockMapper.selectList(queryWrapper);

        int LargestID=0;
        for(Stock stock:StockList){
            if(stock.getId()>LargestID){
                LargestID=stock.getId();
            }
        }

        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode", 200);
        MapInfo.put("StockNum", LargestID);
        return MapInfo;
    }

    public Map<String, Object> InsertStock(Stock stock){
        stockMapper.insert(stock);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode", 200);
        return MapInfo;
    }

    public Map<String, Object> UpdateStock(Stock stock, String SupID){
        UpdateWrapper<Stock> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("sup_id", SupID);
        updateWrapper.set("stock_num", stock.getStockNum());
        updateWrapper.set("lowest_num", stock.getLowestNum());
        updateWrapper.set("highest_num", stock.getHighestNum());
        updateWrapper.set("updator",stock.getUpdator());
        updateWrapper.set("update_time",stock.getUpdateTime());
        stockMapper.update(null, updateWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode", 200);
        return MapInfo;
    }

    public Map<String, Object> GetAllStock(){
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        List<Stock> stockList=stockMapper.selectList(queryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(stockList.size()!=0){
            // Has Info
            MapInfo.put("StockList", stockList);
            MapInfo.put("HTTPCode", 200);
        }
        else{
            MapInfo.put("StockList", null);
            MapInfo.put("HTTPCode", 500);
        }
        return MapInfo;
    }

    public Map<String, Object> GetStockViaSupID(String SupID){
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sup_id", SupID);
        List<Stock> stockList=stockMapper.selectList(queryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(stockList.size()!=1){
            MapInfo.put("HTTPCode",500);
        }
        else{
            Stock stock=stockList.get(0);
            MapInfo.put("Stock", stock);
            MapInfo.put("HTTPCode", 200);
        }
        return MapInfo;
    }
}
