package MPC;

import MPC.tools.Fel_ExpressionProc;
import Racos.Componet.Instance;
import Racos.Method.Continue;
import Racos.ObjectiveFunction.Mission;
//import Racos.ObjectiveFunction.ObjectFunction;
import Racos.ObjectiveFunction.Task;
import Racos.Tools.ValueArc;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Model {
    public String forbiddenConstraints;
    public ArrayList<Automata> automatas;
    public ArrayList<String> commands1;
    public ArrayList<String> commands2;
    File output;
    BufferedWriter bufferedWriter;
    ArrayList<ArrayList<String>>PathMap=new ArrayList<>();
    HashMap<Integer,int[]> encodeMap;
    int choiceSize=39;
    int bound=3;
    int[] info_path;
    double[] ans;

    //public Map<String,Automata> system;
    public Model(String modelFileName, String cfgFileName){
        processModelFile(modelFileName);
        processCFGFile(cfgFileName);
    }
    public Model(ArrayList<String> commands,int matanum){
        commands1=new ArrayList<>();
        commands2=new ArrayList<>();
        automatas=new ArrayList<>();
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
            for (int j = 0; j < commands.size(); j++) {
                String command = commands.get(j);

                if (command.contains("fast")) {
                    //set locations
                    for(int k=0;k<3;k++) {
                        locNum++;
                        Location location = new Location(locNum, "fast_period"+Integer.toString(k+1));
                        temp.locations.put(location.getNo(),location);
                        //set transition
                        if(lastLocNum!=-1){
                            Transition transition = new Transition(lastLocNum, locNum);
                            temp.locations.get(lastLocNum).addNeibour(locNum);
                            temp.transitions.add(transition);
                        }
                        lastLocNum=locNum;
                    }


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

    void processModelFile(String modelFileName) {
        File modelFile = new File(modelFileName);
        BufferedReader reader = null;
        automatas=new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(modelFile));
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
                Automata temp = new Automata();
                if (tempLine.indexOf("<component") != -1){
                    String[] string_temp = tempLine.split("\"");
                    temp.name=string_temp[1];
                    temp.locations = new HashMap<>();
                    temp.transitions = new ArrayList<>();
                    temp.parameters = new ArrayList<>();
                    tempLine = reader.readLine();
                    while (tempLine.indexOf("</component>") == -1){
                        if (tempLine.indexOf("<param") != -1) { // paramater definition
                            while (true) {
                                String[] strings = tempLine.split("\"");
                                if (strings[3].equals("real"))
                                    temp.parameters.add(strings[1]);
                                tempLine = reader.readLine();
                                if (tempLine.indexOf("<para") == -1) {
                                    temp.parametersSort();
                                    break;
                                }
                            }
                        }
                        if (tempLine.indexOf("<location") != -1) { // location definition
                            String[] strings = tempLine.split("\"");
                            //ID stores in strings[1]
                            Location location = new Location(Integer.parseInt(strings[1]), strings[3]);
                            tempLine = reader.readLine();
                            while (tempLine.indexOf("</location>") == -1) {//the end of this location
                                int beginIndex, endIndex;
                                if (tempLine.indexOf("<invar") != -1) {
                                    while (tempLine.indexOf("</invar") == -1) {
                                        if (tempLine.indexOf("<invar") != -1) {
                                            beginIndex = tempLine.indexOf("<invar") + 11;
                                            tempLine = tempLine.substring(beginIndex).trim();
                                        }
                                        location.setVariant(tempLine,temp.parameters);
                                        tempLine = reader.readLine();
                                    }
                                    if (tempLine.indexOf("<invar") != -1) {
                                        beginIndex = tempLine.indexOf("<invar") + 11;
                                        endIndex = tempLine.indexOf("</invar");
                                        tempLine = tempLine.substring(beginIndex, endIndex).trim();
                                    } else {
                                        endIndex = tempLine.indexOf("</invar");
                                        tempLine = tempLine.substring(0, endIndex).trim();
                                    }
                                    location.setVariant(tempLine, temp.parameters);
                                }
                                if (tempLine.indexOf("<flow>") != -1) {
                                    while (tempLine.indexOf("</flow>") == -1) {
                                        if (tempLine.indexOf("<flow>") != -1) {
                                            beginIndex = tempLine.indexOf("<flow>") + 6;
                                            tempLine = tempLine.substring(beginIndex).trim();
                                        }
                                        location.setFlow(tempLine, temp.parameters);
                                        tempLine = reader.readLine();
                                    }
                                    if (tempLine.indexOf("<flow>") != -1) {
                                        beginIndex = tempLine.indexOf("<flow>") + 6;
                                        endIndex = tempLine.indexOf("</flow>");
                                        tempLine = tempLine.substring(beginIndex, endIndex).trim();
                                    } else {
                                        endIndex = tempLine.indexOf("</flow>");
                                        tempLine = tempLine.substring(0, endIndex).trim();
                                    }
                                    location.setFlow(tempLine, temp.parameters);
                                }
                                tempLine = reader.readLine();
                            }
                            temp.locations.put(location.getNo(), location);
                            tempLine = reader.readLine();
                        }
                        if (tempLine.indexOf("<transition") != -1) { // transition definition
                            String[] strings = tempLine.split("\"");
                            int source = Integer.parseInt(strings[1]);
                            int target = Integer.parseInt(strings[3]);
                            Transition transition = new Transition(source, target);
                            temp.locations.get(source).addNeibour(target);
                            tempLine = reader.readLine(); // guard
                            while (tempLine.indexOf("</transi") == -1) {
                                int beginIndex, endIndex;
                                if (tempLine.indexOf("<guard>") != -1) {
                                    beginIndex = tempLine.indexOf("<guard>") + 7;
                                    endIndex = tempLine.indexOf("</guard>");
                                    String guard = tempLine.substring(beginIndex, endIndex).trim();
                                    transition.setGuard(guard, temp.parameters);
                                }
                                if (tempLine.indexOf("<assignment>") != -1) {
                                    beginIndex = tempLine.indexOf("<assignment>") + 12;
                                    endIndex = tempLine.indexOf("</assignment>");
                                    String assignment = tempLine.substring(beginIndex, endIndex).trim();
                                    transition.setAssignment(assignment, temp.parameters);
                                }
                                tempLine = reader.readLine();
                            }
                            temp.transitions.add(transition);
                            tempLine = reader.readLine();
                        }
                        if (tempLine.indexOf("<bind") != -1){
                            String[] strings = tempLine.split("\"");
                            //system=new HashMap<>();
                            for(int i=0;i<automatas.size();i++){
                                if(automatas.get(i).name.equals(strings[1])){
                                    automatas.get(i).labelasName=strings[3];
                                    //system.put(strings[3],automatas.get(i));
                                    break;
                                }
                            }
                            tempLine = reader.readLine();
                            if(tempLine.indexOf("</bind>")!=-1){
                                tempLine = reader.readLine();
                            }
                        }
                    }
                    if(tempLine.indexOf("</component>") != -1 &&temp.parameters.size()!=0){
                        automatas.add(temp);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + '\n' + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception" + '\n' + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("IO Exception" + '\n' + e.getMessage());
                }
            }
        }
    }
    void processCFGFile(String cfgFileName) {
        File cfgFile = new File(cfgFileName);
        BufferedReader reader = null;
        for(int i=0;i<automatas.size();i++){
            automatas.get(i).initParameterValues=new HashMap<>();
        }
        try {
            reader = new BufferedReader(new FileReader(cfgFile));
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
                if (tempLine.charAt(0) == '#')
                    continue;
                if (tempLine.startsWith("initially")) {
                    String[] strings = tempLine.split("\"");
                    setInitParameterValues(strings[1]);
                }
                if (tempLine.startsWith("forbidden")) {
                    String[] strings = tempLine.split("\"");
                    strings[1] = strings[1].replace("pow", "$(Math).pow");
                    strings[1] = strings[1].replace("sin", "$(Math).sin");
                    strings[1] = strings[1].replace("cos", "$(Math).cos");
                    strings[1] = strings[1].replace("tan", "$(Math).tan");
                    strings[1] = strings[1].replace("sqrt", "$(Math).sqrt");
                    forbiddenConstraints = strings[1];
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found" + '\n' + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception" + '\n' + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("IO Exception" + '\n' + e.getMessage());
                }
            }
        }
    }

    Automata findAutomata(String labelname){
        for(int i=0;i<automatas.size();i++){
            if(automatas.get(i).labelasName.equals(labelname))
                return automatas.get(i);
        }
        return null;
    }

    void setInitParameterValues(String initValues){
        String[] strings = initValues.split("&");
        for (int i = 0; i < strings.length; ++i) {
            String[] temp = strings[i].split("==");
            if(temp[0].indexOf('.')!=-1){
                String[] assign_object=temp[0].trim().split("\\.");
                Automata A=findAutomata(assign_object[0]);
                if (temp[1].indexOf('[') != -1){
                    int firstIndex = temp[1].indexOf("[");
                    int lastIndex = temp[1].indexOf("]");
                    String[] bounds = temp[1].substring(firstIndex + 1, lastIndex).trim().split(",");
                    double lowerbound = Double.parseDouble(bounds[0].trim());
                    double upperbound = Double.parseDouble(bounds[1].trim());
                    if (A.rangeParameters == null) A.rangeParameters = new ArrayList<>();
                    A.rangeParameters.add(new RangeParameter(assign_object[1], lowerbound, upperbound));
                }
                else A.initParameterValues.put(assign_object[1], Double.parseDouble(temp[1].trim()));

            } else if(temp[0].trim().indexOf("loc(")!=-1){
                int beginIndex=temp[0].indexOf("loc(");
                int endIndex=temp[0].indexOf(")");
                String label_name=temp[0].substring(beginIndex+4,endIndex).trim();
                Automata A=findAutomata(label_name);
                A.initLocName=temp[1].trim();
                for (Map.Entry<Integer, Location> entry : A.locations.entrySet()) {
                    //System.out.println(allParametersValues.size());
                    if (entry.getValue().name.equals(A.initLocName)) {
                        A.initLoc = entry.getKey();
                        break;
                    }
                }
            }
        }
    }

    void checkAutomata(){
        for(int i=0;i<automatas.size();i++){
            automatas.get(i).bufferedWriter=bufferedWriter;
            automatas.get(i).checkAutomata();
        }
    }
    boolean runRacos() {
        int samplesize = 8;       // parameter: the number of samples in each iteration
        int iteration = 100;       // parameter: the number of iterations for batch racos
        int budget = 2000;         // parameter: the budget of sampling for sequential racos
        int positivenum = 3;       // parameter: the number of positive instances in each iteration
        double probability = 0.95; // parameter: the probability of sampling from the model
        int uncertainbit = 1;      // parameter: the number of sampled dimensions
        Instance ins = null;
        int repeat = 1;
        int[] path=new int[]{1,2,3,4,5,6};
        Task t = new Mission(automatas);
        ArrayList<Instance> result = new ArrayList<>();
        ArrayList<Instance> feasibleResult = new ArrayList<>();
        double feasibleResultAllTime = 0;
        boolean pruning = true;
        for (int i = 0; i < repeat; i++) {
            double currentT = System.currentTimeMillis();
            Continue con=new Continue(t, automatas);
            con.setMaxIteration(iteration);
            con.setSampleSize(samplesize);      // parameter: the number of samples in each iteration
            con.setBudget(budget);              // parameter: the budget of sampling
            con.setPositiveNum(positivenum);    // parameter: the number of positive instances in each iteration
            con.setRandProbability(probability);// parameter: the probability of sampling from the model
            con.setUncertainBits(uncertainbit); // parameter: the number of samplable dimensions
            ValueArc valueArc = con.run();                          // call sequential Racos              // call Racos
//            ValueArc valueArc = con.RRT();                          // call sequential Racos              // call Racos
//            ValueArc valueArc = con.monte();                          // call sequential Racos              // call Racos
//            ValueArc valueArc = con.run2();
            double currentT2 = System.currentTimeMillis();
            ins = con.getOptimal();
            System.out.println("\n【RESULT】");
            System.out.print("ins:");
            for(int k=0;k<10;k++){
                System.out.print(ins.getFeature(k)+" ");
            }
            System.out.println(" ");
            System.out.print("best function value:");
            System.out.println(ins.getValue() + "     ");
            result.add(ins);

            commands1.clear();
            info_path=valueArc.pathinfo;
            ans=new double[]{ins.getValue()};

            commands1=PathMap.get((int)(encodeMap.get((int)(ins.getFeature(0)))[0]));
            commands2=PathMap.get((int)(encodeMap.get((int)(ins.getFeature(0)))[1]));

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

            System.out.println("\nSimulation step:"+t.getstepnum());

        }
        System.out.println("\n【TOTAL TIME COST】");
        System.out.println("computing by flow costs:"+t.getinsTime()[0]);
        System.out.println("(fast mode)calculating X costs:"+t.getinsTime()[1]);
        System.out.println("checking forbidden costs:"+t.getinsTime()[2]);
        return pruning;

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

    public static void main(String[] args) {
        ArrayList<String> commands=new ArrayList<>();
        commands.add(0,"fast");
        commands.add(1,"doorlike");
        commands.add(2,"fast");
        int time=1;
        File result=new File("result1.txt");
        try{
            BufferedWriter buffer=new BufferedWriter(new FileWriter(result));
            while(time-->0){

                Model model=new Model(commands,2);
                double currentTime = System.currentTimeMillis();
                model.createMap();
                model.runRacos();
                double endTime = System.currentTimeMillis();
                double timecost=(endTime - currentTime) / 1000;
                System.out.println("Total Time cost :" + timecost + " seconds\n");

                String tmp=Double.toString(timecost)+" ";
                tmp+=Double.toString(model.ans[0])+" ";
                tmp+=Integer.toString(model.info_path[0])+" ";
                tmp+=Integer.toString(model.info_path[1])+" ";
                tmp+=Integer.toString(model.info_path[2])+" ";

                tmp+="\n";

                try {
                    buffer.write(tmp);
                } catch (IOException e) {
                    System.out.println("write to file error!");
                }

            }
            buffer.close();
        }catch (IOException e) {
            System.out.println("Open result.txt fail!");
        }

    }
}
