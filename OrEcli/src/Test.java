import java.util.ArrayList;




public class Test {
	
	private O o;
	private ArrayList<String> ss;

	public static void main(String[] args) {
		O o = new O();
		Test t1 = new Test(o);
		Test t2 = new Test(o);
		t1.getList().remove(0);
		System.out.println(t1.ss.size() + " " + t2.ss.size());
	}
	
	public Test(O o) {
		this.o = o;
		ss = new ArrayList<String>();
		ss.add("A string");
	}
	
	static class O {
	}
	
	public ArrayList<String> getList() {
		return ss;
	}
	
}
