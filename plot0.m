clear all;
close all;
clc

%绘制有控制的场景，从起点到目标点的直线
data1=importdata('E:\Myfile\Experiment\arm-real-life\log1.txt');
x1=data1(:,1);
y1=data1(:,2);
z1=data1(:,3);
data2=importdata('E:\Myfile\Experiment\arm-real-life\log2.txt');
x2=data2(:,1);
y2=data2(:,2);
z2=data2(:,3);
figure( 2 )
plot3(x1,y1,z1,'r',x2,y2,z2,'g','LineWidth',1.5)
title('Scenario 1: with control synthesis','FontSize',20);
legend('Arm1','Arm2');
xlabel('X');
ylabel('Y');
zlabel('Z');