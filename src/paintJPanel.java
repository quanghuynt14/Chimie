import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;

public class paintJPanel extends JPanel implements MouseListener, MouseMotionListener {

    private LinkedList<Atome> lesAtomes;
    private int at;      // l'atome dont l'electron est interagi par la souris
    private int elhuong; // la direction de l'atome, cela correspond a des lignes de l'attribut pos_eCov dans la classe Atome.java
    private int elthu;   // un de deux electrons d'une paire, cela correspond a des colonnes de l'attribut pos_eCov dans la classe Atome.java
    private int check;   // 0 veut dire qu'on n'a pas clique sur un electron quelconque, une fois qu'on en clique sur 1, check = 1

    public paintJPanel(LinkedList<Atome> lesAtomes) {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.lesAtomes = lesAtomes;
        this.setBounds(10,10,580,580);
        at = -1;
        elhuong = -1;
        elthu = -1;
        check = 0;
    }

    public void paint(Graphics g) {
        // la partie blanche
        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // les atomes et molecules sur l'ecran
        g.setColor(Color.black);
        Font font = new Font("Times New Roman", Font.PLAIN, 40);
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);            // mesurer la taille de font
        int cao = metrics.getHeight();                           // hauteur d'atome = hauteur de font

        for (int i = 0; i < lesAtomes.size(); i++) {
            int dai = metrics.stringWidth(lesAtomes.get(i).nom); // largeur atome = largeur du nom de l'atome avec le meme font
            if (i == at) {
                g.setColor(Color.red);
                lesAtomes.get(i).dessine(g, cao, dai, elhuong, elthu);
            } else {
                g.setColor(Color.black);
                lesAtomes.get(i).dessine(g, cao, dai, -1, -1);
            }

        }


    }



    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){

    }

    public void mouseClicked(MouseEvent e){
        APoint posMouse = new APoint(e.getX(), e.getY()); // position de l'electron qu'on veut gerer

        boolean oo = true;
        for (int i = 0; i < lesAtomes.size(); i++) {      // i indique l'atome
            for (int j = 0; j < 4; j++) {                 // j indique la direction de l'atome i
                for (int h = 0; h < 2; h++) {             // h indique un des deux electrons d'une paire
                    if (lesAtomes.get(i).hash[j][h] == true && posMouse.distance(lesAtomes.get(i).pos_eCov[j][h]) <= 6) {
                        //si l'electron a cette position existe et s'il on clique tres proche de cet electron. Basiquement, les boucles au-dessus ont pour role de trouver les electrons sur lesquels la souris clique

                        if (check == 0) {  // indiquer l'electron qu'on est en train de gerer, plus precisement, la position de l'electron
                            at = i;
                            elhuong = j;
                            elthu = h;
                            check = 1;     // exprimer qu'on a deja clique sur un electron quelconque

                        } else if ( check == 1) { // si on a deja clique sur un electron et on continue a cliquer sur un autre electron d'un autre atome, on va creer des liaisons. Il y a les cas au-dessous

                            if (lesAtomes.get(at).molecule != lesAtomes.get(i).molecule) { // au cas ou 2 atomes dont les electrons sont cliques appartiennent aux molecules differents, on va avoir 2 atomes at et i par la suite

                                int moAt = lesAtomes.get(at).molecule;  // indiquer a quel molecule atome at appartient
                                int moI = lesAtomes.get(i).molecule;    // indiquer a quel molecule atome i appartient
                                if (moAt < moI) {
                                    for (int kn = 0; kn < lesAtomes.size(); kn++) { // kn < le nombre des atomes sur l'ecran
                                        if (lesAtomes.get(kn).molecule == moI)
                                            lesAtomes.get(kn).molecule = moAt;
                                        // si les atomes appartiennent a moI, ils appartiennent a moAt aussi. Sachant que plus petit int molecule est, plus petit la position de ses atomes sont dans le tableau lesAtomes
                                        System.out.println(kn + " " + lesAtomes.get(kn).molecule);
                                    }
                                } else {
                                    for (int kn = 0; kn < lesAtomes.size(); kn++) { // kn < le nombre des atomes sur l'ecran
                                        if (lesAtomes.get(kn).molecule == moAt)
                                            lesAtomes.get(kn).molecule = moI;
                                        // si les atomes appartiennent a moI, ils appartiennent a moAt aussi.
                                        System.out.println(kn + " " + lesAtomes.get(kn).molecule);
                                    }
                                }

                                if (lesAtomes.get(at).atomeXq[elhuong] != null || lesAtomes.get(i).atomeXq[j] != null || (elhuong + 2)%4 != j && lesAtomes.get(i).atomeXq[(elhuong + 2)%4] != null) {
                                    // si un des 2 directions (elhuong pour l'atome at et j pour l'atome i) sur laquelle on clique est occupee par l'autre atome ou si j n'est pas la direction opposee de elhuong ( par ex elhuong = 0 ( nord), j = 2 ( sud) sont opposes)
                                    // et si la position opposee de elhuong de l'electron i est occupee par une coordination atomique

                                    for (int k = 0; k < 4; k++)
                                        if (lesAtomes.get(at).atomeXq[k] == null && lesAtomes.get(i).atomeXq[(k+2)%4] == null) {
                                            // rechercher les autres positions ou il n'y a pas de coordination atomique
                                            elhuong = k; // pour l'atome at
                                            j = (k+2)%4; // opposee de elhuong
                                            break;       // arreter de rechercher une fois qu'on trouve la position qui correspond a if
                                        }
                                }
                                // Cette if declaration a pour role de changer la direction de liaison au cas ou cette direction est occupee par une autre liaison

                                else if ((elhuong + 2)%4 != j && lesAtomes.get(i).atomeXq[(elhuong + 2)%4] == null) {
                                    j = (elhuong+2)%4;
                                }
                                // si elhuong et j ne sont pas opposes. Par exemple elhuong = 0 (nord) et j = 1 (ouest), et si la direction 2 (sud) de l'atome i n'est pas occupee par l'autre atome, on fait j =2
                                // En general, l'atome at dont on clique sur l'electron, par exemple ou elhuong = 0 ( nord), on va faire une liaison au sud de l'atome i si le sud de l'atome i n'est pas occupee par l'autre atome. Si ce n'est pas le cas, on cherche les positions opposees qui ne sont pas occupees par les autres atomes.

                                lesAtomes.get(at).addAtomeXq(elhuong, lesAtomes.get(i)); // ajouter l'atome i a la direction elhuong de l'atome a


                                lesAtomes.get(i).addAtomeXq(j, lesAtomes.get(at));   // et vice versa

                                if (lesAtomes.get(i).hash[j][0]) {
                                    lesAtomes.get(i).hash[j][0] = false;
                                } else if (lesAtomes.get(i).hash[j][1]) {
                                    lesAtomes.get(i).hash[j][1] = false;

                                    // N'oublie pas que false = l'electron a cette position existe
                                    // quand on fait des liaisons, l'electron disparait

                                } else {
                                    // si aucun electron n'existe a la direction j de l'atome i
                                    boolean lo = true;
                                    for (int k = 0; k < 4; k++) {
                                        for (int kk = 0; kk < 2; kk++) {
                                            if (lesAtomes.get(i).hash[k][kk]) {
                                                lesAtomes.get(i).hash[k][kk] = false;
                                                lo = false;
                                                break;
                                            }
                                        }
                                        if (lo == false) break;

                                    }
                                }
                                //  rechercher la direction ou au moins un electron existe et prendre cet electron pour faire la liaison (cette electron donc disparait)


                                if (lesAtomes.get(at).hash[elhuong][0]) {
                                    lesAtomes.get(at).hash[elhuong][0] = false;
                                } else if (lesAtomes.get(at).hash[elhuong][1]) {
                                    lesAtomes.get(at).hash[elhuong][1] = false;
                                } else {
                                    boolean lo = true;
                                    for (int k = 0; k < 4; k++) {
                                        for (int kk = 0; kk < 2; kk++) {
                                            if (lesAtomes.get(at).hash[k][kk]) {
                                                //~ lesAtomes.get(at).pos_eCov[k][kk] = new APoint();
                                                lesAtomes.get(at).hash[k][kk] = false;
                                                lo = false;
                                                break;
                                            }
                                        }
                                        if (lo == false) break;
                                    }
                                }
                                //cette fois, c'est pour l'atome at

                            } else if (at != i) {
                                //si les 2 atomes appartiennent a un meme molecule
                                // au cas ou on veut creer des liaisons doubles, triples, etc

                                for (int k = 0; k < 4; k++) // ceci est l'atome at
                                    if (lesAtomes.get(at).atomeXq[k] == lesAtomes.get(i)) { // si l'atome at a deja fait la liaison avec l'atome i, arreter de boucler une fois c'est vraie grace a break
                                        if (lesAtomes.get(at).hash[k][0]) {
                                            lesAtomes.get(at).hash[k][0] = false;
                                        } else if (lesAtomes.get(at).hash[k][1]) {
                                            lesAtomes.get(at).hash[k][1] = false;
                                            // quand on fait des liaisons, l'electron disparait. C'est le cas ou on faire une liaison simple avec un des 2 electrons d'une paire, si on veut creer une liaison double, on tend a perdre l'electron qui reste n'importe quel l'electron on clique.

                                        } else { // si non, on perd l'electron qu'on clique pour faire la liason multiple
                                            lesAtomes.get(at).hash[elhuong][elthu] = false;
                                        }
                                        lesAtomes.get(at).nLienket[k]++;
                                        elhuong = k;
                                        break;
                                    }

                                for (int k = 0; k < 4; k++) // il faut faire le meme avec l'atome i, cette boucle et cette boucle au-dessus est miroir
                                    if (lesAtomes.get(i).atomeXq[k] == lesAtomes.get(at)) {
                                        if (lesAtomes.get(i).hash[k][0]) {
                                            lesAtomes.get(i).hash[k][0] = false;
                                        } else if (lesAtomes.get(i).hash[k][1]) {
                                            lesAtomes.get(i).hash[k][1] = false;
                                        } else {
                                            lesAtomes.get(i).hash[j][h] = false;
                                        }
                                        lesAtomes.get(i).nLienket[k]++;
                                        break;
                                    }
                            } else if (at == i && j != elhuong) {
                                System.out.println("he");
                                if ((lesAtomes.get(i).hash[j][0] || lesAtomes.get(i).hash[j][1])
                                        && (lesAtomes.get(i).hash[elhuong][0] || lesAtomes.get(i).hash[elhuong][1])){
                                    System.out.println("ok");
                                    if (lesAtomes.get(i).hash[elhuong][0]) lesAtomes.get(i).hash[elhuong][0] = false;
                                    else if (lesAtomes.get(i).hash[elhuong][1]) lesAtomes.get(i).hash[elhuong][1] = false;
                                    if (lesAtomes.get(i).hash[j][0] == false) lesAtomes.get(i).hash[j][0] = true;
                                    else if (lesAtomes.get(i).hash[j][1] == false) lesAtomes.get(i).hash[j][1] = true;
                                }
                            }



                            lesAtomes.get(at).update(lesAtomes.get(at));

                            at = -1;
                            elhuong = -1;
                            elthu = -1;
                            check = 0;
                            // redemarrer les parametres qui permettent de cliquer sur l'atome at
                        }

                        for (int lol = 0; lol < lesAtomes.size(); lol++)
                            System.out.print(lesAtomes.get(lol).molecule + " ");
                        System.out.println();
                        repaint();
                        oo = false;
                    }
                    if (oo == false) break;
                }
                if (oo == false) break;
            }
            if (oo == false) break;
            // arrter de rechercher les positions une fois que le programme a deja trouve avec les breaks
        }

    }

    public void mouseDragged(MouseEvent me) {
    }

    public void mouseMoved(MouseEvent me) {
    }
}
