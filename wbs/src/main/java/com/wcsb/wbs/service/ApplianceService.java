package com.wcsb.wbs.service;

import com.wcsb.wbs.domain.Appliance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 器具表 服务类
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-03
 */
public interface ApplianceService extends IService<Appliance> {
    Map<String, Object> GetAllAppliances();
    Map<String, Object> GetApplianceByID(String ID);
    Map<String, Object> GetApplianceBySupplementIDAndWrapSize(String SupplementID,Integer WrapSize);
    Map<String, Object> GetApplianceBySupID(String SupplementID);

    // Add Appliance
    Map<String, Object> InsertAppliance(Appliance appliance);

    // Delete Appliance
    Map<String, Object> DeleteAppliance(String WrapID);
}
