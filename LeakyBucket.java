import java.util.Scanner;
import java.lang.*;

public class LeakyBucket {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        int i;
        int a[] = new int[20];
        int buck_rem = 0, sent, recv;
        System.out.println("Enter bucket cap");
        int buck_cap=scanner.nextInt();
        System.out.println(("Enter rate"));
        int rate=scanner.nextInt();
        System.out.println("Enter the number of packets");
        int n = scanner.nextInt();
        System.out.println("Enter the packets");
        for (i = 1; i <= n; i++)
            a[i] = scanner.nextInt();

        System.out.println("Clock \t packet size \t accept \t sent \t remaining");
        for (i = 1; i <= n; i++) {
            if (a[i] != 0) {
                if (buck_rem + a[i] > buck_cap)
                    recv = -1;
                else {
                    recv = a[i];
                    buck_rem += a[i];
                }
            } 
            else
                recv = 0;
            if (buck_rem != 0) {
                if (buck_rem < rate) {
                    sent = buck_rem;
                    buck_rem = 0;
                } else {
                    sent = rate;
                    buck_rem = buck_rem - rate;
                }
            } 
            else
                sent = 0;
            if (recv == -1)
                System.out.println(+i + "\t\t" + a[i] + "\t dropped \t" + sent + "\t" + buck_rem);
            else
                System.out.println(+i + "\t\t" + a[i] + "\t\t" + recv + "\t" + sent + "\t" + buck_rem);
        }
        scanner.close();
    }
}