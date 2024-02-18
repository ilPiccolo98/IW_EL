package elplusplus;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
public class Application 
{
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException 
	{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File file = new File("C:\\Users\\Pc\\Desktop\\java projects\\elplusplus\\F1.rdf");
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        ELPlusPlusReasoner reasoner = new ELPlusPlusReasoner(ontology);
        
        //VEICOLI QUERIES-------------------------------------
        //VeicoliQueries.isScuderiaCampionatoF1SubClassOfScuderia(ontology, reasoner);
        //VeicoliQueries.isPilotaDiGrigliaSubClassOfScuderia(ontology, reasoner);
        //VeicoliQueries.isTeamPrincipalSubClassOfiscrittoASomeCampionatoF1(ontology, reasoner);
        //VeicoliQueries.isPilotaF1SubClassOfPerson(ontology, reasoner);
        //VeicoliQueries.isPilotaDiGrigliaSubClassOfAdulto(ontology, reasoner);
        //VeicoliQueries.isPilotaDiGrigliaSubClassOftrasfertaSomeMonza(ontology, reasoner);
        //VeicoliQueries.isPilotaCollaudatoreSubClassOftrasfertaSomeMonza(ontology, reasoner);
        //VeicoliQueries.isScuderiaCampionatoF1SubClassOfIndividualWithTeamPrincipalCapoMeccanicoCapoIngengere(ontology, reasoner);
	}
}
