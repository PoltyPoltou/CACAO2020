package abstraction.fourni;

import abstraction.eq1Producteur1.Producteur1;
import abstraction.eq2Producteur2.Producteur2;
import abstraction.eq3Transformateur1.Transformateur1;
import abstraction.eq4Transformateur2.Transformateur2;
import abstraction.eq5Transformateur3.Transformateur3;
import abstraction.eq6Distributeur1.Distributeur1;
import abstraction.eq7Distributeur2.Distributeur2;
import abstraction.eq8Romu.Romu;
import abstraction.eq8Romu.cacaoCriee.SuperviseurCacaoCriee;
import abstraction.eq8Romu.chocolatBourse.SuperviseurChocolatBourse;

public class FiliereParDefaut extends Filiere {
	
	public FiliereParDefaut() {
		super();
		this.ajouterActeur(new Producteur1());
		this.ajouterActeur(new Producteur2());
		this.ajouterActeur(new Transformateur1());
		this.ajouterActeur(new Transformateur2());
		this.ajouterActeur(new Transformateur3());
		this.ajouterActeur(new Distributeur1());
		this.ajouterActeur(new Distributeur2());
		this.ajouterActeur(new Romu());
		SuperviseurCacaoCriee superviseur = new SuperviseurCacaoCriee();
		this.ajouterActeur(superviseur);
		SuperviseurChocolatBourse superviseurBourse = new SuperviseurChocolatBourse();
		this.ajouterActeur(superviseurBourse);
		this.getIndicateur("BourseChoco cours CHOCOLAT_BASSE").setValeur(superviseur, 4000.0);
		this.getIndicateur("BourseChoco cours CHOCOLAT_MOYENNE").setValeur(superviseur, 10000.0);
		this.getIndicateur("BourseChoco cours CHOCOLAT_HAUTE").setValeur(superviseur, 15000.0);
		this.getIndicateur("BourseChoco cours CHOCOLAT_MOYENNE_EQUITABLE").setValeur(superviseur, 14000.0);
		this.getIndicateur("BourseChoco cours CHOCOLAT_HAUTE_EQUITABLE").setValeur(superviseur, 20000.0);

	}
	/**
	 * Redefinition afin d'interdire l'acces direct au superviseur.
	 * Sans cela, il serait possible de contourner l'autentification
	 */
	public IActeur getActeur(String nom) {
		if (!nom.equals("Sup.C.Criee") && (!nom.equals("BourseChoco"))) {
			return super.getActeur(nom); 
		} else {
			return null;
		}
	}
}
