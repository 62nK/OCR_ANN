import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class OutputDisplay extends Pane  implements Constants {

    public OutputDisplay(){ }

    public void drawOutput(double[] output){
        getChildren().clear();
        Rectangle rectangleOutput = new Rectangle(20, 0, 250, 100);
        rectangleOutput.setFill(Color.TRANSPARENT);
        rectangleOutput.setStroke(Color.GREY);
        rectangleOutput.setStrokeWidth(1);
        getChildren().add(rectangleOutput);

        StringBuilder s = new StringBuilder();
        for(int i=0; i<output.length;i++)
            if( output[i]>0.9) s.append((char) ('A'+i));
        s.append("\n");

        Text out = new Text(25,20, "Category: "+s.toString());
        out.setFont(Font.font(20));
        getChildren().add(out);
    }
    public void drawAnnOutputs(String output){
        Text out = new Text(0,50, output);
        getChildren().add(out);
    }

}
