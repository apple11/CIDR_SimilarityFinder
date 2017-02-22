
public class Subset {
	private int prefix = 0;
	private int mask = 0;
	private String prefix_binary = "";
	String cidr = "";
	private int maskNum = 0;
	public Subset(int prefix, int mask, String cidr, int maskNum){
		this.prefix = prefix;
		this.mask = mask;
		this.prefix_binary = Integer.toBinaryString(prefix);
		this.cidr = cidr;
		this.maskNum = maskNum;
	}
	public String getPrefix_binary() {
		return prefix_binary;
	}
	public void setPrefix_binary(String prefix_binary) {
		this.prefix_binary = prefix_binary;
	}
	public String getCidr() {
		return cidr;
	}
	public void setCidr(String cidr) {
		this.cidr = cidr;
	}
	public int getMaskNum() {
		return maskNum;
	}
	public void setMaskNum(int maskNum) {
		this.maskNum = maskNum;
	}
	public int getPrefix() {
		return prefix;
	}
	public void setPrefix(int prefix) {
		this.prefix = prefix;
	}
	public int getMask() {
		return mask;
	}
	public void setMask(int mask) {
		this.mask = mask;
	}
	
}
