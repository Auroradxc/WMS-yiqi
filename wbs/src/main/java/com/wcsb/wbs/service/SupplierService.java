package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.Customer;
import com.wcsb.wbs.domain.Supplier;
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
public interface SupplierService extends IService<Supplier> {
    // Get All Suppliers
    Map<String, Object> GetAllSuppliers();

    // Insert Supplier
    Map<String, Object> AddSupplier(Supplier supplier);

    // Delete Supplier
    Map<String, Object> DeleteSupplier(Supplier supplier);
}
