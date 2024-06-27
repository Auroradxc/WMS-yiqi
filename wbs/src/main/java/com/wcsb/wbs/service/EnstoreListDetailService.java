package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.EnstoreList;
import com.wcsb.wbs.domain.EnstoreListDetail;
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
public interface EnstoreListDetailService extends IService<EnstoreListDetail> {

    // Insert New Rows Via List Num
    Map<String, Object> InsertNewEnstoreListDetail(EnstoreListDetail enstoreListDetail);

    // Delete Rows Via List Num
    Map<String, Object> DeleteEnstoreListDetail(String ListNum);

    // Get Row Info
    Map<String, Object> RetrieveEnstoreListDetail(String ListNum);

    // Get Row Via List Num & SupID
    Map<String, Object> RetrieveEnstoreListDetailOfSupplement(String ListNum, String SupID);

    // Get All EnstoreList
    Map<String, Object> RetrieveEnstoreListTotal(String ListNum);

    // Get EnstoreListDetail Full Detail
    Map<String, Object> RetrieveEnstoreListFull(String ListNum);

    // Update EnstoreList Detail
    Map<String, Object> UpdateEnstoreListViaSupID(EnstoreListDetail enstoreListDetail, Integer ActInNum);
}
