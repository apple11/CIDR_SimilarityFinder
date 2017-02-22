import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat; 

public class Representation extends JPanel implements Runnable{
	private List<List<String>> blocksStr;
	private List<List<Subset>> blocks;
	private byte[][] matrix;
	
	public Representation(List<List<String>> blocksStr, List<List<Subset>> blocks, byte[][] matrix){
		this.blocksStr = blocksStr;
		this.blocks = blocks;
		this.matrix = matrix;
	}
	
	public void show(){		
		JFrame jfMain = new JFrame("Data Representation");
		jfMain.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    jfMain.setSize(600,400);
	    jfMain.setLocationRelativeTo(null);
	    jfMain.getContentPane().add(this);
	    
		int[] proportion = calProportion();
		ChartPanel chartFrame = plotPieChart(proportion);		
		
		JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanel1.setVisible(true);
        jPanel1.setSize(300, 300);
        jPanel1.add(chartFrame, BorderLayout.CENTER);
        jPanel1.validate();
        
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        jPanel2.setVisible(true);
        jPanel2.setSize(200, 200);
        
        JLabel label1 = new JLabel("input first block number:");
        JTextField block1 = new JTextField(5);
        jPanel2.add(label1);
        jPanel2.add(block1);
        
        JLabel label2 = new JLabel("second block number:");
        JTextField block2 = new JTextField(5);
        jPanel2.add(label2);
        jPanel2.add(block2);
        
		JButton show = new JButton("show");
		jPanel2.add(show);
		
		JTextArea pairRelation = new JTextArea(21, 50);
		pairRelation.setLineWrap(true);        
		pairRelation.setWrapStyleWord(true);
		jPanel2.add(pairRelation);
		pairRelation.setFont(new Font("Serif", Font.BOLD, 15));
		
		
		jfMain.add(jPanel1, BorderLayout.NORTH);
		jfMain.add(jPanel2, BorderLayout.CENTER);
		jfMain.add(pairRelation, BorderLayout.SOUTH);
		jfMain.setVisible(true);
		
		
		show.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int selected_block1 = Integer.parseInt(block1.getText());
				int selected_block2 = Integer.parseInt(block2.getText());
				String text = showPairRelation(selected_block1, selected_block2);
				pairRelation.setText(text);
			}
		});
	}
	
	
	
	private int[] calProportion(){
		int[] vector = new int[5]; // contain, intersection, adjacent, none, all
		for(int i = 0; i < matrix.length; i++){
			for(int j = i + 1; j < matrix[i].length; j++){
				byte el = matrix[i][j];
				if((el & (SimilarityFinder.aContainb_Block | SimilarityFinder.bContaina_Block)) != 0) vector[0]++;
				if((el & SimilarityFinder.intersect_Block) != 0) vector[1]++;
				if((el & SimilarityFinder.adjacent_Block) != 0) vector[2]++;
				if((el & SimilarityFinder.none_Block) != 0) vector[3]++;
				vector[4]++;
			}
		}
		return vector;
	}

	private ChartPanel plotPieChart(int[] proportion){
		DefaultPieDataset data = new DefaultPieDataset();
		String[] lable = new String[]{"Contain", "Intersect", "Adjacent", "None"};
		for(int i = 0; i < lable.length; i++){
			data.setValue(lable[i], proportion[i]);
		}		
		// ceate a chart
		JFreeChart chart = ChartFactory.createPieChart("Pie chart of relations between blocks",data,true,true,false);
		chart.getLegend().setItemFont(new Font("SansSerif", Font.ROMAN_BASELINE, 15));  
		PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));  
        plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})"));  

		plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(false);
        plot.setLabelGap(0.02);
        
        ChartPanel CP = new ChartPanel(chart);
		return CP;
	}
	
	private String showPairRelation(int i, int j){
		if(i >= matrix.length || j >= matrix.length || i < 0 || j < 0 ) return "input not in range";
		StringBuffer sb = new StringBuffer();		
		showSubnet(sb, i);
		String mesg = "";
		if(i == j){
			sb.append("block" + i + " and block" + j + " is the same block");
			return sb.toString();
		}
		showSubnet(sb, j);
		if(i > j){
			int tmp = j;
			j = i; 
			i = tmp;
		}
		if(matrix[i][j] == SimilarityFinder.aContainb_Block) mesg = "block" + i + " contains block" + j;
		if(matrix[i][j] == SimilarityFinder.bContaina_Block) mesg = "block" + j + " contains block" + i;
		if(matrix[i][j] == SimilarityFinder.adjacent_Block) mesg = "block" + i + " is adjacent with block" + j;
		if(matrix[i][j] == SimilarityFinder.intersect_Block) mesg = "block" + i + " intersects with block" + j;
		if(matrix[i][j] == SimilarityFinder.none_Block) mesg = "block" + i + " has none relation with block" + j;
		sb.append(mesg);
		return sb.toString();
	}
	
	private void showSubnet(StringBuffer sb, int i){
		sb.append("block" + i + ": ");
		for(String subset : blocksStr.get(i)){
			sb.append(subset + "\t");
		}
		sb.append("\n\n");
	}

	@Override
	public void run() {
		show();
	}
	
	public void start(){
		run();
	}
}
