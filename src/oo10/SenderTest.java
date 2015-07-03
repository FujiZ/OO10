package oo10;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SenderTest {
	private static Sender sender;
	private static Transponder transponder;
	private static Receiver receiver;
	private static ByteArrayOutputStream outContent;
	
	@BeforeClass
	public static void init() throws Exception {
		sender=new Sender(0);
		transponder=new Transponder(0);
		receiver=new Receiver(0);
		outContent=new ByteArrayOutputStream();
		sender.link(transponder, 1);
		transponder.link(receiver, 1);
		sender.route.put(receiver, transponder);
		transponder.route.put(receiver, receiver);
		ExecutorService executorService=Executors.newFixedThreadPool(3);
		executorService.execute(sender);
		executorService.execute(transponder);
		executorService.execute(receiver);
		executorService.shutdown();
	}
	
	@Before
	public void setUp(){
		System.setOut(new PrintStream(outContent));
	}
	@After
	public void tearDown() throws Exception {
		System.setOut(null);
	}

	@Test
	public void testLs() {
		//fail("Not yet implemented");
		assertEquals("S0:DisEncryptable", sender.ls());
	}

	@Test
	public void testShowTopo() {
		//fail("Not yet implemented");
		assertEquals("S0:(T0,1)", sender.showTopo());
	}

	@Test
	public void testPing() {
		//fail("Not yet implemented");
		assertEquals("S0:R0 ", sender.ping());
	}

	@Test
	public void testSendInfor() throws InterruptedException {
		//fail("Not yet implemented");
		Calendar timeCalendar=Calendar.getInstance();
		Infor infor=new Infor(sender, receiver, "hello", false, timeCalendar);
		sender.sendInfor(infor);
		Thread.sleep(100);
		assertEquals("(S0,R0,\"hello\",T0,"+timeCalendar.getTime()+")", infor.toString());
	}
	
	@Test
	public void testGetId() {
		//fail("Not yet implemented");
		assertEquals(0, (int)sender.getId());
	}

}
