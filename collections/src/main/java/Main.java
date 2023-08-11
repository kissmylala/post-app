import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> list = List.of("Adem","Programmer","Java","Adem","Number one","Adem");
        Iterator<String> iterator = list.listIterator(5);
        list.add("Adem");
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }



    }
}
