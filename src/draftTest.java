import java.util.ArrayList;

public class draftTest {
    public static void main(String[] args) {

        int L = 17;
        int r = 0;

        if (L%6 - L/6 <= 0) {
            r = L / 6;
        }
        else if (L%7 - L/7 <= 0) {
            r = L / 7;
        }
        else if (L%8 == 0) {
            r = L / 8;
        }
        else if (L%5 - L/5 <= 0) {
            r = L / 5;
        }

        ArrayList<Integer> numbers = new ArrayList<>();
        int pn = 1;
        int inv = 1;


        for (int i=L; i>0; i--){
            numbers.add(pn);
            if (pn == r) {
                inv = -1;
                if (numbers.size() != L) {
                    numbers.add(pn);
                    pn = pn + inv;
                    i--;
                    continue;
                }
            }
            if (pn == 1) {
                inv = 1;
                if (numbers.size() != 1 && numbers.size() != L) {
                    numbers.add(pn);
                    pn = pn + inv;
                    i--;
                    continue;
                }
            }

            pn = pn + inv;
        }

        int[] poolSize = new int[r];

        for (int n : numbers){
            poolSize[n-1] = poolSize[n-1] + 1;
        }

        System.out.println(numbers);
        System.out.println("List Length = " + numbers.size());

        for (int i=0; i<poolSize.length; i++){
            System.out.println("Pool " + (i+1) + ": " + poolSize[i]);
        }


    }

}
