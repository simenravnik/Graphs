
import java.util.*;
import java.io.*;

public class Friends {

   public static void main(String[] args) throws IOException {

      BufferedReader vhod = new BufferedReader(new FileReader(args[0]));
      BufferedWriter izhod = new BufferedWriter(new FileWriter(args[1]));

      String vrstica = vhod.readLine();
      int stPovezav = Integer.parseInt(vrstica);

      /** ustvarimo linkedlist mnozic in dodamo prazno mnozico na zacetek linkedlist za lazje delo kasneje */
      LinkedList<Set> listOfSets = new LinkedList<Set>();
      Set b = new Set();
      listOfSets.addLast(b);

      for(int i = 0; i < stPovezav; i++) {
         vrstica = vhod.readLine();
         String[] vector = vrstica.split(",");
         int prvi = Integer.parseInt(vector[0]);
         int drugi = Integer.parseInt(vector[1]);

         /** indexPrvi predstavlja element v listOfSets kjer se nahaja prvi */
         /** indexDrugi predstavlja element v listOfSets kjer se nahaja drugi */
         int indexPrvi = 0;
         int indexDrugi = 0;
         int stevec = 0;
         for(Set a : listOfSets) {
            /** preverimo ali se prvi nahaja v mnozici a */
            if(indexPrvi == 0 && a.locate(prvi) != null) {
               indexPrvi = stevec;
            }
            /** preverimo ali se drugi nahaja v mnozici a */
            if(indexDrugi == 0 && a.locate(drugi) != null) {
               indexDrugi = stevec;
            }
            stevec++;
         }
         /** ce sta indexPrvi in indexDrugi enana 0, potem moramo ustavriti novo mnozico in vstaviti vanjo prvega in drugega */
         if(indexPrvi == 0 && indexDrugi == 0) {
            Set set = new Set();
            set.insert(prvi);
            set.insert(drugi);
            listOfSets.addLast(set);
         } else if(indexPrvi != 0 && indexDrugi == 0) {
            /** v mnozico kjer se nahaja prvi dodamo drugega */
            Set setPrvi = listOfSets.get(indexPrvi);
            setPrvi.insert(drugi);
         } else if(indexPrvi == 0 && indexDrugi != 0) {
            /** v mnozico kjer se nahaja drugi dodamo prvega */
            Set setDrugi = listOfSets.get(indexDrugi);
            setDrugi.insert(prvi);
         } else if(indexPrvi != 0 && indexDrugi != 0) {
            /** mnozici prvega in drugega ze obstajata */
            if(indexPrvi != indexDrugi) {
               /** ce ta te dve mnozici razlicni ju zdruzimo in eno izbrisemo */
               Set setPrvi = listOfSets.get(indexPrvi);
               Set setDrugi = listOfSets.get(indexDrugi);
               setPrvi.union(setDrugi);
               listOfSets.remove(indexDrugi);
            } else if(indexPrvi == indexDrugi) {
               /** prvi in drugi se ze nahajata v isti mnozici zato izpisemo nepotreben podatek */
               String line = prvi + "," + drugi;
               izhod.write(line);
               izhod.newLine();
               System.out.println(prvi+"," + drugi);
            }
         }
      }
      vhod.close();
      izhod.close();
   }
}

class SetElement {
	Object element;
	SetElement next;

	SetElement() {
		element = null;
		next = null;
	}
}


class Set {
	private SetElement first;

	public Set() {
		makenull();
	}

	public void makenull() {
		first = new SetElement();
	}

	public SetElement first() {
		return first;
	}

	public SetElement next(SetElement pos) {
		return pos.next;
	}

	public void insert(Object obj) {
		// nov element vstavimo samo, ce ga ni med obstojecimi elementi mnozice
		if (locate(obj) == null) {
			SetElement nov = new SetElement();
			nov.element = obj;
			nov.next = first.next;
			first.next = nov;
		}
	}

	public boolean overEnd(SetElement pos) {
		if (pos.next == null)
			return true;
		else
			return false;
	}

	public Object retrieve(SetElement pos) {
		return pos.next.element;
	}

	public SetElement locate(Object obj) {
		// sprehodimo se cez seznam elementov in preverimo enakost (uporabimo metodo equals)
		for (SetElement iter = first(); !overEnd(iter); iter = next(iter))
			if (obj.equals(retrieve(iter)))
				return iter;

		return null;
	}

	public void union(Set a) {
		// dodaj vse elemente iz mnozice a (metoda insert poskrbi, da ni podvajanja elementov)
		for (SetElement iter = a.first(); !a.overEnd(iter); iter = a.next(iter))
			insert(a.retrieve(iter));
	}
}
