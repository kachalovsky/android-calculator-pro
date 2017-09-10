package fit.bstu.myapplication.Operations;

import java.util.HashMap;
import java.util.Map;

import fit.bstu.myapplication.Operations.BaseOperations.*;
import fit.bstu.myapplication.Operations.Interface.IOperation;

/**
 * Created by andre on 10.09.2017.
 */

public class OperationsManager {
    private Map<Operations, IOperation> operationImplements =  new HashMap<Operations, IOperation>(){
        {
            put(Operations.PLUS, new PlusOperation());
            put(Operations.MINUS, new MinusOperation());
            put(Operations.SQRT, new SqrtOperation());
            put(Operations.EQUAL, new EqualOperation());
            put(Operations.MULT, new MultOperation());
            put(Operations.DEVIDE, new DevideOperation());
            put(Operations.POW, new PowOperation());
            put(Operations.MOD, new ModOperation());
            put(Operations.SIN, new SinOperation());
            put(Operations.COS, new CosOperation());
            put(Operations.PLUS_MINUS, new PlusMinusOperation());
        }
    };

    public double executeOperation(Operations operation, Double firtsParam, Double secondParam) {
        IOperation operationInstance = operationImplements.get(operation);
        return operationInstance.executeOperation(firtsParam, secondParam);
    }
}