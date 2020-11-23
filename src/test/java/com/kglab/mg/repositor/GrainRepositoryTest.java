package com.kglab.mg.repositor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kglab.mg.entity.po.Grain;
import com.kglab.mg.repositor.GrainRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrainRepositoryTest {
	
	@Autowired GrainRepository grainRepository;
	
//	@Test
	public void test() {
		System.out.println(grainRepository.findAll());
	}
	
	@Test
	public void testMergePropertiesById() {
		grainRepository.mergeBoundLegthById("sam001", 14, 1);
	}
}
