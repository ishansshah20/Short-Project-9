/**
 * Minimum Spanning Tree
 * This program implements Minimum Spanning Tree using Boruvka's and Prim's algorithm
 *
 * @author Ayesha Gurnani      ang170003
 * @author Ishan Shah     ixs180019
 * @version 1.0
 * @since 2020-04-12
 */

package ixs180019;

import ixs180019.BinaryHeap.Index;
import ixs180019.BinaryHeap.IndexedHeap;
import ixs180019.Graph.Edge;
import ixs180019.Graph.Factory;
import ixs180019.Graph.GraphAlgorithm;
import ixs180019.Graph.Vertex;
import ixs180019.Graph.Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class MST extends GraphAlgorithm<MST.MSTVertex> {

    String algorithm;
    public long wmst;
    List<Edge> mst;
    Graph F;
    
    MST(Graph g) {
	super(g, new MSTVertex((Vertex) null));
    F = new Graph(g.n); //Graph with only vertices
    }

	/**
	 * MSTVertex class
	 */
    public static class MSTVertex implements Index, Comparable<MSTVertex>, Factory {
		int state;
		int component;
		Vertex parent;
		int index;
		boolean seen;
		Vertex vertex;
		Edge incoming_edge;
		int d;

		MSTVertex(Vertex u) {
		this.vertex = u;
	}

		MSTVertex(MSTVertex u) {  // for prim2
		this(u.vertex);
	}

		/**
		 * Returns new MSTVertex
		 * @param u: object Vertex
		 * @return MSTVertex
		 */
		public MSTVertex make(Vertex u) { return new MSTVertex(u); }

		/**
		 * Puts the index of MSTVertex in IndexedHeap
		 * @param index index which is set
		 */
		public void putIndex(int index) { this.index = index; }

		/**
		 * Returns index of MSTVertex in IndexedHeap
		 * @return int index
		 */
		public int getIndex() { return index; }

		/**
		 * Compares priority(d) of one MSTVertex to other
		 * @param other: object MSTVertex
		 * @return int 1 if other is null or its priority(d) is less than current(this) MSTVertex,
		 * -1 if its priority(d) is greater than current(this) MSTVertex and 0 if same priority.
		 */
		public int compareTo(MSTVertex other) {
			if(other == null || this.d > other.d)
				return 1;
			else if(this.d < other.d)
				return -1;
			return 0;
		}
    }

	/**
	 * Labels each vertex with its component
	 * and counts total components
	 * @param f: Graph whose components we need to count and label
	 * @return total resulting components
	 */
    public int countAndLabel(Graph f){
    	int count = 0;
    	for (Vertex v: f){
    		get(v).state = 0;
		}
		for(Vertex v: f){
			if(get(v).state == 0){
				count += 1;
				label(f,v,count);
			}
		}
		return count;
	}

	/**
	 * Label the vertices that can be reached from src
	 * in the same component as src
	 * @param v Vertex: source vertex
	 * @param count int: component to label vertices
	 * @param f Graph: whose vertices we need to label
	 */
	public void label(Graph f, Vertex v, int count) {
		Stack<Vertex> bag = new Stack<>();
		bag.push(v);
		while (!bag.isEmpty()) {
			Vertex temp = bag.pop();
			if (get(temp).state == 0) {
				get(temp).state = 1;
				get(temp).component = count;
				if(f.outEdges(temp)!= null) {
					for (Edge e : f.outEdges(temp)) {
						bag.push(e.otherEnd(temp));
					}
				}
			}
		}
	}

	/**
	 * Find edges in g that are safe edges in F and add
	 * them to F and add their weight to wmst
	 * @param F Graph: graph in which we need to add safe edges
	 * @param count int: number of components in F
	 */
	public void AddSafeEdges(Edge[] E, Graph F, int count){
		Edge[] safe = new Edge[count+1];
		for(int i=1; i <= count; i++)
			safe[i]= null;
		for(Edge e: E){
			Vertex u = e.fromVertex();
			Vertex v = e.toVertex();
			int uComp = get(u).component;
			int vComp = get(v).component;
			if(uComp != vComp){
				if(safe[uComp] == null || e.compareTo(safe[uComp]) < 0)
					safe[uComp] = e;
				if(safe[vComp] == null || e.compareTo(safe[vComp]) < 0)
					safe[vComp] = e;
			}
		}

		HashSet<Edge> fEdges = new HashSet<>(); //Store the visited edges
		for(int i=1; i < safe.length ; i++) {
			if(safe[i] != null && !fEdges.contains(safe[i])) {
				F.addEdge(safe[i].fromVertex(), safe[i].toVertex(), safe[i].weight, safe[i].name);
				fEdges.add(safe[i]);
				wmst += safe[i].weight;
			}
			else
			    continue;
		}
    }

	/**
	 * Boruvka algorithm for finding MST
	 * @return long wmst of the given graph (this.g)
	 */
    public long boruvka() {
		algorithm = "Boruvka";
		wmst = 0;
		int count = countAndLabel(this.F);
		while(count > 1){
			AddSafeEdges(g.getEdgeArray() ,this.F, count);
			count = countAndLabel(this.F);
		}
		return wmst;
    }

	/**
	 * Prim's algorithm (Implementation #2) to find MST of the given graph (this.g)
	 * @param s source vertex of the graph(this.g)
	 * @return long wmst of the given graph (this.g)
	 */
	public long prim2(Vertex s) {
		algorithm = "Prim indexed heaps";
		mst = new LinkedList<>();
		wmst = 0;
		IndexedHeap<MSTVertex> q = new IndexedHeap<>(g.size());

		for (Vertex u : g) {
			get(u).seen = false;
			get(u).parent = null;
			get(u).d = Integer.MAX_VALUE;
		}

		get(s).d = 0;
		get(s).seen = true;

		for (Vertex u : g) {
			q.add(get(u));
		}

		while(!q.isEmpty()) {
			MSTVertex u = q.remove();
			Vertex ver = u.vertex;
			u.seen = true;
			wmst = wmst + u.d;

			if (u.parent != null) {
				mst.add(u.incoming_edge);
			}

			for(Edge e : g.incident(ver)) {
				Vertex v = e.otherEnd(ver);
				if(!get(v).seen && e.getWeight() < get(v).d) {
					get(v).d = e.getWeight();
					get(v).parent = ver;
					get(v).incoming_edge = e;
					q.decreaseKey(get(v));
				}
			}
		}
		return wmst;
    }

	/**
	 * To choose an algorithm you want to use to find MST and wmst
	 * @param g Graph: graph whose MST and wmst you want to find
	 * @param s Vertex: source vertex of the given graph(this.g)
	 * @param choice int: algorithm you want to choose
	 * @return MST object
	 */
    public static MST mst(Graph g, Vertex s, int choice) {
		MST m = new MST(g);
		switch(choice) {
			case 0:
				m.boruvka();
				break;
			case 1:
				m.prim2(s);
				break;
			default:
				break;
		}
		return m;
    }

	/**
	 * Main method to find MST and wmst of the graph
	 * @param args if provided then 1st argument is input file that contains input graph, and second argument is choice
	 *             which indicates algorithm you want to implement
	 * @throws FileNotFoundException
	 */
    public static void main(String[] args) throws IOException {
		Scanner in;
		int choice = 1;  // 0: Boruvka and 1: prims2
		if (args.length == 0 || args[0].equals("-")) {
			in = new Scanner(System.in);
		} else {
			File inputFile = new File(args[0]);
			in = new Scanner(inputFile);
		}

		if (args.length > 1) { choice = Integer.parseInt(args[1]); }

		Graph g = Graph.readGraph(in);
		Vertex s = g.getVertex(1);

		Timer timer = new Timer();
		MST m = mst(g, s, choice);
		System.out.println(m.algorithm + "\n" + m.wmst);
		System.out.println(timer.end());
	}
}
