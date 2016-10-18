package Bigrams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

public class Bigrams {

	//using bigrams
		public static void main(String[] args) {
			
			
			ArrayList<String> features = new ArrayList<String>();
			features = readFeature();

			
			//calculate unigrams and MI
			LinkedHashMap<String, Double> unigramMap = new LinkedHashMap<String,Double>();
			for(String gram:features){
				MI feat = new MI(gram);
				unigramMap.put(gram, feat.getMI());
			}
			
			//generate bigrams of all possibilities
			ArrayList<String> bigrams = new ArrayList<String>();
			for(int i = 1;i<features.size();i++){
//				for(int j = 1;i+j<features.size();j++){
//					String temp = features.get(i)+" " +features.get(i+j);
//					System.out.println(temp);
//					bigrams.add(temp);
//				}
				
				String tem = features.get(i)+" " +features.get(i-1);
				bigrams.add(tem);
				
			}
			System.out.println("TOTAL NUMBER OF GENERATED BIGRAMS: " + bigrams.size());
			
			//calculate MI for each bigram then compare with max of the single MIs
			LinkedHashMap<String, Double> bigramMap = new LinkedHashMap<String,Double>();
			for(String gram:bigrams){
				MI newFeature = new MI(gram);
				bigramMap.put(gram,newFeature.getMI());
				System.out.println(gram+" "+ newFeature.getMI());
			}
			
			
			//only keep bigrams with MI higher than max of both components
			//otherwise add the two seperate components
			ArrayList<String> feats = new ArrayList<String>();
			for(String bigram:bigrams){
				String[] comps = bigram.split(" ");
				String comp1 = comps[0];
				String comp2 = comps[1];
				double max = Math.max(unigramMap.get(comp1), unigramMap.get(comp2));
				double bi = bigramMap.get(bigram);
				System.out.println("bigram MI: "+bi +" and larger MI of unigram: " + max);
				if(bi<=max){
					feats.add(comp1);
					
					System.out.println("adding " + comp1);
					feats.add(comp2);
					System.out.println("adding " + comp2);
				}else{
					feats.add(bigram);
					System.out.println("adding " + bigram);
				}
			}

			System.out.println("total number of features left: " + feats.size());
			writeOutput(feats);

		}
		
		
		
		
		
		public static ArrayList<String> readFeature(){
			//read the features.txt
			ArrayList<String> features = new ArrayList<String>();
			BufferedReader input = null;
			try{
				input = new BufferedReader(new FileReader("feature35.txt"));
				
				String str = null;
				while((str =input.readLine())!=null){
					features.add(str);
				}
				System.out.println("FINISH READING FEATURES, and number of features: " + features.size());
			}catch(IOException e){
				System.out.println("cannot open file...");
			}

			try {
				input.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return features;
			
		}
		
		
		public static void writeOutput(ArrayList<String> features){
			//write output
			
					PrintWriter out = null;
					try{
						out = new PrintWriter(new FileOutputStream(new File("output-featureBi.txt"),true));
					}catch(FileNotFoundException e){
						System.out.println("Error writing to file.");
					}
					for(int i=0;i<features.size();i++){
						out.println(features.get(i));
					}
					
					out.close();
		}
}
