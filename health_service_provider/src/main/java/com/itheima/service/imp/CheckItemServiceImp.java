package com.itheima.service.imp;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImp implements CheckItemService {
    //注入dao对象
    @Autowired
   private CheckItemDao checkItemDao;

    /**
     * 新增
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
       this.checkItemDao.add(checkItem);

    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //前端传入后端的数据
        //当前页码
        Integer currentPage = queryPageBean.getCurrentPage();
        //每页条数
        Integer pageSize = queryPageBean.getPageSize();
        //查询条件
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_checkitem limit 0,10
        Page<CheckItem> page = this.checkItemDao.selectByCondition(queryString);
        //后端返回前端的数据
        //总记录数
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();
        //分页结果集
        return new PageResult(total,rows);
    }

    /**
     * 删除id删除检查项
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联到检查组
        long count = this.checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            //当前检查项已经关联到检查组 不允许删除
             throw new RuntimeException("当前检查项已经关联到其他检查组 ，不允许删除");
        }
        //不存在 删除即可
        this.checkItemDao.deleteById(id);
    }

    /**
     * 修改检查项
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        this.checkItemDao.update(checkItem);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        return this.checkItemDao.findById(id);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return this.checkItemDao.findAll();
    }
}
