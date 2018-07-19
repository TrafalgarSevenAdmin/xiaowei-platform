package com.xiaowei.attendancesystem.service;

import com.xiaowei.attendancesystem.entity.ChiefEngineer;
import com.xiaowei.core.basic.service.IBaseService;

public interface IChiefEngineerService extends IBaseService<ChiefEngineer>{

    ChiefEngineer saveChiefEngineer(ChiefEngineer chiefEngineer);

    ChiefEngineer updateChiefEngineer(ChiefEngineer chiefEngineer);

    void fakeDeleteChiefEngineer(String chiefEngineerId);

    ChiefEngineer updateStatus(ChiefEngineer chiefEngineer);
}
