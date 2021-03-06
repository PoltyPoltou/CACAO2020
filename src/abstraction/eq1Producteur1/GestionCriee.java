package abstraction.eq1Producteur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Variable;
import abstraction.eq8Romu.cacaoCriee.IVendeurCacaoCriee;
import abstraction.eq8Romu.cacaoCriee.LotCacaoCriee;
import abstraction.eq8Romu.cacaoCriee.PropositionCriee;
import abstraction.eq8Romu.cacaoCriee.SuperviseurCacaoCriee;
import abstraction.eq8Romu.produits.Feve;
import abstraction.fourni.Filiere;
import abstraction.eq2Producteur2.eq2Vendeur;




/** @author Clément 
 * Implémente les fonctions nécessaire pour la vente a la criée
 * ainsi que les fonctions telles que la mise a jour du prix minimum de vente.
 * 
 * Dans une v1, nous avons testé plusiseurs méthode pour définire le prix minimum de vente d'un lot
 * cependant, pour nous alligner avec ce qui a été fait dans l'équipe 2, nous fixerons les prix a la tonne
 * dans le constructeur.
*/
class GestionCriee //implements IVendeurCacaoCriee 
{
	
	private double lastPrixVenteFeveBasse;
	private double lastPrixVenteFeveMoyenne;
	private boolean venteBasseSurCeTour;
	/**
	 * Booleen qui symbolise si l'ont vend des fèves de basse qualité sur ce tour
	 * puisque l'on ne peut mettre en vent qu'un lot par tour
	 */
	
	private Producteur1 producteur1;
	private List<LotCacaoCriee> miseEnVenteLog;
	private ArrayList<PropositionCriee> venduLog;
	private double v1PrixBasse;
	private double v1PrixMoyenne;
	private int compteurBas;
	private int compteurMoyen;
	private double tailleLotBas;
	private double tailleLotMoyen;
	public static final int tailleLot = 1;


	public GestionCriee(Producteur1 sup) //Clément
	{
		//Prix par unité
		this.lastPrixVenteFeveBasse = 0;
		this.lastPrixVenteFeveMoyenne = 0;
		this.producteur1 = sup;
		this.venduLog = new ArrayList<PropositionCriee>();
		this.miseEnVenteLog = new ArrayList<LotCacaoCriee>();
		this.venteBasseSurCeTour = false;
		v1PrixBasse = 120;
		v1PrixMoyenne = 130;
	}
	
	public GestionCriee(double lastPrixMinInit, double lastPrixVenteInit, IActeur sup) //Clément
	{
		this.lastPrixVenteFeveBasse = lastPrixVenteInit;
	}

	public void next()
	{
		boolean bas = true;
		boolean moy = true;
		if(this.producteur1.getStock(Feve.FEVE_BASSE) <= 0)
		{
			this.compteurBas = -1;
			bas = false;
		}
		if(this.producteur1.getStock(Feve.FEVE_MOYENNE) <= 0)
		{
			this.compteurMoyen = -1;
			moy = false;
		}

		double stock = 0;
		if(bas)
		{
			stock = this.producteur1.getStock(Feve.FEVE_BASSE);
			this.compteurBas = (int) stock/this.tailleLot;
		}
		if(moy)
		{
			stock = this.producteur1.getStock(Feve.FEVE_MOYENNE);
			this.compteurMoyen = (int) stock/this.tailleLot;
		}

	}

	private LotCacaoCriee makeLot(Feve typeFeve, double quantiteAVendre)
	{
		double prixVente = 0; //quantiteAVendre * (PrixMoy+0.004);
		
		prixVente = this.getPrixVente(typeFeve);
		this.producteur1.ajouterJournaux(Color.CYAN, "[GestionCriee] - Mise en vente de : " + typeFeve + " en quantité "+ quantiteAVendre + " au prix minimum de " + prixVente);
		if(quantiteAVendre == 0)
		{
			return null;
		}
		LotCacaoCriee lot = new LotCacaoCriee(this.producteur1, typeFeve, quantiteAVendre, prixVente);
		this.miseEnVenteLog.add(lot);
		return lot;
	}

	//Clément 
	public LotCacaoCriee getLotEnVente() {
		boolean bool = Math.random() < 0.5;
		if(bool)
		{
			if(this.compteurBas > 0)
			{
				this.compteurBas -= 1;
				return makeLot(Feve.FEVE_BASSE, this.tailleLot);
				//min(producteur1.getStock(Feve.FEVE_BASSE), (double) this.tailleLot));
			}
		}
		else
		{
			if(this.compteurMoyen > 0)
			{
				this.compteurMoyen -= 1;
				return makeLot(Feve.FEVE_MOYENNE, this.tailleLot);
				//min(producteur1.getStock(Feve.FEVE_MOYENNE), (double) this.tailleLot));
			}
		}

		return makeLot(Feve.FEVE_BASSE, 0);

		/*
		if(venteBasseSurCeTour)
		{
			this.venteBasseSurCeTour = !this.venteBasseSurCeTour;
			return makeLot(Feve.FEVE_BASSE, producteur1.getStock(Feve.FEVE_BASSE));
		}
		else
		{
			this.venteBasseSurCeTour = !this.venteBasseSurCeTour;
			return makeLot(Feve.FEVE_MOYENNE, producteur1.getStock(Feve.FEVE_MOYENNE));
		}*/


	}
	
	//Clément
	/** 
	* Si on obtient aucunne proposition pour un lot, alors
	* On diminue le prix min acceptable en changeant directement
	* la variable lastPrixFeve... 
	*/
	public void notifierAucuneProposition(LotCacaoCriee lot) {
		if(lot.getFeve() == Feve.FEVE_BASSE)
		{
			this.lastPrixVenteFeveBasse -= 10;
			if(this.lastPrixVenteFeveBasse <= 0)
			{
				this.lastPrixVenteFeveBasse = 0.001;
			}
		}
		else
		{
			this.lastPrixVenteFeveMoyenne -= 10;
			if(this.lastPrixVenteFeveMoyenne <= 0)
			{
				this.lastPrixVenteFeveMoyenne = 0.001;
			}
		}

		this.producteur1.ajouterJournaux(Color.RED, "[GestionCriee] - Non vente de : " + lot.getQuantiteEnTonnes() + " de type : " + lot.getFeve());
	}

	//Clément
	public PropositionCriee choisir(List<PropositionCriee> propositions) {
		int n = propositions.size();
		double prixMax = 0.0000002; // On set le prix max a quelque chose de différent par sécurité pour ne pas accepter des lots de prix 0
		int indPrixMax = -1;
		for(int i = 0; i < n; i++)
		{
			if(propositions.get(i).getPrixPourUneTonne() >= prixMax)
			{
				indPrixMax = i;
			}
		}
		if(indPrixMax >= 0)
		{
			return propositions.get(indPrixMax);
		}
		else
		{
			return null;
		}
	}


	public ArrayList<PropositionCriee> getLotVendu()
	{
		return this.venduLog;
	}
	
	//Clément
	public void notifierVente(PropositionCriee proposition) {
		Feve typeFeve = proposition.getFeve();
		this.producteur1.ajouterJournaux(Color.GREEN, "[GestionCriee] - Vente de : " + 
			proposition.getQuantiteEnTonnes() + 
			" de type : " + typeFeve + 
			" au prix de :" + proposition.getPrixPourUneTonne() + 
			" a l'équipe :" + proposition.getAcheteur().getNom());

		this.producteur1.removeStock(proposition.getQuantiteEnTonnes(), typeFeve);
		this.venduLog.add(proposition);
	}

	/**
	 * Calcul le prix moyen sur les dernières ventes
	 * @return prix_moyen
	 */
	public double prixMoyenDernierreVentes(Feve typeFeve)
	{
		int n = this.venduLog.size();
		if(n == 0)
		{
	        if(typeFeve == Feve.FEVE_BASSE)
	        {
	            return this.v1PrixBasse;
	        }
	        if(typeFeve == Feve.FEVE_MOYENNE)
	        {
	            return this.v1PrixMoyenne;
	        }
		}
		int i = 0;
		int j = 0;
		double moyenne = 0;
		while(i < n && i < 10)
		{
			if(this.venduLog.get(n-i-1).getFeve() == typeFeve)
			{
				moyenne += this.venduLog.get(n-i-1).getPrixPourUneTonne();
				j++;
			}
			i++;
		}
		return moyenne / (double) j;
	}
	
	public double moyenneDerniersTours(Feve typeFeve)
	{
		int n = this.venduLog.size();
		if(n == 0)
		{
			if(typeFeve == Feve.FEVE_MOYENNE)
			{
				return this.v1PrixMoyenne;
			}
			if(typeFeve == Feve.FEVE_BASSE)
			{
				return this.v1PrixBasse;
			}
		}

	    int i = 0;
	    double sum = 0;
	    while(n-i-1 >= 0 && i <= 10)
	    {
	        if(this.venduLog.get(n-i-1).getFeve() == typeFeve)
	        {
	            sum += this.venduLog.get(n-i-1).getPrixPourUneTonne();
	        }
	        i += 1;
	    }


	    return sum/(double)(n);
	}
	
	public double getPrixVente(Feve typeFeve)
	{
	    double prixCentral = moyenneDerniersTours(typeFeve);
	    double prixVar = 0;
	    
		double prixVenteEq2 = 0;
		if(Filiere.LA_FILIERE.getEtape() >= 3)
		{
			for (PropositionCriee proposition:SuperviseurCacaoCriee.getHistorique(Filiere.LA_FILIERE.getEtape()-1)) {
				if ((proposition.getVendeur().getNom() == "Return of the Stonks") && (proposition.getLot().getFeve() == typeFeve)) {
					prixVenteEq2 = proposition.getPrixPourLeLot()/proposition.getQuantiteEnTonnes();
				}
			}
		}

	    if (this.producteur1.getStock(Feve.FEVE_BASSE) > 1000 && prixVenteEq2 >= 20) {
			prixVar = prixCentral - (prixVenteEq2 + 20);
		}
		if(prixVenteEq2 <= 20)
		{
			prixVar = prixCentral - 20;
		}

	    return prixCentral - prixVar;
	}
}
