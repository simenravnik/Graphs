
import java.util.*;
import java.io.*;

public class PathNum {

    private static int START = 0;
    private static int END = 0;
    private static int VISINA;

    public static int stPoti = 0;

    public static void main(String[] args) throws IOException {

      long startTime = System.nanoTime();

        //odpre datoteko
        BufferedReader vhod = new BufferedReader(new FileReader(args[0]));

        //prebere prvo vrstico
        String vrstica = vhod.readLine();
        int stPovezav = Integer.parseInt(vrstica);

        // this graph is directional
        Graph graph = new Graph(stPovezav);

        for(int i = 0; i < stPovezav; i++) {
           vrstica = vhod.readLine();
           String[] vector = vrstica.split(",");
           int node1 = Integer.parseInt(vector[0]);
           int node2 = Integer.parseInt(vector[1]);
           int visina = Integer.parseInt(vector[2]);
           graph.addTwoWayVertex(node1, node2, visina);
        }

        vrstica = vhod.readLine();
        String[] zacetekInKonec = vrstica.split(",");

        START = Integer.parseInt(zacetekInKonec[0]);
        END = Integer.parseInt(zacetekInKonec[1]);

        vrstica = vhod.readLine();
        VISINA = Integer.parseInt(vrstica);

        int[] obiskani = new int[stPovezav];
        obiskani[START] = 1;

        //LinkedList<Integer> visited = new LinkedList<Integer>();
        //visited.add(START);

        depthFirst(graph, obiskani, START);


        System.out.println(stPoti);

        BufferedWriter izhod = new BufferedWriter(new FileWriter(args[1]));
        izhod.write(""+stPoti);

        vhod.close();
        izhod.close();

        //trajanje
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Trajanje: " + (duration/1000000)+" ms");
    }

    private static void depthFirst(Graph graph, int[] obiskani, int zadnji) {
        //LinkedList<Integer> nodes = graph.adjacentNodes(visited.getLast());
        LinkedList<Povezava> nodes = graph.adjacentNodes(zadnji);

        for (Povezava node : nodes) {
           //String[] vector = node.split(",");
           int vozlisce = node.getVozlisce();
           int visina = node.getVisina();

           //int weight = Integer.parseInt(vector[1]);
           if(visina != -1 && visina < VISINA) {
             continue;
           }
            if (obiskani[vozlisce] == 1) {
                continue;
            }
            if(vozlisce == END) stPoti++;
            //System.out.println("smo v vozliscu: " + node);
            //System.out.println(weight);
            //printPath(visited);
            //visited.addLast(vozlisce);
            obiskani[vozlisce] = 1;
            depthFirst(graph, obiskani, vozlisce);
            obiskani[vozlisce] = 0;
            //visited.removeLast();
        }
    }

    private static void printPath(LinkedList<Integer> visited) {
        for (Integer node : visited) {
            System.out.print(node);
            System.out.print(" ");
        }
        System.out.println();
    }
}

class Povezava {
   int vozlisce;
   int visina;

   public Povezava(int vozlisce, int visina) {
      this.vozlisce = vozlisce;
      this.visina = visina;
   }

   public int getVozlisce() {
      return vozlisce;
   }

   public int getVisina() {
      return visina;
   }
}

class Graph {

    private int stVozlisc;
    private LinkedList<LinkedList<Povezava>> list = new LinkedList<LinkedList<Povezava>>();

    public Graph (int stVozlisc) {
      this.stVozlisc = stVozlisc;

      inicializiraj();
    }

    private void inicializiraj(){
      for(int i = 0; i < stVozlisc; i++) {
         list.add(new LinkedList<Povezava>());
      }
    }

    public void addEdge(int node1, int node2, int weight) {
        LinkedList<Povezava> voz = list.get(node1);
        /*if(voz==null) {
            voz = new LinkedList<Povezava>();
            //map.add(Integer.parseInt(node1));
            //map.put(node1, adjacent);
            list.add(voz);
        }*/
        //node2+= "," + weight;
        voz.add(new Povezava(node2, weight));
    }

    public void addTwoWayVertex(int node1, int node2, int weight) {
        addEdge(node1, node2, weight);
        addEdge(node2, node1, weight);
    }

    public LinkedList<Povezava> adjacentNodes(int last) {
      return list.get(last);
    }
}
