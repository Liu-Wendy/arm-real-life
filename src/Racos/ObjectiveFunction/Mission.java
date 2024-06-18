package Racos.ObjectiveFunction;

import MPC.Automata;
import MPC.Location;
import MPC.Transition;
import Racos.Componet.Dimension;
import Racos.Componet.Instance;
import Racos.Tools.ValueArc;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Mission implements Task{
    private Dimension dim;//the number of all parameters
    public ArrayList<Automata> automatas;
    private FelEngine fel;
    private FelContext ctx;
    public ValueArc valueArc;
    //private int []path = new int[]{1,2,3,4,5,6,7,8,9};
    private double XtarMAX;
    private double RtarMAX;
    private double VMAX;
    private ArrayList<Double> Time1;
    private ArrayList<Double> Time2;
    //private ArrayList<HashMap<String,Double>> allParametersValues1;
    //private ArrayList<HashMap<String,Double>> allParametersValues2;
    ArrayList<String> commands;
    int matanum=2;
    public double delta=0.1;
    BufferedWriter bufferedWriter1;
    BufferedWriter bufferedWriter2;
    double[] timeProfile = new double[3];
    double[] singletimeProfile = new double[3];
    private boolean sat = true;
    private double globalPenalty = 0;
    private double cerr = 0.01;
    private double penalty = 0;
    boolean log_flag=true;
    InverseSolution IS;
    Random random=new Random();
    int stepnum=0;

    int paramSize = 37;// one dimension for choosing path. Each command needs 9 arguments 4*9=36.
    int CommandSize = 6;
    ArrayList<ArrayList<String>>PathMap=new ArrayList<>();
    ArrayList<String> commands1=new ArrayList<>();
    ArrayList<String> commands2=new ArrayList<>();
    private int []path1;
    private int []path2;

    HashMap<Integer,int[]> encodeMap;
    int choiceSize=39;



    public Mission(ArrayList<Automata> automatas){
        this.automatas=automatas;
        IS=new InverseSolution();
        Time1 =new ArrayList<>();
        Time2 =new ArrayList<>();
        XtarMAX=210;
        RtarMAX=180;
        VMAX=150;
        createMap();
        dim=new Dimension();
        dim.setSize(paramSize);

        int index=0;
        while(index<paramSize){
            if(index<1){
                dim.setDimension(index,0,1520,false);
                index++;
            }else{
                for(int j=0;j<CommandSize-2;j++){
                    int upIndex=index+3;
                    for(;index<upIndex;index++){
                        if(index==upIndex-1) dim.setDimension(index,-90,0,true);
//                        else if(index==upIndex-2&&index<19) dim.setDimension(index,0,XtarMAX,true);
                        else dim.setDimension(index,(-1)*XtarMAX,XtarMAX,true);

                        if(index==2) dim.setDimension(index,0,XtarMAX,true);
                    }
                    upIndex=index+3;
                    for(;index<upIndex;index++){
                        dim.setDimension(index,-RtarMAX,RtarMAX,true);
                    }
                    dim.setDimension(index++,16,VMAX,true);
                    dim.setDimension(index++,10,40,true);
                    dim.setDimension(index++,10,40,true);
                }
            }
        }

        fel = new FelEngineImpl();
        ctx = fel.getContext();
        valueArc = new ValueArc();

    }
    private void create(){
        encodeMap=new HashMap<>();
        int encode=0;
        for(int i=0;i<choiceSize;i++){
            for(int j=0;j<choiceSize;j++){
                encodeMap.put(encode,new int[]{i,j});
                encode++;
            }
        }
    }
    private void createMap() {
        create();

        ArrayList<String> tmp=new ArrayList<>();
        tmp.add("fast");
        PathMap.add(tmp);//0

        tmp=new ArrayList<>();
        tmp.add("fast");
        PathMap.add(tmp);//1

        tmp=new ArrayList<>();
        tmp.add("fast");
        PathMap.add(tmp);//2

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//3

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//4

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//5

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//6

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//7

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//8

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//9

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//10

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//11

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//12

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//13

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//14

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//15

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//16

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//17

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//18

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//19

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//20

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//21

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//22

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//23

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//24

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//25

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//26

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//27

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//28

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//29

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//30

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//31

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//32

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//33

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//34

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//35

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//36

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//37

        tmp=new ArrayList<>();
        tmp.add("fast");
        tmp.add("fast");
        tmp.add("fast");
        PathMap.add(tmp);//38

    }
//    private void createMap() {
//        create();
//
//        ArrayList<String> tmp=new ArrayList<>();
//        tmp.add("fast");
//        PathMap.add(tmp);//0
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        PathMap.add(tmp);//1
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        PathMap.add(tmp);//2
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("fast");
//        PathMap.add(tmp);//3
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("forward");
//        PathMap.add(tmp);//4
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//5
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("fast");
//        PathMap.add(tmp);//6
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("forward");
//        PathMap.add(tmp);//7
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//8
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("fast");
//        PathMap.add(tmp);//9
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("forward");
//        PathMap.add(tmp);//10
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//11
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("fast");
//        tmp.add("fast");
//        PathMap.add(tmp);//12
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("fast");
//        tmp.add("forward");
//        PathMap.add(tmp);//13
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("fast");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//14
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("forward");
//        tmp.add("fast");
//        PathMap.add(tmp);//15
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("forward");
//        tmp.add("forward");
//        PathMap.add(tmp);//16
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("forward");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//17
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("doorlike");
//        tmp.add("fast");
//        PathMap.add(tmp);//18
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("doorlike");
//        tmp.add("forward");
//        PathMap.add(tmp);//19
//
//        tmp=new ArrayList<>();
//        tmp.add("fast");
//        tmp.add("doorlike");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//20
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("fast");
//        tmp.add("fast");
//        PathMap.add(tmp);//21
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("fast");
//        tmp.add("forward");
//        PathMap.add(tmp);//22
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("fast");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//23
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("forward");
//        tmp.add("fast");
//        PathMap.add(tmp);//24
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("forward");
//        tmp.add("forward");
//        PathMap.add(tmp);//25
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("forward");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//26
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("doorlike");
//        tmp.add("fast");
//        PathMap.add(tmp);//27
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("doorlike");
//        tmp.add("forward");
//        PathMap.add(tmp);//28
//
//        tmp=new ArrayList<>();
//        tmp.add("forward");
//        tmp.add("doorlike");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//29
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("fast");
//        tmp.add("fast");
//        PathMap.add(tmp);//30
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("fast");
//        tmp.add("forward");
//        PathMap.add(tmp);//31
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("fast");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//32
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("forward");
//        tmp.add("fast");
//        PathMap.add(tmp);//33
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("forward");
//        tmp.add("forward");
//        PathMap.add(tmp);//34
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("forward");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//35
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("doorlike");
//        tmp.add("fast");
//        PathMap.add(tmp);//36
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("doorlike");
//        tmp.add("forward");
//        PathMap.add(tmp);//37
//
//        tmp=new ArrayList<>();
//        tmp.add("doorlike");
//        tmp.add("doorlike");
//        tmp.add("doorlike");
//        PathMap.add(tmp);//38
//
//    }

    public double[] getinsTime(){return timeProfile;}

    public void println1(String str) {
        try {
            bufferedWriter1.write(str + "\n");
        } catch (IOException e) {
            System.out.println("write to file error!");
        }
    }
    public void println2(String str) {
        try {
            bufferedWriter2.write(str + "\n");
        } catch (IOException e) {
            System.out.println("write to file error!");
        }
    }

    public void print1(String str) {
        try {
            bufferedWriter1.write(str);
        } catch (IOException e) {
            System.out.println("write to file error!");
        }
    }
    public void print2(String str) {
        try {
            bufferedWriter2.write(str);
        } catch (IOException e) {
            System.out.println("write to file error!");
        }
    }

    @Override
    public double getValue(Instance ins) {
        double[] x={117.0,-63.48,-602.9,-164.62,0,0,0,10,10,10,1,2,3,4,5,6,7,8,9};
//        double[] x={120.0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27};
        ins.setFeature(x,1);
        setAutomataByins(ins);

        //allParametersValues1 = new ArrayList<>();
        //allParametersValues2 = new ArrayList<>();
        double []args1 = new double[commands1.size()];
        double []args2 = new double[commands2.size()];
        double temp1=0,temp2=0;
        for(int i=0;i< commands1.size();i++){
            args1[i]=Time1.get(i)+temp1;
            temp1=args1[i];
        }
        for(int i=0;i< commands2.size();i++){
            args2[i]=Time2.get(i)+temp2;
            temp2=args2[i];
        }
        sat=true;
        globalPenalty = 0;
        penalty=0;
        stepnum=0;
        for(int i=0;i<3;i++){
            singletimeProfile[i]=0;
        }
        if(true){
            int index=1;
            System.out.println("ARM1:");
            double[] last4arm1={208.67,0,229.72};

            for(int ii=0;ii<commands1.size();ii++){
                System.out.print(commands1.get(ii)+": ");
                if(ii==commands1.size()-1){
                    System.out.println("60 240 180");
                }
                else {
                    double[] currPos=new double[3];
                    for(int l=0;l<3;l++){
                        currPos[l]=last4arm1[l]+ins.getFeature(index+l);
                        System.out.print(currPos[l]+" ");
                        last4arm1[l]=currPos[l];
                    }
//                    System.out.print((last4arm1[0]+ins.getFeature(index)) + " " + ins.getFeature(index + 1) + " " + ins.getFeature(index + 2));

                    switch (commands1.get(ii)) {
                        case "forward" -> System.out.println(" " + ins.getFeature(index+3 + 3));
                        case "doorlike" -> System.out.println(" " + ins.getFeature(index+3 + 4));
                        default -> System.out.println(" ");
                    }
                    index+=9;
                }
            }
            double[] last4arm2={208.67,400,229.72};
            System.out.println("ARM2:");
            for(int ii=0;ii< commands2.size();ii++){
                System.out.print(commands2.get(ii)+": ");
                if(ii==commands2.size()-1){
                    System.out.println("10 -225 180");
                }
                else {
                    double[] currPos=new double[3];
                    for(int l=0;l<3;l++){
                        currPos[l]=last4arm1[l]+ins.getFeature(index+l);
                        System.out.print(currPos[l]+" ");
                        last4arm1[l]=currPos[l];
                    }
//                    System.out.print(ins.getFeature(index) + " " + ins.getFeature(index + 1) + " " + ins.getFeature(index + 2));

                    switch (commands2.get(ii)) {
                        case "forward" -> System.out.println(" " + ins.getFeature(index+3 + 3));
                        case "doorlike" -> System.out.println(" " + ins.getFeature(index+3 + 4));
                        default -> System.out.println(" ");
                    }
                    index+=9;
                }
            }
//
//            int index=1;
//            System.out.println("ARM1:");
//
//            for(int ii=0;ii<commands1.size();ii++){
//                System.out.print(commands1.get(ii)+": ");
//                if(ii==commands1.size()-1){
//                    System.out.println("10 100 30");
//                }
//                else {
//                    System.out.print(ins.getFeature(index) + " " + ins.getFeature(index + 1) + " " + ins.getFeature(index + 2));
//
//                    switch (commands1.get(ii)) {
//                        case "forward" -> System.out.println(" " + ins.getFeature(index + 6));
//                        case "doorlike" -> System.out.println(" " + ins.getFeature(index + 7));
//                        default -> System.out.println(" ");
//                    }
//                    index+=9;
//                }
//            }
//            System.out.println("ARM2:");
//            for(int ii=0;ii<commands2.size();ii++){
//                System.out.print(commands2.get(ii)+": ");
//                if(ii==commands2.size()-1){
//                    System.out.println("10 -100 30");
//                }
//                else {
//                    System.out.print(ins.getFeature(index) + " " + ins.getFeature(index + 1) + " " + ins.getFeature(index + 2));
//
//                    switch (commands2.get(ii)) {
//                        case "forward" -> System.out.println(" " + ins.getFeature(index + 6));
//                        case "doorlike" -> System.out.println(" " + ins.getFeature(index + 7));
//                        default -> System.out.println(" ");
//                    }
//                    index+=9;
//                }
//            }



        }
        try {
            bufferedWriter1 = new BufferedWriter(new FileWriter("log1.txt"));
            bufferedWriter2 = new BufferedWriter(new FileWriter("log2.txt"));
        }catch (IOException e) {
            System.out.println("Open output.txt fail!");
        }
        try{
            checkInvarientsByODE(args1,args2);
        } finally {
            try {
                if (bufferedWriter1 != null)
                    bufferedWriter1.close();

                if (bufferedWriter2 != null)
                    bufferedWriter2.close();
            } catch (IOException ex) {
                System.err.format("IOException: %s%n", ex);
            }
        }
        if(!sat) {
            if(penalty + globalPenalty == 0){
                //todo cfg file should have brackets
                System.out.println("penalty = 0 when unsat");
                System.exit(0);
            }
            double penAll = penalty + globalPenalty;
            if(penAll < valueArc.penAll) {
                valueArc.penalty = penalty;
                valueArc.globalPenalty = globalPenalty;
                valueArc.penAll = penAll;
            }
            return penAll;
        }
        return Math.max(args1[path1.length-1],args2[path2.length-1]);
        //return computeValue(ins);
        //return valueArc.value;
    }
    public double computeValue(Instance ins){
        /*HashMap<String,Double> map = allParametersValues.get(allParametersValues.size() - 1);
        for(HashMap.Entry<String,Double> entry : map.entrySet()){
            ctx.set(entry.getKey(),entry.getValue());
        }
        ctx.set("target_x",automata.target_x);
        ctx.set("target_y",automata.target_y);
        Object obj = fel.eval(automata.obj_function);
        double value = 0;
        if(obj instanceof Double)
            value = (double)obj - 10000;
        else if(obj instanceof Integer){
            value = (int) obj - 10000;
        }
        else {
            System.err.println("error: result not of double!");
            System.out.println(obj);
            System.exit(-1);
        }*/
        double value = 0;
        if (value + 10000 < 0){
            System.exit(0);
        }
        if(value < valueArc.value){
            valueArc.value = value;
            //valueArc.allParametersValues = allParametersValues1.get(allParametersValues1.size()-1);
            valueArc.args = ins.getFeature();
        }
        return valueArc.value;
    }


    public HashMap<String, Double> cloneAllInitParametersValues(int i){
        HashMap<String, Double> newMap = new HashMap<>();

        for (Map.Entry<String, Double> entry : automatas.get(i).initParameterValues.entrySet()) {
            if(i==0)
                newMap.put("a1_"+entry.getKey(), entry.getValue());
            else newMap.put("a2_"+entry.getKey(), entry.getValue());
        }

        return newMap;

    }

    public void checkAutomata(HashMap<String,Double> parametersValues,int index,int locIndex,int[] path){
        for(HashMap.Entry<String,Double> entry : parametersValues.entrySet()){
            ctx.set(entry.getKey(),entry.getValue());
        }

        for (int i = 0; i < automatas.get(index).locations.get(path[locIndex]).invariants.size(); ++i) {
            String expression=index==0?"a1_"+automatas.get(index).locations.get(path[locIndex]).invariants.get((i)):"a2_"+automatas.get(index).locations.get(path[locIndex]).invariants.get((i));
            boolean result = (boolean) fel.eval(expression);
            if (!result) {
                String invariant = index==0?"a1_"+automatas.get(index).locations.get(path[locIndex]).invariants.get(i):"a2_"+automatas.get(index).locations.get(path[locIndex]).invariants.get(i);
                if (computePenalty(invariant, false) < cerr)
                    continue;
                if (Double.isNaN(computePenalty(invariant, false))) {
                    sat = false;
                    penalty += 100000;
                } else {
                    sat = false;
                    //System.out.println(invariant);
                    penalty += computePenalty(invariant, false);
                }
            }
        }

    }
    public void updateFastInfo(HashMap<String,Double> newMap1,int locIndex1,HashMap<String,Double> newMap2,int locIndex2,boolean flag) {
        double res;
        if (locIndex1 < path1.length && automatas.get(0).locations.get(path1[locIndex1]).name.contains("fast")) {
            double A1_THETA1 = Math.toRadians(newMap1.get("a1_theta1"));
            double A1_THETA2 = Math.toRadians(newMap1.get("a1_theta2"));
            double A1_THETA3 = Math.toRadians(newMap1.get("a1_theta3"));
            double A1_THETA4 = Math.toRadians(newMap1.get("a1_theta4"));
            double A1_THETA5 = Math.toRadians(newMap1.get("a1_theta5"));
            double A1_THETA6 = Math.toRadians(newMap1.get("a1_theta6"));
            //double x=Math.sin(A1_THETA1)*(-25.28*Math.cos(A1_THETA5)*Math.sin(A1_THETA4)-10*Math.cos(A1_THETA6)*Math.sin(A1_THETA4)*Math.sin(A1_THETA5)-10*Math.cos(A1_THETA4)*Math.sin(A1_THETA6))+Math.cos(A1_THETA1)*(29.69+Math.sin(A1_THETA3)*(108.+Math.sin(A1_THETA3)*(-168.98-10*Math.cos(A1_THETA5)*Math.cos(A1_THETA6)+25.28*Math.sin(A1_THETA5))+Math.cos(A1_THETA3)*(20+Math.cos(A1_THETA4)*(-25.28*Math.cos(A1_THETA5)-10*Math.cos(A1_THETA6)*Math.sin(A1_THETA5))+10*Math.sin(A1_THETA4)*Math.sin(A1_THETA6)))+Math.cos(A1_THETA3)*(Math.cos(A1_THETA3)*(168.98+10*Math.cos(A1_THETA5)*Math.cos(A1_THETA6)-25.28*Math.sin(A1_THETA5))+Math.sin(A1_THETA3)*(20+Math.cos(A1_THETA4)*(-25.28*Math.cos(A1_THETA5)-10*Math.cos(A1_THETA6)*Math.sin(A1_THETA5))+10*Math.sin(A1_THETA4)*Math.sin(A1_THETA6))))
            double x = Math.sin(A1_THETA1) * (-25.28 * Math.cos(A1_THETA5) * Math.sin(A1_THETA4) - 10 * Math.cos(A1_THETA6) * Math.sin(A1_THETA4) * Math.sin(A1_THETA5) - 10 * Math.cos(A1_THETA4) * Math.sin(A1_THETA6)) + Math.cos(A1_THETA1) * (29.69 + Math.sin(A1_THETA2) * (108. + Math.sin(A1_THETA3) * (-168.98 - 10 * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) + Math.cos(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10 * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10 * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))) + Math.cos(A1_THETA2) * (Math.cos(A1_THETA3) * (168.98 + 10 * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) - 25.28 * Math.sin(A1_THETA5)) + Math.sin(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10 * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10 * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))));
            double y = Math.cos(A1_THETA1) * (25.28 * Math.cos(A1_THETA5) * Math.sin(A1_THETA4) + 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA4) * Math.sin(A1_THETA5) + 10. * Math.cos(A1_THETA4) * Math.sin(A1_THETA6)) + Math.sin(A1_THETA1) * (29.69 + Math.sin(A1_THETA2) * (108. + Math.sin(A1_THETA3) * (-168.98 - 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) + Math.cos(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10. * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))) + Math.cos(A1_THETA2) * (Math.cos(A1_THETA3) * (168.98 + 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) - 25.28 * Math.sin(A1_THETA5)) + Math.sin(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10. * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))));
            double z = 127. - 20. * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) + 25.28 * Math.cos(A1_THETA4) * Math.cos(A1_THETA5) * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) + 10. * Math.cos(A1_THETA4) * Math.cos(A1_THETA6) * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) * Math.sin(A1_THETA5) + Math.cos(A1_THETA3) * Math.sin(A1_THETA2) * (-168.98 - 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) - 10. * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) * Math.sin(A1_THETA4) * Math.sin(A1_THETA6) + Math.cos(A1_THETA2) * (108. + Math.sin(A1_THETA3) * (-168.98 - 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) + Math.cos(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10. * Math.sin(A1_THETA4) * Math.sin(A1_THETA6)));

            if(flag){
                System.out.print("Current theta:");
                for(int i=0;i<6;i++){
                    System.out.print(" "+newMap1.get(("a1_theta"+(i+1))));
                }
                System.out.println(" ");

                System.out.println("Current position:"+x+" "+y+" "+z);
            }
            newMap1.put("a1_x1", x);
            newMap1.put("a1_x2", y);
            newMap1.put("a1_x3", z);
        }

        if (locIndex2 < path2.length && automatas.get(1).locations.get(path2[locIndex2]).name.contains("fast")) {
            double A2_THETA1 = Math.toRadians(newMap2.get("a2_theta1"));
            double A2_THETA2 = Math.toRadians(newMap2.get("a2_theta2"));
            double A2_THETA3 = Math.toRadians(newMap2.get("a2_theta3"));
            double A2_THETA4 = Math.toRadians(newMap2.get("a2_theta4"));
            double A2_THETA5 = Math.toRadians(newMap2.get("a2_theta5"));
            double A2_THETA6 = Math.toRadians(newMap2.get("a2_theta6"));

            double x = Math.sin(A2_THETA1) * (-25.28 * Math.cos(A2_THETA5) * Math.sin(A2_THETA4) - 10 * Math.cos(A2_THETA6) * Math.sin(A2_THETA4) * Math.sin(A2_THETA5) - 10 * Math.cos(A2_THETA4) * Math.sin(A2_THETA6)) + Math.cos(A2_THETA1) * (29.69 + Math.sin(A2_THETA2) * (108. + Math.sin(A2_THETA3) * (-168.98 - 10 * Math.cos(A2_THETA5) * Math.cos(A2_THETA6) + 25.28 * Math.sin(A2_THETA5)) + Math.cos(A2_THETA3) * (20. + Math.cos(A2_THETA4) * (-25.28 * Math.cos(A2_THETA5) - 10 * Math.cos(A2_THETA6) * Math.sin(A2_THETA5)) + 10 * Math.sin(A2_THETA4) * Math.sin(A2_THETA6))) + Math.cos(A2_THETA2) * (Math.cos(A2_THETA3) * (168.98 + 10 * Math.cos(A2_THETA5) * Math.cos(A2_THETA6) - 25.28 * Math.sin(A2_THETA5)) + Math.sin(A2_THETA3) * (20. + Math.cos(A2_THETA4) * (-25.28 * Math.cos(A2_THETA5) - 10 * Math.cos(A2_THETA6) * Math.sin(A2_THETA5)) + 10 * Math.sin(A2_THETA4) * Math.sin(A2_THETA6))));
            double y = Math.cos(A2_THETA1) * (25.28 * Math.cos(A2_THETA5) * Math.sin(A2_THETA4) + 10. * Math.cos(A2_THETA6) * Math.sin(A2_THETA4) * Math.sin(A2_THETA5) + 10. * Math.cos(A2_THETA4) * Math.sin(A2_THETA6)) + Math.sin(A2_THETA1) * (29.69 + Math.sin(A2_THETA2) * (108. + Math.sin(A2_THETA3) * (-168.98 - 10. * Math.cos(A2_THETA5) * Math.cos(A2_THETA6) + 25.28 * Math.sin(A2_THETA5)) + Math.cos(A2_THETA3) * (20. + Math.cos(A2_THETA4) * (-25.28 * Math.cos(A2_THETA5) - 10. * Math.cos(A2_THETA6) * Math.sin(A2_THETA5)) + 10. * Math.sin(A2_THETA4) * Math.sin(A2_THETA6))) + Math.cos(A2_THETA2) * (Math.cos(A2_THETA3) * (168.98 + 10. * Math.cos(A2_THETA5) * Math.cos(A2_THETA6) - 25.28 * Math.sin(A2_THETA5)) + Math.sin(A2_THETA3) * (20. + Math.cos(A2_THETA4) * (-25.28 * Math.cos(A2_THETA5) - 10. * Math.cos(A2_THETA6) * Math.sin(A2_THETA5)) + 10. * Math.sin(A2_THETA4) * Math.sin(A2_THETA6))));
            double z = 127. - 20. * Math.sin(A2_THETA2) * Math.sin(A2_THETA3) + 25.28 * Math.cos(A2_THETA4) * Math.cos(A2_THETA5) * Math.sin(A2_THETA2) * Math.sin(A2_THETA3) + 10. * Math.cos(A2_THETA4) * Math.cos(A2_THETA6) * Math.sin(A2_THETA2) * Math.sin(A2_THETA3) * Math.sin(A2_THETA5) + Math.cos(A2_THETA3) * Math.sin(A2_THETA2) * (-168.98 - 10. * Math.cos(A2_THETA5) * Math.cos(A2_THETA6) + 25.28 * Math.sin(A2_THETA5)) - 10. * Math.sin(A2_THETA2) * Math.sin(A2_THETA3) * Math.sin(A2_THETA4) * Math.sin(A2_THETA6) + Math.cos(A2_THETA2) * (108. + Math.sin(A2_THETA3) * (-168.98 - 10. * Math.cos(A2_THETA5) * Math.cos(A2_THETA6) + 25.28 * Math.sin(A2_THETA5)) + Math.cos(A2_THETA3) * (20. + Math.cos(A2_THETA4) * (-25.28 * Math.cos(A2_THETA5) - 10. * Math.cos(A2_THETA6) * Math.sin(A2_THETA5)) + 10. * Math.sin(A2_THETA4) * Math.sin(A2_THETA6)));

            newMap2.put("a2_x1", x);
            newMap2.put("a2_x2", y);
            newMap2.put("a2_x3", z);

        }
    }

    public void updateForwardInfo(HashMap<String, Double> newMap, int locIndex, int autoIndex) {
        if(autoIndex==0){
            if(automatas.get(0).locations.get(locIndex+1).name.contains("forward_period1")){
                InverseSolution IS = new InverseSolution();

            }
        }
    }
    public boolean checkInvarientsByODE(double []args1,double []args2){
        double end1 = 0,end2=0;
        HashMap<String,Double> newMap1 = cloneAllInitParametersValues(0);
        HashMap<String,Double> newMap2 = cloneAllInitParametersValues(1);
        double totalTime1=args1[args1.length-1];
        double totalTime2=args2[args2.length-1];
        double step1 = 0,step2=0;
        int locIndex1=0,locIndex2=0;
        boolean gotoNextLoc1=false,gotoNextLoc2=false;
        updateFastInfo(newMap1,locIndex1,newMap2,locIndex2,false);
        if(log_flag){
            logxyz(newMap1,1);
            logxyz(newMap2,2);
        }
        while(step1<totalTime1&&step2<totalTime2){

            double delta1=delta;
            if(newMap1.get("a1_t_current")+delta1>args1[locIndex1] || newMap2.get("a2_t_current")+delta1>args2[locIndex2]){
                double tmp1=args1[locIndex1]-newMap1.get("a1_t_current");
                double tmp2=args2[locIndex2]-newMap2.get("a2_t_current");
                if(tmp1>tmp2){
                    delta1=tmp2;
                    gotoNextLoc2=true;
                }else if(tmp1==tmp2){
                    delta1=tmp1;
                    gotoNextLoc1=true;
                    gotoNextLoc2=true;
                } else {
                    delta1=tmp1;
                    gotoNextLoc1=true;
                }
            }
            if(delta1>0.0001) {
                stepnum++;
                //System.out.println(stepnum+":");
                double currentTime = System.currentTimeMillis();
                newMap1 = computeValuesByFlow(newMap1, 0, locIndex1, delta1, path1);
                newMap2 = computeValuesByFlow(newMap2, 1, locIndex2, delta1, path2);
                double endTime = System.currentTimeMillis();
                double temptime=(endTime - currentTime) / 1000;
                //System.out.println("\t computing by flow costs: "+temptime);
                timeProfile[0] +=temptime;
                singletimeProfile[0]+=temptime;

                currentTime = System.currentTimeMillis();
                boolean f=false;
                if(stepnum%10==0) f=true;
                updateFastInfo(newMap1, locIndex1, newMap2, locIndex2,f);//fast mode need to calculate X
                endTime = System.currentTimeMillis();
                temptime=(endTime - currentTime) / 1000;
                //System.out.println("\t calculating X costs: "+temptime);
                timeProfile[1] += temptime;
                singletimeProfile[1] += temptime;
                //check forbidden
                currentTime = System.currentTimeMillis();
                checkConstraints(newMap1, newMap2);
                endTime = System.currentTimeMillis();
                temptime=(endTime - currentTime) / 1000;
                //System.out.println("\t checking forbidden costs: "+temptime);
                timeProfile[2] += temptime;
                singletimeProfile[2] += temptime;

                //check Automata1
                //checkAutomata(newMap1, 0, locIndex1);
                //checkAutomata(newMap2, 1, locIndex2);
                logxyz(newMap1,1);
                logxyz(newMap2,2);
                /*if(locIndex1<3||locIndex1>5)
                    logxyz(newMap1,1);
                if(locIndex2<3||locIndex2>5)
                    logxyz(newMap2,2);*/
            }

            if(gotoNextLoc1){
                locIndex1++;
                //allParametersValues1.add(newMap1);
                gotoNextLoc1=false;
                if(locIndex1< path1.length-1) {
                    Transition transition = automatas.get(0).getTransitionBySourceAndTarget(path1[locIndex1-1], path1[locIndex1]);
                    if (transition == null) {
                        System.out.println("Found no transition");
                        System.exit(-1);
                    }
                    for (HashMap.Entry<String, String> entry : transition.assignments.entrySet()) {
                        Object obj = fel.eval(entry.getValue());
                        double result = 0;
                        if (obj instanceof Integer) result = (int) obj;
                        else if (obj instanceof Double) result = (double) obj;
                        else {
                            System.out.println("Not Double and Not Integer!");
                            System.out.println(entry.getValue());
                            System.exit(0);
                        }

                        newMap1.put("a1_"+entry.getKey(), result);
                    }
                }

            }
            if(gotoNextLoc2){
                locIndex2++;
                //allParametersValues2.add(newMap2);
                gotoNextLoc2=false;
                if(locIndex2<path2.length-1) {
                    Transition transition = automatas.get(1).getTransitionBySourceAndTarget(path2[locIndex2-1 ], path2[locIndex2]);
                    if (transition == null) {
                        System.out.println("Found no transition");
                        System.exit(-1);
                    }
                    for (HashMap.Entry<String, String> entry : transition.assignments.entrySet()) {
                        Object obj = fel.eval(entry.getValue());
                        double result = 0;
                        if (obj instanceof Integer) result = (int) obj;
                        else if (obj instanceof Double) result = (double) obj;
                        else {
                            System.out.println("Not Double and Not Integer!");
                            System.out.println(entry.getValue());
                            System.exit(0);
                        }
                        newMap2.put("a2_"+entry.getKey(), result);
                    }
                }
            }
            step1+=delta1;
            step2+=delta1;
            /*System.out.println(stepnum+":");
            System.out.println("\t computing by flow costs: "+timeProfile[0]);
            System.out.println("\t computing by flow costs: "+timeProfile[0]);
            System.out.println("\t computing by flow costs: "+timeProfile[0]);*/

        }
        while(step1<totalTime1){

            double delta1=delta;
            if(newMap1.get("a1_t_current")+delta1>args1[locIndex1]){
                delta1=args1[locIndex1]-newMap1.get("a1_t_current");
                gotoNextLoc1=true;
            }
            if(delta1>0.0001) {
                stepnum++;
                //System.out.println(stepnum+":");
                double currentTime = System.currentTimeMillis();
                newMap1 = computeValuesByFlow(newMap1, 0, locIndex1, delta1, path1);
                double endTime = System.currentTimeMillis();
                double temptime= (endTime - currentTime) / 1000;
                //System.out.printf("\t computing by flow costs: %.8f \n",temptime);
                timeProfile[0] +=temptime;
                singletimeProfile[0] += temptime;

                currentTime = System.currentTimeMillis();
                boolean f=false;
                if(stepnum%10==0) f=true;
                updateFastInfo(newMap1, locIndex1, newMap2, locIndex2,f);
//                updateFastInfo(newMap1, locIndex1, newMap2, locIndex2,false);
                endTime = System.currentTimeMillis();
                temptime=(endTime - currentTime) / 1000;
                //System.out.println("\t calculating X costs: "+temptime);
                timeProfile[1] += temptime;
                singletimeProfile[1] += temptime;

                //check forbidden
                currentTime = System.currentTimeMillis();
                checkConstraints(newMap1, newMap2);
                endTime = System.currentTimeMillis();
                temptime=(endTime - currentTime) / 1000;
                //System.out.println("\t checking forbidden costs: "+temptime);
                timeProfile[2] += temptime;
                singletimeProfile[2] += temptime;

                //checkAutomata(newMap1, 0, locIndex1);
                //if(locIndex1<3||locIndex1>5)
                    logxyz(newMap1,1);
            }
            if(gotoNextLoc1){
                locIndex1++;
                //allParametersValues1.add(newMap1);
                gotoNextLoc1=false;

                if(locIndex1< path1.length) {
                    Transition transition = automatas.get(0).getTransitionBySourceAndTarget(path1[locIndex1 - 1], path1[locIndex1]);
                    if (transition == null) {
                        System.out.println("Found no transition");
                        System.exit(-1);
                    }
                    for (HashMap.Entry<String, String> entry : transition.assignments.entrySet()) {
                        Object obj = fel.eval(entry.getValue());
                        double result = 0;
                        if (obj instanceof Integer) result = (int) obj;
                        else if (obj instanceof Double) result = (double) obj;
                        else {
                            System.out.println("Not Double and Not Integer!");
                            System.out.println(entry.getValue());
                            System.exit(0);
                        }

                        newMap1.put("a1_"+entry.getKey(), result);
                    }
                }
            }
            step1+=delta1;

        }
        while(step2<totalTime2){

            double delta1=delta;
            if(newMap2.get("a2_t_current")+delta1>args2[locIndex2]){
                delta1=args2[locIndex2]-newMap2.get("a2_t_current");
                gotoNextLoc2=true;
            }
            if(delta1>0.0001) {
                stepnum++;
                //System.out.println(stepnum+":");
                double currentTime = System.currentTimeMillis();
                newMap2 = computeValuesByFlow(newMap2, 1, locIndex2, delta1, path2);
                double endTime = System.currentTimeMillis();
                double temptime= (endTime - currentTime) / 1000;
                //System.out.println("\t computing by flow costs: "+temptime);
                timeProfile[0] +=temptime;
                singletimeProfile[0] += temptime;

                currentTime = System.currentTimeMillis();
                updateFastInfo(newMap1, locIndex1, newMap2, locIndex2,false);
                endTime = System.currentTimeMillis();
                temptime=(endTime - currentTime) / 1000;
                //System.out.println("\t calculating X costs: "+temptime);
                timeProfile[1] += temptime;
                singletimeProfile[1] += temptime;

                //check forbidden
                currentTime = System.currentTimeMillis();
                checkConstraints(newMap1, newMap2);
                temptime=(endTime - currentTime) / 1000;
                //System.out.println("\t checking forbidden costs: "+temptime);
                timeProfile[2] += temptime;
                singletimeProfile[2] += temptime;
                //checkAutomata(newMap2, 1, locIndex2);

                //if(locIndex2<3||locIndex2>5)
                    logxyz(newMap2,2);
            }
            if(gotoNextLoc2){
                locIndex2++;
                //allParametersValues2.add(newMap2);
                gotoNextLoc2=false;
                if(locIndex2<path2.length) {
                    Transition transition = automatas.get(1).getTransitionBySourceAndTarget(path2[locIndex2 - 1], path2[locIndex2]);
                    if (transition == null) {
                        System.out.println("Found no transition");
                        System.exit(-1);
                    }
                    for (HashMap.Entry<String, String> entry : transition.assignments.entrySet()) {
                        Object obj = fel.eval(entry.getValue());
                        double result = 0;
                        if (obj instanceof Integer) result = (int) obj;
                        else if (obj instanceof Double) result = (double) obj;
                        else {
                            System.out.println("Not Double and Not Integer!");
                            System.out.println(entry.getValue());
                            System.exit(0);
                        }
                        newMap2.put("a2_"+entry.getKey(), result);
                    }
                }
            }
            step2+=delta1;

        }

        return true;
    }

    private void logxyz(HashMap<String, Double> newMap1,int index) {
        if(!log_flag) return;
        if(index==1){
            print1(Double.toString(newMap1.get("a1_x1"))+" ");
            print1(Double.toString(newMap1.get("a1_x2"))+" ");
            println1(Double.toString(newMap1.get("a1_x3")));
        }
        else {
            print2(Double.toString(newMap1.get("a2_x1"))+" ");
            print2(Double.toString(newMap1.get("a2_x2")+400)+" ");
            println2(Double.toString(newMap1.get("a2_x3")));
        }


    }


    public HashMap<String,Double> computeValuesByFlow(HashMap<String,Double> parametersValues,int index,int locIndex,double arg,int[] path){

        HashMap<String,Double> tempMap = (HashMap<String, Double>) parametersValues.clone();
        Location location= automatas.get(index).locations.get(path[locIndex]);
        for(HashMap.Entry<String,Double> entry : parametersValues.entrySet()){
            ctx.set(entry.getKey(),entry.getValue());
        }
        for(HashMap.Entry<String,String> entry:location.flows.entrySet()){
            String expression;
            if(entry.getKey().contains("omega")||entry.getKey().contains("v")){
                if(isdigit(entry.getValue().trim())){
                    expression = entry.getValue() ;
                }
                else expression = index==0? "a1_"+entry.getValue() : "a2_"+entry.getValue() ;

                Object obj = fel.eval(expression);
                double result;
                if(obj instanceof Double)
                    result = (double)obj;
                else if(obj instanceof Integer) {
                    result = (int) obj;
                }
                else if(obj instanceof Long){
                    result = ((Long)obj).doubleValue();
                }
                else {
                    result = 0;
                    System.out.println("Not Double and Not Integer!");
                    System.out.println(obj.getClass().getName());
                    System.out.println(obj);
                    System.out.println(location.flows.get(entry.getKey()));
                    System.exit(0);
                }
                double delta = result * arg;
                String var_name=index==0? "a1_"+entry.getKey() : "a2_"+entry.getKey();
                double res=parametersValues.get(var_name)+delta;
                tempMap.put(var_name,res);
                ctx.set(var_name,res);
            }

        }

        for(HashMap.Entry<String,String> entry:location.flows.entrySet()) {
            String expression;
            if (entry.getKey().contains("omega")||entry.getKey().contains("v")) {
            }else {
                if(isdigit(entry.getValue().trim())){
                    expression=entry.getValue();
                }else expression = index==0? "a1_"+entry.getValue() : "a2_"+entry.getValue() ;
                //double result=tempMap.get(expression);

                try{
                    Object obj = fel.eval(expression);
                }catch (Exception e){
                    System.out.println(expression);
                    System.out.println("error");
                }

                Object obj = fel.eval(expression);
                double result;
                if(obj instanceof Double)
                    result = (double)obj;
                else if(obj instanceof Integer) {
                    result = (int) obj;
                }
                else if(obj instanceof Long){
                    result = ((Long)obj).doubleValue();
                }
                else {
                    result = 0;
                    System.out.println("Not Double and Not Integer!");
                    System.out.println(obj.getClass().getName());
                    System.out.println(obj);
                    System.out.println(location.flows.get(entry.getKey()));
                    System.exit(0);
                }
                double delta = result * arg;
                String var_name=index==0? "a1_"+entry.getKey() : "a2_"+entry.getKey();
                double res=parametersValues.get(var_name) + delta;
                tempMap.put(var_name,res);
                ctx.set(var_name,res);

            }
        }
        if(index==0){
            String s="a1_t_current";
            tempMap.put(s,tempMap.get(s)+arg);
        }else{
            String s="a2_t_current";
            tempMap.put(s,tempMap.get(s)+arg);
        }
        return tempMap;
    }

    public boolean isdigit(String s){
        for(int i=0;i<s.length();i++){
            if((s.charAt(i)>'9'||s.charAt(i)<'0')&&s.charAt(i)!='.'&&s.charAt(i)!='-'&&s.charAt(i)!='E') return false;
        }
        return true;
    }
    public boolean checkConstraints(HashMap<String,Double> map1,HashMap<String,Double> map2){
        double a1_x1=map1.get("a1_x1");
        double a1_x2=map1.get("a1_x2");
        double a1_x3=map1.get("a1_x3");
        double a2_x1=map2.get("a2_x1");
        double a2_x2=map2.get("a2_x2");
        double a2_x3=map2.get("a2_x3");

        if((a1_x1-a2_x1)*(a1_x1-a2_x1)+(a1_x2-a2_x2-400)*(a1_x2-a2_x2-400)+(a1_x3-a2_x3)*(a1_x3-a2_x3)<10){
            sat = false;
            globalPenalty += computeConstraintValue(automatas.get(0).forbiddenConstraints);

            return false;
        }else{

            return true;
        }
        /*for(Map.Entry<String,Double> entry : map1.entrySet()){
            ctx.set(entry.getKey(),entry.getValue());
        }
        for(Map.Entry<String,Double> entry : map2.entrySet()){
            ctx.set(entry.getKey(),entry.getValue());
        }
        if(automatas.get(0).forbiddenConstraints==null)
            return true;
        double currentTime = System.currentTimeMillis();
        boolean result = (boolean)fel.eval(automatas.get(0).forbiddenConstraints);

        double endTime = System.currentTimeMillis();
        instanceTime+=(endTime - currentTime) / 1000;

        if(!result) return true;
        sat = false;
        globalPenalty += computeConstraintValue(automatas.get(0).forbiddenConstraints);
        return false;*/
    }
    public double computeConstraintValue(String constraint){
        int firstRightBracket = constraint.trim().indexOf(")");
        if(firstRightBracket != -1 && constraint.indexOf('&') == -1 && constraint.indexOf('|') == -1)
            //return computePenalty(constraint.substring(constraint.indexOf('(')+1,constraint.lastIndexOf(")")),false);
            return computePenalty(constraint,false);
        if(firstRightBracket != -1 && firstRightBracket != constraint.length()-1){
            for(int i = firstRightBracket;i < constraint.length();++i){
                if(constraint.charAt(i) == '&'){
                    int index = 0;
                    int numOfBrackets = 0;
                    int partBegin = 0;
                    double pen = 0;
                    while(index < constraint.length()){
                        if(constraint.indexOf(index) == '(')
                            ++numOfBrackets;
                        else if(constraint.indexOf(index) == ')')
                            --numOfBrackets;
                        else if(constraint.indexOf(index) == '&' && numOfBrackets==0){
                            String temp = constraint.substring(partBegin,index);
                            boolean result = (boolean)fel.eval(temp);
                            if(!result) return 0;
                            else pen+= computeConstraintValue(temp);
                            index = index + 2;
                            partBegin = index;
                            constraint = constraint.substring(index);
                            continue;
                        }
                        ++index;
                    }
                    return pen;
                }
                else if(constraint.charAt(i) == '|'){
                    int index = 0;
                    int numOfBrackets = 0;
                    int partBegin = 0;
                    double minPen = Double.MAX_VALUE;
                    while(index < constraint.length()){
                        if(constraint.indexOf(index) == '(')
                            ++numOfBrackets;
                        else if(constraint.indexOf(index) == ')')
                            --numOfBrackets;
                        else if(constraint.indexOf(index) == '|' && numOfBrackets==0){
                            String temp = constraint.substring(partBegin,index);
                            boolean result = (boolean)fel.eval(temp);
                            if(result){
                                minPen = (computeConstraintValue(temp) < minPen) ? computeConstraintValue(temp) : minPen;
                            }
                            index = index + 2;
                            partBegin = index;
                            constraint = constraint.substring(index);
                            continue;
                        }
                        ++index;
                    }
                    return minPen;
                }
            }
        }
        else{
            if(firstRightBracket != -1){
                constraint = constraint.substring(constraint.indexOf('(')+1,firstRightBracket);
            }
            if(constraint.indexOf('&') != -1){
                String []strings = constraint.split("&");
                double pen = 0;
                for(int i = 0;i < strings.length;++i){
                    if(strings[i].equals("")) continue;
                    boolean result = (boolean)fel.eval(strings[i]);
                    if(!result) return 0;
                    else pen += computeConstraintValue(strings[i]);
                }
                return pen;
            }
            else if(constraint.indexOf('|') != -1){
                String []strings = constraint.split("\\|");
                double minPen = Double.MAX_VALUE;
                for(int i = 0;i < strings.length;++i){
                    if(strings[i].equals("")) continue;
                    boolean result = (boolean) fel.eval(strings[i]);
                    if(!result) continue;
                    else minPen = (computeConstraintValue(strings[i]) < minPen) ? computeConstraintValue(strings[i]) : minPen;
                }
                return minPen;
            }
            else return computePenalty(constraint,false);
        }
        return 0;
    }
    private double computePenalty(String expression,boolean isConstraint){
        if(isConstraint && expression.indexOf("|") != -1)
            return computePenaltyOfConstraint(expression);

        String []strings;
        String bigPart = "",smallPart = "";
        strings = expression.split("<=|<|>=|>|==");
        Object obj1 = fel.eval(strings[0].trim());
        Object obj2 = fel.eval(strings[1].trim());
        double big = 0,small = 0;
        if(obj1 instanceof Double)
            big = (double)obj1;
        else if(obj1 instanceof Integer) {
            big = (int) obj1;
            //System.out.println(entry.getKey() + " " + entry.getValue());
        }
        else {
            System.out.println("Not Double and Not Integer!");
            System.out.println(expression);
            System.out.println(obj1);
            System.out.println(obj1.getClass().getName());
            System.out.println("here");
            System.exit(0);
        }
        if(obj2 instanceof Double)
            small = (double)obj2;
        else if(obj2 instanceof Integer) {
            small = (int) obj2;
        }
        else if(obj2 instanceof Long){
            small = ((Long)obj2).doubleValue();
        }
        else {
            small = 0;
            System.out.println("Not Double and Not Integer!");
            System.exit(0);
        }
        return Math.abs(big-small);
    }
    private double computePenaltyOfConstraint(String expression){//just one level
        String []expressions = expression.split("\\|");
        double result = Double.MAX_VALUE;
        for(String string:expressions){
            if(string.length()<=0)  continue;
            double temp = computePenalty(string,false);
            result = (temp < result) ? temp : result;
        }
        return result;
    }


    @Override
    public Dimension getDim() {
        return dim;
    }
    public  void setlogFlag(){
        log_flag=true;
    }

    public int setAutomataByins(Instance ins){

        automatas.clear();
        Time1.clear();
        Time2.clear();

        fillinCommands(ins);
        clearAutomata();


        int current_index=1;

        HashMap<String,Double> newMap = new HashMap<>();
        //InverseSolution IS=new InverseSolution();
        double[] X_tar=new double[3];
        double[] R_tar=new double[3];
        double[] current_Theta=new double[6];//current theta
        double[] current_X=new double[3];
        double[] current_R=new double[3];
        double[] delta_X=new double[3];
        double[] delta_R=new double[3];
        double[] theta=new double[6];
        double[] X=new double[3];
        double[] R=new double[3];
        double lastTime=0;

        for(int i=0;i< automatas.size();i++) {
            boolean first_forward=true;
            boolean first_fast=true, first_doorlike=true;
            int locIndex;
            int len=i==0?(commands1.size()-1):(commands2.size()-1);
            lastTime=0;
            for(locIndex = 0;locIndex < len;++locIndex){
                if(locIndex==0){
                    for(int index=0;index<6;index++){
                        current_Theta[index]=automatas.get(i).initParameterValues.get("theta"+Integer.toString(index+1));
                    }
                    //current_X=getFastInfo();
                    for(int index=0;index<3;index++) {
                        current_X[index]=automatas.get(i).initParameterValues.get("x"+Integer.toString(index+1));
                        current_R[index]=automatas.get(i).initParameterValues.get("r"+Integer.toString(index+1));
                    }
                }
                else {
                    current_Theta=theta.clone();
                    current_X=X.clone();
                    current_R=R.clone();
                }
                String name=automatas.get(i).locations.get(locIndex+1).name;

                if(name.contains("fast")){
//                    if(!first_fast) {
//                        if(name.contains("period3")){
//                            first_fast=true;
//                        }
//                        continue;
//                    }

                    X_tar[0]=ins.getFeature(current_index)+current_X[0];
                    X_tar[1]=ins.getFeature(current_index+1)+current_X[1];
                    X_tar[2]=ins.getFeature(current_index+2)+current_X[2];
                    current_index+=3;

//                    R_tar[0]=ins.getFeature(current_index);
//                    R_tar[1]=ins.getFeature(current_index+1);
//                    R_tar[2]=ins.getFeature(current_index+2);
                    R_tar[0]=0;
                    R_tar[1]=-25;
                    R_tar[2]=-90;
                    current_index+=3;

                    while (i==0&&ins.getFeature(current_index+1)<0){
                        int k=1;
                        ins.setFeature(current_index+k, dim.getRegion(current_index+k)[0]+random.nextDouble()*(dim.getRegion(current_index+k)[1]-dim.getRegion(current_index+k)[0]));
                    }

                    //Calculate inverse solution for theta
                    double[] theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                    /*if(theta_tar[0]==1000){
                        no_solution++;
                        if(i==0) return 1;
                        else return 2;
                    }*/
                    while (theta_tar[0]==1000||X_tar[2]<0){
                        current_index-=6;
                        for (int k = 0; k < 6; k++) {
                            ins.setFeature(current_index+k, dim.getRegion(current_index+k)[0]+random.nextDouble()*(dim.getRegion(current_index+k)[1]-dim.getRegion(current_index+k)[0]));
                        }

                        while (i==0&&ins.getFeature(current_index+1)<0){
                            int k=1;
                            ins.setFeature(current_index+k, dim.getRegion(current_index+k)[0]+random.nextDouble()*(dim.getRegion(current_index+k)[1]-dim.getRegion(current_index+k)[0]));
                        }

                        X_tar[0]=ins.getFeature(current_index)+current_X[0];
                        X_tar[1]=ins.getFeature(current_index+1)+current_X[1];
                        X_tar[2]=ins.getFeature(current_index+2)+current_X[2];
                        current_index+=3;

                        R_tar[0]=0;
                        R_tar[1]=-25;
                        R_tar[2]=-90;
                        current_index+=3;

                        //Calculate inverse solution for theta
                        theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                    }
                    if(i==0) {
                        System.out.print("Automaton's Target Theta:");
                        for (int j = 0; j < 6; j++) {
                            System.out.print(theta_tar[j] + " ");
                        }
                        System.out.println(" ");
                    }
                    if(i==1){
                        System.out.println("current x:");
                        for(int p=0;p<3;p++){
                            System.out.print(current_X[p]+" ");
                        }
                    }


                    double[] delta_theta=new double[6];
                    for(int j=0;j<6;j++) {

                        delta_theta[j] = theta_tar[j] - current_Theta[j];
                    }

                    double[] t=IS.Solve_T12(delta_theta);
                    double[] theta_speed_fast=IS.theta_speed;
//                    double[] a_fast=IS.a_fast;
//                    double[] w_fast=IS.w_fast;
                    if(i==0){
                        Time1.add(t[0]);
                    }else {
                        Time2.add(t[0]);
                    }
                    //double[] omega_bar=IS.Solve_omega_bar(current_Theta,theta_tar);
                    //Time.add(IS.Solve_T12(delta_theta));

                    //TODO calculate invarients/flow/guard... with theta_tar and omega_bar-finish
                    setFast(t,lastTime,i,locIndex,delta_theta,theta_speed_fast);

                    theta=theta_tar.clone();
                    X=X_tar.clone();
                    R=R_tar.clone();
                    first_fast=false;
                    lastTime+=t[0];
                    current_index+=3;
//                    System.out.print("fast:  ");
//                    for(int m=0;m<3;m++){
//                        System.out.print(X_tar[m]+"  ");
//                    }
//                    System.out.println(" ");

                }else if(name.contains("forward")){
                    if(!first_forward) {
                        if(name.contains("period3")){
                            first_forward=true;
                        }
                        continue;
                    }
                    //first_forward=true;
                    X_tar[0]=ins.getFeature(current_index)+current_X[0];
                    X_tar[1]=ins.getFeature(current_index+1)+current_X[1];
                    X_tar[2]=ins.getFeature(current_index+2)+current_X[2];
                    current_index+=3;

                    R_tar[0]=ins.getFeature(current_index);
                    R_tar[1]=ins.getFeature(current_index+1);
                    R_tar[2]=ins.getFeature(current_index+2);
                    current_index+=3;

                    double V_tar=ins.getFeature(current_index);
                    current_index+=1;

                    double[] theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                    while (theta_tar[0]==1000||X_tar[2]<0){
                        current_index-=7;
                        for (int k = 0; k < 7; k++) {
                            ins.setFeature(current_index+k, dim.getRegion(current_index+k)[0]+random.nextDouble()*(dim.getRegion(current_index+k)[1]-dim.getRegion(current_index+k)[0]));
                        }

                        X_tar[0]=ins.getFeature(current_index)+current_X[0];
                        X_tar[1]=ins.getFeature(current_index+1)+current_X[1];
                        X_tar[2]=ins.getFeature(current_index+2)+current_X[2];
                        current_index+=3;

                        R_tar[0]=ins.getFeature(current_index);
                        R_tar[1]=ins.getFeature(current_index+1);
                        R_tar[2]=ins.getFeature(current_index+2);
                        current_index+=3;

                        V_tar=ins.getFeature(current_index);
                        current_index+=1;

                        //Calculate inverse solution for theta
                        theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                    }

                    for(int j=0;j<3;j++) {
                        delta_X[j] = X_tar[j] - current_X[j];
                    }
                    double[] t=IS.Solve_T12_forward(delta_X,V_tar);
                    if(i==0){
                        for(int m=0;m<3;m++){
                            Time1.add(t[m]);
                        }
                    }else {
                        for(int m=0;m<3;m++){
                            Time2.add(t[m]);
                        }
                    }
                    //Time.add(IS.Solve_T12_forward(delta_X,V_tar));
                    double[] a_forward=IS.a_forward;
                    double[] v_forward=IS.v_forward;
                    //TODO calculate invarients/flow/guard... with theta_tar and omega_bar
                    setForward(t,lastTime,i,locIndex,theta_tar,a_forward,v_forward);

                    theta=theta_tar.clone();
                    X=X_tar.clone();
                    R=R_tar.clone();
                    first_forward=false;
                    lastTime+=t[2];
                    current_index+=2;

//                    System.out.print("forward:  ");
//                    for(int m=0;m<3;m++){
//                        System.out.print(X_tar[m]+"  ");
//                    }
//                    System.out.println(" ");


                }else if(name.contains("doorlike")){
                    if(!first_doorlike) {
                        if(name.contains("period3")){
                            first_doorlike=true;
                        }
                        continue;
                    }

                    X_tar[0]=ins.getFeature(current_index)+current_X[0];
                    X_tar[1]=ins.getFeature(current_index+1)+current_X[1];
                    X_tar[2]=ins.getFeature(current_index+2)+current_X[2];
                    current_index+=3;

                    R_tar[0]=ins.getFeature(current_index);
                    R_tar[1]=ins.getFeature(current_index+1);
                    R_tar[2]=ins.getFeature(current_index+2);
                    current_index+=4;

                    double Height=ins.getFeature(current_index);
                    current_index+=1;

                    double[] tmp_tar1=new double[]{current_X[0],current_X[1],current_X[2]+Height};
                    double[] tmp_tar2=new double[]{X_tar[0],X_tar[1],X_tar[2]+Height};

                    double[] theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                    while (theta_tar[0]==1000||X_tar[2]<0){
                        current_index-=8;
                        for (int k = 0; k < 7; k++) {
                            ins.setFeature(current_index+k, dim.getRegion(current_index+k)[0]+random.nextDouble()*(dim.getRegion(current_index+k)[1]-dim.getRegion(current_index+k)[0]));
                        }

                        X_tar[0]=ins.getFeature(current_index)+current_X[0];
                        X_tar[1]=ins.getFeature(current_index+1)+current_X[1];
                        X_tar[2]=ins.getFeature(current_index+2)+current_X[2];
                        current_index+=3;

                        R_tar[0]=ins.getFeature(current_index);
                        R_tar[1]=ins.getFeature(current_index+1);
                        R_tar[2]=ins.getFeature(current_index+2);
                        current_index+=4;

                        Height=ins.getFeature(current_index);
                        current_index+=1;

                        tmp_tar1=new double[]{current_X[0],current_X[1],current_X[2]+Height};
                        tmp_tar2=new double[]{X_tar[0],X_tar[1],X_tar[2]+Height};

                        //Calculate inverse solution for theta
                        theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                    }

                    for(int j=0;j<3;j++) {
                        delta_X[j] = tmp_tar2[j] - tmp_tar1[j];
                    }
                    double[] t=IS.Solve_T12_doorlike(delta_X,Height);
                    if(i==0){
                        for(int m=0;m<3;m++){
                            Time1.add(t[m]);
                        }
                    }else {
                        for(int m=0;m<3;m++){
                            Time2.add(t[m]);
                        }
                    }
                    double[] v_doorlike=IS.v_doorlike;
                    setDoorlike(t,lastTime,i,locIndex,theta_tar,v_doorlike, IS.v_max);

                    theta=theta_tar.clone();
                    X=X_tar.clone();
                    R=R_tar.clone();
                    first_doorlike=false;
                    lastTime+=t[2];
                    current_index+=1;
//                    System.out.print("doorlike:  ");
//                    for(int m=0;m<3;m++){
//                        System.out.print(X_tar[m]+"  ");
//                    }
//                    System.out.println(" ");
                }

            }

            //last moving to given target point
            //arm1:[20,50,150,0,-25,-90]arm2:[30,-50,150,0,-25,-90]
            if(len==0){
                for(int index=0;index<6;index++){
                    current_Theta[index]=automatas.get(i).initParameterValues.get("theta"+Integer.toString(index+1));
                }
                //current_X=getFastInfo();
                for(int index=0;index<3;index++) {
                    current_X[index]=automatas.get(i).initParameterValues.get("x"+Integer.toString(index+1));
                    current_R[index]=automatas.get(i).initParameterValues.get("r"+Integer.toString(index+1));
                }
            }
            else {
                current_Theta=theta.clone();
                current_X=X.clone();
                current_R=R.clone();
            }
            String name=automatas.get(i).locations.get(locIndex+1).name;

            if(name.contains("fast")){
                if(i==0) {
                    X_tar[0] = 60;
                    X_tar[1] = 240;
                    X_tar[2] = 180;

                    R_tar[0]=0;
                    R_tar[1]=-25;
                    R_tar[2]=-90;
                }else {
                    X_tar[0] = 10;
                    X_tar[1] = -225;
                    X_tar[2] = 180;

                    R_tar[0]=0;
                    R_tar[1]=-25;
                    R_tar[2]=-90;
                }

                double[] theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);

                double[] delta_theta=new double[6];
                for(int j=0;j<6;j++) {
                    delta_theta[j] = theta_tar[j] - current_Theta[j];
                }
                double[] t=IS.Solve_T12(delta_theta);
                double[] theta_speed_fast=IS.theta_speed;
//                    double[] a_fast=IS.a_fast;
//                    double[] w_fast=IS.w_fast;
                if(i==0){
                    System.out.print("Automaton's Theta:");
                    for(int j=0;j<6;j++) {
                        System.out.print(theta_tar[j]+" ");
                    }
                    System.out.println(" ");
                    Time1.add(t[0]);
                }else {
                    Time2.add(t[0]);
                }
                //double[] omega_bar=IS.Solve_omega_bar(current_Theta,theta_tar);
                //Time.add(IS.Solve_T12(delta_theta));

                //TODO calculate invarients/flow/guard... with theta_tar and omega_bar-finish
                setFast(t,lastTime,i,locIndex,delta_theta,theta_speed_fast);

//                System.out.print("fast:  ");
//                for(int m=0;m<3;m++){
//                    System.out.print(X_tar[m]+"  ");
//                }
//                System.out.println(" ");

            }
            else if(name.contains("forward")){
                if(i==0) {
                    X_tar[0] = 10;
                    X_tar[1] = 100;
                    X_tar[2] = 30;

                    R_tar[0] = 1;
                    R_tar[1] = 2;
                    R_tar[2] = 3;
                }else {
                    X_tar[0] = 10;
                    X_tar[1] = -100;
                    X_tar[2] = 30;

                    R_tar[0] = 1;
                    R_tar[1] = 2;
                    R_tar[2] = 3;
                }

                double V_tar=20;
                double[] theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                for(int j=0;j<3;j++) {
                    delta_X[j] = X_tar[j] - current_X[j];
                }
                double[] t=IS.Solve_T12_forward(delta_X,V_tar);
                if(i==0){
                    for(int m=0;m<3;m++){
                        Time1.add(t[m]);
                    }
                }else {
                    for(int m=0;m<3;m++){
                        Time2.add(t[m]);
                    }
                }
                //Time.add(IS.Solve_T12_forward(delta_X,V_tar));
                double[] a_forward=IS.a_forward;
                double[] v_forward=IS.v_forward;
                setForward(t,lastTime,i,locIndex,theta_tar,a_forward,v_forward);
//                System.out.print("forward:  ");
//                for(int m=0;m<3;m++){
//                    System.out.print(X_tar[m]+"  ");
//                }
//                System.out.println(" ");
            }
            else if(name.contains("doorlike")){
                if(i==0) {
                    X_tar[0] = 10;
                    X_tar[1] = 100;
                    X_tar[2] = 30;

                    R_tar[0] = 1;
                    R_tar[1] = 2;
                    R_tar[2] = 3;
                }else {
                    X_tar[0] = 10;
                    X_tar[1] = -100;
                    X_tar[2] = 30;

                    R_tar[0] = 1;
                    R_tar[1] = 2;
                    R_tar[2] = 3;
                }

                double Height=10;
                double[] tmp_tar1=new double[]{current_X[0],current_X[1],current_X[2]+Height};
                double[] tmp_tar2=new double[]{X_tar[0],X_tar[1],X_tar[2]+Height};

                double[] theta_tar=IS.F_inverse(X_tar,R_tar,current_Theta);
                for(int j=0;j<3;j++) {
                    delta_X[j] = tmp_tar2[j] - tmp_tar1[j];
                }
                double[] t=IS.Solve_T12_doorlike(delta_X,Height);
                if(i==0){
                    for(int m=0;m<3;m++){
                        Time1.add(t[m]);
                    }
                }else {
                    for(int m=0;m<3;m++){
                        Time2.add(t[m]);
                    }
                }
                double[] v_doorlike=IS.v_doorlike;
                setDoorlike(t,lastTime,i,locIndex,theta_tar,v_doorlike, IS.v_max);
//                System.out.print("doorlike:  ");
//                for(int m=0;m<3;m++){
//                    System.out.print(X_tar[m]+"  ");
//                }
//                System.out.println(" ");
            }
        //todo set last move

        }
        return 0;



    }

    private void fillinCommands(Instance ins) {
        commands1=PathMap.get((int)(encodeMap.get((int)(ins.getFeature(0)))[0]));
        commands2=PathMap.get((int)(encodeMap.get((int)(ins.getFeature(0)))[1]));
        path1=new int[commands1.size()];
        path2=new int[commands2.size()];
        for(int i=0;i<commands1.size();i++){
            path1[i]=i+1;
        }
        for(int i=0;i<commands2.size();i++){
            path2[i]=i+1;
        }

    }


    private double[] getFastInfo() {
        double A1_THETA1 = 0;
        double A1_THETA2 = 0;
        double A1_THETA3 = 0;
        double A1_THETA4 = 0;
        double A1_THETA5 = 0;
        double A1_THETA6 = 0;
        //double x=Math.sin(A1_THETA1)*(-25.28*Math.cos(A1_THETA5)*Math.sin(A1_THETA4)-10*Math.cos(A1_THETA6)*Math.sin(A1_THETA4)*Math.sin(A1_THETA5)-10*Math.cos(A1_THETA4)*Math.sin(A1_THETA6))+Math.cos(A1_THETA1)*(29.69+Math.sin(A1_THETA3)*(108.+Math.sin(A1_THETA3)*(-168.98-10*Math.cos(A1_THETA5)*Math.cos(A1_THETA6)+25.28*Math.sin(A1_THETA5))+Math.cos(A1_THETA3)*(20+Math.cos(A1_THETA4)*(-25.28*Math.cos(A1_THETA5)-10*Math.cos(A1_THETA6)*Math.sin(A1_THETA5))+10*Math.sin(A1_THETA4)*Math.sin(A1_THETA6)))+Math.cos(A1_THETA3)*(Math.cos(A1_THETA3)*(168.98+10*Math.cos(A1_THETA5)*Math.cos(A1_THETA6)-25.28*Math.sin(A1_THETA5))+Math.sin(A1_THETA3)*(20+Math.cos(A1_THETA4)*(-25.28*Math.cos(A1_THETA5)-10*Math.cos(A1_THETA6)*Math.sin(A1_THETA5))+10*Math.sin(A1_THETA4)*Math.sin(A1_THETA6))))
        double x = Math.sin(A1_THETA1) * (-25.28 * Math.cos(A1_THETA5) * Math.sin(A1_THETA4) - 10 * Math.cos(A1_THETA6) * Math.sin(A1_THETA4) * Math.sin(A1_THETA5) - 10 * Math.cos(A1_THETA4) * Math.sin(A1_THETA6)) + Math.cos(A1_THETA1) * (29.69 + Math.sin(A1_THETA2) * (108. + Math.sin(A1_THETA3) * (-168.98 - 10 * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) + Math.cos(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10 * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10 * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))) + Math.cos(A1_THETA2) * (Math.cos(A1_THETA3) * (168.98 + 10 * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) - 25.28 * Math.sin(A1_THETA5)) + Math.sin(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10 * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10 * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))));
        double y = Math.cos(A1_THETA1) * (25.28 * Math.cos(A1_THETA5) * Math.sin(A1_THETA4) + 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA4) * Math.sin(A1_THETA5) + 10. * Math.cos(A1_THETA4) * Math.sin(A1_THETA6)) + Math.sin(A1_THETA1) * (29.69 + Math.sin(A1_THETA2) * (108. + Math.sin(A1_THETA3) * (-168.98 - 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) + Math.cos(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10. * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))) + Math.cos(A1_THETA2) * (Math.cos(A1_THETA3) * (168.98 + 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) - 25.28 * Math.sin(A1_THETA5)) + Math.sin(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10. * Math.sin(A1_THETA4) * Math.sin(A1_THETA6))));
        double z = 127. - 20. * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) + 25.28 * Math.cos(A1_THETA4) * Math.cos(A1_THETA5) * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) + 10. * Math.cos(A1_THETA4) * Math.cos(A1_THETA6) * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) * Math.sin(A1_THETA5) + Math.cos(A1_THETA3) * Math.sin(A1_THETA2) * (-168.98 - 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) - 10. * Math.sin(A1_THETA2) * Math.sin(A1_THETA3) * Math.sin(A1_THETA4) * Math.sin(A1_THETA6) + Math.cos(A1_THETA2) * (108. + Math.sin(A1_THETA3) * (-168.98 - 10. * Math.cos(A1_THETA5) * Math.cos(A1_THETA6) + 25.28 * Math.sin(A1_THETA5)) + Math.cos(A1_THETA3) * (20. + Math.cos(A1_THETA4) * (-25.28 * Math.cos(A1_THETA5) - 10. * Math.cos(A1_THETA6) * Math.sin(A1_THETA5)) + 10. * Math.sin(A1_THETA4) * Math.sin(A1_THETA6)));

        return new double[]{x,y,z};
    }

    public void clearAutomata() {
        for(int i = 0; i < matanum ; i++) {
            Automata temp=new Automata();
            temp.parameters = new ArrayList<>();
            temp.locations = new HashMap<>();
            temp.transitions = new ArrayList<>();
            temp.initParameterValues=new HashMap<>();
            for(int j=0;j<6;j++){
                temp.parameters.add(j,"theta"+Integer.toString(j+1));
                temp.parameters.add(j,"omega"+Integer.toString(j+1));
                temp.initParameterValues.put("theta"+Integer.toString(j+1),0.0);
                temp.initParameterValues.put("omega"+Integer.toString(j+1),0.0);
            }
            temp.initParameterValues.put("x"+Integer.toString(1),208.67);
            temp.initParameterValues.put("x"+Integer.toString(2),0.0);
            temp.initParameterValues.put("x"+Integer.toString(3),229.72);
            for(int j=0;j<3;j++){
                temp.parameters.add("x"+Integer.toString(j+1));
                temp.parameters.add("v"+Integer.toString(j+1));
                temp.initParameterValues.put("v"+Integer.toString(j+1),0.0);
                temp.initParameterValues.put("r"+Integer.toString(j+1),0.0);
            }
            temp.parameters.add("t_current");
            temp.initParameterValues.put("t_current",0.0);
            int locNum=0;
            int lastLocNum=-1;
            ArrayList<String> tmp;
            if(i==0) tmp=commands1;
            else tmp=commands2;

            for (int index =0 ; index < tmp.size(); index++) {
                String command = tmp.get(index);

                if (command.contains("fast")) {
                    //set locations
                    locNum++;
                    Location location = new Location(locNum, "fast");
                    temp.locations.put(location.getNo(),location);
                    if(lastLocNum!=-1){
                        Transition transition = new Transition(lastLocNum, locNum);
                        temp.locations.get(lastLocNum).addNeibour(locNum);
                        temp.transitions.add(transition);
                    }
                    lastLocNum=locNum;
//                    for(int k=0;k<3;k++) {
//                        locNum++;
//                        Location location = new Location(locNum, "fast_period"+Integer.toString(k+1));
//                        temp.locations.put(location.getNo(),location);
//                        //set transition
//                        if(lastLocNum!=-1){
//                            Transition transition = new Transition(lastLocNum, locNum);
//                            temp.locations.get(lastLocNum).addNeibour(locNum);
//                            temp.transitions.add(transition);
//                        }
//                        lastLocNum=locNum;
//                    }



                }else if(command.contains("forward")){
                    for(int k=0;k<3;k++) {
                        locNum++;
                        Location location = new Location(locNum, "forward_period"+Integer.toString(k+1));
                        temp.locations.put(location.getNo(),location);
                        if(lastLocNum!=-1){
                            Transition transition = new Transition(lastLocNum, locNum);
                            temp.locations.get(lastLocNum).addNeibour(locNum);
                            temp.transitions.add(transition);
                        }
                        lastLocNum=locNum;
                    }
                }else if(command.contains("doorlike")){
                    for(int k=0;k<3;k++) {
                        locNum++;
                        Location location = new Location(locNum, "doorlike_period"+Integer.toString(k+1));
                        temp.locations.put(location.getNo(),location);
                        if(lastLocNum!=-1){
                            Transition transition = new Transition(lastLocNum, locNum);
                            temp.locations.get(lastLocNum).addNeibour(locNum);
                            temp.transitions.add(transition);
                        }
                        lastLocNum=locNum;
                    }

                }else{
                    //todo other modes
                }
            }
            automatas.add(temp);
        }
        //String tmp="(-24.28*sin(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+cos(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5)))) - (-24.28*sin(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+cos(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5))))))*(-24.28*sin(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+cos(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5)))) - (-24.28*sin(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+cos(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5))))))+(24.28*cos(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+sin(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5))))- (24.28*cos(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+sin(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5)))))-0)*(24.28*cos(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+sin(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5))))-(24.28*cos(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+sin(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5)))))-0)+(127.0+sin(a1_theta2)*(-108.0+(168.98+24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5)))+cos(a1_theta2)*(cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))+sin(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5))) - (127.0+sin(a2_theta2)*(-108.0+(168.98+24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))+cos(a2_theta2)*(cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))+sin(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))))*(127.0+sin(a1_theta2)*(-108.0+(168.98+24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5)))+cos(a1_theta2)*(cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))+sin(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5))) - (127.0+sin(a2_theta2)*(-108.0+(168.98+24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))+cos(a2_theta2)*(cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))+sin(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))))<1";
        //String tmp="(-24.28*sin(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+cos(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5)))) - (-24.28*sin(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+cos(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5))))))*(-24.28*sin(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+cos(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5)))) - (-24.28*sin(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+cos(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5))))))+(24.28*cos(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+sin(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5))))- (24.28*cos(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+sin(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5)))))-207)*(24.28*cos(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+sin(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5))))-(24.28*cos(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+sin(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5)))))-207)+(127.0+sin(a1_theta2)*(-108.0+(168.98+24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5)))+cos(a1_theta2)*(cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))+sin(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5))) - (127.0+sin(a2_theta2)*(-108.0+(168.98+24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))+cos(a2_theta2)*(cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))+sin(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))))*(127.0+sin(a1_theta2)*(-108.0+(168.98+24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5)))+cos(a1_theta2)*(cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))+sin(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5))) - (127.0+sin(a2_theta2)*(-108.0+(168.98+24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))+cos(a2_theta2)*(cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))+sin(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))))<5184";
        //String tmp="(-24.28*sin(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+cos(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5)))) - (-24.28*sin(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+cos(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5))))))*(-24.28*sin(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+cos(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5)))) - (-24.28*sin(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+cos(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5))))))+(24.28*cos(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+sin(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5))))- (24.28*cos(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+sin(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5)))))-414)*(24.28*cos(a1_theta1)*sin(a1_theta4)*sin(a1_theta5)+sin(a1_theta1)*(29.69+cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))*sin(a1_theta2)-20.0*sin(a1_theta2)*sin(a1_theta3)+24.28*cos(a1_theta4)*sin(a1_theta2)*sin(a1_theta3)*sin(a1_theta5)+cos(a1_theta2)*(108.0+(-168.98-24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(20.0-24.28*cos(a1_theta4)*sin(a1_theta5))))-(24.28*cos(a2_theta2)*sin(a2_theta4)*sin(a2_theta5)+sin(a2_theta2)*(29.69+cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))*sin(a2_theta2)-20.0*sin(a2_theta2)*sin(a2_theta3)+24.28*cos(a2_theta4)*sin(a2_theta2)*sin(a2_theta3)*sin(a2_theta5)+cos(a2_theta2)*(108.0+(-168.98-24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(20.0-24.28*cos(a2_theta4)*sin(a2_theta5)))))-414)+(127.0+sin(a1_theta2)*(-108.0+(168.98+24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5)))+cos(a1_theta2)*(cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))+sin(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5))) - (127.0+sin(a2_theta2)*(-108.0+(168.98+24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))+cos(a2_theta2)*(cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))+sin(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))))*(127.0+sin(a1_theta2)*(-108.0+(168.98+24.28*cos(a1_theta5))*sin(a1_theta3)+cos(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5)))+cos(a1_theta2)*(cos(a1_theta3)*(-168.98-24.28*cos(a1_theta5))+sin(a1_theta3)*(-20.0+24.28*cos(a1_theta4)*sin(a1_theta5))) - (127.0+sin(a2_theta2)*(-108.0+(168.98+24.28*cos(a2_theta5))*sin(a2_theta3)+cos(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))+cos(a2_theta2)*(cos(a2_theta3)*(-168.98-24.28*cos(a2_theta5))+sin(a2_theta3)*(-20.0+24.28*cos(a2_theta4)*sin(a2_theta5)))))<10000";
        String tmp="(a1_x1-a2_x1)*(a1_x1-a2_x1)+(a1_x2-a2_x2-101)*(a1_x2-a2_x2-101)+(a1_x3-a2_x3)*(a1_x3-a2_x3)<20000";
        tmp = tmp.replace("pow", "$(Math).pow");
        tmp = tmp.replace("sin", "$(Math).sin");
        tmp = tmp.replace("cos", "$(Math).cos");
        tmp = tmp.replace("tan", "$(Math).tan");
        tmp = tmp.replace("sqrt", "$(Math).sqrt");
        automatas.get(0).forbiddenConstraints=tmp;
        automatas.get(1).forbiddenConstraints=tmp;
    }


    void setFast(double[] T12,double lastTime, int Auto_index,int locIndex,double[] delta_theta,double[] theta_speed_fast){
        //T12[0]=tamx,T12[0]=t2
        double t2=T12[0]+lastTime;
        if(locIndex!=0){
            Transition transition=automatas.get(Auto_index).getTransitionBySourceAndTarget(locIndex-1,locIndex);
            //todo fast mode is not the first mode
        }

        String tmp2="";
        for(int i=0;i<6;i++){
            tmp2=tmp2+"theta"+Integer.toString(i+1)+"'="+Double.toString(theta_speed_fast[i])+"&amp;";
        }
        Location location=automatas.get(Auto_index).locations.get(locIndex+1);
        location.setVariant("t_current<="+t2,automatas.get(Auto_index).parameters);
        location.setFlow(tmp2,automatas.get(Auto_index).parameters);

    }
    void setForward(double[] time,double lastTime, int Auto_index,int locIndex, double[] theta_tar,double[] a_forward,double[] v_forward){
        double t1=time[0]+lastTime;
        double t2=time[1]+lastTime;
        double t3=time[2]+lastTime;
        String tmp="";
        for(int i=0;i<6;i++){
            if(Auto_index==0){
                tmp=tmp+"a1_theta"+Integer.toString(i+1)+"="+Double.toString(theta_tar[i])+" &amp;";
            }
            else{
                tmp=tmp+"a2_theta"+Integer.toString(i+1)+"="+Double.toString(theta_tar[i])+" &amp;";
            }
        }
        int[] path=Auto_index==0?path1:path2;
        Transition transition1=automatas.get(Auto_index).getTransitionBySourceAndTarget(path[locIndex],path[locIndex+1]);
        //todo forward mode is not the first mode
        transition1.setAssignment(tmp,automatas.get(Auto_index).parameters);

        String tmp1="";
        String tmp2="";
        String tmp3="";
        for(int i=0;i<3;i++){
            tmp1=tmp1+"x"+Integer.toString(i+1)+"'=v"+Integer.toString(i+1)+" &amp;";
            tmp1=tmp1+"v"+Integer.toString(i+1)+"'="+Double.toString(a_forward[i])+"&amp;";

            tmp2=tmp2+"x"+Integer.toString(i+1)+"'="+Double.toString(v_forward[i])+"&amp;";

            tmp3=tmp3+"x"+Integer.toString(i+1)+"'=v"+Integer.toString(i+1)+" &amp;";
            tmp3=tmp3+"v"+Integer.toString(i+1)+"'="+Double.toString(a_forward[i]*(-1))+"&amp;";


        }
        Location location=automatas.get(Auto_index).locations.get(locIndex+1);
        location.setVariant("t_current<="+t1,automatas.get(Auto_index).parameters);
        location.setFlow(tmp1,automatas.get(Auto_index).parameters);
        Transition transition=automatas.get(Auto_index).getTransitionBySourceAndTarget(locIndex+1,locIndex+2);
        transition.setGuard("t=="+t1,automatas.get(Auto_index).parameters);

        location=automatas.get(Auto_index).locations.get(locIndex+2);
        location.setVariant("t_current<="+t2,automatas.get(Auto_index).parameters);
        location.setFlow(tmp2,automatas.get(Auto_index).parameters);
        transition=automatas.get(Auto_index).getTransitionBySourceAndTarget(locIndex+2,locIndex+3);
        transition.setGuard("t=="+t2,automatas.get(Auto_index).parameters);

        location=automatas.get(Auto_index).locations.get(locIndex+3);
        location.setVariant("t_current<="+t3,automatas.get(Auto_index).parameters);
        location.setFlow(tmp3,automatas.get(Auto_index).parameters);
    }
    private void setDoorlike(double[] time, double lastTime, int Auto_index, int locIndex, double[] theta_tar, double[] v_doorlike,double v_max) {
        double t1=time[0]+lastTime;
        double t2=time[1]+lastTime;
        double t3=time[2]+lastTime;
        String tmp="";
        for(int i=0;i<6;i++){
            if(Auto_index==0){
                tmp=tmp+"a1_theta"+Integer.toString(i+1)+"="+Double.toString(theta_tar[i])+" &amp;";
            }
            else{
                tmp=tmp+"a2_theta"+Integer.toString(i+1)+"="+Double.toString(theta_tar[i])+" &amp;";
            }
        }
        int[] path=Auto_index==0?path1:path2;
        Transition transition1=automatas.get(Auto_index).getTransitionBySourceAndTarget(path[locIndex],path[locIndex+1]);
        //todo forward mode is not the first mode
        transition1.setAssignment(tmp,automatas.get(Auto_index).parameters);


        String tmp1="x3'="+Double.toString(v_max)+"&amp;";
        String tmp2="";
        String tmp3="x3'="+Double.toString(v_max*(-1))+"&amp;";

        for(int i=0;i<3;i++){
            tmp2=tmp2+"x"+Integer.toString(i+1)+"'="+Double.toString(v_doorlike[i])+"&amp;";
        }
        Location location=automatas.get(Auto_index).locations.get(locIndex+1);
        location.setVariant("t_current<="+t1,automatas.get(Auto_index).parameters);
        location.setFlow(tmp1,automatas.get(Auto_index).parameters);
        Transition transition=automatas.get(Auto_index).getTransitionBySourceAndTarget(locIndex+1,locIndex+2);
        transition.setGuard("t=="+t1,automatas.get(Auto_index).parameters);

        location=automatas.get(Auto_index).locations.get(locIndex+2);
        location.setVariant("t_current<="+t2,automatas.get(Auto_index).parameters);
        location.setFlow(tmp2,automatas.get(Auto_index).parameters);
        transition=automatas.get(Auto_index).getTransitionBySourceAndTarget(locIndex+2,locIndex+3);
        transition.setGuard("t=="+t2,automatas.get(Auto_index).parameters);

        location=automatas.get(Auto_index).locations.get(locIndex+3);
        location.setVariant("t_current<="+t3,automatas.get(Auto_index).parameters);
        location.setFlow(tmp3,automatas.get(Auto_index).parameters);
    }
    public int getstepnum(){return stepnum;}
    public double[] getsingleTime(){return singletimeProfile;}
    public ArrayList<Integer> getVaildRange(Instance pos){
        ArrayList<Integer> vaildRange=new ArrayList<>();
        //Two arms choose paths
        double choice1=encodeMap.get((int)pos.getFeature(0))[0];
        double choice2=encodeMap.get((int)pos.getFeature(0))[1];

        ArrayList<String> com1 = PathMap.get((int) choice1);
        ArrayList<String> com2 = PathMap.get((int) choice2);
        //first two dimensions are used to choose paths
        vaildRange.add(0);vaildRange.add(1);

        int index=2;
        for(int j=0;j<com1.size()-1;j++){
            String s=com1.get(j);
            switch (s){
                case "fast":
                    for(int i=0 ;i<6;i++){
                        vaildRange.add(index+i);
                    }
                    index+=9;
                    break;
                case "forward":
                    for(int i=0 ;i<7;i++){
                        vaildRange.add(index+i);
                    }
                    index+=9;
                    break;
                case "doorlike":
                    for(int i=0 ;i<6;i++){
                        vaildRange.add(index+i);
                    }
                    vaildRange.add(index+7);
                    index+=9;
                    break;
            }
        }
        for(int j=0;j<com2.size()-1;j++){
            String s=com2.get(j);
            switch (s){
                case "fast":
                    for(int i=0 ;i<6;i++){
                        vaildRange.add(index+i);
                    }
                    index+=9;
                    break;
                case "forward":
                    for(int i=0 ;i<7;i++){
                        vaildRange.add(index+i);
                    }
                    index+=9;
                    break;
                case "doorlike":
                    for(int i=0 ;i<6;i++){
                        vaildRange.add(index+i);
                    }
                    vaildRange.add(index+7);
                    index+=9;
                    break;
            }
        }
        return vaildRange;
    }

}
