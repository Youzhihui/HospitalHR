package com.jxau.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jxau.pojo.Department;
import com.jxau.pojo.Employee;
import com.jxau.pojo.Overtime;
import com.jxau.service.DepartmentService;
import com.jxau.service.EmployeeService;
import com.jxau.service.OvertimeService;
import com.jxau.util.MTimeUtil;

@Controller
@RequestMapping("/overtime")
public class OvertimeController {

	@Autowired
	private OvertimeService overtimeService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private DepartmentService departmentService;
	
	/*所以员工加班信息管理*/
	@RequestMapping("/listPage.do")
	public String selectListByPgae(Model model, int pageNo){
		Page<Overtime> page = overtimeService.selectListByPage(pageNo);
		model.addAttribute("page",page);
		return "admin/overtime_list";
	}
	
	/*跳转添加安排加班信息（还未填写）*/
	@RequestMapping("/toAdd.do")
	public String toAdd(Model model){
		//查询出所有的部门
		List<Department> dList = departmentService.selectList(new EntityWrapper<Department>());
		model.addAttribute("dList", dList);
		//查询出所有的员工
		List<Employee> eList = employeeService.selectList(new EntityWrapper<Employee>());
		model.addAttribute("eList", eList );
		return "admin/overtime_add";
	}
	
	/*添加安排加班信息入数据库（已填写完）*/
	@RequestMapping("/add.do")
	public String add(Overtime overtime, String date){
		overtime.setDay(MTimeUtil.stringParse(date));
		overtimeService.insert(overtime);
		return "forward:/overtime/listPage.do?pageNo=1";
	}
	
	/*跳转“去”修改加班信息（还未修改）*/
	@RequestMapping("/{id}/toUpdate.do")
	public String toUpdate(Model model, @PathVariable Integer id){
		//查询出要修改的记录信息
		Overtime overtime = overtimeService.selectById(id);
		model.addAttribute("overtime", overtime);
		//查询出所有的部门
		List<Department> dList = departmentService.selectList(new EntityWrapper<Department>());
		model.addAttribute("dList", dList);
		//查询出所有的员工
		List<Employee> eList = employeeService.selectList(new EntityWrapper<Employee>());
		model.addAttribute("eList", eList );
		return "admin/overtime_update";
	}
	
	/*跳转修改加班信息（已修改完）*/
	@RequestMapping("/{id}/update.do")
	public String updateById(@PathVariable Integer id,  String date, Overtime overtime){
		overtime.setId(id);
		overtime.setDay(MTimeUtil.stringParse(date));
		overtimeService.updateById(overtime);
		return "forward:/overtime/listPage.do?pageNo=1";//重定向：跳转所以员工加班信息
	}
	
	@RequestMapping("/{id}/delete.do")
	public String deleteById(@PathVariable Integer id){
		overtimeService.deleteById(id);
		return "forward:/overtime/listPage.do?pageNo=1";
	}
	
	/*查看自己个人加班记录*/
	@RequestMapping("/{employeeNumber}/oneself.do")
	public String select(Model model, @PathVariable Integer employeeNumber, int pageNo){
		Page<Overtime> page = overtimeService.selectByEmployee(pageNo, employeeNumber);
		model.addAttribute("page",page);
		return "admin/oneself_overtime";
	}
	
}
