import java.net.InetAddress;
import java.net.UnknownHostException;

public class T {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getByName("161.117.87.5").getHostAddress());


    }
}
