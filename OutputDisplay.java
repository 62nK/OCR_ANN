import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;


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
        int indexOfMax1 = indexesOf2Max(output)[0];
        int indexOfMax2 = indexesOf2Max(output)[1];

        s.append((char) ('A'+indexOfMax1));
        s.append("("+String.format("%.1f", output[indexOfMax1]*100)+"%), ");
        s.append((char) ('A'+indexOfMax2));
        s.append("("+String.format("%.1f", output[indexOfMax2]*100)+"%)");
        s.append("\n");

        Text out = new Text(25,20, "Category: "+s.toString());
        out.setFont(Font.font(15));
        getChildren().add(out);
    }
    public void drawAnnOutputs(String output){
        Text out = new Text(0,50, output);
        getChildren().add(out);
    }
    public int indexOfMax(double[] array){
        int indexOfMax=0;
        double max = array[0];
        for(int index=1; index<array.length; index++){
            if(array[index]>array[indexOfMax]) {
                max = array[index];
                indexOfMax = index;
            }
        }
        return indexOfMax;
    }
    public int[] indexesOf2Max(double[] array){
        int[] indexesOf2Max = new int[]{0,1};
        double[] maxes = new double[] {array[0], array[1]};
        for(int index=2; index<array.length; index++){
            if(array[index]> maxes[0]){
                maxes[0] = array[index];
                indexesOf2Max[0] = index;
                continue;
            }
            if(array[index]> maxes[1]){
                maxes[1] = array[index];
                indexesOf2Max[1] = index;
            }
        }
        if(maxes[0]<maxes[1]){
            int temp = indexesOf2Max[0];
            indexesOf2Max[0]=indexesOf2Max[1];
            indexesOf2Max[1]=temp;
        }
        return indexesOf2Max;
    }


    }
