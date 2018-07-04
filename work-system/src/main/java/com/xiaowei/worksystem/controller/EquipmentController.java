package com.xiaowei.worksystem.controller;

import com.xiaowei.worksystem.service.IEquipmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备管理
 */
@Api(tags = "设备接口")
@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private IEquipmentService equipmentService;

}
