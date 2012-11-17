package scheduling;

import java.util.HashMap;

public class Scheduler {
	
	 private String[] strategies =  {"SMA", "LWMA", "LMA", "TMA"};
	 private String[] managers = {"Manager1", "Manager2", "Manager3", "Manager4", "Manager5", "Manager6", "Manager7"};
	 private HashMap<String, String> [] schedule;
	 private static int HOUR = 3600;
	 
	 public Scheduler(){
		 schedule(0, 2*HOUR, "Manager1", "Manager1", "Manager2", "Manager2");
		 schedule(2*HOUR, HOUR/2,"Manager3", "Manager3", "Manager4", "Manager4");
		 schedule(5/2*HOUR, 3*HOUR/2,"Manager1", "Manager2", "Manager3", "Manager4");
		 schedule(4*HOUR, HOUR/2,"Manager1", "Manager1", "Manager2", "Manager2");
		 schedule(9/2*HOUR, 3/2*HOUR,"Manager3", "Manager3", "Manager4", "Manager4");
		 schedule(6*HOUR, HOUR/2,"Manager3", "Manager4", "Manager5", "Manager5");
		 schedule(13/2*HOUR, 3*HOUR/2,"Manager5", "Manager5", "Manager6", "Manager6");
		 schedule(8*HOUR, HOUR/2,"Manager6", "Manager6", "Manager7", "Manager7");
		 schedule(17/2*HOUR, HOUR/2,"Manager5", "Manager5", "Manager7", "Manager7");
	 }
	 
	 public void schedule(int start, int dt, String M1, String M2, String M3, String M4){
		 while(start<start + dt){
			 schedule[start].put("SMA", M1);
			 schedule[start].put("LWMA", M2);
			 schedule[start].put("EMA", M3);
			 schedule[start].put("TMA", M4);
		 }
	 }
	 
	 
	 
	 
	 public String getManager(int tick, String strategy){
		 return schedule[tick].get(strategy);
	 }
}
