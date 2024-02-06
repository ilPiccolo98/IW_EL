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
	public Normalizer(OWLOntology ontology, OWLDataFactory dataFactory, Set<GCI> expressions)
	{
		this.ontology = ontology;
		this.phaseOneExpressions = new LinkedList<GCI>(expressions);
		this.phaseTwoExpressions = new LinkedList<GCI>();
		normalizedExpressions = new HashSet<GCI>();
		this.dataFactory = dataFactory;
		this.IOR = ontology.getOntologyID().getOntologyIRI().get();
	}
	
	public void execute()
	{
		PhaseOne();
	}
	
	public Set<GCI> getNormalizedExpressions()
	{
		return normalizedExpressions;
	}
	
	public Set<GCI> Expressions()
	{
		return new HashSet<GCI>(phaseOneExpressions);
	}
	
	public void setExpressions(Set<GCI> expressions)
	{
		this.phaseOneExpressions = new LinkedList<GCI>(expressions);
	}
	
	public Set<GCI> getPhaseTwoExpressions()
	{
		return new HashSet<GCI>(phaseTwoExpressions);
	}
	
	private void PhaseOne()
	{
		System.out.println("Phase One algorithm------------------");
		while(!phaseOneExpressions.isEmpty())
		{
			GCI current_gci = phaseOneExpressions.element();
			System.out.println("Current formula: " + current_gci);
			phaseOneExpressions.remove();
			OWLClassExpression lhs = current_gci.getSubClass();
			OWLClassExpression rhs = current_gci.getSuperClass();
			if((lhs.isTopEntity() || lhs.isOWLClass() || lhs.isIndividual()) && !lhs.isBottomEntity())
			{
				System.out.println("First if");
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
			//l'operatore a sinistra è il bottom
			else if(lhs.isBottomEntity())
			{
				System.out.println("Second if");
			}
			//l'operatore a sinistra è una congiunzione
			else if(lhs.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF)
			{
				System.out.println("Third if");
				OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) lhs;
                ArrayList<OWLClassExpression> operands = new ArrayList<OWLClassExpression>(intersection.getOperands());
                //se gli operandi della congiunzione sono in BC è in forma normale
                if(operands.size() == 2 && Utilities.isInBC(operands.get(0)) && Utilities.isInBC(operands.get(1)))
                {
                	System.out.println("It's in normal form");
                	normalizedExpressions.add(current_gci);
                }
                //se ci sono pià di 2 operatori e almeno 1 è in forma normale applica NF2
                else if(operands.size() >= 2 && isThereSomeBCOperandInConjunction(operands))
                {
                	System.out.println("Some operands are not in DC");
                	OWLClassExpression BCoperand = getOperandInBCFromConjunction(operands);
                	operands.remove(BCoperand);
                	String uniqueID = UUID.randomUUID().toString();
                	OWLClass newName = dataFactory.getOWLClass(IOR + "#newName" + uniqueID);
                	OWLObjectIntersectionOf newIntersection1 = dataFactory.getOWLObjectIntersectionOf(BCoperand, newName);
                	OWLObjectIntersectionOf newIntersection2 = dataFactory.getOWLObjectIntersectionOf(operands);
                	phaseOneExpressions.add(new GCI(newIntersection1, rhs));
                    phaseOneExpressions.add(new GCI(newIntersection2, newName));
                }
                //nessuna regola da applicare, passa alla fase 2
                else
                	phaseTwoExpressions.add(current_gci);
			}
			//l'operatore a sinistra è un esistenziale
			else if(lhs.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM)
			{
				System.out.println("Fourth if");
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
	
	private void PhaseTwo()
	{
		while(!phaseTwoExpressions.isEmpty())
		{
			GCI current_gci = phaseOneExpressions.element();
			phaseTwoExpressions.remove();
			OWLClassExpression lhs = current_gci.getSubClass();
			OWLClassExpression rhs = current_gci.getSuperClass();
			//se lhs e rhs non sono in BC applica NF5
			if(!Utilities.isInBC(lhs) && !Utilities.isInBC(rhs))
			{
				String uniqueID = UUID.randomUUID().toString();
            	OWLClass newName = dataFactory.getOWLClass(IOR + "#newName" + uniqueID);
            	phaseTwoExpressions.add(new GCI(lhs, newName));
            	phaseTwoExpressions.add(new GCI(newName, rhs));
			}
			else if(rhs.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM)
			{
				OWLObjectSomeValuesFrom someValuesFrom = (OWLObjectSomeValuesFrom) rhs;
				OWLClassExpression filler = someValuesFrom.getFiller();
				//errore
				if(Utilities.isInBC(filler) && !Utilities.isInBC(lhs))
					System.out.println("Found existential restriction on RHS, but the LHS is not in BCD in normalization phase 2. MISTAKE");
				//forma normale
				else if(Utilities.isInBC(filler) && Utilities.isInBC(lhs))
					normalizedExpressions.add(current_gci);
				//NF6
				else if(!Utilities.isInBC(filler))
				{
					String uniqueID = UUID.randomUUID().toString();
	            	OWLClass newName = dataFactory.getOWLClass(IOR + "#newName" + uniqueID);
	            	OWLObjectPropertyExpression property = someValuesFrom.getProperty();
                	OWLObjectSomeValuesFrom newSomeValuesFrom = dataFactory.getOWLObjectSomeValuesFrom(property, newName);
                	phaseTwoExpressions.add(new GCI(lhs, newSomeValuesFrom));
                	phaseTwoExpressions.add(new GCI(newName, filler));
				}
			}
			else if(rhs.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF)
			{
				OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) rhs;
                ArrayList<OWLClassExpression> operands = new ArrayList<OWLClassExpression>(intersection.getOperands());
                OWLClassExpression leftOperand = operands.get(0);
                OWLClassExpression rightOperand = operands.get(1);
            	phaseTwoExpressions.add(new GCI(lhs, leftOperand));
            	phaseTwoExpressions.add(new GCI(lhs, rightOperand));
			}
			else
			{
				//primo caso: lhs è in BC e rhs è in BC o è il bottom
				if(Utilities.isInBC(lhs) && Utilities.isInBCOrBottom(rhs))
					normalizedExpressions.add(current_gci);
				//secondo caso: lhs è una congiunzione, C1 e C2 devono essere in BC
				//e D deve essere in BC o bottom
				else if(lhs.getClassExpressionType() == ClassExpressionType.OBJECT_INTERSECTION_OF && Utilities.isInBCOrBottom(rhs))
				{
					OWLObjectIntersectionOf intersection = (OWLObjectIntersectionOf) lhs;
	                ArrayList<OWLClassExpression> operands = new ArrayList<OWLClassExpression>(intersection.getOperands());
	                OWLClassExpression leftOperand = operands.get(0);
	                OWLClassExpression rightOperand = operands.get(1);
	                if(Utilities.isInBC(leftOperand) && Utilities.isInBC(rightOperand))
	                	normalizedExpressions.add(current_gci);
	                else
	                	System.out.println("Normalization phase two: Found conjunction on LHS, but its parts are not both in the BCD");
				}
				//il caso 3 non deve essere controllato: esistenziale su rhs, già controllato prima
				//caso 4: esistenziale si lhs, il concetto deve essere in BC ed rhs deve essere in BC o bottom
				else if(lhs.getClassExpressionType() == ClassExpressionType.OBJECT_SOME_VALUES_FROM && Utilities.isInBCOrBottom(rhs))
				{
					OWLObjectSomeValuesFrom someValuesFrom = (OWLObjectSomeValuesFrom) lhs;
					OWLClassExpression filler = someValuesFrom.getFiller();
					if(Utilities.isInBC(filler))
						normalizedExpressions.add(current_gci);
					else
						System.out.println("Normalization phase two: Found existential on LHS, but its concept is not in BCD: MISTAKE");
				}
			}
		}
	}
	
	private OWLClassExpression getOperandInBCFromConjunction(ArrayList<OWLClassExpression> operands)
	{
		for(OWLClassExpression operand : operands)
		{
			if(Utilities.isInBC(operand))
				return operand;			
		}
		return null;
	}
	
	private boolean isThereSomeBCOperandInConjunction(ArrayList<OWLClassExpression> operands)
	{
		for(OWLClassExpression operand : operands)
			if(Utilities.isInBC(operand))
				return true;
		return false;
	}
	
	private OWLOntology ontology;
	private Queue<GCI> phaseOneExpressions;
	private Queue<GCI> phaseTwoExpressions;
	private Set<GCI> normalizedExpressions;
	private OWLDataFactory dataFactory;
	private IRI IOR;
}
