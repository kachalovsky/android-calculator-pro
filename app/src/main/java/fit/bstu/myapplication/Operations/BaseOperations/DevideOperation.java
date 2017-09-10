package fit.bstu.myapplication.Operations.BaseOperations;

import fit.bstu.myapplication.Operations.Interface.IOperation;

/**
 * Created by andre on 10.09.2017.
 */

public class DevideOperation implements IOperation {
    @Override
    public double executeOperation(Double firtsOperand, Double secondOperand) {
        if (secondOperand == null || firtsOperand == null) return firtsOperand;

        return firtsOperand / secondOperand;
    }
}
