package oo10;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class OO10 {
	//OVERVIEW:主类，提供了输入界面供用户输入配置文件路径和具体命令
	private static void shell(BlackBox blackBox) throws InterruptedException, IOException, InvalidArgException{
		//Requires：blackBox!=null
		//Modifies:none
		//Effects:根据不同的命令作出不同的反应
		String show_topo="show-topo";
		String ping="ping";
		String ls_n="ls-n";
		String ls_sender="ls-sender";
		String ls_transponder="ls-transponder";
		String ls_receiver="ls-receiver";
		String end="#";
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			System.out.println(">");
			String str=reader.readLine();
			if(str!=null){
				if(str.equals(show_topo))
					blackBox.showTopo();
				else if(str.equals(ping))
					blackBox.ping();
				else if(str.equals(ls_n))
					blackBox.ls_n();
				else if(str.equals(ls_sender))
					blackBox.ls_sender();
				else if(str.equals(ls_transponder))
					blackBox.ls_transponder();
				else if(str.equals(ls_receiver))
					blackBox.ls_receiver();
				else if(str.equals(end)){
					System.out.println("Exit!");
					break;
				}
				else
					blackBox.sendInfor(str);
			}else{
				reader.close();
				throw new InvalidArgException("Ctrl-Z catched: JVM terminated");
			}
		}
		reader.close();
		
	}
	
	public static void main(String[] args){
		//Requires：configFile.isFile&&configFile.canRead
		//Modifies:none
		//Effects:根据给定的配置文件初始化blackbox，并转到一个命令输入界面
		try {
			Scanner scanner=new Scanner(System.in);
			String path=scanner.nextLine();
			File configFile=new File(path);
			if(!(configFile.isFile()&&configFile.canRead())){
				scanner.close();
				throw new NotConfigFileException("Error: "+configFile.getAbsolutePath()+"is not a valid config file");
			}
			BlackBox blackBox=BlackBox.initBlackBox(configFile);
			shell(blackBox);
			scanner.close();
			System.exit(0);
		}
		catch (NotConfigFileException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		catch (InvalidArgException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		catch (Throwable e) {
			System.out.println("JVM terminated");
			System.exit(-1);
		}
	}
	
}
