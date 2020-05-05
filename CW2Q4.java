import javax.print.attribute.standard.NumberUp;
import java.io.*;
import java.util.Scanner;

public class CW2Q4 {

	private int LIST_SIZE = 10000;
	Node[] List;
	int firstNode = 0;
	int endNode = 0;

	// Node class represents the format of each node in the linked list
	static class Node {
		String data;
		int addr;

		public Node(String newData, int newAddr) {
			data = newData;
			addr = newAddr;
		}

		void setAddr(int newAddr) {
			addr = newAddr;
		}

		public int getNextAddr(int pNode) {
			return addr ^ pNode;
		}
	}

	// class constructor
	public CW2Q4() {
		List = new Node[LIST_SIZE];
		List[0] = null;
	}


	// ALTERNATIVE to length()
	int stringLength(String str) {
		int len = 0;
		char[] strArray = str.toCharArray();
		for (char c : strArray) {
			len++;
		}
		return len;
	}


	//ALTERNATIVE to equals
	boolean isEqual(String str1, String str2) {
		int str1Len = stringLength(str1);
		int str2Len = stringLength(str2);
		char[] str1Array = str1.toCharArray();
		char[] str2Array = str2.toCharArray();
		if (str1Len != str2Len) {
			return false;
		}
		for (int i = 0; i < str1Len; i++) {
			if (str1Array[i] != str2Array[i]) {
				return false;
			}
		}
		return true;
	}


	// ALTERNATIVE to split
	// removes speech marks from names
	String trim(String str) {
		char[] strArray = str.toCharArray();

		int len = stringLength(str);

		// loop to remove speech marks from names
		String new_str = "";
		for (int c = 0; c < len; c++) {
			if (strArray[c] != '\"') {
				// ALTERNATIVE to String Builder
				new_str = new_str + strArray[c];
			}
		}
		return new_str;
	}


	// function goes through each node in the list to find one that's empty
	int findFreeNode() {
		for (int i = 1; i < LIST_SIZE; i++) {
			if (List[i] == null) {
				return i;
			}
		}
		return -1;
	}


	// procedure prints all full nodes in the linked list
	void printList() {
		// take start node, follow address continually until at end node
		int current = firstNode;
		int previous = 0;
		int next;
		Node node;
		while (current != 0) {
			node = List[current];
			if (node != null) {
				System.out.println(node.data);
				int temp = node.getNextAddr(previous);
				previous = current;
				current = temp;
			} else {
				current = -1;
			}
		}
	}


	//
	public int[] findNode(String data) {
		int cNode = firstNode;
		int pNode = 0;
		while (cNode != 0) {
			if (isEqual(data, List[cNode].data)) {
				return new int[]{cNode, pNode};
			} else {
				int temp = List[cNode].getNextAddr(pNode);
				pNode = cNode;
				cNode = temp;
			}
		}
		return new int[]{-1};
	}


	// needed to add data from file to linked list
	void insertAtEnd(String newData) {
		int index = findFreeNode();
		List[index] = new Node(newData, endNode);

		if (firstNode == 0) {
			firstNode = index;
		}

		if (endNode != 0) {
			List[endNode].setAddr(index ^ List[endNode].addr);
		}
		endNode = index;
	}


	// procedure adds string in between 2 nodes and updates all addresses
	public void insertAfter(String after, String newObj) {
		if (findNode(after)[0] == -1) {
			System.out.println(after + " does not exist in Linked List, therefore, cannot add a node after");
			return;
		}
		try {
			int pIndex = findNode(after)[0];
			int nIndex = findNode(after)[1] ^ List[pIndex].addr;
			int newAddr = pIndex ^ nIndex;
			int index = findFreeNode();
			List[index] = new Node(newObj, newAddr);

			// update address of nodes
			List[pIndex].setAddr((List[pIndex].addr ^ nIndex) ^ index);
			List[nIndex].setAddr((List[nIndex].addr ^ pIndex) ^ index);
			System.out.println(newObj + " has been added to the linked list.");
		}
		catch (NullPointerException e) {
			System.out.println(after + " is first name in list, therefore, cannot add a node before");
		}
	}


	// procedure adds string before the passed one
	public void insertBefore(String before, String newObj) {
		if (findNode(before)[0] == -1) {
			System.out.println(before + " does not exist in Linked List, therefore, cannot add a node before");
			return;
		}
		try {
			int nIndex = findNode(before)[0];
			int pIndex = findNode(List[nIndex].data)[1];

			int newAddr = pIndex ^ nIndex;
			int index = findFreeNode();
			List[index] = new Node(newObj, newAddr);

			List[pIndex].setAddr((List[pIndex].addr ^ nIndex) ^ index);
			List[nIndex].setAddr((List[nIndex].addr ^ pIndex) ^ index);
			System.out.println(newObj + " has been added to the linked list.");
		}
		catch (NullPointerException e) {
			System.out.println(before + " is first name in list, therefore, cannot add a node before");
		}
	}


	//
	public String removeAfter(String after) {
		int index = findNode(after)[0];
		if (index == endNode) {
			return after + " is last node in the list therefore, no node after";
		}
		try {
			index = List[index].getNextAddr(findNode(after)[1]);
			int addr = List[index].addr;
			String data = List[index].data;

			int pIndex = findNode(after)[0];
			int nIndex = addr ^ findNode(after)[0];

			// XOR to combine addresses
			int pAddr = List[pIndex].addr ^ index ^ nIndex;
			int nAddr = List[nIndex].addr ^ index ^ pIndex;

			// change addresses
			List[pIndex].setAddr(pAddr);
			List[nIndex].setAddr(nAddr);

			// clear current contents
			List[index] = null;

			return data + " has been removed from the Linked list";
		}
		catch (IndexOutOfBoundsException e) {
			return after + " does not exist in Linked List, therefore, no node after it.";
		}
	}


	//
	String removeBefore(String before) {

		try {
			int index = findNode(before)[1];
			if (index + 1 == firstNode) {
				return before + " is first node in the list therefore, no node before";
			}
			int addr = List[index].addr;
			String data = List[index].data;

			int nIndex = findNode(before)[0];
			int pIndex = addr ^ findNode(List[nIndex].data)[0];

			// XOR to combine addresses
			int pAddr = List[pIndex].addr ^ index ^ nIndex;
			int nAddr = List[nIndex].addr ^ index ^ pIndex;
			// change addresses
			List[pIndex].setAddr(pAddr);
			List[nIndex].setAddr(nAddr);

			// clear current contents
			List[index] = null;
			return data + " has been removed from the Linked list";
		}
		catch (IndexOutOfBoundsException e) {
			return before + " does not exist in Linked List, therefore, no node before it.";
		}
	}


	// Read names in from file and add to linked list
	void readFile() {
		File file = new File("names.txt");
		try {
			Scanner sc = new Scanner(file);
			// reads the file as a CSV
			sc.useDelimiter(",");
			int i = 0;
			String prev = null;
			// loop to add names from file to list
			while (sc.hasNext()) {
				String str = trim(sc.next());

				// can't use insertAfter here as we're adding to the end of the list
				insertAtEnd(str);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}


	public static void main(String[] args) {
		CW2Q4 ob = new CW2Q4();
		ob.readFile();
		//ob.printList();
		System.out.println("\nAll names have now been added from file to Linked List\n");
		System.out.println("----------------------------------------------------");
		System.out.println("           START OF SHOWING OFF METHODS");
		System.out.println("----------------------------------------------------");

		// Testing valid names
		System.out.println("\nTesting valid names:\n");
		System.out.println(ob.removeBefore("ALONSO"));
		System.out.println(ob.removeAfter("MARY"));
		ob.insertBefore("JENNIFER", "TEST0");
		ob.insertAfter("JENNIFER", "TEST1");
		System.out.println(ob.removeBefore("TEST1"));

		// Testing boundary names
		System.out.println("\n\nTesting boundary names:\n");
		System.out.println(ob.removeBefore("MARY"));
		System.out.println(ob.removeAfter("ALONSO"));
		ob.insertBefore("MARY", "TEST2");
		ob.insertAfter("ALONSO", "TEST3");

		// Testing erroneous names
		System.out.println("\n\nTesting erroneous names:\n");
		System.out.println(ob.removeBefore("FLINT"));
		System.out.println(ob.removeAfter("FLINT"));
		ob.insertBefore("FLINT", "TEST4");
		ob.insertAfter("FLINT", "TEST5");
		// punctuation can be added to linked list
		ob.insertAfter("ALONSO", "?'][");
		ob.insertAfter("ALONSO", " ");

		// Output final linked list
		System.out.println("\nResulting Linked List:");
		ob.printList();


	}
}