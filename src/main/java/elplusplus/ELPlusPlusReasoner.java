package elplusplus;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ELPlusPlusReasoner {
    private final Set<GCI> normalizedGCIs;
    private final OWLOntology ontology;
    private final OWLReasoner reasoner;
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
