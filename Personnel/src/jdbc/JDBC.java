package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.*;

public class JDBC implements Passerelle 
{
	Connection connection;

	public JDBC()
	{
		
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
			
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel() 
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try 
		{
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			
			while (ligues.next()) {
				gestionPersonnel.addLigue(ligues.getInt("id_ligue"), ligues.getString("nom_ligue"));
			
				PreparedStatement req = connection.prepareStatement("SELECT * FROM employe WHERE id_ligue = ?");
				req.setInt(1, ligues.getInt("id_ligue"));
				ResultSet employe = req.executeQuery();
				Ligue ligue = gestionPersonnel.getLigues().last();
				
				while (employe.next()) {
					
					int id = employe.getInt("id_employee");
					String nom = employe.getString("nom");
					String prenom = employe.getString("prénom");
					String mail = employe.getString("mail");
					String password = employe.getString("password");
					LocalDate dateArrivee = employe.getDate("datearrivée") != null
							? LocalDate.parse(employe.getString("datearrivée"))
							: null;
					LocalDate dateDepart = employe.getDate("datedépart") != null
							? LocalDate.parse(employe.getString("datedépart"))
							: null;
					int type = employe.getInt("habilitation");
					Employe employee = ligue.addEmploye(nom, prenom, mail, password, dateArrivee, dateDepart, id);
					
					

				} 
			}
			}
		
		catch (SQLException e)
		{
			System.out.println(e);
		}
		return gestionPersonnel;
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (nom_ligue) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	@Override
	public int insert(Employe employe) throws SauvegardeImpossible {
		try {
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
					"insert into employe (nom, prenom, mail, password, date_arrivee, date_depart,habilitation,id_ligue) values(?,?,?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, employe.getdateArrivee() == null ? null : Date.valueOf(employe.getdateArrivee()));
			instruction.setDate(6, employe.getdateDepart() == null ? null : Date.valueOf(employe.getdateDepart()));
			instruction.setBoolean(7, employe.estAdmin(null));
			instruction.setInt(8, employe.getLigue().getId());
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}
	
	@Override
	public void update(Ligue ligue) throws SauvegardeImpossible {
		try {
			PreparedStatement instruction;
			instruction = connection.prepareStatement("UPDATE ligue SET nom_ligue = ? WHERE ligue.id_ligue = ?");
			instruction.setString(1, ligue.getNom());
			instruction.setInt(2, ligue.getId());
			instruction.executeUpdate();

		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}
	
	@Override
	public void update(Employe employe) throws SauvegardeImpossible {
		try {
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
					"UPDATE employe SET nom = (?), prenom = (?), mail = (?), password = (?), date_arrivee = (?), date_depart = (?), habilitation = (?) WHERE id_employee = (?)");
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, employe.getdateArrivee() == null ? null : Date.valueOf(employe.getdateArrivee()));
			instruction.setDate(6, employe.getdateDepart() == null ? null : Date.valueOf(employe.getdateDepart()));
			instruction.setBoolean(7, employe.estAdmin(null));
			instruction.setInt(8, employe.getId());
			instruction.executeUpdate();

		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}
	
	@Override
	public void delete(Ligue ligue) throws SauvegardeImpossible {
		try {
			PreparedStatement instruction;
			instruction = connection.prepareStatement("DELETE FROM ligue WHERE id_ligue = ?");
			instruction.setInt(1, ligue.getId());
			instruction.executeUpdate();

		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}

	}
	
	@Override
	public void delete(Employe employe) throws SauvegardeImpossible {
		try {
			PreparedStatement instruction;
			instruction = connection.prepareStatement("DELETE FROM employe WHERE id_employee = ?");
			instruction.setInt(1, employe.getId());
			instruction.executeUpdate();

		} catch (SQLException exception) {
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}
	
}
