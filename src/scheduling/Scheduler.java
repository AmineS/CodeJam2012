package scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scheduler {

	private String[] strategies = { "SMA", "LWMA", "EMA", "TMA" };

	List<HashMap<String, String>> schedule;
	private static int HOUR = 2; // hour = 2*block
	private static int DAY = 9; // 9 hours
	private static int halfHour = 1800;

	public Scheduler() {
		init();

		schedule(0, 2 * HOUR, "Manager1", "Manager1", "Manager2", "Manager2");
		schedule(2 * HOUR, HOUR / 2, "Manager3", "Manager3", "Manager4",
				"Manager4");
		schedule(5 * HOUR / 2 , 3 * HOUR / 2, "Manager1", "Manager2",
				"Manager3", "Manager4");
		schedule(4 * HOUR, HOUR / 2, "Manager1", "Manager1", "Manager2",
				"Manager2");
		schedule(9 * HOUR / 2, 3 * HOUR / 2 , "Manager3", "Manager3",
				"Manager4", "Manager4");
		schedule(6 * HOUR, HOUR / 2, "Manager3", "Manager4", "Manager5",
				"Manager5");
		schedule(13 * HOUR / 2 , 3 * HOUR / 2, "Manager5", "Manager5",
				"Manager6", "Manager6");
		schedule(8 * HOUR, HOUR / 2, "Manager6", "Manager6", "Manager7",
				"Manager7");
		schedule(17 * HOUR / 2 , HOUR / 2, "Manager5", "Manager5", "Manager7",
				"Manager7");
	}

	public void init() {
		this.schedule = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 2 * DAY; i++) {
			schedule.add(new HashMap<String, String>());
		}
	}

	public void schedule(int start, int dt, String M1, String M2, String M3,
			String M4) {
		int i = start;
		while (i < start + dt) {
			schedule.get(i).put("SMA", M1);
			schedule.get(i).put("LWMA", M2);
			schedule.get(i).put("EMA", M3);
			schedule.get(i).put("TMA", M4);
			i++;
		}
	}

	public List<Integer> getManagerSchedule(String manager) {
		boolean working = false;
		List<Integer> work = new ArrayList<Integer>(4);
		for (int i = 0; i < 2 * DAY; i++) {
			HashMap<String, String> map = schedule.get(i);
			if (map.containsValue(manager) & !working ) {
				work.add(i);
				working = true;
			}else if((!map.containsValue(manager)) & working){
				working = false;
				work.add(i-1);
			}else if((i==2*DAY-1) & working){
				work.add(i);
			}
		}
		return work;
	}

	public String getManager(int tick, int strategyIndex) {
		int index = tick / halfHour;
		return schedule.get(index).get(strategies[strategyIndex]);
	}
	
	public static void main(String args[]){
		 String[] managers = { "Manager1", "Manager2", "Manager3",
			"Manager4", "Manager5", "Manager6", "Manager7" };
		 Scheduler s = new Scheduler();
		 for(String m: managers){
			 System.out.println(printList(s.getManagerSchedule(m)));
		 }
	}
	
	public static String printList(List<Integer> l){
		String s = "[";
		for(int n: l){
			s += n+"  ";
		}
		s+= "]";
		return s;
	}
}
