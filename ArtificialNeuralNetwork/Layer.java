package ArtificialNeuralNetwork;

import java.util.HashMap;
import java.util.Map;

public class Layer implements NetworkInterface{

    // Parameters
    private int layerID;
    private TYPE layerType;
    private Neuron[] neurons;

    private int dendritesCount;
    private double[] axons;

    // Constructors
    public Layer(int layerID, TYPE layerType, int neuronCount, int inputSize){
        neurons = new Neuron[neuronCount];
        this.layerID = layerID;
        this.layerType = layerType;
        this.dendritesCount = inputSize;
        for(int index=0; index<neuronCount; index++)
            neurons[index] = (new Neuron(layerID, index, layerType, inputSize));
    }
    public Layer(Map<String, Map> neuronMap, int layerID, TYPE layerType, int inputSize){
        neurons = new Neuron[neuronMap.size()];
        this.layerID = layerID;
        this.layerType = layerType;
        this.dendritesCount = inputSize;
        for(int index=0; index<neuronMap.size(); index++)
            neurons[index] = new Neuron(neuronMap.get("neuron"+index), layerID, index, layerType, inputSize);
    }

    // Methods
    public void feedForward(double[] input){
        axons = new double[neurons.length];
        for(int index=0; index<neurons.length; index++){
            neurons[index].feedForward(input);
            axons[index] = neurons[index].getAxon();
        }
    }
    public double[] backPropagate(double learningRate, double[] deltaTargetObtained, double alpha, boolean isNewEpoch){
        double[] correctionArray = new double[dendritesCount];
        for(int index=0; index<neurons.length; index++){
            accumulate(correctionArray, neurons[index].backPropagate(learningRate, deltaTargetObtained[index], alpha, isNewEpoch));
        }
        return correctionArray;
    }

    // Getters
    public int getAxonCount(){
        return neurons.length;
    }
    public double[] getAxons(){ return axons; }

    // Setters
    @Override
    public void resetSynapses(){
        for(int index=0; index<neurons.length; index++){
            neurons[index].resetSynapses();
        }
    }

    // IO
    public Map<String, Map> toMap(){
        Map<String, Map> neuronMap = new HashMap<>();
        for(int i=0; i<neurons.length; i++)
            neuronMap.put("neuron"+i, neurons[i].toMap());
        return neuronMap;
    }
    public void toNeurons(Map<String, Map> neuronMap){
        for(int i=0; i<neurons.length; i++)
            neurons[i].toNeurons(neuronMap.get("neuron"+i));
    }
    @Override
    public String outputToString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(layerType+" layer (id"+layerID+") axons: ");
        for(int index = 0; index< axons.length; index++) stringBuilder.append("n"+index+"=["+ axons[index]+"] ");
        return stringBuilder.toString();
    }

    // Services
    public static void accumulate(double[] accumulator, double[] array){
        for(int index=0; index<accumulator.length; index++)
            accumulator[index]+=array[index];
    }
    public static void init(double value, double[] array){
        for(int index=0; index<array.length; index++)
            array[index]+= value;
    }
}
