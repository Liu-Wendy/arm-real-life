clc
clear
close all
grid minor
hold on
data1=importdata('E:\Myfile\Experiment\arm-real-life\log1.txt');
x1=data1(:,1);
y1=data1(:,2);
z1=data1(:,3);
data2=importdata('E:\Myfile\Experiment\arm-real-life\log2.txt');
x2=data2(:,1);
y2=data2(:,2);
z2=data2(:,3);
figure( 2 )

%% 生成图片
for i = 1:length(data1)
    view(-9.611107304202044,49.609345794392482);    
    plot3(x1(1:i),y1(1:i),z1(1:i),'b',x2(1:i),y2(1:i),z2(1:i),'r','LineWidth',1.5)
    axis([0 300 -240 400 140 240]); 
    legend('Arm2','Arm1');
    xlabel('X');
    ylabel('Y');
    zlabel('Z');
    hold on
    drawnow
    print(['E:\Myfile\Experiment\arm-real-life\fig\picture',num2str(i)],'-dpng');   %保存图片
end
%% 图片播放
GifName = 'collision-free-0531.gif';  %动图名字
delay = 0.1;              %图片之间延迟
for i = 1:length(data1)     
    A = imread(['E:\Myfile\Experiment\arm-real-life\fig\picture',num2str(i),'.png']); %读取图片
    [X, map] = rgb2ind(A, 256);            %彩色图像转化成索引图像
    if i == 1
        imwrite(X, map, GifName, 'gif', 'LoopCount', inf, 'DelayTime', delay)
    else
        imwrite(X, map, GifName, 'gif', 'WriteMode', 'append', 'DelayTime', delay)
        end
end
