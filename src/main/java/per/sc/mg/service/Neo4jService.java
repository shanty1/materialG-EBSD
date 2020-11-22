package per.sc.mg.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import per.sc.mg.entity.po.BoundRelation;
import per.sc.mg.entity.po.Grain;
import per.sc.mg.repositor.BoundRelationRepository;
import per.sc.mg.repositor.GrainRepository;
import per.sc.tool.util.parser.ExcelReaderUtil;

@Service
@EnableTransactionManagement
public class Neo4jService {

	
	@Autowired private GrainRepository grainRepository;
	@Autowired private BoundRelationRepository boundRelationRepository;
	
	/**
	 * 
	 * 
	 * @param ATEX_OUT_InfosGrains_File
	 * @param ATEX_OUT_InfosGrainsPixels
	 * @param ATEX_OUT_Neighbors_File 
	 * @author sc
	 * @date 2020年11月11日
	 */
	public void buildGrainGraph(String ATEX_OUT_InfosGrains_File,String ATEX_OUT_InfosGrainsPixels, String ATEX_OUT_Neighbors_File,int divideNum, String... labelsStr) {
		buildGrainNode(ATEX_OUT_InfosGrains_File, ATEX_OUT_InfosGrainsPixels, ATEX_OUT_Neighbors_File,divideNum, labelsStr);
		buildGrainRelation(ATEX_OUT_Neighbors_File, labelsStr);
	}
	
	/**
	 * 构建晶粒图---1.构建晶粒结点
	 * 
	 * @param ATEX_OUT_InfosGrains_File
	 * @param ATEX_OUT_InfosGrainsPixels
	 * @param ATEX_OUT_Neighbors_File
	 * @param divideNum
	 * @param labelsStr 
	 * @author sc
	 * @date 2020年11月11日
	 */
	public void buildGrainNode(String ATEX_OUT_InfosGrains_File,String ATEX_OUT_InfosGrainsPixels, String ATEX_OUT_Neighbors_File,int divideNum, String... labelsStr) {
		List<List<String>> lists = ExcelReaderUtil.readExcel(ATEX_OUT_InfosGrains_File);
		List<String> title = lists.get(0);
		List<Grain> grains = new ArrayList<Grain>();
		Map<Integer,Integer> grainLengthMap = readGrainLength(ExcelReaderUtil.readExcel(ATEX_OUT_Neighbors_File));
		Map<Integer,Float[]> grainAxisMap = readGrainXY(ATEX_OUT_InfosGrainsPixels);
		for(int i=1; i<lists.size(); i++) {
			List<String> item = lists.get(i);
			int gID = Integer.parseInt(item.get(title.indexOf("#ID")));
			String grainDivideLabel = "No." + computeGrainGrid(divideNum, grainAxisMap.get(gID), grainAxisMap.get(-1));
			Set<String> labelist = new HashSet<String>();
			labelist.addAll(Arrays.asList(labelsStr));labelist.add(grainDivideLabel);
			Grain grainNode = Grain.builder()
					.labels(labelist)
					.grainID(gID)
					.phi1(Float.parseFloat(item.get(title.indexOf("Phi1"))))
					.phi(Float.parseFloat(item.get(title.indexOf("Phi"))))
					.phi2(Float.parseFloat(item.get(title.indexOf("Phi2"))))
					.size(Float.parseFloat(item.get(title.indexOf("Size"))))
					.surface(Float.parseFloat(item.get(title.indexOf("Surface"))))
					.boundLength(grainLengthMap.get(gID))
					.ellipticity(Float.parseFloat(item.get(title.indexOf("Ellip"))))
					.angle(Float.parseFloat(item.get(title.indexOf("EllAng"))))
					.xAve(Float.parseFloat(item.get(title.indexOf("Xg"))))
					.yAve(Float.parseFloat(item.get(title.indexOf("Yg"))))
					.build();
			grains.add(grainNode);
		}
		grainRepository.saveAll(grains);
	}
	
	/**
	 * 构建晶粒图---21.构建晶粒关系
	 * 
	 * @param ATEX_OUT_Neighbors_File
	 * @param labelsStr 
	 * @author sc
	 * @date 2020年11月11日
	 */
	public void buildGrainRelation(String ATEX_OUT_Neighbors_File,String... labelsStr) {
		List<List<String>> lists = ExcelReaderUtil.readExcel(ATEX_OUT_Neighbors_File);
		Map<Integer,Integer> grainLengthMap = readGrainLength(lists);
		List<String> title = lists.get(1);
		for(int i=2; i<lists.size(); i++) {
			List<String> item = lists.get(i);
			int grain1 = Integer.parseInt(item.get(title.indexOf("IDgrain")));
			int grain2 = Integer.parseInt(item.get(title.indexOf("IDneighbor")));
			int boundLengthForGrain1 = Integer.parseInt(item.get(title.indexOf("BoundLengthTOT"))); 
			int boundLengthForGrain2 = grainLengthMap.get(grain2);
			int boundLengthForCommon = Integer.parseInt(item.get(title.indexOf("BoundLength"))); 
			float disorientation = Float.parseFloat(item.get(title.indexOf("Disorientation")));
			if(boundLengthForGrain1>boundLengthForGrain2) {
				boundRelationRepository.addWeakBoundRelation(grain1, grain2, boundLengthForCommon, disorientation);
			}else {
				boundRelationRepository.addStrongBoundRelation(grain1,grain2,boundLengthForCommon,disorientation);
			}
			System.out.println("".format("%d,%d,%d,%f", grain1, grain2, boundLengthForCommon, disorientation));
		}
	}
	
	/**
	 * 获取晶粒的边界长度(ATEX_OUT_InfosGrains_Filede的Xg,Yg即为晶粒中心点)
	 * 
	 * @param lists ATEX_OUT_Neighbors_File文件列表
	 * @return 
	 * @author sc
	 * @date 2020年11月11日
	 */
	@Deprecated
	private Map<Integer,Integer> readGrainLength(List<List<String>> lists) {
		List<String> title = lists.get(1);
		Map<Integer,Integer> grainLengthMap = new HashMap<Integer, Integer>();
		for(int i=2; i<lists.size(); i++) {
			List<String> item = lists.get(i);
			int grainID = Integer.parseInt(item.get(title.indexOf("IDgrain")));
			int boundLengthForGrain = Integer.parseInt(item.get(title.indexOf("BoundLengthTOT"))); 
			grainLengthMap.put(grainID, boundLengthForGrain);
		}
		return grainLengthMap;
	}
	
	/**
	 * 获取晶粒的平均x，y坐标
	 * 
	 * @param ATEX_OUT_Neighbors_File
	 * @return 
	 * @author sc
	 * @date 2020年11月11日
	 */
	private Map<Integer,Float[]> readGrainXY(String ATEX_OUT_InfosGrainsPixels) {
		List<List<String>> lists = ExcelReaderUtil.readExcel(ATEX_OUT_InfosGrainsPixels);
		List<String> title = lists.get(0);
		Map<Integer,Float[]> grainAxisMap = new HashMap<Integer, Float[]>();
     		for(int i=1; i<lists.size(); i++) {
			List<String> item = lists.get(i);
			Integer grainID = Integer.valueOf(item.get(title.indexOf("IDgrain(g)")));
			if(grainID==null) continue;
			Float x =Float.valueOf(item.get(title.indexOf("X(p)"))); 
			Float y = Float.valueOf(item.get(title.indexOf("Y(p)")));
			grainAxisMap.put(-1, new Float[] {x,y}); // x，y最大坐标
			Float[] axis = grainAxisMap.get(grainID);
			if(axis!=null) {
				x = (x+axis[0])/2;
				y = (y+axis[1])/2;
			}
			grainAxisMap.put(grainID, new Float[] {x,y});
		}
		return grainAxisMap;
	}
	
	private int computeGrainGrid(int divideNum,Float[] grainAxis,Float[] maxAxis) {
		// TODO Auto-generated method stub
		int step = Math.round((float)Math.sqrt(divideNum)); // 划分步长
		float perWidth = maxAxis[0]/step; // 横向每格长度
		float perHeight = maxAxis[1]/step;
		float x = grainAxis[0];
		float y = grainAxis[1];
		Integer w = null,h = null; // 晶粒处于x和y方向上的格数
		for(int i=0; i<step; i++) { // 横向扫描
			for(int j=0; j<step; j++) { // 纵向扫描
				if(j*perWidth<=x && x<=(j+1)*perWidth) {
					w = j; // 晶粒在横向上的第j格(从0开始计数)
				}
				if(i*perHeight<=y && y<=(i+1)*perHeight) {
					h = i;
				}
			}
		}
		if(w==null || h==null) {
			throw new RuntimeException("晶粒划分计算程序出错");
		}
		return h*step+w+1;
	}
}
