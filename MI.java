package Bigrams;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


//this class calculates the MI of a feature contributing to a city
//create an instance of the class with feature as the argument,  call getMI()
public class MI {


	private String feature;

	public static int bos,hous,sea,san,w = 0;//individual count
	public static int notBosF,notHousF,notSeaF,notSanF,notWF = 0;//not joint count with each class
	public static int bosF,housF,seaF,sanF,wF = 0;//joint count with each class
	public static int total = 0;//total instances	

	public static int featureCount = 0;

	public MI(String feature){
		this.feature = feature;
	}
	
	
	public void read(){
		//read in the text file
				Scanner in = null;
				try{
					in = new Scanner(new FileInputStream("train-tweets.txt"));
					String[] tokens;
					while(in.hasNextLine()){
						String line = in.nextLine();
						total++;
						String[] contents =line.split("\t");
						String content = contents[2];
						String classifier = contents[3];
						content = content.toLowerCase();
						content = content.replaceAll("[^a-zA-Z ]", "");
						
						//counts class freq
						if(classifier.equals("B")){
							bos++;
						}else if(classifier.equals("SD")){
							hous++;
						}else if(classifier.equals("W")){
							sea++;
						}else if(classifier.equals("H")){
							san++;
						}else if(classifier.equals("Se")){
							w++;
						}

						
						if(feature.contains(" ")){
							//this is a bigram
							String[] bi = feature.split(" ");
							String token1 = bi[0];
							String token2 = bi[1];

							
							//count feature freq
							if(content.contains(token1) && content.contains(token2)){
								featureCount++;
							}

							//count joint 
							if(content.contains(token1) && content.contains(token2)&&classifier.equals("B")){
								bosF++;
							}
							if(content.contains(token1) && content.contains(token2) &&classifier.equals("H")){
								housF++;
							}
							if(content.contains(token1) && content.contains(token2) &&classifier.equals("SD")){
								sanF++;
							}
							if(content.contains(token1) && content.contains(token2) &&classifier.equals("SE")){
								seaF++;
							}
							if(content.contains(token1) && content.contains(token2) &&classifier.equals("W")){
								wF++;
							}
							//not joint
							if(!(content.contains(token1) && content.contains(token2)) &&classifier.equals("B")){
								notBosF++;
							}
							if(!(content.contains(token1) && content.contains(token2)) &&classifier.equals("H")){
								notHousF++;
							}
							if(!(content.contains(token1) && content.contains(token2))&&classifier.equals("SD")){
								notSanF++;
							}
							if(!(content.contains(token1) && content.contains(token2))&&classifier.equals("SE")){
								notSeaF++;
							}
							if(!(content.contains(token1) && content.contains(token2))&&classifier.equals("W")){
								notWF++;
							}
							
							
						}else{
							//unigram

							//count feature freq
							if(content.contains(feature)){
								featureCount++;
							}

							//count joint 
							if(content.contains(feature)&&classifier.equals("B")){
								bosF++;
							}
							if(content.contains(feature)&&classifier.equals("H")){
								housF++;
							}
							if(content.contains(feature)&&classifier.equals("SD")){
								sanF++;
							}
							if(content.contains(feature)&&classifier.equals("SE")){
								seaF++;
							}
							if(content.contains(feature)&&classifier.equals("W")){
								wF++;
							}
							//not joint
							if(!content.contains(feature)&&classifier.equals("B")){
								notBosF++;
							}
							if(!content.contains(feature)&&classifier.equals("H")){
								notHousF++;
							}
							if(!content.contains(feature)&&classifier.equals("SD")){
								notSanF++;
							}
							if(!content.contains(feature)&&classifier.equals("SE")){
								notSeaF++;
							}
							if(!content.contains(feature)&&classifier.equals("W")){
								notWF++;
							}
							
						}




					}
				}catch(IOException e){
					System.out.println("cannot open file...");
				}

				in.close();		
				
	}


	public double getMI(){
		read();
		System.out.println("calculating MI for " + feature);
		double mi = getJoint("Boston")*getLog("Boston")
				+getJoint("Houston")*getLog("Houston")
				+getJoint("Seattle")*getLog("Seattle")
				+getJoint("San diego")*getLog("San diego")
				+getJoint("washington")*getLog("washington");
		System.out.println("MI for "+ feature + " is " + mi);
		return mi;
	}
	
	public double getLog(String city){
	
		double log = 0.0;
		
		if(getJoint(city)!=0){
			log =(double)log(getJoint(city)/(getFeatP()*getClassP(city)));
		}
		//System.out.println("log is: " + log+" while joint is " + getJoint(city) + " and featp, classp are " + getFeatP() +" " + getClassP(city));
		
		return log;
	}
	
	public double log(double x){
		//            log[10]x
		// log[2]x = ----------
		//            log[10]2
		int base = 2;
		return Math.log10(x)/Math.log10(base);
		
	}
	
	public double getJoint(String city){
		double joint = 0.0;
		if(city.equalsIgnoreCase("Boston")){
			joint = (double)bosF/total;
		}else if(city.equalsIgnoreCase("Houston")){
			joint = (double)housF/total;
		}else if(city.equalsIgnoreCase("Seattle")){
			joint = (double)seaF/total;
		}else if(city.equalsIgnoreCase("san diego")){
			joint = (double)sanF/total;
		}else if(city.equalsIgnoreCase("washington")){
			joint = (double)wF/total;
		}
		
		//System.out.println("joint prob is " +joint);
		return joint;
		
	}
	
	public double getNotJoint(String city){
		double joint = 0.0;
		if(city.equalsIgnoreCase("Boston")){
			joint = (double)notBosF/total;
		}else if(city.equalsIgnoreCase("Houston")){
			joint = (double)notHousF/total;
		}else if(city.equalsIgnoreCase("Seattle")){
			joint = (double)notSeaF/total;
		}else if(city.equalsIgnoreCase("san diego")){
			joint = (double)notSanF/total;
		}else if(city.equalsIgnoreCase("washington")){
			joint = (double)notWF/total;
		}
		return joint;
	}
	
	
	
	public double getFeatP(){
		return (double)featureCount/total;
	}
	
	public double getNotFeatP(){
		return 1-(double)featureCount/total;
	}
	
	public double getClassP(String city){
		double pclass = 0.0;
		if(city.equalsIgnoreCase("Boston")){
			pclass = (double)bos/total;
		}else if(city.equalsIgnoreCase("Houston")){
			pclass = (double) hous/total;
		}else if(city.equalsIgnoreCase("Seattle")){
			pclass = (double)sea/total;
		}else if(city.equalsIgnoreCase("san diego")){
			pclass = (double)san/total;
		}else if(city.equalsIgnoreCase("washington")){
			pclass = (double)w/total;
		}
		
		return pclass;
	}
	



}
