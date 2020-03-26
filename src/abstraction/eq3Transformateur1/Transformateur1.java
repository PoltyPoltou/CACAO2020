package abstraction.eq3Transformateur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Variable;
import abstraction.fourni.Filiere;

public class Transformateur1 implements IActeur {
	
	private Variable stockFeves;
	private Variable stockChocolat;
	private Integer cryptogramme;
	private Journal journalEq3;

	public Transformateur1() {
		this.stockFeves=new Variable(getNom()+" stock feves", this, 50);
		this.stockChocolat=new Variable(getNom()+" stock chocolat", this, 100);
		this.journalEq3 = new Journal("Eq3 activites", this);
	}
	
	public String getNom() {
		return "EQ3";
	}

	public String getDescription() {
		return "Transformateur bla bla bla";
	}
	
	public Color getColor() {
		return new Color(52, 152, 219);
	}

	public void initialiser() {
	}

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}
	public void next() {
	}

	public List<String> getNomsFilieresProposees() {
		return new ArrayList<String>();
	}

	public Filiere getFiliere(String nom) {
		return null;
	}
	
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.stockFeves);
		res.add(stockChocolat);
		return res;
	}

	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journalEq3);
		return res;
	}

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
		System.out.println("I'll be back... or not... "+this.getNom());
		} else {
			System.out.println("Poor "+acteur.getNom()+"... We will miss you. "+this.getNom());
		}
	}
	
	public void notificationOperationBancaire(double montant) {
	}
}
