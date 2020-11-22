package per.sc.mg.entity.po;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import per.sc.mg.entity.po.Grain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RelationshipEntity(type = "WeakBoundRelation")
public class WeakBoundRelation {

	
}
