package com.ayu.ml.svm2;

public class MySVM {

    public static void main(String[] args) throws Exception {  
        // TODO Auto-generated method stub  
        String[] arg = { "data/svm/svm_train_data.txt", // ���SVMѵ��ģ���õ����ݵ�·��  
                "data/svm/model_r.txt" }; // ���SVMͨ��ѵ������ѵ/ //��������ģ�͵�·��  
  
        String[] parg = { "data/svm/svm_test_data.txt", // ����Ǵ�Ų�������  
                "data/svm/model_r.txt", // ���õ���ѵ���Ժ��ģ��  
                "data/svm/out_r.txt" }; // ���ɵĽ�����ļ���·��  
        System.out.println("........SVM���п�ʼ..........");  
        // ����һ��ѵ������  
        svm_train t = new svm_train();  
        // ����һ��Ԥ����߷���Ķ���  
        svm_predict p = new svm_predict();  
        t.main(arg); // ����  
        p.main(parg); // ����  
    }  
}