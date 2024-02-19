package com.youblog.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.youblog.entities.ClassDetailsEntity;

@Repository
public interface ClassDetailsRepository extends JpaRepository<ClassDetailsEntity, Long> {

	@Query(value = "select case when count(*)>0 then false else true end from class_details cd where cd.time_details_id = :timeDetailsId \r\n"
			+ "and cd.trainer_id = :trainerId and cd.week_day = :weekDay and cd.active_flag = true",nativeQuery = true)
	public Boolean checkTiming(Long trainerId, Long timeDetailsId, String weekDay);

	@Query(value = "select count(*) from class_details cd where \r\n"
			+ "cd.trainer_id = :trainerId and cd.week_day = :weekDay and cd.active_flag = true",nativeQuery = true)
	public int checkCount(Long trainerId, String weekDay);

	@Query(value = "select case when cl.list ->>'classList' is null then cast(json_build_object('classList',cast(array[] as numeric[])) as varchar) else cast(cl.list as varchar) end from\r\n"
			+ "(select json_build_object('classList',json_agg(json_build_object('weekDay',li.week_day,'classCount',json_array_length(li.list_data),'classes',li.list_data)))list from (\r\n"
			+ "select cd.week_day,json_agg(json_build_object('classDetailsId',cd.class_details_id,'classMasterId',cd.class_master_id,'className',cm.class_name,'startDate',to_char(cd.start_date,'dd Mon yy'),\r\n"
			+ "'endDate',to_char(cd.end_date,'dd Mon yy'),'timeDetailsId',cd.time_details_id,'timings',\r\n"
			+ "case when timings.timings is null then 'N/A' else timings.timings end,\r\n"
			+ "'trainerDetails',users.user_data,'tempCancelFlag',cd.temp_cancel_flag,'tempChangeFlag',cd.temp_change_flag,\r\n"
			+ "'tempTimeId',case when cd.temp_time_id is null then 0 else cd.temp_time_id end,\r\n"
			+ "'tempTimings',case when temp_timings.temp_timings is null then 'N/A' else temp_timings.temp_timings end) order by cd.class_details_id asc) list_data\r\n"
			+ "from class_details cd\r\n"
			+ "inner join class_master cm on cm.class_master_id = cd.class_master_id and cm.active_flag=true\r\n"
			+ "inner join (select td.time_details_id,td.active_flag,concat(case when td.start_time<12 then concat(td.start_time,' AM') else \r\n"
			+ "case when td.start_time=12 then concat(td.start_time,' PM') else concat(td.start_time-12,' PM') end end,' - ',\r\n"
			+ "case when td.end_time<12 then concat(td.end_time,' AM') else case when td.end_time=12 then concat(td.end_time,' PM') else concat(td.end_time-12,' PM') end end)timings from time_details td)timings\r\n"
			+ "on timings.time_details_id = cd.time_details_id and timings.active_flag = true\r\n"
			+ "left join  (select td.time_details_id,td.active_flag,concat(case when td.start_time<12 then concat(td.start_time,' AM') else\r\n"
			+ "case when td.start_time=12 then concat(td.start_time,' PM') else concat(td.start_time-12,' PM') end end,' - ',\r\n"
			+ "case when td.end_time<12 then concat(td.end_time,' AM') else case when td.end_time=12 then concat(td.end_time,' PM') else concat(td.end_time-12,' PM') end end)temp_timings from time_details td)temp_timings\r\n"
			+ "on temp_timings.time_details_id = cd.temp_time_id and temp_timings.active_flag = true\r\n"
			+ "inner join (select ud.user_id,json_build_object('trainerId',ud.user_id,'trainerName',concat(ud.first_name,' ',ud.last_name),'rating',\r\n"
			+ "case when ROUND(cast(AVG(FD.RATING) as numeric),2) is null then 0 else ROUND(cast(AVG(FD.RATING) as numeric),2) end) as user_data from user_details ud\r\n"
			+ "left join feedback_details as fd on ud.user_id = fd.trainer_user_id where ud.active_flag = true group by ud.user_id)users on users.user_id = cd.trainer_id\r\n"
			+ "where cd.gym_id = :gymId and :currentDate between cd.start_date and cd.end_date group by cd.week_day)li)cl",nativeQuery = true)
	public String classDetailsList(Long gymId, Date currentDate);

	@Query(value = "select case when cl.list ->>'classListTrainer' is null then cast(json_build_object('classListTrainer',cast(array[] as numeric[])) as varchar) else cast(cl.list as varchar) end from\r\n"
			+ "(select json_build_object('classListTrainer',json_agg(json_build_object('weekDay',li.week_day,'classCount',json_array_length(li.list_data),'classes',li.list_data)))list from (\r\n"
			+ "select cd.week_day,json_agg(json_build_object('classDetailsId',cd.class_details_id,'classMasterId',cd.class_master_id,'className',cm.class_name,'startDate',to_char(cd.start_date,'dd Mon yy'),\r\n"
			+ "'endDate',to_char(cd.end_date,'dd Mon yy'),'timeDetailsId',cd.time_details_id,'timings',\r\n"
			+ "case when timings.timings is null then 'N/A' else timings.timings end,\r\n"
			+ "'trainerDetails',users.user_data,'tempCancelFlag',cd.temp_cancel_flag,'tempChangeFlag',cd.temp_change_flag,\r\n"
			+ "'tempTimeId',case when cd.temp_time_id is null then 0 else cd.temp_time_id end,\r\n"
			+ "'tempTimings',case when temp_timings.temp_timings is null then 'N/A' else temp_timings.temp_timings end) order by cd.class_details_id desc) list_data\r\n"
			+ "from class_details cd\r\n"
			+ "inner join class_master cm on cm.class_master_id = cd.class_master_id and cm.active_flag=true\r\n"
			+ "inner join (select td.time_details_id,td.active_flag,concat(case when td.start_time<12 then concat(td.start_time,' AM') else \r\n"
			+ "case when td.start_time=12 then concat(td.start_time,' PM') else concat(td.start_time-12,' PM') end end,' - ',\r\n"
			+ "case when td.end_time<12 then concat(td.end_time,' AM') else case when td.end_time=12 then concat(td.end_time,' PM') else concat(td.end_time-12,' PM') end end)timings from time_details td)timings\r\n"
			+ "on timings.time_details_id = cd.time_details_id and timings.active_flag = true\r\n"
			+ "left join  (select td.time_details_id,td.active_flag,concat(case when td.start_time<12 then concat(td.start_time,' AM') else\r\n"
			+ "case when td.start_time=12 then concat(td.start_time,' PM') else concat(td.start_time-12,' PM') end end,' - ',\r\n"
			+ "case when td.end_time<12 then concat(td.end_time,' AM') else case when td.end_time=12 then concat(td.end_time,' PM') else concat(td.end_time-12,' PM') end end)temp_timings from time_details td)temp_timings\r\n"
			+ "on temp_timings.time_details_id = cd.temp_time_id and temp_timings.active_flag = true\r\n"
			+ "inner join (select ud.user_id,json_build_object('trainerId',ud.user_id,'trainerName',concat(ud.first_name,' ',ud.last_name),'rating',\r\n"
			+ "case when ROUND(cast(AVG(FD.RATING) as numeric),2) is null then 0 else ROUND(cast(AVG(FD.RATING) as numeric),2) end) as user_data from user_details ud\r\n"
			+ "left join feedback_details as fd on ud.user_id = fd.trainer_user_id where ud.active_flag = true group by ud.user_id)users on users.user_id = cd.trainer_id\r\n"
			+ "where cd.trainer_id = :trainerId and :currentDate between cd.start_date and cd.end_date group by cd.week_day)li)cl",nativeQuery = true)
	public String classDetailsListTrainer(Long trainerId, Date currentDate); 

	}
