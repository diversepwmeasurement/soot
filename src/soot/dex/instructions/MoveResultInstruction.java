/* Soot - a Java Optimization Framework
 * Copyright (C) 2012 Michael Markert, Frank Hartmann
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.dex.instructions;

import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.SingleRegisterInstruction;

import soot.Local;
import soot.dex.DexBody;
import soot.dex.DvkTyper;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.internal.JAssignStmt;
import soot.tagkit.Tag;

public class MoveResultInstruction extends DexlibAbstractInstruction {
    private Local local;
    private Expr expr;
    private Tag tag;

    public MoveResultInstruction (Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    public void jimplify (DexBody body) {
//        if (local != null && expr != null)
//            throw new RuntimeException("Both local and expr are set to move.");

        int dest = ((SingleRegisterInstruction)instruction).getRegisterA();
        AssignStmt assign;

//        if (local != null)
//            assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), local);
//        else if (expr != null)
//            assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), expr);
//        else
//            throw new RuntimeException("Neither local and expr are set to move.");
        assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), body.getStoreResultLocal());
        defineBlock(assign);
        tagWithLineNumber(assign);
        if (tag != null)
            assign.addTag(tag);
        body.add(assign);
        if (DvkTyper.ENABLE_DVKTYPER) {
          int op = (int)instruction.opcode.value;
          JAssignStmt jassign = (JAssignStmt)assign;
          body.captureAssign(jassign, op);
        }
    }

    public void setLocalToMove(Local l) {
        local = l;
    }
    public void setExpr(Expr e) {
        expr = e;
    }
    public void setTag(Tag t) {
        tag = t;
    }

    @Override
    boolean overridesRegister(int register) {
        SingleRegisterInstruction i = (SingleRegisterInstruction) instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }
}
