package dta_jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;


public class Connect {

	//Initialise variables
	private String dbname = "postgres";
	private String username = "postgres";
	private String password = "357135bc";
	private Connection connection =null;
	private static String sm;

	//Connects and disconnects to/from the Database
	public void connector() 
	{
		try
		{
			connection =
					DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname,username, password);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return;
		}
		if (connection != null)
		{
		} else
		{
			System.err.println("Failed to make connection!");
		}	
	}	
	public void disconnect() {
		try
		{
			connection.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Connection could not be closed – SQL exception");
		}
	}
	public String [] complist(String chemid, String id)	// method creating the connection
	{
		connector();
		String[] complist = new String[30]; // array is 30, one for each compound in database
		String temp = null;
		Statement connect = null;
		int i = 0;

		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(chemid);
			while (list.next())
			{
				temp = list.getString(id);
				complist[i] = temp;
				i++;
			}

		}

		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + id);
		}
		disconnect();
		return complist;

	}

	public String mwt(String molwght, String mol, String select)
	{
		connector();
		String mw = null;

		Statement connect = null;


		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(molwght);
			while (list.next())
			{

				mw = list.getString(mol);

			}

		}

		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + select );
		}
		disconnect();
		return mw;
	}

	public String smles(String smile, String cd_smiles, String select)
	{
		connector();
		sm = null;

		Statement connect = null;


		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(smile);
			while (list.next())
			{

				sm = list.getString(cd_smiles);

			}

		}

		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + select );
		}
		disconnect();
		return sm;
	}

	public void updateLog(String query)
	{
		connector();		
		Statement connect = null;


		try 
		{
			connect = connection.createStatement();
			connect.executeUpdate(query);


		}

		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + query);
		}
		disconnect();
	}

	public String logp(String p, String pee, String select)
	{
		connector();
		String lgp = null;

		Statement connect = null;


		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(p);
			while (list.next())
			{

				lgp = list.getString(pee);

			}

		}



		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + select );
		}
		disconnect();
		return lgp;
	}

	public String haccept(String hacceptor, String ha, String select)
	{
		connector();
		String hcxcpt = null;

		Statement connect = null;


		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(hacceptor);
			while (list.next())
			{

				hcxcpt = list.getString(ha);

			}


		}
		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + select );
		}
		disconnect();
		return hcxcpt;	
	}


	public String hdono(String hdonor, String hd, String select)
	{
		connector();
		String hdnr = null;

		Statement connect = null;
		try
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery (hdonor);
			while(list.next())
			{
				hdnr = list.getString(hd);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.err.println("error executing query" + select );
		}
		disconnect();
		return hdnr;
	}


	/*	public String molord (String morder)
	{
		connector();
		String mord = null;
		Statement connect = null;
		String morwt = null;
		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(morder);
			while(list.next())
			{
				String mor = list.getString("cd_formula");
				String wt = list.getString("cd_molweight");
				morwt = mor +("\t") + wt;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.err.println("error executing query" + morder );

		}
		disconnect();
		return morwt;
	}
	 */
	public String [] orderlist(String morder)
	{
		connector();
		String[] ordlist = new String[30];
		String morwt = null;

		Statement connect = null;
		int i = 0;

		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(morder);
			while (list.next())
			{
				String mor = list.getString("cd_formula");
				String wt = list.getString("cd_molweight");
				double decimal = Double.parseDouble(wt);
				String makedecimal = String.format("%.2f", decimal);
				if (mor.length() > 11)
				{
					morwt = mor + ("\t\t") + makedecimal;
				}
				else
					morwt = mor + ("\t\t\t") + makedecimal;
				ordlist[i] = morwt;
				i++;
			}

		}

		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + morwt);
		}
		disconnect();
		return ordlist;

	}

	public String[] logpord (String logporder)

	{
		connector();
		String morlgp = null;
		String[] loglist = new String[30];
		String orlogp = null;
		Statement connect = null;
		int i = 0;

		try
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(logporder);
			while (list.next())
			{
				String mol = list.getString("cd_formula");
				String lgp = list.getString("aalogp");
				double decimal = Double.parseDouble(lgp);
				String makedecimal = String.format("%.2f", decimal);
				if (mol.length() > 11)
				{
					morlgp = mol + ("\t\t") + makedecimal;
				}
				else
					morlgp = mol + ("\t\t\t") + makedecimal;
				loglist[i] = morlgp;
				i++;
			}
		}

		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + morlgp);
		}
		disconnect();
		return loglist;
	}

	public String[] donord(String donororder)

	{
		connector();
		String mordon = null;
		String[] donlist = new String[30];
		String ordon = null;
		Statement connect = null;
		int i = 0;

		try 
		{
			connect = connection.createStatement();
			ResultSet list = connect.executeQuery(donororder);
			while (list.next())
			{
				String mol = list.getString("cd_formula");
				String hdonor = list.getString("h_donor");
				double decimal = Double.parseDouble(hdonor);
				String makedecimal = String.format("%.2f", decimal);
				if (mol.length() > 11)
				{
					mordon = mol + ("\t\t") + makedecimal;
				}
				else
					mordon = mol + ("\t\t\t") + makedecimal;
				donlist[i] = mordon;
				i++;
			}
		}

		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + mordon);
		}
		disconnect();
		return donlist;
	}


	public String[] accord(String acceptorder)
	{
		connector();
		String moracc = null;
		String[] acclist = new String[30];
		Statement connect = null;
		int i = 0;

		try
		{
			connect = connection.createStatement();
			ResultSet acceptorlist = connect.executeQuery(acceptorder);
			while(acceptorlist.next())
			{
				String molecule = acceptorlist.getString("cd_formula");
				String hacc = acceptorlist.getString("h_acceptor");
				double decimal = Double.parseDouble(hacc);
				String rounder = String.format("%.2f", decimal);
				if (molecule.length() >11)
				{
					moracc = molecule + ("\t\t") + rounder;
				}
				else
					moracc = molecule + ("\t\t\t") + rounder;
				acclist[i] = moracc;
				i++;
			}
		}


		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + moracc);
		}
		disconnect();
		return acclist;
	}

	public String [] lepinsk (String allcomp)
	{
		connector();
		String molrule = null;
		String [] rulelist = new String[30];
		Statement connect = null;
		int i = 0;


		try
		{
			connect = connection.createStatement();
			ResultSet leplist = connect.executeQuery(allcomp);
			while(leplist.next())
			{
				String lepmol = leplist.getString("cd_formula");
				String mweyt = leplist.getString("cd_molweight");
				String ralogp = leplist.getString("aalogp");
				String hacc = leplist.getString("h_acceptor");
				String hdon = leplist.getString("h_donor");
				double mwet =  Double.parseDouble(mweyt);
				double logpe = Double.parseDouble(ralogp);
				double hacce = Double.parseDouble(hacc);
				double hdono = Double.parseDouble(hdon);
				if (lepmol.length() > 11)
				{
					molrule = lepmol + ("\t\t") + mwet + ("\t\t\t") + logpe + ("\t\t\t") + hacce + ("\t\t\t") + hdono; 

				}
				else
					molrule =  lepmol + ("\t\t\t") + mwet + ("\t\t\t") + logpe + ("\t\t\t") + hacce + ("\t\t\t") + hdono;
				rulelist[i] = molrule;
				i++;
			}


		}
		catch (SQLException e )
		{
			e.printStackTrace();
			System.err.println("error executing query" + molrule);
		}
		disconnect();
		return rulelist;

	}
	public static String retsmle()
	{
		return sm;
	}
}








