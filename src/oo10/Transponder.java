package oo10;

import java.util.Map;

public class Transponder extends TransNode implements Runnable{
	//OVERVIEW:��ת���������յ�����Ϣ�������һ���ڵ�
	private final Integer id;
	
	public Transponder(int id){
		//Requires��none
    	//Modifies:this
    	//Effects:��ʼ������
		super();
		this.id=id;
	}
	
	public void run(){
		//Requires��none
    	//Modifies:none
    	//Effects:�������е���Ϣȡ�����������ȼ����򴫸���һ��ת����
		try {
			while(true){
				Infor infor=inforQueue.take();
				infor.addTrace(this);
				TransNode nextNode=route.get(infor.getReceiver());
				if(nextNode!=null)
					nextNode.sendInfor(infor);
			}
		} catch (Throwable e) {
			System.out.println("JVM terminated");
			System.exit(-1);
		}
	}
	
	public Integer getId(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Transponder��ID
		return id;
	}
	
	public String ls(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Transponder�ľ�����Ϣ
		StringBuffer sb=new StringBuffer();
		sb.append(this);
		sb.append(":");
		//������ת����
		for (Map.Entry<Receiver, TransNode> entry:route.entrySet()){
			sb.append("\n[");
			sb.append(entry.getKey());
			sb.append(":(HiPri:");
			sb.append(entry.getValue());
			sb.append(" )(DefPri:");
			//��ʣ�µ�·������Defpri
			for (TransNode key:tMap.keySet()){
				if(key!=entry.getValue()){
					sb.append(key);
					sb.append(" ");
				}
			}
			sb.append(")]");
		}
		return sb.toString();
	}
	
	
	public String toString(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Transponder������
		return "T"+id;
	}
	
	public boolean repOK(){
		//super.repOK
		return super.repOK();
	}
}
