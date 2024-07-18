package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Staff;
import com.example.entity.vo.request.StaffConditionVO;
import com.example.entity.vo.request.StaffSaveVO;
import com.example.entity.vo.response.StaffPreViewVO;
import com.example.mapper.StaffMapper;
import com.example.service.StaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {
    @Override
    public List<StaffPreViewVO> listStaffByPage(StaffConditionVO vo) {
        Page<Staff> page = Page.of(vo.getPage(), 10);

        QueryWrapper<Staff> query = Wrappers.query();
        Optional.ofNullable(vo.getName()).filter(name -> !name.isEmpty())
                .ifPresent(name -> query.like("name", name));
        Optional.ofNullable(vo.getGender())
                .ifPresent(gender -> query.eq("gender", gender));
        Optional.ofNullable(vo.getDepartment()).filter(department -> !department.isEmpty())
                .ifPresent(department -> query.like("department", department));
        query.orderByDesc("time");
        // 查询并转换返回结果
        return baseMapper.selectPage(page, query).getRecords().stream()
                .map(this::convertToStaffPreViewVO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveStaff(StaffSaveVO vo) {
        Staff staff = new Staff();
        staff.setTime(new Date());
        BeanUtils.copyProperties(vo, staff);
        if(baseMapper.selectById(vo.getId()) == null)
            baseMapper.insert(staff);
        else
            baseMapper.updateById(staff);
    }

    @Override
    public void deleteStaff(Integer id) {
        baseMapper.deleteById(id);
    }

    private StaffPreViewVO convertToStaffPreViewVO(Staff staff) {
        StaffPreViewVO vo = new StaffPreViewVO();
        BeanUtils.copyProperties(staff, vo);
        return vo;
    }
}
