package oo10;

public class Receiver extends TransNode implements Runnable{
	//OVERVIEW:接收器，将接收到的信息输出
	//private static int count=0;
	private final Integer id;
	private boolean decipherable=false;
	
	public Receiver(int id){
		//Requires：none
    	//Modifies:this
    	//Effects:初始化对象
		super();
		this.id=id;
	}
	
	public void run(){
		//Requires：none
    	//Modifies:none
    	//Effects:将队列中的信息取出并输出
		try {
			while(true){
				Infor infor=inforQueue.take();
				if(decipherable&&infor.isEncrypted())
					decipher(infor);
				System.out.print(this+" received: "+infor+"\n");
			}
		} catch (Throwable e) {
			System.out.println("JVM terminated");
			System.exit(-1);
		}
		
	}
	
	public Integer getId(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Receiver的ID
		return id;
	}
	
	public String ls(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Receiver的具体信息
		StringBuffer sb=new StringBuffer();
		sb.append(this);
		sb.append(":");
		if(decipherable)
			sb.append("Decipherable");
		else
			sb.append("DisDecipherable");
		return sb.toString();
	}
	
	public void setDecipherable(boolean decipherable){
		//Requires：none
    	//Modifies:this
    	//Effects:将当前Receiver的状态设置为decipherable
		this.decipherable=decipherable;
	}
	
	public boolean isDecipherable(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Receiver的decipherable状态
		return decipherable;
	}
	
	public String toString(){
		//Requires：none
    	//Modifies:none
    	//Effects:返回Receiver的名称
		return "R"+id;
	}
	
	private void decipher(Infor infor){
		//Requires：infor!=null
    	//Modifies:infor
    	//Effects:将被加密的信息解密
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
