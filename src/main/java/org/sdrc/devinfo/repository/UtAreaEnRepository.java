package org.sdrc.devinfo.repository;

import java.util.List;

import org.sdrc.devinfo.domain.UtAreaEn;
/**
 * @author Harsh
 * @since version 1.0.0.0
 *
 */
public interface UtAreaEnRepository 
{
List<UtAreaEn> getAllAreaByLevel(Integer level);

List<UtAreaEn> findByArea_Parent_NId(int parentId);

List<UtAreaEn> findAll();
}
