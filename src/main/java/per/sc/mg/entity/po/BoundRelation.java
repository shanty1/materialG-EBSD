package per.sc.mg.entity.po;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.StartNode;


public abstract class BoundRelation {
	@Id
	@GeneratedValue
	private Long id;

	//开始节点
	@StartNode
	private Grain startNode;

	//结束节点
	@EndNode
	private Grain endNode;
	
	@Property
	private float disorientation;

	@Property
	private int boundLength;
}
