import java.text.SimpleDateFormat;
import java.util.*;
//import org.apache.commons.net.util.SubnetUtils;

public class SimilarityFinder {
	//for block
	public static byte same_Block = 0x01;
	public static byte aContainb_Block = 0x02;//0x01 << 1;
	public static byte bContaina_Block = 0x04;//0x01 << 2;
	public static byte adjacent_Block = 0x08;//0x01 << 3;
	public static byte intersect_Block = 0x10;//0x01 << 4;
	public static byte none_Block = 0x20;//0x01 << 5;
	
	//for subset
	private static byte same_subset = 0x01;
	private static byte aContainb_subset = 0x02;//0x01 << 1;
	private static byte bContaina_subset = 0x04;//0x01 << 2;
	private static byte adjacent_subset = 0x08;//0x01 << 3;
	private static byte none_subset = 0x10;//0x01 << 4;

	
	public static List<List<Subset>> convertDataset(List<List<String>> blocksStr){
		if(blocksStr == null || blocksStr.size() == 0) return null;
		List<List<Subset>> blocks = new ArrayList<>();
		for(int i = 0; i < blocksStr.size(); i++){
			List<Subset> block = new ArrayList<>();
			List<String> group = blocksStr.get(i);
			for(int j = 0; j < group.size(); j++){
				block.add(decodeCIDR(group.get(j)));
			}
			blocks.add(block);
		}
		return blocks;
	}
	

	
	public static Subset decodeCIDR(String cidr){
		String[] info = cidr.split("/");
		int maskNum = Integer.valueOf(info[1]);
		int mask = 0xffffffff << (32 - maskNum);
		String[] segs = info[0].split("\\.");
		int ip = 0;
		int offset = 24;
		for(int pos = 0; pos < 4; pos++){
			ip += Integer.valueOf(segs[pos]) << offset;
			offset -=8;
		}

		int prefix = ip & mask;
		Subset subset = new Subset(prefix, mask, cidr, maskNum);
		return subset;
	}
	
	public static byte[][] generateRelationMatrix(List<List<Subset>> blocks){
		int n = blocks.size();
		byte[][] result_matrix = new byte[n][n];
		for(int i = 0; i < n; i++){//a
			for(int j = i + 1; j < n; j++){//b
				result_matrix[i][j] = calculateRelation(blocks.get(i), blocks.get(j));
			}
		}
		return result_matrix;
	}
	

	
	public static byte calculateRelation(List<Subset> alist, List<Subset> blist){	
		byte relation = 0;
		boolean[] bContained = new boolean[blist.size()];//
		for(int i = 0; i < alist.size(); i++){
			byte relation_each = 0;
			for(int j = 0; j < blist.size(); j++){
				int tmpRelation = findRelation(alist.get(i), blist.get(j));
				if((tmpRelation & (same_subset | aContainb_subset)) != 0) bContained[j] = true;//
				relation_each |= tmpRelation;
			}
			
			//output: b contain a, a contains b, adjacent, none
			relation_each = covertRelation(relation_each);
			relation |= relation_each;
		}
		byte relationByGroup = findRelationByGroup(relation,bContained);
		return relationByGroup;
	}
	
	public static byte covertRelation(byte relation){
		if((relation & same_subset ) != 0) return (byte) (same_subset | bContaina_subset);
		if((relation & bContaina_subset) != 0) return bContaina_subset;
		else if((relation & aContainb_subset)!= 0) return aContainb_subset;
		else if((relation & adjacent_subset)!= 0) return adjacent_subset;
		else return none_subset;
	}

	//0x01: same; 0x02: b in a; 0x04: a in b; 0x08: a and b adjacent, 0x10: none
	public static byte findRelation(Subset a, Subset b){		
		int a_prefix = a.getPrefix();
		int b_prefix = b.getPrefix();
		int a_and_b = a_prefix & b_prefix;
		if(a_prefix == b_prefix && a.getMaskNum() == b.getMaskNum()) return same_subset;
		if((a_prefix & b.getMask()) == b_prefix && a.getMaskNum() > b.getMaskNum()) return bContaina_subset;//a in b 
		if((b_prefix & a.getMask()) == a_prefix && a.getMaskNum() < b.getMaskNum()) return aContainb_subset;//b in a 
		if(a_prefix + ~a.getMask() + 1 == b_prefix || b_prefix + ~b.getMask() + 1 == a_prefix) return adjacent_subset;
		return none_subset;
	}
	
	public static byte findRelationByGroup(byte relation, boolean[] bContained){
		if(relation == none_subset) return none_Block;
		if(relation == adjacent_subset || relation == (adjacent_subset | none_subset)) return adjacent_Block;	
		if(relation == bContaina_subset || relation == (bContaina_subset | same_subset)) return bContaina_Block;
		//if((relation == aContainb_subset || relation == (aContainb_subset | same_subset))) return aContainb_Block;
		if((relation & (aContainb_subset | same_subset)) == 0) return intersect_Block;
		for(boolean val: bContained){
			if(!val) return intersect_Block;
		}
		return aContainb_Block;//unknown, maybe aContainb_Block or intersect_Block
	}
	
}
