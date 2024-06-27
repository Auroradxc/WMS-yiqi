package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.ExstoreListDetail;
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
public interface ExstoreListDetailService extends IService<ExstoreListDetail> {
    // Insert One ExstoreList Detail (listnum, sup_id)
    Map<String, Object> InsertExstoreListDetail(ExstoreListDetail exstoreListDetail);

    // Get All ExstoreList
    Map<String, Object> RetrieveExstoreListTotal(String ListNum);

    // Get ExstoreList Via SupplementID And ListNum
    Map<String, Object> RetrieveOneExstoreList(String ListNum, String SupplementID);

    // Delete Rows Via List Num
    Map<String, Object> DeleteExstoreListDetail(String ListNum);

    // Get Full ExstoreList
    Map<String, Object> RetrieveExstoreListFull(String ListNum);

    // Update One ExstoreList
    Map<String, Object> UpdateExstoreListViaSupID(ExstoreListDetail exstoreListDetail, Integer ActInNum);
}
