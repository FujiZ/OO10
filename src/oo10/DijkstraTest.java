package oo10;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class DijkstraTest {
	private static Sender[] sender=new Sender[3];
	private static Transponder[] transponder=new Transponder[3];
	private static Receiver[] receiver=new Receiver[3];
	private static Set<TransNode> tSet=new HashSet<TransNode>();
	private Dijkstra dijkstra;
	
	@BeforeClass
	public static void setUp() throws Exception {
		for(int i=0;i<3;++i){
			sender[i]=new Sender(i);
			transponder[i]=new Transponder(i);
			receiver[i]=new Receiver(i);
			sender[i].link(transponder[i], 1);
			transponder[i].link(receiver[i], 1);
			tSet.add(transponder[i]);
			tSet.add(receiver[i]);
		}
		for(int i=0;i<3;++i){
			transponder[i].link(transponder[(i+1)%3], 1);
		}
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun0() {
		//fail("Not yet implemented");
		dijkstra=new Dijkstra(sender[0], tSet);
		assertNotEquals("", dijkstra.run().toString());
	}

	@Test
	public void testRun1() {
		//fail("Not yet implemented");
		dijkstra=new Dijkstra(sender[1], tSet);
		assertNotEquals("", dijkstra.run().toString());
	}
	
	@Test
	public void testRun2() {
		//fail("Not yet implemented");
		dijkstra=new Dijkstra(sender[2], tSet);
		assertNotEquals("", dijkstra.run().toString());
	}
	
}
