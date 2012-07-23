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

import org.jf.dexlib.StringIdItem;
import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.SingleRegisterInstruction;
import org.jf.dexlib.Code.Format.Instruction21c;
import org.jf.dexlib.Code.Format.Instruction31c;

import soot.dex.DexBody;
import soot.dex.DvkTyper;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;
import soot.jimple.internal.JAssignStmt;

public class ConstStringInstruction extends DexlibAbstractInstruction {

    public ConstStringInstruction (Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    public void jimplify (DexBody body) {
        int dest = ((SingleRegisterInstruction) instruction).getRegisterA();
        String s;
        if (instruction instanceof Instruction21c) {
            Instruction21c i = (Instruction21c)instruction;
            s = ((StringIdItem)(i.getReferencedItem())).getStringValue();
        } else if (instruction instanceof Instruction31c) {
            Instruction31c i = (Instruction31c)instruction;
            s = ((StringIdItem)(i.getReferencedItem())).getStringValue();
        } else
            throw new IllegalArgumentException("Expected Instruction21c or Instruction31c but got neither.");
        StringConstant sc = StringConstant.v(s);
        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), sc);
        defineBlock(assign);
        tagWithLineNumber(assign);
        body.add(assign);
        if (DvkTyper.ENABLE_DVKTYPER) {
          int op = (int)instruction.opcode.value;
          body.captureAssign((JAssignStmt)assign, op);
        }
    }

    @Override
    boolean overridesRegister(int register) {
        SingleRegisterInstruction i = (SingleRegisterInstruction) instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }
}
