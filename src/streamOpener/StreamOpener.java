package streamOpener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class StreamOpener {
	
	private String strategyName, fileName;
	
	public StreamOpener(String fileName, String strategyName){
		this.fileName = fileName;
		this.strategyName = strategyName;
	}
	
	public static void main (String[] args)
	{
		StreamOpener opener = new StreamOpener("SMA.txt", "SMA");
		
		System.out.println(opener.valueAttick(1));		
		System.out.println(opener.valueAttick(50));
		System.out.println(opener.valueAttick(100));
		
		System.out.println("\n " + StreamOpener.valueAtTick("SMA.txt", 1));
		System.out.println(StreamOpener.valueAtTick("SMA.txt", 50));
		System.out.println(StreamOpener.valueAtTick("SMA.txt", 100));		
	}
	
	public String valueAttick(int tick) {
		String strLine = "";
		File f = new File(fileName);
		if (!f.exists()){
			strLine =  "";
		}
		else{
			try{
			  FileInputStream fstream;
			  fstream = new FileInputStream(fileName);	
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));			  
			  //Read File Line By Line
			  for(int i = 0; i < tick; i++) {
				  try {
					if ((strLine = br.readLine()) == null){
						  strLine = "";
						  break;
					  }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return strLine;
	}
	
	public static String valueAtTick(String file, int tick) {
		String strLine = "";
		File f = new File(file);
		if (!f.exists()){
			strLine =  "";
		}
		else{
			try{
			  FileInputStream fstream;
			  fstream = new FileInputStream(file);	
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));			  
			  //Read File Line By Line
			  for(int i = 0; i < tick; i++) {
				  try {
					if ((strLine = br.readLine()) == null){
						  strLine = "";
						  break;
					  }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return strLine;
	}	
}
