package com.wanj.myapplication_20170805;
/*written by wanj
* */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.Arrays;

import bsh.EvalError;
import bsh.Interpreter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView editText;
    GridLayout gridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (TextView) findViewById(R.id.number);
        editText.setGravity(Gravity.RIGHT | Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        String str = editText.getText().toString();

        if (btn.getText().equals("清除")) {
            editText.setText("0");
        } else if (btn.getText().equals("后退")) {
            if (str.length() > 0)
                editText.setText(str.substring(0, str.length() - 1));
        } else if (btn.getText().equals("=")) {
            str = str.replaceAll("×", "*");
            str = str.replaceAll("÷", "/");
            editText.setText(getRs(str));
        } else {
            if (str.equals("0") || str.equals("算数公式错误"))
                editText.setText(btn.getText());
            else editText.setText(str + btn.getText());
        }
    }

    private String getRs(String exp) {
        Interpreter bsh = new Interpreter();
        Number result = null;
        try {
            exp = filterExp(exp);
            result = (Number) bsh.eval(exp);
        } catch (EvalError e) {
            e.printStackTrace();
            return "算数公式错误";
        }
        exp = result.doubleValue() + "";
        if (exp.endsWith(".0"))
            exp = exp.substring(0, exp.indexOf(".0"));
        return exp;
    }

    private String filterExp(String exp) {
        String num[] = exp.split("");
        String temp = null;
        int begin = 0, end = 0;
        for (int i = 1; i < num.length; i++) {
            temp = num[i];
            if (temp.matches("[+-/()*]")) {
                if (temp.equals(".")) continue;
                end = i - 1;
                temp = exp.substring(begin, end);
                if (temp.trim().length() > 0 && !temp.contains("."))
                    num[i - 1] = num[i - 1] + ".0";
                begin = end + 1;
            }
        }
        return Arrays.toString(num).replaceAll("[\\[\\], ]", "");
    }

    private boolean isEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }

}
