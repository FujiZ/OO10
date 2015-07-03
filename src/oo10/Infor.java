package oo10;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;


public class Infor implements Runnable{
	//OVERVIEW:包对象，记录发送路径
	private Sender sender;
	private Receiver receiver;
	private String text;
	private LinkedList<Transponder> tList;
	private boolean encrypted;
	private Calendar time;
	
	public Infor(Sender sender,Receiver receiver,String text,boolean encrypted,Calendar time){
		//Requires：sender!=null,receiver!=null,text!=null,time!=null
    	//Modifies:this
    	//Effects:初始化对象
		this.sender=sender;
		this.receiver=receiver;
		this.text=text;
		this.tList=new LinkedList<Transponder>();
		this.encrypted=encrypted;
		this.time=time;
	}
	
	public void run(){
		//Requires：sender!=null
    	//Modifies:none
    	//Effects:开启新线程，将当前包发送给sender
		try {
			send();
		} catch (Throwable e) {
			System.out.println("JVM terminated");
			System.exit(-1);
		}
	}
	
	private void send() throws InterruptedException{
		//Requires：sender!=null
    	//Modifies:none
    	//Effects:将当前包发送给sender
		sender.sendInfor(this);
	}
	
	public Receiver getReceiver(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回receiver
		return receiver;
	}
	
	public boolean isEncrypted(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回是否为加密信息
		return encrypted;
	}
	
	public void setEncrypted(boolean encrypted){
		//Requires：none
    	//Modifies:this
    	//Effects:修改infor的加密情况
		this.encrypted=encrypted;
	}
	
	public String getText(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回信息明文
		return text;
	}
	
	public void setText(String text){
		//Requires：text!=null
    	//Modifies:this
    	//Effects:修改信息明文
		this.text=text;
	}
	
	public void addTrace(Transponder transponder){
		//Requires：transponder!=null
    	//Modifies:this
    	//Effects:增加路径信息
		tList.add(transponder);
	}
	
	public String toString(){
		//Requires：sender!=null,receiver!=null,text!=null,time!=null
    	//Modifies:none
    	//Effects:返回包的具体信息
		StringBuffer sb=new StringBuffer();
		sb.append("(");
		sb.append(sender);
		sb.append(",");
		sb.append(receiver);
		sb.append(",");
		sb.append("\"");
		sb.append(text);
		sb.append("\",");
		Iterator<Transponder> iterator=tList.iterator();
		while(iterator.hasNext()){
			Transponder temTransponder=iterator.next();
			sb.append(temTransponder);
			sb.append(",");
		}
		sb.append(time.getTime());
		sb.append(")");
		return sb.toString();
	}
	
	public boolean repOK(){
		//sender!=null&&receiver!=null&&text!=null&&time!=null
		return sender!=null&&receiver!=null&&text!=null&&time!=null;
	}
}
