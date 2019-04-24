import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class InputImagePane extends Pane implements Constants {

    private double[] input;
    private int size;
    private Image character;

    public InputImagePane(int size){
        this.size = size;
        input = new double[size];
        randomGenerator();
    }

    public double[] getInput() {
        return input;
    }

    public void draw(){
        clear();
        drawCharacter();
    }
    private void drawCharacter(){
        ImageView imageViewCharacter = new ImageView(character);
        imageViewCharacter.setX(20);
        imageViewCharacter.setFitWidth(IMAGE_SIZE);
        imageViewCharacter.setPreserveRatio(true);
        imageViewCharacter.setSmooth(true);
        imageViewCharacter.setCache(true);
        getChildren().add(imageViewCharacter);
    }
    public void randomGenerator(){
        char sampleID = (char)((int)(Math.random()*26)+'A');
        int imageID = (int) (Math.random()*1016);
        BufferedImage bufferedImageSample = null;
        try {
            bufferedImageSample = ImageIO.read(new File("src/sample/img"+sampleID+"-"+ imageID + ".png"));
            character = new Image(new FileInputStream("src/sample/img"+sampleID+"-"+ imageID + ".png"));
        } catch (IOException e) {
        }
        input = convertToArray(bufferedImageSample);
    }
    private void clear(){
        getChildren().clear();
    }

    public static double[] convertToArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[] result = new double[height*width];

        for (int index = 0; index < height*width; index++) {
            int row = index/width;
            int col = index%width;
            java.awt.Color color = new java.awt.Color(image.getRGB(col, row));
            result[index] =((color.getRed()+color.getGreen()+color.getBlue())/3.0-128);
        }

        return result;
    }
}