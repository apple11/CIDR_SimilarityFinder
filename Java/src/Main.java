import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
	public static void main(String[] args){
		int[] input = readInput();
		
		Date startDate = new Date();
		System.out.println("generating data:" + new SimpleDateFormat("HH:mm:ss").format(startDate));
		
		DataGenerator dg = new DataGenerator(input[0],input[1]);		
		List<List<String>> blocksStr = dg.generateDataset();
		
		Date generateDate = new Date();
		long time = generateDate.getTime() - startDate.getTime();
		System.out.println("data generation completed:processing time(ms)" + time);
		System.out.println("converting data with cidr format to IP");
	
		SimilarityFinder sf = new SimilarityFinder();
		List<List<Subset>> blocks = sf.convertDataset(blocksStr);
		
		Date convertDate = new Date();
		time = convertDate.getTime() - generateDate.getTime();
		System.out.println("data convertion completed:processing time(ms)" + time);
		
		byte[][] matrix = sf.generateRelationMatrix(blocks);

		time = new Date().getTime() - convertDate.getTime();
		System.out.println("matrix generation completed. processing time(ms)" + time);
		
		Representation rp = new Representation(blocksStr, blocks, matrix);
		Thread rpThrd = new Thread(rp);
		rpThrd.start();
		
	}
	
	public static int[] readInput(){
		String blocksNumStr = readUserInput("please input the number of blocks(Integer)£º");
		String subnetLimitStr = readUserInput("please input Number Limit of Subset in each Block(Integer)£º");
		int blockNum = Integer.valueOf(blocksNumStr).intValue();
		int subsetLimit = Integer.valueOf(subnetLimitStr).intValue();
		int[] rs = new int[]{blockNum, subsetLimit};
		return rs;
	}
	
	public static String readUserInput(String prompt){
		try {
			System.out.print(prompt);
	        InputStreamReader input = new InputStreamReader(System.in);
	        return new BufferedReader(input).readLine();
		}catch (IOException e) {
            e.printStackTrace();
            System.out.println("invalid input");
        }
		return null;
    }
	
	
}
