package personnel;

public interface Passerelle 
{
	public GestionPersonnel getGestionPersonnel();
	
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)  throws SauvegardeImpossible;
	
	public int insert(Ligue ligue) throws SauvegardeImpossible;

	int insert(Employe employe) throws SauvegardeImpossible;

	void update(Ligue ligue) throws SauvegardeImpossible;

	void update(Employe employe, String column) throws SauvegardeImpossible;

	void delete(Ligue ligue) throws SauvegardeImpossible;
	
	void delete(Employe employe) throws SauvegardeImpossible;
}
