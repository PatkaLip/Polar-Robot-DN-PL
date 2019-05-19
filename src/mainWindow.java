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
        canvas3D.setPreferredSize(new Dimension(800,600));

        add(canvas3D);
        pack();
        setVisible(true);

        BranchGroup scena = utworzScene();
	    scena.compile();

        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.0f,10.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);

        simpleU.addBranchGraph(scena);

    }

   BranchGroup utworzScene(){

      BranchGroup wezel_scena = new BranchGroup();


      TransformGroup obrot_animacja = new TransformGroup();
      obrot_animacja.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
      wezel_scena.addChild(obrot_animacja);

      Alpha alpha_animacja = new Alpha(-1,5000);

      RotationInterpolator obracacz = new RotationInterpolator(alpha_animacja, obrot_animacja);

      BoundingSphere bounds = new BoundingSphere();
      obracacz.setSchedulingBounds(bounds);
      obrot_animacja.addChild(obracacz);

      //ŚWIATŁA

      AmbientLight lightA = new AmbientLight();
      lightA.setInfluencingBounds(bounds);
      wezel_scena.addChild(lightA);

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
      lightD.setColor(new Color3f(1.0f, 1.0f, 1.0f));
      wezel_scena.addChild(lightD);

      //CYLINDER

      Appearance  wygladCylindra = new Appearance();
      wygladCylindra.setColoringAttributes(new ColoringAttributes(0.9f,0.9f,0.9f,ColoringAttributes.NICEST));
      Cylinder cylinder = new Cylinder(0.2f, 1.3f, wygladCylindra);

      Transform3D  p_cylindra   = new Transform3D();
      p_cylindra.set(new Vector3f(0.4f,-0.4f,-3.0f));

      Transform3D  tmp_rot      = new Transform3D();
      //tmp_rot.rotZ(Math.PI/2);
      tmp_rot.rotZ(0);
      p_cylindra.mul(tmp_rot);

      TransformGroup transformacja_c = new TransformGroup(p_cylindra);

      transformacja_c.addChild(cylinder);
      obrot_animacja.addChild(transformacja_c);

      //STOZEK

      return wezel_scena;

    }

   public static void main(String args[]){
      new mainWindow();

   }



}


