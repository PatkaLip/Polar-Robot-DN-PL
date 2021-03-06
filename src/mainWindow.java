import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.util.Vector;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.sound.sampled.*;

public class mainWindow extends JFrame implements KeyListener {

    BranchGroup     wezel_scena, scena;
    BoundingSphere  bounds ;
    Transform3D     pozycja_koncowki,p_podstawy, p_cylindra, p_cylindra2, p_cylindra3, p_chwytaka, p_robota, p_sroba1, p_sroba2, obrot_laczika_p,obrot_laczika_t, obrot_podstawy_p, obrot_podstawy_t, p_przyssawka;
    TransformGroup  robot, podstawka, czesc_pierwsza, czesc_druga, czesc_trzecia, chwytak,sroba_1, sroba_2, przyssawka, koncowka;
    Box[] klocki = new Box[2];
    Transform3D[] p_klocka = new Transform3D[2];
    TransformGroup[] _klocki = new TransformGroup[2];
    Vector3f[] polozenie_klockow = new Vector3f[2];
    Appearance wyglad_klockow;
    Vector<Integer> kolejka = new Vector<Integer>();
    Sphere koncowka_;
    
    
    int zadanie;
        
    Vector3f pozycja_sroba1     = new Vector3f(0.0f,-0.25f,0.0f);
    Vector3f pozycja_sroba2     = new Vector3f(0.0f,-0.5f,0.0f);
    Vector3f pozycja_podstawy   = new Vector3f(0.0f,-0.5f,0.0f);
    Vector3f pozycja_cylindra   = new Vector3f(0.0f,-0.0f,0.0f);
    Vector3f pozycja_cylindra2  = new Vector3f(0.0f,0.5f, 0.0f);
    Vector3f pozyjcja_cylindra3 = new Vector3f(0.0f, 0.0f,0.0f);
    Vector3f pozycja_robota     = new Vector3f(0.0f,0.0f, 0.0f); 
    Vector3f pozycja_chwytaka   = new Vector3f(-0.15f,0.45f,0.0f);
    Vector3f pozycja_przyssawki = new Vector3f(0f,0.04f,0f);
    float chwytak_X = -0.15f;
    float czesc_trzecia_Y = 0;
    float krok = 0.02f;
    
    
    void Reset()
    {
        p_sroba1.set(pozycja_sroba1);
        p_sroba2.set(pozycja_sroba2);
        p_podstawy.set(pozycja_podstawy);
        p_cylindra.set(pozycja_cylindra);
        p_cylindra2.set(pozycja_cylindra2);
        p_cylindra3.set(pozyjcja_cylindra3);
        p_robota.set(pozycja_robota);
        p_chwytaka.set(pozycja_chwytaka);
        p_przyssawka.set(pozycja_przyssawki);
        
        p_cylindra2.mul(tmp_rot_Z_90); 
        p_chwytaka.mul( tmp_rot_Z_90);
        
        chwytak.setTransform(p_chwytaka);
        czesc_trzecia.setTransform(p_cylindra3); 
        czesc_druga.setTransform(p_cylindra2);
        przyssawka.setTransform(p_przyssawka);
        czesc_pierwsza.setTransform(p_cylindra);
        
        losowanie_polozen_klockow();
                    p_klocka[0]= new Transform3D();
            p_klocka[0].set(polozenie_klockow[0]); 
        _klocki[0].setTransform(p_klocka[0]);
        
    }
    
    boolean kolizja_ = false;
    boolean czy_wziete = false;
    boolean czy_blisko = false;
    boolean kolizja = false;    
    
    
    Transform3D tmp_rot_Z_90  = new Transform3D();
    Transform3D tmp_rot_Z_270 = new Transform3D();
    Transform3D tmp_rot_X_90  = new Transform3D();
    Transform3D tmp_rot_X_270 = new Transform3D();
    
    Transform3D temporary1 = new Transform3D();
    Transform3D temporary2 = new Transform3D();
    TextureLoader loader_tekstura = new TextureLoader("img/metal.jpg",null);
    TextureLoader loader_niebo = new TextureLoader("img/back2.jpg",null); 
    TextureLoader loader_stol = new TextureLoader("img/panele.jpg",null);
   
    
    private final String clink = "sound/clink.wav";
       
    
    mainWindow(){       
        super("Polar Robot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
                   
        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(1200,700));
        canvas3D.addKeyListener(this);
        
        add(canvas3D);
        pack();
        setVisible(true);
        BranchGroup scena = utworzScene();
	scena.compile();
                
        //Obsługa klawiatury
        this.addKeyListener(this);

        
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
        
        //Ustawienie obserwatora
        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.5f,4.0f));
        Transform3D obrot_obserwatora = new Transform3D();
        obrot_obserwatora.rotX(-Math.PI/30);
        przesuniecie_obserwatora.mul(new Transform3D());
        przesuniecie_obserwatora.mul(obrot_obserwatora);
        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        
        //Obsługa myszki
        OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ROTATE);
        orbit.setSchedulingBounds(new BoundingSphere());
	orbit.setRotYFactor(0);
	orbit.setMinRadius(Math.PI);
	orbit.setBounds(new BoundingSphere(new Point3d(0.0d, 0.0d, 0.0d), 20d));
                     
        
        simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);
        simpleU.addBranchGraph(scena);   
    }
    
    BranchGroup utworzScene(){

        wezel_scena = new BranchGroup();
        bounds =  new BoundingSphere(new Point3d(0, 0, 0), 5);
 
        swiatla();
        robot();
        kolizja();
        tlo();
        losowanie_polozen_klockow();
        klocki();
       
        
       return wezel_scena;
   }
    
    public void losowanie_polozen_klockow(){
        Random rand = new Random();
        Random rand2 = new Random();
        
        Random rand3 = new Random();
        Random rand4 = new Random();
        
        int x1 = Math.abs(rand.nextInt(4));
        int x2 = Math.abs(rand2.nextInt(4));
        
        float x1f = x1*0.1f;
        float x2f = x2*0.1f;
                
        int z1=Math.abs(rand3.nextInt(4));
        int z2=Math.abs(rand4.nextInt(4));
        
        float z1f = z1*0.1f;
        float z2f = z2*0.1f;
        
        if(x1f>-0.1f && x1f<0.1f){
            x1f=x1f+0.3f;
        }
        
        polozenie_klockow[0] = new Vector3f(-1*x1f, -0.42f, z1f);
        polozenie_klockow[1] = polozenie_klockow[0];
        
    }
    
    public void klocki(){
        
        
        wyglad_klockow = new Appearance();
        Material material = new Material(new Color3f(0.8f, 0.9f,0.8f), new Color3f(0.1f,0.2f,0.3f),
                                             new Color3f(0.2f, 0.9f, 0.0f), new Color3f(1.0f, 0.8f, 1.0f), 80.0f);
        wyglad_klockow.setMaterial(material);
        
        for(int i=0; i<1; i++){
            klocki[i] = new Box(0.1f, 0.1f, 0.1f, Box.ALLOW_CHILDREN_READ + Box.ALLOW_PARENT_READ, wyglad_klockow);
            p_klocka[i]= new Transform3D();
            p_klocka[i].set(polozenie_klockow[i]);       
            _klocki[i] = new TransformGroup(p_klocka[i]);
            _klocki[i].addChild(klocki[i]);
            _klocki[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            if(!czy_wziete){
               wezel_scena.addChild(_klocki[i]);  
            }  
        }        
    }
    
    public void tlo(){
        
        ImageComponent2D image_niebo = loader_niebo.getImage();

                  
        ImageComponent2D image_stol = loader_stol.getImage();
        
        
        Texture2D niebo = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image_niebo.getWidth(), image_niebo.getHeight());

        niebo.setImage(0, image_niebo);
        niebo.setBoundaryModeS(Texture.WRAP);
        niebo.setBoundaryModeT(Texture.WRAP);
        Appearance wyglad_tla = new Appearance();
        wyglad_tla.setTexture(niebo);
        Sphere sfera = new Sphere(5f,Sphere.GENERATE_NORMALS_INWARD| Cylinder.GENERATE_TEXTURE_COORDS, wyglad_tla);
        wezel_scena.addChild(sfera);
        
        
        Texture2D stol = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image_stol.getWidth(), image_stol.getHeight());
        stol.setImage(0, image_stol);
        stol.setBoundaryModeS(Texture.WRAP);
        stol.setBoundaryModeT(Texture.WRAP);
        Appearance w_stolik = new Appearance();
        w_stolik.setTexture(stol);
        Box stolik = new Box(10, 0.4f, 10, w_stolik);
        Transform3D p_stolika = new Transform3D();
        p_stolika.set(new Vector3f(0.0f,-0.9f,0.0f));
        TransformGroup stoliki = new TransformGroup(p_stolika);
        stoliki.addChild(stolik);
        wezel_scena.addChild(stoliki);
    }
   
 public void swiatla(){
      AmbientLight lightA = new AmbientLight();
      lightA.setInfluencingBounds(bounds);
      wezel_scena.addChild(lightA);
      
      SpotLight lightB = new SpotLight(new Color3f(0.5f, 0.5f, 0.5f), new Point3f(3f, 4f, 4f), new Point3f(0.5f, 0.5f, 0.5f), new Vector3f(-2f,-1f,-3f), 20f, 20f);
      lightB.setInfluencingBounds(bounds);
      wezel_scena.addChild(lightB);
      
      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
      lightD.setColor(new Color3f(0f, 0f, 0f));
      wezel_scena.addChild(lightD);
   }

      
      
  public void robot(){
        
        tmp_rot_Z_90.rotZ(Math.PI/2);  // obrot o 90*
        tmp_rot_Z_270.rotZ(-Math.PI); //obrot o -90*
        tmp_rot_X_90.rotX(Math.PI/2);
        tmp_rot_X_270.rotX(Math.PI/2);        

        
        Appearance wygladPodstawy = new Appearance();
        wygladPodstawy.setColoringAttributes(new ColoringAttributes(0.9f,0.9f,0.8f,ColoringAttributes.NICEST));   
        
                  
        ImageComponent2D image_tekstura = loader_tekstura.getImage();
    
        
        Texture2D metal = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image_tekstura.getWidth(), image_tekstura.getHeight());

        metal.setImage(0, image_tekstura);
        metal.setBoundaryModeS(Texture.WRAP);
        metal.setBoundaryModeT(Texture.WRAP);
        
      
        Appearance  wygladCylindra = new Appearance();
        wygladCylindra.setTexture(metal);   

        p_podstawy    = new Transform3D();   
        p_cylindra    = new Transform3D();  
        p_cylindra2   = new Transform3D();  
        p_cylindra3   = new Transform3D();  
        p_chwytaka    = new Transform3D();  
        p_robota      = new Transform3D(); 
        p_sroba1      = new Transform3D();  
        p_sroba2      = new Transform3D();    
//SROBY 
        Cylinder sroba1 = new Cylinder(0.05f , 0.03f , wygladPodstawy);
        p_sroba1.set(pozycja_sroba1);
        sroba_1 = new TransformGroup(p_sroba1);
        sroba_1.addChild(sroba1);
        
        Cylinder sroba2 = new Cylinder(0.05f , 0.03f , wygladPodstawy);
        p_sroba2.set(pozycja_sroba2);
        sroba_2 = new TransformGroup(p_sroba2);
        sroba_2.addChild(sroba2);
//PODSTAWA
        Cylinder podstawa = new Cylinder(0.12f , 0.08f ,Cylinder.ALLOW_CHILDREN_READ + Cylinder.ALLOW_PARENT_READ , wygladPodstawy);
        p_podstawy.set(pozycja_podstawy);
        
        podstawka = new TransformGroup(p_podstawy);
        podstawka.addChild(podstawa);
//CZESC PIERWSZA ROBOTA
        Cylinder cylinder = new Cylinder(0.04f, 0.9f,Cylinder.ALLOW_CHILDREN_READ + Cylinder.ALLOW_PARENT_READ , wygladCylindra);
        p_cylindra.set(pozycja_cylindra);
        
        czesc_pierwsza = new TransformGroup(p_cylindra);
        czesc_pierwsza.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        czesc_pierwsza.addChild(cylinder);
//CZESC DRUGA ROBOTA
        Cylinder lacznik = new Cylinder(0.12f , 0.15f ,Cylinder.ALLOW_CHILDREN_READ + Cylinder.ALLOW_PARENT_READ , wygladPodstawy);
        TransformGroup ustawiam_lacznik = new TransformGroup(tmp_rot_X_90);
        ustawiam_lacznik.addChild(lacznik);
        p_cylindra2.set(pozycja_cylindra2);
        p_cylindra2.mul(tmp_rot_Z_90);             
        
        czesc_druga = new TransformGroup(p_cylindra2);
        czesc_druga.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        czesc_druga.addChild(ustawiam_lacznik); 
//CZESC TRZECIA ROBOTA        
        Cylinder cylinder3 = new Cylinder(0.04f, 1f, Cylinder.ALLOW_CHILDREN_READ + Cylinder.ALLOW_PARENT_READ , wygladCylindra);
        p_cylindra3.set(pozyjcja_cylindra3); 
        
        czesc_trzecia = new TransformGroup(p_cylindra3);
        czesc_trzecia.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        czesc_trzecia.addChild(cylinder3);
//chwytak
        Cylinder chwytak_ = new Cylinder(0.04f, 0.5f, wygladCylindra);
        p_chwytaka.set(pozycja_chwytaka);
        p_chwytaka.mul( tmp_rot_Z_90);

        chwytak = new TransformGroup(p_chwytaka);
        chwytak.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        chwytak.addChild(chwytak_);
        
///////////////////      
        koncowka_ = new Sphere(0.05f, wygladPodstawy);
        Transform3D p_koncowka = new Transform3D();
        p_koncowka.set(new Vector3f(0f,0.25f,0f));
        
        koncowka = new TransformGroup(p_koncowka);
        koncowka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        koncowka.addChild(koncowka_);
        chwytak.addChild(koncowka);
        
        Cylinder przyssawka_ = new Cylinder(0.1f, 0.02f, wygladPodstawy);
        p_przyssawka = new Transform3D();
        p_przyssawka.set(pozycja_przyssawki);
        
        przyssawka = new TransformGroup(p_przyssawka);
        przyssawka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        przyssawka.addChild(przyssawka_);
        koncowka.addChild(przyssawka);          
                
 ////////////////////       
        
        
// Składanie robota        
        TransformGroup pomocniczy = new TransformGroup();
        TransformGroup pomocniczy2 = new TransformGroup();
        p_robota.set(pozycja_robota);
      
        robot = new TransformGroup(p_robota);
       
        chwytak.addChild(sroba_1);
        czesc_trzecia.addChild(chwytak);
        czesc_trzecia.addChild(sroba_2);       
        czesc_druga.addChild(czesc_trzecia);
        pomocniczy.addChild(czesc_druga);
        robot.addChild(czesc_pierwsza);
        czesc_pierwsza.addChild(pomocniczy);
        pomocniczy2.addChild(robot);
        
        
        wezel_scena.addChild(podstawka);
        wezel_scena.addChild(pomocniczy2);  
        
        obrot_laczika_p = new Transform3D();
        obrot_laczika_p.rotZ(-Math.PI/50);        
        
        obrot_laczika_t = new Transform3D();
        obrot_laczika_t.rotZ(Math.PI/50);
        
        obrot_podstawy_p = new Transform3D();
        obrot_podstawy_p.rotY(Math.PI/50);
        
        obrot_podstawy_t = new Transform3D();
        obrot_podstawy_t.rotY(-Math.PI/50);

   }
  
      private void dzwiek(String txt) {
       try{
            Clip sound = AudioSystem.getClip();
            sound.open(AudioSystem.getAudioInputStream(new File(txt)));
            sound.start();
        }
        catch (IOException | LineUnavailableException | UnsupportedAudioFileException exc){
            exc.printStackTrace(System.out);
        }
    }
  
  public void odwzorowanie_wektora() 
  {
      int i=0;
      int j;
    while( i<kolejka.size() )
    {
       j=kolejka.elementAt(i); 
        System.out.println(j);
       switch(j){
        case 0:
        {
            chwytak_X += krok;
            p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
            chwytak.setTransform(p_chwytaka);
            if(czy_wziete){
                podniesienie_klocka();
            }
        }
            break;
        case 1:
        {
            chwytak_X -= krok;
            p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
            chwytak.setTransform(p_chwytaka);
            if(czy_wziete){
                podniesienie_klocka();
            }            
        }
            break;
        case 2:
        {
            czesc_trzecia_Y -= krok;
            p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
            czesc_trzecia.setTransform(p_cylindra3);
            if(czy_wziete){
                podniesienie_klocka();
            }
        }
            break;
        case 3:
        {
            czesc_trzecia_Y += krok;
            p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
            czesc_trzecia.setTransform(p_cylindra3);
            if(czy_wziete){
                podniesienie_klocka();
            }
        }
            break;
        case 4:
        {
             p_cylindra2.mul(obrot_laczika_p);
            czesc_druga.setTransform(p_cylindra2);
            
            p_przyssawka.mul(obrot_laczika_t);
            przyssawka.setTransform(p_przyssawka);
            if(czy_wziete){
                podniesienie_klocka();
            }
        }
            break;
        case 5:
        {
            p_cylindra2.mul(obrot_laczika_t);
            czesc_druga.setTransform(p_cylindra2);
            
            p_przyssawka.mul(obrot_laczika_p);
            przyssawka.setTransform(p_przyssawka); 
            if(czy_wziete){
                podniesienie_klocka();
            }
        }
            break;
        case 6:
        {
            p_cylindra.mul(obrot_podstawy_p);
            czesc_pierwsza.setTransform(p_cylindra);
            if(czy_wziete){
                podniesienie_klocka();
            }
        }
            break;
        case 7:
        {
            p_cylindra.mul(obrot_podstawy_t);
            czesc_pierwsza.setTransform(p_cylindra);
            if(czy_wziete){
                podniesienie_klocka();
            }
        }
        break;
        case 8:
        {
            czy_wziete=true;
            podniesienie_klocka();
        }
        break;
        case 9:
        {
            czy_wziete=false;
            uposc_klocek();
        }
        break;
    }
       try
        {
            Thread.sleep(100); 
        }
        catch(InterruptedException e)
        {

        }
       i++;
    }
  }
   
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            
        case KeyEvent.VK_UP: 
            
            
            if(chwytak_X<0.15){
                chwytak_X += krok;
                p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
                chwytak.setTransform(p_chwytaka);
                kolejka.add(0);
                //System.out.println(p_sroba1);
            }
            else if(chwytak_X>0.13)
            {
                dzwiek(clink);
            }
            if(czy_wziete){
                podniesienie_klocka();
            }
            kolizja();
            break;
        
        case KeyEvent.VK_DOWN:
            
            if (!kolizja() && !kolizja2() && chwytak_X>-0.2){
                chwytak_X -= krok;
                p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
                chwytak.setTransform(p_chwytaka); 
                kolejka.add(1);
            }
            else if(chwytak_X<-0)
            {
                dzwiek(clink);
            }
            if(czy_wziete){
                podniesienie_klocka();
            }
            
            kolizja();
            break;
            
        case KeyEvent.VK_RIGHT:
            if(!kolizja() && !kolizja2()){//(czesc_trzecia_Y>-0.25){
                czesc_trzecia_Y -= krok;
                p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
                czesc_trzecia.setTransform(p_cylindra3); 
                kolejka.add(2);
            }
            else if(czesc_trzecia_Y<-0.23)
            {
                dzwiek(clink);
            }
            if(czy_wziete){
                podniesienie_klocka();
            }
            break;

        case KeyEvent.VK_LEFT: 
            if(czesc_trzecia_Y<0.35){
                czesc_trzecia_Y += krok;
                p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
                czesc_trzecia.setTransform(p_cylindra3);   
                kolejka.add(3);
            } 
            else if(czesc_trzecia_Y>0.33)
            {
                dzwiek(clink);
            }            
            if(czy_wziete){
                podniesienie_klocka();
            }
            
            kolizja();
            break;
        case KeyEvent.VK_W:
            Vector3f vek  = new Vector3f();     
            p_cylindra2.get(vek);
            
            if(!kolizja3()){
                p_cylindra2.mul(obrot_laczika_p);
                czesc_druga.setTransform(p_cylindra2);

                p_przyssawka.mul(obrot_laczika_t);
                przyssawka.setTransform(p_przyssawka);
                kolejka.add(4);
                if(czy_wziete){
                    podniesienie_klocka();
                }
            
            }
            
            kolizja();
            break;
        case KeyEvent.VK_S:

            if(!kolizja() && !kolizja2())
            {
            p_cylindra2.mul(obrot_laczika_t);
            czesc_druga.setTransform(p_cylindra2);
            
            p_przyssawka.mul(obrot_laczika_p);
            przyssawka.setTransform(p_przyssawka); 
            kolejka.add(5);
            }
            if(czy_wziete){
                podniesienie_klocka();
            }

            break;
        case KeyEvent.VK_A:
            p_cylindra.mul(obrot_podstawy_p);
            czesc_pierwsza.setTransform(p_cylindra);
            kolejka.add(6);
            if(czy_wziete){
                podniesienie_klocka();
            }
            break;
        case KeyEvent.VK_D:
            p_cylindra.mul(obrot_podstawy_t);
            czesc_pierwsza.setTransform(p_cylindra);
            kolejka.add(7);
            if(czy_wziete){
                podniesienie_klocka();
            }
            break;    
        case KeyEvent.VK_R:
            chwytak_X=0;
            czesc_trzecia_Y=0;
            p_klocka[0].set(polozenie_klockow[1]);
            p_cylindra2.set(new Vector3f(0.0f,0.5f, 0.0f));
            p_cylindra2.mul(tmp_rot_Z_90);
            p_cylindra.set(new Vector3f(0.0f,-0.0f,0.0f));
            p_przyssawka.set(new Vector3f(0f,0.04f,0f));
            
            czesc_trzecia.setTransform(p_cylindra3); 
            czesc_druga.setTransform(p_cylindra2);
            przyssawka.setTransform(p_przyssawka);
            czesc_pierwsza.setTransform(p_cylindra);
            _klocki[0].setTransform(p_klocka[0]);
            odwzorowanie_wektora();
            break;

        case KeyEvent.VK_P:
            if(czy_moge_podniesc())
            {
                czy_wziete=true;
                podniesienie_klocka();
                kolejka.add(8);
                
            }
            else dzwiek(clink); 
            
         break;   
                   
        
        case KeyEvent.VK_O:
            czy_wziete=false;
            uposc_klocek();
            kolejka.add(9);
            
         break;   
         
        case KeyEvent.VK_Z:
            
            Reset();
        break;
        case KeyEvent.VK_M:      
            kolizja3();
        break;
        
                   
        } 
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) { 
    }
    
    public boolean kolizja(){
        
        pozycja_koncowki = new Transform3D();

        float a1, a3;
        Vector3f aa = new Vector3f(0, 0, 0);
        przyssawka.getLocalToVworld(pozycja_koncowki) ;
        pozycja_koncowki.get(aa);
        a1 = aa.x*1000000000;
        a3 = aa.z*1000000000;
        
        float r ;
        r = (float) sqrt(a1*a1 + a3*a3);
        
        if(r < 1.90000032E8){
            dzwiek(clink);
            return true;
        }
       
        return false;
    }
        public boolean kolizja2(){
        
        pozycja_koncowki = new Transform3D();

        float a2;
        Vector3f aa = new Vector3f(0, 0, 0);
        przyssawka.getLocalToVworld(pozycja_koncowki) ;
        pozycja_koncowki.get(aa);
        a2 = aa.y;   

        if(a2 < -0.2628777){
            return true;
        }
       
        return false;
    }
        
        public boolean kolizja3(){
        
        pozycja_koncowki = new Transform3D();

        float a2;
        Vector3f aa = new Vector3f(0, 0, 0);
        przyssawka.getLocalToVworld(pozycja_koncowki) ;
        pozycja_koncowki.get(aa);
        a2 = aa.y;   
        
            System.out.println(a2);

        if(a2 > 0.62894356){
            return true;
        }
        return false;
    }
        
    public boolean czy_moge_podniesc(){
        pozycja_koncowki = new Transform3D();
        Vector3f aa = new Vector3f();
        przyssawka.getLocalToVworld(pozycja_koncowki) ;
        pozycja_koncowki.get(aa);
        float odlegloscX = abs(polozenie_klockow[0].x - aa.x);
        float odlegloscY = abs(polozenie_klockow[0].y - aa.y);
        float odlegloscZ = abs(polozenie_klockow[0].z - aa.z);
        if(odlegloscX<0.1f && odlegloscY<0.2f && odlegloscZ<0.2f)
        return true;
        return false;
        
    }
    
    void podniesienie_klocka ()
    {
        pozycja_koncowki = new Transform3D();
            przyssawka.getLocalToVworld(pozycja_koncowki);
            //System.out.println(pozycja_koncowki); //p_przyssawka.get(pozycja_przyssawki)

            
            p_klocka[0]= pozycja_koncowki;
            
            Transform3D cos = new Transform3D();
            cos.set(new Vector3f(-0.1f, 0f, 0f));
            
            Vector3f cosik = new Vector3f();
            p_klocka[0].get(cosik);
            cosik.y= cosik.y-0.15f;
            p_klocka[0].set(cosik);
            _klocki[0].setTransform(p_klocka[0]);
    }
    void uposc_klocek()
    {
      Vector3f cosik = new Vector3f();
      p_klocka[0].get(cosik);
      for(int i=0 ; i<(cosik.y+0.43f)*100f; i++){
          cosik.y = cosik.y-0.01f;
          p_klocka[0].set(cosik);
          _klocki[0].setTransform(p_klocka[0]);
         try
        {
            Thread.sleep(10); 
        }
        catch(InterruptedException e)
        {
        }       
      }
      
      
    }
    
   

   public static void main(String args[]){
      new mainWindow();   
   }   
}


