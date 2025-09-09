package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.Landlord;
import com.ztbdz.web.util.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LandlordService{

    /**
     * 查询业主信息
     * @param landlord
     * @return
     * @throws Exception
     */
    Landlord select(Landlord landlord) throws Exception;

    /**
     * 查询业主信息
     * @param id
     * @return
     * @throws Exception
     */
    Landlord getById(Long id) throws Exception;


    /**
     * 查询多业主
     * @param page
     * @param size
     * @param landlord
     * @return
     */
    PageInfo<Landlord> selectList(Integer page, Integer size, Landlord landlord) throws Exception;

    /**
     * 查询人员信息
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 查询业主列表
     * @param page
     * @param size
     * @param landlord
     * @return
     */
    Result list(Integer page,Integer size,Landlord landlord);

    /**
     * 新增业主
     * @param landlord
     * @return
     * @throws Exception
     */
    Integer insert(Landlord landlord) throws Exception;

    /**
     * 更新业主
     * @param landlord
     * @return
     * @throws Exception
     */
    Integer update(Landlord landlord) throws Exception;

    /**
     * 注册业主
     * @param landlord
     * @param code
     * @return
     */
    Result create(Landlord landlord,String code);

    /**
     * 修改业主
     * @param landlord
     * @return
     */
    Result updateById(Landlord landlord);

    /**
     * 查询业主数量
     * @param landlord
     * @return
     * @throws Exception
     */
    Integer count(Landlord landlord) throws Exception;


    /**
     * 删除业主
     * @param ids
     * @return
     */
    Integer deletes(List<Long> ids) throws Exception;


    /**
     * 批量删除业主
     * @param ids
     * @return
     */
    Result deleteList(List<Long> ids);

    /**
     * 上传Excel业主文件
     * @param file
     * @return
     */
    Result uploadExcel(MultipartFile file);
}
