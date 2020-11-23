package com.kglab.mg.entity.po;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.kglab.mg.entity.po.Grain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RelationshipEntity(type = "StrongBoundRelation")
public class StrongBoundRelation extends BoundRelation{

	
}
