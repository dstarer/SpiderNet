import java.util.concurrent.atomic.AtomicInteger;

public class TestRequest{

    public void add(AtomicInteger x) {
        x.incrementAndGet();
    }

    public static class A extends Thread {
        private TestRequest request = null;
        private AtomicInteger x = null;
        public void setX(Integer x) {
            this.x = new AtomicInteger(x);
        }
        public void setRequest(TestRequest request) {
            this.request = request;
        }

        public void start() {
            if (x==null) {
                throw new ExceptionInInitializerError("");
            }
            super.start();
        }
        public void run() {
            for(int i = 0; i < 100; ++ i) {
                request.add(x);
                try{
                    Thread.sleep(1);
                }catch (InterruptedException e) {

                }
            }
            System.out.println(getName() + " x = " + x);
        }

    }
	public static void main(String[] args) {
        TestRequest request = new TestRequest();
        A a = new A();
        A b = new A();
        a.setRequest(request);
        b.setRequest(request);
//        a.setX(0);
//        b.setX(10000);
        a.setName("a");
        b.setName("b");
        a.start();
        b.start();

        try{
            a.join();
            b.join();
        }catch (InterruptedException e) {

        }
    }
}
