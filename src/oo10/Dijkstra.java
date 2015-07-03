package oo10;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Dijkstra {
	//OVERVIEW:����Dijkstra�㷨����ͼ���������·���������
	private Sender startNode;
	private Set<TransNode> tSet;//�洢��sender������нڵ�
	private Set<TransNode> open=new HashSet<TransNode>(); 
	private Set<TransNode> close=new HashSet<TransNode>(); 
	private Map<TransNode,Integer> path=new HashMap<TransNode,Integer>();//��װ·������ 
	private Map<TransNode, LinkedList<TransNode>> pathInfo=new HashMap<TransNode, LinkedList<TransNode>>();//��װ·����Ϣ 
	
    public Dijkstra(Sender startNode,Set<TransNode> tSet){
    	//Requires��startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:��ʼ������
    	this.startNode=startNode;
    	this.tSet=tSet;
    }
    
    public Map<TransNode, LinkedList<TransNode>> run(){
    	//Requires��startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:�������·���ļ�����
    	init();
    	computePath(startNode);
    	return pathInfo;
    }
    
    private void init(){
    	//Requires��startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:��ʼ������,���ó�ʼ״̬
    	close.add(startNode);
    	//����set����startNodeֱ����ֱ�ӷ��룬����path��ΪInteger.MAX_VALUE
    	for(TransNode node:tSet){
    		Integer length=startNode.getChild().get(node);
    		LinkedList<TransNode> temPath=new LinkedList<TransNode>();
    		temPath.add(startNode);
    		if(length!=null){
    			path.put(node, length);
    			temPath.add(node);
    		}
    		else{
    			path.put(node, Integer.MAX_VALUE);
    		}
    		pathInfo.put(node, temPath);
    		open.add(node);
    	}
    }

    private void computePath(TransNode start){
    	//Requires��startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:����Dijkstra�㷨Ѱ�����·��
    	TransNode nearest=getShortestPath(start);//ȡ����start�ڵ�������ӽڵ�,����close
        if(nearest==null){
            return;
        }
        close.add(nearest);
        open.remove(nearest);
        Map<TransNode,Integer> childs=nearest.tMap;
        for(TransNode child:childs.keySet()){
            if(open.contains(child)){//����ӽڵ���open��
                Integer newCompute=path.get(nearest)+childs.get(child);
                if(path.get(child)>newCompute){//֮ǰ���õľ�������¼�������ľ��� 
                    path.put(child, newCompute);
                    @SuppressWarnings("unchecked")
					LinkedList<TransNode> temPath=(LinkedList<TransNode>)pathInfo.get(nearest).clone();
                    temPath.add(child);
                    pathInfo.put(child, temPath);
                }
            }
        }
        computePath(start);//�ظ�ִ���Լ�,ȷ�������ӽڵ㱻���� 
        computePath(nearest);//����һ���ݹ�,ֱ�����ж��㱻���� 
    } 
    
	private TransNode getShortestPath(TransNode node){
		//Requires��startNode!=null,tSet!=null��node!=null
    	//Modifies:none
    	//Effects:�����뵱ǰ�ڵ������̵Ľڵ�
		TransNode res=null;
		int minDis=Integer.MAX_VALUE;
		Map<TransNode, Integer> childs=node.tMap;
		for(TransNode child:childs.keySet()){
            if(open.contains(child)){
                int distance=childs.get(child);
                if(distance<minDis){
                    minDis=distance;
                    res=child;
                }
            }
        }
        return res;
	}
	
	public boolean repOK(){
		//startNode!=null&&tSet!=null
		return startNode!=null&&tSet!=null;
	}
	
}
