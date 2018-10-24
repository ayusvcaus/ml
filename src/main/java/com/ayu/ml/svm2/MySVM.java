package com.ayu.ml.svm2;

public class MySVM {

    public static void main(String[] args) throws Exception {  
        // TODO Auto-generated method stub  
        String[] arg = { "data/svm/svm_train_data.txt", // 存放SVM训练模型用的数据的路径  
                "data/svm/model_r.txt" }; // 存放SVM通过训练数据训/ //练出来的模型的路径  
  
        String[] parg = { "data/svm/svm_test_data.txt", // 这个是存放测试数据  
                "data/svm/model_r.txt", // 调用的是训练以后的模型  
                "data/svm/out_r.txt" }; // 生成的结果的文件的路径  
        System.out.println("........SVM运行开始..........");  
        // 创建一个训练对象  
        svm_train t = new svm_train();  
        // 创建一个预测或者分类的对象  
        svm_predict p = new svm_predict();  
        t.main(arg); // 调用  
        p.main(parg); // 调用  
    }  
}