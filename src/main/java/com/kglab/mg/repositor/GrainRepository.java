package com.kglab.mg.repositor;



import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kglab.mg.entity.po.Grain;

@Repository
public interface GrainRepository extends Neo4jRepository<Grain, Long>{
	
	@Query("merge (g:Grain:label{{boundLegth:{boundLegth},grainID:{grainID}}}) return g")
	public Grain mergeBoundLegthById(@Param("label")String label,@Param("grainID")int grainID,@Param("boundLegth")int boundLegth);
}
 