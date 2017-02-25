import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
	
	private int count;
	private Node first, last;
	
	private class Node {
		public Node(Item item, Node next, Node pre) {
			this.item = item;
			this.next = next;
			this.pre = pre;
		}
		private Item item;
		private Node next;
		private Node pre;
	}
	
	public Deque() {							// construct an empty deque
		this.count = 0;
		this.first = null;
		this.last = null;
	}   
                 
	
	public boolean isEmpty() {                 	// is the deque empty?
		return this.count == 0;
	}
   
	public int size() {                        	// return the number of items on the deque
		return this.count;
	}
	
   	public void addFirst(Item item) {          	// add the item to the front
   		if (null == item) throw new NullPointerException();
   		
   		Node oldFirst = this.first;
   		this.first = new Node(item, oldFirst, null);
   		this.count++;
   		
   		if (this.count == 1) {
   			this.last = this.first;
   		} else {
   			oldFirst.pre = this.first;
   		}
   	}
   	
   	public void addLast(Item item) {           	// add the item to the end
   		if (null == item) throw new NullPointerException();
   		
   		Node oldlast = this.last;
   		this.last = new Node(item, null, oldlast);
   		this.count++;
   		
   		if (this.count == 1) {
   			this.first = this.last;
   		} else {
   			oldlast.next = this.last;
   		}
   	}
   	
   	public Item removeFirst() {               // remove and return the item from the front
   		if (this.isEmpty()) throw new NoSuchElementException();
   		
   		Item item = this.first.item;
   		this.first = this.first.next;
   		
   		count--;
   		
   		if (this.count == 0) {
   			this.last = this.first;
   		} else {
   			this.first.pre = null;
   		}
 
   		return item;
   	}
   	
   	public Item removeLast() {                	// remove and return the item from the end
   		if (this.isEmpty()) throw new NoSuchElementException();
   		
   		Item item = this.last.item;
   		this.last = this.last.pre;

   		count--;
   		
   		if (this.count == 0) {
   			this.first = this.last;
   		} else {
   			this.last.next = null;
   		}
   		return item;
   	}
   	
   	public Iterator<Item> iterator() {          // return an iterator over items in order from front to end
   		return new DequeIterator<Item>();
   	}
   	
   	private class DequeIterator<Item> implements Iterator {
   		private Node first = Deque.this.first;
   		private int count = Deque.this.count;

		@Override
		public boolean hasNext() {
			return this.count > 0;
		}

		@Override
		public Object next() {
			if (!this.hasNext()) throw new NoSuchElementException();
			
			Item item = (Item) this.first.item;
			this.first = this.first.next;
			this.count--;
			return item;
		}	
   	}
   	
   	public static void main(String[] args) {	// unit testing (optional)
   		
//   		Deque<String> sDeque = new Deque<String>();
//   		if(args[0].equals("addFirst")){
//   	   		StdOut.println("addFirst:");
//   	   		while(!StdIn.isEmpty()){
//   	   			String word = StdIn.readString();
//   	   			sDeque.addFirst(word);
//   	   		}
//   		}else if(args[0].equals("addLast")){
//   	   		StdOut.println("addLast:");
//   	   		while(!StdIn.isEmpty()){
//   	   			String word = StdIn.readString();
//   	   			sDeque.addLast(word);
//   	   		}
//   		}
//
//   		for (String word:sDeque) {
//   			StdOut.print(word + " ");
//   		}
//   		StdOut.println();
   		Deque<Integer> iDeque = new Deque<Integer>();
   		int item;
   		iDeque.addFirst(1);
   		iDeque.addFirst(2);
   		iDeque.addLast(3);
   		iDeque.addLast(4);
   		StdOut.println("2<=>1<=>3<=>4");
   		
   		for (Integer i: iDeque) {
   			StdOut.println(i);
   		}
   		
   		item = iDeque.removeFirst();
   		StdOut.println(item);
   		
   		StdOut.println("1<=>3<=>4");
   		for (Integer i: iDeque) {
   			StdOut.println(i);
   		}
   		
   		iDeque.removeFirst();
   		iDeque.removeFirst();
   		StdOut.println("4");
   		for (Integer i: iDeque) {
   			StdOut.println(i);
   		}
   		
   		iDeque.removeFirst();
   		StdOut.println("null");
   		for (Integer i: iDeque) {
   			StdOut.println(i);
   		}
   	}
}



