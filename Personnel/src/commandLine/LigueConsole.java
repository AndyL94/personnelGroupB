package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;

import personnel.*;
import java.time.LocalDate;
import java.time.format.*;
import java.time.format.DateTimeParseException; 

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	private Option afficherLigues()
	{
		return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
	}

	private Option afficher(final Ligue ligue)
	{
		return new Option("Afficher la ligue", "l", 
				() -> 
				{
					System.out.println(ligue);
					System.out.println("administrée par " + ligue.getAdministrateur());
				}
		);
	}
	private Option afficherEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
	}

	private Option ajouterLigue()
	{
		return new Option("Ajouter une ligue", "a", () -> 
		{
			try
			{
				gestionPersonnel.addLigue(getString("nom : "));
			}
			catch(SauvegardeImpossible exception)
			{
				System.err.println("Impossible de sauvegarder cette ligue");
			}
		});
	}
	
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.add(afficher(ligue));
		menu.add(gererEmployes(ligue));
		menu.add(changerAdministrateur(ligue));
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {try {
					ligue.setNom(getString("Nouveau nom : "));
				} catch (SauvegardeImpossible e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}});
	}

	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",
				() -> 
				{
					String nom, prenom, mail, password;
					LocalDate Arrivee = null, Depart = null;
						nom = getString("nom : ");
						prenom = getString("prenom : ");
						mail = getString("mail : ");
						password = getString("password : ");
						Arrivee = parseDate("Date d'arrivée YYYY-MM-DD : ");
                        
                        while (true)
                        {
                        	Depart = parseDate("Date de départ YYYY-MM-DD : ");
                        	if(Depart.isBefore(Arrivee))
                    		{
                        		System.out.print("La date d'arrivée doit étre inférieur à la date de départ");
                        		
                        		
                    			}
                        	break;
                        	
                    		
                        }
                        ligue.addEmploye(nom, prenom, mail, password, Arrivee, Depart, 0);
                       
				}
		);
	}
	

	

	private LocalDate parseDate(String string){
        while(true)
            try {
            return LocalDate.parse(getString(string));
             }
        catch(DateTimeParseException e) {
             System.out.println("Erreur!");
             }
    }

	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.add(selectionnerEmploye(ligue));
		menu.addBack("q");
		return menu;
	}
	
	private List<Employe> selectionnerEmploye(final Ligue ligue)
	{
		return new List<>("Selectionner un employe", "s", 
				() -> new ArrayList<>(ligue.getEmployes()),
				employeConsole.editerEmploye()
				);
	}

	
	private List<Employe> changerAdministrateur(final Ligue ligue)
	{
		return new List<>("Changer d'administrateur", "a", 
                () -> new ArrayList<>(ligue.getEmployes()),
                (index, element) -> {ligue.setAdministrateur(element);}
                ); 

	}		
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {ligue.remove();});
	}
	
}
