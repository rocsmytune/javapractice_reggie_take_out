package com.project.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.project.reggie.common.R;
import com.project.reggie.entity.Employee;
import com.project.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /***
     * employee login
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //梳理好流程，再编写代码

        //md5 digest
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //query DB according to the username form page
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>(); //wrapping query object
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //return failure if there are no matching result
        if (emp == null) {
            return R.error("Login Failure");
        }

        //compare the password and judge if return failure
        if (!emp.getPassword().equals(password)) {
            return R.error("Login Failure");
        }

        //check employee account status
        if (emp.getStatus() != 1) {
            return R.error("Account Blocked");
        }

        //login success, add employee id into Session and return success
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }
}
