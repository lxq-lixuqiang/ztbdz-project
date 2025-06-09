package com.ztbdz.user.service;

import com.github.pagehelper.PageInfo;
import com.ztbdz.user.pojo.Account;
import com.ztbdz.user.pojo.BidderInfo;
import com.ztbdz.user.pojo.ExpertInfo;
import com.ztbdz.web.util.Result;
import org.springframework.web.multipart.MultipartFile;

public interface ExpertInfoService {

    /**
     * 添加专家
     * @param expertInfo
     * @return
     */
    Result create(ExpertInfo expertInfo);

    /**
     * 查询专家
     * @param memberId
     * @return
     */
    Result getMemberInfo(Long memberId);

    /**
     * 更新专家
     * @param expertInfo
     * @return
     */
    Result update(ExpertInfo expertInfo);

    /**
     * 查询专家
     * @param memberId
     * @return
     */
    ExpertInfo selectByMemberId(Long memberId) throws Exception;

    /**
     * 更新专家
     * @param expertInfo
     * @return
     */
    Integer updateById(ExpertInfo expertInfo) throws Exception;

    /**
     * 添加专家
     * @param expertInfo
     * @return
     */
    Integer insert(ExpertInfo expertInfo) throws Exception;

    /**
     * 查询多专家
     * @param page
     * @param size
     * @param expertInfo
     * @return
     */
    Result list(Integer page,Integer size,ExpertInfo expertInfo);

    /**
     * 查询多专家
     * @param page
     * @param size
     * @param expertInfo
     * @return
     */
    PageInfo<ExpertInfo> page(Integer page, Integer size, ExpertInfo expertInfo) throws Exception;


    /**
     * 上传Excel专家文件
     * @param file
     * @return
     */
    Result uploadExcel(MultipartFile file);
}
