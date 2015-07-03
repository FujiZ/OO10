package oo10;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public abstract class TransNode{
	//OVERVIEW:传输节点，是Sender/Transponder/Receiver的父类
	protected Map<TransNode,Integer> tMap;
	protected BlockingQueue<Infor> inforQueue;
	protected Map<Receiver, TransNode> route;//中转规则
	
	public TransNode(){
		//Requires：none
    	//Modifies:this
    	//Effects:初始化对象
		tMap=new HashMap<TransNode,Integer>();
		inforQueue=new LinkedBlockingQueue<Infor>();
		route=new HashMap<Receiver,TransNode>();
	}
	
	public Map<TransNode,Integer> getChild(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回该节点链接的节点
		return tMap;
	}
	
	public String showTopo(){
		//Requires：tMap!=null
    	//Modifies:none
    	//Effects:返回该节点的拓扑结构
		StringBuffer sb=new StringBuffer();
		sb.append(this);
		sb.append(":");
		for(Map.Entry<TransNode,Integer> entry:tMap.entrySet()){
			sb.append("(");
			sb.append(entry.getKey());
			sb.append(",");
			sb.append(entry.getValue());
			sb.append(")");
		}
		return sb.toString();
	}
	
	public String ping(){
		//Requires：route!=null
    	//Modifies:none
    	//Effects:返回该节点能链接到的receiver
		StringBuffer sb=new StringBuffer();
		sb.append(this);
		sb.append(":");
		for (Receiver key:route.keySet()){
			sb.append(key);
			sb.append(" ");
		}
		return sb.toString();
	}
	
	public void sendInfor(Infor infor) throws InterruptedException{
		//Requires：infor!=null,inforQueue!=null
    	//Modifies:none
    	//Effects:将信息发送至对应节点
		inforQueue.put(infor);
	}
	
	public void link(TransNode node,Integer length){
		//Requires：node!=null,length!=null,length>0
    	//Modifies:this
    	//Effects:将该节点与当前节点连接
		tMap.put(node, length);
	}
	
	public abstract Integer getId();
	//Requires：none
	//Modifies:none
	//Effects:返回设备的ID
	
	public abstract String ls();
	//Requires：none
	//Modifies:none
	//Effects:返回设备的具体信息
	
	public boolean repOK(){
		//tMap!=null&&inforQueue!=null&&route!=null
		return tMap!=null&&inforQueue!=null&&route!=null;
	}
	
}
