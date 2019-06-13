import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector3d;

import java.util.Timer;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.TimerTask;
import javax.media.j3d.Background;



public class mainWindow extends JFrame implements KeyListener {

    BranchGroup     wezel_scena, scena;
    BoundingSphere  bounds ;
    Transform3D     p_podstawy, p_cylindra, p_cylindra2, p_cylindra3, p_chwytaka, p_robota, p_sroba1, p_sroba2, obrot_laczika_p,obrot_laczika_t, obrot_podstawy_p, obrot_podstawy_t, p_przyssawka;
    TransformGroup  robot, podstawka, czesc_pierwsza, czesc_druga, czesc_trzecia, chwytak,sroba_1, sroba_2, przyssawka;
    Box[] klocki = new Box[10];
    Transform3D[] p_klocka = new Transform3D[10];
    TransformGroup[] _klocki = new TransformGroup[10];
    Appearance wyglad_klockow;
    
    
    int zadanie;
        
    Vector3f pozycja_sroba1     = new Vector3f(0.0f,-0.25f,0.0f);
    Vector3f pozycja_sroba2     = new Vector3f(0.0f,-0.5f,0.0f);
    Vector3f pozycja_podstawy   = new Vector3f(0.0f,-0.5f,0.0f);
    Vector3f pozycja_cylindra   = new Vector3f(0.0f,-0.0f,0.0f);
    Vector3f pozycja_cylindra2  = new Vector3f(0.0f,0.5f, 0.0f);
    Vector3f pozyjcja_cylindra3 = new Vector3f(0.0f, 0.0f,0.0f);
    Vector3f pozycja_robota     = new Vector3f(0.0f,0.0f, 0.0f); 
    Vector3f pozycja_chwytaka   = new Vector3f(-0.15f,0.45f,0.0f);
    
    float chwytak_X = -0.15f;
    float czesc_trzecia_Y = 0;
    float krok = 0.02f;
    //double obrot =(double) Math.PI/20;
    
    Transform3D tmp_rot_Z_90  = new Transform3D();
    Transform3D tmp_rot_Z_270 = new Transform3D();
    Transform3D tmp_rot_X_90  = new Transform3D();
    Transform3D tmp_rot_X_270 = new Transform3D();
    TextureLoader loader_tekstura = new TextureLoader("img/metal.jpg",null);
    TextureLoader loader_niebo = new TextureLoader("img/back2.jpg",null); 
    TextureLoader loader_stol = new TextureLoader("img/panele.jpg",null);
   

    
    
    
    
    Timer zegar = new Timer();
    TimerTask zegar_ruchu = new TimerTask() {
	public void run() {
            
        }
    };       
    
    mainWindow(){       
        super("Polar Robot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        
        
        
        
        zegar = new Timer();
        zegar.schedule(zegar_ruchu, 0, 200);
                
        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(1200,700));
        
        
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
        przesuniecie_obserwatora.set(new Vector3f(-0.0f,0.0f,4.0f));
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
        tlo();
        klocki();
       
        
       return wezel_scena;
   }
    
    public void klocki(){
        
        wyglad_klockow = new Appearance();
        //wyglad_klockow.setColoringAttributes(new ColoringAttributes(1f,0.2f,0.2f,ColoringAttributes.NICEST)); 
        Material material = new Material(new Color3f(0.8f, 0.9f,0.8f), new Color3f(0.1f,0.2f,0.3f),
                                             new Color3f(0.2f, 0.9f, 0.0f), new Color3f(1.0f, 0.8f, 1.0f), 80.0f);
        wyglad_klockow.setMaterial(material);
        
        for(int i=0; i<10; i++){
            klocki[i] = new Box(0.1f, 0.1f, 0.1f, Box.ALLOW_CHILDREN_READ + Box.ALLOW_PARENT_READ, wyglad_klockow);
            p_klocka[i]= new Transform3D();
            p_klocka[i].set(new Vector3f(0+0.2f+i*0.3f, -0.43f, 0f));       /// tu trzeba użyć rand do losowaia pozycji!!! 
            _klocki[i] = new TransformGroup(p_klocka[i]);
            _klocki[i].addChild(klocki[i]);
            _klocki[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            wezel_scena.addChild(_klocki[i]);   
        }        
    }
    
    public void tlo (){
        
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
        //PolygonAttributes polygAttr = new PolygonAttributes();
        //polygAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        //wygladCylindra.setPolygonAttributes(polygAttr);     

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
     //   p_sroba1.mul(tmp_rot_Z_90);
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
        Sphere koncowka_ = new Sphere(0.05f, wygladPodstawy);
        Transform3D p_koncowka = new Transform3D();
        p_koncowka.set(new Vector3f(0f,0.25f,0f));
        
        TransformGroup koncowka = new TransformGroup(p_koncowka);
        koncowka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        koncowka.addChild(koncowka_);
        chwytak.addChild(koncowka);
        
        Cylinder przyssawka_ = new Cylinder(0.1f, 0.02f, wygladPodstawy);
        p_przyssawka = new Transform3D();
        p_przyssawka.set(new Vector3f(0f,0.04f,0f));
        
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
        obrot_laczika_p.rotZ(-Math.PI/30);        
        
        obrot_laczika_t = new Transform3D();
        obrot_laczika_t.rotZ(Math.PI/30);
        
        obrot_podstawy_p = new Transform3D();
        obrot_podstawy_p.rotY(Math.PI/30);
        
        obrot_podstawy_t = new Transform3D();
        obrot_podstawy_t.rotY(-Math.PI/30);
   }
  
   
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            
        case KeyEvent.VK_UP: 
            
            
            if(chwytak_X<0.15){
                chwytak_X += krok;
                p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
                chwytak.setTransform(p_chwytaka);
                //System.out.println(p_sroba1);
            }
            
            break;
        
        case KeyEvent.VK_DOWN:
            
            if(chwytak_X>-0.2){
                chwytak_X -= krok;
                p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
                chwytak.setTransform(p_chwytaka);  
            }
            
            
            break;
            
        case KeyEvent.VK_RIGHT:
            if(czesc_trzecia_Y>-0.25){
                czesc_trzecia_Y -= krok;
                p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
                czesc_trzecia.setTransform(p_cylindra3);           
            }
            break;

        case KeyEvent.VK_LEFT: 
            if(czesc_trzecia_Y<0.35){
                czesc_trzecia_Y += krok;
                p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
                czesc_trzecia.setTransform(p_cylindra3);            
            }       
            break;
        case KeyEvent.VK_W:
            p_cylindra2.mul(obrot_laczika_p);
            czesc_druga.setTransform(p_cylindra2);
            
            p_przyssawka.mul(obrot_laczika_t);
            przyssawka.setTransform(p_przyssawka);
            break;
        case KeyEvent.VK_S:
            p_cylindra2.mul(obrot_laczika_t);
            czesc_druga.setTransform(p_cylindra2);
            
            p_przyssawka.mul(obrot_laczika_p);
            przyssawka.setTransform(p_przyssawka);            
            break;
        case KeyEvent.VK_A:
            p_cylindra.mul(obrot_podstawy_p);
            czesc_pierwsza.setTransform(p_cylindra);
            break;
        case KeyEvent.VK_D:
            p_cylindra.mul(obrot_podstawy_t);
            czesc_pierwsza.setTransform(p_cylindra);
            break;    
            
        }   
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) { 
    }

   public static void main(String args[]){
      new mainWindow();   
   }   
}


