package com.jxau.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jxau.pojo.Department;
import com.jxau.pojo.Employee;
import com.jxau.pojo.History;
import com.jxau.pojo.Position;
import com.jxau.service.DepartmentService;
import com.jxau.service.EmployeeService;
import com.jxau.service.HistoryService;
import com.jxau.service.PositionService;
import com.jxau.util.MTimeUtil;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	/*以下全局变量不会存在线程不安全的问题*/
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private HistoryService historyService;
	
	@RequestMapping("/login.do")
	public String toLogin(){
		return "login";
	}
	
	/*登录验证控制*/
	@RequestMapping("/checkLogin.do")
	public String checkLogin(HttpSession session, Employee employee){
		Employee employee2 = employeeService.checkLogin(employee.getEmployeeNumber(),
				employee.getPassword());
		if (employee2 != null) {
			session.setAttribute("loged", employee2);
			String level = employee2.getPosition().getLevel();
			if (level.equals("人事部主任")) {
				return "admin/index1";
			}else if (level.equals("人事部员工")) {
				return "admin/index2";
			}else if (level.equals("部门主任")) {
				return "admin/index3";
			}else {
				return "admin/index4";
			}
		}else{
			return "login";
		}
	}
	
	@RequestMapping("/welcome.do")
	public String toWelcome(){
		return "welcome";
	}
	
	/*人事部门人员对所有在职员工管理控制*/
	@RequestMapping("/listPage.do")
	public String selectList(Model model, int pageNo){
		Page<Employee> page = employeeService.selectListByPage(pageNo);
		model.addAttribute("page", page);
		return "admin/employee_list";
	}
	
	/*人事部门人员对所有在职员工查看详细信息控制*/
	@RequestMapping("/{id}/detial.do")
	public String selectEmployee(@PathVariable Integer id, Model model){
		Employee employee = employeeService.selectEmployee(id);
		model.addAttribute("employee", employee);
		return "admin/employee_detail";
	}
	
	/*人事部门人员对新入职员工“去”添加信息页面控制（暂时还未填好信息页面）*/
	@RequestMapping("/toAdd.do")
	public String toAdd(Model model){
		List<History> eList = historyService.selectList(new EntityWrapper<History>()
				.orderBy("employee_number", false));
		model.addAttribute("employeeNumber",eList.get(0).getEmployeeNumber()+1);
		List<Department> dList = departmentService.selectList(new EntityWrapper<Department>());
		model.addAttribute("dList", dList);
		List<Position> pList = positionService.selectList(new EntityWrapper<Position>());
		model.addAttribute("pList", pList);
		return "admin/employee_add";
	}
	
	/*人事部门人员对新入职员工“已”添加信息控制（已经调好信息页面）*/
	@RequestMapping("/add.do")
	public String add(Employee employee, String date) {
		employee.setBirthday(MTimeUtil.stringParse(date));
		employeeService.addEmployee(employee);
		return "forward:/employee/listPage.do?pageNo=1";
	}
	
	/*人事部门人员对在职员工“去”修改信息控制（还未修改信息页面）*/
	@RequestMapping("/{id}/toUpdate.do")
	public String toUpdate(Model model, @PathVariable Integer id){
		Employee employee = employeeService.selectById(id);
		model.addAttribute("employee", employee);
		List<Department> dList = departmentService.selectList(new EntityWrapper<Department>());
		model.addAttribute("dList", dList);
		List<Position> pList = positionService.selectList(new EntityWrapper<Position>());
		model.addAttribute("pList", pList);
		return "admin/employee_update";
	}
	
	/*人事部门人员对在职员工“已”修改信息控制（已修改信息页面）*/
	@RequestMapping("/{id}/update.do")
	public String updateById(@PathVariable Integer id, Employee employee, String date, String status, 
			HttpSession session){
		employee.setId(id);
		employee.setBirthday(MTimeUtil.stringParse(date));
		//得到操作人员的名字
		Employee employee2 = (Employee) session.getAttribute("loged");
		if(employee.getDepartmentNumber() == null){
			employee.setDepartment(employee2.getDepartment());
			employee.setDepartmentNumber(employee2.getDepartmentNumber());
		}
		if(employee.getPositionNumber() == null){
			employee.setPosition(employee2.getPosition());
			employee.setPositionNumber(employee2.getPositionNumber());
		}
		if(status == null){
			status = "在职";
		}
		employeeService.updateEmployee(employee, status, employee2.getName());
		return "forward:/employee/listPage.do?pageNo=1";
	}
	
	/*人事部门人员删除在职人员表（该人员已离职或退休）信息控制（已修改信息页面）*/
	@RequestMapping("/{id}/delete.do")
	public String deleteById(@PathVariable Integer id){
		employeeService.deleteEmployee(id);
		return "forward:/employee/listPage.do?pageNo=1";
	}
	
	/*查看自己个人信息*/
	@RequestMapping("/oneself/{id}/detial.do")
	public String selectEmployee2(@PathVariable Integer id, Model model){
		Employee employee = employeeService.selectEmployee(id);
		model.addAttribute("employee", employee);
		return "admin/oneself_detail";
	}
	
	/*修改自己个人信息*/
	@RequestMapping("/oneself/{id}/toUpdate.do")
	public String toUpdate2(Model model, @PathVariable Integer id){
		Employee employee = employeeService.selectById(id);
		model.addAttribute("employee", employee);
		return "admin/oneself_update";
	}
	
	/*通过员工姓名搜索员工信息*/
	@RequestMapping("/search")
	public String search(Model model, String input, int pageNo){
		Page<Employee> page = employeeService.search(input, pageNo);
		model.addAttribute("page", page);
		return "admin/search_result";
	}
	
	/*转发到登录*/
	@RequestMapping("/logout.do")
	public String logout(HttpSession session){
		session.removeAttribute("loged");
		return "login";
	}
		
}
