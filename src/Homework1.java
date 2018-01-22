import java.util.Scanner;
import java.util.Stack;
import javax.imageio.metadata.IIOMetadataNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Homework1 {
	static Stack<Character> _stack = new Stack<Character>();
	static Node root;
	
	static public void infix(Node n) {
		if (n != null){
			if(isOperate(n.getNodeName().charAt(0)) && n != root) System.out.print("(");
			NodeList list = n.getChildNodes();
			infix(list.item(1)); //recursive left node
            System.out.print(n.getNodeName());   //print itself
            infix(list.item(0));    //recursive right node
            if(isOperate(n.getNodeName().charAt(0)) && n != root) System.out.print(")");
		}
	}
	
	static public void inorder(Node n) {
		//base case
		if(!isOperate(n.getNodeName().charAt(0))) {
			n.setNodeValue(n.getNodeName());
			return;
		}
		
		Node second_child = new IIOMetadataNode(_stack.pop().toString());
		inorder(second_child);
		
		Node first_child = new IIOMetadataNode(_stack.pop().toString());
		inorder(first_child);
		
		n.appendChild(second_child);
		n.appendChild(first_child);
		calculate(n);
	}
	
	static public void calculate(Node n) {
		NodeList list = n.getChildNodes();
		int first_value = Integer.parseInt(list.item(1).getNodeValue());
		int second_value = Integer.parseInt(list.item(0).getNodeValue());
		int result = 0;
		switch(n.getNodeName()) {
			case "+" :{
				result = first_value + second_value;
				break;
			}
			case "-" :{
				result = first_value - second_value;
				break;
			}
			case "*" :{
				result = first_value * second_value;
				break;
			}
			case "/" :{
				result = first_value / second_value;
				break;
			}
		}
		n.setNodeValue(Integer.toString(result));
	}
	
	public static boolean isOperate(char _string) {
		return	(_string == '+' || _string == '-' || 
				_string == '*' || _string == '/');
	}
	
	public static void main(String[] args) {		
		// TODO: Implement your project here
		
		String data = "251-*32*+";
		
		if(args.length > 0) data = args[0];
		
		for(int i = 0; i < data.length(); i++) {
			_stack.push(data.charAt(i));
		}
		
		root = new IIOMetadataNode(_stack.pop().toString());
		inorder(root);
		infix(root);
        System.out.println("=" + root.getNodeValue());
        
        TreeIconDemo tree = new TreeIconDemo(root);
        tree.main(root);
	}
}