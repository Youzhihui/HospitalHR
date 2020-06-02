package com.jxau.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jxau.pojo.Attendance;
import com.jxau.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;
	
	/*上班前签到*/
	@RequestMapping("/addStart.do")
	public String addStart(Integer employeeNumber){
		attendanceService.addStart(employeeNumber);
		return "welcome";
	}
	
	/*下班班前签到*/
	@RequestMapping("/addEnd.do")
	public String addEnd(Integer employeeNumber){
		attendanceService.addEnd(employeeNumber);
		return "welcome";
	}
	
	/*所以员工考勤信息*/
	@RequestMapping("/list.do")
	public String selectList(Model model){
		List<Attendance> list = attendanceService.selectList();
		model.addAttribute("aList",list);
		return "admin/attendance_list";
	}
	
	/*查看自己个人考勤记录*/
	@RequestMapping("/{employeeNumber}/oneself.do")
	public String select(Model model, @PathVariable Integer employeeNumber){
		List<Attendance> list = attendanceService.selectByEmployee(employeeNumber);
		model.addAttribute("aList",list);
		return "admin/oneself_attendance";
	}
}
