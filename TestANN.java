import ArtificialNeuralNetwork.NeuralNetwork;
import ArtificialNeuralNetwork.InputTargetPair;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TestANN extends Application implements Constants {

    // Variables
    int layerCount = LAYER_COUNT;
    int inputLayerNeuronCount = INPUT_LAYER_NEURON_COUNT;
    int hiddenLayerNeuronCount = HIDDEN_LAYER_NEURON_COUNT;
    int outputLayerNeuronCount = OUTPUT_LAYER_NEURON_COUNT;
    int epochs = EPOCHS;
    double learningRate = LEARNING_RATE;
    double momentum = MOMENTUM;

    String filename = PATH+FILENAME;

    @Override
    public void start(Stage primaryStage) {

        // Objects
        NeuralNetwork artificialNeuralNetwork;
        BorderPane borderPane = new BorderPane();
        InputImagePane inputImagePane = new InputImagePane(inputLayerNeuronCount);
        OutputDisplay outputDisplay = new OutputDisplay();
        VBox vBoxIO = new VBox();
        vBoxIO.setSpacing(5);
        vBoxIO.setPadding(new Insets(5));
        vBoxIO.getChildren().addAll(inputImagePane, outputDisplay);
        borderPane.setCenter(vBoxIO);

        // Input
        ArrayList<InputTargetPair> trainingSet = new ArrayList<>();
        {
            for(char sampleID=INITIAL_CHAR; sampleID<=TERMINAL_CHAR; sampleID++) {
                double[] target = new double[26];
                target[sampleID-'A']=1;

                for (int imageID = 1; imageID <= TRAINING_SET_SIZE; imageID++) {
                    BufferedImage bufferedImageSample = null;
                    try {
                        bufferedImageSample = ImageIO.read(new File("src/sample/img"+sampleID+"-"+ imageID + ".png"));
                    } catch (IOException e) {
                        break;
                    }
                    double[] inputArray = InputImagePane.convertToArray(bufferedImageSample);
                    trainingSet.add(new InputTargetPair(inputArray, target));
                }
            }
            if(DEBUG) System.out.println(trainingSet.size()+" samples successfully imported to training set");
        }
        Collections.shuffle(trainingSet);
        ArrayList<InputTargetPair> testSet = new ArrayList<>();
        {
            for(char sampleID=INITIAL_CHAR; sampleID<=TERMINAL_CHAR; sampleID++) {
                double[] target = new double[26];
                target[sampleID-'A']=1;
                for (int imageID = 1016; imageID >= 1016- TEST_SET_SIZE; imageID--) {
                    BufferedImage bufferedImageSample = null;
                    try {
                        bufferedImageSample = ImageIO.read(new File("src/sample/img"+sampleID+"-"+ imageID + ".png"));
                    } catch (IOException e) {
                        break;
                    }
                    double[] inputArray = InputImagePane.convertToArray(bufferedImageSample);
                    testSet.add(new InputTargetPair(inputArray, target));
                }
            }
            if(DEBUG) System.out.println(testSet.size()+" samples successfully imported to test set");
        }

        artificialNeuralNetwork = new NeuralNetwork(layerCount, inputLayerNeuronCount, hiddenLayerNeuronCount, outputLayerNeuronCount);


        // TextFields
        HBox hBoxEpochs = new HBox();
        Text textEpochs = new Text(0,0, "Epochs");
        TextField textFieldEpochs = new TextField(""+epochs);
        textFieldEpochs.setMaxWidth(60);
        textFieldEpochs.textProperty().addListener(e -> {
            int value = Integer.parseInt(textFieldEpochs.getText());
            if(value>=0)
                epochs = value;
            if(DEBUG) System.out.println("Epochs value changed to: "+epochs);
        });
        hBoxEpochs.setAlignment(Pos.CENTER_LEFT);
        hBoxEpochs.setSpacing(10);
        hBoxEpochs.getChildren().add(textEpochs);
        hBoxEpochs.getChildren().add(textFieldEpochs);

        HBox hbLearningRate = new HBox();
        Text textLearningRate = new Text(0,0, "Learning Rate");
        TextField textFieldLearningRate = new TextField(""+learningRate);
        textFieldLearningRate.setMaxWidth(80);
        textFieldLearningRate.textProperty().addListener(e -> {
            double value = Double.parseDouble(textFieldLearningRate.getText());
            if(value<=1 && value>=0 || value==-1)
                learningRate = value;
            if(DEBUG) System.out.println("learning rate value changed to: "+learningRate);
        });
        hbLearningRate.setAlignment(Pos.CENTER_LEFT);
        hbLearningRate.setSpacing(10);
        hbLearningRate.getChildren().add(textLearningRate);
        hbLearningRate.getChildren().add(textFieldLearningRate);

        HBox hbMomentum = new HBox();
        Text textMomentum = new Text(0,0, "Momentum");
        TextField textFieldMomentum = new TextField(""+momentum);
        textFieldMomentum.setMaxWidth(50);
        textFieldMomentum.textProperty().addListener(e -> {
            double value = Double.parseDouble(textFieldMomentum.getText());
            if(value<=1 && value>=0)
                momentum = value;
            if(DEBUG) System.out.println("momentum value changed to: "+momentum);
        });
        hbMomentum.setAlignment(Pos.CENTER_LEFT);
        hbMomentum.setSpacing(10);
        hbMomentum.getChildren().add(textMomentum);
        hbMomentum.getChildren().add(textFieldMomentum);

        // Buttons
        Button nextState = new Button("Next Example");
        Button train = new Button("Train (Back Propagation)");
        Button test = new Button("Test Set");
        Button reset = new Button("Reset Synapses");
        Button restore = new Button("Import ANN");
        Button save = new Button("Export ANN");
        VBox controlPanel = new VBox();
        controlPanel.setAlignment(Pos.TOP_CENTER);
        controlPanel.setSpacing(5);
        controlPanel.setPadding(new Insets(5));
        controlPanel.getChildren().addAll(hBoxEpochs, hbLearningRate, hbMomentum,
                restore, save, reset, train, test, nextState);
        nextState.setMinHeight(40);
        borderPane.setLeft(controlPanel);
        nextState.setOnMouseClicked(event -> {
            inputImagePane.randomGenerator();
            inputImagePane.draw();
            outputDisplay.drawOutput(categorize(artificialNeuralNetwork, inputImagePane.getInput()));
        });
        train.setOnMouseClicked(event -> {
            Collections.shuffle(trainingSet);
            train(artificialNeuralNetwork, trainingSet, epochs, learningRate, momentum);
        });
        test.setOnMouseClicked(event -> {
            test(artificialNeuralNetwork, testSet);
        });
        reset.setOnMouseClicked(event -> {
            reset(artificialNeuralNetwork);
        });
        restore.setOnMouseClicked(event -> {
            read(artificialNeuralNetwork, filename);
        });
        save.setOnMouseClicked(event -> {
            write(artificialNeuralNetwork, filename);
        });

        // Put the pane on the scene and the scene on the stage
        Scene scene = new Scene(borderPane, WIDTH, Constants.HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("OCR with Artificial Neural Network - Andrea Pinardi");
        primaryStage.show();
    }

    public static void reset(NeuralNetwork neuralNetwork){
        neuralNetwork.resetSynapses();
        if(DEBUG) print(neuralNetwork);
    }
    public static void write(NeuralNetwork neuralNetwork, String filename){
        neuralNetwork.writeANNToFile(filename);
        if(DEBUG) System.out.println(neuralNetwork.toString("Neural Network written to file \""+filename+"\""));
    }
    public static void print(NeuralNetwork neuralNetwork){
        System.out.println(neuralNetwork.toString("NeuralNetwork"));
    }
    private static void read(NeuralNetwork neuralNetwork, String filename){
        neuralNetwork.toNeurons(NeuralNetwork.readANNFromFile(filename));
        if(DEBUG) System.out.println(neuralNetwork.toString("Neural Network read from file \""+filename+"\""));
    }
    public static void train(NeuralNetwork neuralNetwork, ArrayList<InputTargetPair> trainingSet, int epochs, double learningRate, double momentum){
        neuralNetwork.train(epochs, learningRate, trainingSet, momentum);
        if(DEBUG) System.out.println("Training set error("+trainingSet.size()+"):"+neuralNetwork.getNetworkError());
    }
    private static void test(NeuralNetwork neuralNetwork, ArrayList<InputTargetPair> testSet){
        neuralNetwork.test(testSet);
        if(DEBUG) System.out.println("Test set error("+testSet.size()+"):"+neuralNetwork.getNetworkError());
    }
    private static double[] categorize(NeuralNetwork neuralNetwork, double[] input) {
        neuralNetwork.feedForward(input);
        return neuralNetwork.getOutput();
    }
    private static void init(double value, double[] array){
        for(int i=0; i<array.length; i++){
            array[i]=value;
        }
    }
}