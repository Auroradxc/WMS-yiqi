package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.Stock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
public interface StockService extends IService<Stock> {
    // Check if Stock Already Exists
    Map<String, Object> CheckForStockExistence(String SupID);

    // Check for Stock Total Number
    Map<String, Object> CheckForStockNum();

    // Insert Stock
    Map<String, Object> InsertStock(Stock stock);

    // Update Stock
    Map<String, Object> UpdateStock(Stock stock, String SupID);

    // Get All Stock
    Map<String, Object> GetAllStock();

    // Get Stock Via SupID
    Map<String, Object> GetStockViaSupID(String SupID);
}
