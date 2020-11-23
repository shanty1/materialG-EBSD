package com.kglab.mg.entity.po;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import lombok.Builder;
import lombok.Data;

/**
 * 
 * @author sc
 *
 */
@Data
@Builder 
@NodeEntity()
public class Grain {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Labels
	private Set<String> labels ;
	
	@Property(name = "grainID")
	private int grainID;
	
	@Property(name = "phi1")
	private float phi1;
	
	@Property(name = "phi")
	private float phi;
	
	@Property(name = "phi2")
	private float phi2;
	
	@Property(name = "size")
	private float size;
	
	@Property(name = "surface")
	private float surface;
	
	@Property(name = "boundLength")
	private int boundLength;
	
	@Property(name = "ellipticity")
	private Float ellipticity;
	
	@Property(name = "angle")
	private Float angle;
	
	@Property(name = "phase")
	private int phase;
	
	@Property(name="xAve")
	private float xAve;
	
	@Property(name="yAve")
	private float yAve;
	
}
