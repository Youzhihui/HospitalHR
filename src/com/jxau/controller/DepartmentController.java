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
import com.jxau.service.DepartmentService;

@Controller
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	/*部门管理*/
	@RequestMapping("/listPage.do")
	public String selectListByPgae(Model model, int pageNo){
		Page<Department> page = departmentService.selectListByPage(pageNo);
		model.addAttribute("page",page);
		return "admin/department_list";
	}
	
	/*跳转“去”添加新的部门信息（还未填写新部门信息）*/
	@RequestMapping("/toAdd.do")
	public String toAdd(Model model){
		List<Department> dList = departmentService.selectList(new EntityWrapper<Department>()
				.orderBy("department_number", false));
		model.addAttribute("departmentNumber", dList.get(0).getDepartmentNumber()+1);
		return "admin/department_add";
	}
	
	/*插入添加新的部门信息入数据库（已填写新部门信息）*/
	@RequestMapping("/add.do")
	public String add(Department department){
		departmentService.insert(department);
		return "forward:/department/listPage.do?pageNo=1";
	}
	
	/*跳转“去”修改部门信息（还未修改信息）*/
	@RequestMapping("/{id}/toUpdate.do")
	public String toUpdate(@PathVariable Integer id, Model model){
		Department department = departmentService.selectById(id);
		model.addAttribute("department", department);
		return "admin/department_update";
	}
	
	/*插入修改部门信息（已修改信息）*/
	@RequestMapping("/{id}/update.do")
	public String updateById(@PathVariable Integer id, Department department){
		department.setId(id);
		departmentService.updateById(department);
		return "forward:/department/listPage.do?pageNo=1";
	}
	
	/*删除部门信息*/
	@RequestMapping("/{id}/delete.do")
	public String deleteById(@PathVariable Integer id){
		departmentService.deleteById(id);
		return "forward:/department/listPage.do?pageNo=1";
	}
	
}
