package elplusplus;

import org.semanticweb.owlapi.model.OWLClassExpression;

public class GCI 
{
	public GCI(OWLClassExpression subClass, OWLClassExpression superClass)
	{
		this.subClass = subClass;
		this.superClass = superClass;
	}
	
	public OWLClassExpression getSubClass() 
	{
		return subClass;
	}
	
	public void setSubClass(OWLClassExpression subClass)
	{
		this.subClass = subClass;
	}
	
	public OWLClassExpression getSuperClass() 
	{
		return superClass;
	}
	
	public void setSuperClass(OWLClassExpression superClass)
	{
		this.superClass = superClass;
	}

	@Override
	public String toString()
	{
//		return "Subclass: " + subClass.toString() + "; Superclass: " + superClass.toString();
		return Utilities.prettyPrint(subClass) + " ⊑ " + Utilities.prettyPrint(superClass);
	}
	
	
	private OWLClassExpression subClass;
	private OWLClassExpression superClass;
}
