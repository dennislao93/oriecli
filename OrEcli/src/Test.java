


public class Test {
	
	O o;

	public static void main(String[] args) {
		O o = new O();;
		Test t1 = new Test(o);
		Test t2 = new Test(o);
		System.out.println(t1.o == t2.o);
	}
	
	public Test(O o) {
		this.o = o;
	}
	
	static class O {
	}
	
}
