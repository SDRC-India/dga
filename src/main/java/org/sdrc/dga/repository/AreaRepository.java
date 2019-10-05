package org.sdrc.dga.repository;

import java.util.List;

import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.AreaLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AreaRepository {
	
	List<Area> findByAreaLevelAreaLevelId(Integer areaLevelId);
   
	List<Integer> findAreaIdByParentAreaId(Integer paremtAreaID);
	
	Area findByAreaId(Integer areaId);

	List<Area> findAll();

	Area findByAreaCode(String areaCode);

	List<Area> findTopOneByParentAreaIdOrderByAreaCodeDesc(Integer parentAreaId);

	@Transactional
	Area save(Area area);

	List<Area> findByAreaLevelAreaLevelIdIn(List<Integer> areaLevelIds);

	List<Area> findByAreaLevelAreaLevelIdOrderByAreaNameAsc(int i);
	
	List<Area> findByAreaLevelAreaLevelIdAndAreaIdNotOrderByAreaNameAsc(int i,int id);
	
	List<Area> findByParentAreaIdAndAreaLevelAreaLevelIdOrderByAreaNameAsc(int  parentAreaId,int i);
	
	List<Area> findByParentAreaIdIn(List<Integer> blocks);
	
	//List<Area> findByParentAreaIdInAndAreaCodeLike(List params);
	
	
	@Query("SELECT area FROM Area area WHERE area.areaLevel In (7,8,9,10) And area.isLive IS true And area.areaCode LIKE (select areaCode from Area where areaId =:stateId)+ '%' ")
	public List<Area> findAreaDataModelWithDistrict(@Param("stateId")Integer stateId);
	
	List<Area> findByParentAreaId(Integer paremtAreaID);
	
	@Query("SELECT area FROM Area area WHERE area.areaLevel = 4 And area.isLive IS true And area.parentAreaId =:stateId")
	public List<Area> findDistrictWithStateId(@Param("stateId")Integer stateId);
	
	List<Area> findByAreaIdIn(List<Integer> ids);
	
	
	Area findByParentAreaIdAndAreaLevelAreaLevelId(int parentId,int areaLevelId);
	

	List<Area> findAllByIsLiveTrue();
	
}
