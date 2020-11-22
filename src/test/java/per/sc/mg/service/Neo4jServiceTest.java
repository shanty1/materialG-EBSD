package per.sc.mg.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class Neo4jServiceTest {
	
	String grainfile = "F:\\GithubRepository\\materialG-EDSB\\Grain-File\\ATEX_OUT_InfosGrains.xlsx";	
	String relationfile = "F:\\GithubRepository\\materialG-EDSB\\Grain-File\\ATEX_OUT_Neighbors.xlsx";	
	String grainPixelsFile="F:\\GithubRepository\\materialG-EDSB\\Grain-File\\ATEX_OUT_InfosGrainsPixels.xlsx";
	
	@Autowired Neo4jService neo4jService;
	@Test
	public void test() {
		
//		neo4jService.buildGrainNode(grainfile,"Sam001");
//		neo4jService.buildGrainRelation(relationfile, "Sam001");
		neo4jService.buildGrainGraph(grainfile, grainPixelsFile, relationfile, 9, "Sample001");
	}
 
	public String sds(String a ,String d) {
		// TODO Auto-generated method stub
		  
return null;
	}

}
  