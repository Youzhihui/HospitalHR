package com.jxau.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jxau.pojo.Move;
import com.jxau.service.MoveService;

@Controller
@RequestMapping("/move")
public class MoveController {

	@Autowired
	private MoveService moveService;
	
	/*员工调动记录*/
	@RequestMapping("/list.do")
	public String selectList(Model model){
		List<Move> list = moveService.selectList();
		model.addAttribute("mList",list);
		return "admin/move_list";
	}
	
}
