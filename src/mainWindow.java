import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.*;
import javax.swing.*;
import java.awt.*;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.Transform3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;



public class mainWindow extends JFrame{

    mainWindow(){
        
        super("Polar Robot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

    
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(1950,900));

        add(canvas3D);
        pack();
        setVisible(true);

        BranchGroup scena = utworzScene();
	    scena.compile();

        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(-0.0f,0.0f,4.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena);

    }

   BranchGroup utworzScene(){

      BranchGroup wezel_scena = new BranchGroup();


      

      BoundingSphere bounds =  new BoundingSphere(new Point3d(0, 0, 0), 5);
      TransformGroup obrot_animacja = new TransformGroup();
      obrot_animacja.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      Alpha alpha_animacja = new Alpha(-1,5000); 
      RotationInterpolator obracacz = new RotationInterpolator(alpha_animacja, obrot_animacja);
      obracacz.setSchedulingBounds(bounds);
      obrot_animacja.addChild(obracacz);
      wezel_scena.addChild(obrot_animacja);
      

      //ŚWIATŁA

      AmbientLight lightA = new AmbientLight();
      lightA.setInfluencingBounds(bounds);
      wezel_scena.addChild(lightA);

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
      lightD.setColor(new Color3f(1.0f, 1.0f, 1.0f));
      wezel_scena.addChild(lightD);

      
//PODSTAWA
        Appearance wygladPodstawy = new Appearance();
        wygladPodstawy.setColoringAttributes(new ColoringAttributes(0.9f,0.9f,0.8f,ColoringAttributes.NICEST));
        Cylinder podstawa = new Cylinder(0.12f , 0.08f , wygladPodstawy);
        Transform3D  p_podstawy   = new Transform3D();
        p_podstawy.set(new Vector3f(0.0f,-0.5f,0f));
        TransformGroup podstawka = new TransformGroup(p_podstawy);
        podstawka.addChild(podstawa);
//CZESC PIERWSZA ROBOTA
        Appearance  wygladCylindra = new Appearance();

        PolygonAttributes polygAttr = new PolygonAttributes();
        polygAttr.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        wygladCylindra.setPolygonAttributes(polygAttr);
        
        //wygladCylindra.setColoringAttributes(new ColoringAttributes(0.3f,0.3f,0.3f,ColoringAttributes.NICEST));       //ewemtualny szary kolor robota
        Cylinder cylinder = new Cylinder(0.04f, 0.9f, wygladCylindra);
        Transform3D  p_cylindra   = new Transform3D();
        p_cylindra.set(new Vector3f(0.0f,-0.0f,0.0f));
        TransformGroup czesc_pierwsza = new TransformGroup(p_cylindra);
        czesc_pierwsza.addChild(cylinder);
//CZESC DRUGA ROBOTA
        Cylinder cylinder2 = new Cylinder(0.04f, 0.6f, wygladCylindra);
        Transform3D  p_cylindra2   = new Transform3D();
        p_cylindra2.set(new Vector3f(-0.3f,0.40f,0.0f));
        
        Transform3D  tmp_rot      = new Transform3D();
        tmp_rot.rotZ(Math.PI/2);
        
        p_cylindra2.mul(tmp_rot);
        TransformGroup czesc_druga = new TransformGroup(p_cylindra2);
        czesc_druga.addChild(cylinder2);
//CZESC TRZECIA ROBOTA
        Cylinder cylinder3 = new Cylinder(0.04f, 0.5f, wygladCylindra);
        Transform3D  p_cylindra3   = new Transform3D();
        p_cylindra3.set(new Vector3f(-0.76f,0.32f,0.0f));
  
        Transform3D  tmp_rot2      = new Transform3D();
        tmp_rot2.rotX(Math.PI/2);
        
        p_cylindra3.mul( tmp_rot);
        TransformGroup czesc_trzecia = new TransformGroup(p_cylindra3);
        czesc_trzecia.addChild(cylinder3);
//chwytak
        Cylinder chwytak_ = new Cylinder(0.04f, 0.4f, wygladCylindra);
        Transform3D  p_chwytaka   = new Transform3D();
        p_chwytaka.set(new Vector3f(-0.97f,0.15f,0.0f));
        TransformGroup chwytak = new TransformGroup(p_chwytaka);
        chwytak.addChild(chwytak_);
        
              
      Transform3D  p_robota   = new Transform3D();
      p_robota.set(new Vector3f(0f,0f,0.0f));
      
      TransformGroup robot = new TransformGroup(p_robota);
      robot.addChild(podstawka);
      robot.addChild(czesc_pierwsza);
      robot.addChild(czesc_druga);
      robot.addChild(czesc_trzecia);
      robot.addChild(chwytak);
      obrot_animacja.addChild(robot);
        
      
      
      
      return wezel_scena;

    }

   public static void main(String args[]){
      new mainWindow();

   }



}


