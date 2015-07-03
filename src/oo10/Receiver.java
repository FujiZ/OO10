package oo10;

public class Receiver extends TransNode implements Runnable{
	//OVERVIEW:�������������յ�����Ϣ���
	//private static int count=0;
	private final Integer id;
	private boolean decipherable=false;
	
	public Receiver(int id){
		//Requires��none
    	//Modifies:this
    	//Effects:��ʼ������
		super();
		this.id=id;
	}
	
	public void run(){
		//Requires��none
    	//Modifies:none
    	//Effects:�������е���Ϣȡ�������
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
		//Requires��none
    	//Modifies:none
    	//Effects:����Receiver��ID
		return id;
	}
	
	public String ls(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Receiver�ľ�����Ϣ
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
		//Requires��none
    	//Modifies:this
    	//Effects:����ǰReceiver��״̬����Ϊdecipherable
		this.decipherable=decipherable;
	}
	
	public boolean isDecipherable(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Receiver��decipherable״̬
		return decipherable;
	}
	
	public String toString(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Receiver������
		return "R"+id;
	}
	
	private void decipher(Infor infor){
		//Requires��infor!=null
    	//Modifies:infor
    	//Effects:�������ܵ���Ϣ����
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
