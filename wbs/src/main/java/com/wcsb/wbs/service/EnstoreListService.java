package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.EnstoreList;
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
public interface EnstoreListService extends IService<EnstoreList> {
    // Returns whole table
    Map<String, Object> GetEnstoreList();

    // Returns by searching
    Map<String, Object> GetEnstoreListByStatus(String Status);
    Map<String, Object> GetEnstoreListByListNum(String ListNum);
    Map<String, Object> GetEnstoreListByStatusAndListnum(String Status, String ListNum);

    // Get Size
    int GetEnstoreListSize();

    // Add Row
    Map<String, Object> InsertNewEnstoreList(EnstoreList enstoreList);

    // Delete Row
    Map<String, Object> DeleteEnstoreList(String ListNum);

    // Modify Row : Supplier
    Map<String, Object> UpdateEnstoreListViaListNum(EnstoreList enstoreList, String Supplier);

    // Modify Row : Status
    Map<String, Object> UpdateEnstoreListStatus(EnstoreList enstoreList, String Status);

    // Modify Row: Supplier, Status
    Map<String, Object> UpdateEnstoreList(String ListNum, String Supplier, String Status);
}
