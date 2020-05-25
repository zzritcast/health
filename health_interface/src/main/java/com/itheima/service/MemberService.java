package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    //根据手机号查询会员
    public Member findByTelephone(String telephone);
    //增加会员
    public void add(Member member);

    //会员统计  根据前12个月
    List<Integer> findMemberCountByMonths(List<String> months);
    //分页
    PageResult pageQuery(QueryPageBean queryPageBean);

    void updateById(Member member);

    void deleteById(Integer id);

    Member findById(Integer id);
}
