package elplusplus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Graph<T> {
    private Map<T, List<T>> adjacencyList;

    public boolean addVertex(T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new LinkedList<>());
            return true;
        } else {
            return false;
        }
    }

    public void addEdge(T source, T destination) {
        if (!adjacencyList.containsKey(source)) {
            throw new RuntimeException("Source vertex does not exist");
        }
        if (!adjacencyList.containsKey(destination)) {
            throw new RuntimeException("Destination vertex does not exist");
        }
        adjacencyList.get(source).add(destination);
    }

    public boolean hasVertex(T vertex) {
        return adjacencyList.containsKey(vertex);
    }

    public boolean hasEdge(T source, T destination) {
        if (adjacencyList.containsKey(source) && adjacencyList.containsKey(destination)) {
            return adjacencyList.get(source).contains(destination);
        } else {
            return false;
        }
    }

    public int vertexNumber(){
        return adjacencyList.size();
    }

    public int edgeNumber(){
        int count = 0;
        for (T vertex : adjacencyList.keySet()) {
            count += adjacencyList.get(vertex).size();
        }
        return count;
    }

    public List<T> getAdjacentNodes(T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            throw new RuntimeException("Vertex does not exist");
        }
        return adjacencyList.get(vertex);
    }

    public void removeVertex(T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            throw new RuntimeException("Vertex does not exist");
        }
        adjacencyList.remove(vertex);
        for (T v : adjacencyList.keySet()) {
            adjacencyList.get(v).remove(vertex);
        }
    }

    public void removeEdge(T source, T destination) {
        if (!adjacencyList.containsKey(source)) {
            throw new RuntimeException("Source vertex does not exist");
        }
        if (!adjacencyList.containsKey(destination)) {
            throw new RuntimeException("Destination vertex does not exist");
        }
        adjacencyList.get(source).remove(destination);
    }
    
    public boolean BFS(T source, T destination)
    {
    	Queue<T> queue = new LinkedList<T>();
    	queue.add(source);
    	while(!queue.isEmpty())
    	{
    		T current_node = queue.remove();
    		for(T adjacent_node : adjacencyList.get(current_node))
    			if(adjacent_node.equals(destination))
    				return true;
    	}
    	return false;
    }
}
