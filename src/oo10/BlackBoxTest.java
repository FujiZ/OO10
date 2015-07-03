package oo10;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BlackBoxTest {
	
	private static File configFile;
	private static File badConfigFile;
	private static BlackBox blackbox;
	
	private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@BeforeClass
	public static void setUp() throws Exception {
		configFile=new File("./config.ini");
		badConfigFile=new File("./badConfig.ini");
		if(configFile.exists()){
			configFile.delete();
		}
		configFile.createNewFile();
		FileWriter fw=new FileWriter(configFile);
		fw.write("(3,3,2)\nS0\nS2\n#\nR0\n#\n(S0,T0)\n(T0,R0)\n(T0,T1)\n(T1,T0)\n(T1,R1)\n(S1,T1)\n#");
		fw.close();
		
		if(badConfigFile.exists()){
			badConfigFile.delete();
		}
		badConfigFile.createNewFile();
		fw=new FileWriter(badConfigFile);
		fw.write("(3,3,2)123\nS0\nS2\n#\nR0\n#\n(S0,T0)\n(T0,R0)\n(T0,T1)\n(T1,R1)\n(S1,T1)\n#");
		fw.close();
		
		blackbox=BlackBox.initBlackBox(configFile);
	}
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		configFile.delete();
		badConfigFile.delete();
	}

	@Test
	//普通情况
	public void testSendInfor0() throws InterruptedException {
		//fail("Not yet implemented");
		Pattern pattern=Pattern.compile("R1 received: \\(S1,R1,\"hello world\",.*\\)");
		blackbox.sendInfor("S1,0,\"hello world\",R1,");
		Thread.sleep(100);
		Matcher matcher=pattern.matcher(outContent.toString());
		assertTrue(matcher.find());
	}

	@Test
	//加密情况1
	public void testSendInfor1() throws InterruptedException {
		//fail("Not yet implemented");
		Pattern pattern=Pattern.compile("R0 received: \\(S0,R0,\"hello world\",.*\\)");
		blackbox.sendInfor("S0,1,\"hello world\",R0,");
		Thread.sleep(100);
		Matcher matcher=pattern.matcher(outContent.toString());
		assertTrue(matcher.find());
	}
	
	@Test
	//加密情况2
	public void testSendInfor2() throws InterruptedException {
		//fail("Not yet implemented");
		Pattern pattern=Pattern.compile("R1 received: \\(S0,R1,\"ehll oowlrd\",.*\\)");
		blackbox.sendInfor("S0,1,\"hello world\",R1,");
		Thread.sleep(100);
		Matcher matcher=pattern.matcher(outContent.toString());
		assertTrue(matcher.find());
	}
	
	@Test
	//加密情况3
	public void testSendInfor3() throws InterruptedException {
		//fail("Not yet implemented");
		Pattern pattern=Pattern.compile("R0 received: \\(S1,R0,\"hello world\",.*\\)");
		blackbox.sendInfor("S1,1,\"hello world\",R0,");
		Thread.sleep(100);
		Matcher matcher=pattern.matcher(outContent.toString());
		assertTrue(matcher.find());
	}
	
	@Test
	//加密情况4
	public void testSendInfor4() throws InterruptedException {
		//fail("Not yet implemented");
		Pattern pattern=Pattern.compile("R1 received: \\(S1,R1,\"hello world\",.*\\)");
		blackbox.sendInfor("S1,1,\"hello world\",R1,");
		Thread.sleep(100);
		Matcher matcher=pattern.matcher(outContent.toString());
		assertTrue(matcher.find());
	}
	
	@Test
	//孤立Sender节点
	public void testSendInfor5() throws InterruptedException {
		//fail("Not yet implemented");
		blackbox.sendInfor("S2,0,\"hello world\",R0,");
		Thread.sleep(100);
		assertEquals("", outContent.toString());
		
	}
	
	@Test
	//从不存在的节点发送
	public void testSendInfor6() throws InterruptedException {
		//fail("Not yet implemented");
		blackbox.sendInfor("S3,0,\"hello world\",R0,");
		Thread.sleep(100);
		assertEquals("Error: Index out of bound: S3\n", outContent.toString());
		
	}
	
	@Test
	//从不存在的节点接收
	public void testSendInfor7() throws InterruptedException {
		//fail("Not yet implemented");
		blackbox.sendInfor("S0,0,\"hello world\",R2,");
		Thread.sleep(100);
		assertEquals("Error: Index out of bound: R2\n", outContent.toString());
		
	}
	
	@Test
	//语法错误1
	public void testSendInfor8() throws InterruptedException {
		//fail("Not yet implemented");
		blackbox.sendInfor("S0,0,\"hello world\",R0");
		Thread.sleep(100);
		assertEquals("Invalid arg: S0,0,\"hello world\",R0\n", outContent.toString());
	}
	
	@Test
	//语法错误2
	public void testSendInfor9() throws InterruptedException {
		//fail("Not yet implemented");
		blackbox.sendInfor("S0,0,\"hello world\",R0");
		Thread.sleep(100);
		assertEquals("Invalid arg: S0,0,\"hello world\",R0\n", outContent.toString());
	}
	
	@Test
	public void testShowTopo() {
		//fail("Not yet implemented");
		blackbox.showTopo();
		assertNotEquals("", outContent.toString());
	}

	@Test
	public void testPing() {
		//fail("Not yet implemented");
		blackbox.ping();
		assertNotEquals("", outContent.toString());
	}

	@Test
	public void testLs_n() {
		//fail("Not yet implemented");
		blackbox.ls_n();
		assertEquals("(Sender: 3,Transponder: 3,Receiver: 2)\n", outContent.toString());
	}

	@Test
	public void testLs_sender() {
		//fail("Not yet implemented");
		blackbox.ls_sender();
		assertEquals("S0:Encryptable\nS1:DisEncryptable\nS2:Encryptable\n", outContent.toString());
	}

	@Test
	public void testLs_transponder() {
		//fail("Not yet implemented");
		blackbox.ls_transponder();
		assertNotEquals("", outContent.toString());
	}

	@Test
	public void testLs_receiver() {
		//fail("Not yet implemented");
		blackbox.ls_receiver();
		assertEquals("R0:Decipherable\nR1:DisDecipherable\n", outContent.toString());
	}

	@Test(expected = NotConfigFileException.class)
	public void testInitBlackBox() throws IOException, NotConfigFileException {
		//fail("Not yet implemented");
		BlackBox.initBlackBox(badConfigFile);
	}

}
