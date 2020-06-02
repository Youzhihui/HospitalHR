package com.jxau.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jxau.mapper.DepartmentMapper;
import com.jxau.mapper.HistoryMapper;
import com.jxau.mapper.PositionMapper;
import com.jxau.pojo.Department;
import com.jxau.pojo.History;
import com.jxau.pojo.Position;
import com.jxau.service.HistoryService;

@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History>
	implements HistoryService{
	
	@Autowired
	private DepartmentMapper departmentMapper;
	@Autowired
	private PositionMapper positionMapper;
	
	/**
	 * history对象setDepartment setPosition
	 * @param history
	 * @return
	 */
	public History setObject(History history){
		Integer departmentNumber = history.getDepartmentNumber();
		if (departmentNumber != null) {
			Department  department = departmentMapper.selectByNumber(departmentNumber);
			history.setDepartment(department);
		}else{
			history.setDepartment(null);
		}
		Integer positionNumber = history.getPositionNumber();
		if (positionNumber != null) {
			Position position = positionMapper.selectByNumber(positionNumber);
			history.setPosition(position);
		}else{
			history.setPosition(null);
		}
		return history;
	}

	@Override
	public Page<History> selectRetireByPage(int pageNo) {
		Page<History> page = new Page<History>(pageNo, 10, "id");
		//是否为升序 ASC（ 默认： true ）
		page.setAsc(false);
		List<History> hList = baseMapper.selectRetireByPage(page);
		for(History h : hList){
			setObject(h);
		}
		page.setRecords(hList);
		return page;
	}

	@Override
	public History selectHistory(Integer id) {
		History history = baseMapper.selectById(id);
		setObject(history);
		return history;
	}
	
	@Override
	public Page<History> selectLisByPage(int pageNo) {
		Page<History> page = new Page<History>(pageNo, 10);
		//是否为升序 ASC（ 默认： true ）
		page.setAsc(false);
		List<History> hList = baseMapper.selectPage(page, null);
		for(History h : hList){
			setObject(h);
		}
		page.setRecords(hList);
		return page;
	}

	@Override
	public History selectByNumber(Integer employeeNumber) {
		return baseMapper.selectByNumber(employeeNumber);
	}

	@Override
	public List<History> selectList() {
		List<History> hList = baseMapper.selectList(new EntityWrapper<History>());
		for(History h : hList){
			setObject(h);
		}
		return hList;
	}
}
