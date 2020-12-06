import java.awt.*;
import java.util.*;

public class Atome {

    public int molecule;          // indiquer le molecule auquel this.atome appartient
    public int size;              // le nombre d'atome dans un molecule
    public int Z;                 // position de l'atome sur le tableau periodique, aka numero atomique
    public String nom;
    public String famille;        // metal par exemple
    public int eCov;              // le nombre d'electrons covalents
    public APoint posAtome;       // position de l'atome
    public APoint [][] pos_eCov;  // position des electrons covalents, les colonnes s'expriment l'un de 2 electrons dans une paire; ligne : 0 correspond a nord de l'atome, 1 correspond a ouest de l'atome, 2 correspond a sud de l'atome, 3 correspond a est de l'atome
    public Atome [] atomeXq;      // les atomes alentour
    public int [] nLienket;       // nombre de liaison dans une direction
    public boolean [][] hash;     // indiquer s'il les electrons existent dans la position indiquer par le tableau, false = existe, true = non existe
    public int cao;               // hauteur
    public int dai;               // largeur

    public Atome(int molecule, int Z, String nom, int eCov, String famille) {
        this.molecule = molecule;
        size = 1;
        this.Z = Z;
        this.nom = nom;
        this.famille = famille;
        this.eCov = eCov;
        this.posAtome = new APoint();
        pos_eCov = new APoint [4][2];
        // on tente de creer des atomes ou il y a 4 coordinations alentour, une paire ou un electron simple est aussi considere comme coordination. Ligne : 0 correspond a nord de l'atome, 1 correspond a ouest de l'atome, 2 correspond a sud de l'atome, 3 correspond a est de l'atome
        atomeXq = new Atome[4];
        // on tente de creer des atomes ou il y a 4 coordinations alentour,0 correspond a nord de l'atome, 1 correspond a ouest de l'atome, 2 correspond a sud de l'atome, 3 correspond a est de l'atome
        nLienket = new int[4];
        // c'est le même cas : 0 correspond a nord de l'atome, 1 correspond a ouest de l'atome, 2 correspond a sud de l'atome, 3 correspond a est de l'atome
        hash = elbandau();
        // false veut dire qu'il existe un electron, true s'exprime le contraire

        cao = 0;  // hauteur
        dai = 0;  // largeur
    }

    public Atome(int molecule, int Z, String nom, int eCov, String famille, APoint posAtome) {
        this.molecule = molecule;
        size = 1;
        this.Z = Z;
        this.nom = nom;
        this.famille = famille;
        this.eCov = eCov;
        this.posAtome = new APoint(posAtome.x, posAtome.y);
        pos_eCov = new APoint[4][2];
        atomeXq = new Atome[4];
        nLienket = new int[4];
        hash = elbandau();
        cao = 0;
        dai = 0;
    }

    public void dessine(Graphics g, int cao, int dai, int elhuong, int elthu) {
        // cette methode est pour ajouter les electrons a l'alentour de l'atome en se basant sur le nombre d'electrons covalents intrinseque ainsi que pour indiquer quel electron qu'on est en train de gerer
        g.drawString(nom, (int)posAtome.x, (int)posAtome.y);
        this.cao = cao;
        this.dai = dai;
        // les coordonees x des alentours d'un atome, 8 positions possibles.
        double [] xP = {posAtome.x+dai/2-7-5, posAtome.x - 10, posAtome.x+dai/2+7-5, posAtome.x + dai, posAtome.x+dai/2+7-5, posAtome.x - 10, posAtome.x+dai/2-7-5, posAtome.x + dai};

        // les coordonnes y des alentours d'un atome, 8 positions possibles.
        double [] yP = {posAtome.y-cao+5, posAtome.y-cao/2+7+5, posAtome.y+5, posAtome.y-cao/2-7+5, posAtome.y-cao+5, posAtome.y-cao/2-7+5, posAtome.y+5,  posAtome.y-cao/2+7+5};

        // compter nombre des electrons covalents
        for (int i = 0; i < 8; i++)
            if (hash[i % 4][i / 4] == true) {

                pos_eCov[i % 4][i / 4] = new APoint(xP[i], yP[i]);
                if (i % 4 == elhuong && elthu == i/4) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.black);
                }

                g.fillOval((int)pos_eCov[i % 4][i /4].x, (int)pos_eCov[i % 4][i /4].y,10,10);

            }


        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 2; j++)
                if (pos_eCov[i][j] == null) {
                    pos_eCov[i][j] = new APoint();
                    // il n'y a pas d'electrons dans ces positions
                }

        for (int i = 0; i < 4; i++)  // dessiner les liaisons
            if (nLienket[i] != 0)    // s'il n'y a pas de liaison dans cette direction
            {
                double xx = 0;
                double yy = 0;
                // xx et yy representent la taille de liaison
                if (i == 0) {
                    xx = posAtome.x+25/(nLienket[i]+1);
                    yy = posAtome.y-cao;
                    // au-dessus de l'atome
                    for (int k = 0; k < nLienket[i]; k++) {
                        g.fillRect((int)xx + 25/(nLienket[i]+1) * k, (int)yy, 3, 15);
                        // dessiner les liaisons au nord de l'atome
                    }
                } else if (i == 1) {
                    xx = posAtome.x - 18;
                    yy = posAtome.y-(cao-20)/(nLienket[i]+1);
                    // a gauche de l'atome
                    for (int k = 0; k < nLienket[i]; k++) {
                        g.fillRect((int)xx, (int)yy - (cao-20)/(nLienket[i]+1)*k, 15, 3);
                        // dessiner les liaisons au ouest de l'atome
                    }
                } else if (i == 2) {
                    xx = posAtome.x+25/(nLienket[i]+1);
                    yy = posAtome.y +5;
                    // au-dessous de l'atome
                    for (int k = 0; k < nLienket[i]; k++) {
                        g.fillRect((int)xx+ 25/(nLienket[i]+1) * k, (int)yy, 3, 15);
                        // dessiner les liaisons au ouest de l'atome
                    }
                } else if (i == 3) {
                    xx = posAtome.x + dai + 3;
                    yy = posAtome.y - (cao-20)/(nLienket[i]+1) ;
                    // a droite de l'atome
                    for (int k = 0; k < nLienket[i]; k++) {
                        g.fillRect((int)xx, (int)yy - (cao-20)/(nLienket[i]+1)*k, 15, 3);
                        // dessiner les liaisons au ouest de l'atome
                    }
                }

            }

    }

    public boolean [][] elbandau() {
        boolean [][] t = new boolean [4][2];
        for (int i = 0; i < eCov; i++)
            t[i % 4][i / 4] = true;
        return t;
    }

    public void marque(Graphics g, int i, int j) {
        g.fillOval((int)pos_eCov[i][j].x, (int)pos_eCov[i][j].y,10,10);
        // dessiner les electrons
    }

    public void addAtomeXq(int vitri, Atome x) {
        // cette methode est pour faire les liaisons avec les autres atomes
        atomeXq[vitri] = x;
        // vitri est la position de la coordination
        nLienket[vitri]++;

    }

    public void update(Atome x) {
        for (int i = 0; i < 4; i++)
            if (atomeXq[i] != null && atomeXq[i] != x) {
                // s'il y a des coordinations atomiques a position [i] et les coordinations atomiques sont differents a l'atome x
                if (i == 0) {
                    atomeXq[i].posAtome.x = this.posAtome.x;
                    atomeXq[i].posAtome.y = this.posAtome.y-50;
                    // faire la coordination atomique de la position [0] au-dessus de l'atome x
                } else if (i == 1) {
                    atomeXq[i].posAtome.x = this.posAtome.x-atomeXq[i].dai-21;
                    atomeXq[i].posAtome.y = this.posAtome.y;
                    // faire la coordination atomique de la position [1] a gauche de l'atome x
                } else if (i == 2) {
                    atomeXq[i].posAtome.x = this.posAtome.x;
                    atomeXq[i].posAtome.y = this.posAtome.y+50;
                    // faire la coordination atomique de la position [2] au-dessous de l'atome x
                } else if (i == 3) {
                    atomeXq[i].posAtome.x = this.posAtome.x+this.dai+21;
                    atomeXq[i].posAtome.y = this.posAtome.y;
                    // faire la coordination atomique de la position [3] a droite de l'atome x
                }
                atomeXq[i].update(this);
                // on va repositioner les alentours de l'atome this apres avoir mettre a jour sa position par rapport a l'atome x
            }
    }

    public String toString(LinkedList<Atome> x) {
        String res = "";
        res += molecule + "\n" + size + "\n" + Z + "\n" + nom + "\n" + famille + "\n" + eCov + "\n" + posAtome + "\n";
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 2; j++)
                res += pos_eCov[i][j].toString() + "\n";
        for (int i = 0; i < 4; i++)
            res += nLienket[i] + "\n";
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 2; j++)
                res += hash[i][j] + "\n";
        for (int i = 0; i < 4; i++)
            if (atomeXq[i] == null)
                res += "8" + "\n"; // "8" s'exprime la non-existence d'une coordination atomique a position i de l'atome considere
            else
                res += x.indexOf(atomeXq[i]) + "\n";
        return res;

    }
}

