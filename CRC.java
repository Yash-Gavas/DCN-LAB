import java.util.Scanner;

public class CRC {
    public static String xor(String a, String b) {
        int len = Math.max(a.length(), b.length());
        int x = Integer.parseInt(a, 2);
        int y = Integer.parseInt(b, 2);
        String res = Integer.toBinaryString(x ^ y);
        res = String.format("%" + len + "s", res).replace(" ", "0");
        return res;
    }

    public static String divide(String div, String divid) {
        int dlen = div.length();
        int dndlen = divid.length();
        while (dndlen >= dlen) {
            String temp;
            if (divid.charAt(0) == '1') {
                temp = xor(div, divid.substring(0, dlen));
            } else {
                temp = xor(repeatString("0", dlen), divid.substring(0, dlen));
            }
            divid = temp.substring(1) + divid.substring(dlen);
            dndlen -= 1;
        }
        return divid;
    }

    public static String generator(String msg, String gen) {
        int genlen = gen.length();
        String divid = msg + repeatString("0", genlen - 1);
        String rem = divide(gen, divid);
        return msg + rem;
    }

    public static boolean checkcode(String cw, String gw) {
        String temp = divide(gw, cw);
        return temp.equals(repeatString("0", gw.length() - 1));
    }

    private static String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("CRC Program");
        String gen = "10001000000100001"; // Generator polynomial

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Generate Codeword from Dataword");
            System.out.println("2. Check Codeword for Errors");
            System.out.print("Enter your choice: ");
            int ch = s.nextInt();
            switch (ch) {
                case 1:
                    System.out.print("Enter dataword: ");
                    String dw = s.next();
                    System.out.println("Generated Codeword: " + generator(dw, gen));
                    break;
                case 2:
                    System.out.print("Enter codeword: ");
                    String cw = s.next();
                    if (checkcode(cw, gen)) {
                        System.out.println("No Error detected in the codeword.");
                    } else {
                        System.out.println("Error detected in the codeword.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
