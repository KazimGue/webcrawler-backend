package de.awa.training.webcrawler.services;

import de.awa.training.webcrawler.entity.*;
import de.awa.training.webcrawler.model.Daten;
import de.awa.training.webcrawler.model.PreisDaten;
import de.awa.training.webcrawler.repository.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class AnfragenServiceTest {

    @Mock
    PLZRepository plzRepository;

    @Mock
    UnternehemensRepository unternehemensRepository;

    @Mock
    PreiseingabeUnternehmenRepository preiseingabeUnternehmenRepository;

    @InjectMocks
    NeuerAnfragenService neuerAnfragenService;

    ArrayList<UnternehmenEntity> gemockteUternehmen = new ArrayList<>();
    ArrayList<PreiseingabeUnternehmenEntity> gemockteListePreisliste = new ArrayList<>();
    List<PostleitzahlenEntity> plzliste = new ArrayList<>();

    @Before
    public void testDatenVorbereiten(){

        gemockteUternehmen.add(new UnternehmenEntity(1,"Propan Rheingas GmbH & Co. KG","Fischenicher Straße 23","50321","Brühl"));
        gemockteUternehmen.add(new UnternehmenEntity(2,"DFG Deutsche Flüssiggas GmbH","Stau 169","262122","Oldenburg"));
        gemockteUternehmen.add(new UnternehmenEntity(3,"TEGA - Technische Gase und Gasetechnik GmbH", "Blumenstraße 70","99092", "Erfurt"));

        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(1,"29","30","31",1,1));
        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(2,"32","33","34",1,1));
        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(3,"35","36","37",1,1));

        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(4,"39","40","41",1,2));
        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(5,"42","43","44",1,2));
        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(6,"45","46","47",1,2));

        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(7,"49","50","51",1,3));
        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(8,"52","53","54",1,3));
        gemockteListePreisliste.add(new PreiseingabeUnternehmenEntity(9,"55","56","57",1,3));

        plzliste.add(new PostleitzahlenEntity(1,"51570"));
        plzliste.add(new PostleitzahlenEntity(2,"52152"));
        plzliste.add(new PostleitzahlenEntity(3,"53804"));

    }

    @Test
    public void holePLZidTEST(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(plzRepository.findAll()).thenReturn(plzliste);
        Integer id = neuerAnfragenService.holePLZid("51570");
        Integer id2 = neuerAnfragenService.holePLZid("52152");
        Integer id3 = neuerAnfragenService.holePLZid("55555");
        Assert.assertEquals(Integer.valueOf(1),id);
        Assert.assertEquals(Integer.valueOf(2),id2);
        Assert.assertNull(null,id);
    }

    @Test
    public void tankgrößeInIndexumwandelnTEST(){
        MockitoAnnotations.initMocks(this);
        Integer kleinerTank=neuerAnfragenService.tankgrößeInIndexumwandeln("preis2700liter");
        Integer mittlererTank=neuerAnfragenService.tankgrößeInIndexumwandeln("preis4850liter");
        Integer tankIDnichtvorhanden=neuerAnfragenService.tankgrößeInIndexumwandeln("test");
        Assert.assertEquals(Integer.valueOf(0),kleinerTank);
        Assert.assertEquals(Integer.valueOf(1),mittlererTank);
        Assert.assertNull(tankIDnichtvorhanden);
    }

    @Test
    public void holePreisausEntitytabelleTEST(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(preiseingabeUnternehmenRepository.findAll()).thenReturn(gemockteListePreisliste);
        String ergebnis = neuerAnfragenService.holePreisausEntitytabelle(preiseingabeUnternehmenRepository,1,1,0);
        String ergebnis2 = neuerAnfragenService.holePreisausEntitytabelle(preiseingabeUnternehmenRepository,1,1,1);
        String ergebnis3 = neuerAnfragenService.holePreisausEntitytabelle(preiseingabeUnternehmenRepository,2,2,2);
        Assert.assertEquals(ergebnis,"35");
        Assert.assertEquals(ergebnis2,"36");
        Assert.assertEquals(ergebnis3,"Kein Preis gefunden");
    }


    @Test
    public void sammlePreiseTEST(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(preiseingabeUnternehmenRepository.findAll()).thenReturn(gemockteListePreisliste);
        Mockito.when(unternehemensRepository.findAll()).thenReturn(gemockteUternehmen);
        List<PreisDaten> erwarteteListe = neuerAnfragenService.sammlePreise(1,1);
        Assert.assertEquals(erwarteteListe.size(),3);
        Assert.assertEquals(erwarteteListe.get(0).toString(),new PreisDaten(1,"36").toString());
        Assert.assertEquals(erwarteteListe.get(1).toString(),new PreisDaten(2,"46").toString());
        Assert.assertEquals(erwarteteListe.get(2).toString(),new PreisDaten(3,"56").toString());
    }

    @Test
    public void unternehmensDatenGenerierenTEST(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(unternehemensRepository.findAll()).thenReturn(gemockteUternehmen);
        ArrayList<Daten> unternehemensListemitPreis = neuerAnfragenService.unternehmensDatenGenerieren();
        Daten daten = new Daten("1","Propan Rheingas GmbH & Co. KG","Fischenicher Straße 23","50321","Brühl");
        Daten daten1 = new Daten("3","TEGA - Technische Gase und Gasetechnik GmbH", "Blumenstraße 70","99092", "Erfurt");
        Assert.assertEquals(unternehemensListemitPreis.get(0).toString(),daten.toString());
        Assert.assertEquals(unternehemensListemitPreis.get(2).toString(),daten1.toString());

    }

    @Test
    public void preisUnternehmenZuweisenTEST(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(unternehemensRepository.findAll()).thenReturn(gemockteUternehmen);
        Mockito.when(preiseingabeUnternehmenRepository.findAll()).thenReturn(gemockteListePreisliste);
        List<Daten> testListe = neuerAnfragenService.preisUnternehmenZuweisen(neuerAnfragenService.sammlePreise(1,1));
        List<Daten> testListe2 = neuerAnfragenService.preisUnternehmenZuweisen(neuerAnfragenService.sammlePreise(1,0));
        Daten daten = new Daten();
        daten.setPreis("36");
        Assert.assertEquals(daten.getPreis(),testListe.get(0).getPreis());
        daten.setPreis("45");
        Assert.assertEquals(daten.getPreis(),testListe2.get(1).getPreis());
    }


    @Test
    public void sortiereListeTEST(){
      MockitoAnnotations.initMocks(this);
      Mockito.when(preiseingabeUnternehmenRepository.findAll()).thenReturn(gemockteListePreisliste);
      List<EntityInterface> sortierteListe = neuerAnfragenService.sortiereAbsteigend(preiseingabeUnternehmenRepository);
      Assert.assertEquals(sortierteListe.get(0).getId(),Integer.valueOf(9));
      Assert.assertEquals(sortierteListe.get(2).getId(),Integer.valueOf(7));
      Assert.assertEquals(sortierteListe.get(0).getPreis6400Liter(),"57");
    }

    @Test
    public void filtereUnternehmenOhnePreisTEST(){
        MockitoAnnotations.initMocks(this);
        Mockito.when(unternehemensRepository.findAll()).thenReturn(gemockteUternehmen);
        List<Daten>daten   = neuerAnfragenService.unternehmensDatenGenerieren();
        daten.get(0).setPreis("40");
        daten.get(1).setPreis("Kein Preis gefunden");
        daten.get(2).setPreis("30");
        daten = neuerAnfragenService.filtereUnternehmenOhnePreis(daten);
        Assert.assertEquals(daten.size(),2);
        Assert.assertEquals(daten.get(0).getPreis(),"40");
        Assert.assertEquals(daten.get(1).getPreis(),"30");
    }
}
