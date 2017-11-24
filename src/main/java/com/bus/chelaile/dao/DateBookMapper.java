package com.bus.chelaile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bus.chelaile.model.DateInfo;

public interface DateBookMapper {
	
	List<DateInfo> listValidDates(@Param("date_before") String dateBefore, @Param("date_after") String dateAfter);

}
