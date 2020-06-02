package com.jxau.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.jxau.pojo.Move;

public interface MoveService extends IService<Move>{

	/**
	 * 查询所有的调动记录
	 * @return
	 */
	List<Move> selectList();
}
