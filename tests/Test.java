import divbyzero.qual.*;

class Foo {
    public static void test(int x) {
        int a = 1 / 0;
        int b = 1 / 1;

        int c = 1 / (0 + 0);
        int d = 1 / (0 + 1);

        if (x == 0) {
            int e = 1 / (x + 1);
        } else {
            int e = 1 / x;
        }
    }
}
