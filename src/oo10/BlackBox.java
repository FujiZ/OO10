package oo10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BlackBox {
	//OVERVIEW:密室，管理所有通信工具，并提供对外的接口
	private ArrayList<Sender> senderList;
	private ArrayList<Transponder> transponderList;
	private ArrayList<Receiver> receiverList;
	
	private Set<TransNode> tSet;//除sender外所有节点
	
	private static final Pattern linkPattern=Pattern.compile("^\\(([ST])(\\d{1,3}),([TR])(\\d{1,3})\\)$");
	private static final Pattern inforPattern=Pattern.compile("^S(\\d{1,3}),([01]),\"([A-Za-z0-9 ]+)\",(R(\\d{1,3}),)+$");
	
	private static final String senderType="S";
	private static final String transponderType="T";
	private static final String receiverType="R";
	
	private BlackBox(int senderNum,int transponderNum,int receiverNum){
		//Requires:senderNum>0,transponderNum>0,receiverNum>0;
		//Modifies:this
    	//Effects:初始化对象，并启动设备线程
		senderList=new ArrayList<Sender>(senderNum);
		transponderList=new ArrayList<Transponder>(transponderNum);
		receiverList=new ArrayList<Receiver>(receiverNum);
		tSet=new HashSet<TransNode>();
		ExecutorService exec=Executors.newFixedThreadPool(senderNum+transponderNum+receiverNum);
		//初始化sender
		for(int i=0;i<senderNum;++i){
			Sender temSender=new Sender(i);
			exec.execute(temSender);
			senderList.add(temSender);
		}
		//初始化transponder
		for(int i=0;i<transponderNum;++i){
			Transponder temTransponder=new Transponder(i);
			exec.execute(temTransponder);
			transponderList.add(temTransponder);
			tSet.add(temTransponder);
		}
		//初始化receiver
		for(int i=0;i<receiverNum;++i){
			Receiver temReceiver=new Receiver(i);
			exec.execute(temReceiver);
			receiverList.add(temReceiver);
			tSet.add(temReceiver);
		}
		exec.shutdown();
	}
	
	public void sendInfor(String str) throws InterruptedException{
		//Requires：str!=null
    	//Modifies:none
    	//Effects:若str为传输命令，则传给对应sender；否则抛出异常
		try {
			Calendar time=Calendar.getInstance();
			LinkedList<Infor> inforList=new LinkedList<Infor>();
			//先分词，并将所有空字符串删除
			ArrayList<String> argList=cleanEmptyStr(str.split("\\|"));
			//检查所有arg，只要有一个不满足就报错
			Iterator<String> argIterator=argList.iterator();
			while(argIterator.hasNext()){
				String arg=argIterator.next();
				Matcher inforMatcher=inforPattern.matcher(arg);
				if(inforMatcher.find()){
					Sender sender=getSender(Integer.parseInt(inforMatcher.group(1)));
					boolean encrypted=((inforMatcher.group(2).equals("1"))? true:false);
					String text=inforMatcher.group(3);
					Set<Receiver> receiverSet=new HashSet<Receiver>();
					inforMatcher.reset(arg);
					for(int length=arg.length();inforMatcher.find();){
						receiverSet.add(getReceiver(Integer.parseInt(inforMatcher.group(5))));
						length-=inforMatcher.group(4).length();
						inforMatcher.reset(arg.substring(0, length));
					}
					//遍历set，将新建的Infor包加入列表中
					for(Receiver receiver:receiverSet)
						inforList.add(new Infor(sender, receiver, text, encrypted,time));
				}
				else
					throw new InvalidArgException("Invalid arg: "+arg);
			}
			//将list中所有的infor包发送出去
			if(inforList.size()>0){
				ExecutorService exec=Executors.newFixedThreadPool(inforList.size());
				for(Infor infor:inforList)
					exec.execute(infor);
				exec.shutdown();
			}
		}
		catch (IndexOutOfBoundsException e) {
			System.out.print(e.getMessage()+"\n");
		}
		catch (InvalidArgException e) {
			System.out.print(e.getMessage()+"\n");
		}
	}
	
	public void showTopo(){
		//Requires：senderList!=null,transponderList!=null,receiverList!=null
    	//Modifies:none
    	//Effects:输出设备节点的拓扑结构
		for(TransNode node:senderList)
			System.out.print(node.showTopo()+"\n");
		for(TransNode node:transponderList)
			System.out.print(node.showTopo()+"\n");
		for(TransNode node:receiverList)
			System.out.print(node.showTopo()+"\n");
	}
	
	public void ping(){
		//Requires：senderList!=null
    	//Modifies:none
    	//Effects:输出设备节点的ping情况
		for(TransNode node:senderList)
			System.out.print(node.ping()+"\n");
	}
	
	public void ls_n(){
		//Requires：senderList!=null,transponderList!=null,receiverList!=null
    	//Modifies:none
    	//Effects:输出当前的可用通信工具数量
		StringBuffer sb=new StringBuffer();
		sb.append("(Sender: ");
		sb.append(senderList.size());
		sb.append(",");
		sb.append("Transponder: ");
		sb.append(transponderList.size());
		sb.append(",");
		sb.append("Receiver: ");
		sb.append(receiverList.size());
		sb.append(")");
		System.out.print(sb+"\n");
	}
	
	public void ls_sender(){
		//Requires：senderList!=null
    	//Modifies:none
    	//Effects:输出所有sender的信息
		for(TransNode node:senderList)
			System.out.print(node.ls()+"\n");
	}
	
	public void ls_transponder(){
		//Requires：transponderList!=null
    	//Modifies:none
    	//Effects:输出所有transponder的信息
		for(TransNode node:transponderList)
			System.out.print(node.ls()+"\n");
	}
	
	public void ls_receiver(){
		//Requires：receiverList!=null
    	//Modifies:none
    	//Effects:输出所有receiver的信息
		for(TransNode node:receiverList)
			System.out.print(node.ls()+"\n");
	}
	
	public Iterator<TransNode> iterator(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回一个TransNode的迭代器，按照S,T,R的顺序来迭代
		return new TransNodeIterator();
	}
	
	@SuppressWarnings("resource")
	public static BlackBox initBlackBox(File configFile) throws IOException, NotConfigFileException{
		//Requires：configFile.canRead&&configFile.isFile
    	//Modifies:this
    	//Effects:根据configFile初始化BlackBox；若配置文件出错则抛出异常并终止程序
		BlackBox blackBox=null;
		BufferedReader br = new BufferedReader(new FileReader(configFile));
		String line=null;
		
		Pattern numPattern=Pattern.compile("^\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)$");
		Pattern senderPattern=Pattern.compile("^S(\\d{1,3})$");
		Pattern receiverPattern=Pattern.compile("^R(\\d{1,3})$");
		
		//先读取第一行，初始化blackbox
		if((line=br.readLine())!=null){
			Matcher numMatcher=numPattern.matcher(line);
			if(numMatcher.find()){
				int senderNum=Integer.parseInt(numMatcher.group(1));
				int transponderNum=Integer.parseInt(numMatcher.group(2));
				int receiverNum=Integer.parseInt(numMatcher.group(3));
				if(senderNum==0||transponderNum==0||receiverNum==0)
					throw new NotConfigFileException("Error: the number of sender/transponder/receiver must >=0");
				blackBox=new BlackBox(senderNum, transponderNum, receiverNum);
			}
			else
				throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
		}
		else
			throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
		
		//接着读取能够加密的sender列表
		while(true){
			Matcher senderMatcher=null;
			if((line=br.readLine())!=null){
				if(line.equals("#"))
					break;
				else {
					senderMatcher=senderPattern.matcher(line);
					if(senderMatcher.find()){
						int senderIndex=Integer.parseInt(senderMatcher.group(1));
						if(senderIndex<blackBox.senderList.size())
							blackBox.senderList.get(senderIndex).setEencryptable(true);
						else
							throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
					}
					else
						throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
				}
			}
			else
				throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
		}
		
		//接着读取receiver的列表
		while(true){
			Matcher receiverMatcher=null;
			if((line=br.readLine())!=null){
				if(line.equals("#"))
					break;
				else {
					receiverMatcher=receiverPattern.matcher(line);
					if(receiverMatcher.find()){
						int receiverIndex=Integer.parseInt(receiverMatcher.group(1));
						if(receiverIndex<blackBox.receiverList.size())
							blackBox.receiverList.get(receiverIndex).setDecipherable(true);
						else
							throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
					}
					else
						throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
				}
			}
			else
				throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
		}
		
		//读取连接情况
		while(true){
			if((line=br.readLine())!=null){
				if(line.equals("#"))
					break;
				else
					blackBox.link(line);
			}
			else
				throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
		}
		br.close();
		blackBox.computeMap();
		return blackBox;
		
	}
		
	private void link(String arg){
		//Requires：arg满足(S1,T1)的形式
    	//Modifies:this
    	//Effects:将对应的两个节点相连接。若出现违反规则的连接情况则抛出异常并终止程序
		//不能使SR直连
		Matcher matcher=linkPattern.matcher(arg);
		try {
			if(matcher.find()){
				String srcType=matcher.group(1);
				int srcIndex=Integer.parseInt(matcher.group(2));
				String dstType=matcher.group(3);
				int dstIndex=Integer.parseInt(matcher.group(4));
				if(srcType.equals(senderType)&&dstType.equals(receiverType))
					throw new InvalidArgException("Error: Senders can not directly connect to Receivers");
				//取出SRC
				TransNode srcNode=null;
				if(srcType.equals(transponderType))
					srcNode=getTransponder(srcIndex);
				else//SENDER
					srcNode=getSender(srcIndex);
				
				//取出DST
				TransNode dstNode=null;
				if(dstType.equals(transponderType))
					dstNode=getTransponder(dstIndex);
				else//RECEIVER
					dstNode=getReceiver(dstIndex);
				if(srcNode==dstNode)
					throw new LoopException("Error: "+srcType+srcIndex+" can not be linked to itself");
				srcNode.link(dstNode, 1);
			}
			else{
				throw new InvalidArgException("Invalid arg: "+arg);
			}
		}
		catch (IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		catch (InvalidArgException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		catch (LoopException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	
	private void computeMap(){
		//Requires：senderList!=null,tSet!=null
    	//Modifies:this
    	//Effects:计算出当前网络的中转规则，并填入各个通信工具中
		for(Iterator<Sender> senderIterator=senderList.iterator();senderIterator.hasNext();){
			Sender curSender=senderIterator.next();
			Dijkstra dijkstra=new Dijkstra(curSender, tSet);
			Map<TransNode, LinkedList<TransNode>> pathMap=dijkstra.run();
			//printPath(curSender,pathMap);//DEBUG
			for(Iterator<Map.Entry<TransNode, LinkedList<TransNode>>> pathIterator=pathMap.entrySet().iterator();pathIterator.hasNext();){
				Map.Entry<TransNode, LinkedList<TransNode>> pathEntry=pathIterator.next();
				if(pathEntry.getKey() instanceof Receiver){//是一条指向receiver的最短路径
					Receiver curReceiver=(Receiver)pathEntry.getKey();
					//遍历pathlist，将当前节点的下一个放入route
					TransNode preNode=null;
					for(Iterator<TransNode> nodeIterator=pathEntry.getValue().iterator();nodeIterator.hasNext();){
						TransNode curNode=nodeIterator.next();
						if(preNode!=null){
							preNode.route.put(curReceiver, curNode);
						}
						preNode=curNode;
					}
				}
			}
		}
	}		
	
	private ArrayList<String> cleanEmptyStr(String[] strs){
		//Requires：strs!=null
		//Modifies:none
		//Effects:返回将strs中的空白符删除后的ArrayList<String>
		ArrayList<String> strList=new ArrayList<String>();
		for(String str:strs){
			if(str!=null&&str.length()>0)
				strList.add(str);
		}
		return strList;
	}
	
	private Sender getSender(int index){
		//Requires：senderList!=null，index>=0
    	//Modifies:none
    	//Effects:返回ID对应的设备
		if(index>=senderList.size())
			throw new IndexOutOfBoundsException("Error: Index out of bound: S"+index);
		return senderList.get(index);
	}
	
	private Transponder getTransponder(int index){
		//Requires：senderList!=null，index>=0
    	//Modifies:none
    	//Effects:返回ID对应的设备
		if(index>=transponderList.size())
			throw new IndexOutOfBoundsException("Error: Index out of bound: T"+index);
		return transponderList.get(index);
	}
	
	private Receiver getReceiver(int index){
		//Requires：senderList!=null，index>=0
    	//Modifies:none
    	//Effects:返回ID对应的设备
		if(index>=receiverList.size())
			throw new IndexOutOfBoundsException("Error: Index out of bound: R"+index);
		return receiverList.get(index);
	}
	
	private class TransNodeIterator implements Iterator<TransNode>{
		//OVERVIEW:迭代器类，按照S0...N,T0...N,R0...N的顺序遍历
		private Iterator<Sender> senderIterator=senderList.iterator();
		private Iterator<Transponder> transponderIterator=transponderList.iterator();
		private Iterator<Receiver> receiverIterator=receiverList.iterator();
		
		public boolean hasNext(){
			//Requires：senderIterator!=null,transponderIterator!=null,receiverIterator!=null
	    	//Modifies:none
	    	//Effects:返回下面是否还有元素
			return senderIterator.hasNext()||transponderIterator.hasNext()||receiverIterator.hasNext();
		}
		
		public TransNode next(){
			//Requires：senderIterator!=null,transponderIterator!=null,receiverIterator!=null
	    	//Modifies:none
	    	//Effects:若下面有元素，返回该元素；否则抛出异常
			if(senderIterator.hasNext())
				return senderIterator.next();
			else if(transponderIterator.hasNext())
				return transponderIterator.next();
			else if(receiverIterator.hasNext())
				return receiverIterator.next();
			else
				throw new NoSuchElementException();
		}
		
		public void remove(){
			//Requires:none
	    	//Modifies:none
	    	//Effects:不支持的操作，直接抛出异常
			throw new UnsupportedOperationException();
		}
		
		@SuppressWarnings("unused")
		public boolean repOK(){
			//senderIterator!=null&&transponderIterator!=null&&receiverIterator!=null;
			return senderIterator!=null&&transponderIterator!=null&&receiverIterator!=null;
		}
	}
	
	/*
	private void printPath(Sender curSender,Map<TransNode, LinkedList<TransNode>> pathMap){
		System.out.println(curSender+":");
		for(Iterator<Map.Entry<TransNode, LinkedList<TransNode>>> pathIterator=pathMap.entrySet().iterator();pathIterator.hasNext();){
			Map.Entry<TransNode, LinkedList<TransNode>> pathEntry=pathIterator.next();
			if(pathEntry.getKey() instanceof Receiver){//是一条指向receiver的最短路径
				Receiver curReceiver=(Receiver)pathEntry.getKey();
				System.out.print(curReceiver+": ");
				//遍历pathlist
				for(Iterator<TransNode> nodeIterator=pathEntry.getValue().iterator();nodeIterator.hasNext();){
					TransNode curNode=nodeIterator.next();
					System.out.print(curNode+"->");
				}
				System.out.println();
			}
		}
	}
	*/
	
}

class NotConfigFileException extends Exception{
	//OVERVIEW:异常类，表示配置文档无效
	private static final long serialVersionUID = 2693467301799653598L;

	public NotConfigFileException(){
		super();
	}
	
	public NotConfigFileException(String str){
		super(str);
	}
}

class InvalidArgException extends Exception{
	//OVERVIEW:异常类，表示当前指令非法
	private static final long serialVersionUID = 5834400849913238092L;

	public InvalidArgException(){
		super();
	}
	
	public InvalidArgException(String str){
		super(str);
	}
}

class LoopException extends Exception{
	//OVERVIEW:异常类，表示尝试将通信工具与其自身链接
	private static final long serialVersionUID = -5774918292053671485L;
	
	public LoopException(){
		super();
	}
	
	public LoopException(String str){
		super(str);
	}
}
