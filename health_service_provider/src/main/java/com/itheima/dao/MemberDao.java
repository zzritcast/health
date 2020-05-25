package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;
import com.itheima.pojo.Setmeal;

import java.util.List;

public interface MemberDao {
    //增加会员
    public void add(Member member);
    //通过手机号查询会员对象
    public Member findByTelephone(String telephone);
    //查询所有
    public List<Member> findAll();
    //分页条件查询
    public Page<Member> selectByCondition(String queryString);
    //删除
    public void deleteById(Integer id);
    //同过id查询
    public Member findById(Integer id);
    //编辑
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();


}
