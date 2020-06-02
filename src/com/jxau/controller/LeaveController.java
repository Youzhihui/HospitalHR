package com.jxau.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.plugins.Page;
import com.jxau.pojo.Employee;
import com.jxau.pojo.Leave;
import com.jxau.service.LeaveService;
import com.jxau.util.MTimeUtil;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	private LeaveService leaveService;
	
	/*所有员工请假记录*/
	@RequestMapping("/list.do")
	public String selectList(Model model){
		List<Leave> list = leaveService.selectList();
		model.addAttribute("list", list);
		return "admin/leave_list";
	}
	
	/*查看员工还未批准请假的详细信息*/
	@RequestMapping("/{id}/detail.do")
	public String selectLeave(@PathVariable Integer id, Model model){
		Leave leave = leaveService.selectLeave(id);
		model.addAttribute("leave", leave);
		return "admin/leave_detail";
	}
	
	/*批准员工还未批准请假记录*/
	@RequestMapping("/{id}/update.do")
	public String updateStatus(@PathVariable Integer id){
		leaveService.updateStatus(id);
		return "forward:/leave/notlist.do";
	}
	
	/*跳转“去”填写申请请假信息（还未填写）*/
	@RequestMapping("/toAdd.do")
	public String toAdd(){
		return "admin/leave_add";
	}
	
	/*添加申请请假信息（已填写完）*/
	@RequestMapping("/add.do")
	public String add(HttpSession session,Integer employeeNumber, Leave leave, String start, String end){
		leave.setEmployeeNumber(employeeNumber);
		leave.setStartTime(MTimeUtil.stringParse(start));
		leave.setEndTime(MTimeUtil.stringParse(end));
		Employee employee = (Employee)session.getAttribute("loged");
		leave.setDepartmentNumber(employee.getDepartmentNumber());
		leaveService.insert(leave);
		return "forward:/employee/welcome.do";//重定向
	}
	
	/*查看自己个人请假信息*/
	@RequestMapping("/oneself.do")
	public String seletByEmployee(HttpSession session, int pageNo, Model model){
		Employee employee = (Employee)session.getAttribute("loged");
		Page<Leave> page = leaveService.seletByEmployee(employee.getEmployeeNumber(), pageNo);
		model.addAttribute("page", page);
		return "admin/oneself_leave";
	}
	
	/*未批准列表记录*/
	@RequestMapping("/notlist.do")
	public String selectNotList(Model model, HttpSession session){
		//获取登录用户的信息
		Employee employee = (Employee) session.getAttribute("loged");
		List<Leave> list = leaveService.selectListByStatus(employee.getDepartmentNumber(), "未批准");
		model.addAttribute("list", list);
		return "admin/leave_notlist";
	}
	
	/*已批准列表记录*/
	@RequestMapping("/yeslist.do")
	public String selectYesList(Model model, HttpSession session){
		//获取登录用户的信息
		Employee employee = (Employee) session.getAttribute("loged");
		List<Leave> list = leaveService.selectListByStatus(employee.getDepartmentNumber(), "已批准");
		model.addAttribute("list", list);
		return "admin/leave_yeslist";
	}
}
