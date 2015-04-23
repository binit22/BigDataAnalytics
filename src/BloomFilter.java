import java.io.File;
import java.util.Scanner;


public class BloomFilter {

	public static final int bits = 131072;
	public static int bitVectorSize = bits/32;
	public static int[] vector = new int[bitVectorSize];
	
	// simple summation function
	public static int hash1(String x){

		char ch[];
		ch = x.toCharArray();
		int i, sum;
		for (sum=0, i=0; i < x.length(); i++)
			sum += ch[i];
		return Math.abs(sum) % bits;
	}

	// Use folding on a string, summed 4 bytes at a time
	public static int hash2(String s){

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
	public static int hash3(String s){
		int h = 0;
		for(int i = s.length()-1; i >= 0; i--){
			h = (s.charAt(i) + 128*h) % bits;
		}
		return h;
	}

	// djb2
	public static int hash4(String str){
		long hash = 5381;
		int c;
		for (int i=0; i < str.length(); i++){
			c = str.charAt(i);
			hash = ((hash << 5) + hash) + c; /* hash * 33 + c */
		}
		return (int)(Math.abs(hash) % bits);
	}

	// sdbm
	public static int hash5(String str){
		long hash = 0;
		int c;
		for (int i=0; i < str.length(); i++){
			c = str.charAt(i);
			hash = c + (hash << 6) + (hash << 16) - hash;
		}
		return (int)(Math.abs(hash) % bits);
	}

	// lose lose
	public static int hash6(String str){
		int hash = 0;
		int c;
		for (int i=0; i < str.length(); i++){
			c = str.charAt(i);
			hash += c;
		}
		return (Math.abs(hash) % bits);
	}

	// DEK hash
	public static int hash7(String str){
		long hash = str.length();

		for(int i = 0; i < str.length(); i++)
		{
			hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
		}

		return (int)(Math.abs(hash) % bits);
	}

	// RS hash
	public static int hash8(String str){
		int b = 378551;
		int a = 63689;
		long hash = 0;

		for(int i = 0; i < str.length(); i++)
		{
			hash = hash * a + str.charAt(i);
			a = a * b;
		}

		return (int)(Math.abs(hash) % bits);
	}

	// JS hash
	public static int hash9(String str){
		long hash = 1315423911;

		for(int i = 0; i < str.length(); i++)
		{
			hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
		}

		return (int)(Math.abs(hash) % bits);
	}

	public static void main(String[] args) {

		File file1 = new File(args[0]);
		File file2 = new File(args[1]);

		Scanner sc = null;
		try{
			sc = new Scanner(file1);
			int count = 0;
			while(sc.hasNext()){
				String URL = sc.nextLine();
				URL = URL.substring(URL.indexOf("\"")+4).trim(); //, URL.indexOf("HTTP")
				count++;
				//				System.out.println(count + " " + URL);

				int hash = hash1(URL);
				int pos = hash % bitVectorSize;
				int bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash2(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash3(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash4(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash5(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash6(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash7(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash8(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);

				hash = hash9(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				vector[pos] = vector[pos] | (1 << bit);
			}
			System.out.println(count);

			count = 1;
			sc = new Scanner(file2);
			while(sc.hasNext()){
				String URL = sc.nextLine();
				URL = URL.substring(URL.indexOf("\"")+4).trim(); // , URL.indexOf("HTTP")
				count++;

				int hash = hash1(URL);
				int pos = hash % bitVectorSize;
				int bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash2(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash3(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash4(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash5(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash6(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash7(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash8(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}

				hash = hash9(URL);
				pos = hash % bitVectorSize;
				bit = hash % 32;
				if((vector[pos] & (1 << bit)) == 0){
					System.out.println(count + " " + URL);
					continue;
				}
			}
			System.out.println(count);

		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
