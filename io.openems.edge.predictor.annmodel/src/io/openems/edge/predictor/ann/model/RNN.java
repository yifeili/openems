package io.openems.edge.predictor.ann.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.datavec.api.records.reader.SequenceRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.transform.schema.Schema;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator;
import org.deeplearning4j.eval.RegressionEvaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.lossfunctions.impl.LossL1;

public class RNN {
	

	private static MultiLayerNetwork MODEL;

    private static Schema CURRENT_SCHEMA;

    private static Schema SCHEMA_1;
    private static Schema SCHEMA_2;
    private static Schema SCHEMA_3;
    private static Schema SCHEMA_4;
    private static Schema SCHEMA_5;

    private static final String DATASET_1 = "DataSet_1";
    private static final String DATASET_2 = "DataSet_2";
    private static final String DATASET_3 = "DataSet_3";

    // 01.10.2019 02:00 - 07.10.2019 23:00
    private static final String DATASET_4 = "DataSet_4";

    // 08.10.2019 02:00 - 13.10.2019 23:00
    private static final String DATASET_5 = "DataSet_5";

    // 01.10.2019 02:00 - 13.10.2019 23:00
    private static final String DATASET_6 = "DataSet_6";

    // 01.10.2019 02:00 - 13.10.2019 23:00, reduced parameter
    private static final String DATASET_7 = "DataSet_7";

    // 10.10.2019 02:00 - 17.10.2019 01:00
    private static final String DATASET_8 = "DataSet_8";

    // 01.10.2019 02:00 - 17.10.2019 01:00
    private static final String DATASET_9 = "DataSet_9";

    // 01.10.2019 02:00 - 17.10.2019 01:00, reduced parameter
    private static final String DATASET_10 = "DataSet_10";

    // 01.10.2019 02:00 - 17.10.2019 01:00 -> test with forecasted values from weatherbit
    //                                        17.10.2019 14:00 - 19.10.2019 13:00 (48 hours)
    private static final String DATASET_11 = "DataSet_11";

    private static final String TRAIN = "Train";
    private static final String TRAIN_5 = "Train_5";
    private static final String TRAIN_24 = "Train_24";

    private static final String TEST = "Test";
    private static final String TEST_5 = "Test_5";
    private static final String TEST_24 = "Test_24";

    private static String CURRENT_DATASET;
    private static String CURRENT_TRAIN;
    private static String CURRENT_TEST;
    private static DataNormalization NORMALIZER;
    private static MultiLayerConfiguration CONFIG;

    private static final int BATCH_SIZE = 20;
    private static final int EPOCHS = 2000;
    private static int LSTM_LAYER_SIZE = 118;

    /* package */ static {
        // DataSet_1
        SCHEMA_1 = new Schema.Builder()
                .addColumnsDouble("solar_rad", "uv", "temp")
                .addColumnCategorical("pod", "0", "1")
                .addColumnsDouble("rh", "PV output")
                .build();

        // DataSet_2
        SCHEMA_2 = new Schema.Builder()
                .addColumnsDouble("solar_rad", "uv", "temp")
                .addColumnCategorical("pod", "0", "1")
                .addColumnsInteger("clouds")
                .addColumnsDouble("PV output")
                .build();

        // DataSet_3
        SCHEMA_3 = new Schema.Builder()
                .addColumnsDouble("solar_rad", "uv", "temp")
                .addColumnCategorical("pod", "0", "1")
                .addColumnsInteger("clouds")
                .addColumnsDouble("wind_spd")
                .addColumnsInteger("rh")
                .addColumnsDouble("PV output")
                .build();

        // DataSet_4, DataSet_5, DataSet_6, DataSet_8, DataSet_9, DataSet_11
        SCHEMA_4 = new Schema.Builder()
                .addColumnsDouble("solar_rad", "uv", "temp")
                .addColumnCategorical("pod", "0", "1")
                .addColumnsInteger("clouds")
                .addColumnsDouble("wind_spd")
                .addColumnsDouble("wind_dir")
                .addColumnsInteger("rh")
                .addColumnsDouble("PV output")
                .build();

        // DataSet_7, DataSet_10
        SCHEMA_5 = new Schema.Builder()
                .addColumnsDouble("solar_rad", "uv", "temp")
                .addColumnCategorical("pod", "0", "1")
                .addColumnsInteger("clouds")
                .addColumnsInteger("rh")
                .addColumnsDouble("PV output")
                .build();

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CURRENT_SCHEMA = SCHEMA_4;
        CURRENT_DATASET = DATASET_9;
        CURRENT_TRAIN = TRAIN_24;
        CURRENT_TEST = TEST_24;

        createRNN();
        //evaluate();
        //optimizingHyperParams();
    }
    
    /*

    private static void optimizingHyperParams() throws InterruptedException, IOException {
        // Same for all layers
        ParameterSpace<Double> learningRateHyperparam = new ContinuousParameterSpace(0.0001, 0.1);
        DiscreteParameterSpace<WeightInit> weightInitSpace = new DiscreteParameterSpace<>(
                WeightInit.XAVIER, WeightInit.RELU, WeightInit.DISTRIBUTION);

        // Same for both hidden layers
        ParameterSpace<Integer> layerSizeHyperparam = new IntegerParameterSpace(16, 256);

        // Same for both hidden layers
        DiscreteParameterSpace<Activation> activationSpaceHiddenLayer = new DiscreteParameterSpace<>(
                Activation.TANH, Activation.SIGMOID, Activation.LEAKYRELU, Activation.RELU);

        DiscreteParameterSpace<Activation> activationSpaceOutputLayer = new DiscreteParameterSpace<>(
                Activation.RELU, Activation.IDENTITY);

        MultiLayerSpace mls =
                new MultiLayerSpace.Builder()
                        .seed(0xC0FFEE)
                        .weightInit(weightInitSpace)
                        .l2(0.0001)
                        .updater(new AdamSpace(learningRateHyperparam))
                        .addLayer(new LSTMLayerSpace.Builder()
                                // Fixed input neurons
                                .nIn(CURRENT_SCHEMA.getColumnNames().size() - 1)
                                // HyperParam for output neurons
                                .nOut(layerSizeHyperparam)
                                .activation(activationSpaceHiddenLayer)
                                .build())
                        .addLayer(new LSTMLayerSpace.Builder()
                                // Input neurons not defined, is done automatically by DL4J for matching to number of output neurons of layer above
                                .nOut(layerSizeHyperparam)
                                .activation(activationSpaceHiddenLayer)
                                .build())
                        .layer(new RnnOutputLayerSpace.Builder()
                                .nOut(1)
                                .lossFunction(LossFunctions.LossFunction.MSE)
                                .activation(activationSpaceOutputLayer)
                                .build())
                        .setInputType(InputType.recurrent((CURRENT_SCHEMA.numColumns() - 1)))
                        .build();

        // Generate candidates by random search
        CandidateGenerator candidateGenerator = new RandomSearchGenerator(mls, null);

        // Data Source
        Class<? extends DataSource> dataSourceClass = ExampleDataSource.class;
        Properties dataSourceProperties = new Properties();
        dataSourceProperties.setProperty("minibatchSize", "25");

        // Save respective models
        String baseSaveDirectory = "src/main/resources/arbiter/";
        File f = new File(baseSaveDirectory);
        if (f.exists()) {
            f.delete();
        } else {
            f.mkdir();
        }

        ResultSaver modelSaver = new FileModelSaver(baseSaveDirectory);
        ScoreFunction scoreFunction = new RegressionScoreFunction(org.deeplearning4j.eval.RegressionEvaluation.Metric.MAE);

        TerminationCondition[] terminationConditions = {
                new MaxTimeCondition(10, TimeUnit.MINUTES),
                //new MaxCandidatesCondition(10)
        };

        // Configuration that stores the above parameters
        OptimizationConfiguration configuration = new OptimizationConfiguration.Builder()
                .candidateGenerator(candidateGenerator)
                .dataSource(dataSourceClass, dataSourceProperties)
                .modelSaver(modelSaver)
                .scoreFunction(scoreFunction)
                .terminationConditions(terminationConditions)
                .build();

        // Runner for local execution
        IOptimizationRunner runner = new LocalOptimizationRunner(configuration, new MultiLayerNetworkTaskCreator());

        StatsStorage ss = new FileStatsStorage(new File(System.getProperty("java.io.tmpdir"), "arbiterExampleUiStats.dl4j"));
        runner.addListeners(new ArbiterStatusListener(ss));
        runner.execute();

        //Print out some basic stats regarding the optimization procedure
        String s = "Best score: " + runner.bestScore() + "\n" +
                "Index of model with best score: " + runner.bestScoreCandidateIndex() + "\n" +
                "Number of configurations evaluated: " + runner.numCandidatesCompleted() + "\n";
        System.out.println(s);

        //Get all results, and print out details of the best result:
        int indexOfBestResult = runner.bestScoreCandidateIndex();
        List<ResultReference> allResults = runner.getResults();

        OptimizationResult bestResult = allResults.get(indexOfBestResult).getResult();
        MultiLayerNetwork bestModel = (MultiLayerNetwork) bestResult.getResultReference().getResultModel();

        System.out.println("\n\nConfiguration of best model:\n");
        System.out.println(bestModel.getLayerWiseConfigurations().toJson());
    }
    */
    
    /*

    public static class ExampleDataSource implements DataSource {
        private int minibatchSize;

        public ExampleDataSource() { }

        @Override
        public void configure(Properties properties) {
            this.minibatchSize = Integer.parseInt(properties.getProperty("minibatchSize", "16"));
        }

        @Override
        public Object trainData() {
            Random random = new Random();
            random.setSeed(0xC0FFEE);
            FileSplit inputSplit = new FileSplit(new File("src/main/resources/" + CURRENT_DATASET + "/" + CURRENT_TRAIN + "/"), random);

            SequenceRecordReader recordReader = new CSVSequenceRecordReader(0, ",");
            try {
                recordReader.initialize(inputSplit);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            DataSetIterator trainIterator =
                    new SequenceRecordReaderDataSetIterator(recordReader, minibatchSize, -1,
                            CURRENT_SCHEMA.getIndexOfColumn("PV output"), true);

            NORMALIZER = new NormalizerStandardize();
            NORMALIZER.fitLabel(true);
            NORMALIZER.fit(trainIterator);
            trainIterator.reset();
            trainIterator.setPreProcessor(NORMALIZER);

            return trainIterator;
        }

        @Override
        public Object testData() {
            SequenceRecordReader recordReaderTest = new CSVSequenceRecordReader(0, ",");
            try {
                recordReaderTest.initialize(new FileSplit(new File("src/main/resources/" + CURRENT_DATASET + "/" + CURRENT_TEST + "/")));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DataSetIterator testIterator = new SequenceRecordReaderDataSetIterator(recordReaderTest, minibatchSize, -1,
                    CURRENT_SCHEMA.getIndexOfColumn("PV output"), true);
            testIterator.setPreProcessor(NORMALIZER);

            return testIterator;
        }

        @Override
        public Class<?> getDataType() {
            return DataSetIterator.class;
        }
    }
    
    */

    private static void createRNN() throws IOException, InterruptedException {
        Random random = new Random();
        random.setSeed(0xC0FFEE);
        FileSplit inputSplit = new FileSplit(new File("src/main/resources/" + CURRENT_DATASET + "/" + CURRENT_TRAIN + "/"), random);

        //*TODO Delimiter seems to be different */
        SequenceRecordReader recordReader = new CSVSequenceRecordReader(0, ";");
        recordReader.initialize(inputSplit);

        DataSetIterator trainIterator =
                new SequenceRecordReaderDataSetIterator(recordReader, BATCH_SIZE, -1,
                        CURRENT_SCHEMA.getIndexOfColumn("PV output"), true);

        // TODO: perform appropriate normalizations
        NORMALIZER = new NormalizerStandardize();
        NORMALIZER.fitLabel(true);
        NORMALIZER.fit(trainIterator);
        trainIterator.reset();

        trainIterator.setPreProcessor(NORMALIZER);

        // TODO: configure hyperparameters accordingly

        // Predicts NON NaN values
        CONFIG =
                new NeuralNetConfiguration.Builder()
                        .seed(0xC0FFEE)
                        .weightInit(WeightInit.XAVIER)
                        .updater(new Adam.Builder().learningRate(0.004664987569179995).build())
                        .l2(0.0000316)
                        .list(
                                new LSTM.Builder()
                                        .nIn(CURRENT_SCHEMA.getColumnNames().size() - 1)
                                        .nOut(LSTM_LAYER_SIZE)
                                        .activation(Activation.TANH)
                                        .build(),
                                new LSTM.Builder()
                                        .nOut(LSTM_LAYER_SIZE)
                                        .activation(Activation.TANH)
                                        .build(),
                                new RnnOutputLayer.Builder(new LossL1())
                                        .nOut(1)
                                        .activation(Activation.IDENTITY)
                                        .build())
                        .setInputType(InputType.recurrent((CURRENT_SCHEMA.numColumns() - 1)))
                        .build();

        // Predicts only NaN values -> with TANH instead of LEAKYRELU as activation function it works (poorly)
        /*CONFIG =
                new NeuralNetConfiguration.Builder()
                        .seed(12648430)
                        .weightInit(WeightInit.RELU)
                        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                        .miniBatch(true)
                        .minimize(true)
                        .maxNumLineSearchIterations(5)
                        .updater(new Adam.Builder().learningRate(0.09475997296744358).build())
                        .list(
                                new LSTM.Builder()
                                        .nIn(CURRENT_SCHEMA.getColumnNames().size() - 1)
                                        .nOut(LSTM_LAYER_SIZE)
                                        .activation(Activation.LEAKYRELU)
                                        .biasInit(0.0)
                                        .gateActivationFunction(Activation.SIGMOID)
                                        .gradientNormalization(GradientNormalization.None)
                                        .gradientNormalizationThreshold(1.0)
                                        .forgetGateBiasInit(1.0)
                                        .l2(0.0001)
                                        .l2Bias(0.0)
                                        .l1(0.0)
                                        .l1Bias(0.0)
                                        .build(),
                                new LSTM.Builder()
                                        .nIn(LSTM_LAYER_SIZE)
                                        .nOut(LSTM_LAYER_SIZE)
                                        .activation(Activation.LEAKYRELU)
                                        .biasInit(0.0)
                                        .gateActivationFunction(Activation.SIGMOID)
                                        .gradientNormalization(GradientNormalization.None)
                                        .gradientNormalizationThreshold(1.0)
                                        .forgetGateBiasInit(1.0)
                                        .l2(0.0001)
                                        .l2Bias(0.0)
                                        .l1(0.0)
                                        .l1Bias(0.0)
                                        .build(),
                                new RnnOutputLayer.Builder()
                                        .nIn(LSTM_LAYER_SIZE)
                                        .nOut(1)
                                        .activation(Activation.IDENTITY)
                                        .biasInit(0.0)
                                        .gradientNormalization(GradientNormalization.None)
                                        .gradientNormalizationThreshold(1.0)
                                        .l2(0.0001)
                                        .l2Bias(0.0)
                                        .l1(0.0)
                                        .l1Bias(0.0)
                                        .lossFunction(LossFunctions.LossFunction.MSE)
                                        .build())
                        .setInputType(InputType.recurrent((CURRENT_SCHEMA.numColumns() - 1)))
                        .build();
        CONFIG.setPretrain(false);*/

        MODEL = new MultiLayerNetwork(CONFIG);
        MODEL.init();

        // Visualize scores and created model

        /*UIServer uiServer = UIServer.getInstance();
        StatsStorage statsStorage = new InMemoryStatsStorage();
        uiServer.attach(statsStorage);

        MODEL.addListeners(new ScoreIterationListener(50));
        MODEL.addListeners(new StatsListener(statsStorage, 50));*/

        MODEL.fit(trainIterator, EPOCHS);
    }

    
    /*
    private static void evaluate() throws IOException, InterruptedException {
        // evaluate fitted model
        SequenceRecordReader recordReaderTest = new CSVSequenceRecordReader(0, ";");
        recordReaderTest.initialize(new FileSplit(new File("src/main/resources/" + CURRENT_DATASET + "/" + CURRENT_TEST + "/")));
        DataSetIterator testIterator = new SequenceRecordReaderDataSetIterator(recordReaderTest, BATCH_SIZE, -1,
                CURRENT_SCHEMA.getIndexOfColumn("PV output"), true);
        testIterator.setPreProcessor(NORMALIZER);

        RegressionEvaluation evaluate = MODEL.evaluateRegression(testIterator);
        System.out.println(evaluate.stats());

        // predict pv output for next hours
        testIterator.reset();
        DataSet testData = testIterator.next(24);

        INDArray predictedValuesArr = MODEL.output(testData.getFeatures(), false);
        NORMALIZER.revertLabels(predictedValuesArr);

        double[] predictedValues = Nd4j.toFlattened(
                'c', predictedValuesArr).toDoubleVector();

        // get actual values and write result into a csv file
        NORMALIZER.revert(testData);
        double[] actualValues = Nd4j.toFlattened('c', testData.getLabels()).toDoubleVector();

        // Time information
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());

        File file = new File("RNN_24_hours_prediction_01_10_17_10" + CURRENT_DATASET + "_" + EPOCHS + "_epochs" + formatter.format(date) + ".csv");
        FileWriter fw = new FileWriter(file);
        StringBuilder sb = new StringBuilder();
        sb.append("timestep;actual;predicted");
        sb.append(System.getProperty("line.separator"));

        for (int i = 0; i < predictedValues.length; i++) {
            sb.append(i + 1).append(';').append(actualValues[i]).append(';').append(predictedValues[i]);
            sb.append(System.getProperty("line.separator"));
        }

        fw.write(sb.toString());
        fw.flush();
        fw.close();
    }
    */
}
