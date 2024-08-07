import java.util.*;

public class ford {
    static class Edge {
        int src, dest, weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }

    public static void bellmanFord(int v, List<Edge> edges, int src) {
        int[] dis = new int[v];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[src] = 0;

        for (int i = 1; i < v; i++) {
            for (Edge edge : edges) {
                if (dis[edge.src] != Integer.MAX_VALUE && dis[edge.src] + edge.weight < dis[edge.dest]) {
                    dis[edge.dest] = dis[edge.src] + edge.weight;
                }
            }
        }

        for (Edge edge : edges) {
            if (dis[edge.src] != Integer.MAX_VALUE && dis[edge.src] + edge.weight < dis[edge.dest]) {
                System.out.println("Graph contains a negative-weight cycle");
                return;
            }
        }

        System.out.println("Vertex\t\tDistance from Source");
        for (int i = 0; i < v; i++) {
            System.out.println(i + "\t\t" + dis[i]);
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the number of vertices and edges:");
        int v = s.nextInt();
        int e = s.nextInt();

        List<Edge> edges = new ArrayList<>();
        System.out.println("Enter the edges (source, destination, weight):");
        for (int i = 0; i < e; i++) {
            int src = s.nextInt();
            int dest = s.nextInt();
            int weight = s.nextInt();
            edges.add(new Edge(src, dest, weight));
        }

        System.out.println("Enter the start vertex:");
        int src = s.nextInt();

        bellmanFord(v, edges, src);
        s.close();
    }
}
