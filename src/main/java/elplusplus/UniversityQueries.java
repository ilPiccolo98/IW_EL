package elplusplus;

import org.semanticweb.owlapi.model.*;

public class UniversityQueries {
    public static void student4IsSDevStudentPassedCyberSecExamAndAttendsLogicExam(OWLOntology ontology, ELPlusPlusReasoner reasoner) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLObjectOneOf singleton1 = df.getOWLObjectOneOf(df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Student4")));
        OWLObjectOneOf singleton2 = df.getOWLObjectOneOf(df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Student4")));

        OWLClass conceptA = df.getOWLClass(IRI.create(ontologyIRI + "#SoftwareDevStudent"));
        OWLClass conceptB = df.getOWLClass(IRI.create(ontologyIRI + "#CyberSecurityExam"));
        OWLClass conceptC = df.getOWLClass(IRI.create(ontologyIRI + "#LogicExam"));

        OWLObjectSomeValuesFrom someValuesFrom1 = df.getOWLObjectSomeValuesFrom(
                df.getOWLObjectProperty(IRI.create(ontologyIRI + "#passed")),
                conceptB
        );
        OWLObjectSomeValuesFrom someValuesFrom2 = df.getOWLObjectSomeValuesFrom(
                df.getOWLObjectProperty(IRI.create(ontologyIRI + "#attends")),
                conceptC
        );

        OWLObjectIntersectionOf intersection = df.getOWLObjectIntersectionOf(conceptA, someValuesFrom1, someValuesFrom2);

        boolean result = reasoner.subsumption(singleton1, intersection);
        System.out.println("Query: " + Utilities.prettyPrint(singleton1) + " ⊑ " + Utilities.prettyPrint(intersection));
        System.out.println("Result: " + result);
    }

    public static void staraceTeachesBothSpmeAndSo2(OWLOntology ontology, ELPlusPlusReasoner reasoner) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLObjectOneOf singleton1 = df.getOWLObjectOneOf(df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Starace")));
        OWLObjectOneOf singleton2 = df.getOWLObjectOneOf(df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#SPME")));
        OWLObjectOneOf singleton3 = df.getOWLObjectOneOf(df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#SO2")));

        OWLObjectSomeValuesFrom someValuesFrom1 = df.getOWLObjectSomeValuesFrom(
                df.getOWLObjectProperty(IRI.create(ontologyIRI + "#teaches")),
                singleton2
        );

        OWLObjectSomeValuesFrom someValuesFrom2 = df.getOWLObjectSomeValuesFrom(
                df.getOWLObjectProperty(IRI.create(ontologyIRI + "#teaches")),
                singleton3
        );

        OWLObjectIntersectionOf intersection = df.getOWLObjectIntersectionOf(someValuesFrom1, someValuesFrom2);

        boolean result = reasoner.subsumption(singleton1, intersection);
        System.out.println("Starace teaches both SPME and SO2: " + result);
    }
    public static void student3and4PassedIngSw(OWLOntology ontology, ELPlusPlusReasoner reasoner){
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLIndividual individualStudent3 = df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Student3"));
        OWLIndividual individualStudent4 = df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Student4"));
        OWLObjectOneOf studentSingleton3 = df.getOWLObjectOneOf(individualStudent3);
        OWLObjectOneOf studentSingleton4 = df.getOWLObjectOneOf(individualStudent4);

//        OWLClassExpression passedIngSw = df.getOWLObjectHasValue(
//                df.getOWLObjectProperty(IRI.create(ontologyIRI + "#passed")),
//                df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#IngSw"))
//        );
        OWLClassExpression passedIngSw = df.getOWLObjectSomeValuesFrom(
                df.getOWLObjectProperty(IRI.create(ontologyIRI + "#passed")),
                df.getOWLObjectOneOf(df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#IngSw")))
        );

        boolean result = reasoner.subsumption(studentSingleton3, passedIngSw);
        System.out.println("Student3 passed IngSw: " + result);
        result = reasoner.subsumption(studentSingleton4, passedIngSw);
        System.out.println("Student4 passed IngSw: " + result);
    }

    public static void student2IsDataScienceStudent(OWLOntology ontology, ELPlusPlusReasoner reasoner) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLIndividual individualStudent = df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Student2"));
        OWLClass student = df.getOWLClass(IRI.create(ontologyIRI + "#Student"));
        OWLClass dataScience = df.getOWLClass(IRI.create(ontologyIRI + "#DataScienceStudent"));
        OWLClass ai = df.getOWLClass(IRI.create(ontologyIRI + "#AiStudent"));
        OWLClass cs = df.getOWLClass(IRI.create(ontologyIRI + "#CyberSecurityStudent"));
        OWLClass sd = df.getOWLClass(IRI.create(ontologyIRI + "#SoftwareDevStudent"));
        OWLObjectOneOf studentSingleton = df.getOWLObjectOneOf(individualStudent);

        if (!ontology.containsClassInSignature(dataScience.getIRI()) ||
                !ontology.containsClassInSignature(student.getIRI()) ||
                !ontology.containsIndividualInSignature(individualStudent.asOWLNamedIndividual().getIRI()))
        {
            System.out.println("Concepts not found in the ontology.");
            return;
        }

        boolean aiResult = reasoner.subsumption(studentSingleton, ai);
        boolean csResult = reasoner.subsumption(studentSingleton, cs);
        boolean dsResult = reasoner.subsumption(studentSingleton, dataScience);
        boolean sdResult = reasoner.subsumption(studentSingleton, sd);
        System.out.println("Student2 is a AiStudent: " + aiResult);
        System.out.println("Student2 is a Cyber Security student: " + csResult);
        System.out.println("Student2 is a Data Science student: " + dsResult);
        System.out.println("Student2 is a Software Dev student: " + sdResult);
    }

    public static void student1NotYetChoseAPlan(OWLOntology ontology, ELPlusPlusReasoner reasoner) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLIndividual individualStudent = df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#Student1"));
        OWLClass student = df.getOWLClass(IRI.create(ontologyIRI + "#Student"));
        OWLClass person = df.getOWLClass(IRI.create(ontologyIRI + "#Person"));
        OWLClass ai = df.getOWLClass(IRI.create(ontologyIRI + "#AiStudent"));
        OWLClass cs = df.getOWLClass(IRI.create(ontologyIRI + "#CyberSecurityStudent"));
        OWLClass ds = df.getOWLClass(IRI.create(ontologyIRI + "#DataScienceStudent"));
        OWLClass sd = df.getOWLClass(IRI.create(ontologyIRI + "#SoftwareDevStudent"));
        OWLObjectOneOf studentSingleton = df.getOWLObjectOneOf(individualStudent);

        if (!ontology.containsClassInSignature(ai.getIRI()) ||
                !ontology.containsClassInSignature(cs.getIRI()) ||
                !ontology.containsClassInSignature(ds.getIRI()) ||
                !ontology.containsClassInSignature(sd.getIRI()) ||
                !ontology.containsIndividualInSignature(individualStudent.asOWLNamedIndividual().getIRI()))
        {
            System.out.println("Concepts not found in the ontology.");
            return;
        }

        boolean personResult = reasoner.subsumption(studentSingleton, person);
        System.out.println("Student1 is a Person: " + personResult);
        boolean studentResult = reasoner.subsumption(studentSingleton, student);
        System.out.println("Student1 is a Student: " + studentResult);

        boolean aiResult = reasoner.subsumption(studentSingleton, ai);
        boolean csResult = reasoner.subsumption(studentSingleton, cs);
        boolean dsResult = reasoner.subsumption(studentSingleton, ds);
        boolean sdResult = reasoner.subsumption(studentSingleton, sd);
        System.out.println("Student1 is a AiStudent: " + aiResult);
        System.out.println("Student1 is a Cyber Security student: " + csResult);
        System.out.println("Student1 is a Data Science student: " + dsResult);
        System.out.println("Student1 is a Software Dev student: " + sdResult);
    }

    public static void isSecurityPrivacyInactiveExam(OWLOntology ontology, ELPlusPlusReasoner reasoner) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLIndividual individual = df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#SecurityPrivacy"));
        OWLClass activeExam = df.getOWLClass(IRI.create(ontologyIRI + "#ActiveExam"));
        OWLObjectOneOf singleton = df.getOWLObjectOneOf(individual);

        if (ontology.containsIndividualInSignature(IRI.create(ontologyIRI + "#SecurityPrivacy")) &&
                ontology.containsClassInSignature(IRI.create(ontologyIRI + "#ActiveExam")) )
        {
            boolean result = reasoner.subsumption(singleton, activeExam);
            System.out.println(Utilities.prettyPrint(individual) + " ⊑ " + Utilities.prettyPrint(activeExam) + ": " + result);
        } else {
            System.out.println("Concepts not found in the ontology.");
        }
    }

    public static void isMaiAnActiveExam(OWLOntology ontology, ELPlusPlusReasoner reasoner) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLIndividual individualMai = df.getOWLNamedIndividual(IRI.create(ontologyIRI + "#MAI"));
        OWLClass activeExam = df.getOWLClass(IRI.create(ontologyIRI + "#ActiveExam"));
        OWLObjectOneOf maiSingleton = df.getOWLObjectOneOf(individualMai);

        if (ontology.containsIndividualInSignature(IRI.create(ontologyIRI + "#MAI")) &&
                ontology.containsClassInSignature(IRI.create(ontologyIRI + "#ActiveExam")) )
        {
            boolean result = reasoner.subsumption(maiSingleton, activeExam);
            System.out.println("Result subsumption: " + result);
        } else {
            System.out.println("Concepts not found in the ontology.");
        }
    }

    public static void areStudentAndProfessorBothPerson(OWLOntology ontology, ELPlusPlusReasoner reasoner) {
        IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
        OWLOntologyManager manager = ontology.getOWLOntologyManager();
        OWLDataFactory df = manager.getOWLDataFactory();

        OWLClass student = df.getOWLClass(IRI.create(ontologyIRI + "#Student"));
        OWLClass professor = df.getOWLClass(IRI.create(ontologyIRI + "#Professor"));
        OWLClass person = df.getOWLClass(IRI.create(ontologyIRI + "#Person"));

        if (ontology.containsClassInSignature(IRI.create(ontologyIRI + "#Student")) &&
                ontology.containsClassInSignature(IRI.create(ontologyIRI + "#Professor")) &&
                ontology.containsClassInSignature(IRI.create(ontologyIRI + "#Person")) ) {
            boolean studentResult = reasoner.subsumption(student, person);
            boolean professorResult = reasoner.subsumption(professor, person);
            System.out.println("Student subsumption: " + studentResult);
            System.out.println("Professor subsumption: " + professorResult);
        } else {
            System.out.println("Concepts not found in the ontology.");
        }
    }
}
