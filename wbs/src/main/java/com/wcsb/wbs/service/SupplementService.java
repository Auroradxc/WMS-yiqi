package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.Supplement;
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
public interface SupplementService extends IService<Supplement> {
    // Returns the whole table
    Map<String, Object> GetSupplementList();

    // Returns by searching
    Map<String, Object> GetSupplementByID(String sup_id);
    Map<String, Object> GetSupplementBySupplierID(String supplier_id);
    Map<String, Object> GetSupplementByIDAndSupplierID(String sup_id, String supplier_id);

    // Insert Supplement
    Map<String, Object> InsertSupplement(Supplement supplement);

    // Delete Supplement
    Map<String, Object> DeleteSupplement(Supplement supplement);
}
