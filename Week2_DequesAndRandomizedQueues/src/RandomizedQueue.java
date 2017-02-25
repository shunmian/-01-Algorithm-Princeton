import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] items;
	private int tail;
	
	public RandomizedQueue() {                 // construct an empty randomized queue
		this.items = (Item[]) new Object[2];
		this.tail = 0;
	}
	
	public boolean isEmpty() {                 // is the queue empty?
		return this.tail == 0;
	}
	
	public int size() {                        // return the number of items on the queue
		return this.tail;
	}
	
	public void enqueue(Item item) {           // add the item
		if (null == item) throw new NullPointerException();
		this.resize();
		this.items[tail++] = item;
	}
	
	private void resize() {
		if (this.tail >= this.items.length) {
			Item[] newItems = (Item[]) new Object[this.items.length * 2];
			for (int i = 0; i < this.items.length; i++) {
				newItems[i] = this.items[i];
			}
			this.items = newItems;
		} else if (this.tail <= this.items.length/4 && 2 <= this.items.length / 2) {
			Item[] newItems = (Item[]) new Object[this.items.length / 2];
			for (int i = 0; i < newItems.length; i++) {
				newItems[i] = this.items[i];
			}
			this.items = newItems;
		}
	}
	
	public Item dequeue() {                    // remove and return a random item
		if (this.isEmpty()) throw new NoSuchElementException();
		
		this.resize();
		int random = StdRandom.uniform(0, tail);
		Item item = this.items[random];
		this.items[random] = this.items[--tail];
		this.items[tail] = null;
		return item;
	}
	
	
	public Item sample() {                     // return (but do not remove) a random item
		if (this.isEmpty()) throw new NoSuchElementException();
		
		int random = StdRandom.uniform(0, tail);
		return this.items[random];
	}
	public Iterator<Item> iterator() {         // return an independent iterator over items in random order
		return new RandomizedQueueIterator<Item>();
	}
	
   	private class RandomizedQueueIterator<Item> implements Iterator {
   		private Item[] items;
   		private int tail;
   		
   		public RandomizedQueueIterator() {
   			items = (Item[]) RandomizedQueue.this.items.clone();
   			tail = RandomizedQueue.this.tail;
   			if (this.tail > 0) StdRandom.shuffle(items, 0, tail);  // avoid empty RandomizedQueue shuffle exception
   		}

		@Override
		public boolean hasNext() {
			return this.tail > 0;
		}

		@Override
		public Object next() {
			if (!this.hasNext()) throw new NoSuchElementException();
			
			Item item = (Item) this.items[--tail];
			return item;
		}	
   	}
	
	public static void main(String[] args) {   // unit testing (optional)
		RandomizedQueue<Integer> randQ = new RandomizedQueue<Integer>();
		
		for (int i = 0; i < 4; i++) {
			randQ.enqueue(i);
			StdOut.printf("count|length: %d|%d\n", randQ.tail, ((Object[])randQ.items).length);
		}
		
		StdOut.printf("nested iterator---------\n");
		for (Integer i:randQ) {
			StdOut.printf("%dth: ", i);
			for (Integer j:randQ) {
				StdOut.printf("%d ", j);
			}
			StdOut.println();
		}
	}
}