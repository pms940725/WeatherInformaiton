package Tree;

import java.util.ArrayList;

import initProject.Parameter;

public class Tree {
    // 부모 노드에 자식 노드 추가
    public static void add(Node parent, Node child) {
    	//System.out.println("Parent Node :" + parent.getData() + " : " + parent);
    	//System.out.println("Child Node :" + child.getData()+" : " + child);
    	
        // 부모 노드의 자식 노드가 없다면
        if(parent.getLeftChild() == null) {
        
            parent.setLeftChild(child);
        }
        // 부모 노드의 자식 노드가 있다면
        else {
            // 자식 노드의 형제로 노드로 추가
            Node temp = parent.getLeftChild();
           
            while(true) {
            	// System.out.println("temp Node :" + temp.getData()+" : " + temp);
                /*if(child.getData().equals(temp.getData())) {
                	System.out.println("Don't save Node");
                	return;
                }*/
                if(temp.getRightSibling() == null) {
                	break;
                }
            	temp = temp.getRightSibling();
            }
         
            temp.setRightSibling(child);
        }
        //System.out.println("==========================");
    }
    
    public static int getMaxDepth() {
    	return Parameter.MaxDepth;
    }
    
    public static void printMaxDepth() {
    	System.out.println("MaxDepth : " + Parameter.MaxDepth);
    }
     
    // 레벨 출력
    public static void printLevel(Node node, int level) {
        int depth = 0;
        Node tempChild = node;
        Node tempParent = node;
         
        // 레벨에 도달할 때까지 반복
        while(depth <= level) {
            if(depth == level) {
                // 해당 레벨의 노드가 존재한다면
                while(tempChild != null) {
                    // 데이터를 출력하고 형제노드로 이동한다.
                    System.out.print(tempChild.getData() + " ");
                    tempChild = tempChild.getRightSibling();
                }
                 
                // 부모 노드의 형제노드가 존재한다면
                // 그 노드의 자식 노드들도 출력해줘야 한다.
                if(tempParent.getRightSibling() != null) {
                    tempParent = tempParent.getRightSibling();
                    tempChild = tempParent.getLeftChild();
                } else
                    break;
            } else {
                // 깊이와 레벨이 맞지 않으면 부모 노드를 저장하고
                // 한단계 아래로 내려간다.
                tempParent = tempChild;
                tempChild = tempChild.getLeftChild();
                depth++;
            }
        }
    }
     
    // 모두 출력
    public static void printTree(Node node, int depth) {
        for(int i = 0; i < depth; i++)
            System.out.print(" ");
         
        // 데이터 출력
        System.out.println(node.getData());
         
        // 자식 노드가 존재한다면
        if(node.getLeftChild() != null)
            printTree(node.getLeftChild(), depth + 1);
         
        // 형제 노드가 존재한다면
        if(node.getRightSibling() != null)
            printTree(node.getRightSibling(), depth);
    }
    
    // 원하는 Data값을 가진 Node를 반환하는 함수
    public static Node findData(Node node, String wantToFindData) {
 
    	//System.out.println(node.getData());
    
        // 결과를 찾으면 해당 결과  리턴
        if(node.getData().equals(wantToFindData))
        	return node;
        //System.out.println(node.getData());
         
        // 자식 노드가 존재한다면
        if(node.getLeftChild() != null) {
        	Node temp_node;
            temp_node=findData(node.getLeftChild(), wantToFindData);
            if(temp_node != null)
            	return temp_node;
        }


        // 형제 노드가 존재한다면
        if(node.getRightSibling() != null) {
        	Node temp_node;
            temp_node = findData(node.getRightSibling(), wantToFindData);
            if(temp_node != null)
            	return temp_node;
        }
        return null;
    }
    
    
    public static void getTailNode(Node node, int depth, ArrayList<Node> nodeArrayList){
    	
    	
    	// 자식 노드가 존재한다면
        if(node.getLeftChild() != null) {
        	getTailNode(node.getLeftChild(),depth+1, nodeArrayList);    
        }
        else {
        	nodeArrayList.add(node);
        }
        
        // 형제 노드가 존재한다면
        if(node.getRightSibling() != null && depth != 0) {
        	getTailNode(node.getRightSibling(), depth, nodeArrayList);
        }

    }
    
    
    
   
}
