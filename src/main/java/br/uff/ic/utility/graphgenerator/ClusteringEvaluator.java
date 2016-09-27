/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.utility.graphgenerator;

import br.uff.ic.graphmatching.GraphMatching;
import br.uff.ic.provviewer.Inference.AutomaticInference;
import br.uff.ic.utility.AttributeErrorMargin;
import br.uff.ic.utility.Dbscan;
import br.uff.ic.utility.Utils;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.Vertex;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kohwalter
 */
public class ClusteringEvaluator {

    double noiseProbability = 1.0F;
//    ArrayList<ArrayList<Double>> precision = new ArrayList<ArrayList<Double>>();
//    ArrayList<ArrayList<Double>> recall = new ArrayList<ArrayList<Double>>();
//    ArrayList<ArrayList<Double>> fmeasure = new ArrayList<ArrayList<Double>>();
//    ArrayList<ArrayList<Double>> clusters = new ArrayList<ArrayList<Double>>();
    
    ArrayList<Double> p_similarity = new ArrayList<>();
    ArrayList<Double> r_similarity = new ArrayList<>();
    ArrayList<Double> f_similarity = new ArrayList<>();
    ArrayList<Double> c_similarity = new ArrayList<>();  
    ArrayList<Double> p_similarityFT = new ArrayList<>();
    ArrayList<Double> r_similarityFT = new ArrayList<>();
    ArrayList<Double> f_similarityFT = new ArrayList<>();
    ArrayList<Double> c_similarityFT = new ArrayList<>(); 
    ArrayList<Double> p_similarityTT = new ArrayList<>();
    ArrayList<Double> r_similarityTT = new ArrayList<>();
    ArrayList<Double> f_similarityTT = new ArrayList<>();
    ArrayList<Double> c_similarityTT = new ArrayList<>(); 
    ArrayList<Double> p_dbscan = new ArrayList<>();
    ArrayList<Double> r_dbscan = new ArrayList<>();
    ArrayList<Double> f_dbscan = new ArrayList<>();
    ArrayList<Double> c_dbscan = new ArrayList<>();  // number of clusters
    private boolean isMonotonic = false;
    
    int similarity = 0;
    int similarityFT = 0;
    int similarityTT = 0;
    int dbscan = 0;

    ClusteringEvaluator(boolean b) {
        isMonotonic = b;
    }
    
//    File file = new File("clusterEvaluation.txt");
//    FileWriter fw;
//    BufferedWriter bw;

    public void setMonotonic(boolean t) {
        isMonotonic = t;
    }
    public void comparePRF(DirectedGraph<Object, Edge> oracle, String list, ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f, ArrayList<Double> c) throws IOException {
        List<String> clusters = new ArrayList<>();
        double relevantDocuments = oracle.getVertexCount();
        double retrievedDocuments;
        double intersection = 0;
        double precision;
        double recall;
        double fmeasure;
        String[] elements = list.split(" ");
        clusters.addAll(Arrays.asList(elements));
        retrievedDocuments = clusters.size();
        for (String cluster : clusters) {
            boolean computedCluster = false;
//            bw.write("Cluster: " + cluster);
//            bw.newLine();
            for (Object v : oracle.getVertices()) {
                String id = ((Vertex) v).getID();
                if (cluster.contains(id) && !computedCluster) {
                    computedCluster = true;
                    intersection++;
                }
            }
        }
        precision = intersection / retrievedDocuments;
        
        recall = intersection / relevantDocuments;
        fmeasure = 2 * (precision * recall) / (precision + recall);
        
        p.add(precision);
        r.add(recall);
        f.add(fmeasure);
        c.add(retrievedDocuments);

//        bw.write("=========================");
//        bw.newLine();
//        bw.write("Intersection: " + intersection);
//        bw.newLine();
//        bw.write("Retrieved Documents: " + retrievedDocuments);
//        bw.newLine();
//        bw.write("Relevant Documents: " + relevantDocuments);
//        bw.newLine();
//        bw.write("");
//        bw.newLine();
//        bw.write("Precision: " + precision);
//        bw.newLine();
//        bw.write("Recall: " + recall);
//        bw.newLine();
//        bw.write("F-Measure: " + fmeasure);
//        bw.newLine();
    }

    public DirectedGraph<Object, Edge> createOracleGraph(OracleGraph oracleGraph, String typeGraph) {
        if(typeGraph.equalsIgnoreCase("DAG"))
            return oracleGraph.generateDagGraph();
        else if(typeGraph.equalsIgnoreCase("TREE"))
            return oracleGraph.generateTreeGraph();
        else
            return oracleGraph.generateLinearGraph();
    }
    
    public void collapse(OracleGraph oracleGraph, 
            int NUMBER_OF_ORACLE_GRAPHS, 
            int NUMBER_OF_NOISE_GRAPHS, 
            double INITIAL_NOISE_GRAPH_SIZE, 
            double NOISE_INCREASE_NUMBER, 
            int NUMBER_ITERATIONS,
            File file, 
            File fileR,
            String typeGraph,
            double epsMod,
            int s_size,
            int s_increase,
            int qnt) throws IOException {
        int i = 0;
        int j = 0;
        int w = 1;
        int total_similarity = 0;
        int total_dbscan = 0;
        int total_tt = 0;
        int total_ft = 0;
        BufferedWriter bw;
        BufferedWriter bwR;
        
        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        
        if (!fileR.exists()) {
            fileR.createNewFile();
        }

        bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
        bwR = new BufferedWriter(new FileWriter(fileR.getAbsoluteFile()));
        
        System.out.println("DBSCAN EPS MOD: " + epsMod);
        System.out.println("Similarity small cluster definition: " + s_increase);
        System.out.println("Similarity threshold small cluster (multiplicates the normal threshold by): " + s_increase);
        System.out.println("Similarity Threshold (in STD): " + qnt);
        bw.write("DBSCAN EPS: " + epsMod);
        bw.newLine();
        bw.write("Similarity small cluster definition: " + s_increase);
        bw.newLine();
        bw.write("Similarity threshold small cluster (multiplicates the normal threshold by): " + s_increase);
        bw.newLine();
        bw.write("Similarity Threshold (in STD): " + qnt);
        bw.newLine();
        double noiseFactor = INITIAL_NOISE_GRAPH_SIZE;  
        for (w = 0; w < NUMBER_ITERATIONS; w++) {
            System.out.println("Iteration NUMBER #" + w);
            bw.write("==============================================================");
                bw.newLine();
                bw.write("ITERATION NUMBER #" + w);
                bw.newLine();
                DirectedGraph<Object, Edge> oracle;
                oracle = createOracleGraph(oracleGraph, typeGraph);
                bw.write("Oracle size: " + oracle.getVertexCount());
                bw.newLine();
                bw.write("NoiseGraph size: " + oracle.getVertexCount() * noiseFactor);
                bw.newLine();

//                oracle = createOracleGraph(oracleGraph, typeGraph);
            for (j = 1; j <= NUMBER_OF_ORACLE_GRAPHS; j++) {
//                bw.write("==============================================================");
//                bw.newLine();
//                bw.write("ORACLE NUMBER #" + j);
//                bw.newLine();
//                System.out.println("ORACLE NUMBER #" + j);
//                DirectedGraph<Object, Edge> oracle;

                oracle = createOracleGraph(oracleGraph, typeGraph);

//                bw.write("Oracle size: " + oracle.getVertexCount());
//                bw.newLine();
//                bw.write("NoiseGraph size: " + oracle.getVertexCount() * noiseFactor);
//                bw.newLine();
                for (i = 0; i < NUMBER_OF_NOISE_GRAPHS; i++) {
    //                bw.write("==============================================================");
    //                bw.newLine();
    //                bw.write("TEST NUMBER #" + i);
    //                bw.newLine();
                    NoiseGraph instance = new NoiseGraph(oracle, oracleGraph.attribute, isMonotonic);
                    DirectedGraph<Object, Edge> noiseGraph = instance.generateNoiseGraph(noiseFactor, noiseProbability, "" + j + i);

                    // Good when data is sorted
                    SimilarityCollapse(oracleGraph, oracle, noiseGraph, true, false, p_similarity, r_similarity, f_similarity, c_similarity, s_size, s_increase, qnt);
                    // Good when data is monotonic-ish
                    SimilarityCollapse(oracleGraph, oracle, noiseGraph, false, true, p_similarityFT, r_similarityFT, f_similarityFT, c_similarityFT, s_size, s_increase, qnt);
                    // Terrible in all types of data so far
                    SimilarityCollapse(oracleGraph, oracle, noiseGraph, true, true, p_similarityTT, r_similarityTT, f_similarityTT, c_similarityTT, s_size, s_increase, qnt);
                    dbscan(oracleGraph, oracle, noiseGraph, epsMod, p_dbscan, r_dbscan, f_dbscan, c_dbscan);

                }
            }
            printResults(bw, bwR, w);
            bw.newLine();
            bw.write("Similarity wins: " + similarity);
            bw.newLine();
            bw.write("dbscan wins: " + dbscan);
            bw.newLine();
            bw.write("Similarity TF wins: " + similarityFT);
            bw.newLine();
            bw.write("Similarity TT wins: " + similarityTT);
            bw.newLine();
            total_similarity += similarity;
            total_dbscan += dbscan;
            total_ft += similarityFT;
            total_tt += similarityTT;
            similarity = 0;
            dbscan = 0;
            similarityTT = 0;
            similarityFT = 0;
            noiseFactor *= NOISE_INCREASE_NUMBER; 
        }
        
        System.out.println("===============================");
        System.out.println("Final Result");
        System.out.println("Similarity: " + total_similarity);
        System.out.println("dbscan: " + total_dbscan);
        bw.write("===============================");
        bw.newLine();
        bw.write("Final Result");
        bw.newLine();
        bw.write("Similarity (tf): " + total_similarity);
        bw.newLine();
        bw.write("dbscan (ff): " + total_dbscan);
        bw.newLine();
        bw.write("Similarity (ft): " + total_ft);
        bw.newLine();
        bw.write("Similarity (tt): " + total_tt);
        bw.newLine();
        
        bw.close();
        bwR.close();
    }
    
    public void SimilarityCollapse(OracleGraph oracleGraph, 
            DirectedGraph<Object, Edge> oracle, 
            DirectedGraph<Object, Edge> noiseGraph, 
            boolean updateError, 
            boolean verifyWithinCluster,
            ArrayList<Double> p,
            ArrayList<Double> r,
            ArrayList<Double> f, 
            ArrayList<Double> c,
            int minSize, int thresholdIncrease, int qnt) throws IOException {
        
        GraphMatching combiner = configureSimilarityMatcher(oracleGraph, noiseGraph);
        AutomaticInference infer = new AutomaticInference(minSize, thresholdIncrease, qnt);
        String similarity = infer.cluster(noiseGraph, combiner, updateError, verifyWithinCluster);
        comparePRF(oracle, similarity, p, r, f, c);
    }
    
    private GraphMatching configureSimilarityMatcher(OracleGraph oracleGraph, DirectedGraph<Object, Edge> noiseGraph) {
        double std = Utils.std(noiseGraph.getVertices(), oracleGraph.attribute);
        double similarityThreshold = 0.5;
        String defaultError = "0";
        Map<String, AttributeErrorMargin> restrictionList = new HashMap<>();
        AttributeErrorMargin epsilon;
        epsilon = new AttributeErrorMargin(oracleGraph.attribute, "" + std);
        restrictionList.put(oracleGraph.attribute, epsilon);
        return new GraphMatching(restrictionList, similarityThreshold, defaultError, 0);
    }
    
    
    
    public void dbscan(OracleGraph oracleGraph, 
            DirectedGraph<Object, Edge> oracle, 
            DirectedGraph<Object, Edge> noiseGraph, 
            double epsMod, 
            ArrayList<Double> p,
            ArrayList<Double> r,
            ArrayList<Double> f,
            ArrayList<Double> c) throws IOException {
        
        double eps = epsMod;
        Dbscan instance = new Dbscan(noiseGraph, oracleGraph.attribute, eps, 1);
        String dbscan = instance.applyDbscan(); 
        comparePRF(oracle, dbscan, p, r, f, c);
    }
    
    private void printResults(BufferedWriter bw, BufferedWriter bwR, int iteration) throws IOException {
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse (TF)");
        bw.newLine();
        printPrf(p_similarity, r_similarity, f_similarity, c_similarity, bw, bwR, "s", iteration);
        bw.write("=========================");
        bw.newLine();
        bw.write("DBSCAN (FF)");
        bw.newLine();
        printPrf(p_dbscan, r_dbscan, f_dbscan, c_dbscan, bw, bwR, "d", iteration);
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse (FT)");
        bw.newLine();
        printPrf(p_similarityFT, r_similarityFT, f_similarityFT, c_similarityFT, bw, bwR, "ft", iteration);
        bw.write("=========================");
        bw.newLine();
        bw.write("Similarity Collapse (TT)");
        bw.newLine();
        printPrf(p_similarityTT, r_similarityTT, f_similarityTT, c_similarityTT, bw, bwR, "tt", iteration);
        
        checkWinner();
        clearLists(p_similarity, r_similarity, f_similarity, c_similarity);
        clearLists(p_dbscan, r_dbscan, f_dbscan, c_dbscan);
    }
    
    private void printPrf(ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f, ArrayList<Double> c, BufferedWriter bw, BufferedWriter bwR, String name, int iteration) throws IOException {
        String precision = "";
        String recall = "";
        String fmeasure = "";
        String clusters = "";
        precision = "Precision> Mean: " + Utils.mean(Utils.listToDoubleArray(p)) + 
                " / STD:" + Utils.stdev(Utils.listToDoubleArray(p)) + 
                " / Min: " + Utils.minimumValue(Utils.listToDoubleArray(p));
        
        recall = "Recall> Mean: " + Utils.mean(Utils.listToDoubleArray(r)) + 
                " / STD:" + Utils.stdev(Utils.listToDoubleArray(r)) + 
                " / Min: " + Utils.minimumValue(Utils.listToDoubleArray(r));
        
        fmeasure = "F-Measure> Mean: " + Utils.mean(Utils.listToDoubleArray(f)) + 
                " / STD:" + Utils.stdev(Utils.listToDoubleArray(f)) + 
                " / Min: " + Utils.minimumValue(Utils.listToDoubleArray(f));
        clusters = "#Clusters> Mean: " + Utils.mean(Utils.listToDoubleArray(c)) + 
                " / STD:" + Utils.stdev(Utils.listToDoubleArray(c)) + 
                " / Min: " + Utils.minimumValue(Utils.listToDoubleArray(c)) +
                " / Max: " + Utils.maximumValue(Utils.listToDoubleArray(c));
        
        bw.write(precision);
        bw.newLine();
        bw.write(recall);
        bw.newLine();
        bw.write(fmeasure);
        bw.newLine();
        bw.write(clusters);
        bw.newLine();
        bwR.write(printValues(p, "p" + name + iteration));
        bwR.newLine();
        bwR.write(printValues(r, "r" + name + iteration));
        bwR.newLine();
        bwR.write(printValues(f, "f" + name + iteration));
        bwR.newLine();
        bwR.newLine();
        
//        System.out.println(precision);
//        System.out.println(recall);
//        System.out.println(fmeasure);
        
    }
    
    private String printValues (ArrayList<Double> v, String type) {
        DecimalFormat df = new DecimalFormat("#.###"); 
        String values = type + " <- c(";
        for(Double e : v) {
            values += Double.valueOf(df.format(e)) + ",";
        }
        values = values.substring(0, values.length()-1);
        values += ")";
        return values;
    }
    public void clearLists(ArrayList<Double> p, ArrayList<Double> r, ArrayList<Double> f, ArrayList<Double> c) {
        p.clear();
        r.clear();
        f.clear();
        c.clear();
    }
    
    private void checkWinner() {
        countWinnings(f_similarity, f_dbscan, f_similarityFT, f_similarityTT);
    }
    
    public int isWinner(ArrayList<Double> first, ArrayList<Double> second) {
        int win = 0;
//        if (Utils.mean(Utils.listToDoubleArray(first)) > Utils.mean(Utils.listToDoubleArray(second))) {
//            if (Utils.mean(Utils.listToDoubleArray(first)) > Utils.mean(Utils.listToDoubleArray(third))) {
//                    win = 1;
//            }
//        }
        for (int i = 0; i < first.size(); i++) {
            if(first.get(i) >= second.get(i)) {
                        win += 1;
            }
        }
        return win;
    }
    
    public void countWinnings(ArrayList<Double> sim, ArrayList<Double> db, ArrayList<Double> ft, ArrayList<Double> tt) {
        for (int i = 0; i < sim.size(); i++) {
            
            if((sim.get(i) >= db.get(i)) 
                    && (sim.get(i) >= ft.get(i))
                    && (sim.get(i) >= tt.get(i))) {
                similarity += 1;
            }
            if((db.get(i) >= sim.get(i)) 
                    && (db.get(i) >= ft.get(i))
                    && (db.get(i) >= tt.get(i))) {
                dbscan += 1;
            }
            if((ft.get(i) >= db.get(i)) 
                    && (ft.get(i) >= sim.get(i))
                    && (ft.get(i) >= tt.get(i))) {
                similarityFT += 1;
            }
            if((tt.get(i) >= db.get(i)) 
                    && (tt.get(i) >= ft.get(i))
                    && (tt.get(i) >= sim.get(i))) {
                similarityTT += 1;
            }
            
        }
    }
    
}
