package ixs180019;// Starter code for SP9

// Change to your netid


import java.util.NoSuchElementException;

/**
 * Program implements a Binary Heap using data structures 'arrays'
 * It consists of following methods: add(), offer(), remove(), poll(), isEmpty(), move(), percolateDown(),  percolateUp(), printHeap(), peek(), min(), parent(),
 * leftChild(), buildHeap(), compare(), resize()
 * @aurthor Ishan Suketu Shah(ixs180019)
 * @author Ayesha Gurnani (ang170003)
 * @param <T>
 * */
public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;
    int maxCapacity;
    boolean resize;

    // Constructor for building an empty priority queue using natural ordering of T
    /**
     * Constructor of ixs180019.BinaryHeap
     * @param maxCapacity: Capacity of the heap
     * @return null
     * */
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
        resize = false;
        this.maxCapacity = maxCapacity;
    }

    // add method: resize pq if needed
    /**
     * Method: add()
     * Adds element to the queue. Returns TRUE after element is added. Also resizes the priority queue if needed.
     * @param x: Element to be added
     * @return: boolean values, True or False
     * */
    public boolean add(T x) {
        if(size == maxCapacity){
            resize();
        }
        move(size,x);
        percolateUp(size);
        size += 1;
        return true;
    }

    /**
     * Method: offer()
     * Calls add method. Return false if element is not added else return true
     * @param x: Element to be added
     * @return: boolean values, True or False
     * */
    public boolean offer(T x) {
	return add(x);
    }

    // throw exception if pq is empty
    /**
     * Method: remove()
     * Calls poll() method, save and return result.
     * @param: null
     * @return: If the queue is empty then throw exception else returns result from the poll() method.
     * */
    public T remove() throws NoSuchElementException {
        T result = poll();
        if(result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    // return null if pq is empty
    /**
     * Method: remove()
     * Returns and remove element with the highest priority(minimum element)
     * @param: null
     * @return: If the queue is empty then return 'null' else return element form top of the queue.
     * */
    public T poll() {

        if (isEmpty()) {
            return null;
        }
        T min = min();
        move(0, pq[--size]);
        percolateDown(0);
        return min;

    }

    /**
     * Method: min()
     * Calls peek() method
     * @param: null
     * @return: return the result of peek() method
     * */
    public T min() { 
	return peek();
    }

    // return null if pq is empty
    /**
     * Method: peek()
     * Returns element with the highest priority(minimum element)
     * @param: null
     * @return: If the queue is empty then return 'null' else return element form top of the queue.
     * */
    public T peek() {

        if(isEmpty() == true){
            return null;
        }
        else{
            return (T) pq[0];
        }

    }

    /**
     * Method: parent()
     * Returns parent of the node
     * @param: Integer i; node whose parent is to be found
     * @return: parent of the element i
     * */
    int parent(int i) {
	return (i-1)/2;
    }

    /**
     * Method: leftChild()
     * Returns left child of the node
     * @param: Integer i; node whose left child is to be found
     * @return: left child of the element i
     * */
    int leftChild(int i) {
	return 2*i + 1;
    }

    /** pq[index] may violate heap order with parent */
    /**
     * Method: percolateUp()
     * Moves element to its position in the queue according to its priority, after adding an element
     * @param: Integer index; position from where we need to percolate up
     * @return: null
     * */
    void percolateUp(int index) {
        T x = ((T)pq[index]);
        while(compare(index,0) > 0 && (compare(pq[parent(index)],x)) > 0){
            move(index , pq[parent(index)]);
            index = parent(index);
        }
        move(index,x);
    }

    /** pq[index] may violate heap order with children */
    /**
     * Method: percolateDown()
     * Moves element to its position in the queue according to its priority, after removing the minimum element
     * @param: Integer index; position from where we need to percolate down
     * @return: null
     * */
    void percolateDown(int index) {
        T x = ((T) pq[index]);
        int small = leftChild(index);
        while (small <= size-1) {
            if ((small < size-1) && compare(pq[small], pq[small+1]) > 0) {
                small++;
            }
            if (compare(x, pq[small]) < 0) {
                break;
            }
            move(index, pq[small]);
            index = small;
            small = leftChild(index);
        }
        move(index, x);
    }

	/** use this whenever an element moved/stored in heap. Will be overridden by IndexedHeap */
    /**
     * Method: move()
     * Moves element x to the dest position in queue
     * @param: Integer dest,  Comparable x
     * @return: null
     * */
    void move(int dest, Comparable x) {
	    pq[dest] = x;
    }

    /**
     * Method: compare()
     * Compares two elements
     * @param: Comparable a, Comparable b
     * @return: Returns a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     * */
    int compare(Comparable a, Comparable b) {
	    return ((T) a).compareTo((T) b);
    }
    
    /** Create a heap.  Precondition: none. */
    /**
     * Method: buildHeap()
     * Builds heap from an array
     * @param: An array of type T
     * @return: null
     * */
    void buildHeap(T[] a) {
        System.arraycopy(a,0,pq,0,size());
        for(int i=parent(size-1); i>=0; i--) {
	        percolateDown(i);
	    }
    }

    /**
     * Method: isEmpty()
     * Checks if the queue is empty
     * @param: null
     * @return: Boolean value, True or False
     * */
    public boolean isEmpty() {
	return size() == 0;
    }

    /**
     * Method: size()
     * Returns size of the queue
     * @param: null
     * @return: Integer value, size of the queue
     * */
    public int size() {
	return size;
    }

    // Resize array to double the current size
    /**
     * Method: resize()
     * Resizes the queue
     * @param: null
     * @return: null
     * */
    void resize() {
        Comparable[] pq1 = new Comparable[maxCapacity];
        System.arraycopy(pq,0 , pq1 ,0,size());
        pq = new Comparable[maxCapacity*2];
        System.arraycopy(pq1,0 , pq ,0,pq1.length);
        resize = true;
    }

    /**
     * Method: printHeap()
     * prints the elements of queue
     * @param: null
     * @return: null
     * */
    void printHeap(){
        for(Comparable x:pq){
            System.out.print(" " + x);
        }
    }
    
    public interface Index {
        public void putIndex(int index);
        public int getIndex();
    }

    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {
        /** Build a priority queue with a given array */
        IndexedHeap(int capacity) {
            super(capacity);
	}

        /** restore heap order property after the priority of x has decreased */
        void decreaseKey(T x) {
            int index = x.getIndex();
            percolateUp(index);
        }

    	@Override
        void move(int i, Comparable x) {
            super.move(i, x);
            ((T) x).putIndex(i);
        }
    }

    public static void main(String[] args) {
	Integer[] arr = {0,9,7,5,3,1,8,6,4,2,11,12,10,17,15,14,16,13,18,19};
	BinaryHeap<Integer> h = new BinaryHeap(10);

	System.out.print("Before:");
	for(Integer x: arr) {
	    h.offer(x);
	    System.out.print(" " + x);

	}

	System.out.println();
	if(h.resize){
        System.out.print("Queue Resized");
    }

	System.out.println();
    System.out.print("Binary Tree:");
	for(int i=0;i<arr.length-1;i++) {
        System.out.print(" " + h.pq[i]);
    }

    System.out.println();
	System.out.println("Polling and Peeking:");
	System.out.print("Heap:");
	h.printHeap();
	System.out.println();
	for(int i=0; i<arr.length; i++) {
        System.out.println("Peek:" + h.min());
        arr[i] = h.poll();
        System.out.print("Heap:");
        h.printHeap();
        System.out.println();
	}

    System.out.println();
    System.out.print("Sorted array :");
	for(Integer x: arr) {
	    System.out.print(" " + x);
	}
	System.out.println();
    }
}
