package com.jxau.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.plugins.Page;
import com.jxau.pojo.Employee;
import com.jxau.pojo.History;
import com.jxau.service.EmployeeService;
import com.jxau.service.HistoryService;
import com.jxau.util.MTimeUtil;

@Controller
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private HistoryService historyService;
	@Autowired
	private EmployeeService employeeService;
	
	/*查看离休员工管理*/
	@RequestMapping("/retireListPage.do")
	public String selectRetireByPage(Model model, int pageNo){
		Page<History> page = historyService.selectRetireByPage(pageNo);
		model.addAttribute("page", page);
		return "admin/retire_list";
	}
	
	/*查看人员详细信息（包括离休和在职）*/
	@RequestMapping("/{id}/detail.do")
	public String selectHistory(@PathVariable Integer id, Model model){
		History history = historyService.selectHistory(id);
		model.addAttribute("history", history);
		return "admin/history_detail";
	}
	
	/*跳转“去”修改人员信息（还未修改信息）*/
	@RequestMapping("/{id}/toUpdate.do")
	public String toUpdate(Model model, @PathVariable Integer id){
		History history = historyService.selectHistory(id);
		if (history.getStatus().equals("在职")) {
			Employee employee = employeeService.selectByNumber(history.getEmployeeNumber());
			return "forward:/employee/"+ employee.getId() +"/toUpdate.do";
		}else{
			model.addAttribute("history", history);
			return "admin/history_update";
		}
	}
	
	@RequestMapping("/{id}/updateRetire.do")
	public String updateRetire(@PathVariable Integer id, History history, String date){
		history.setId(id);
		history.setBirthday(MTimeUtil.stringParse(date));
		historyService.updateById(history);
		return "forward:/history/retireListPage.do?pageNo=1";
	}
	
	
	@RequestMapping("/listPage.do")
	public String selectListByPage(Model model, int pageNo){
		Page<History> page = historyService.selectLisByPage(pageNo);
		model.addAttribute("page", page);
		return "admin/history_list";
	}
	
	/*插入修改离休人员信息（已修改信息）*/
	@RequestMapping("/{id}/update.do")
	public String updateById(@PathVariable Integer id, History history, String date){
		history.setId(id);
		history.setBirthday(MTimeUtil.stringParse(date));
		historyService.updateById(history);
		return "redirect:/history/retireListPage.do?pageNo=1";
	}
	
	/*所有员工档案管理（包括在职员工和离休员工）*/
	@RequestMapping("/list.do")
	public String list(Model model){
		List<History> hList = historyService.selectList();
		model.addAttribute("hList", hList);
		return "admin/history_list";
	}
}
