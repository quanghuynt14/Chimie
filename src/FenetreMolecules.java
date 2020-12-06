import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.*;
import java.io.*;

public class FenetreMolecules extends JFrame implements ActionListener {

    private LinkedList<Atome> elementList; // tableau periodique
    private LinkedList<Atome> lesAtomes;   // une liste des atomes afiches sur l'ecran
    private JButton [] bAtomes;            // afficher le tableau periodique
    private paintJPanel zonePaint;	       // la zone afiche les molecules, les atomes
    private JButton bSave;                 // pour enregistrer les molecules qu'on a faits
    private JTextField tSave;              // ecrire le nom des molecules qu'on a faits pour enregistrer
    private JButton bGet;                  // pour faire afficher les molecules qu'on a faits
    private Choice tGet;               // ecrire le nom des molecules qu'on a faits pour afficher
    private JButton bXoa;                  // clear all
    public int nMolecule;

    public FenetreMolecules(LinkedList<Atome> elementList) {

        this.elementList = elementList;
        this.lesAtomes = new LinkedList<Atome>();

        this.setTitle("Chimie");                       // nom de la fenetre

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);  // maximizer la fenetre quand on execute la classe Test
        this.setUndecorated(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        // Panneau Choix, c'est la grande partie a droite qui contient le tableau periodique et l'indication de famille
        JPanel panneauChoix = new JPanel();
        panneauChoix.setBounds(600,10,(int) (width/1.85),(int) height);
        panneauChoix.setLayout(null);
        panneauChoix.setBackground(new Color(86, 101, 115));

        JLabel tpe = new JLabel("TABLEAU PERIODIQUE DES ELEMENTS");
        tpe.setFont(new Font("Rockwell", Font.BOLD, 25));
        tpe.setBounds(120, 20, 500, 50);
        tpe.setForeground(Color.white);

        bAtomes = new JButton[119];

        int n = 0; // numero atomique
        for (int i = 1; i < 8; i++) {     // i s'exprime la ligne du tableau
            for (int j = 1; j < 19; j++)  // j s'exprime la colonne du tableau
                if (!(i == 1 && j > 1 && j < 18) && !(((i == 2) || (i == 3)) && j > 2 && j < 13)) {	 // pour la ligne 1, il n'y a que 2 colonnes 1 et 18.Pour les lignes 2 et 3, il n'y pas les colonnes de 3 a 12
                    n++;
                    int m = n;
                    if (i == 6 && j == 3) m += 14;        // les atomes de la couche f. Ils sont affiches au-dessous du grand tableau
                    else if (i == 7 && j == 3) m += 14;	  // les atomes de la couche f. Ils sont affiches au-dessous du grand tableau

                    for (int k = n; k <= m; k++) {        //boucler une fois
                        bAtomes[k] = new JButton(elementList.get(k).nom);
                        bAtomes[k].setFont(new Font("Rockwell", Font.BOLD, 16));
                        // generer les boutons d'atome dans le tableau periodique

                        if (i == 6 && j == 3)
                            bAtomes[k].setBounds(j+(j-1)*41 + (k-57)*42,100+i+(i-1)*50 + 126,41,50);
                        else if (i == 7 && j == 3)
                            bAtomes[k].setBounds(j+(j-1)*41 + (k-89)*42,100+i+(i-1)*50 + 126,41,50);
                            // les 2 cas au-dessus sont les premiers atomes de la couche f qui appartiennent a la ligne 6 et 7. On les laisse vides
                        else
                            bAtomes[k].setBounds(j+(j-1)*41,100+i+(i-1)*50,41,50);
                        // faire afficher les boutons d'atome
                        if (elementList.get(k).famille.equals("non-metaux")) {
                            bAtomes[k].setBackground(new Color(160, 255, 160));
                        } else if (elementList.get(k).famille.equals("alcalins")) {
                            bAtomes[k].setBackground(new Color(255, 102, 102));
                        } else if (elementList.get(k).famille.equals("alcalino-terreux")) {
                            bAtomes[k].setBackground(new Color(246, 207, 161));
                        } else if (elementList.get(k).famille.equals("metaux-de-transition")) {
                            bAtomes[k].setBackground(new Color(255, 192, 192));
                        } else if (elementList.get(k).famille.equals("metaux-pauvres")) {
                            bAtomes[k].setBackground(new Color(204, 204, 204));
                        } else if (elementList.get(k).famille.equals("metalloides")) {
                            bAtomes[k].setBackground(new Color(204, 204, 153));
                        } else if (elementList.get(k).famille.equals("halogenes")) {
                            bAtomes[k].setBackground(new Color(255, 255, 153));
                        } else if (elementList.get(k).famille.equals("gaz-nobles")) {
                            bAtomes[k].setBackground(new Color(192, 232, 255));
                        } else if (elementList.get(k).famille.equals("lanthanides")) {
                            bAtomes[k].setBackground(new Color(255, 191, 255));
                        } else if (elementList.get(k).famille.equals("actinides")) {
                            bAtomes[k].setBackground(new Color(255, 153, 204));
                        } else if (elementList.get(k).famille.equals("elements-non-classes")) {
                            bAtomes[k].setBackground(new Color(245, 237, 222));
                        }

                        // ajouter les couleurs aux boutons en se basant sur la famille de l'atome

                        bAtomes[k].setMargin( new Insets(1, 1, 1, 1) );
                        bAtomes[k].setBorderPainted(false);
                        bAtomes[k].setFocusPainted(false);
                        if (elementList.get(k).famille.equals("elements-non-classes")) {
                            bAtomes[k].setForeground(new Color(0, 0, 0));
                        } else {
                            bAtomes[k].setForeground(new Color(26, 93, 186));
                        }
                        bAtomes[k].addActionListener(this); // permet d'afficher les atomes sur la partie blanche quand on clique sur les boutons sur le tableau periodique

                        panneauChoix.add(bAtomes[k]);
                    }
                    n = m;

                }
        }

        // Métaux
        JButton metaux = new JButton("Métaux");
        metaux.setFont(new Font("Rockwell", Font.BOLD, 15));
        metaux.setBounds(0,620,395,30);
        metaux.setBackground(new Color(255, 255, 255));
        metaux.setMargin( new Insets(1, 1, 1, 1) );
        metaux.setBorderPainted(false);
        metaux.setFocusPainted(false);
        metaux.setForeground(new Color(26, 93, 186));
        metaux.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/M%C3%A9tal");
            }
        });
        panneauChoix.add(metaux);

        JButton alcalins = new JButton("Alcalins");
        alcalins.setFont(new Font("Rockwell", Font.BOLD, 15));
        alcalins.setBounds(0,655,70,50);
        alcalins.setBackground(new Color(255, 102, 102));
        alcalins.setMargin( new Insets(1, 1, 1, 1) );
        alcalins.setBorderPainted(false);
        alcalins.setFocusPainted(false);
        alcalins.setForeground(new Color(26, 93, 186));
        alcalins.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/M%C3%A9tal_alcalin");
            }
        });
        panneauChoix.add(alcalins);

        JButton alcalino_terreux = new JButton("<html>Alcalino-<br>terreux</html>");
        alcalino_terreux.setFont(new Font("Rockwell", Font.BOLD, 14));
        alcalino_terreux.setBounds(75,655,70,50);
        alcalino_terreux.setBackground(new Color(246, 207, 161));
        alcalino_terreux.setMargin( new Insets(1, 1, 1, 1) );
        alcalino_terreux.setBorderPainted(false);
        alcalino_terreux.setFocusPainted(false);
        alcalino_terreux.setForeground(new Color(26, 93, 186));
        alcalino_terreux.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/M%C3%A9tal_alcalino-terreux");
            }
        });
        panneauChoix.add(alcalino_terreux);

        JButton lanthanides = new JButton("Lanthanides");
        lanthanides.setFont(new Font("Rockwell", Font.BOLD, 13));
        lanthanides.setBounds(150,655,90,23);
        lanthanides.setBackground(new Color(255, 191, 255));
        lanthanides.setMargin( new Insets(1, 1, 1, 1) );
        lanthanides.setBorderPainted(false);
        lanthanides.setFocusPainted(false);
        lanthanides.setForeground(new Color(26, 93, 186));
        lanthanides.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/Lanthanide");
            }
        });
        panneauChoix.add(lanthanides);

        JButton actinides = new JButton("Actinides");
        actinides.setFont(new Font("Rockwell", Font.BOLD, 13));
        actinides.setBounds(150,682,90,23);
        actinides.setBackground(new Color(255, 153, 204));
        actinides.setMargin( new Insets(1, 1, 1, 1) );
        actinides.setBorderPainted(false);
        actinides.setFocusPainted(false);
        actinides.setForeground(new Color(26, 93, 186));
        actinides.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/Actinide");
            }
        });
        panneauChoix.add(actinides);

        JButton metaux_de_transition = new JButton("<html>Métaux de<br>transition</html>");
        metaux_de_transition.setFont(new Font("Rockwell", Font.BOLD, 13));
        metaux_de_transition.setBounds(245,655,80,50);
        metaux_de_transition.setBackground(new Color(255, 192, 192));
        metaux_de_transition.setMargin( new Insets(1, 1, 1, 1) );
        metaux_de_transition.setBorderPainted(false);
        metaux_de_transition.setFocusPainted(false);
        metaux_de_transition.setForeground(new Color(26, 93, 186));
        metaux_de_transition.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/M%C3%A9tal_de_transition");
            }
        });
        panneauChoix.add(metaux_de_transition);

        JButton metaux_pauvres = new JButton("<html>Métaux<br>pauvres</html>");
        metaux_pauvres.setFont(new Font("Rockwell", Font.BOLD, 14));
        metaux_pauvres.setBounds(330,655,65,50);
        metaux_pauvres.setBackground(new Color(204, 204, 204));
        metaux_pauvres.setMargin( new Insets(1, 1, 1, 1) );
        metaux_pauvres.setBorderPainted(false);
        metaux_pauvres.setFocusPainted(false);
        metaux_pauvres.setForeground(new Color(26, 93, 186));
        metaux_pauvres.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/M%C3%A9tal_pauvre");
            }
        });
        panneauChoix.add(metaux_pauvres);

        JButton metalloides = new JButton("Métalloïdes");
        metalloides.setFont(new Font("Rockwell", Font.BOLD, 13));
        metalloides.setBounds(400,655,90,50);
        metalloides.setBackground(new Color(204, 204, 153));
        metalloides.setMargin( new Insets(1, 1, 1, 1) );
        metalloides.setBorderPainted(false);
        metalloides.setFocusPainted(false);
        metalloides.setForeground(new Color(26, 93, 186));
        metalloides.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/M%C3%A9tallo%C3%AFde");
            }
        });
        panneauChoix.add(metalloides);

        // Non métaux
        JButton non_metaux = new JButton("Non-métaux");
        non_metaux.setFont(new Font("Rockwell", Font.BOLD, 15));
        non_metaux.setBounds(495,620,240,30);
        non_metaux.setBackground(new Color(255, 255, 255));
        non_metaux.setMargin( new Insets(1, 1, 1, 1) );
        non_metaux.setBorderPainted(false);
        non_metaux.setFocusPainted(false);
        non_metaux.setForeground(new Color(26, 93, 186));
        non_metaux.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/Non-m%C3%A9tal");
            }
        });
        panneauChoix.add(non_metaux);

        JButton autre_non_metaux = new JButton("<html>Autre<br>non-métaux</html>");
        autre_non_metaux.setFont(new Font("Rockwell", Font.BOLD, 13));
        autre_non_metaux.setBounds(495,655,90,50);
        autre_non_metaux.setBackground(new Color(160, 255, 160));
        autre_non_metaux.setMargin( new Insets(1, 1, 1, 1) );
        autre_non_metaux.setBorderPainted(false);
        autre_non_metaux.setFocusPainted(false);
        autre_non_metaux.setForeground(new Color(26, 93, 186));
        autre_non_metaux.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/Non-m%C3%A9tal#Gaz_nobles,_halog%C3%A8nes_et_%C2%AB_CHNOPS_%C2%BB");
            }
        });
        panneauChoix.add(autre_non_metaux);

        JButton halogenes = new JButton("Halogènes");
        halogenes.setFont(new Font("Rockwell", Font.BOLD, 13));
        halogenes.setBounds(590,655,80,50);
        halogenes.setBackground(new Color(255, 255, 153));
        halogenes.setMargin( new Insets(1, 1, 1, 1) );
        halogenes.setBorderPainted(false);
        halogenes.setFocusPainted(false);
        halogenes.setForeground(new Color(26, 93, 186));
        halogenes.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/Halog%C3%A8ne");
            }
        });
        panneauChoix.add(halogenes);

        JButton gaz_nobles = new JButton("<html>Gaz<br>nobles</html>");
        gaz_nobles.setFont(new Font("Rockwell", Font.BOLD, 14));
        gaz_nobles.setBounds(675,655,60,50);
        gaz_nobles.setBackground(new Color(192, 232, 255));
        gaz_nobles.setMargin( new Insets(1, 1, 1, 1) );
        gaz_nobles.setBorderPainted(false);
        gaz_nobles.setFocusPainted(false);
        gaz_nobles.setForeground(new Color(26, 93, 186));
        gaz_nobles.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                openWebPage("https://fr.wikipedia.org/wiki/Gaz_noble");
            }
        });
        panneauChoix.add(gaz_nobles);

        JButton elements_non_classes = new JButton("<html>Éléments<br>non classés</html>");
        elements_non_classes.setFont(new Font("Rockwell", Font.BOLD, 14));
        elements_non_classes.setBounds(740,655,90,50);
        elements_non_classes.setBackground(new Color(245, 237, 222));
        elements_non_classes.setMargin( new Insets(1, 1, 1, 1) );
        elements_non_classes.setBorderPainted(false);
        elements_non_classes.setFocusPainted(false);
        elements_non_classes.setForeground(new Color(0, 0, 0));
        panneauChoix.add(elements_non_classes);

        panneauChoix.add(tpe);

        // Panneau Result
        zonePaint = new paintJPanel(lesAtomes); // il y a une classe paintJpanel.java

        // Panneau pour les fonctions Save et Get
        JPanel panneauSaveGet = new JPanel();
        panneauSaveGet.setBounds(10,600,580,1000);
        panneauSaveGet.setLayout(null);
        panneauSaveGet.setBackground(new Color(86, 101, 115));

        //text field pour la fonction Save
        tSave = new JTextField();
        tSave.setBounds(10,10,200,30);
        tSave.setLayout(null);
        tSave.setFont(new Font("Rockwell", Font.TRUETYPE_FONT, 14));

        // button Save
        bSave = new JButton("Save");
        bSave.setBounds(230,10,100,31);
        bSave.setLayout(null);
        bSave.setBorderPainted(false);
        bSave.setFocusPainted(false);
        bSave.setBackground(new Color(200, 200, 200));
        bSave.addActionListener(this);

        // list Choice pour la fonction Get
        tGet = new Choice();
        afficheLesMolecules();
        tGet.setBounds(10,50,250,50);
        tGet.setFont(new Font("Rockwell", Font.TRUETYPE_FONT, 15));

        // button Get
        bGet = new JButton("Get");
        bGet.setBounds(230,50,100,30);
        bGet.setLayout(null);
        bGet.setBorderPainted(false);
        bGet.setFocusPainted(false);
        bGet.setBackground(new Color(150, 150, 150));
        bGet.addActionListener(this);

        // button ClearAll
        bXoa = new JButton("Clear All");
        bXoa.setBounds(400,10,100,70);
        bXoa.setLayout(null);
        bXoa.setBorderPainted(false);
        bXoa.setFocusPainted(false);
        bXoa.setBackground(Color.pink);
        bXoa.addActionListener(this);

        // Panneau Global
        panneauSaveGet.add(tSave);
        panneauSaveGet.add(bSave);
        panneauSaveGet.add(tGet);
        panneauSaveGet.add(bGet);
        panneauSaveGet.add(bXoa);

        // Panneau Global
        JPanel panneauGlobal = new JPanel();
        panneauGlobal.setBounds(0,0,800,600);
        panneauGlobal.setLayout(null);
        panneauGlobal.setBackground(new Color(44, 62, 80));
        panneauGlobal.add(panneauSaveGet);
        panneauGlobal.add(zonePaint);
        panneauGlobal.add(panneauChoix);

        this.add(panneauGlobal);


        this.setVisible(true);

        nMolecule = 0;

    }

    public void actionPerformed (ActionEvent e){
        for (int i = 1; i <= 118; i++) { // generer les atomes sur l'ecran
            if (e.getSource() == bAtomes[i]) {
                APoint temp = new APoint(Math.random()*480 + 40, Math.random()*480 + 40);
                boolean ok = true;       // pour tester si les atomes s'ecrasent ou non
                while (ok) {
                    ok = false;
                    for (int j = 0; j < lesAtomes.size(); j++) {              // tester tous les atomes d'un molecule
                        if (temp.distance(lesAtomes.get(j).posAtome) < 60) {  // les atomes doivent s'eloignent une distance 60
                            temp = new APoint(Math.random()*480 + 40, Math.random()*480 + 40);
                            ok = true;
                        }
                    }
                }
                nMolecule++;
                lesAtomes.add(new Atome(nMolecule-1, elementList.get(i).Z, elementList.get(i).nom, elementList.get(i).eCov, elementList.get(i).famille, temp));
            }
        }
        zonePaint.repaint();  // mettre a jour zonePaint

        if (e.getSource() == bSave) {
            System.out.println("button save");

            String tenFile = tSave.getText();  // nom de molecule enregistre, le contenu de txt est Atome.toString
            System.out.println(tenFile);
            String dir = System.getProperty("user.dir"); //  System.out.println(System.getProperty("user.dir")) pour savoir la repetoire
            if (!(tenFile.equals(""))) {
                try {
                    FileWriter writer = new FileWriter(dir + "\\libMolecules\\" + tenFile + ".txt"); // creer document dans user.dir\libMolecules ( normalement c'est ...\projet\libMolecules)
                    BufferedWriter buffer = new BufferedWriter(writer);  // pour reutilser le document si on utilise la fonction Get

                    buffer.write(String.valueOf(lesAtomes.size()) + "\n");
                    for (int i = 0; i < lesAtomes.size(); i++) {

                        buffer.write(lesAtomes.get(i).toString(lesAtomes));

                    }
                    buffer.write("End");
                    buffer.close();
                } catch(Exception ef) {
                    ef.printStackTrace();  // pour savoir s'il y a l'exception dans la partie try
                }
            }

            boolean lol = true;
            for (int i = 0; i < tGet.countItems(); i++) {
                if (tGet.getItem(i).equals(tenFile)) lol = false;
            }
            if (lol == true) {
                tGet.add(tenFile);
                try {
                    FileWriter writer = new FileWriter("ListMolecules.txt", true);
                    BufferedWriter buffer = new BufferedWriter(writer);

                    buffer.write(tenFile + "\n");

                    buffer.close();
                } catch(Exception ef) {
                    ef.printStackTrace();  // pour savoir s'il y a l'exception dans la partie try
                }
            }


        }

        if (e.getSource() == bGet) {
            System.out.println("button get");

            String tenFile = tGet.getSelectedItem();
            System.out.println(tenFile);
            if (!(tenFile.equals(""))) {
                String dir = System.getProperty("user.dir");
                try {
                    BufferedReader inFile = new BufferedReader(new FileReader(dir + "\\libMolecules\\" + tenFile + ".txt")); // lire le document
                    String line = "";

                    lesAtomes.clear();                           // effacer tout chaque fois qu'on charge un molecule
                    int n = Integer.parseInt(inFile.readLine()); //converser ce que bufferedread a lu en decimal
                    System.out.println(n);

                    int [][] atXq = new int[n][];                // n represente le nombre des atomes dans un molecule
                    for (int i = 0; i < n; i++) {
                        atXq[i] = new int[4];
                        int molecule = Integer.parseInt(inFile.readLine());
                        int size = Integer.parseInt(inFile.readLine());
                        int Z = Integer.parseInt(inFile.readLine());
                        String nom = inFile.readLine();
                        String famille = inFile.readLine();
                        int eCov = Integer.parseInt(inFile.readLine());
                        line = inFile.readLine();
                        line = line.trim();
                        APoint posAtome = new APoint(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));

                        Atome atomeR = new Atome(molecule, Z, nom, eCov, famille, posAtome);
                        atomeR.size = size;
                        // recreer l'atome

                        for (int h = 0; h < 4; h++)  //repliquer les electrons covalents
                            for (int k = 0; k < 2; k++) {
                                line = inFile.readLine();
                                line = line.trim();
                                atomeR.pos_eCov[h][k] = new APoint(Double.parseDouble(line.split(" ")[0]), Double.parseDouble(line.split(" ")[1]));

                            }

                        for (int h = 0; h < 4; h++) {  // repliquer les liaisons
                            line = inFile.readLine();
                            line = line.trim();
                            atomeR.nLienket[h] = Integer.parseInt(line.split(" ")[0]);
                        }
                        for (int h = 0; h < 4; h++)     // repliquer l'etat d'existence des electrons de chaque atome
                            for (int k = 0; k < 2; k++) {
                                line = inFile.readLine();
                                line = line.trim();
                                if (line.split(" ")[0].equals("true"))
                                    atomeR.hash[h][k] = true;
                                else atomeR.hash[h][k] = false;
                            }

                        for (int h = 0; h < 4; h++) {
                            line = inFile.readLine();
                            line = line.trim();
                            if (line.split(" ")[0].equals("8"))  // "8" s'exprime la non-existence d'une coordination atomique, reference a la classe Atome.java, la methode toString
                                atXq[i][h] = 8;
                            else atXq[i][h] = Integer.parseInt(line.split(" ")[0]);
                        }
                        lesAtomes.add(atomeR);
                    }

                    for (int i = 0; i < n; i++) {
                        for (int h = 0; h < 4; h++)
                            if (atXq[i][h] == 8)
                                lesAtomes.get(i).atomeXq[h] = null; // declarer qu'il n'y a pas de coordination atomique a la position h de l'atome i dans un molecule
                            else
                                lesAtomes.get(i).atomeXq[h] = lesAtomes.get(atXq[i][h]); // relier les atomes
                    }


                } catch(Exception ef) {
                    ef.printStackTrace();
                }
            }
        }

        // supprimer l'ecran
        if (e.getSource() == bXoa) {
            lesAtomes.clear();
        }
    }


    public void afficheLesMolecules() {
        String dir = System.getProperty("user.dir");
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(dir + "\\ListMolecules.txt")); // lire le document
            String line = "";

            while ((line = inFile.readLine()) != null) {
                tGet.add(line);
            }

        } catch(Exception ef) {
            ef.printStackTrace();
        }
    }

    public void openWebPage(String url){
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        }
        catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

