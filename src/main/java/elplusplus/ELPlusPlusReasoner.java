package elplusplus;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ELPlusPlusReasoner {
    private final Set<GCI> normalizedGCIs;
    private final OWLOntology ontology;
    private final OWLReasoner reasoner;
    private final Graph arrowRelationGraph;
    private Map<OWLObject, Set<OWLObject>> mappingS;
    private Map<OWLProperty, Set<Tuple<OWLObject, OWLObject>>> mappingR;
    private Set<OWLObjectOneOf> oneOfObjects;
    private Normalizer normalizer;

    public ELPlusPlusReasoner(OWLOntology ontology){
        this.ontology = ontology;
        OWLReasonerFactory rf = new ReasonerFactory();
        this.reasoner = rf.createReasoner(ontology);
        Set<GCI> gcis = Utilities.getGCIs(ontology, reasoner);
        this.oneOfObjects = Utilities.getNominalsFromCBox(gcis);
        normalizer = new ElPlusPlusNormalizer(ontology, gcis);
        normalizer.execute();
        this.normalizedGCIs = normalizer.getNormalizedExpressions();
        arrowRelationGraph = new Graph();
        initializeMappingS();
        initializeMappingR();
    }
    
    public void execute()
    {
    	useCompletionRules();
    }
    
    public boolean subsumption(OWLObject subclass, OWLObject superclass)
    {
    	if(checkFirstConditionOfSubsumption(subclass, superclass) || checkSecondConditionOfSubsumption())
    		return true;
    	return false;
    }
    
    public void printMappingS()
    {
        System.out.println("Mapping S-----------------------------------------");
    	for(OWLObject key : mappingS.keySet())
    	{
    		System.out.print("\nS(" + Utilities.prettyPrint(key) + "): ");
    		for(OWLObject value : mappingS.get(key))
    			System.out.print(Utilities.prettyPrint(value) + ", ");
    	}
        System.out.println("\n");
    }
    
    public void printMappingR()
    {
        System.out.println("Mapping R-----------------------------------------");
    	for(OWLProperty key : mappingR.keySet())
    	{
    		System.out.print("\nR(" + Utilities.prettyPrint(key) + "): ");
    		for(Tuple<OWLObject, OWLObject> value : mappingR.get(key))
    			System.out.print("(" + Utilities.prettyPrint(value.getFirst()) + ", " + Utilities.prettyPrint(value.getSecond()) + "), ");
    	}
        System.out.println("\n");
    }
    
    private boolean checkFirstConditionOfSubsumption(OWLObject subclass, OWLObject superclass)
    {
    	OWLClass bottom = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNothing();
    	if(mappingS.get(subclass).contains(superclass) || mappingS.get(subclass).contains(bottom))
    		return true;
    	return false;
    }

    private boolean checkSecondConditionOfSubsumption()
    {
    	AtomicBoolean found = new AtomicBoolean(false);
    	OWLClass bottom = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNothing();
    	oneOfObjects.forEach(oneOfObject -> {
    		if(mappingS.get(oneOfObject) != null && mappingS.get(oneOfObject).contains(bottom))
    			found.set(true);
    	});
    	return found.get();
    }
    
    private void useCompletionRules(){
        boolean change = false;
        do {
        	change = false;
            for (GCI gci: normalizedGCIs) {
                if (isCR1Applied(gci)){
                    change = true;
                } else if (isCR2Applied(gci)){
                    change = true;
                } else if (isCR3Applied(gci)){
                    change = true;
                } else if (isCR4Applied(gci)){
                    change = true;
                }
            }
        } while (change);
        applyCR5();
        initGraph();
        initReachabilityMatrix();
        applyCR6();
    }

    private boolean isCR1Applied(GCI gci) {
    	AtomicBoolean found = new AtomicBoolean(false);
        OWLObject lhs = gci.getSubClass(); // C'
        OWLObject rhs = gci.getSuperClass(); // D
        // checks if lhs and rhs are simple concepts
        if ((lhs instanceof OWLClass || lhs instanceof OWLObjectOneOf) && 
        		(rhs instanceof OWLClass || rhs instanceof OWLObjectOneOf))
        {	
            Map<OWLObject, OWLObject> toBeAddedToMappingS = new HashMap<>();
            mappingS.forEach((C, S_di_C) -> {
                if (S_di_C.contains(lhs) && !S_di_C.contains(rhs)){
                	toBeAddedToMappingS.put(C, rhs);
                    found.set(true);
                }
            });
            toBeAddedToMappingS.forEach((C, D) -> mappingS.get(C).add(D));
        }
        return found.get();
    }

    private boolean isCR2Applied(GCI gci) {
    	AtomicBoolean found = new AtomicBoolean(false);
        OWLObject lhs = gci.getSubClass(); // C1 ∩ C2
        OWLObject rhs = gci.getSuperClass(); // D
        // checks if lhs is an intersection and rhs is a simple concept
        if (lhs instanceof OWLObjectIntersectionOf && 
        		(rhs instanceof OWLClass || rhs instanceof OWLObjectOneOf)) {
            // checks if all the elements of the intersection are in S(C)
            List<OWLClassExpression> operands = ((OWLObjectIntersectionOf) lhs).getOperandsAsList();
            OWLObject C1 = operands.get(0);
            OWLObject C2 = operands.get(1);
            Map<OWLObject, OWLObject> toBeAddedToMappingS = new HashMap<>();
            mappingS.forEach((C, S_di_C) -> {
                if (S_di_C.contains(C1) && S_di_C.contains(C2) && !S_di_C.contains(rhs)){
                    toBeAddedToMappingS.put(C, rhs);
                    found.set(true);
                }
            });
            toBeAddedToMappingS.forEach((C, D) -> mappingS.get(C).add(D));
        }
        return found.get();
    }

    private boolean isCR3Applied(GCI gci) {
    	AtomicBoolean found = new AtomicBoolean(false);
        OWLObject lhs = gci.getSubClass(); // C'
        OWLObject rhs = gci.getSuperClass(); // ∃r.D
        // checks if lhs is a simple concept and rhs is an existential restriction
        if ((lhs instanceof OWLClass || lhs instanceof OWLIndividual) && 
        		rhs instanceof OWLObjectSomeValuesFrom) {
            // checks if the tuple (C,D) is not in mappingR for the property r
            OWLObject D = ((OWLObjectSomeValuesFrom) rhs).getFiller();
            OWLProperty r = ((OWLObjectProperty) ((OWLObjectSomeValuesFrom) rhs).getProperty());
            mappingS.forEach((C, S_di_C) -> {
                Tuple<OWLObject, OWLObject> C_D = new Tuple<>(C, D);
                Set<Tuple<OWLObject, OWLObject>> R_di_r = mappingR.get(r);
                if (R_di_r == null)
                    throw new RuntimeException("Property " + r + " has not been initialized in mappingR");
                if (S_di_C.contains(lhs) && !R_di_r.contains(C_D)){
                    found.set(true);
                    R_di_r.add(C_D);
                }
            });
        }
        return found.get();
    }

    private boolean isCR4Applied(GCI gci) {
    	AtomicBoolean found = new AtomicBoolean(false);
        OWLObject lhs = gci.getSubClass(); // ∃r.D'
        OWLObject rhs = gci.getSuperClass(); // E

        if (lhs instanceof OWLObjectSomeValuesFrom && 
        		(rhs instanceof OWLClass || rhs instanceof OWLIndividual)) {
            OWLObject D_primo = ((OWLObjectSomeValuesFrom) lhs).getFiller();
            OWLProperty r = ((OWLObjectProperty) ((OWLObjectSomeValuesFrom) lhs).getProperty());
            Set<Tuple<OWLObject, OWLObject>> R_di_r = mappingR.get(r);
            R_di_r.forEach(C_D -> {
                OWLObject C = C_D.getFirst();
                OWLObject D = C_D.getSecond();
                Set<OWLObject> S_di_D = mappingS.get(D);
                Set<OWLObject> S_di_C = mappingS.get(C);
                if (S_di_D.contains(D_primo) && !S_di_C.contains(rhs)){
                    S_di_C.add(rhs);
                    found.set(true);
                }
            });
        }
        return found.get();
    }

    private void applyCR5() {
        mappingR.forEach((r, R_di_r) -> {
            R_di_r.forEach(C_D -> {
                OWLObject C = C_D.getFirst();
                OWLObject D = C_D.getSecond();
                Set<OWLObject> S_di_C = mappingS.get(C);
                Set<OWLObject> S_di_D = mappingS.get(D);
                OWLClass bottom = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLNothing();
                if (S_di_D.contains(bottom) && !S_di_C.contains(bottom)) {
                    S_di_C.add(bottom);
                }
            });
        });
    }

    private void applyCR6() {
    	ArrayList<OWLObject> classes = new ArrayList<OWLObject>(mappingS.keySet());
    	for(OWLObjectOneOf oneOfObject : oneOfObjects)
    		for(int i = 0; i != classes.size(); ++i)
    			if(mappingS.get(classes.get(i)).contains(oneOfObject))
    				for(int j = i + 1; j != classes.size(); ++j)
    					if(mappingS.get(classes.get(j)).contains(oneOfObject))
    					{
    						if(arrowRelationGraph.hasPathBetween(classes.get(i), classes.get(j)))
    						{
    							Set<OWLObject> newValue = mappingS.get(classes.get(i));
    							newValue.addAll(mappingS.get(classes.get(j)));
    						}
    						if(arrowRelationGraph.hasPathBetween(classes.get(j), classes.get(i)))
    						{
    							Set<OWLObject> newValue = mappingS.get(classes.get(j));
    							newValue.addAll(mappingS.get(classes.get(i)));
    						}
    					}
    }

    private void initGraph(){
    	System.out.println("Init graph");
        for (OWLProperty r : mappingR.keySet()){
            Set<Tuple<OWLObject, OWLObject>> R_di_r = mappingR.get(r);
            R_di_r.forEach(C_D -> {
                OWLObject C = C_D.getFirst();
                OWLObject D = C_D.getSecond();
                arrowRelationGraph.addVertex(C);
                arrowRelationGraph.addVertex(D);
                arrowRelationGraph.addEdge(C, D);
            });
        }
    }
    
    private void initReachabilityMatrix()
    {
    	for(OWLObject source : arrowRelationGraph.getVerteces())
    		arrowRelationGraph.initAdjacentNodes(source);
    }

    private void initializeMappingR() {
        mappingR = new HashMap<>();
        Set<OWLProperty> properties = getProperties();
        for (OWLProperty property: properties){
            mappingR.put(property, new HashSet<>());
        }
    }

    private void initializeMappingS() {
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        mappingS = new HashMap<>();
        for(GCI gci: normalizedGCIs){
        	Set<OWLClassExpression> nestedClassOfSubClass =  gci.getSubClass().getNestedClassExpressions();
        	Set<OWLClassExpression> nestedClassOfSuperClass = gci.getSuperClass().getNestedClassExpressions();
        	nestedClassOfSubClass.forEach(expression -> {
        		if(expression.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF || 
        				(expression.isOWLClass() && !expression.isBottomEntity()))
        		{
        			Set<OWLObject> mapped = new HashSet<>();
        			mapped.add(expression); // Adding C to the set
                    mapped.add(factory.getOWLThing()); // Adding ⊤ to the set
                    mappingS.put(expression, mapped);
        		}
        	});
        	nestedClassOfSuperClass.forEach(expression -> {
        		if(expression.getClassExpressionType() == ClassExpressionType.OBJECT_ONE_OF || 
        				(expression.isOWLClass() && !expression.isBottomEntity()))
        		{
        			Set<OWLObject> mapped = new HashSet<>();
        			mapped.add(expression); // Adding C to the set
                    mapped.add(factory.getOWLThing()); // Adding ⊤ to the set
                    mappingS.put(expression, mapped);
        		}
        	});
        }
    }

    private Set<OWLProperty> getProperties() {
        Set<OWLProperty> properties = new HashSet<>();
        // Get the TBox axioms
        Set<OWLObjectProperty> objectProperties = ontology.getObjectPropertiesInSignature();
        for (OWLObjectProperty property : objectProperties) {
        	if(!property.isOWLTopObjectProperty())
        		properties.add(property);
        }
        Set<OWLDataProperty> dataProperties = ontology.getDataPropertiesInSignature();
        for (OWLDataProperty property : dataProperties) {
        	if(!property.isOWLTopDataProperty())
        		properties.add(property);
        }
        return properties;
    }

    public void printProperties(){
        Set<OWLProperty> properties = getProperties();
        for (OWLProperty property: properties){
            System.out.println(property.toString());
        }
    }
}
