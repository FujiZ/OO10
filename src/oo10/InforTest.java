package oo10;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InforTest {
	private Infor infor;
	private Sender sender;
	private Transponder transponder;
	private Receiver receiver;
	private Calendar time;
	
	@Before
	public void setUp() throws Exception {
		sender=new Sender(0);
		transponder=new Transponder(0);
		receiver=new Receiver(0);
		time=Calendar.getInstance();
		infor=new Infor(sender, receiver, "hello", true, time);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetReceiver() {
		//fail("Not yet implemented");
		assertEquals(receiver, infor.getReceiver());
	}

	@Test
	public void testIsEncrypted() {
		//fail("Not yet implemented");
		assertEquals(true, infor.isEncrypted());
	}

	@Test
	public void testSetEncrypted() {
		//fail("Not yet implemented");
		infor.setEncrypted(false);
		assertEquals(false, infor.isEncrypted());
	}

	@Test
	public void testGetText() {
		//fail("Not yet implemented");
		assertEquals("hello", infor.getText());
	}

	@Test
	public void testSetText() {
		//fail("Not yet implemented");
		infor.setText("world");
		assertEquals("world", infor.getText());
	}

	@Test
	public void testAddTrace() {
		//fail("Not yet implemented");
		infor.addTrace(transponder);
		assertEquals("("+sender+","+receiver+","+"\"hello\","+transponder+","+time.getTime()+")",infor.toString());
	}

}
