package oo10;


public class Sender extends TransNode implements Runnable{
	//OVERVIEW:�������������յ�����Ϣ���
	//private static int count=0;
	private final Integer id;
	private boolean encryptable=false;
	
	
	public Sender(int id){
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
				//�ȼ���
				if(infor.isEncrypted()){
					if(encryptable)
						encrypt(infor);
					else
						infor.setEncrypted(false);
				}
				//������Ϣ
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
    	//Effects:����Sender��ID
		return id;
	}
	
	public String ls(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Sender�ľ�����Ϣ
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
		//Requires��none
    	//Modifies:this
    	//Effects:����ǰSender��״̬����Ϊencryptable
		this.encryptable=encryptable;
	}
	
	public boolean isEencryptable(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Sender��encryptable״̬
		return encryptable;
	}
	
	public String toString(){
		//Requires��none
    	//Modifies:none
    	//Effects:����Sender������
		return "S"+id;
	}
	
	private void encrypt(Infor infor){
		//Requires��infor!=null
    	//Modifies:infor
    	//Effects:����Ϣ����
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
