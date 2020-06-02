package com.jxau.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jxau.pojo.Position;
import com.jxau.service.PositionService;

@Controller
@RequestMapping("/position")
public class PositionController {
	
	@Autowired
	private PositionService positionService;
	
	/*所有职称信息管理*/
	@RequestMapping("/listPage.do")
	public String selecListByPage(int pageNo, Model model){
		Page<Position> page = positionService.selectListByPage(pageNo);
		model.addAttribute("page", page);
 		return "admin/position_list";
	}
	
	/*跳转“去”添加新职位信息（还未填写添加新职位信息）*/
	@RequestMapping("/toAdd.do")
	public String toAdd(Model model){
		List<Position> dList = positionService.selectList(new EntityWrapper<Position>()
				.orderBy("position_number", false));
		model.addAttribute("positionNumber", dList.get(0).getPositionNumber()+1);
		return "admin/position_add";
	}
	
	/*插入新职位信息入数据库（已填写完）*/
	@RequestMapping("/add.do")
	public String add(Position position){
		positionService.insert(position);
		return "forward:/position/listPage.do?pageNo=1";
	}
	
	/*跳转“去”修改职位信息（还未修改职位信息）*/
	@RequestMapping("/{id}/toUpdate.do")
	public String toUpdate(@PathVariable Integer id, Model model){
		Position position = positionService.selectById(id);
		model.addAttribute("position", position);
		return "admin/position_update";
	}
	
	/*插入已修改信息入数据库（已修改完）*/
	@RequestMapping("/{id}/update.do")
	public String updateById(@PathVariable Integer id, Position position){
		position.setId(id);
		positionService.updateById(position);
		return "forward:/position/listPage.do?pageNo=1";
	}
	
	/*删除职位信息*/
	@RequestMapping("/{id}/delete.do")
	public String deleteById(@PathVariable Integer id){
		positionService.deleteById(id);
		return "forward:/position/listPage.do?pageNo=1";
	}
}
