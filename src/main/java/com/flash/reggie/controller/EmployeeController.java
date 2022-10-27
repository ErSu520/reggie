package com.flash.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flash.reggie.common.R;
import com.flash.reggie.entity.Employee;
import com.flash.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // 将页面提交的密码使用md5进行加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 根据页面的用户名进行数据库查询
        LambdaQueryWrapper<Employee> queryChainWrapper = new LambdaQueryWrapper<>();
        queryChainWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee employee1 = employeeService.getOne(queryChainWrapper);
        // 判断查询结果
        if(employee1 == null){
            return  R.error("登录失败");
        }
        // 密码比对
        if(!employee1.getPassword().equals(password)){
            return R.error("登录失败");
        }
        // 查看员工的状态 是否为禁用状态
        if(employee1.getStatus() == 0){
            return R.error("登录失败");
        }
        // 登录成功 将员工信息放入session 并返回成功结果
        request.getSession().setAttribute("employee", employee1.getId());
        return R.success(employee1);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("logout success");
    }

    private static final String DEFAULT_PASSWORD = DigestUtils.md5DigestAsHex("123456".getBytes());

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("插入员工信息成功{}", employee.toString());
        employee.setPassword(DEFAULT_PASSWORD);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long operatorId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(operatorId);
//        employee.setUpdateUser(operatorId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    @GetMapping("page")
    public R<Page<Employee>> page(HttpServletRequest request, int page, int pageSize, String name){
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee){
        // 构建更新信息
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getId, employee.getId());
        employeeService.update(employee, queryWrapper);
        return R.success("更新成功");
    }

    @GetMapping("/{id}")
    public R<Employee> singleEmployee(@PathVariable Long id){
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getId, id);
        return R.success(employeeService.getOne(lambdaQueryWrapper));
    }

}
