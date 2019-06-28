
import java.util.*;
import java.io.*;

public class Automatization {

   public static void main(String[] args) throws IOException {
      BufferedReader vhod = new BufferedReader(new FileReader(args[0]));

      String vrstica = vhod.readLine();
      int stPovezav = Integer.parseInt(vrstica);

      int stMest = 0;
      int[] mesta = new int[stPovezav+1];

      int[][] povezave = new int[stPovezav][4];

      for(int i = 0; i < stPovezav; i++) {
         vrstica = vhod.readLine();
         String[] vector = vrstica.split(",");
         int node1 = Integer.parseInt(vector[1]);
         int node2 = Integer.parseInt(vector[2]);
         int idPovezave = Integer.parseInt(vector[0]);
         int dolzina = Integer.parseInt(vector[3]);

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
         povezave[i][2] = idPovezave;
         povezave[i][3] = dolzina;
      }

      vrstica = vhod.readLine();
      int maxDolzina = Integer.parseInt(vrstica);

      vrstica = vhod.readLine();
      double verjetnostOkvare = Double.parseDouble(vrstica);

      Graph graph = new Graph(stMest);

      for(int i = 0; i < povezave.length; i++) {
         if(povezave[i][3] <= maxDolzina) {
            graph.addEdge(povezave[i][0]-1, povezave[i][1]-1, povezave[i][3], povezave[i][2]);
         }
      }

      String zaporedje = graph.poisciMST();

      String[] vektor = zaporedje.split(",");
      for(int i = 0; i < vektor.length; i++) {
         int povezava = Integer.parseInt(vektor[i]);
      }

      BufferedWriter izhod = new BufferedWriter(new FileWriter(args[1]));
      izhod.write(zaporedje);
      izhod.newLine();

      izhod.close();
      vhod.close();
   }
}

class Povezava {
   int source;
   int destination;
   int dolzina;
   int idPovezave;

   public Povezava(int source, int destination, int dolzina, int idPovezave) {
      this.source = source;
      this.destination = destination;
      this.dolzina = dolzina;
      this.idPovezave = idPovezave;
   }
}

class Graph {
   int stVozlisc;
   ArrayList<Povezava> vsePovezave = new ArrayList<>();

   int[][] idPovezave;

   public Graph(int stVozlisc) {
      this.stVozlisc = stVozlisc;
      this.idPovezave = new int[stVozlisc][stVozlisc];
   }

   public void addEdge(int node1, int node2, int dolzina, int idPovezave) {
      Povezava povezava = new Povezava(node1, node2, dolzina, idPovezave);
      /** dodamo povezavo v arraylist vsePovezave */
      vsePovezave.add(povezava);

      this.idPovezave[node1][node2] = idPovezave;
      this.idPovezave[node2][node1] = idPovezave;
   }

   public String poisciMST() {

      /** V vrsto postavimo vse povezave in jo uredimo po dolzinah od najkrajse do najdaljse */
      PriorityQueue<Povezava> vrsta = new PriorityQueue<>(vsePovezave.size(), Comparator.comparingInt(o -> o.dolzina));
      /** dodajamo v vrsto */
      for(int i = 0; i < vsePovezave.size(); i++) {
         vrsta.add(vsePovezave.get(i));
      }

      /** ustvarimo tabelo parent za hranjenje ocetov */
      int[] parent = new int[stVozlisc];
      /** nastavimo, da je vsako vozlisce je samemu sebi oce */
      for(int i = 0; i < stVozlisc; i++) {
         parent[i] = i;
      }

      /** naredimo vrsto za vstavljanje dodanih povezav */
      PriorityQueue<Povezava> mst = new PriorityQueue<>(Comparator.comparingInt(o -> o.idPovezave));

      int index = 0;
      while(index < stVozlisc-1) {
         Povezava povezava = vrsta.remove();

         /** preverimo ali bo mogoce nastal cikel */
         int oceIzvora = poisciOceta(parent, povezava.source);
         int ocePonora = poisciOceta(parent, povezava.destination);

         /** ce ni cikla, dodamo povezavo v minimalno vpeto drevo int povecamo index*/
         if(oceIzvora != ocePonora) {
            mst.add(povezava);
            index++;
            zdruziDrevesi(parent, oceIzvora, ocePonora);
         }
      }
      return sestaviZaporedje(stVozlisc-1, mst);
   }

   /** dve drevesi zdruzimo tako da imata enakega naj oceta */
   public void zdruziDrevesi(int[] parent, int x, int y) {
      int oceOdX = poisciOceta(parent, x);
      int oceOdY = poisciOceta(parent, y);

      /** oce od x-sa pstane tudi oce od y-a */
      parent[oceOdY] = oceOdX;
   }

   /** poiscemo naj oceta vozlisca vozlisce */
   public int poisciOceta(int[] parent, int vozlisce) {
      if(parent[vozlisce] != vozlisce)
         return poisciOceta(parent, parent[vozlisce]);
      return vozlisce;
   }

   /** sestavimo zaporedje za izpis */
   public String sestaviZaporedje(int stVozlisc, PriorityQueue<Povezava> mst) {
      Povezava povezava = mst.remove();
      String vrni = "" + povezava.idPovezave;
      //System.out.print(povezava.idPovezave);
      for(int i = 0; i < stVozlisc-1; i++) {
         povezava = mst.remove();
         vrni+=","+povezava.idPovezave;
         //System.out.print(","+povezava.idPovezave);
      }
      //System.out.println();
      return vrni;
   }
}
