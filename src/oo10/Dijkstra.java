package oo10;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Dijkstra {
	//OVERVIEW:利用Dijkstra算法遍历图，并将最短路径结果返回
	private Sender startNode;
	private Set<TransNode> tSet;//存储除sender外的所有节点
	private Set<TransNode> open=new HashSet<TransNode>(); 
	private Set<TransNode> close=new HashSet<TransNode>(); 
	private Map<TransNode,Integer> path=new HashMap<TransNode,Integer>();//封装路径距离 
	private Map<TransNode, LinkedList<TransNode>> pathInfo=new HashMap<TransNode, LinkedList<TransNode>>();//封装路径信息 
	
    public Dijkstra(Sender startNode,Set<TransNode> tSet){
    	//Requires：startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:初始化对象
    	this.startNode=startNode;
    	this.tSet=tSet;
    }
    
    public Map<TransNode, LinkedList<TransNode>> run(){
    	//Requires：startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:返回最短路径的计算结果
    	init();
    	computePath(startNode);
    	return pathInfo;
    }
    
    private void init(){
    	//Requires：startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:初始化对象,设置初始状态
    	close.add(startNode);
    	//遍历set，对startNode直连的直接放入，否则将path设为Integer.MAX_VALUE
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
    	//Requires：startNode!=null,tSet!=null
    	//Modifies:this
    	//Effects:利用Dijkstra算法寻找最短路径
    	TransNode nearest=getShortestPath(start);//取距离start节点最近的子节点,放入close
        if(nearest==null){
            return;
        }
        close.add(nearest);
        open.remove(nearest);
        Map<TransNode,Integer> childs=nearest.tMap;
        for(TransNode child:childs.keySet()){
            if(open.contains(child)){//如果子节点在open中
                Integer newCompute=path.get(nearest)+childs.get(child);
                if(path.get(child)>newCompute){//之前设置的距离大于新计算出来的距离 
                    path.put(child, newCompute);
                    @SuppressWarnings("unchecked")
					LinkedList<TransNode> temPath=(LinkedList<TransNode>)pathInfo.get(nearest).clone();
                    temPath.add(child);
                    pathInfo.put(child, temPath);
                }
            }
        }
        computePath(start);//重复执行自己,确保所有子节点被遍历 
        computePath(nearest);//向外一层层递归,直至所有顶点被遍历 
    } 
    
	private TransNode getShortestPath(TransNode node){
		//Requires：startNode!=null,tSet!=null，node!=null
    	//Modifies:none
    	//Effects:返回与当前节点距离最短的节点
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
