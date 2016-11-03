package wordcount;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Set;

public class DominantCategory {
	public static void main( String[] args ){
//		Map<String,List<Map<String, Integer>>> states = new HashMap<String,List<Map<String, Integer>>>();
		Map<String, Map<String,Integer>> states = new HashMap<String, Map<String, Integer>>();
//		List<Map<String,Integer>> section = new ArrayList<Map<String,Integer>>();
//		Map<String,Integer> category = new HashMap<String,Integer>();
		
		String filename = "midop.txt";
		String line = null;
		
		try{
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader( fileReader );
			while( (line = bufferedReader.readLine()) != null ){
				String l = line.trim().replaceAll( "\t", " " );
				String[] contents = l.split( " " );
				String[] key = contents[0].split( ":" );
//				System.out.println(l);
//				System.out.println(key[0]);
				// we got what we want from a line: state, category and value
				String st = key[0];
				String cat = key[1];
				int value = Integer.parseInt( contents[1] );
				
				if( !states.containsKey(st) ){
					Map<String, Integer> tcat = new HashMap<String, Integer>();
					tcat.put(cat, value);
					states.put(st, tcat);
				}
				else{
					Map<String, Integer> sec = null;
					sec = states.get(st);
					if( !sec.containsKey(cat) ){
						sec.put(cat, value);
					}
					states.put(st, sec);
				}
			}
			bufferedReader.close();
		}
		catch( FileNotFoundException ex ){
			System.out.println( "Unable to open file '" + filename + "'" );
		}
		catch( IOException ex ){
			System.out.println( "Error reading file '" + filename + "'" );
		}
		
		Set set = states.entrySet();
		Iterator itr = set.iterator();
		while( itr.hasNext() ){
			Map<String,Integer> unsorted = null;
			Map.Entry mentry = (Map.Entry)itr.next();
			
			unsorted = (Map<String, Integer>) mentry.getValue();
			
			List<Entry<String, Integer>> list = new LinkedList<Entry<String,Integer>>(unsorted.entrySet());
			Collections.sort(list, new Comparator<Entry<String, Integer>>(){
				public int compare( Entry<String, Integer>o1, Entry<String, Integer> o2 ){
					return o2.getValue().compareTo(o1.getValue());
				}
			});
			
			Map<String,Integer> sortedMap = new LinkedHashMap<String, Integer>();
			for( Entry<String, Integer> entry: list ){
				sortedMap.put(entry.getKey(), entry.getValue());
			}
			
//			System.out.println(mentry.getKey());
//			System.out.println(sortedMap);
			
			states.put((String) mentry.getKey(), sortedMap);
		}
		
		int cnt_agri = 0;
		int cnt_edu = 0;
		int cnt_pol = 0;
		int cnt_spo = 0;
		
		Set st = states.entrySet();
		Iterator iter = set.iterator();
		while( iter.hasNext() ){
			Map.Entry me = (Entry) iter.next();
			Map<String, Integer> child = null;
			
			child = (HashMap<String, Integer>) me.getValue();
			Map.Entry me2 = child.entrySet().iterator().next();
			
			if( me2.getKey().equals("agriculture") ){
				cnt_agri += 1;
			}
			else if( me2.getKey().equals("education") ){
				cnt_edu += 1;
			}
			else if( me2.getKey().equals("politics") ){
				cnt_pol += 1;
			}
			else if( me2.getKey().equals("sports") ){
				cnt_spo += 1;
			}
//			System.out.println( me.getKey() + " -> " + me2.getKey() + " : " + me2.getValue() );
		}
		
		System.out.println( "Number of states having Agriculture dominant: " + cnt_agri );
		System.out.println( "Number of states having Education dominant: " + cnt_edu );
		System.out.println( "Number of states having Sports dominant: " + cnt_spo );
		System.out.println( "Number of states having Politics dominant: " + cnt_pol );
		// this is q.1 done
		
//		id-allocation
//		agriculture 1
//		sports 2
//		politics 3
//		education 4
		
		Map<String,String> q2_fmt = new HashMap<String,String>();
		Set set1 = states.entrySet();
		Iterator itr1 = set1.iterator();
		while( itr1.hasNext() ){
			Map.Entry me = (Map.Entry)itr1.next();
			Map<String, Integer> child = null;
			
			child = (Map<String, Integer>) me.getValue();
			String comb = "";
			
			Set s = child.entrySet();
			Iterator i = s.iterator();
			while( i.hasNext() ){
				Map.Entry m = (Map.Entry)i.next();
//				System.out.println(m.getKey());
				if( m.getKey().equals("agriculture") ){
					comb += "1";
				}
				else if( m.getKey().equals("sports") ){
					comb += "2";
				}
				else if( m.getKey().equals("politics") ){
					comb += "3";
				}
				else if( m.getKey().equals("education") ){
					comb += "4";
				}
				else{
//					System.out.println("here");
					continue;
				}
			}
			q2_fmt.put((String) me.getKey(), comb );
			comb = "";
		}
//		System.out.println(q2_fmt);
		
//		id-allocation
//		agriculture 1
//		sports 2
//		politics 3
//		education 4
		
		Multimap<String, String> multimap = HashMultimap.create();
		for( Entry<String, String> entry: q2_fmt.entrySet()){
			String dfmt = entry.getValue();
			String fmt = "";
			
			for( int i=0; i<dfmt.length(); i++ ){
				if( dfmt.charAt(i) == '1' ){
					fmt += "agriculture > ";
				}
				else if( dfmt.charAt(i) == '2' ){
					fmt += "sports > ";
				}
				else if( dfmt.charAt(i) == '3' ){
					fmt += "politics > ";
				}
				else if( dfmt.charAt(i) == '4' ){
					fmt += "education > ";
				}
			}
			fmt = fmt.substring( 0, fmt.length()-3 );
			multimap.put(fmt, entry.getKey());
		}
		
		System.out.println( "\nFollowing are the states having same ranks: ");
		System.out.println(multimap);
		
//		for( Entry<String,Collection<String>> entry: multimap.asMap().entrySet()){
//			System.out.println( entry.getKey() + "::-> " + entry.getValue());
//		}
//		System.out.println(states);
	}
}
