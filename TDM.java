import java.util.Scanner;
public class TDM{
    public static void main(String[] args){
        Scanner s =new Scanner(System.in);
        int i,n,qt,temp,count=0;
        int sq=0;
        float awt=0,atat=0;
        int bt[]=new int[10];
        int wt[]=new int[10];
        int tat[]=new int[10];
        int rbt[]=new int[10];

        System.out.println("Enter stations");
        n=s.nextInt();
        System.out.println("Enter P.T");
        for(i=0;i<n;i++){
            System.out.println("S" +i+ "=");
            bt[i]=s.nextInt();
            rbt[i]=bt[i];
        }
        System.out.println("Enter frames");
        qt=s.nextInt();
        while(true){
            for(i=0,count=0;i<n;i++){
                temp=qt;
                if(rbt[i]==0){
                    count++;
                    continue;
                }
                if(rbt[i]>qt){
                    rbt[i]-=qt;
                }
                else if(rbt[i]>=0){
                    temp=rbt[i];
                    rbt[i]=0;
                }
                sq = sq+temp;
                tat[i]=sq;
            }
            if(n==count){
                break;
            }
        }

        System.out.println("-------------------------------------------");
        System.out.println("Stations\tP.T\t\tC.T\t\tW.T");
        System.out.println("-------------------------------------------");
        for(i=0;i<n;i++){
            wt[i]=tat[i]-bt[i];
            awt=awt+wt[i];
            atat=atat+tat[i];
            System.out.println("\n" +(i+1) +"\t\t" + bt[i] +"\t\t" + tat[i] +"\t\t" +wt[i] +"\n");
        }
        s.close();
    }
}