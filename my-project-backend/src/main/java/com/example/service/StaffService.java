package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Staff;
import com.example.entity.vo.request.StaffConditionVO;
import com.example.entity.vo.request.StaffSaveVO;
import com.example.entity.vo.response.StaffPreViewVO;

import java.util.List;

public interface StaffService extends IService<Staff> {
    List<StaffPreViewVO> listStaffByPage(StaffConditionVO vo);
    void saveStaff(StaffSaveVO vo);
    void deleteStaff(Integer id);
}
