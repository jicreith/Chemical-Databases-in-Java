package dta_jdbc;

import java.awt.*;
import java.awt.event.*;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.logPPlugin;
import chemaxon.marvin.io.formats.mdl.MolImport;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;
import chemaxon.marvin.beans.MView;
import chemaxon.marvin.beans.MViewPane;
import chemaxon.marvin.calculations.HBDAPlugin;
public class ConGUI extends JFrame
implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//GUI Variables
	private JButton b1, b2, b3,b4, b5, b6;
	private JTextField tf1, tf2, tf3;
	private JLabel l1, l2, l3,l4;
	private JComboBox cb1, cb2;
	private String Arrylist[];
	private String id;
	private String select;
	private String guiwgt;
	private Molecule molimp;
	private double lepcheck;
	private String guismiles;
	private double guilogp;
	private int guihaccept;
	private int guihdonor;
	private JPanel norPan = new JPanel();
	private JPanel southPan = new JPanel();
	private JPanel midPan = new JPanel();
	private JPanel pan3 = new JPanel();
	private JTextArea ta1, ta2, ta3, ta4, ta5;
	private JFrame MolFrame;
	private JPanel MolPanel;
	private boolean moldefined;
	private MViewPane moleView;





	//HELPER METHODS FOR GUI
	private void windParam()
	{
		//Parameters for the GUI
		setTitle("Chemical database window");
		setSize(1200,800);
		setLocation(400,50);
		setBackground(Color.YELLOW);
		setForeground(Color.YELLOW);
	}

	private void testcon()
	{
		Connect mycon = new Connect();
		mycon.connector();
		mycon.disconnect();
	}


	public ConGUI()
	{
		//Calls parameters
		windParam();
		Combo();
		box();
		add(norPan,"North");
		button();
		orderButton();
		add(midPan);
		add(southPan, "South");
		textarea();



		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testcon();

	}

	public void Combo()
	
	{
		// method for creating the combobox gui
		String chemid = ("SELECT cd_formula FROM protein_structures");
		String id = ("cd_formula");
		Connect combolist = new Connect();
		String Arrylist[] = combolist.complist(chemid,id);
		String chemlist = Arrays.toString(Arrylist);
		cb1 = new JComboBox(Arrylist);
		cb1.addActionListener(this);
			
	}

	public void box()
	{
		// method for appending
		l1 = new JLabel("Please select your compound");
		norPan.add(l1,"West");
		l1.setVisible(true);
		norPan.add(cb1);
		cb1.setVisible(true);




	}

	public void button()
	{
		// method for searching the database in a textfield.
		l2 = new JLabel ("Or search for it");
		norPan.add (l2);
		l2.setVisible(true);
		tf1 = new JTextField(10);
		norPan.add(tf1);
		tf1.setVisible(true);
		b1 = new JButton("Search database");
		b1.addActionListener(this);
		norPan.add(b1);
		b1.setVisible(true);
	}

public void orderButton()

{
	
	// method for the buttons that will be ordering the compounds
	l3 = new JLabel ("Filter the compounds by certain parameters:");
	midPan.add(l3);
	b2 = new JButton ("Molecular Weight");
	b2.addActionListener(this);
	b3 = new JButton ("LogP value");
	b3.addActionListener(this);
	b4 = new JButton ("Hydrogen Donors");
	b4.addActionListener(this);
	b5 = new JButton (" Hydrogen Acceptors");
	b5.addActionListener(this);
	b6 = new JButton ("Lepinskis Rules");
	b6.addActionListener(this);
	midPan.add(b2);
	midPan.add(b3);
	midPan.add(b4);
	midPan.add(b5);
	midPan.add(b6);
	
	
	
}


	public void molwt()
	{
		// this method retrieves the query for molecular weight
		String molwght = ("SELECT cd_molweight FROM protein_structures WHERE cd_formula = '" + select + "'");
		String mol = ("cd_molweight");
		Connect mw = new Connect();
		 guiwgt = mw.mwt(molwght, mol, select);
		 lepcheck = Double.parseDouble(guiwgt); // converted to double to use rounding


		if (lepcheck > 500) // checks the weight of the molecule
		{
			ta1.append( "Molecular Weight = " + lepcheck + " Daltons " + "  Chemical is larger than expected under lepinskis rules of 5" +  "\n");
		}
		else 
		{
			ta1.append("Molecular Weight = " + lepcheck + " Daltons " + "\n");
		}
	}

	public void SQLsmiles()
	
	// method for getting the SMILES from the database
	{
		String smile = ("SELECT cd_smiles FROM protein_structures WHERE cd_formula = '" + select + "'");
		String cd_smiles = ("cd_smiles");
		Connect sm = new Connect();
		sm.smles(smile, cd_smiles, select);
		guismiles = sm.smles(smile, cd_smiles, select);
		ta1.append("Smile structure =  " + guismiles + "\n");
	}

	
	//this was one of the methods used to calculate logP and H acceptors and donors
	/*	public void logpee() throws MolFormatException, IOException, PluginException
	{
		Molecule mol = new Molecule();
		mol = MolImporter.importMol(guismiles);
		logPPlugin plugin = new logPPlugin();
		plugin.setMolecule(mol);
		plugin.run();
		double logp = plugin.getlogPTrue();
		String logP = String.format("%.2f", logp);
		System.err.println(logp);
		String query = "UPDATE protein_structures SET aalogp = '" + logP + "' WHERE cd_formula ='" + select + "'";
		Connect p = new Connect();
		p.updateLog(query);
	}

	public void hacc() throws MolFormatException,IOException,PluginException
	{
		Molecule hmol = new Molecule();
		hmol = MolImporter.importMol(guismiles);
		HBDAPlugin plugin = new HBDAPlugin();
		 plugin.setMolecule(hmol);
		 plugin.run();
		 int msacc = plugin.getAcceptorAtomCount();
		 int msdon = plugin.getDonorAtomCount();
		 System.err.println(msacc);
		 System.err.println(msdon);
		 String accquery = "UPDATE protein_structures SET h_donor = '" + msdon + "' WHERE cd_formula ='" + select + "'";
		 Connect don = new Connect();
		 don.updateLog(accquery);


	}
	 */
	
	// method for querying logp once calculated
	public void LOGP()
	{
		String p = ("SELECT aalogp FROM protein_structures WHERE cd_formula = '" + select + "'");
		String pee = ("aalogp");
		Connect lp = new Connect();
		String logp = lp.logp(p,pee, select);
		System.err.println(logp);
		guilogp = Double.parseDouble(logp);
		if (guilogp >5)
		{
			ta1.append("LOGP value = " + guilogp +":  The Octanol Water value is larger than expected under lepinskis rules of 5" + "\n"); // tells the user if its logP value is higher than acceptable
		}
	
		else
		{
			ta1.append("LOGP value = " + guilogp +"\n");
		}

	}

	public void Hacc ()
	
	{
		// this method queries the numbe rof hydrogen acceptors
		String hacceptor = ("SELECT h_acceptor FROM protein_structures WHERE cd_formula = '" + select + "'");
		String ha = ("h_acceptor");
		Connect hac = new Connect();
		String hydrogenacc = hac.haccept(hacceptor,ha,select);
		guihaccept = Integer.parseInt(hydrogenacc);

		if (guihaccept > 5)
		{
			ta1.append("No. of H acceptors = " + guihaccept + ": The number of H acceptors is larger than expected under lepinskis rules of 5" + "\n");
		}
		else
		{
			ta1.append( "No. of H acceptors = " + guihaccept + "\n");
		}
	}


	public void Hdon()
	{
		String hdonor = ("SELECT h_donor FROM protein_structures WHERE cd_formula = '" + select + "'");
		String hd = ("h_donor");
		Connect hdon = new Connect();
		String hydrodonor = hdon.hdono(hdonor,hd, select);
		guihdonor = Integer.parseInt(hydrodonor);
		if (guihdonor > 5)
		{
			ta1.append("No. of H donors = " + guihdonor + ": The number of H donors is larger than expected under lepinskis rules of 5" + "\n");
		}
		else
		{
			ta1.append( "No. of H donors = " + guihdonor + "\n");
		}

	}

	

		//Define the Frame & Panel for the molecule
        public void MoleFrame()
        {
                MolFrame = new JFrame();
                MolPanel = new JPanel();
                MolPanel.setBorder(new TitledBorder(new EtchedBorder(),"Molecular View Window"));
                MolFrame.add(MolPanel);
                MolFrame.setSize(500, 500);
                MolFrame.setVisible(true);
        }

        //Defines the molecule from the smile string
       public void getMolecule() throws MolFormatException
        {
               
                molimp = MolImporter.importMol(Connect.retsmle());
       }


       //Checks if the molecule frame & panel have already been defined & creates the moleview
      public void defineMolecule()
      {
      	moleView = new MViewPane();
       			
      			
               MolPanel.add(moleView);
               moleView.setM(0, molimp);
               MolPanel.add(moleView);
               MolFrame.pack();

            
       }

	
	public void textarea()
	{
		// this is the method for the text areas that display the results
		ta1 = new JTextArea();
		southPan.add(ta1);
		ta1.setVisible(true);
		ta2 = new JTextArea();
		southPan.add(ta2);

	}
	
	public void ordermol()
	{
		// this is the method orders the compounds by molecular weight
		String morder = (" SELECT cd_formula, cd_molweight FROM protein_structures ORDER BY cd_molweight DESC ");
		Connect mor = new Connect();
		String arraymolord = null;
		for (int i = 0; i <30; i++)
		{
			arraymolord = arraymolord + mor.orderlist(morder)[i] + "\n\r";
		}
		arraymolord = arraymolord.replaceAll("null", "");
		String basic = "Formula: " + "\t\t" +"    Molecular Weight: "  + "\n\r";
		ta1.append(basic);
		ta1.append(arraymolord);
		
		
		
	}
	
	public void orderLogP ()
	{
		// this is the method orders the compounds by logP value
		String logporder = ("SELECT cd_formula, aalogp FROM protein_structures ORDER BY aalogp DESC");
		Connect lp = new Connect();
		String arraylogp = null;
		for(int i = 0; i <30; i++)
		{
			arraylogp = arraylogp + lp.logpord(logporder)[i] + "\n\r";
		}
		arraylogp = arraylogp.replaceAll("null", "");
		String logphead = "Formula: " + "\t\t" +"    LogP: "  + "\n\r";
		ta1.append(logphead);
		ta1.append(arraylogp);
	}
	
	
	public void orderdon()
	{
		// this is the method orders the compounds by H donor number
		String donororder = ("SELECT cd_formula, h_donor FROM protein_structures ORDER BY h_donor DESC");
		Connect dor = new Connect();
		String arraydon = null;
		for (int i = 0; i <30; i++)
		{
			arraydon = arraydon + dor.donord(donororder)[i] + "\n\r";
		}
		arraydon = arraydon.replaceAll("null","");
		String donhead = ("Formula: " + "\t\t" +"    H donors: "  + "\n\r");
		ta1.append(donhead);
		ta1.append(arraydon);
	}
	
	
	public void orderaccept()
	
	{
		// this is the method orders the compounds by H acceptor number
		String acceptorder = ("SELECT cd_formula, h_acceptor FROM protein_structures ORDER BY h_acceptor DESC");
		Connect acor = new Connect();
		String arrayacc = null;
		for (int i = 0; i <30; i++)
			
		{
			arrayacc = arrayacc + acor.accord(acceptorder)[i] + "\n\r";
		}
		arrayacc = arrayacc.replaceAll("null","");
		String acchead = ("Formula: " + "\t\t" +"    H acceptors: "  + "\n\r");
		ta1.append(acchead);
		ta1.append(arrayacc);
		
	}

	
	public void filter5()
	
	{
		// this is the method filters the compounds by Lepinskis rule of five
		 String allcomp = ("SELECT cd_formula, cd_molweight, aalogp, h_acceptor, h_donor FROM protein_structures WHERE cd_molweight <500 AND aalogp <5 AND h_acceptor <5 AND h_donor <5");
		 Connect rules = new Connect();
		 String arrayrules = null;
		 for (int i = 0; i <30; i++)
			 
		 {
			 arrayrules = arrayrules + rules.lepinsk(allcomp)[i] + "\n\r";
		 }
		  arrayrules = arrayrules.replaceAll("null","");
		  String header = ("Formula: " + "\t\t\t" +" molweight " + "\t\t\t" + "logp" + "\t\t\t" + "H acceptors" + "\t\t\t" + "Hdonors");
		  ta1.append (header);
		  ta2.append(arrayrules);
		  
		
	}
	@Override
	public void actionPerformed(ActionEvent e) // method that determines which events correspond to the buttons pressed
	{
		if(e.getSource()==b1)
		{

			select = tf1.getText();

			if (tf1.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Please enter a compound from the database", "Querry error", JOptionPane.ERROR_MESSAGE);

			}
		
			else if (select != "")
			{

				ta1.setText("");
				ta2.setText("");
				molwt();
				SQLsmiles();
				LOGP();
				Hacc();
				Hdon();
				MoleFrame();
				try {
					getMolecule();
				} catch (MolFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				defineMolecule();

			}
		}
		else if (e.getSource()==b2)
			
		{
			ta1.setText("");
			ta2.setText("");
			ordermol();
		}
		
		else if ((e.getSource()==b3))
		{
			ta1.setText("");
			ta2.setText("");
			orderLogP();
		}
		
		
		else if ((e.getSource()==b4))
			
		{
			ta1.setText("");
			ta2.setText("");
			orderdon();
		}
		
		else if ((e.getSource()==b5))
			
		{
			ta1.setText("");
			ta2.setText("");
			orderaccept();
		}
		else if ((e.getSource()==b6))
			
		{
			ta1.setText("");
			ta2.setText("");
			filter5();
			
		}
		
		else
		{
			cb1 = (JComboBox)e.getSource();
			select = (String)cb1.getSelectedItem();
			if (select == ""|| select == null)
			{

			}


			else 
			{
				// calls the methods if there is a choice in 
				ta1.setText("");
				ta2.setText("");
				molwt();
				SQLsmiles();
				LOGP();
				Hacc();
				Hdon();
				MoleFrame();
				try {
					getMolecule();
				} catch (MolFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				defineMolecule();


				/* try 
				{
					logpee();
					hacc();
				} catch (IOException | PluginException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 */
			}

		}
	}



}






