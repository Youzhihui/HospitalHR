package com.jxau.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.jxau.mapper.PositionMapper;
import com.jxau.pojo.Position;
import com.jxau.service.PositionService;

@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position>
	implements PositionService{

	@Override
	public Position selectByNumber(Integer positionNumber) {
		return baseMapper.selectByNumber(positionNumber);
	}

	@Override
	public Page<Position> selectListByPage(int pageNo) {
		Page<Position> page = new Page<Position>(pageNo, 10, "id");
		page.setAsc(false);
		page.setRecords(baseMapper.selectPage(page, null));
		return page;
	}

}
