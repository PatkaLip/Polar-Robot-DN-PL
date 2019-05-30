import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;




public class mainWindow extends JFrame{
    public int jaka_akcja ; 
    BranchGroup wezel_scena, scena;
    BoundingSphere bounds ;
    TransformGroup obrot_animacja_podstawa,obrot_animacja_gora, robot, podstawka, czesc_pierwsza, czesc_druga, czesc_trzecia, chwytak,sroba_1, sroba_2;
    RotationInterpolator obracacz, obracacz2;
    Transform3D  p_podstawy, p_cylindra, p_cylindra2, p_cylindra3, p_chwytaka, p_robota, p_sroba1, p_sroba2, tmp_rot_Z_90, tmp_rot_Z_270,tmp_rot_X_90, tmp_rot_X_270;


            
    mainWindow(){
        
        super("Polar Robot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

    
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(1950,1000));
        add(canvas3D);
        pack();
        setVisible(true);
        BranchGroup scena = utworzScene();
	    scena.compile();
        
        
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(-0.0f,0.0f,6.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena); 
    }
    
 

    
    
   BranchGroup utworzScene(){

       wezel_scena = new BranchGroup();
       bounds =  new BoundingSphere(new Point3d(0, 0, 0), 5);
            
      //sterowanie();
      swiatla();
      robot();
          obrot_A();
          obrot_B();
     
      
      return wezel_scena;
   }
   
   void swiatla(){
      AmbientLight lightA = new AmbientLight();            //ŚWIATLA
      lightA.setInfluencingBounds(bounds);
      wezel_scena.addChild(lightA);

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
      lightD.setColor(new Color3f(1.0f, 1.0f, 1.0f));
      wezel_scena.addChild(lightD);

   }
      
      
      
      
      
   void robot(){
        
        obrot_animacja_gora = new TransformGroup();
        obrot_animacja_gora.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Alpha alpha_animacja = new Alpha(-1,5000); 
        obracacz2 = new RotationInterpolator(alpha_animacja, obrot_animacja_gora);
        obracacz2.setSchedulingBounds(bounds);
        
        obrot_animacja_podstawa = new TransformGroup();
        obrot_animacja_podstawa.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Alpha alpha_animacja2 = new Alpha(-1,25000); 
        obracacz = new RotationInterpolator(alpha_animacja2, obrot_animacja_podstawa);
        obracacz.setSchedulingBounds(bounds);
        
        
       
        Appearance wygladPodstawy = new Appearance();
        wygladPodstawy.setColoringAttributes(new ColoringAttributes(0.9f,0.9f,0.8f,ColoringAttributes.NICEST));    
      
        Appearance  wygladCylindra = new Appearance();
        PolygonAttributes polygAttr = new PolygonAttributes();
        polygAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        wygladCylindra.setPolygonAttributes(polygAttr);     

  tmp_rot_Z_90  = new Transform3D();         tmp_rot_Z_90.rotZ(Math.PI/2);  // obrot o 90*
  tmp_rot_Z_270 = new Transform3D();         tmp_rot_Z_270.rotZ(-Math.PI); //obrot o -90*
  tmp_rot_X_90  = new Transform3D();         tmp_rot_X_90.rotX(Math.PI/2);
  tmp_rot_X_270 = new Transform3D();         tmp_rot_X_270.rotX(Math.PI/2);
  p_podstawy    = new Transform3D();   
  p_cylindra    = new Transform3D();  
  p_cylindra2   = new Transform3D();  
  p_cylindra3   = new Transform3D();  
  p_chwytaka    = new Transform3D();  
  p_robota      = new Transform3D(); 
  p_sroba1      = new Transform3D();  
  p_sroba2      = new Transform3D();    
Cylinder sroba1 = new Cylinder(0.05f , 0.03f , wygladPodstawy);
p_sroba1.set(new Vector3f(0.1f,0.45f,0f));
p_sroba1.mul(tmp_rot_Z_90);
sroba_1 = new TransformGroup(p_sroba1);
sroba_1.addChild(sroba1);
Cylinder sroba2 = new Cylinder(0.05f , 0.03f , wygladPodstawy);
p_sroba2.set(new Vector3f(0.0f,-0.5f,0f));
sroba_2 = new TransformGroup(p_sroba2);
sroba_2.addChild(sroba2);
//PODSTAWA
        Cylinder podstawa = new Cylinder(0.12f , 0.08f , wygladPodstawy);
        p_podstawy.set(new Vector3f(0.0f,-0.5f,0f));
        
         podstawka = new TransformGroup(p_podstawy);
        podstawka.addChild(podstawa);
//CZESC PIERWSZA ROBOTA
        Cylinder cylinder = new Cylinder(0.04f, 0.9f, wygladCylindra);
        p_cylindra.set(new Vector3f(0.0f,-0.0f,0.0f));
        
         czesc_pierwsza = new TransformGroup(p_cylindra);
        czesc_pierwsza.addChild(cylinder);
//CZESC DRUGA ROBOTA
        Cylinder lacznik = new Cylinder(0.12f , 0.15f , wygladPodstawy);
        TransformGroup ustawiam_lacznik = new TransformGroup(tmp_rot_X_90);
        ustawiam_lacznik.addChild(lacznik);
        p_cylindra2.set(new Vector3f(-0.0f,0.5f,0.0f));
        p_cylindra2.mul(tmp_rot_Z_90);             
        
         czesc_druga = new TransformGroup(p_cylindra2);
         czesc_druga.addChild(ustawiam_lacznik); 
//CZESC TRZECIA ROBOTA
        Cylinder cylinder3 = new Cylinder(0.04f, 1f, wygladCylindra);
        p_cylindra3.set(new Vector3f(-0f,0.2f,0.0f));
        //p_cylindra3.mul( tmp_rot_Z_270);           //cofniecie obrotu o 90* (względem piprzedniego transformgrupa)
        
        czesc_trzecia = new TransformGroup(p_cylindra3);
        czesc_trzecia.addChild(cylinder3);
//chwytak
        Cylinder chwytak_ = new Cylinder(0.04f, 0.5f, wygladCylindra);
        p_chwytaka.set(new Vector3f(-0.15f,0.45f,0.0f));
        p_chwytaka.mul( tmp_rot_Z_90);

        chwytak = new TransformGroup(p_chwytaka);
        chwytak.addChild(chwytak_);
        
        TransformGroup pomocniczy = new TransformGroup();
        p_robota.set(new Vector3f(0f,0f,0.0f));
      
        robot = new TransformGroup(p_robota);
       
        czesc_trzecia.addChild(sroba_1);
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
      
   }
   
   void obrot_A(){
        p_cylindra2.mul(tmp_rot_X_90);
        obracacz2.setTransformAxis(p_cylindra2);
        obrot_animacja_gora.addChild(obracacz2);
   }
   
   void obrot_B(){
       obracacz.setTransformAxis(p_robota);
       obrot_animacja_podstawa.addChild(obracacz);
   }
   
 
   

   
   

   public static void main(String args[]){
      new mainWindow();
      
        
            
    }
}


