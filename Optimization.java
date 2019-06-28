
import java.util.*;
import java.io.*;

public class Optimization {

    private static int START = 0;
    private static int END = 0;
    public static int[] cenaDoKoncnega;

    public static boolean seSpremeni;

    public static String line = "";

    public static void main(String[] args) throws IOException {

      long startTime = System.nanoTime();

        //odpre datoteko
        BufferedReader vhod = new BufferedReader(new FileReader(args[0]));

        //prebere prvo vrstico
        String vrstica = vhod.readLine();
        int stPovezav = Integer.parseInt(vrstica);

        int stMest = 0;
        int[] mesta = new int[stPovezav+1];

        int[][] povezave = new int[stPovezav][4];

        for(int i = 0; i < stPovezav; i++) {
          vrstica = vhod.readLine();
          String[] vector = vrstica.split(",");
          int node1 = Integer.parseInt(vector[0]);
          int node2 = Integer.parseInt(vector[1]);
          int cena = Integer.parseInt(vector[2]);

          if(mesta[node1] == 0) {
              mesta[node1] = 1;
              stMest++;
          }
          if(mesta[node2] == 0) {
            mesta[node2] = 1;
            stMest++;
          }
          povezave[i][0] = node1;
          povezave[i][1] = node2;
          povezave[i][2] = cena;
        }

        Graph graph = new Graph(stMest+1);

        for(int i = 0 ; i< povezave.length; i++)
            graph.addEdge(povezave[i][0], povezave[i][1], povezave[i][2]);

      vrstica = vhod.readLine();
      String[] zacetekInKonec = vrstica.split(",");

      START = Integer.parseInt(zacetekInKonec[0]);
      END = Integer.parseInt(zacetekInKonec[1]);

      cenaDoKoncnega = new int[stMest+1];

      int dolzinaPrejsni = 0;

      while(true) {
         int[] visited = new int[stMest+1];
         visited[START] = 1;

         LinkedList<Integer> zaporedje = new LinkedList<Integer>();
         zaporedje.addLast(START);
         seSpremeni = false;
         dolzinaPrejsni = trenutnaDolzina;
         /** rekurzija */
         racunajCeno(graph, visited, zaporedje, START);

         if(seSpremeni == false) break;
      }

      BufferedWriter izhod = new BufferedWriter(new FileWriter(args[1]));

      izhod.write(line);
      if(dolzinaPrejsni != trenutnaDolzina) izhod.write(zadnjaVrstica);

      vhod.close();
      izhod.close();

      //trajanje
      long endTime = System.nanoTime();
      long duration = (endTime - startTime);
      System.out.println("Trajanje: " + (duration/1000000)+" ms");
   }

   public static int trenutnaDolzina = 0;
   public static String zadnjaVrstica = "";

   public static void racunajCeno(Graph graph, int[] visited, LinkedList<Integer> zaporedje, int last) {
      if(last == END) {
         trenutnaDolzina = zaporedje.size();
         /*line += zaporedje.get(0);
         zadnjaVrstica = ""+zaporedje.get(0);
         //System.out.print(zaporedje.get(0));
         zaporedje.removeFirst();
         for(Integer node : zaporedje) {
            line += "," + node;
            zadnjaVrstica += "," + node;
            //System.out.print("," + node);
         }
         line+='\n';
         zadnjaVrstica+='\n';
         //System.out.println();*/
         dodajVIzpis(zaporedje);
         return;
      }
      LinkedList<Povezava> nodes = graph.adjacentNodes(last);

      int naslednji = -1;
      int najCena = -1;

      if(nodes == null) {
         cenaDoKoncnega[last] = -1;
         //System.out.println(zaporedje);
         /*line += zaporedje.get(0);
         //System.out.print(zaporedje.get(0));
         zaporedje.removeFirst();
         for(Integer node : zaporedje) {
            line += "," + node;
            //System.out.print("," + node);
         }
         line+='\n';*/
         dodajVIzpis(zaporedje);
         return;
      }

      for(Povezava node : nodes) {
         int vozlisce = node.vozlisce;
         int cena = node.cena;
         if(visited[vozlisce] == 1 || cenaDoKoncnega[vozlisce] == -1) continue;

         for(int i = 0; i < 10; i+=2) i--;

         int izracunanaCena = cena + cenaDoKoncnega[vozlisce];
         //System.out.println(vozlisce + "---> cena: " + izracunanaCena);

         if(najCena == -1 || izracunanaCena < najCena) {
            najCena = izracunanaCena;
            naslednji = vozlisce;
            //System.out.println(naslednji);
         } else if(najCena == izracunanaCena)
            if(naslednji > vozlisce) naslednji = vozlisce;
      }

      /*System.out.println("SEM TU");
      System.out.println(cenaDoKoncnega[last]);
      System.out.println(last + " ---> "+naslednji);*/

      if(naslednji == -1) {
         //cenaDoKoncnega[last] = -1;
         //System.out.println(zaporedje);
         /*line += zaporedje.get(0);
         //System.out.print(zaporedje.get(0));
         zaporedje.removeFirst();
         for(Integer node : zaporedje) {
            line += "," + node;
            //System.out.print("," + node);
         }
         line+='\n';*/
         dodajVIzpis(zaporedje);
         return;
      }

      if(najCena > cenaDoKoncnega[last]) {
         seSpremeni = true;
         cenaDoKoncnega[last] = najCena;
      }

      visited[naslednji] = 1;
      zaporedje.addLast(naslednji);
      racunajCeno(graph, visited, zaporedje, naslednji);
   }

   public static void dodajVIzpis(LinkedList<Integer> zaporedje) {
      line += zaporedje.get(0);
      zadnjaVrstica = ""+zaporedje.get(0);
      //System.out.print(zaporedje.get(0));
      zaporedje.removeFirst();
      for(Integer node : zaporedje) {
         line += "," + node;
         zadnjaVrstica += "," + node;
         //System.out.print("," + node);
      }
      line+='\n';
      zadnjaVrstica+='\n';
   }
}

class Povezava {
   int vozlisce;
   int cena;
   //int cenaDoKoncnega;

   public Povezava(int vozlisce, int cena) {
      this.vozlisce = vozlisce;
      this.cena = cena;
      //this.cenaDoKoncnega = 0;
   }
}

class Graph {

    private int stVozlisc;
    public LinkedList<LinkedList<Povezava>> list = new LinkedList<LinkedList<Povezava>>();

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
        if(voz==null) {
            voz = new LinkedList<Povezava>();
            list.add(voz);
        }
        voz.add(new Povezava(node2, weight));
    }

    public LinkedList<Povezava> adjacentNodes(int last) {
      return list.get(last);
    }
}
