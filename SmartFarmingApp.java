import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.*;

public class SmartFarmingApp extends Application {

    Gestionnaire g  = new Gestionnaire();
    ZoneCulture  zc;
    ZoneElevage  ze;
    ZoneAquacole za;
    ObservableList<Zone> zonesObs = FXCollections.observableArrayList();
    StackPane mainContent;
    Node pageZones;
    Node pageCultures;
    Node pageAnimaux;
    Node pageAquacole;
    Node pageCapteurs;
    Node pageReleves;
    Node pageAlertes;
    ComboBox<Zone> cbZoneCultureField;
    ComboBox<Zone> cbZoneElevageField;
    VBox animauxListContainer;
    TableView<Zone> tableZonesView;
    private final List<ComboBox<String>> capteurComboBoxes = new ArrayList<>();
    private final List<Runnable> capteurTableRefreshers = new ArrayList<>();

    // ── couleurs ──
    static final String C_BLEU    = "#1A3C2E";
    static final String C_VERT    = "#4CAF50";
    static final String C_ROUGE   = "#E53935";
    static final String C_ORANGE  = "#FB8C00";
    static final String C_GRIS    = "#F1F5F0";
    static final String C_BLANC   = "#FFFFFF";
    static final String C_FOND    = "#F0F5F1";
    static final String C_PANNEAU = "#FFFFFF";
    static final String C_BORDURE = "#C8DCC8";
    static final String C_TEXTE   = "#1A3C2E";
    static final String C_MUTED   = "#5A7A5A";
    static final String C_SIDEBAR_HAUT = "#1A3C2E";
    static final String C_SIDEBAR_BAS  = "#0D2418";
    static final String C_SIDEBAR_BTN  = "rgba(255,255,255,0.08)";
    static final String C_CARD_SHADOW  = "rgba(26, 60, 46, 0.12)";
    static final String C_ACCENT       = "#A8D5A2";
    static final String C_ACCENT2      = "#D4EDDA";

    @Override
    public void start(Stage stage) {
        initDonnees();
        zonesObs.setAll(g.getZones());

        BorderPane root = new BorderPane();
        root.setTop(creerHeader());

        pageZones = creerPanneauZones();
        pageCultures = creerPanneauCultures();
        pageAnimaux = creerPanneauAnimaux();
        pageAquacole = creerPanneauAquacole();
        pageCapteurs = creerPanneauCapteurs();
        pageReleves = creerPanneauReleves();
        pageAlertes = creerPanneauAlertes();

        VBox sidebar = creerSidebar();
        mainContent = new StackPane();
        mainContent.setStyle("-fx-background-color: " + C_FOND + ";");
        mainContent.setPadding(new Insets(12));
        mainContent.getChildren().add(pageZones);
        showPage(pageZones);

        BorderPane corps = new BorderPane();
        corps.setStyle("-fx-background-color: " + C_FOND + ";");
        corps.setLeft(sidebar);
        corps.setCenter(mainContent);
        BorderPane.setMargin(sidebar, new Insets(0, 12, 0, 0));
        root.setCenter(corps);
        stage.setScene(new Scene(root, 1200, 850));
        stage.setTitle("ESI – Smart Farming");
        stage.show();
    }

    // ══════════════════════════════════════════
    // INIT
    // ══════════════════════════════════════════
    void initDonnees() {
        zc = new ZoneCulture(1,  "Zone Ble");
        ze = new ZoneElevage(2,  "Zone Vaches");
        za = new ZoneAquacole(3, "Zone Poissons");
        g.ajouterZone(zc); g.ajouterZone(ze); g.ajouterZone(za);

        g.ajouterEtConfigurerCapteur("SOL",             zc, 5.5,  8.0);
        g.ajouterEtConfigurerCapteur("ENVIRONNEMENTAL", zc, 10.0, 40.0);
        g.ajouterEtConfigurerCapteur("GPS",             ze, 0.0,  0.0);
        g.ajouterEtConfigurerCapteur("BIOMETRIQUE",     ze, 36.0, 42.0);
        g.ajouterEtConfigurerCapteur("EAU",             za, 6.0,  12.0);

        ze.setLimites(new LimitesGeographiques(36.0, 36.8, 2.5, 3.2));

        ((CapteurSol)zc.getCapteurs().get(0)).ajouterRelevePH(6.5);
        ((CapteurSol)zc.getCapteurs().get(0)).ajouterRelevePH(7.8);
        ((CapteurSol)zc.getCapteurs().get(0)).ajouterRelevePH(9.2);
        ((CapteurEnvironnemental)zc.getCapteurs().get(1)).ajouterReleveTemperature(25.0);
        ((CapteurEnvironnemental)zc.getCapteurs().get(1)).ajouterReleveTemperature(38.5);
        ((CapteurEnvironnemental)zc.getCapteurs().get(1)).ajouterReleveTemperature(55.0);
        ((CapteurGPS)ze.getCapteurs().get(0)).ajouterReleveGPS(36.7372, 3.0865);
        ((CapteurGPS)ze.getCapteurs().get(0)).ajouterReleveGPS(36.9000, 3.5000);
        ((CapteurBiometrique)ze.getCapteurs().get(1)).ajouterReleveTemperature(38.5);
        ((CapteurBiometrique)ze.getCapteurs().get(1)).ajouterReleveTemperature(41.8);
        ((CapteurBiometrique)ze.getCapteurs().get(1)).ajouterReleveTemperature(43.0);
        ((CapteurEau)za.getCapteurs().get(0)).ajouterReleveOxygene(9.0);
        ((CapteurEau)za.getCapteurs().get(0)).ajouterReleveOxygene(11.5);
        ((CapteurEau)za.getCapteurs().get(0)).ajouterReleveOxygene(3.0);
    }

    // ══════════════════════════════════════════
    // HEADER
    // ══════════════════════════════════════════
    HBox creerHeader() {
        HBox h = new HBox(16);
        h.setStyle(
            "-fx-background-color: linear-gradient(to right, #0D2418, #1A3C2E);" +
            "-fx-padding: 14 24;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0.2, 0, 3);"
        );
        h.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("🌱");
        logo.setFont(Font.font("Arial", 28));
        logo.setStyle(
            "-fx-background-color: #4CAF50;" +
            "-fx-background-radius: 50;" +
            "-fx-padding: 6 10;" +
            "-fx-text-fill: white;"
        );

        VBox titres = new VBox(2);
        Label t = new Label("Smart Farming");
        t.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        t.setTextFill(Color.WHITE);
        Label s = new Label("ESI-Alger — Gestion Intelligente de la Ferme");
        s.setFont(Font.font("Arial", 11));
        s.setTextFill(Color.web("#A8D5A2"));
        titres.getChildren().addAll(t, s);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label date = new Label(LocalDate.now().toString());
        date.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        date.setStyle(
            "-fx-text-fill: #A8D5A2;" +
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 6 14;"
        );

        h.getChildren().addAll(logo, titres, spacer, date);
        return h;
    }

    Tab creerTab(String titre, javafx.scene.Node contenu) {
        Tab t = new Tab(titre, contenu);
        return t;
    }

    VBox creerSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        sidebar.setPrefWidth(200);
        sidebar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1A3C2E, #0D2418);" +
            "-fx-background-radius: 0 24 24 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 16, 0.2, 4, 0);"
        );

        Label title = new Label("Navigation");
        title.setTextFill(Color.web("#A8D5A2"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        title.setStyle("-fx-padding: 0 0 8 6; -fx-letter-spacing: 1;");

        String[][] pages = {
            {"🏠", "Zones"},
            {"🌿", "Cultures"},
            {"🐄", "Animaux"},
            {"🐟", "Aquacole"},
            {"📡", "Capteurs"},
            {"📊", "Relevés"},
            {"🔔", "Alertes"}
        };

        Node[] pageNodes = {pageZones, pageCultures, pageAnimaux, pageAquacole, pageCapteurs, pageReleves, pageAlertes};

        VBox boutons = new VBox(4);
        for (int i = 0; i < pages.length; i++) {
            final int idx = i;
            Button b = new Button(pages[i][0] + " " + pages[i][1]);
            b.setMaxWidth(Double.MAX_VALUE);
            b.setAlignment(Pos.CENTER_LEFT);
            b.setFont(Font.font("Arial", 12));
            b.setStyle(
                "-fx-background-color: rgba(255,255,255,0.06);" +
                "-fx-text-fill: #D4EDDA;" +
                "-fx-padding: 11 14;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;"
            );
            b.setOnMouseEntered(e -> b.setStyle(
                "-fx-background-color: rgba(76,175,80,0.25);" +
                "-fx-text-fill: white;" +
                "-fx-padding: 11 14;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;"
            ));
            b.setOnMouseExited(e -> b.setStyle(
                "-fx-background-color: rgba(255,255,255,0.06);" +
                "-fx-text-fill: #D4EDDA;" +
                "-fx-padding: 11 14;" +
                "-fx-background-radius: 12;" +
                "-fx-cursor: hand;"
            ));
            b.setOnAction(e -> showPage(pageNodes[idx]));
            boutons.getChildren().add(b);
        }

        sidebar.getChildren().addAll(title, sep(), boutons);
        return sidebar;
    }

    Button sidebarButton(String texte, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button b = new Button(texte);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setOnAction(action);
        b.setStyle("-fx-background-color: " + C_SIDEBAR_BTN + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 14; -fx-background-radius: 14; -fx-cursor: hand;");
        return b;
    }

    void showPage(Node page) {
        if (mainContent == null || page == null) return;
        mainContent.getChildren().setAll(page);
    }

    // ══════════════════════════════════════════
    // PANNEAU ZONES
    // ══════════════════════════════════════════
    SplitPane creerPanneauZones() {
        SplitPane sp = new SplitPane();

        VBox gauche = boite();
        gauche.getChildren().add(titre("Gestion des Zones"));

        HBox bar = new HBox(8);
        ToggleGroup groupe = new ToggleGroup();
        ToggleButton btnAjouter = new ToggleButton("Ajouter");
        ToggleButton btnModifier = new ToggleButton("Modifier");
        ToggleButton btnSusp = new ToggleButton("Suspendre / Réactiver");
        ToggleButton btnProd = new ToggleButton("Production");
        for (ToggleButton b : Arrays.asList(btnAjouter, btnModifier, btnSusp, btnProd)) {
            b.setToggleGroup(groupe);
            b.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(b, Priority.ALWAYS);
        }
        btnAjouter.setSelected(true);
        bar.getChildren().addAll(btnAjouter, btnModifier, btnSusp, btnProd);

        StackPane forms = new StackPane();

        VBox formAjouter = boite();
        TextField tfCode = tfPlaceholder("Code unique (ex: 4)");
        TextField tfNom = tfPlaceholder("Nom de la zone");
        ComboBox<String> cbType = combo("ZoneCulture", "ZoneElevage", "ZoneAquacole");
        cbType.setValue("ZoneCulture");
        ComboBox<String> cbTypeElevage = combo("RUMINANT", "VOLAILLE");
        cbTypeElevage.setValue("RUMINANT");
        Label lblMsg1 = msg();
        Button btnAjouterZone = btn("Ajouter la Zone", C_VERT);
        HBox ligneTypeElevage = ligne("Type élevage :", cbTypeElevage);
        ligneTypeElevage.setVisible(false);
        formAjouter.getChildren().addAll(
            sousTitre("Ajouter une zone"),
            ligne("Code :", tfCode), ligne("Nom :", tfNom), ligne("Type :", cbType), ligneTypeElevage,
            btnAjouterZone, lblMsg1
        );

        cbType.valueProperty().addListener((obs, oldV, newV) -> {
            ligneTypeElevage.setVisible("ZoneElevage".equals(newV));
        });

        btnAjouterZone.setOnAction(e -> {
            try {
                int code = Integer.parseInt(tfCode.getText().trim());
                String nom = tfNom.getText().trim();
                String type = cbType.getValue();
                if (g.chercherzone(code) != null) {
                    lblMsg1.setTextFill(Color.RED);
                    lblMsg1.setText("Ce code existe déjà !");
                    return;
                }
                Zone z;
                switch (type) {
                    case "ZoneElevage": {
                        String te = cbTypeElevage.getValue();
                        TypeElevage typeE = "VOLAILLE".equals(te) ? TypeElevage.VOLAILLE : TypeElevage.RUMINANT;
                        z = new ZoneElevage(code, nom, typeE);
                        break;
                    }
                    case "ZoneAquacole": z = new ZoneAquacole(code, nom); break;
                    default: z = new ZoneCulture(code, nom); break;
                }
                g.ajouterZone(z);
                lblMsg1.setTextFill(Color.GREEN);
                lblMsg1.setText("Zone ajoutée !");
                tfCode.clear();
                tfNom.clear();
                cbType.setValue("ZoneCulture");
                cbTypeElevage.setValue("RUMINANT");
                rafraichirZones(tableZonesView);
                zonesObs.setAll(g.getZones());
            } catch (NumberFormatException ex) {
                lblMsg1.setTextFill(Color.RED);
                lblMsg1.setText("Code invalide !");
            } catch (Exception ex) {
                lblMsg1.setTextFill(Color.RED);
                lblMsg1.setText("Erreur : " + ex.getMessage());
            }
        });

        VBox formModifier = boite();
        TextField tfCodeMod = tfPlaceholder("Code à modifier");
        TextField tfNomMod = tfPlaceholder("Nouveau nom");
        Label lblMsg2 = msg();
        Button btnModifierAction = btn("Modifier", C_ORANGE);
        formModifier.getChildren().addAll(
            sousTitre("Modifier une zone"),
            ligne("Code :", tfCodeMod), ligne("Nouveau nom :", tfNomMod), btnModifierAction, lblMsg2
        );

        VBox formSusp = boite();
        TextField tfCodeSusp = tfPlaceholder("Code de la zone");
        Button btnSuspAction = btn("Suspendre", C_ROUGE);
        Button btnReacAction = btn("Réactiver", C_VERT);
        Label lblMsg3 = msg();
        formSusp.getChildren().addAll(
            sousTitre("Suspendre / Réactiver"),
            ligne("Code :", tfCodeSusp), new HBox(10, btnSuspAction, btnReacAction), lblMsg3
        );

        VBox formProd = boite();
        TextField tfCodeProd = tfPlaceholder("Code zone");
        TextField tfValProd = tfPlaceholder("Valeur");
        ComboBox<TypeProduction> cbProd = new ComboBox<>();
        cbProd.setPrefWidth(220);
        ComboBox<UniteMesure> cbUnite = new ComboBox<>(FXCollections.observableArrayList(
            UniteMesure.KG, UniteMesure.LITRE, UniteMesure.TONNE, 
            UniteMesure.MILLILITRE, UniteMesure.GRAMME, UniteMesure.UNITE
        ));
        cbUnite.setPrefWidth(220);
        cbUnite.setValue(UniteMesure.KG);
        Label lblProdAide = new Label("Entrez un code zone pour afficher les types autorisés.");
        lblProdAide.setWrapText(true);
        Button btnProdAction = btn("Enregistrer", C_BLEU);
        Label lblMsg4 = msg();
        formProd.getChildren().addAll(
            sousTitre("Enregistrer une Production"),
            ligne("Code :", tfCodeProd), ligne("Type :", cbProd), 
            ligne("Valeur :", tfValProd), ligne("Unité :", cbUnite), 
            lblProdAide, btnProdAction, lblMsg4
        );

        Runnable rafraichirTypesProd = () -> {
            try {
                int codeZone = Integer.parseInt(tfCodeProd.getText().trim());
                Zone zone = g.chercherzone(codeZone);
                ObservableList<TypeProduction> autorises = FXCollections.observableArrayList(typesProductionPourZone(zone));
                cbProd.setItems(autorises);
                if (!autorises.isEmpty()) cbProd.setValue(autorises.get(0));
                lblProdAide.setText(messageTypesProduction(zone));
            } catch (Exception ex) {
                cbProd.setItems(FXCollections.observableArrayList());
                cbProd.setValue(null);
                lblProdAide.setText("Entrez un code zone valide pour afficher les types autorisés.");
            }
        };

        tfCodeProd.textProperty().addListener((obs, oldValue, newValue) -> rafraichirTypesProd.run());
        rafraichirTypesProd.run();

        forms.getChildren().addAll(formAjouter, formModifier, formSusp, formProd);
        setVue(formAjouter, formModifier, formSusp, formProd);

        btnAjouter.setOnAction(e -> setVue(formAjouter, formModifier, formSusp, formProd));
        btnModifier.setOnAction(e -> setVue(formModifier, formAjouter, formSusp, formProd));
        btnSusp.setOnAction(e -> setVue(formSusp, formAjouter, formModifier, formProd));
        btnProd.setOnAction(e -> setVue(formProd, formAjouter, formModifier, formSusp));

        tableZonesView = tableZones();
        rafraichirZones(tableZonesView);

        // Auto-fill fields when table row is clicked
        tableZonesView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                tfCodeMod.setText(String.valueOf(newSel.getCode()));
                tfNomMod.setText(newSel.getNom());
                tfCodeSusp.setText(String.valueOf(newSel.getCode()));
                tfCodeProd.setText(String.valueOf(newSel.getCode()));
            }
        });

        Button btnRefresh = btn("Rafraîchir", C_BLEU);
        Button btnVoirProd = btn("Voir Production", C_BLEU);

        btnVoirProd.setOnAction(e -> {
            Zone sel = tableZonesView.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Sélectionnez une zone dans le tableau.", ButtonType.OK);
                a.setHeaderText("Aucune zone sélectionnée");
                a.showAndWait();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Historique production — ").append(sel.getNom()).append(" (code ").append(sel.getCode()).append(")\n\n");
            if (sel.getHistorique().isEmpty()) sb.append("Aucune production enregistrée.");
            else for (ProductionRecord p : sel.getHistorique()) sb.append(p.getResume()).append("\n");
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Historique Production");
            info.setHeaderText(null);
            TextArea ta = new TextArea(sb.toString());
            ta.setEditable(false);
            ta.setWrapText(true);
            ta.setPrefWidth(600);
            ta.setPrefHeight(300);
            info.getDialogPane().setContent(ta);
            info.showAndWait();
        });

        VBox zoneCenter = boite();
        zoneCenter.getChildren().addAll(bar, forms, new Separator(), new HBox(10, btnRefresh, btnVoirProd), tableZonesView);

        btnModifierAction.setOnAction(e -> {
            try {
                int code = Integer.parseInt(tfCodeMod.getText().trim());
                g.modifierZone(code, code, tfNomMod.getText().trim());
                lblMsg2.setTextFill(Color.GREEN);
                lblMsg2.setText("Zone modifiée !");
                tfCodeMod.clear();
                tfNomMod.clear();
                rafraichirZones(tableZonesView);
                zonesObs.setAll(g.getZones());
            } catch (Exception ex) {
                lblMsg2.setTextFill(Color.RED);
                lblMsg2.setText("Erreur : " + ex.getMessage());
            }
        });

        btnSuspAction.setOnAction(e -> {
            try {
                g.desactiverZone(Integer.parseInt(tfCodeSusp.getText().trim()));
                lblMsg3.setTextFill(Color.ORANGE);
                lblMsg3.setText("Zone suspendue.");
                tfCodeSusp.clear();
                rafraichirZones(tableZonesView);
                zonesObs.setAll(g.getZones());
            } catch (Exception ex) {
                lblMsg3.setTextFill(Color.RED);
                lblMsg3.setText(ex.getMessage());
            }
        });

        btnReacAction.setOnAction(e -> {
            try {
                Zone z = g.chercherzone(Integer.parseInt(tfCodeSusp.getText().trim()));
                if (z != null) { z.activer(); rafraichirZones(tableZonesView); }
                lblMsg3.setTextFill(Color.GREEN);
                lblMsg3.setText("Zone réactivée.");
                tfCodeSusp.clear();
                zonesObs.setAll(g.getZones());
            } catch (Exception ex) {
                lblMsg3.setTextFill(Color.RED);
                lblMsg3.setText(ex.getMessage());
            }
        });

        btnProdAction.setOnAction(e -> {
            try {
                int code = Integer.parseInt(tfCodeProd.getText().trim());
                double val = Double.parseDouble(tfValProd.getText().trim());
                Zone zone = g.chercherzone(code);
                TypeProduction type = cbProd.getValue();
                UniteMesure unite = cbUnite.getValue();
                if (zone == null) {
                    throw new IllegalArgumentException("Zone introuvable.");
                }
                if (type == null || !typesProductionPourZone(zone).contains(type)) {
                    throw new IllegalArgumentException("Type de production non autorisé pour cette zone.");
                }
                g.enregistrerProduction(code, type, val, unite);
                lblMsg4.setTextFill(Color.GREEN);
                lblMsg4.setText("Production enregistrée !");
                tfCodeProd.clear();
                tfValProd.clear();
                cbProd.setValue(null);
                cbUnite.setValue(UniteMesure.KG);
                rafraichirZones(tableZonesView);
                zonesObs.setAll(g.getZones());
            } catch (Exception ex) {
                lblMsg4.setTextFill(Color.RED);
                lblMsg4.setText("Erreur : " + ex.getMessage());
            }
        });

        btnRefresh.setOnAction(e -> rafraichirZones(tableZonesView));

        sp.getItems().add(zoneCenter);
        return sp;
    }

    // ══════════════════════════════════════════
    // PANNEAU CULTURES
    // ══════════════════════════════════════════
    SplitPane creerPanneauCultures() {
        SplitPane sp = new SplitPane();
        VBox page = boite();
        page.getChildren().add(titre("Gestion des Cultures"));

        // Create zone selection dropdown that updates dynamically
        ObservableList<ZoneCulture> cultureZones = FXCollections.observableArrayList();
        
        // Function to update culture zones list
        Runnable refreshCultureZones = () -> {
            cultureZones.clear();
            for (Zone z : zonesObs) {
                if (z instanceof ZoneCulture) cultureZones.add((ZoneCulture) z);
            }
        };
        refreshCultureZones.run();
        
        // Listen for zone changes
        zonesObs.addListener((ListChangeListener<Zone>) c -> refreshCultureZones.run());

        // Create zone selection dropdown
        ComboBox<ZoneCulture> cbZoneSelection = new ComboBox<>(cultureZones);
        cbZoneSelection.setStyle("-fx-font-size: 13; -fx-padding: 8;");
        cbZoneSelection.setPrefWidth(Double.MAX_VALUE);
        if (!cultureZones.isEmpty()) {
            cbZoneSelection.setValue(cultureZones.get(0));
        }

        // Create container for selected zone's form
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: white; -fx-padding: 12;");

        // Update content when zone selection changes
        cbZoneSelection.setOnAction(e -> {
            ZoneCulture selectedZone = cbZoneSelection.getValue();
            if (selectedZone != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(creerCarteCulture(selectedZone));
            }
        });

        // Initial display
        if (!cultureZones.isEmpty()) {
            contentArea.getChildren().add(creerCarteCulture(cultureZones.get(0)));
        }

        VBox top = boite();
        top.getChildren().addAll(sousTitre("Sélectionner une Zone de Culture"), cbZoneSelection);

        ScrollPane scroll = new ScrollPane(contentArea);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: white;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        
        page.getChildren().addAll(top, sep(), scroll);
        sp.getItems().add(page);
        return sp;
    }

    // ══════════════════════════════════════════
    // PANNEAU ANIMAUX
    // ══════════════════════════════════════════
    SplitPane creerPanneauAnimaux() {
        SplitPane sp = new SplitPane();
        VBox page = boite();
        page.getChildren().add(titre("Gestion des Animaux"));

        // Create zone selection dropdown that updates dynamically
        ObservableList<ZoneElevage> elevageZones = FXCollections.observableArrayList();
        
        // Function to update elevage zones list
        Runnable refreshElevageZones = () -> {
            elevageZones.clear();
            for (Zone z : zonesObs) {
                if (z instanceof ZoneElevage) elevageZones.add((ZoneElevage) z);
            }
        };
        refreshElevageZones.run();
        
        // Listen for zone changes
        zonesObs.addListener((ListChangeListener<Zone>) c -> refreshElevageZones.run());

        // Create zone selection dropdown
        ComboBox<ZoneElevage> cbZoneSelection = new ComboBox<>(elevageZones);
        cbZoneSelection.setStyle("-fx-font-size: 13; -fx-padding: 8;");
        cbZoneSelection.setPrefWidth(Double.MAX_VALUE);
        if (!elevageZones.isEmpty()) {
            cbZoneSelection.setValue(elevageZones.get(0));
        }

        // Create container for selected zone's form
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: white; -fx-padding: 12;");

        // Update content when zone selection changes
        cbZoneSelection.setOnAction(e -> {
            ZoneElevage selectedZone = cbZoneSelection.getValue();
            if (selectedZone != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(creerCarteElevage(selectedZone));
            }
        });

        // Initial display
        if (!elevageZones.isEmpty()) {
            contentArea.getChildren().add(creerCarteElevage(elevageZones.get(0)));
        }

        VBox top = boite();
        top.getChildren().addAll(sousTitre("Sélectionner une Zone d'Élevage"), cbZoneSelection);

        ScrollPane scroll = new ScrollPane(contentArea);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: white;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        
        page.getChildren().addAll(top, sep(), scroll);
        sp.getItems().add(page);
        return sp;
    }

    // ══════════════════════════════════════════
    // PANNEAU AQUACOLE
    // ══════════════════════════════════════════
    VBox creerPanneauAquacole() {
        VBox page = boite();
        page.getChildren().add(titre("Gestion des Zones Aquacoles"));

        // Create zone selection dropdown that updates dynamically
        ObservableList<ZoneAquacole> aquacoleZones = FXCollections.observableArrayList();
        
        // Function to update aquacole zones list
        Runnable refreshAquacoleZones = () -> {
            aquacoleZones.clear();
            for (Zone z : zonesObs) {
                if (z instanceof ZoneAquacole) aquacoleZones.add((ZoneAquacole) z);
            }
        };
        refreshAquacoleZones.run();
        
        // Listen for zone changes
        zonesObs.addListener((ListChangeListener<Zone>) c -> refreshAquacoleZones.run());

        // Create zone selection dropdown
        ComboBox<ZoneAquacole> cbZoneSelection = new ComboBox<>(aquacoleZones);
        cbZoneSelection.setStyle("-fx-font-size: 13; -fx-padding: 8;");
        cbZoneSelection.setPrefWidth(Double.MAX_VALUE);
        if (!aquacoleZones.isEmpty()) {
            cbZoneSelection.setValue(aquacoleZones.get(0));
        }

        // Create container for selected zone's form
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: white; -fx-padding: 12;");

        // Update content when zone selection changes
        cbZoneSelection.setOnAction(e -> {
            ZoneAquacole selectedZone = cbZoneSelection.getValue();
            if (selectedZone != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(creerCarteAquacole(selectedZone));
            }
        });

        // Initial display
        if (!aquacoleZones.isEmpty()) {
            contentArea.getChildren().add(creerCarteAquacole(aquacoleZones.get(0)));
        }

        VBox top = boite();
        top.getChildren().addAll(sousTitre("Sélectionner une Zone Aquacole"), cbZoneSelection);

        ScrollPane scroll = new ScrollPane(contentArea);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: white;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        
        page.getChildren().addAll(top, sep(), scroll);
        return page;
    }

    VBox creerCarteAquacole(ZoneAquacole za) {
        VBox page = boite();
        page.getChildren().add(titre("Zone Aquacole — " + za.getNom()));

        ToggleGroup groupe = new ToggleGroup();
        ToggleButton btnEspece = new ToggleButton("Ajouter Espèce");
        ToggleButton btnProg = new ToggleButton("Programme espèce");
        ToggleButton btnVoir = new ToggleButton("Voir Programme");
        for (ToggleButton b : Arrays.asList(btnEspece, btnProg, btnVoir)) { b.setToggleGroup(groupe); b.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(b, Priority.ALWAYS); }
        btnEspece.setSelected(true);
        HBox top = new HBox(8, btnEspece, btnProg, btnVoir);

        TextField tfNom = tfPlaceholder("Nom espèce (ex: Poisson)");
        TextField tfNb = tfPlaceholder("Nombre individus");
        Button btnAjouterAction = btn("Ajouter", C_VERT);
        Label lblMsg = msg();
        VBox formEspece = boite();
        formEspece.getChildren().addAll(sousTitre("Ajouter une Espèce"), ligne("Nom :", tfNom), ligne("Individus :", tfNb), btnAjouterAction, lblMsg);

        TextField tfAlim = tfPlaceholder("Type aliment");
        TextField tfQte = tfPlaceholder("Quantité/repas");
        Button btnProgAction = btn("Définir pour espèce", C_BLEU);
        Label lblProg = msg();
        Label lblProgramme = new Label("Sélectionnez une espèce dans le tableau pour voir/éditer son programme.");
        lblProgramme.setWrapText(true);
        VBox formProg = boite();
        formProg.getChildren().addAll(sousTitre("Programme Alimentation"), ligne("Aliment :", tfAlim), ligne("Quantité :", tfQte), btnProgAction, lblProg, lblProgramme);

        TextArea taProgramme = new TextArea();
        taProgramme.setEditable(false);
        taProgramme.setWrapText(true);
        taProgramme.setPrefHeight(180);
        Button btnVoirAction = btn("Afficher", C_ORANGE);
        VBox formVoir = boite();
        formVoir.getChildren().addAll(sousTitre("Voir le Programme"), btnVoirAction, taProgramme);

        StackPane forms = new StackPane(formEspece, formProg, formVoir);
        setVue(formEspece, formProg, formVoir);
        btnEspece.setOnAction(e -> setVue(formEspece, formProg, formVoir));
        btnProg.setOnAction(e -> setVue(formProg, formEspece, formVoir));
        btnVoir.setOnAction(e -> setVue(formVoir, formEspece, formProg));

        TableView<String[]> table = new TableView<>();
        table.getColumns().addAll(col("Espèce", d -> d[0]), col("Individus", d -> d[1]), col("Programme", d -> d[2]));
        table.setPrefHeight(250);
        styleTable(table);
        Label lblTotal = new Label();
        lblTotal.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Runnable refresh = () -> { ObservableList<String[]> data = FXCollections.observableArrayList(); for (EspeceAquacole es : za.getEspecesAquacoles()) data.add(new String[]{es.getNom(), String.valueOf(es.getNombreIndividus()), es.getProgramme() == null ? "-" : es.getProgramme().getDetails()}); table.setItems(data); lblTotal.setText("Total individus : " + za.getNombreTotalIndividus()); };
        refresh.run();

        btnAjouterAction.setOnAction(e -> {
            try { za.ajouterEspece(new EspeceAquacole(tfNom.getText().trim(), Integer.parseInt(tfNb.getText().trim()))); lblMsg.setTextFill(Color.GREEN); lblMsg.setText("Espèce ajoutée !"); tfNom.clear(); tfNb.clear(); refresh.run(); }
            catch (Exception ex) { lblMsg.setTextFill(Color.RED); lblMsg.setText("Erreur : " + ex.getMessage()); }
        });
        btnProgAction.setOnAction(e -> {
            try {
                String[] selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) throw new IllegalArgumentException("Sélectionnez une espèce dans le tableau");
                ProgrammeAlimentation programme = new ProgrammeAlimentation(tfAlim.getText().trim(), Double.parseDouble(tfQte.getText().trim()));
                g.definirProgramme(za.getCode(), selected[0], programme);
                lblProg.setTextFill(Color.GREEN);
                lblProg.setText("Programme défini pour " + selected[0] + " !");
                tfAlim.clear(); tfQte.clear();
                lblProgramme.setText(programme.getDetails());
                refresh.run();
            } catch (Exception ex) { lblProg.setTextFill(Color.RED); lblProg.setText("Erreur : " + ex.getMessage()); }
        });
        btnVoirAction.setOnAction(e -> {
            String[] selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                taProgramme.setText("Sélectionnez une espèce dans le tableau.");
                return;
            }
            EspeceAquacole espece = za.trouverEspece(selected[0]);
            taProgramme.setText(espece == null || espece.getProgramme() == null ? "Aucun programme défini pour cette espèce." : espece.getProgramme().getDetails());
        });

        page.getChildren().addAll(top, forms, sep(), table, lblTotal, lblProgramme);
        return page;
    }

    VBox creerCarteCulture(ZoneCulture zone) {
        VBox carte = boite();
        carte.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: " + C_BORDURE + "; -fx-border-radius: 16; -fx-border-width: 1; -fx-padding: 12;");

        Label titreZone = sousTitre(zone.getCode() + " - " + zone.getNom());
        ToggleGroup groupe = new ToggleGroup();
        ToggleButton btnAjouter = new ToggleButton("Ajouter Culture");
        ToggleButton btnStade = new ToggleButton("Stade");
        ToggleButton btnRapport = new ToggleButton("Rapport");
        for (ToggleButton b : Arrays.asList(btnAjouter, btnStade, btnRapport)) { b.setToggleGroup(groupe); b.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(b, Priority.ALWAYS); }
        btnAjouter.setSelected(true);
        HBox top = new HBox(8, btnAjouter, btnStade, btnRapport);

        TextField tfNom = tfPlaceholder("Nom de la culture");
        ComboBox<FamilleCulture> cbFam = new ComboBox<>(FXCollections.observableArrayList(FamilleCulture.values()));
        cbFam.setValue(FamilleCulture.CEREALE);
        DatePicker dpPlant = new DatePicker(LocalDate.now());
        DatePicker dpRecol = new DatePicker(LocalDate.now().plusMonths(5));
        TextField tfPhMin = tfPlaceholder("pH min");
        TextField tfPhMax = tfPlaceholder("pH max");
        TextField tfHumMin = tfPlaceholder("Hum min");
        TextField tfHumMax = tfPlaceholder("Hum max");
        Button btnAjouterAction = btn("Ajouter", C_VERT);
        Label lblMsg = msg();
        VBox formAjouter = boite();
        formAjouter.getChildren().addAll(sousTitre("Ajouter une Culture"), ligne("Nom :", tfNom), ligne("Famille :", cbFam), ligne("Plantation :", dpPlant), ligne("Récolte :", dpRecol), ligne("pH min :", tfPhMin), ligne("pH max :", tfPhMax), ligne("Hum min :", tfHumMin), ligne("Hum max :", tfHumMax), btnAjouterAction, lblMsg);

        TextField tfNomStade = tfPlaceholder("Nom de la culture");
        ComboBox<StadeDeCroissance> cbStade = new ComboBox<>(FXCollections.observableArrayList(StadeDeCroissance.values()));
        cbStade.setValue(StadeDeCroissance.GERMINATION);
        Button btnStadeAction = btn("Mettre à jour", C_ORANGE);
        Label lblMsg2 = msg();
        Label lblSelectionCulture = new Label("Sélectionnez une culture dans le tableau pour la modifier.");
        lblSelectionCulture.setWrapText(true);
        VBox formStade = boite();
        formStade.getChildren().addAll(sousTitre("Mettre à Jour le Stade"), ligne("Culture :", tfNomStade), ligne("Stade :", cbStade), lblSelectionCulture, btnStadeAction, lblMsg2);

        TextArea taRapport = new TextArea(zone.getRapportCultures());
        taRapport.setEditable(false);
        taRapport.setPrefHeight(220);
        Button btnActualiserRapport = btn("Actualiser", C_BLEU);
        VBox formRapport = boite();
        formRapport.getChildren().addAll(sousTitre("Rapport Cultures"), btnActualiserRapport, taRapport);

        StackPane forms = new StackPane(formAjouter, formStade, formRapport);
        setVue(formAjouter, formStade, formRapport);
        btnAjouter.setOnAction(e -> setVue(formAjouter, formStade, formRapport));
        btnStade.setOnAction(e -> setVue(formStade, formAjouter, formRapport));
        btnRapport.setOnAction(e -> setVue(formRapport, formAjouter, formStade));

        TableView<String[]> table = new TableView<>();
        table.getColumns().addAll(col("Nom", d -> d[0]), col("Famille", d -> d[1]), col("Stade", d -> d[2]), col("Plantation", d -> d[3]), col("Récolte", d -> d[4]));
        table.setPrefHeight(220);
        styleTable(table);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                tfNomStade.setText(newSel[0]);
                lblSelectionCulture.setText("Culture sélectionnée : " + newSel[0]);
            }
        });
        Runnable refresh = () -> {
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Culture c : zone.getCultures()) data.add(new String[]{c.getNom(), c.getFamille().toString(), c.getStadeCroissance().toString(), c.getDatePlantation().toString(), c.getDateRecolte().toString()});
            table.setItems(data);
            taRapport.setText(zone.getRapportCultures());
        };
        refresh.run();

        btnAjouterAction.setOnAction(e -> {
            try {
                ExigencesPedologiques ex = new ExigencesPedologiques(Double.parseDouble(tfPhMin.getText()), Double.parseDouble(tfPhMax.getText()), Double.parseDouble(tfHumMin.getText()), Double.parseDouble(tfHumMax.getText()));
                Culture c = new Culture(tfNom.getText().trim(), cbFam.getValue(), dpPlant.getValue(), dpRecol.getValue(), ex);
                g.ajouterCulture(zone.getCode(), c);
                lblMsg.setTextFill(Color.GREEN);
                lblMsg.setText("Culture ajoutée !");
                tfNom.clear(); tfPhMin.clear(); tfPhMax.clear(); tfHumMin.clear(); tfHumMax.clear();
                cbFam.setValue(FamilleCulture.CEREALE);
                dpPlant.setValue(LocalDate.now());
                dpRecol.setValue(LocalDate.now().plusMonths(5));
                refresh.run();
            } catch (Exception ex) { lblMsg.setTextFill(Color.RED); lblMsg.setText("Erreur : " + ex.getMessage()); }
        });

        btnStadeAction.setOnAction(e -> {
            try {
                String nomCulture = tfNomStade.getText().trim();
                if (nomCulture.isEmpty()) {
                    throw new IllegalArgumentException("Sélectionnez une culture dans le tableau.");
                }
                g.mettreAJourStade(zone.getCode(), nomCulture, cbStade.getValue());
                lblMsg2.setTextFill(Color.GREEN);
                lblMsg2.setText("Stade mis à jour !");
                refresh.run();
            } catch (Exception ex) { lblMsg2.setTextFill(Color.RED); lblMsg2.setText("Erreur : " + ex.getMessage()); }
        });

        btnActualiserRapport.setOnAction(e -> taRapport.setText(zone.getRapportCultures()));

        carte.getChildren().addAll(titreZone, top, forms, sep(), table);
        return carte;
    }

    VBox creerCarteElevage(ZoneElevage zone) {
        VBox carte = boite();
        carte.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 16; -fx-border-color: " + C_BORDURE + "; -fx-border-radius: 16; -fx-border-width: 1; -fx-padding: 12;");

        Label titreZone = sousTitre(zone.getCode() + " - " + zone.getNom());
        ToggleGroup groupe = new ToggleGroup();
        ToggleButton btnAjouter = new ToggleButton("Ajouter Animal");
        ToggleButton btnEv = new ToggleButton("Événement");
        ToggleButton btnProg = new ToggleButton("Programme");
        for (ToggleButton b : Arrays.asList(btnAjouter, btnEv, btnProg)) { b.setToggleGroup(groupe); b.setMaxWidth(Double.MAX_VALUE); HBox.setHgrow(b, Priority.ALWAYS); }
        btnAjouter.setSelected(true);
        HBox top = new HBox(8, btnAjouter, btnEv, btnProg);

        TextField tfNum = tfPlaceholder("Numéro");
        TextField tfEspece = tfPlaceholder("Espèce");
        TextField tfAge = tfPlaceholder("Age");
        TextField tfPoids = tfPlaceholder("Poids");
        ComboBox<EtatDesante> cbEtat = new ComboBox<>(FXCollections.observableArrayList(EtatDesante.values()));
        cbEtat.setValue(EtatDesante.SAIN);
        Button btnAjouterAction = btn("Ajouter", C_VERT);
        Label lblMsg = msg();
        VBox formAjouter = boite();
        formAjouter.getChildren().addAll(sousTitre("Ajouter un Animal"), ligne("Numéro :", tfNum), ligne("Espèce :", tfEspece), ligne("Age :", tfAge), ligne("Poids :", tfPoids), ligne("Etat :", cbEtat), btnAjouterAction, lblMsg);

        TextField tfNumEv = tfPlaceholder("Numéro animal");
        ComboBox<TypeEvenementSanitaire> cbEv = new ComboBox<>(FXCollections.observableArrayList(TypeEvenementSanitaire.values()));
        cbEv.setValue(TypeEvenementSanitaire.MALADIE);
        TextField tfDesc = tfPlaceholder("Description");
        TextField tfAncPoids = tfPlaceholder("Ancien poids");
        TextField tfNvPoids = tfPlaceholder("Nouveau poids");
        Button btnEvAction = btn("Consigner", C_ORANGE);
        Label lblEv = msg();
        VBox formEv = boite();
        formEv.getChildren().addAll(sousTitre("Consigner un Événement"), ligne("Numéro :", tfNumEv), ligne("Type :", cbEv), ligne("Description :", tfDesc), ligne("Ancien poids :", tfAncPoids), ligne("Nouveau poids :", tfNvPoids), btnEvAction, lblEv);

        TextField tfAlim = tfPlaceholder("Type aliment");
        TextField tfQte = tfPlaceholder("Quantité/repas");
        Button btnProgAction = btn("Définir", C_BLEU);
        Label lblProg = msg();
        Label lblProgrammeDetail = new Label();
        lblProgrammeDetail.setWrapText(true);
        VBox formProg = boite();
        formProg.getChildren().addAll(sousTitre("Programme Alimentation"), ligne("Aliment :", tfAlim), ligne("Quantité :", tfQte), btnProgAction, lblProg, lblProgrammeDetail);

        StackPane forms = new StackPane(formAjouter, formEv, formProg);
        setVue(formAjouter, formEv, formProg);
        btnAjouter.setOnAction(e -> setVue(formAjouter, formEv, formProg));
        btnEv.setOnAction(e -> setVue(formEv, formAjouter, formProg));
        btnProg.setOnAction(e -> setVue(formProg, formAjouter, formEv));

        TableView<String[]> table = new TableView<>();
        table.getColumns().addAll(col("Num", d -> d[0]), col("Espèce", d -> d[1]), col("Age", d -> d[2]), col("Poids", d -> d[3]), col("Santé", d -> d[4]));
        table.setPrefHeight(220);
        styleTable(table);
        Runnable refresh = () -> {
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Animal a : zone.getAnimaux()) data.add(new String[]{String.valueOf(a.getCode()), a.getEspece(), String.valueOf(a.getAge()), a.getPoids()+"kg", a.getEtat_sante().toString()});
            table.setItems(data);
        };
        refresh.run();

        Button btnHist = btn("Historique", C_BLEU);
        HBox histBox = new HBox(8, btnHist);
        histBox.setAlignment(Pos.CENTER_LEFT);
        btnHist.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Sélectionnez un animal dans le tableau.", ButtonType.OK);
                a.setHeaderText("Aucune sélection");
                a.showAndWait();
                return;
            }
            int code;
            try { code = Integer.parseInt(sel[0]); } catch (NumberFormatException ex) { return; }
            Animal found = null;
            for (Animal aa : zone.getAnimaux()) if (aa.getCode() == code) { found = aa; break; }
            if (found == null) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Animal introuvable dans la zone.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            StringBuilder sb = new StringBuilder();
            if (found.getHistoriqueSanitaire().isEmpty()) sb.append("Aucun événement sanitaire pour cet animal.");
            else for (EvenementSanitaire ev : found.getHistoriqueSanitaire()) sb.append(ev.toString()).append("\n");
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Historique — Animal " + found.getCode());
            info.setHeaderText(null);
            TextArea ta = new TextArea(sb.toString());
            ta.setEditable(false);
            ta.setWrapText(true);
            ta.setPrefWidth(600);
            ta.setPrefHeight(300);
            info.getDialogPane().setContent(ta);
            info.showAndWait();
        });

        btnAjouterAction.setOnAction(e -> {
            try {
                Animal a = new Animal(Integer.parseInt(tfNum.getText().trim()), tfEspece.getText().trim(), Integer.parseInt(tfAge.getText().trim()), Integer.parseInt(tfPoids.getText().trim()), cbEtat.getValue());
                g.ajouterAnimal(zone.getCode(), a);
                lblMsg.setTextFill(Color.GREEN);
                lblMsg.setText("Animal ajouté !");
                tfNum.clear(); tfEspece.clear(); tfAge.clear(); tfPoids.clear();
                cbEtat.setValue(EtatDesante.SAIN);
                refresh.run();
            } catch (Exception ex) { lblMsg.setTextFill(Color.RED); lblMsg.setText("Erreur : " + ex.getMessage()); }
        });

        btnEvAction.setOnAction(e -> {
            try {
                EvenementSanitaire ev = (cbEv.getValue() == TypeEvenementSanitaire.EVOLUTION_POIDS)
                    ? new EvenementSanitaire(LocalDate.now(), cbEv.getValue(), tfDesc.getText(), Double.parseDouble(tfAncPoids.getText()), Double.parseDouble(tfNvPoids.getText()))
                    : new EvenementSanitaire(LocalDate.now(), cbEv.getValue(), tfDesc.getText());
                g.consignerEvenement(zone.getCode(), tfNumEv.getText().trim(), ev);
                lblEv.setTextFill(Color.GREEN);
                lblEv.setText("Événement consigné !");
                tfNumEv.clear(); tfDesc.clear(); tfAncPoids.clear(); tfNvPoids.clear();
                cbEv.setValue(TypeEvenementSanitaire.MALADIE);
                refresh.run();
            } catch (Exception ex) { lblEv.setTextFill(Color.RED); lblEv.setText("Erreur : " + ex.getMessage()); }
        });

        btnProgAction.setOnAction(e -> {
            try {
                g.definirProgramme(zone.getCode(), new ProgrammeAlimentation(tfAlim.getText().trim(), Double.parseDouble(tfQte.getText().trim())));
                lblProg.setTextFill(Color.GREEN);
                lblProg.setText("Programme défini !");
                tfAlim.clear(); tfQte.clear();
                ProgrammeAlimentation p = zone.getProgramme();
                lblProgrammeDetail.setText(p == null ? "Aucun programme défini." : p.getDetails());
            } catch (Exception ex) { lblProg.setTextFill(Color.RED); lblProg.setText("Erreur : " + ex.getMessage()); }
        });

        carte.getChildren().addAll(titreZone, top, forms, sep(), histBox, table);
        return carte;
    }

    // ══════════════════════════════════════════
    // PANNEAU CAPTEURS
    // ══════════════════════════════════════════
    TabPane creerPanneauCapteurs() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            creerTab("Ajouter Capteur",   ongletAjouterCapteur()),
            creerTab("Changer Statut",    ongletChangerStatut())
        );
        return tabs;
    }

    VBox ongletAjouterCapteur() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Ajouter et Configurer un Capteur"));

        ComboBox<Zone> cbZone = new ComboBox<>(zonesObs);
        cbZone.setConverter(new javafx.util.StringConverter<Zone>(){
            @Override public String toString(Zone z) { return z == null ? "" : z.getCode() + " - " + z.getNom(); }
            @Override public Zone fromString(String s) { return null; }
        });
        if (!cbZone.getItems().isEmpty()) cbZone.setValue(cbZone.getItems().get(0));
        ComboBox<String> cbType = combo("SOL","ENVIRONNEMENTAL","GPS","BIOMETRIQUE","EAU");
        cbType.setValue("SOL");
        TextField tfMin = tfPlaceholder("Seuil min");
        TextField tfMax = tfPlaceholder("Seuil max");
        Button btnAjouter = btn("Ajouter le Capteur", C_VERT);
        Label  lblMsg     = msg();

        vbox.getChildren().addAll(
            ligne("Zone :", cbZone), ligne("Type :", cbType),
            ligne("Seuil min :", tfMin), ligne("Seuil max :", tfMax),
            btnAjouter, lblMsg, sep()
        );

        vbox.getChildren().add(sousTitre("Capteurs existants"));
        TableView<String[]> table = new TableView<>();
        table.getColumns().addAll(
            col("Code",   d -> d[0]),
            col("Type",   d -> d[1]),
            col("Zone",   d -> d[2]),
            col("Seuils", d -> d[3]),
            col("Statut", d -> d[4])
        );
        table.setPrefHeight(300);

        Runnable refresh = () -> {
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Zone z : g.getZones())
                for (Capteur c : z.getCapteurs())
                    data.add(new String[]{
                        c.getCode(), c.getClass().getSimpleName(), z.getNom(),
                        c.getPlageSeuils() != null ? c.getPlageSeuils().toString() : "-",
                        c.getStatut().toString()
                    });
            table.setItems(data);
        };
        refresh.run();
        
        // Listen for zone changes to refresh sensor list
        zonesObs.addListener((ListChangeListener<Zone>) c -> refresh.run());
        
        btnAjouter.setOnAction(e -> {
            try {
                Zone zone = cbZone.getValue();
                double min = Double.parseDouble(tfMin.getText().trim());
                double max = Double.parseDouble(tfMax.getText().trim());
                g.ajouterEtConfigurerCapteur(cbType.getValue(), zone, min, max);
                lblMsg.setTextFill(Color.GREEN);
                lblMsg.setText("Capteur ajouté !");
                tfMin.clear(); tfMax.clear();
                cbType.setValue("SOL");
                if (!cbZone.getItems().isEmpty()) cbZone.setValue(cbZone.getItems().get(0));
                rafraichirMenusCapteurs();
                rafraichirTableauxCapteurs();
                refresh.run();
            } catch (Exception ex) {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("Erreur : " + ex.getMessage());
            }
        });

        vbox.getChildren().add(table);
        return vbox;
    }

    VBox ongletChangerStatut() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Changer le Statut d'un Capteur"));

        TableView<String[]> table = new TableView<>();
        table.getColumns().addAll(
            col("Code",   d -> d[0]),
            col("Type",   d -> d[1]),
            col("Zone",   d -> d[2]),
            col("Statut", d -> d[3])
        );
        colorerStatutCapteur(table);
        table.setPrefHeight(300);

        Runnable refresh = () -> {
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Zone z : g.getZones())
                for (Capteur c : z.getCapteurs())
                    data.add(new String[]{
                        c.getCode(), c.getClass().getSimpleName(),
                        z.getNom(), c.getStatut().toString()
                    });
            table.setItems(data);
        };
        capteurTableRefreshers.add(refresh);
        refresh.run();
        
        // Listen for zone changes to refresh sensor list
        zonesObs.addListener((ListChangeListener<Zone>) c -> refresh.run());

        ComboBox<String> cbStatut = combo("ACTIF","SUSPENDU","DEFAILLANT");
        cbStatut.setValue("ACTIF");
        Button btnChanger = btn("Changer le Statut", C_ORANGE);
        Label  lblMsg     = msg();

        btnChanger.setOnAction(e -> {
            String[] sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { lblMsg.setTextFill(Color.RED); lblMsg.setText("Sélectionnez un capteur !"); return; }
            Capteur c = trouverCapteur(sel[0]);
            if (c == null) return;
            StatutCapteur s = StatutCapteur.valueOf(cbStatut.getValue());
            boolean ok = g.changerStatutCapteur(c, s);
            if (ok) {
                lblMsg.setTextFill(Color.GREEN);
                lblMsg.setText("Statut changé : " + s);
            } else {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("Changement refusé pour ce capteur.");
            }
            refresh.run();
        });

        vbox.getChildren().addAll(table, ligne("Nouveau statut :", cbStatut), btnChanger, lblMsg);
        return vbox;
    }

    // ══════════════════════════════════════════
    // PANNEAU RELEVES
    // ══════════════════════════════════════════
    TabPane creerPanneauReleves() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            creerTab("Ajouter Relevé",   ongletAjouterReleve()),
            creerTab("Tableau de Bord",   ongletTableauBord()),
            creerTab("Historique",        ongletHistoriqueReleves()),
            creerTab("Graphiques",        ongletGraphiques())
        );
        return tabs;
    }

    VBox ongletAjouterReleve() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Ajouter un Relevé"));

        ComboBox<String> cbCap = new ComboBox<>();
        capteurComboBoxes.add(cbCap);
        remplirComboCapteurs(cbCap);
        cbCap.setPromptText("Choisir capteur");
        cbCap.setPrefWidth(260);

        TextField tfValeur = tfPlaceholder("Valeur");
        ComboBox<String> cbUnite = combo();
        cbUnite.setPrefWidth(180);

        TextField tfLat = tfPlaceholder("Latitude");
        TextField tfLon = tfPlaceholder("Longitude");

        TableView<String[]> tableReleves = new TableView<>();
        tableReleves.getColumns().addAll(
            col("Date", d -> d[0]),
            col("Type", d -> d[1]),
            col("Valeur", d -> d[2]),
            col("Unité", d -> d[3]),
            col("Niveau", d -> d[4])
        );
        colorerNiveau((TableColumn<String[], String>) tableReleves.getColumns().get(4));
        tableReleves.setPrefHeight(260);

        VBox boxNumerique = boite();
        boxNumerique.getChildren().addAll(
            sousTitre("Relevé numérique"),
            ligne("Valeur :", tfValeur),
            ligne("Unité :", cbUnite)
        );

        VBox boxGps = boite();
        boxGps.getChildren().addAll(
            sousTitre("Relevé GPS"),
            ligne("Latitude :", tfLat),
            ligne("Longitude :", tfLon)
        );
        boxGps.setVisible(false);
        boxGps.setManaged(false);

        Label lblMsg = msg();
        Button btnAjouter = btn("Ajouter le Relevé", C_VERT);

        Runnable rafraichirTableReleves = () -> {
            Capteur capteur = trouverCapteurParLabel(cbCap.getValue());
            ObservableList<String[]> data = FXCollections.observableArrayList();
            if (capteur != null) {
                for (Releve r : capteur.getReleves()) {
                    if (r instanceof ReleveGPS) {
                        ReleveGPS rg = (ReleveGPS) r;
                        data.add(new String[]{
                            r.getDate().toString(),
                            "GPS",
                            rg.getLatitude() + ", " + rg.getLongitude(),
                            "°",
                            r.getNiveauReleve().toString()
                        });
                    } else if (r instanceof ReleveNumerique) {
                        ReleveNumerique rn = (ReleveNumerique) r;
                        data.add(new String[]{
                            r.getDate().toString(),
                            capteur.getClass().getSimpleName(),
                            String.valueOf(rn.getValeur()),
                            rn.getUnite().getSymbole(),
                            r.getNiveauReleve().toString()
                        });
                    }
                }
            }
            tableReleves.setItems(data);
        };

        Runnable majFormulaire = () -> {
            Capteur capteur = trouverCapteurParLabel(cbCap.getValue());
            if (capteur == null) {
                boxNumerique.setVisible(true);
                boxNumerique.setManaged(true);
                boxGps.setVisible(false);
                boxGps.setManaged(false);
                cbUnite.getItems().clear();
                return;
            }

            boolean gps = capteur instanceof CapteurGPS;
            boxGps.setVisible(gps);
            boxGps.setManaged(gps);
            boxNumerique.setVisible(!gps);
            boxNumerique.setManaged(!gps);

            if (!gps) {
                remplirUnitesPourCapteur(cbUnite, capteur);
            }
        };

        cbCap.valueProperty().addListener((obs, oldV, newV) -> majFormulaire.run());
        cbCap.valueProperty().addListener((obs, oldV, newV) -> rafraichirTableReleves.run());
        if (!cbCap.getItems().isEmpty()) {
            cbCap.setValue(cbCap.getItems().get(0));
        }
        majFormulaire.run();
        rafraichirTableReleves.run();

        btnAjouter.setOnAction(e -> {
            try {
                Capteur capteur = trouverCapteurParLabel(cbCap.getValue());
                if (capteur == null) {
                    lblMsg.setTextFill(Color.RED);
                    lblMsg.setText("Sélectionnez un capteur !");
                    return;
                }

                if (capteur instanceof CapteurGPS) {
                    double latitude = Double.parseDouble(tfLat.getText().trim());
                    double longitude = Double.parseDouble(tfLon.getText().trim());
                    String message = g.enregistrerReleveGPS(capteur.getCode(), latitude, longitude);
                    boolean added = message != null && message.toLowerCase().contains("ajouté");
                    lblMsg.setTextFill(added ? Color.GREEN : Color.RED);
                    lblMsg.setText(message == null ? "Erreur inconnue." : message);
                    tfLat.clear();
                    tfLon.clear();
                } else if (capteur instanceof CapteurNumerique) {
                    double valeur = Double.parseDouble(tfValeur.getText().trim());
                    String unite = cbUnite.getValue();
                    String message = g.enregistrerReleveNumerique(capteur.getCode(), valeur, unite);
                    boolean added = message != null && message.toLowerCase().contains("ajouté");
                    lblMsg.setTextFill(added ? Color.GREEN : Color.RED);
                    lblMsg.setText(message == null ? "Erreur inconnue." : message);
                    tfValeur.clear();
                } else {
                    lblMsg.setTextFill(Color.RED);
                    lblMsg.setText("Type de capteur non supporté !");
                }
                rafraichirTableReleves.run();
            } catch (NumberFormatException ex) {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("Valeur numérique invalide !");
            } catch (Exception ex) {
                lblMsg.setTextFill(Color.RED);
                lblMsg.setText("Erreur : " + ex.getMessage());
            }
        });

        vbox.getChildren().addAll(
            ligne("Capteur :", cbCap),
            boxNumerique,
            boxGps,
            btnAjouter,
            lblMsg,
            sousTitre("Relevés du capteur sélectionné"),
            tableReleves,
            sep()
        );
        return vbox;
    }

    VBox ongletTableauBord() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Tableau de Bord des Relevés par Zone"));

        ComboBox<String> cbZone = new ComboBox<>(zonesLabels(false));
        cbZone.setValue(zoneLabel(zc));
        Button btnAfficher = btn("Afficher", C_BLEU);
        HBox hb = new HBox(10, new Label("Zone :"), cbZone, btnAfficher);
        hb.setAlignment(Pos.CENTER_LEFT);

        TableView<String[]> table = new TableView<>();
        TableColumn<String[], String> colCap    = col("Capteur",  d -> d[0]);
        TableColumn<String[], String> colValeur = col("Valeur",   d -> d[1]);
        TableColumn<String[], String> colUnite  = col("Unité",    d -> d[2]);
        TableColumn<String[], String> colNiveau = col("Niveau",   d -> d[3]);
        TableColumn<String[], String> colDate   = col("Date",     d -> d[4]);
        colorerNiveau(colNiveau);
        table.getColumns().addAll(colCap, colValeur, colUnite, colNiveau, colDate);
        table.setPrefHeight(450);

        btnAfficher.setOnAction(e -> {
            Zone zone = trouverZoneParLibelle(cbZone.getValue());
            if (zone == null) return;
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Capteur c : zone.getCapteurs()) {
                for (Releve r : c.getReleves()) {
                    if (r instanceof ReleveGPS) {
                        ReleveGPS rg = (ReleveGPS) r;
                        data.add(new String[]{
                            c.getCode()+" [GPS]",
                            rg.getLatitude()+", "+rg.getLongitude(),
                            "°", "NORMAL", r.getDate().toString()
                        });
                    } else {
                        ReleveNumerique rn = (ReleveNumerique) r;
                        data.add(new String[]{
                            c.getCode()+" ["+c.getClass().getSimpleName()+"]",
                            String.valueOf(rn.getValeur()),
                            rn.getUnite().getSymbole(),
                            r.getNiveauReleve().toString(),
                            r.getDate().toString()
                        });
                    }
                }
            }
            table.setItems(data);
        });

        vbox.getChildren().addAll(hb, sep(), table);
        return vbox;
    }

    VBox ongletHistoriqueReleves() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Historique des Relevés — Filtrable par Dates"));

        ComboBox<String> cbCap = new ComboBox<>();
        capteurComboBoxes.add(cbCap);
        remplirComboCapteurs(cbCap);
        cbCap.setPromptText("Choisir capteur");
        cbCap.setPrefWidth(220);
        DatePicker dpDeb = new DatePicker(LocalDate.now().minusDays(1));
        DatePicker dpFin = new DatePicker(LocalDate.now());
        Button btnSearch = btn("Rechercher", C_BLEU);
        Label  lblCount  = new Label();

        HBox hb = new HBox(10, new Label("Capteur :"), cbCap,
                           new Label("Du :"), dpDeb, new Label("Au :"), dpFin, btnSearch);
        hb.setAlignment(Pos.CENTER_LEFT);

        TableView<String[]> table = new TableView<>();
        TableColumn<String[], String> colDate   = col("Date",   d -> d[0]);
        TableColumn<String[], String> colValeur = col("Valeur", d -> d[1]);
        TableColumn<String[], String> colUnite  = col("Unité",  d -> d[2]);
        TableColumn<String[], String> colNiveau = col("Niveau", d -> d[3]);
        colorerNiveau(colNiveau);
        table.getColumns().addAll(colDate, colValeur, colUnite, colNiveau);
        table.setPrefHeight(400);

        btnSearch.setOnAction(e -> {
            Capteur c = trouverCapteurParLabel(cbCap.getValue());
            if (c == null) return;
            Date deb = java.sql.Date.valueOf(dpDeb.getValue());
            Date fin = java.sql.Date.valueOf(dpFin.getValue());
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Releve r : c.getReleves()) {
                if (!r.getDate().before(deb) && !r.getDate().after(fin)
                 && r instanceof ReleveNumerique) {
                    ReleveNumerique rn = (ReleveNumerique) r;
                    data.add(new String[]{
                        r.getDate().toString(),
                        String.valueOf(rn.getValeur()),
                        rn.getUnite().getSymbole(),
                        r.getNiveauReleve().toString()
                    });
                }
            }
            table.setItems(data);
            lblCount.setText(data.size() + " relevé(s) trouvé(s).");
        });

        vbox.getChildren().addAll(hb, lblCount, sep(), table);
        return vbox;
    }

    VBox ongletGraphiques() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Graphiques d'Évolution des Relevés"));

        ComboBox<String> cbMode = combo("Par Capteur","Par Zone");
        cbMode.setValue("Par Capteur");
        ComboBox<String> cbCap = new ComboBox<>();
        capteurComboBoxes.add(cbCap);
        remplirComboCapteurs(cbCap);
        ComboBox<String> cbZone = new ComboBox<>(zonesLabels(false));
        cbZone.setValue(zoneLabel(zc));
        cbZone.setVisible(false);

        cbMode.setOnAction(e -> {
            cbCap.setVisible(cbMode.getValue().equals("Par Capteur"));
            cbZone.setVisible(cbMode.getValue().equals("Par Zone"));
        });

        Button btnGen = btn("Générer le Graphique", C_BLEU);
        HBox hb = new HBox(10, new Label("Mode :"), cbMode, cbCap, cbZone, btnGen);
        hb.setAlignment(Pos.CENTER_LEFT);

        VBox vbGraph = new VBox();
        vbGraph.setPrefHeight(420);

        btnGen.setOnAction(e -> {
            vbGraph.getChildren().clear();
            if (cbMode.getValue().equals("Par Capteur")) {
                Capteur c = trouverCapteurParLabel(cbCap.getValue());
                if (c != null) vbGraph.getChildren().add(graphiqueCapteur(c));
            } else {
                Zone z = trouverZoneParLibelle(cbZone.getValue());
                if (z == null) return;
                vbGraph.getChildren().add(graphiqueZone(z));
            }
        });

        vbox.getChildren().addAll(hb, sep(), vbGraph);
        return vbox;
    }

    LineChart<String,Number> graphiqueCapteur(Capteur c) {
        CategoryAxis x = new CategoryAxis();
        NumberAxis   y = new NumberAxis();
        x.setLabel("Relevé");
        y.setLabel("Valeur");
        LineChart<String,Number> chart = new LineChart<>(x, y);
        chart.setTitle("Évolution — " + c.getCode() + " [" + c.getClass().getSimpleName() + "]");
        int i = 1;
        if (c instanceof CapteurGPS) {
            XYChart.Series<String,Number> serieLat = new XYChart.Series<>();
            XYChart.Series<String,Number> serieLon = new XYChart.Series<>();
            serieLat.setName(c.getCode() + " - Latitude");
            serieLon.setName(c.getCode() + " - Longitude");
            for (Releve r : c.getReleves()) {
                if (r instanceof ReleveGPS) {
                    ReleveGPS rg = (ReleveGPS) r;
                    String label = "R" + i++;
                    serieLat.getData().add(new XYChart.Data<>(label, rg.getLatitude()));
                    serieLon.getData().add(new XYChart.Data<>(label, rg.getLongitude()));
                }
            }
            chart.getData().addAll(serieLat, serieLon);
        } else {
            XYChart.Series<String,Number> serie = new XYChart.Series<>();
            serie.setName(c.getCode());
            for (Releve r : c.getReleves())
                if (r instanceof ReleveNumerique)
                    serie.getData().add(new XYChart.Data<>("R"+i++, ((ReleveNumerique)r).getValeur()));
            chart.getData().add(serie);
        }
        chart.setPrefHeight(400);
        return chart;
    }

    LineChart<String,Number> graphiqueZone(Zone zone) {
        CategoryAxis x = new CategoryAxis();
        NumberAxis   y = new NumberAxis();
        LineChart<String,Number> chart = new LineChart<>(x, y);
        chart.setTitle("Évolution — " + zone.getNom());
        for (Capteur c : zone.getCapteurs()) {
            int i = 1;
            if (c instanceof CapteurGPS) {
                XYChart.Series<String,Number> serieLat = new XYChart.Series<>();
                XYChart.Series<String,Number> serieLon = new XYChart.Series<>();
                serieLat.setName(c.getCode() + " - Latitude");
                serieLon.setName(c.getCode() + " - Longitude");
                for (Releve r : c.getReleves()) {
                    if (r instanceof ReleveGPS) {
                        ReleveGPS rg = (ReleveGPS) r;
                        String label = "R" + i++;
                        serieLat.getData().add(new XYChart.Data<>(label, rg.getLatitude()));
                        serieLon.getData().add(new XYChart.Data<>(label, rg.getLongitude()));
                    }
                }
                chart.getData().addAll(serieLat, serieLon);
            } else if (c instanceof CapteurNumerique) {
                XYChart.Series<String,Number> serie = new XYChart.Series<>();
                serie.setName(c.getCode());
                for (Releve r : c.getReleves())
                    if (r instanceof ReleveNumerique)
                        serie.getData().add(new XYChart.Data<>("R"+i++, ((ReleveNumerique)r).getValeur()));
                chart.getData().add(serie);
            }
        }
        chart.setPrefHeight(400);
        return chart;
    }

    // ══════════════════════════════════════════
    // PANNEAU ALERTES
    // ══════════════════════════════════════════
    TabPane creerPanneauAlertes() {
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
            creerTab("Panneau Actives",   ongletPanneauAlertes()),
            creerTab("Gérer Alertes",     ongletGererAlertes()),
            creerTab("Historique",        ongletHistoriqueAlertes())
        );
        return tabs;
    }

    VBox ongletPanneauAlertes() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Panneau des Alertes Actives — Critique en Premier"));

        Button btnRefresh = btn("Rafraîchir", C_BLEU);
        Label  lblInfo    = new Label("CRITIQUE s'affiche en premier — rouge = critique, orange = avertissement");
        lblInfo.setStyle("-fx-text-fill:#C0392B; -fx-font-style: italic;");

        TableView<String[]> table = new TableView<>();
        TableColumn<String[], String> colNiv = col("Niveau",  d -> d[0]);
        TableColumn<String[], String> colCap = col("Capteur", d -> d[1]);
        TableColumn<String[], String> colZon = col("Zone",    d -> d[2]);
        TableColumn<String[], String> colDat = col("Date",    d -> d[3]);
        TableColumn<String[], String> colSta = col("Statut",  d -> d[4]);
        colorerNiveauAlerte(colNiv);
        table.getColumns().addAll(colNiv, colCap, colZon, colDat, colSta);
        table.setPrefHeight(500);

        Runnable refresh = () -> {
            List<Alerte> critiques = new ArrayList<>();
            List<Alerte> avertissements = new ArrayList<>();
            for (Zone z : g.getZones())
                for (Alerte a : z.getAlertes())
                    if (a.getStatut() == StatutAlerte.ACTIVE) {
                        if (a.getNiveauGravite() == NiveauGravite.CRITIQUE)
                            critiques.add(a);
                        else
                            avertissements.add(a);
                    }
            critiques.addAll(avertissements);
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Alerte a : critiques)
                data.add(new String[]{
                    a.getNiveauGravite().toString(),
                    a.getCapteur().getCode()+" ["+a.getCapteur().getClass().getSimpleName()+"]",
                    a.getCapteur().getZone().getNom(),
                    a.getDate().toString(),
                    a.getStatut().toString()
                });
            table.setItems(data);
        };
        refresh.run();
        btnRefresh.setOnAction(e -> refresh.run());
        vbox.getChildren().addAll(btnRefresh, lblInfo, sep(), table);
        return vbox;
    }

    VBox ongletGererAlertes() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Acquitter ou Supprimer une Alerte"));

        ComboBox<String> cbZone = new ComboBox<>(zonesLabels(false));
        cbZone.setValue(zoneLabel(zc));
        Button btnCharger = btn("Charger", C_BLEU);
        HBox hb = new HBox(10, new Label("Zone :"), cbZone, btnCharger);
        hb.setAlignment(Pos.CENTER_LEFT);

        TableView<Alerte> table = new TableView<>();
        table.getColumns().addAll(
            colAlerte("Niveau",  a -> a.getNiveauGravite().toString()),
            colAlerte("Capteur", a -> a.getCapteur().getCode()),
            colAlerte("Date",    a -> a.getDate().toString()),
            colAlerte("Statut",  a -> a.getStatut().toString())
        );
        table.setPrefHeight(350);

        Button btnAcq = btn("Acquitter",  C_VERT);
        Button btnSup = btn("Supprimer",  C_ROUGE);
        Label  lblMsg = msg();
        HBox hbBtn = new HBox(10, btnAcq, btnSup);

        btnCharger.setOnAction(e -> {
            Zone z = trouverZoneParLibelle(cbZone.getValue());
            if (z == null) return;
            table.setItems(FXCollections.observableArrayList(z.getAlertes()));
        });

        btnAcq.setOnAction(e -> {
            Alerte a = table.getSelectionModel().getSelectedItem();
            if (a == null) { lblMsg.setTextFill(Color.RED); lblMsg.setText("Sélectionnez une alerte !"); return; }
            g.acquitterAlerte(a);
            lblMsg.setTextFill(Color.GREEN); lblMsg.setText("Alerte acquittée !");
            table.refresh();
        });

        btnSup.setOnAction(e -> {
            Alerte a = table.getSelectionModel().getSelectedItem();
            if (a == null) { lblMsg.setTextFill(Color.RED); lblMsg.setText("Sélectionnez une alerte !"); return; }
            g.supprimerAlerte(a);
            lblMsg.setTextFill(Color.GREEN); lblMsg.setText("Alerte supprimée !");
            table.refresh();
        });

        vbox.getChildren().addAll(hb, sep(), table, hbBtn, lblMsg);
        return vbox;
    }

    VBox ongletHistoriqueAlertes() {
        VBox vbox = boite();
        vbox.getChildren().add(titre("Historique des Alertes — Filtres"));

        ComboBox<String> cbZone = new ComboBox<>(zonesLabels(true));
        cbZone.setValue("Toutes");
        ComboBox<String> cbType = combo("Tous","CapteurSol","CapteurEnvironnemental",
            "CapteurGPS","CapteurBiometrique","CapteurEau");
        cbType.setValue("Tous");
        ComboBox<String> cbNiv = combo("Tous","AVERTISSEMENT","CRITIQUE");
        cbNiv.setValue("Tous");
        DatePicker dpDeb = new DatePicker(LocalDate.now().minusDays(1));
        DatePicker dpFin = new DatePicker(LocalDate.now());
        Button btnSearch = btn("Rechercher", C_BLEU);
        Label  lblCount  = new Label();

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.addRow(0, new Label("Zone :"),     cbZone);
        grid.addRow(1, new Label("Capteur :"),  cbType);
        grid.addRow(2, new Label("Niveau :"),   cbNiv);
        grid.addRow(3, new Label("Du :"),       dpDeb);
        grid.addRow(4, new Label("Au :"),       dpFin);

        TableView<String[]> table = new TableView<>();
        TableColumn<String[], String> colNiv = col("Niveau",  d -> d[0]);
        TableColumn<String[], String> colCap = col("Capteur", d -> d[1]);
        TableColumn<String[], String> colZon = col("Zone",    d -> d[2]);
        TableColumn<String[], String> colDat = col("Date",    d -> d[3]);
        TableColumn<String[], String> colSta = col("Statut",  d -> d[4]);
        colorerNiveauAlerte(colNiv);
        table.getColumns().addAll(colNiv, colCap, colZon, colDat, colSta);
        table.setPrefHeight(350);

        btnSearch.setOnAction(e -> {
            Date deb = java.sql.Date.valueOf(dpDeb.getValue());
            Date fin = java.sql.Date.valueOf(dpFin.getValue());
            List<Zone> zonesF = new ArrayList<>();
            if (cbZone.getValue().equals("Toutes"))
                zonesF.addAll(g.getZones());
            else {
                Zone z = trouverZoneParLibelle(cbZone.getValue());
                if (z != null) zonesF.add(z);
            }
            ObservableList<String[]> data = FXCollections.observableArrayList();
            for (Zone z : zonesF) {
                for (Alerte a : z.getAlertes()) {
                    if (!cbType.getValue().equals("Tous") &&
                        !a.getCapteur().getClass().getSimpleName().equals(cbType.getValue())) continue;
                    if (!cbNiv.getValue().equals("Tous") &&
                        !a.getNiveauGravite().toString().equals(cbNiv.getValue())) continue;
                    if (a.getDate().before(deb) || a.getDate().after(fin)) continue;
                    data.add(new String[]{
                        a.getNiveauGravite().toString(),
                        a.getCapteur().getCode()+" ["+a.getCapteur().getClass().getSimpleName()+"]",
                        z.getNom(), a.getDate().toString(), a.getStatut().toString()
                    });
                }
            }
            table.setItems(data);
            lblCount.setText(data.size() + " alerte(s) trouvée(s).");
        });

        vbox.getChildren().addAll(grid, btnSearch, lblCount, sep(), table);
        return vbox;
    }

    // ══════════════════════════════════════════
    // UTILITAIRES UI
    // ══════════════════════════════════════════
    VBox boite() {
        VBox v = new VBox(8);
        v.setPadding(new Insets(16));
        v.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: #C8DCC8;" +
            "-fx-border-radius: 20;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(26,60,46,0.10), 20, 0.12, 0, 4);"
        );
        return v;
    }

    Label titre(String t) {
        Label l = new Label(t);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        l.setStyle(
            "-fx-text-fill: #1A3C2E;" +
            "-fx-padding: 0 0 4 0;"
        );
        return l;
    }

    Label sousTitre(String t) {
        Label l = new Label(t);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        l.setStyle(
            "-fx-text-fill: #5A7A5A;" +
            "-fx-padding: 6 0 2 0;" +
            "-fx-letter-spacing: 0.5;"
        );
        return l;
    }

    Button btn(String t, String c) {
        Button b = new Button(t);
        String textColor = c.equals(C_VERT) || c.equals("#4CAF50") ? "#0D2418" : "white";
        b.setStyle(
            "-fx-background-color: " + c + ";" +
            "-fx-text-fill: " + textColor + ";" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 9 18;" +
            "-fx-background-radius: 14;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0.1, 0, 2);"
        );
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e -> b.setOpacity(1.0));
        return b;
    }

    TextField tfPlaceholder(String p) {
        TextField tf = new TextField();
        tf.setPromptText(p);
        tf.setPrefWidth(220);
        tf.setStyle(
            "-fx-background-color: #F0F5F1;" +
            "-fx-border-color: #C8DCC8;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 8 12;" +
            "-fx-font-size: 13;"
        );
        tf.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                tf.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                    "-fx-border-color: #4CAF50;" +
                    "-fx-border-radius: 10;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 12;" +
                    "-fx-font-size: 13;"
                );
            } else {
                tf.setStyle(
                    "-fx-background-color: #F0F5F1;" +
                    "-fx-border-color: #C8DCC8;" +
                    "-fx-border-radius: 10;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 12;" +
                    "-fx-font-size: 13;"
                );
            }
        });
        return tf;
    }

    ComboBox<String> combo(String... items) {
        return new ComboBox<>(FXCollections.observableArrayList(items));
    }

    Label msg() {
        Label l = new Label();
        l.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        return l;
    }

    Separator sep() { return new Separator(); }

    void styleTable(TableView<?> table) {
        table.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #C8DCC8;" +
            "-fx-border-radius: 14;" +
            "-fx-background-radius: 14;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    ScrollPane sidebar(VBox contenu) {
        contenu.setMinHeight(Region.USE_PREF_SIZE);
        contenu.setMaxWidth(Double.MAX_VALUE);
        ScrollPane scrollPane = new ScrollPane(contenu);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return scrollPane;
    }

    void setVue(VBox active, VBox... inactives) {
        active.setVisible(true);
        active.setManaged(true);
        for (VBox v : inactives) {
            v.setVisible(false);
            v.setManaged(false);
        }
    }

    HBox ligne(String label, javafx.scene.Node champ) {
        Label l = new Label(label);
        l.setMinWidth(120);
        HBox h = new HBox(8, l, champ);
        h.setAlignment(Pos.CENTER_LEFT);
        return h;
    }

    <T> TableColumn<T, String> col(String titre,
            java.util.function.Function<T,String> fn) {
        TableColumn<T,String> c = new TableColumn<>(titre);
        c.setCellValueFactory(d -> new SimpleStringProperty(fn.apply(d.getValue())));
        c.setPrefWidth(150);
        return c;
    }

    TableColumn<Alerte,String> colAlerte(String titre,
            java.util.function.Function<Alerte,String> fn) {
        TableColumn<Alerte,String> c = new TableColumn<>(titre);
        c.setCellValueFactory(d -> new SimpleStringProperty(fn.apply(d.getValue())));
        c.setPrefWidth(150);
        return c;
    }

    void colorerNiveau(TableColumn<String[],String> col) {
        col.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) { setText(null); setStyle(""); }
                else {
                    setText(item);
                    setStyle(item.equals("CRITIQUE") ?
                        "-fx-background-color:#FADBD8;-fx-text-fill:#C0392B;-fx-font-weight:bold;" :
                        item.equals("AVERTISSEMENT") ?
                        "-fx-background-color:#FDEBD0;-fx-text-fill:#E67E22;-fx-font-weight:bold;" :
                        "-fx-background-color:#D5F5E3;-fx-text-fill:#27AE60;-fx-font-weight:bold;");
                }
            }
        });
    }

    void colorerNiveauAlerte(TableColumn<String[],String> col) {
        colorerNiveau(col);
    }

    void colorerStatutCapteur(TableView<String[]> table) {
        // colorer la 4ème colonne (statut) après ajout
    }

    TableView<Zone> tableZones() {
        TableView<Zone> t = new TableView<>();
        t.getColumns().addAll(
            colZone("Code",   z -> String.valueOf(z.getCode())),
            colZone("Nom",    z -> z.getNom()),
            colZone("Type",   z -> z.getClass().getSimpleName()),
            colZone("Statut", z -> z.getStatut().toString()),
            colZone("Capteurs", z -> String.valueOf(z.getCapteurs().size())),
            colZone("Dernière Prod", z -> resumeDerniereProd(z)),
            colActionsZone()
        );
        t.setPrefHeight(350);
        return t;
    }

    TableColumn<Zone, Void> colActionsZone() {
        TableColumn<Zone, Void> col = new TableColumn<>("Actions");
        col.setPrefWidth(260);
        col.setCellFactory(tc -> new TableCell<>() {
            private final Button btnCulture = btn("Culture", C_VERT);
            private final Button btnAnimal  = btn("Animal", C_ORANGE);
            private final Button btnAquacole = btn("Aquacole", C_BLEU);
            private final HBox box = new HBox(8, btnCulture, btnAnimal, btnAquacole);
            {
                box.setAlignment(Pos.CENTER);
                btnCulture.setOnAction(e -> {
                    Zone zone = getTableView().getItems().get(getIndex());
                    ouvrirAffectationCulture(zone);
                });
                btnAnimal.setOnAction(e -> {
                    Zone zone = getTableView().getItems().get(getIndex());
                    ouvrirAffectationAnimal(zone);
                });
                btnAquacole.setOnAction(e -> {
                    Zone zone = getTableView().getItems().get(getIndex());
                    ouvrirAquacole(zone);
                });
            }

            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                Zone zone = getTableView().getItems().get(getIndex());
                btnCulture.setVisible(zone instanceof ZoneCulture);
                btnCulture.setManaged(zone instanceof ZoneCulture);
                btnAnimal.setVisible(zone instanceof ZoneElevage);
                btnAnimal.setManaged(zone instanceof ZoneElevage);
                btnAquacole.setVisible(zone instanceof ZoneAquacole);
                btnAquacole.setManaged(zone instanceof ZoneAquacole);
                setGraphic(box);
            }
        });
        return col;
    }

    TableColumn<Zone,String> colZone(String titre,
            java.util.function.Function<Zone,String> fn) {
        TableColumn<Zone,String> c = new TableColumn<>(titre);
        c.setCellValueFactory(d -> new SimpleStringProperty(fn.apply(d.getValue())));
        c.setPrefWidth(150);
        return c;
    }

    void rafraichirZones(TableView<Zone> table) {
        ObservableList<Zone> data = FXCollections.observableArrayList();
        for (Zone z : g.getZones()) data.add(z);
        table.setItems(data);
        table.refresh();
    }

    String resumeDerniereProd(Zone z) {
        if (z.getHistorique() == null || z.getHistorique().isEmpty()) return "-";
        ProductionRecord p = z.getHistorique().get(z.getHistorique().size()-1);
        return p.getDate() + " | " + p.getType() + " | " + p.getValeur() + " " + p.getUnite().getSymbole();
    }

    ObservableList<String> zonesLabels(boolean inclureToutes) {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (inclureToutes) items.add("Toutes");
        for (Zone z : g.getZones()) items.add(zoneLabel(z));
        return items;
    }

    String zoneLabel(Zone z) {
        return z == null ? "" : z.getCode() + " - " + z.getNom();
    }

    List<TypeProduction> typesProductionPourZone(Zone zone) {
        if (zone instanceof ZoneCulture) {
            return Arrays.asList(TypeProduction.RENDEMENT);
        }
        if (zone instanceof ZoneAquacole) {
            return Arrays.asList(TypeProduction.POIDS_RECOLTE);
        }
        if (zone instanceof ZoneElevage) {
            if (((ZoneElevage) zone).getTypeElevage() == TypeElevage.VOLAILLE) {
                return Arrays.asList(TypeProduction.OEUFS);
            }
            return Arrays.asList(TypeProduction.LAITIER);
        }
        return Arrays.asList(TypeProduction.values());
    }

    String messageTypesProduction(Zone zone) {
        if (zone == null) return "Entrez un code zone valide pour afficher les types autorisés.";
        if (zone instanceof ZoneCulture) return "Zone culture : seul RENDEMENT est autorisé.";
        if (zone instanceof ZoneAquacole) return "Zone aquacole : seul POIDS_RECOLTE est autorisé.";
        if (zone instanceof ZoneElevage) {
            if (((ZoneElevage) zone).getTypeElevage() == TypeElevage.VOLAILLE) {
                return "Zone élevage volaille : seul OEUFS est autorisé.";
            }
            return "Zone élevage ruminant : seul LAITIER est autorisé.";
        }
        return "Types autorisés selon la zone.";
    }

    Zone trouverZoneParLibelle(String libelle) {
        if (libelle == null) return null;
        for (Zone z : g.getZones()) {
            if (libelle.equals(zoneLabel(z))) return z;
        }
        return null;
    }

    void ouvrirAffectationCulture(Zone zone) {
        if (!(zone instanceof ZoneCulture)) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Cette zone n'est pas une zone de culture.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        if (cbZoneCultureField != null) cbZoneCultureField.setValue(zone);
        showPage(pageCultures);
    }

    void ouvrirAffectationAnimal(Zone zone) {
        if (!(zone instanceof ZoneElevage)) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Cette zone n'est pas une zone d'élevage.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        if (cbZoneElevageField != null) cbZoneElevageField.setValue(zone);
        showPage(pageAnimaux);
    }

    void ouvrirAquacole(Zone zone) {
        if (!(zone instanceof ZoneAquacole)) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Cette zone n'est pas une zone aquacole.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        showPage(pageAquacole);
    }

    void remplirComboCapteurs(ComboBox<String> cb) {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Zone z : g.getZones())
            for (Capteur c : z.getCapteurs())
                items.add(c.getCode() + " [" + c.getClass().getSimpleName() + "]");
        cb.setItems(items);
        if (!items.isEmpty()) cb.setValue(items.get(0));
    }

    void remplirUnitesPourCapteur(ComboBox<String> cbUnite, Capteur capteur) {
        ObservableList<String> items = FXCollections.observableArrayList();
        if (capteur instanceof CapteurSol) {
            items.addAll(UniteMesure.PH.getSymbole(), UniteMesure.POURCENTAGE.getSymbole(), UniteMesure.MILLIGRAMME_PAR_LITRE.getSymbole());
        } else if (capteur instanceof CapteurEnvironnemental) {
            items.addAll(UniteMesure.CELSIUS.getSymbole(), UniteMesure.POURCENTAGE.getSymbole(), UniteMesure.MILLIMETRE.getSymbole());
        } else if (capteur instanceof CapteurBiometrique) {
            items.addAll(UniteMesure.CELSIUS.getSymbole(), UniteMesure.BATTEMENT_PAR_MINUTE.getSymbole());
        } else if (capteur instanceof CapteurEau) {
            items.addAll(UniteMesure.CELSIUS.getSymbole(), UniteMesure.MILLIGRAMME_PAR_LITRE.getSymbole());
        }
        cbUnite.setItems(items);
        if (!items.isEmpty()) {
            cbUnite.setValue(items.get(0));
        }
    }

    void rafraichirMenusCapteurs() {
        for (ComboBox<String> cb : capteurComboBoxes) {
            if (cb != null) {
                remplirComboCapteurs(cb);
            }
        }
    }

    void rafraichirTableauxCapteurs() {
        for (Runnable refresh : capteurTableRefreshers) {
            refresh.run();
        }
    }

    Capteur trouverCapteur(String code) {
        for (Zone z : g.getZones())
            for (Capteur c : z.getCapteurs())
                if (c.getCode().equals(code)) return c;
        return null;
    }

    Capteur trouverCapteurParLabel(String label) {
        if (label == null) return null;
        return trouverCapteur(label.split(" ")[0]);
    }

    public static void main(String[] args) { launch(args); }
}