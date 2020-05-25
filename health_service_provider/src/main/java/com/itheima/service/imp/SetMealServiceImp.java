package com.itheima.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐管理
 */
@Service(interfaceClass =SetMealService.class)
@Transactional
public class SetMealServiceImp implements SetMealService {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    @Value("${out_put_path}")
    private String outPutPath;  //从属性文件中读取要生成的html对应的目录

    /**
     * 套餐的新增
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //增加套餐管理
        this.setMealDao.add(setmeal);
        //获取当前套餐的id
        Integer setmealId = setmeal.getId();
        //设置套餐和检查组的多对多的关联关系，操作t_setmeal_checkgroup表
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
            //将保存到数据库的图片存放于redis中
        String fileName = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,fileName);
        //新增套餐后需要重新生成静态页面(套餐列表页面  套餐详情页面)
        generateMobileStaticHtml();

    }

    //生成当前方法所需的静态页面
    public void generateMobileStaticHtml(){
        //准备模板文件中所需要的数据
        List<Setmeal> setmealList = this.setMealDao.findAll();
        //生成套餐列表静态页面
        generateMobileSetmealListHtml(setmealList);
        //生成套餐详情静态页面（多个）
        generateMobileSetmealDetailHtml(setmealList);
    }
    //生成套餐详情静态页面（多个）
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {

        Map<String, Object> map = new HashMap<>();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList",setmealList);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }
    //生成套餐列表静态页面
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        for (Setmeal setmeal : setmealList) {
            Map<String, Object> map = new HashMap<>();
            map.put("setmeal",this.setMealDao.findBySetmealId(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",map);
        }
    }
    //用于生产静态页面  模板名 页面名  参数
    public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
        //获得freemarker的配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try{
            //加载模板文件
            Template template = configuration.getTemplate(templateName);
            configuration.setDefaultEncoding("utf-8");
            //生成数据
            File docFile = new File(outPutPath + "\\" +htmlPageName);
           // 构造输出流
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            //输出文件
            template.process(dataMap,out);

         }catch(Exception e){
            e.printStackTrace();
        }finally {
             try{
                 if(null != out){
                     out.flush();
                     out.close();
                 }
             }catch (Exception e2){
                 e2.printStackTrace();;
             }

        }

    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //获取当前页
        Integer currentPage = queryPageBean.getCurrentPage();

        Integer pageSize = queryPageBean.getPageSize();//每页显示的记录数

        String queryString = queryPageBean.getQueryString();//查询的条件呢

        //分页助手
        PageHelper.startPage(currentPage,pageSize);
        //根据条件查询
        Page<Setmeal> setmealPage =this.setMealDao.findByCondition(queryString);

        System.out.println(setmealPage.getTotal());
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    /**
     * 删除套餐
     * @param id
     */
    @Override
    public void deleteById(Integer id) {


    //清理当前套餐以及关联的检查组
        this.setMealDao.deleteAssocication(id);
        /*this.checkGroupDao.deleteAssocication(id);*/
        //通过套餐id删除套餐
        this.setMealDao.deleteById(id);

    }
////查询所有
    @Override
    public List<Setmeal> findAll() {
        return this.setMealDao.findAll();
    }
//通过id查询
    @Override
    public Setmeal findById(Integer id) {
        return this.setMealDao.findById(id);
    }
// //根据检查组ID查询关联的检查项ID
    @Override
    public List<Integer> findCheckGroupIdsBySetMealId(Integer id) {
        return this.setMealDao.findCheckGroupIdsBySetMealId(id);
    }
    //编辑
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //修改检查组基本信息，操作检查组t_checkgroup表
        this.setMealDao.edit(setmeal);
        //清理当前检查组关联的检查项，操作中间关系表t_checkgroup_checkitem表
        this.setMealDao.deleteAssocication(setmeal.getId());
        //重新建立当前检查组和检查项的关联关系
        Integer setmealId = setmeal.getId();
        this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
    }
    //根据套餐id查询 （检查组  检查项）
    @Override
    public Setmeal findBySetmealId(int id) {
        return this.setMealDao.findBySetmealId(id);
    }
    //查询预约占比数据
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return this.setMealDao.findSetmealCount();
    }


    //建立套餐和检查组多对多关系  抽取
    public void setSetmealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){

        if(checkgroupIds != null && checkgroupIds.length > 0){
            Map<String ,Integer> map =null;
            for (Integer checkgroupId : checkgroupIds) {
                map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                this.setMealDao.setSetMealAndCheckGroup(map);
            }
        }
    }
}
