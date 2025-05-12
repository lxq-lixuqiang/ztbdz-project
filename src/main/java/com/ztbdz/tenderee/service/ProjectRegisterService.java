package com.ztbdz.tenderee.service;

import com.ztbdz.tenderee.pojo.Project;
import com.ztbdz.tenderee.pojo.ProjectRegister;
import com.ztbdz.web.util.Result;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProjectRegisterService {

    /**
     * 新增项目报名
     * @param projectRegister
     * @return
     */
    Result create(ProjectRegister projectRegister);

    /**
     * 添加项目报名
     * @param projectRegister
     * @return
     */
    Integer insert(ProjectRegister projectRegister) throws Exception;

    /**
     * 查询项目报名
     * @param projectRegister
     * @return
     * @throws Exception
     */
    List<ProjectRegister> selectList(ProjectRegister projectRegister) throws Exception;

    /**
     * 查询项目报名列表
     * @param page
     * @param size
     * @param project
     * @return
     */
    Result page(Integer page,Integer size,Project project,Integer state);

    /**
     * 查询项目报名列表
     * @return
     */
    List<ProjectRegister> selectByCountProjectId(Project project,Long memberId)throws Exception;

    /**
     * 校验报名资质
     * @param memberId
     * @return
     */
    Result checkAptitude(Long memberId);


    /**
     * 上传合同盖章
     * @param id
     * @param contractImprint
     * @return
     */
    Result contractImprint(Long id, String contractImprint);

    /**
     * 上传标书
     * @param id
     * @param bidDocumentId
     * @return
     */
    Result bidDocument(Long id, String bidDocumentId);

    /**
     * 查询投标
     * @param id
     * @return
     */
    Result get(Long id);

    /**
     * 查询投标
     * @param id
     * @return
     */
    ProjectRegister selectById(Long id) throws Exception;

    /**
     * 根据id更新投标
     * @param projectRegister
     * @return
     */
    Integer updateById(ProjectRegister projectRegister) throws Exception;

    /**
     * 批量更新投标
     * @param ids
     * @param projectRegister
     * @return
     */
    Integer updateWinBidState(List<Long> ids,ProjectRegister projectRegister) throws Exception;

    /**
     * 根据项目id查询投标
     * @param projectId
     * @return
     */
    Result getProject(Long projectId);

    /**
     * 统计投标分数
     * @param id
     * @param fileId
     * @return
     */
    Result countScore(Long id,String fileId);

    /**
     * 更新中标状态
     * @param ids
     * @return
     */
    Result winBid(List<Long> ids);

    /**
     * 根据投标id获取其他投标信息
     * @param id
     * @param notIds
     * @return
     * @throws Exception
     */
    List<ProjectRegister> selectListById(Long id,List<Long> notIds) throws Exception;

    /**
     * 根据id查询投标信息包括项目和人员
     * @param ids
     * @return
     * @throws Exception
     */
    List<ProjectRegister> selectMemberProjectByIds(List<Long> ids) throws Exception;
}
