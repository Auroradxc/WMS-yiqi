package com.wcsb.wbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wcsb.wbs.domain.Customer;
import com.wcsb.wbs.mapper.CustomerMapper;
import com.wcsb.wbs.service.CustomerService;
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
 * @since 2024-06-19
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    public Map<String, Object> GetCustomerList(){
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        List<Customer> CustomerList=customerMapper.selectList(queryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(CustomerList.size()==0){
            MapInfo.put("HTTPCode", 500);
            MapInfo.put("CustomerList", null);
        }
        else{
            MapInfo.put("HTTPCode", 200);
            MapInfo.put("CustomerList", CustomerList);
        }
        return MapInfo;
    }

    public Map<String, Object> AddCustomer(Customer customer){
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", customer.getCustomerId());
        List<Customer> customerList=customerMapper.selectList(queryWrapper);
        Map<String, Object> MapInfo=new HashMap<>();
        if(customerList.size()==0){
            customerMapper.insert(customer);
            MapInfo.put("HTTPCode", 200);
        }
        else{
            MapInfo.put("HTTPCode", 500);
        }
        return MapInfo;
    }

    public Map<String, Object> DeleteCustomer(Customer customer){
        Map<String, Object> MapInfo=new HashMap<>();
        MapInfo.put("customer_id",customer.getCustomerId());
        customerMapper.deleteByMap(MapInfo);
        MapInfo=new HashMap<>();
        MapInfo.put("HTTPCode",200);
        return MapInfo;
    }

}
