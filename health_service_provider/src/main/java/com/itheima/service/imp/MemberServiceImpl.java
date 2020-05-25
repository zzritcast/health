package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MemberDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员服务
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //保存会员信息
    public void add(Member member) {
        //为了防止其他表单调用
        String password = member.getPassword();
        if(password != null){
            //使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }

    /**
     * 统计会员数量  前十二个月的数据
     * @param months
     * @return
     */
    //根据月份查询会员数量
    public List<Integer> findMemberCountByMonths(List<String> months) {//2018.05
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            String date = month + ".31";//2018.05.31
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }
        return memberCount;
    }
    //分页
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //获取当前页
        Integer currentPage = queryPageBean.getCurrentPage();

        Integer pageSize = queryPageBean.getPageSize();//每页显示的记录数

        String queryString = queryPageBean.getQueryString();//查询的条件呢

        //分页助手
        PageHelper.startPage(currentPage,pageSize);
        //根据条件查询
      Page<Member> memberPage = this.memberDao.selectByCondition(queryString);

        System.out.println(memberPage.getTotal());
        return new PageResult(memberPage.getTotal(),memberPage.getResult());
    }
//更新
    @Override
    public void updateById(Member member) {
        this.memberDao.edit(member);
    }
//删除
    @Override
    public void deleteById(Integer id) {
        this.memberDao.deleteById(id);

    }
//单个
    @Override
    public Member findById(Integer id) {
       return this.memberDao.findById(id);
    }
}
