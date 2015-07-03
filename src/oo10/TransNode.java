package oo10;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public abstract class TransNode{
	//OVERVIEW:����ڵ㣬��Sender/Transponder/Receiver�ĸ���
	protected Map<TransNode,Integer> tMap;
	protected BlockingQueue<Infor> inforQueue;
	protected Map<Receiver, TransNode> route;//��ת����
	
	public TransNode(){
		//Requires��none
    	//Modifies:this
    	//Effects:��ʼ������
		tMap=new HashMap<TransNode,Integer>();
		inforQueue=new LinkedBlockingQueue<Infor>();
		route=new HashMap<Receiver,TransNode>();
	}
	
	public Map<TransNode,Integer> getChild(){
		//Requires��none
    	//Modifies:none
    	//Effects:���ظýڵ����ӵĽڵ�
		return tMap;
	}
	
	public String showTopo(){
		//Requires��tMap!=null
    	//Modifies:none
    	//Effects:���ظýڵ�����˽ṹ
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
		//Requires��route!=null
    	//Modifies:none
    	//Effects:���ظýڵ������ӵ���receiver
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
		//Requires��infor!=null,inforQueue!=null
    	//Modifies:none
    	//Effects:����Ϣ��������Ӧ�ڵ�
		inforQueue.put(infor);
	}
	
	public void link(TransNode node,Integer length){
		//Requires��node!=null,length!=null,length>0
    	//Modifies:this
    	//Effects:���ýڵ��뵱ǰ�ڵ�����
		tMap.put(node, length);
	}
	
	public abstract Integer getId();
	//Requires��none
	//Modifies:none
	//Effects:�����豸��ID
	
	public abstract String ls();
	//Requires��none
	//Modifies:none
	//Effects:�����豸�ľ�����Ϣ
	
	public boolean repOK(){
		//tMap!=null&&inforQueue!=null&&route!=null
		return tMap!=null&&inforQueue!=null&&route!=null;
	}
	
}
