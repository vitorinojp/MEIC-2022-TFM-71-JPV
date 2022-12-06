package asaintsever.httpsinkconnector.utils;

public class Pair<A, B> {
    private A a;
    private B b;

    public Pair(A key, B value) {
        this.a = key;
        this.b = value;
    }

    public A getKey(){
        return a;
    }

    public B getValue(){
        return b;
    }
}
