package elplusplus;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.*;

public class ELPlusPlusReasoner {
    private final Set<GCI> normalizedGCIs;
    private final OWLOntology ontology;
    private final OWLReasoner reasoner;
    private final Graph<OWLObject> arrowRelationGraph = new Graph<>();
    private Map<OWLObject, Set<OWLObject>> mappingS;
    private Map<OWLProperty, Set<Tuple<OWLObject, OWLObject>>> mappingR;

    public ELPlusPlusReasoner(OWLOntology ontology, Set<GCI> normalizedGCIs){
        this.ontology = ontology;
        this.normalizedGCIs = normalizedGCIs;
        OWLReasonerFactory rf = new ReasonerFactory();
        this.reasoner = rf.createReasoner(ontology);

        initializeMappingS();
        initializeMappingR();
    }

    private void useCompletionRules(){
        boolean change = false;
        do {
            for (GCI gci: normalizedGCIs) {
                if (isCR1Applied(gci)){
                    change = true;
                } else if (isCR2Applied(gci)){
                    change = true;
                } else if (isCR3Applied(gci)){
                    change = true;
                } else if (isCR4Applied(gci)){
                    change = true;
                } else {
                    change = false;
                }
            }
        } while (change);
        applyCR5();
    }

    private boolean isCR1Applied(GCI gci) {
        OWLObject lhs = gci.getSubClass(); // C'
        OWLObject rhs = gci.getSuperClass(); // D
        // checks if lhs and rhs are simple concepts
        if (lhs instanceof OWLClass && rhs instanceof OWLClass){
            Map<OWLObject, OWLObject> toBeAddedToMappingS = new HashMap<>();
            mappingS.forEach((C, S_di_C) -> {
                if (S_di_C.contains(lhs) && !S_di_C.contains(rhs)){
                    toBeAddedToMappingS.put(C, rhs);
                }
            });
            toBeAddedToMappingS.forEach((C, D) -> mappingS.get(C).add(D));
            return true;
        } else {
            return false;
        }
    }

    private boolean isCR2Applied(GCI gci) {
        OWLObject lhs = gci.getSubClass(); // C1 ∩ C2
        OWLObject rhs = gci.getSuperClass(); // D
        // checks if lhs is an intersection and rhs is a simple concept
        if (lhs instanceof OWLObjectIntersectionOf && rhs instanceof OWLClass) {
            // checks if all the elements of the intersection are in S(C)
            List<OWLClassExpression> operands = ((OWLObjectIntersectionOf) lhs).getOperandsAsList();
            OWLObject C1 = operands.get(0);
            OWLObject C2 = operands.get(1);
            Map<OWLObject, OWLObject> toBeAddedToMappingS = new HashMap<>();
            mappingS.forEach((C, S_di_C) -> {
                if (S_di_C.contains(C1) && S_di_C.contains(C2) && !S_di_C.contains(rhs)){
                    toBeAddedToMappingS.put(C, rhs);
                }
            });
            toBeAddedToMappingS.forEach((C, D) -> mappingS.get(C).add(D));
            return true;
        } else {
            return false;
        }
    }

    private boolean isCR3Applied(GCI gci) {
        OWLObject lhs = gci.getSubClass(); // C1
        OWLObject rhs = gci.getSuperClass(); // ∃r.D
        // checks if lhs is a simple concept and rhs is an existential restriction
        if (lhs instanceof OWLClass && rhs instanceof OWLObjectSomeValuesFrom) {
            // checks if the tuple (C,D) is not in mappingR for the property r
            OWLObject D = ((OWLObjectSomeValuesFrom) rhs).getFiller();
            OWLProperty r = ((OWLObjectProperty) ((OWLObjectSomeValuesFrom) rhs).getProperty());
            mappingS.forEach((C, S_di_C) -> {
                Tuple<OWLObject, OWLObject> C_D = new Tuple<>(C, D);
                Set<Tuple<OWLObject, OWLObject>> R_di_r = mappingR.get(r);
                if (S_di_C.contains(lhs) && (R_di_r == null || !R_di_r.contains(C_D))){
                    if (R_di_r == null){ // credo non possa succedere
                        R_di_r = new HashSet<>();
                        mappingR.put(r, R_di_r);
                    }
                    R_di_r.add(C_D);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    private boolean isCR4Applied(GCI gci) {
        OWLObject lhs = gci.getSubClass(); // ∃r.D'
        OWLObject rhs = gci.getSuperClass(); // E

        if (lhs instanceof OWLObjectSomeValuesFrom && rhs instanceof OWLClass) {
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
                }
            });
            return true;
        } else {
            return false;
        }
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

    }

    private boolean arrowRelationMatches(OWLObject C, OWLObject D){
        // todo: si deve fare qualche altro check?
        return arrowRelationGraph.hasPathBetween(C, D);
    }

    private void initGraph(){
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

    private void initializeMappingR() {
        mappingR = new HashMap<>();
        Set<OWLProperty> properties = getProperties();
        for (OWLProperty property: properties){
            mappingR.put(property, new HashSet<>());
        }
    }

    private void initializeMappingS() {
        Set<GCI> GCIs = Utilities.getGCIs(ontology, reasoner);
        OWLDataFactory factory = ontology.getOWLOntologyManager().getOWLDataFactory();
        mappingS = new HashMap<>();
        for(GCI gci: GCIs){
            if (Utilities.isInBC(gci.getSubClass())){
                Set<OWLObject> mapped = new HashSet<>();
                mapped.add(gci.getSubClass()); // Adding C to the set
                mapped.add(factory.getOWLThing()); // Adding ⊤ to the set
                mappingS.put(gci.getSuperClass(), mapped);
            }
            if (Utilities.isInBC(gci.getSuperClass())){
                Set<OWLObject> mapped = new HashSet<>();
                mapped.add(gci.getSuperClass()); // Adding C to the set
                mapped.add(factory.getOWLThing()); // Adding ⊤ to the set
                mappingS.put(gci.getSuperClass(), mapped);
            }
        }
    }

    private Set<OWLProperty> getProperties() {
        Set<OWLProperty> properties = new HashSet<>();
        // Get the TBox axioms
        Set<OWLLogicalAxiom> tboxAxioms = ontology.getLogicalAxioms();
        // Iterate over the TBox axioms
        for (OWLLogicalAxiom axiom : tboxAxioms) {
            // Check if the axiom is a subclass axiom
            if (axiom instanceof OWLSubClassOfAxiom) {
                OWLSubClassOfAxiom subClassAxiom = (OWLSubClassOfAxiom) axiom;
                // Get the super class expression
                OWLClassExpression superClass = subClassAxiom.getSuperClass();
                // Check if the super class expression is an OWLObjectProperty or OWLDataProperty
                if (superClass instanceof OWLObjectSomeValuesFrom) {
                    OWLObjectSomeValuesFrom someValuesFrom = (OWLObjectSomeValuesFrom) superClass;
                    OWLObjectPropertyExpression propertyExpression = someValuesFrom.getProperty();
                    if (propertyExpression instanceof OWLObjectProperty) {
                        properties.add((OWLObjectProperty) propertyExpression);
                    }
                } else if (superClass instanceof OWLDataSomeValuesFrom) {
                    OWLDataSomeValuesFrom dataSomeValuesFrom = (OWLDataSomeValuesFrom) superClass;
                    OWLDataPropertyExpression propertyExpression = dataSomeValuesFrom.getProperty();
                    if (propertyExpression instanceof OWLDataProperty) {
                        properties.add((OWLDataProperty) propertyExpression);
                    }
                }
            }
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
