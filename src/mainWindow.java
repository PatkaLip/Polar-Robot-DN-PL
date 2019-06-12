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



public class mainWindow extends JFrame implements KeyListener {

    BranchGroup     wezel_scena, scena;
    BoundingSphere  bounds ;
    Transform3D     p_podstawy, p_cylindra, p_cylindra2, p_cylindra3, p_chwytaka, p_robota, p_sroba1, p_sroba2, obrot_laczika_p,obrot_laczika_t, obrot_podstawy_p, obrot_podstawy_t;
    TransformGroup  obrot_animacja_podstawa,obrot_animacja_gora, robot, podstawka, czesc_pierwsza, czesc_druga, czesc_trzecia, chwytak,sroba_1, sroba_2;
    RotationInterpolator obracacz, obracacz2;
    
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
        przesuniecie_obserwatora.set(new Vector3f(-0.0f,0.0f,6.0f));
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
       
       // obrot_A();
       // obrot_B();
        
       return wezel_scena;
   }
   
 public void swiatla(){
      AmbientLight lightA = new AmbientLight();
      lightA.setInfluencingBounds(bounds);
      wezel_scena.addChild(lightA);

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
      lightD.setColor(new Color3f(0f, 0f, 0f));
      wezel_scena.addChild(lightD);
   }

      
      
  public void robot(){
        
        obrot_animacja_gora = new TransformGroup();
        obrot_animacja_gora.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Alpha alpha_animacja = new Alpha(-1,2500); 
        obracacz2 = new RotationInterpolator(alpha_animacja, obrot_animacja_gora);
        obracacz2.setSchedulingBounds(bounds);
        obracacz2.setMaximumAngle(0.5f);
        obracacz2.setMinimumAngle(0f);

        
        obrot_animacja_podstawa = new TransformGroup();
        obrot_animacja_podstawa.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Alpha alpha_animacja2 = new Alpha(-1,25000); 
        obracacz = new RotationInterpolator(alpha_animacja2, obrot_animacja_podstawa);
        obracacz.setSchedulingBounds(bounds);

        
        tmp_rot_Z_90.rotZ(Math.PI/2);  // obrot o 90*
        tmp_rot_Z_270.rotZ(-Math.PI); //obrot o -90*
        tmp_rot_X_90.rotX(Math.PI/2);
        tmp_rot_X_270.rotX(Math.PI/2);        

        
        Appearance wygladPodstawy = new Appearance();
        wygladPodstawy.setColoringAttributes(new ColoringAttributes(0.9f,0.9f,0.8f,ColoringAttributes.NICEST));    
      
        Appearance  wygladCylindra = new Appearance();
        PolygonAttributes polygAttr = new PolygonAttributes();
        polygAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        wygladCylindra.setPolygonAttributes(polygAttr);     

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
// Składanie robota        
        TransformGroup pomocniczy = new TransformGroup();
        
        p_robota.set(pozycja_robota);
      
        robot = new TransformGroup(p_robota);
       
        chwytak.addChild(sroba_1);
        czesc_trzecia.addChild(chwytak);
        czesc_trzecia.addChild(sroba_2);       
        czesc_druga.addChild(czesc_trzecia);
        obrot_animacja_gora.addChild(czesc_druga);
        pomocniczy.addChild(obrot_animacja_gora);
        robot.addChild(czesc_pierwsza);
        czesc_pierwsza.addChild(pomocniczy);
        obrot_animacja_podstawa.addChild(robot);
        
        wezel_scena.addChild(podstawka);
        wezel_scena.addChild(obrot_animacja_podstawa);  
        
        obrot_laczika_p = new Transform3D();
        obrot_laczika_p.rotZ(Math.PI/20);
        
        obrot_laczika_t = new Transform3D();
        obrot_laczika_t.rotZ(-Math.PI/20);
        
        obrot_podstawy_p = new Transform3D();
        obrot_podstawy_p.rotY(Math.PI/20);
        
        obrot_podstawy_t = new Transform3D();
        obrot_podstawy_t.rotY(-Math.PI/20);
   }
     
   public void obrot_A(){
                    
        p_cylindra2.mul(tmp_rot_X_270);
        obracacz2.setTransformAxis(p_cylindra2);
        obrot_animacja_gora.addChild(obracacz2);
   } 

    
   public void obrot_B(){
       obracacz.setTransformAxis(p_robota);
       obrot_animacja_podstawa.addChild(obracacz);
   } 
   
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            
        case KeyEvent.VK_UP: 
            chwytak_X += krok;

            p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
            chwytak.setTransform(p_chwytaka);
            break;
        
        case KeyEvent.VK_DOWN:
            chwytak_X -= krok;

            p_chwytaka.setTranslation(new Vector3f(chwytak_X,0.45f,0.0f));
            chwytak.setTransform(p_chwytaka); 
            break;
            
        case KeyEvent.VK_RIGHT:
            System.out.println("bb"); 

            czesc_trzecia_Y -= krok;

            p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
            czesc_trzecia.setTransform(p_cylindra3);            
            
            
            break;

        case KeyEvent.VK_LEFT: 
            
            czesc_trzecia_Y += krok;

            p_cylindra3.setTranslation(new Vector3f(0, czesc_trzecia_Y, 0));
            czesc_trzecia.setTransform(p_cylindra3);            
                    
            break;
        case KeyEvent.VK_W:
            p_cylindra2.mul(obrot_laczika_p);
            czesc_druga.setTransform(p_cylindra2);
            break;
        case KeyEvent.VK_S:
            p_cylindra2.mul(obrot_laczika_t);
            czesc_druga.setTransform(p_cylindra2);
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


