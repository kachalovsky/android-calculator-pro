package fit.bstu.myapplication;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import fit.bstu.myapplication.Operations.*;
import fit.bstu.myapplication.Operations.Interface.IOperation;

import static java.lang.System.in;

public class MainActivity extends AppCompatActivity {
    protected boolean willForceResultView = false;
    protected boolean valueWasChanged = false;
    protected Operations currentOperation = null;
    protected String showingResult = "0";
    protected Double result = 0d;
    protected Double temporaryValue = null;
    protected OperationsManager operationsManager = new OperationsManager();

    protected enum OperationTypes {
        UNARY,
        BINARY
    }

    protected enum SpecialTypes {
        DEL,
        DOT,
        CE,
        PI
    }

    protected Map<Operations, OperationTypes> operationTypes  = new HashMap<Operations, OperationTypes>() {
        {
            put(Operations.MULT, OperationTypes.BINARY);
            put(Operations.DEVIDE, OperationTypes.BINARY);
            put(Operations.POW, OperationTypes.BINARY);
            put(Operations.MINUS, OperationTypes.BINARY);
            put(Operations.PLUS, OperationTypes.BINARY);
            put(Operations.SQRT, OperationTypes.UNARY);
            put(Operations.EQUAL, OperationTypes.UNARY);
            put(Operations.SIN, OperationTypes.UNARY);
            put(Operations.COS, OperationTypes.UNARY);
            put(Operations.MOD, OperationTypes.UNARY);
            put(Operations.PLUS_MINUS, OperationTypes.UNARY);
        }
    };

    protected Map<Integer, Operations> operationVisuals = new HashMap<Integer, Operations>(){
        {
            put(R.id.plus, Operations.PLUS);
            put(R.id.min, Operations.MINUS);
            put(R.id.sqrt, Operations.SQRT);
            put(R.id.eq, Operations.EQUAL);
            put(R.id.mul, Operations.MULT);
            put(R.id.dev, Operations.DEVIDE);
            put(R.id.pow2, Operations.POW);
            put(R.id.sin, Operations.SIN);
            put(R.id.cos, Operations.COS);
            put(R.id.mod, Operations.MOD);
            put(R.id.plus_min, Operations.PLUS_MINUS);
        }
    };

    protected Map<Integer, SpecialTypes> specialKeysVisuals = new HashMap<Integer, SpecialTypes>(){
        {
            put(R.id.back, SpecialTypes.DEL);
            put(R.id.ce, SpecialTypes.CE);
            put(R.id.dot, SpecialTypes.DOT);
            put(R.id.pi, SpecialTypes.PI);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListenersForNumKeys();
        setListenersForOperationKeys();
        setListenersForSpecialKeys();
    }

    protected void setListenersForOperationKeys() {
        for (int buttonId: operationVisuals.keySet()) {
            final Button operationButton = (Button) findViewById(buttonId);
            operationButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (!valueWasChanged) return;
                    if (currentOperation != null) result = executeOperation(currentOperation);
                    prepareDataAfterOperation(currentOperation);
                    currentOperation = operationVisuals.get(v.getId());
                    valueWasChanged = false;
                    if (operationTypes.get(currentOperation) == OperationTypes.UNARY) {
                        result = executeOperation(currentOperation);
                        prepareDataAfterOperation(currentOperation);
                        currentOperation = null;
                        valueWasChanged = true;
                    }
                    updateResultView();
                }
            });
        }
    }

    protected void setListenersForSpecialKeys() {
        for (int buttonId: specialKeysVisuals.keySet()) {
            final Button specialButton = (Button) findViewById(buttonId);
            specialButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final SpecialTypes currentSpecialCommand = specialKeysVisuals.get(v.getId());
                    switch (currentSpecialCommand) {
                        case DEL:
                            delKeyWasPressed();
                            break;
                        case DOT:
                            dotKeyWasPressed();
                            return;
                        case CE:
                            result = 0d;
                            temporaryValue = 0d;
                            currentOperation = null;
                            break;
                        case PI:
                            piKeyWasPressed();
                            break;
                    }

                    updateResultView();
                }
            });
        }
    }

    protected void prepareDataAfterOperation(Operations operation) {
        temporaryValue = result;
        willForceResultView = true;
    }

    protected double executeOperation(Operations operation) {
        return operationsManager.executeOperation(operation, temporaryValue , result);
    }

    protected int getIdByParam(String paramValue, String paramKey) {
        return this.getResources().getIdentifier(paramValue, paramKey, this.getPackageName());
    }

    protected void setListenersForNumKeys() {
        for (int i = 0; i < 10; i++ ) {
            int id = getIdByParam("num_" + i, "id");
            final Button numBtn = (Button) findViewById(id);
            numBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Button b = (Button) v;
                    if (willForceResultView) {showingResult = "";result = 0d; willForceResultView = false;}
                    numKeyWasPressed(Integer.parseInt((String)b.getText()));
                    updateResultView();
                }
            });
        }
    }

    protected void piKeyWasPressed() {
        valueWasChanged = true;
        result = Math.PI;
        updateResultView();
    }

    protected void numKeyWasPressed(int num) {
        valueWasChanged = true;
        result = Double.parseDouble(showingResult + num);
    }

    protected void delKeyWasPressed() {
        if (showingResult.length() <= 1) showingResult = "0";
        else showingResult = showingResult.substring(0, showingResult.length() - 1);
        result =  Double.parseDouble(showingResult);
        updateResultView();
    }

    protected void dotKeyWasPressed() {
        if(!doubleIsInteger(result)) return;
        valueWasChanged = false;
        showingResult = showingResult + ".";
        showResult(showingResult);
    }

    protected void updateResultView() {
        showingResult = decorateDouble(result);
        showResult(showingResult);
    }

    protected void showResult(String showingResult) {
        TextView lblResult = (TextView)findViewById(R.id.lbl_result);
        lblResult.setText(showingResult);
    }

    protected String decorateDouble (double num) {
        return (doubleIsInteger(num)) ? String.valueOf((int)num) : String.valueOf(num);
    }

    protected boolean doubleIsInteger(double num) {
        return ((num == Math.floor(num)) && !Double.isInfinite(num));
    }

}
