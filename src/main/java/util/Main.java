package util;

public class Main {

    public static void main(String[] args) {
        int a=12;
        int b=4;
        add(a,b);
        min(a,b);
        mul(a,b);
        div(a,b);
        System.out.println(add(a,b));
        System.out.println(min(a,b));
        System.out.println(mul(a,b));
        System.out.println(div(a,b));
    }

    private static int add(int a, int b) {
        return a + b;
    }
    private static int min(int a, int b) {
        return a - b;
    }
    private static int mul(int a, int b) {
        return a * b;
    }
    private static int div(int a, int b) {
        return a / b;
    }

}
