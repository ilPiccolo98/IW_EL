package elplusplus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;

public class Normalizer 
{
	public Normalizer(OWLOntology ontology, OWLDataFactory dataFactory, Set<GCI> expressions, IRI IOR)
	{
		this.ontology = ontology;
		this.phaseOneExpressions = new LinkedList<GCI>(expressions);
		this.phaseTwoExpressions = new LinkedList<GCI>();
		normalizedExpressions = new HashSet<GCI>();
		this.dataFactory = dataFactory;
		this.IOR = IOR;
	}
	
	private void PhaseOne()
	{
		while(!phaseOneExpressions.isEmpty())
		{
			GCI current_gci = phaseOneExpressions.element();
			phaseOneExpressions.remove();
			OWLClassExpression lhs = current_gci.getSubClass();
			OWLClassExpression rhs = current_gci.getSuperClass();
			if(lhs.isTopEntity() || lhs.isOWLClass() || lhs.isIndividual())
			{
				//L'operatore a destra è bottom o classe o individuo
				//qindi è in forma normale
				if(rhs.isBottomEntity() || rhs.isOWLClass() || rhs.isIndividual() ||  rhs.isTopEntity())
					normalizedExpressions.add(current_gci);
				//L'operatore a destra è un some values from
				else if (rhs.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM)
				{
					OWLObjectSomeValuesFrom someValuesFrom = (OWLObjectSomeValuesFrom) rhs;
			        OWLClassExpression fillerClass = someValuesFrom.getFiller();
			        //Se la classe filler appartiene a BC, l'espressione è in forma normale
			        if(Utilities.isInBC(fillerClass))
			        	normalizedExpressions.add(current_gci);
			        //Altrimenti viene elaborata nella fase 2
			        else
			        	phaseTwoExpressions.add(current_gci);
				}
				//Altrimenti viene elaborata nella fase 2
				else
					phaseTwoExpressions.add(current_gci);
			}
			//l'operatore a sinistra è una congiunzione
			else if(lhs.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF)
			{
				OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) lhs;
                ArrayList<OWLClassExpression> operands = new ArrayList<OWLClassExpression>(intersection.getOperands());
                OWLClassExpression leftOperand = operands.get(0);
                OWLClassExpression rightOperand = operands.get(1);
                //se gli operandi della congiunzione sono in BC è in forma normale
                if(Utilities.isInBC(leftOperand) && Utilities.isInBC(rightOperand))
                	normalizedExpressions.add(current_gci);
                //se l'operatore a sx della congiunzione non è in BC applica NF2
                else if(!Utilities.isInBC(leftOperand))
                {
                	String uniqueID = UUID.randomUUID().toString();
                	OWLClass newName = dataFactory.getOWLClass(IOR + "#newName" + uniqueID);
                	OWLObjectIntersectionOf newIntersection = dataFactory.getOWLObjectIntersectionOf(rightOperand, newName);
                    phaseOneExpressions.add(new GCI(newIntersection, rhs));
                    phaseOneExpressions.add(new GCI(leftOperand, newName));
                }
                //se l'operatore a dx della congiunzione non è in BC applica NF2
                else if(!Utilities.isInBC(rightOperand))
                {
                	String uniqueID = UUID.randomUUID().toString();
                	OWLClass newName = dataFactory.getOWLClass(IOR + "#newName" + uniqueID);
                	OWLObjectIntersectionOf newIntersection = dataFactory.getOWLObjectIntersectionOf(leftOperand, newName);
                	phaseOneExpressions.add(new GCI(newIntersection, rhs));
                	phaseOneExpressions.add(new GCI(rightOperand, newName));
                }
                //nessuna regola da applicare, passa alla fase 2
                else
                	phaseTwoExpressions.add(current_gci);
			}
			//l'operatore a sinistra è un esistenziale
			else if(lhs.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM)
			{
				OWLObjectSomeValuesFrom someValuesFrom = (OWLObjectSomeValuesFrom) lhs;
				OWLClassExpression filler = someValuesFrom.getFiller();
				//Se l'espressione è in forma normale
				if(Utilities.isInBC(filler) && Utilities.isInBCOrBottom(rhs))
					normalizedExpressions.add(current_gci);
				//Non si può applicare nessuna regola, si passa alla fase 2
				else if(Utilities.isInBC(filler) && !Utilities.isInBC(rhs))
					phaseTwoExpressions.add(current_gci);
				//Applica la regola NF3
				else if(!Utilities.isInBC(filler))
				{
					String uniqueID = UUID.randomUUID().toString();
                	OWLClass newName = dataFactory.getOWLClass(IOR + "#newName" + uniqueID);
                	OWLObjectPropertyExpression property = someValuesFrom.getProperty();
                	OWLObjectSomeValuesFrom newSomeValuesFrom = dataFactory.getOWLObjectSomeValuesFrom(property, newName);
                	phaseOneExpressions.add(new GCI(newSomeValuesFrom, rhs));
                	phaseOneExpressions.add(new GCI(filler, newName));
				}
			}
		}
	}
	
	private OWLOntology ontology;
	private Queue<GCI> phaseOneExpressions;
	private Queue<GCI> phaseTwoExpressions;
	private Set<GCI> normalizedExpressions;
	private OWLDataFactory dataFactory;
	private IRI IOR;
}
