/**
 * Created by rushabhmehta91 on 4/24/15.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class BloomFilter {

    public static final int bits = 4194304 * 4;
    public static int bitVectorSize = bits / 32;
    public static int[] vector = new int[bitVectorSize];
    public static int[] vector2 = new int[bitVectorSize];
    public int unique = 0;
    public int uniquetest = 0;

    public static void main(String[] args) {

        BloomFilter filter = new BloomFilter();

        File file1 = new File(args[0]);
        File file2 = new File(args[1]);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file1));
            int count = 0;
            String URL = null;
            while ((URL = br.readLine()) != null) {
                URL = URL.split("\\s+")[6];
                count++;
                filter.add(filter.hash3(URL));
                filter.add(filter.hash4(URL));
                filter.add(filter.hash5(URL));
            }
            System.out.println(count);

            count = 0;
            br = new BufferedReader(new FileReader(file2));
            while ((URL = br.readLine()) != null) {
                URL = URL.split("\\s+")[6];
                count++;
                if (filter.search(filter.hash3(URL), URL))
                    continue;
                if (filter.search(filter.hash4(URL), URL))
                    continue;
                if (filter.search(filter.hash5(URL), URL))
                    continue;
            }
            System.out.println(count);
            System.out.println("Uniquetest: " + filter.uniquetest);
            System.out.println("Unique: " + filter.unique);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // simple summation function
    public int hash1(String x) {

        char ch[];
        ch = x.toCharArray();
        int i, sum;
        for (sum = 0, i = 0; i < x.length(); i++)
            sum += ch[i];
        return Math.abs(sum) % bits;
    }

    // Use folding on a string, summed 4 bytes at a time
    public int hash2(String s) {

        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++) {
            char c[] = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++) {
                sum += c[k] * mult;
                mult *= 256;
            }
        }
        char c[] = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++) {
            sum += c[k] * mult;
            mult *= 256;
        }
        return (int)(Math.abs(sum) % bits);
    }

    // Horner's rule
    public int hash3(String s) {
        int h = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            h += (s.charAt(i) + 128 * h) % bits;
        }
        return (h % bits);
    }

    // djb2
    public int hash4(String str) {
        long hash = 5381;
        int c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            hash += ((hash << 5) + hash) + c; // hash * 33 + c
        }
        return (int) (Math.abs(hash) % bits);
    }

    // sdbm
    public int hash5(String str) {
        long hash = 0;
        int c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            hash += c + (hash << 6) + (hash << 16) - hash;
        }
        return (int)(Math.abs(hash) % bits);
    }

    // lose lose
    public int hash6(String str) {
        int hash = 0;
        int c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            hash += c;
        }
        return (Math.abs(hash) % bits);
    }

    // DEK hash
    public int hash7(String str) {
        long hash = str.length();

        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
        }

        return (int) (Math.abs(hash) % bits);
    }

    // RS hash
    public int hash8(String str) {
        int b = 378551;
        int a = 63689;
        long hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i);
            a = a * b;
        }

        return (int)(Math.abs(hash) % bits);
    }

    // JS hash
    public int hash9(String str) {
        long hash = 1315423911;

        for (int i = 0; i < str.length(); i++) {
            hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
        }

        return (int)(Math.abs(hash) % bits);
    }

    public void add(int hash) {
        //hash = hash % bitVectorSize;
        int pos = hash / 32;
        int bit = hash % 32;
        vector[pos] = vector[pos] | (1 << bit);
    }

    public boolean search(int hash, String URL) {
        //hash = hash % bitVectorSize;
        int pos = hash / 32;
        int bit = hash % 32;
        if ((vector[pos] & (1 << bit)) == 0) {
            this.uniquetest++;
//            System.out.println(searchUnique(hash3(URL))+" : "+searchUnique(hash4(URL)));
//            System.out.println(URL);
            if (searchUnique(hash3(URL)) && searchUnique(hash4(URL))) {
                addUnique(hash3(URL));
                addUnique(hash4(URL));
                this.unique++;
            }

//			System.out.println(count + " " + URL);
            return true;
        }
        return false;
    }

    public void addUnique(int hash) {
        //hash = hash % bitVectorSize;
        int pos = hash / 32;
        int bit = hash % 32;
        vector2[pos] = vector2[pos] | (1 << bit);
    }

    public boolean searchUnique(int hash) {
        //hash = hash % bitVectorSize;
        int pos = hash / 32;
        int bit = hash % 32;
        if ((vector2[pos] & (1 << bit)) == 0) {
            return true;
        }
        return false;
    }
}