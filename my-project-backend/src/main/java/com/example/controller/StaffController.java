package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.StaffConditionVO;
import com.example.entity.vo.request.StaffSaveVO;
import com.example.entity.vo.response.StaffPreViewVO;
import com.example.service.StaffService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Resource
    private StaffService service;

    @PostMapping("/list-staff")
    public RestBean<List<StaffPreViewVO>> listStaff(@RequestBody @Valid StaffConditionVO vo){
        return RestBean.success(service.listStaffByPage(vo));
    }

    @PostMapping("/save-staff")
    public RestBean<Void> saveStaff(@RequestBody @Valid StaffSaveVO vo){
        service.saveStaff(vo);
        return RestBean.success();
    }

    @GetMapping("/delete-staff")
    public RestBean<Void> deleteStaff(@RequestParam("id") Integer id){
        service.deleteStaff(id);
        return RestBean.success();
    }
}
