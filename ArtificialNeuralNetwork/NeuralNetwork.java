package ArtificialNeuralNetwork;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NeuralNetwork implements NetworkInterface {

    // Parameters
    private int layerCount, inNeuronCount, hiddenNeuronCount, outNeuronCount;
    private Layer[] layers;
    private double[] output;
    private double networkError;

    // Constructors
    public NeuralNetwork(int layerCount, int inNeuronCount, int hiddenNeuronCount, int outNeuronCount){
        this.layerCount = layerCount;
        this.inNeuronCount = inNeuronCount;
        this.hiddenNeuronCount = hiddenNeuronCount;
        this.outNeuronCount = outNeuronCount;
        layers = new Layer[layerCount];

        // Input Layer
        layers[0] = new Layer(0, TYPE.input, this.inNeuronCount, 1);

        // Hidden Layers
        for(int layerID=1; layerID<layerCount-1; layerID++) {
            layers[layerID] = new Layer(layerID, TYPE.hidden0, this.hiddenNeuronCount, layers[layerID-1].getAxonCount());
        }

        // Output Layer
        layers[layerCount-1] = new Layer(layerCount-1, TYPE.output, outNeuronCount, layers[layerCount-2].getAxonCount());
    }
    public NeuralNetwork(Map<String, Map> annMap){
        Map<String, Integer> info = annMap.get("info");
        this.layerCount = info.get("layerCount");
        this.inNeuronCount = info.get("inNeuronCount");
        this.hiddenNeuronCount = info.get("hiddenNeuronCount");
        this.outNeuronCount = info.get("outNeuronCount");
        Map<String, Map> layerMap = annMap.get("layerMap");
        layers = new Layer[layerCount];

        // Input Layer
        layers[0] = new Layer(layerMap.get("layer"+0), 0, TYPE.input, 1);

        // Hidden Layer
        for(int layerID=1; layerID<layerCount-1; layerID++)
            layers[layerID] = new Layer(layerMap.get("layer"+layerID), layerID, TYPE.hidden0, layers[layerID-1].getAxonCount());

        // Output Layer
        layers[layerCount-1] = new Layer(layerMap.get("layer"+(layerCount-1)), layerCount-1, TYPE.output, layers[layerCount-2].getAxonCount());
    }

    // Functions
    @Override
    public void feedForward(double[] input){
        for(int index=0; index<layers.length; index++) {
            layers[index].feedForward(input);
            output = layers[index].getAxons();
            input = output;
        }
        if(DEBUG) System.out.println(outputToString());
    }
    private void backPropagate(double learningRate, double[] target, double alpha, boolean isNewEpoch){
        double[] deltaTargetObtained = new double[target.length];
        for(int index=0; index<target.length; index++) {
            double error = target[index]-output[index];
            deltaTargetObtained[index] = error;
            networkError+= error*error;
        }
        for(int index=layers.length-1; index>0; index--){
            double[] newDeltaTargetObtained = layers[index].backPropagate(learningRate, deltaTargetObtained, alpha, isNewEpoch);
            deltaTargetObtained = newDeltaTargetObtained;
        }
    }

    public void train(int epochs, double learningRate, ArrayList<InputTargetPair> inputTargetPairs, double alpha){
        boolean variableLearningRate = false;
        if(learningRate==-1)
            variableLearningRate = true;
        for(int epoch=1; epoch<=epochs; epoch++){
            if(variableLearningRate)
                learningRate=1.0/(1+epoch);
            boolean isNewEpoch = true;
            if(DEBUG) System.out.println("Epoch: "+epoch+" ~ Learning Rate="+learningRate+" : Momentum="+alpha);
            networkError = 0;
            for(InputTargetPair inputTargetPair: inputTargetPairs) {
                if(inputTargetPair.getInputSize()==inNeuronCount && inputTargetPair.getTargetSize()==outNeuronCount) {
                    feedForward(inputTargetPair.getInput());
                    backPropagate(learningRate, inputTargetPair.getTarget(), alpha, isNewEpoch);
                    isNewEpoch = false;
                }
            }
            networkError/=2;
        }
    }
    public void test(ArrayList<InputTargetPair> inputTargetPairs){
        networkError = 0;
        for(InputTargetPair inputTargetPair: inputTargetPairs) {
            if(inputTargetPair.getInputSize()==inNeuronCount && inputTargetPair.getTargetSize()==outNeuronCount) {
                feedForward(inputTargetPair.getInput());
                for(int index=0; index<2; index++) {
                    double error = inputTargetPair.getTarget()[index]-output[index];
                    networkError+= error*error;
                }
            }
        }
        networkError/=2;
    }

    // Getters
    public double[] getOutput() {
        return output;
    }
    public double getNetworkError() {
        return networkError;
    }

    // Setters
    public void resetSynapses(){
        for(int index=0; index<layers.length; index++)
            layers[index].resetSynapses();
    }

    // IO
    private Map<String, Map> toMap(){
        Map<String, Map> annMap = new HashMap<>();
        Map<String, Integer> info = new HashMap<>();
        info.put("layerCount", layerCount);
        info.put("inNeuronCount", inNeuronCount);
        info.put("hiddenNeuronCount", hiddenNeuronCount);
        info.put("outNeuronCount", outNeuronCount);
        annMap.put("info", info);
        Map<String, Map> layerMap = new HashMap<>();
        annMap.put("layerMap", layerMap);
        for(int i=0; i<layers.length; i++)
            layerMap.put("layer"+i, layers[i].toMap());
        return annMap;
    }
    public void toNeurons(Map<String, Map> annMap){
        Map<String, Integer> info = annMap.get("info");
        layerCount = info.get("layerCount");
        inNeuronCount = info.get("inNeuronCount");
        hiddenNeuronCount = info.get("hiddenNeuronCount");
        outNeuronCount = info.get("outNeuronCount");
        Map<String, Map> layerMap = annMap.get("layerMap");
        for(int i=0; i<layers.length; i++)
            layers[i].toNeurons(layerMap.get("layer"+i));
    }
    @Override
    public String toString(){
        return toMap().toString();
    }
    public String toString(String title){
        return title+": "+toString();
    }
    @Override
    public String outputToString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<layerCount; i++) stringBuilder.append(layers[i].outputToString()+"\n");
        stringBuilder.append("Network Error:"+networkError);
        return stringBuilder.toString();
    }
    public static HashMap<String, Map> readANNFromFile(String filename){
        HashMap<String, Map> ANNmap = new HashMap<>();
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
            ANNmap = (HashMap<String, Map>)is.readObject();
            is.close();
        }
        catch (IOException e){e.printStackTrace();}
        catch (ClassNotFoundException e){e.printStackTrace();}
        return ANNmap;
    }
    public void writeANNToFile(String filename){
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
            os.writeObject(toMap());
            os.close();
        } catch (IOException e){e.printStackTrace();}
    }

}
