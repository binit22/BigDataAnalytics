import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class BloomFilter {

	public static final int bits = (int)Math.pow(2, 24);
	
	public static int bitVectorSize = bits/32;
	public static int[] vector = new int[bitVectorSize];
	public static int[] vectornew = new int[bitVectorSize];
	public int unique = 0;

	
	public int hash1(String str){
		long hash = 5381;
		int letter;
		for (int i = 0; i < str.length(); i++){
			letter = str.charAt(i);
			hash = ((hash << 5) + hash) + letter;
		}
		return (int)(Math.abs(hash) % bits);
	}

	
	public int hash2(String str){
		int p = 378551;
		int q = 63689;
		long hash = 0;

		for(int i = 0; i < str.length(); i++)
		{
			hash = hash * q + str.charAt(i);
			q = q * p;
		}
		return (int)(Math.abs(hash) % bits);
	}

	public void add(int hash){
		int pos = hash / 32;
		int bit = hash % 32;
		vector[pos] = vector[pos] | (1 << bit);	
	}

	public boolean search(int hash){
		int pos = hash / 32;
		int bit = hash % 32;
		if((vector[pos] & (1 << bit)) == 0){
			return true;
		}
		return false;
	}

	public void addnew(int hash){
		int pos = hash / 32;
		int bit = hash % 32;
		vectornew[pos] = vectornew[pos] | (1 << bit);	
	}

	public boolean searchnew(int hash){
		int pos = hash / 32;
		int bit = hash % 32;
		if((vectornew[pos] & (1 << bit)) == 0){
			return true;
		}
		return false;
	}

	public static void main(String[] args) {

		BloomFilter filter = new BloomFilter();

		File file1 = new File(args[0]);
		File file2 = new File(args[1]);

		BufferedReader br = null;
		try{
			Set<String> old = new HashSet<String>();
			br = new BufferedReader(new FileReader(file1));
			int count = 0;
			String URL = null;
			while((URL = br.readLine()) != null){
				URL = URL.split("\\s+")[6].trim();
				old.add(URL);
				count++;
				filter.add(filter.hash1(URL));
				filter.add(filter.hash2(URL));
			}
			System.out.println(count);
			System.out.println("Unique old " + old.size());
			
			count = 0;
			Set<String> latest = new HashSet<String>();
			br = new BufferedReader(new FileReader(file2));
			while((URL = br.readLine()) != null){
				URL = URL.split("\\s+")[6].trim();
				if(!old.contains(URL)){
					latest.add(URL);
					old.add(URL);
				}
				if(filter.search(filter.hash1(URL)) || filter.search(filter.hash2(URL))){
					count++;
//					System.out.println(URL);
					if(filter.searchnew(filter.hash1(URL)) || filter.searchnew(filter.hash2(URL))){
						filter.addnew(filter.hash1(URL));
						filter.addnew(filter.hash2(URL));
						filter.unique++;
					}
				}
			}
			System.out.println("Unique using hashset " + latest.size());
			System.out.println("All new " + count);
			System.out.println("Unique using filter " + filter.unique);
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
