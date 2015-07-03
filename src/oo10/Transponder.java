package oo10;

import java.util.Map;

public class Transponder extends TransNode implements Runnable{
	//OVERVIEW:中转器，将接收到的信息输出到下一个节点
	private final Integer id;
	
	public Transponder(int id){
		//Requires：none
    	//Modifies:this
    	//Effects:初始化对象
		super();
		this.id=id;
	}
	
	public void run(){
		//Requires：none
    	//Modifies:none
    	//Effects:将队列中的信息取出并根据优先级规则传给下一个转发器
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
		//Requires：none
    	//Modifies:none
    	//Effects:返回Transponder的ID
		return id;
	}
	
	public String ls(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Transponder的具体信息
		StringBuffer sb=new StringBuffer();
		sb.append(this);
		sb.append(":");
		//遍历中转规则
		for (Map.Entry<Receiver, TransNode> entry:route.entrySet()){
			sb.append("\n[");
			sb.append(entry.getKey());
			sb.append(":(HiPri:");
			sb.append(entry.getValue());
			sb.append(" )(DefPri:");
			//将剩下的路径加入Defpri
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
		//Requires：none
    	//Modifies:none
    	//Effects:返回Transponder的名称
		return "T"+id;
	}
	
	public boolean repOK(){
		//super.repOK
		return super.repOK();
	}
}
