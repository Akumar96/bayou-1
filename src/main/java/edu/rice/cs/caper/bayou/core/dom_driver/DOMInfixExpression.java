/*
Copyright 2017 Rice University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package edu.rice.cs.caper.bayou.core.dom_driver;

import edu.rice.cs.caper.bayou.core.dsl.DAPICall;
import edu.rice.cs.caper.bayou.core.dsl.DSubTree;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;

public class DOMInfixExpression implements Handler {

    final InfixExpression expr;
    final Visitor visitor;

    public DOMInfixExpression(InfixExpression expr, Visitor visitor) {
        this.expr = expr;
        this.visitor = visitor;
    }

    @Override
    public DSubTree handle() {
        DSubTree tree = new DSubTree();

        DSubTree Tleft = new DOMExpression(expr.getLeftOperand(), visitor).handle();
        DSubTree Tright = new DOMExpression(expr.getRightOperand(), visitor).handle();

        if (expr.getOperator() == InfixExpression.Operator.MINUS
                && (expr.getRightOperand() instanceof NumberLiteral)
                && ((NumberLiteral) expr.getRightOperand()).getToken().equals("1")
                && Tleft.getNodes().size() == 1
                && Tleft.getNodes().get(0) instanceof DAPICall) {
            DAPICall call = (DAPICall) Tleft.getNodes().get(0);
            call.setMinusOnePredicate();
        }


        tree.addNodes(Tleft.getNodes());
        tree.addNodes(Tright.getNodes());

        return tree;
    }
}
