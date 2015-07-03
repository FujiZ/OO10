package oo10;


public class Sender extends TransNode implements Runnable{
	//OVERVIEW:发射器，将接收到的信息输出
	//private static int count=0;
	private final Integer id;
	private boolean encryptable=false;
	
	
	public Sender(int id){
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
				//先加密
				if(infor.isEncrypted()){
					if(encryptable)
						encrypt(infor);
					else
						infor.setEncrypted(false);
				}
				//发送信息
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
    	//Effects:返回Sender的ID
		return id;
	}
	
	public String ls(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Sender的具体信息
		StringBuffer sb=new StringBuffer();
		sb.append(this);
		sb.append(":");
		if(encryptable)
			sb.append("Encryptable");
		else
			sb.append("DisEncryptable");
		return sb.toString();
	}
	
	public void setEencryptable(boolean encryptable){
		//Requires：none
    	//Modifies:this
    	//Effects:将当前Sender的状态设置为encryptable
		this.encryptable=encryptable;
	}
	
	public boolean isEencryptable(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Sender的encryptable状态
		return encryptable;
	}
	
	public String toString(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Sender的名称
		return "S"+id;
	}
	
	private void encrypt(Infor infor){
		//Requires：infor!=null
    	//Modifies:infor
    	//Effects:将信息加密
		if(infor.getText()==null||infor.getText().length()<=1)
			return;
		StringBuffer sb=new StringBuffer(infor.getText());
		for(int i=0;i+1<sb.length();i+=2){
			char c=sb.charAt(i);
			sb.setCharAt(i, sb.charAt(i+1));
			sb.setCharAt(i+1, c);
		}
		infor.setText(sb.toString());
	}

	public boolean repOK(){
		//super.repOK
		return super.repOK();
	}
}
