package com.kglab.mg.repositor;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kglab.mg.entity.po.BoundRelation;
import com.kglab.mg.entity.po.StrongBoundRelation;
import com.kglab.mg.entity.po.WeakBoundRelation;

@Repository
public interface BoundRelationRepository extends Neo4jRepository<BoundRelation, Long> {
	@Query("match p=(g1:Grain)<-[r]->(g2:Grain) where g1.grainID={grainID1} and g2.grainID={grainID2} return p")
	Iterable<BoundRelation> findBoundRelationByGrainID(@Param("grainID1")String grainID1, @Param("grainID2")String grainID2);

	@Query ("match (g1:Grain),(g2:Grain) where g1.grainID={grainID1} and g2.grainID={grainID2}  create p=(g1)-[r: StrongBoundRelation{ boundLength:{boundLength},disorientation:{disorientation}}]->(g2) return p")
	Iterable<StrongBoundRelation> addStrongBoundRelation(@Param("grainID1")int grainID1, @Param("grainID2")int grainID2, @Param("boundLength")int boundLength,@Param("disorientation")float disorientation);
	
	@Query ("match (g1:Grain),(g2:Grain) where g1.grainID={grainID1} and g2.grainID={grainID2}  create p=(g1)-[r: WeakBoundRelation{ boundLength:{boundLength},disorientation:{disorientation}}]->(g2) return p")
	Iterable<WeakBoundRelation> addWeakBoundRelation(@Param("grainID1")int grainID1, @Param("grainID2")int grainID2, @Param("boundLength")int boundLength,@Param("disorientation")float disorientation);
	
	
}

//