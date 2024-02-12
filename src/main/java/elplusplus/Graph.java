package elplusplus;

import java.util.*;

import org.semanticweb.owlapi.model.OWLObject;

public class Graph {
    private Map<OWLObject, List<OWLObject>> adjacencyList;
    private Map<OWLObject, Set<OWLObject>> reachableNodes;
    
    public Graph()
    {
    	adjacencyList = new HashMap<OWLObject, List<OWLObject>>();
    	reachableNodes = new HashMap<OWLObject, Set<OWLObject>>();
    }
    
    public Set<OWLObject> getVerteces()
    {
    	return adjacencyList.keySet();
    }
    
    public boolean addVertex(OWLObject vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new LinkedList<>());
            return true;
        } else {
            return false;
        }
    }

    public void addEdge(OWLObject source, OWLObject destination) {
        if (!adjacencyList.containsKey(source)) {
            throw new RuntimeException("Source vertex does not exist");
        }
        if (!adjacencyList.containsKey(destination)) {
            throw new RuntimeException("Destination vertex does not exist");
        }
        List<OWLObject> sourceNeighbours = adjacencyList.get(source);
        if (!sourceNeighbours.contains(destination)) {
            sourceNeighbours.add(destination);
        }
    }

    public boolean hasVertex(OWLObject vertex) {
        return adjacencyList.containsKey(vertex);
    }

    public boolean hasEdge(OWLObject source, OWLObject destination) {
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
        for (OWLObject vertex : adjacencyList.keySet()) {
            count += adjacencyList.get(vertex).size();
        }
        return count;
    }

    public List<OWLObject> getAdjacentNodes(OWLObject vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            throw new RuntimeException("Vertex does not exist");
        }
        return adjacencyList.get(vertex);
    }

    public void removeVertex(OWLObject vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            throw new RuntimeException("Vertex does not exist");
        }
        adjacencyList.remove(vertex);
        for (OWLObject v : adjacencyList.keySet()) {
            adjacencyList.get(v).remove(vertex);
        }
        reachableNodes.clear();
    }

    public void removeEdge(OWLObject source, OWLObject destination) {
        if (!adjacencyList.containsKey(source)) {
            throw new RuntimeException("Source vertex does not exist");
        }
        if (!adjacencyList.containsKey(destination)) {
            throw new RuntimeException("Destination vertex does not exist");
        }
        adjacencyList.get(source).remove(destination);
        reachableNodes.clear();
    }

    private Set<OWLObject> BFS(OWLObject source)
    {
    	Queue<OWLObject> queue = new LinkedList<OWLObject>();
    	Set<OWLObject> reachedNodes = new HashSet<OWLObject>();
    	queue.add(source);
    	while(!queue.isEmpty())
    	{
    		OWLObject current_node = queue.remove();
    		for(OWLObject adjacent_node : adjacencyList.get(current_node))
    			if(!reachedNodes.contains(adjacent_node))
    			{
    				queue.add(adjacent_node);
    				reachedNodes.add(adjacent_node);
    			}
    	}
    	return reachedNodes;
    }
    
    public void initAdjacentNodes(OWLObject source)
    {
    	reachableNodes.put(source, BFS(source));
    }

    public boolean hasPathBetween(OWLObject source, OWLObject destination)
    {
        return reachableNodes.get(source) != null && reachableNodes.get(source).contains(destination);
    }
}
