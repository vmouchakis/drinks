/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drinks;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

/**
 *
 * @author theo
 */
public class Drinks {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String namespace = "http://www.drinks.gr/";
        
        Model model = ModelFactory.createDefaultModel();
        
        Resource drink = model.createResource(namespace + "ποτό");  
        Resource greece = model.createResource(namespace + "Ελλάδα");        
        Resource ouzo = model.createResource(namespace + "ούζο");
        Resource c15 = model.createResource(namespace + "κόστος_15_ευρώ");
        Resource wine = model.createResource(namespace + "κρασί");
        Resource france = model.createResource(namespace + "Γαλλία");
        Resource grey_goose = model.createResource(namespace + "Grey_Goose");
        Resource a40 = model.createResource(namespace + "βαθμοί_αλκοόλ_40");   
        Resource johny_walker = model.createResource(namespace + "Johny_Walker_Black_Lable");
        Resource jack_daniels  = model.createResource(namespace + "Jack_Daniels");
        Resource whiskey = model.createResource(namespace + "Ουίσκι");
        Resource jameson = model.createResource(namespace + "Jameson");
        Resource ireland = model.createResource(namespace + "Ιρλανδία");
        Resource guinness = model.createResource(namespace + "Guinness");
        Resource beer = model.createResource(namespace + "μπύρα");
        
        
        Property made_in = model.createProperty(namespace + "made_in");
        Property has = model.createProperty(namespace + "has");
        Property costs = model.createProperty(namespace + "costs");
        Property is = model.createProperty(namespace + "is");
       
        model.add(wine, made_in, greece)
                .add(ouzo, made_in, greece)
                .add(wine, made_in, france)
                .add(wine, costs, c15)
                .add(ouzo, costs, c15)
                .add(ouzo, has, a40)
                
                .add(jack_daniels, is, whiskey)
                .add(jameson, is, whiskey)
                .add(jameson, made_in , ireland)
                .add(guinness, made_in, ireland)
                .add(guinness, is, beer)
                
                .add(grey_goose, made_in, france)
                .add(grey_goose, has, a40)
                .add(johny_walker, has, a40)
                
                .add(ouzo, is, drink)
                .add(wine, is, drink)
                .add(jack_daniels, is, drink)
                .add(jameson, is, drink)
                .add(guinness, is, drink)
                .add(grey_goose, is, drink)
                .add(johny_walker, is, drink)
                ;
                
        
        try{
            PrintWriter p = new PrintWriter(new File("drinks.rdf"));
            model.write(p);
            model.write(System.out);
            p.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
        
        Graph<RDFNode, Statement> g = new JenaJungGraph(model);

        Layout<RDFNode, Statement> layout = new FRLayout(g);

        int width = 1400;
        int height = 800;

        layout.setSize(new Dimension(width, height));
        VisualizationImageServer<RDFNode, Statement> viz =
               new VisualizationImageServer(layout, new Dimension(width, height));
        RenderContext context = viz.getRenderContext();
        context.setEdgeLabelTransformer(Transformers.EDGE);
        context.setVertexLabelTransformer(Transformers.NODE);


        viz.setPreferredSize(new Dimension(width, height));
        Image img = viz.getImage(new Point(width/2, height/2), new Dimension(width, height));

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        // Fill the background in white
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        // Draw the image
        g2d.setColor(Color.white);
        g2d.drawImage(img, 0, 0, width, height, Color.blue, null);

        try {
            // Save the image to a file
            ImageIO.write(bi, "PNG", new File("drinks.png"));
            System.out.println("Image drinks.png saved");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        JFrame frame = new JFrame("drinks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(viz);
        frame.pack();
        frame.setVisible(true);
    }
    
}
