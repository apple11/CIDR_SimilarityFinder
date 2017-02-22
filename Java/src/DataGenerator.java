import java.util.*;

public class DataGenerator {
	private int blocksNum = 0;
	private int subsetNumLimit = 0;
	
	public DataGenerator(int blocksNum, int subsetNumLimit){
		this.blocksNum = blocksNum;
		this.subsetNumLimit = subsetNumLimit;
	}
	
	public List<List<String>> generateDataset(){
		List<List<String>> dataset = new ArrayList<>();
		for(int i = 0; i < blocksNum; i++){
			List<String> group = new ArrayList<>();
			int dimension = getRandomInt(1, subsetNumLimit+1);
			for(int j = 0; j < dimension; j++){
				String subnetStr = generateSubnetStr();
				group.add(subnetStr);
			}
			dataset.add(group);
		}
		return dataset;
	}
	
	public String generateSubnetStr(){
	    String subnetStr = "";
	    for (int j = 0; j< 4; j++) {
	        int part = getRandomInt(0,255);
	        subnetStr += String.valueOf(part);
	        if(j<3) subnetStr += '.';
	    }
	    subnetStr += "/" + getRandomInt(0,32);
	    return subnetStr; 
	}
	
	public int getRandomInt(int min, int max){
		min = (int) Math.ceil(min);
		max = (int) Math.floor(max);
		return (int) (Math.floor(Math.random() * (max - min)) + min);
	}
	


}
