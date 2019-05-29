import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Box;
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
      
       Transform3D  pozycja_obrotu   = new Transform3D();
        pozycja_obrotu.set(new Vector3f(0.0f,40f,0f));
      
      TransformGroup obrot_animacja_podstawa = new TransformGroup(pozycja_obrotu);
      obrot_animacja_podstawa.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      Alpha alpha_animacja = new Alpha(-1,5000); 
      RotationInterpolator obracacz = new RotationInterpolator(alpha_animacja, obrot_animacja_podstawa);
      obracacz.setSchedulingBounds(bounds);
      
        Transform3D  tmp_rot0      = new Transform3D();
        
      
      
      
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
        Box cylinder2 = new Box(0.1f, 0.2f, 0.1f ,wygladCylindra);
        Transform3D  p_cylindra2   = new Transform3D();
        p_cylindra2.set(new Vector3f(-0.0f,0.5f,0.0f));
        
        Transform3D  tmp_rot      = new Transform3D();
        tmp_rot.rotZ(Math.PI/2);
        p_cylindra2.mul(tmp_rot);
        
       
    
        tmp_rot0.rotY(Math.PI/3);
        tmp_rot0.mul(tmp_rot);
     
      obracacz.setTransformAxis(tmp_rot0);
      obrot_animacja_podstawa.addChild(obracacz);
      //obrot_animacja_podstawa.addChild(pozycja_obrotu);
      wezel_scena.addChild(obrot_animacja_podstawa);
      
      
        TransformGroup czesc_druga = new TransformGroup(p_cylindra2);
        czesc_druga.addChild(cylinder2);
        //czesc_druga.addChild(obrot_animacja_pochyl);
      //  czesc_druga.addChild(obrot_animacja_pochyl);
        
//CZESC TRZECIA ROBOTA
        Cylinder cylinder3 = new Cylinder(0.04f, 1f, wygladCylindra);
        Transform3D  p_cylindra3   = new Transform3D();
        p_cylindra3.set(new Vector3f(-0f,0.5f,0.0f));
  
        Transform3D  tmp_rot2      = new Transform3D();
        tmp_rot2.rotY(-Math.PI/2);
       // tmp_rot2.rotZ(Math.PI/2);
        
        p_cylindra3.mul( tmp_rot2);
        TransformGroup czesc_trzecia = new TransformGroup(p_cylindra3);
        czesc_trzecia.addChild(cylinder3);
//chwytak


        Cylinder chwytak_ = new Cylinder(0.04f, 0.5f, wygladCylindra);
        Transform3D  p_chwytaka   = new Transform3D();
        p_chwytaka.set(new Vector3f(-0.1f,0.9f,0.0f));
        p_chwytaka.mul( tmp_rot);

        
        TransformGroup chwytak = new TransformGroup(p_chwytaka);
        chwytak.addChild(chwytak_);
        
              
      Transform3D  p_robota   = new Transform3D();
      p_robota.set(new Vector3f(0f,0f,0.0f));
      
      TransformGroup robot = new TransformGroup(p_robota);
     // robot.addChild(podstawka);
      robot.addChild(czesc_pierwsza);
      czesc_druga.addChild(czesc_trzecia);
      czesc_druga.addChild(chwytak);
      obrot_animacja_podstawa.addChild(czesc_druga);
     // obrot_animacja_podstawa.addChild(robot);
     // obrot_animacja_podstawa.addChild(robot);
      wezel_scena.addChild(podstawka);
      wezel_scena.addChild(robot);
      //obrot_animacja_pochyl.addChild(robot);

            
      return wezel_scena;

    }

   public static void main(String args[]){
      new mainWindow();

   }

    private TransformGroup rotate(TransformGroup czesc_druga, Alpha alpha) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}


