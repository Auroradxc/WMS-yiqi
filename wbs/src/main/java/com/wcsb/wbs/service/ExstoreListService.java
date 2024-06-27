package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.ExstoreList;
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
public interface ExstoreListService extends IService<ExstoreList> {
    // Get All ExstoreList (Later To Be Filtered)
    Map<String, Object> GetExstoreList();

    // Get ExstoreList Via List Num
    Map<String, Object> GetExstoreListByListNum(String ListNum);

    // Create ExstoreList Entry
    Map<String, Object> CreateExstoreList(ExstoreList exstoreList);

    // Modify ExstoreList Customer
    Map<String, Object> UpdateExstoreListViaListNum(ExstoreList exstoreList, String Customer);

    // Delete ExstoreList Entry
    Map<String, Object> DeleteExstoreList(String ListNum);

    // Update ExstoreList Entry Status
    Map<String, Object> UpdateEnstoreListStatus(ExstoreList exstoreList, String Status);

    // Update ExstoreList Combined
    Map<String, Object> UpdateExstoreList(String ListNum, String Customer, String Status);
}
