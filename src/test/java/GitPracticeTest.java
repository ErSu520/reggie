

public class GitPracticeTest {

    public static void main(String[] args) {
        Test test = new Test1();
        do {
            test.test();
            System.out.println("done");
        }while (test == null);
    }
}
