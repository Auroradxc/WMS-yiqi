package com.wcsb.wbs.controller;


import com.wcsb.wbs.domain.BarCode;
import com.wcsb.wbs.domain.EnstoreListDetail;
import com.wcsb.wbs.service.BarCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 不知道这个in_out_state是来自于那里的，所以目前不用这个表 前端控制器
 * </p>
 *
 * @author Jessica_Clorize
 * @since 2024-06-14
 */
@RestController
public class BarCodeController {
    @Autowired
    private BarCodeService barCodeService;

    @PostMapping("/generate_bar_code")
    public List<BarCode> generateBarCode(@RequestBody EnstoreListDetail enstoreListDetail) {
        // Maybe the info sent in is supposed to be list_num and sup_id
        // With list_num and sup_id, one can locate a single entry within enstore_list_detail
        // which has critical value we need to derive from, (act_in_num) and (wrap_size)
        // Now obviously act_in_num and wrap_size can lead to the calculation of a box seperation
        // The only problem is, there is no way of finding how many stock there is in a box
        return null;
    }
}

